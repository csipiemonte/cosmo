/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.fileshare.exceptions.FileShareBadConfigurationException;
import it.csi.cosmo.common.fileshare.exceptions.FileShareIOException;
import it.csi.cosmo.common.fileshare.exceptions.FileShareRetrievalException;
import it.csi.cosmo.common.fileshare.model.CompleteUploadSessionRequest;
import it.csi.cosmo.common.fileshare.model.CompleteUploadSessionResponse;
import it.csi.cosmo.common.fileshare.model.CreateUploadSessionRequest;
import it.csi.cosmo.common.fileshare.model.CreateUploadSessionResponse;
import it.csi.cosmo.common.fileshare.model.FileUploadResult;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.fileshare.model.RetrievedFile;
import it.csi.cosmo.common.fileshare.model.RetrievedUploadSession;
import it.csi.cosmo.common.fileshare.model.RetrievedUploadSessionPart;
import it.csi.cosmo.common.fileshare.model.UploadHashFileContent;
import it.csi.cosmo.common.fileshare.model.UploadSessionMetadata;
import it.csi.cosmo.common.fileshare.model.UploadSessionPartMetadata;
import it.csi.cosmo.common.fileshare.model.UploadedFileMetadata;
import it.csi.cosmo.common.util.FilesUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.TemporaryFolder;
import it.csi.cosmo.common.util.ValidationUtils;

/**
 *
 */

public class DefaultFileShareManagerImpl implements Closeable, IFileShareManager {

  private Logger logger;


  private static final int MAX_DAYS_TO_KEEP_CONTENT = 7;


  private static final int MAX_DAYS_TO_KEEP_METADATA = 15;

  //@formatter:off
  /*
   * Algoritmo proposto:
   * in input uno stream e filename
   *
   * 1 - viene generata una cartella temporanea sotto TEMP_PATH, automaticamente eliminata a fine metodo
   * di seguito nominata TEMP_UPLOAD_FOLDER
   *
   * 2 - lo stream viene scritto su file TEMP_UPLOAD_FOLDER/{contentUUID} e nel frattempo viene calcolato l'hash del contenuto.
   * viene inoltre generato un uuid per il content e uno per il file
   * sono ora noti i dati: fileUUID, contentUUID, filename, contentHash
   *
   * 3 - si calcola hash dell'upload combinando contentHash e codice fiscale dell'utente
   * (piu' eventuali altri dati di contesto)
   * sono ora noti i dati: fileUUID, contentUUID, filename, contentHash, uploadHash
   *
   * 4 - si verifica la presenza di contenuto duplicato verificando se esiste il file UPLOAD_HASH_PATH/uploadHash
   * avente contenuto duplicatedContentUUID
   * per cui esista almeno un file "pointer"-* nella cartella CONTENT_POINTERS_PATH/duplicatedContentUUID
   *
   * 5a - se non esiste duplicazione di contenuto si va al punto 20
   *
   * 5b - se esiste contenuto duplicato
   * si rimpiazza la variabile {contentUUID} con il valore di {duplicatedContentUUID} individuato
   *
   * 6 - si scrive il contenuto effettivo (deduplicato) nel seguente ordine:
   * in CONTENT_POINTERS_PATH/contentUUID si scrive il file "pointer"-fileUUID
   * in META_PATH si scrive il file fileUUID con tutti i metadati
   * si va al punto 90
   *
   * 20 - si scrive il contenuto effettivo nel seguente ordine:
   * in CONTENT_POINTERS_PATH/contentUUID si scrive il file "pointer"-fileUUID
   * in UPLOAD_HASH_PATH si scrive il file uploadHash con contenuto contentUUID
   * si SPOSTA il contenuto da TEMP_UPLOAD_FOLDER/contentUUID a CONTENT_PATH/contentUUID
   * in META_PATH si scrive il file fileUUID con tutti i metadati
   *
   * 90 - si cancella la cartella temporanea TEMP_UPLOAD_FOLDER
   *
   * 91 - si ritorna in output il valore di fileUUID
   *
   */
  //@formatter:on

  private String rootPath;

  private ScheduledExecutorService scheduledThreadPool;

  private String loggerPrefix;

  private DefaultFileShareManagerImpl(Builder builder) {
    this.loggerPrefix = builder != null ? builder.loggingPrefix : Constants.PRODUCT;
    logger = Logger.getLogger(this.loggerPrefix + ".fileshare.FileShareManager");
    this.rootPath = builder != null ? builder.rootPath : "/tmp";
    scheduledThreadPool = Executors.newScheduledThreadPool(1);
  }

  @Override
  public void close() throws IOException {
    if (scheduledThreadPool != null) {
      logger.info("shutting down scheduled thread pool");
      scheduledThreadPool.shutdownNow();
    }
  }

  @Override
  public void cleanup() {
    try {
      doCleanup();
    } catch (Exception e) {
      throw new FileShareIOException("error cleaning folder: " + e.getMessage(), e);
    }
  }

  private synchronized void doCleanup() throws IOException {
    logger.info("executing share folder cleanup");
    LocalDate today = LocalDate.now();

    Path root = getRoot();
    try (Stream<Path> stream = Files.list(root)) {
      stream.filter(Files::isDirectory)
      .filter(dir -> dir.getFileName().toString().matches("[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}"))
      .forEach(dir -> {
        LocalDate dirDate = LocalDate.parse(dir.getFileName().toString());
        long diff = dirDate.until(today, ChronoUnit.DAYS);
        logger.debug("found folder [" + dir.toString() + "] which is [" + diff + "] days old");

        if (diff > MAX_DAYS_TO_KEEP_METADATA) {
          // delete completely
          logger.debug("deleting everything for folder [" + dir.toString() + "]");
          attemptCompleteFolderDelete(dir);

        } else if (diff > MAX_DAYS_TO_KEEP_CONTENT) {
          // delete content only but keep metadata
          logger.debug(
              "deleting only content but keeping metadata for folder [" + dir.toString() + "]");
          attemptFolderContentDelete(dir);

        } else {
          logger.debug("keeping folder [" + dir.toString() + "] without touching");
        }
      });
    }
  }

  private void attemptFolderContentDelete(Path p) {
    logger.info("deleting only content from folder [" + p.toString() + "]");
    try {
      Path subpath = p.resolve(Constants.FILESHARE.CONTENT_PATH);
      if (Files.exists(subpath)) {
        FilesUtils.deletePath(subpath);
      }
    } catch (Exception e) {
      logger.error("failed to delete content for folder [" + p.toString() + "]: " + e.getMessage(),
          e);
    }
  }

  private void attemptCompleteFolderDelete(Path p) {
    logger.info("deleting folder [" + p.toString() + "] completely");
    try {
      FilesUtils.deletePath(p);
    } catch (Exception e) {
      logger.error("failed to delete folder [" + p.toString() + "]: " + e.getMessage(), e);
    }
  }

  @Override
  public FileUploadResult handleUpload(InputStream stream, String filename, String contentType,
      String principalIdentifier) {

    UploadedFileMetadata metadata = new UploadedFileMetadata();
    metadata.setFilename(filename);
    metadata.setContentUUID(UUID.randomUUID().toString());
    metadata.setFileUUID(UUID.randomUUID().toString());
    metadata.setUploadedBy(principalIdentifier);
    metadata.setUploadedAt(ZonedDateTime.now());
    metadata.setContentType(contentType);

    logger.info("handling upload for file [" + metadata.getFileUUID() + "] and content ["
        + metadata.getContentUUID() + "]");

    Path workingFolder = getWorkingFolder(getRoot());
    Path tempWorkingFolder = getOrCreate(workingFolder, Constants.FILESHARE.TEMP_PATH);

    try (TemporaryFolder tempUploadFolder =
        TemporaryFolder.create(tempWorkingFolder, loggerPrefix)) {
      Path tempUploadContent = tempUploadFolder.getTemporaryPath().resolve("content");
      logger.debug(String.format("file [ %s ] temporary content holder is [ %s ]",
          metadata.getFileUUID(), tempUploadContent));

      UploadedFileMetadata writeHashResult = writeAndHash(stream, tempUploadContent);
      metadata.setContentHash(writeHashResult.getContentHash());
      metadata.setContentSize(writeHashResult.getContentSize());



      logger.debug(String.format("file [ %s ] content hash is [ %s ]", metadata.getFileUUID(),
          metadata.getContentHash()));

      metadata.setUploadHash(computeUploadHash(metadata.getContentHash(), principalIdentifier));
      logger.debug(String.format("file [ %s ] upload hash is [ %s ]", metadata.getFileUUID(),
          metadata.getUploadHash()));

      UploadHashFileContent duplicationCheck = checkDuplication(workingFolder, metadata);

      if (duplicationCheck == null) {
        writeNewContent(workingFolder, tempUploadContent, metadata);

      } else {
        logger.debug(String.format(
            "detected content duplication, replacing temporary content UUID [ %s ] with previously existing content UUID [ %s ]",
            metadata.getContentUUID(), duplicationCheck.getContentUUID()));
        metadata.setContentUUID(duplicationCheck.getContentUUID());
        writeDuplicatedContent(workingFolder, metadata);
      }
    }

    FileUploadResult output = new FileUploadResult();
    output.setMetadata(metadata);

    try {
      output.setLocation(
          new URI(Constants.FILESHARE.FILESHARE_PROTOCOL + "://" + metadata.getFileUUID()));
    } catch (URISyntaxException e) {
      throw new FileShareBadConfigurationException("Error encoding output URI: " + e.getMessage(),
          e);
    }

    return output;
  }

  private void writeDuplicatedContent(Path workingFolder, UploadedFileMetadata metadata) {

    writePointer(workingFolder, metadata.getContentUUID(), metadata.getFileUUID());
    writeMetadata(workingFolder, metadata.getFileUUID(), metadata);
  }

  private void writeNewContent(Path workingFolder, Path tempUploadContent,
      UploadedFileMetadata metadata) {

    writePointer(workingFolder, metadata.getContentUUID(), metadata.getFileUUID());
    writeUploadHash(workingFolder, metadata.getUploadHash(), metadata.getContentUUID());
    moveContentFromTemp(workingFolder, tempUploadContent, metadata.getContentUUID());
    writeMetadata(workingFolder, metadata.getFileUUID(), metadata);
  }

  private UploadHashFileContent checkDuplication(Path workingFolder,
      UploadedFileMetadata metadata) {

    /*
     * si verifica la presenza di contenuto duplicato verificando se esiste il file
     * UPLOAD_HASH_PATH/uploadHash avente contenuto duplicatedContentUUID per cui esista almeno un
     * file nella cartella CONTENT_POINTERS_PATH/duplicatedContentUUID
     */
    Path eventualUploadHashFile = workingFolder.resolve(Constants.FILESHARE.UPLOAD_HASH_PATH)
        .resolve(metadata.getUploadHash());

    logger.debug(
        "checking for existing content marked by file [" + eventualUploadHashFile.toString() + "]");

    if (Files.notExists(eventualUploadHashFile)) {
      logger.debug("no duplicated content found");
      return null;
    }

    logger
    .info("found marker for duplicated content at [" + eventualUploadHashFile.toString() + "]");

    UploadHashFileContent content = read(eventualUploadHashFile, UploadHashFileContent.class);

    if (hasActivePointers(workingFolder, content.getContentUUID())) {
      logger.debug(
          "found duplicated content has active pointers and will be used as valid deduplication source");
      return content;

    } else {
      logger.warn(
          "found duplicated content has no active pointers and will be deleted, so not using it for deduplication");
      return null;
    }
  }

  private boolean hasActivePointers(Path workingFolder, String contentUUID) {

    /*
     * [...] per cui esista almeno un file nella cartella
     * CONTENT_POINTERS_PATH/duplicatedContentUUID
     */

    Path eventualPointersFolder =
        workingFolder.resolve(Constants.FILESHARE.CONTENT_POINTERS_PATH).resolve(contentUUID);

    logger.debug(
        "checking for active pointers in folder [" + eventualPointersFolder.toString() + "]");

    if (Files.notExists(eventualPointersFolder)) {
      logger.warn("no active pointers folder found at [" + eventualPointersFolder.toString()
      + "]. This should never happen");
      return false;
    }

    final boolean[] found = {false};

    FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path filename = file.getFileName();
        if (filename != null
            && filename.toString().startsWith(Constants.FILESHARE.POINTER_FILE_PREFIX)) {
          logger.debug("found file " + file.toString() + " which is a valid pointer");
          found[0] = true;
          return FileVisitResult.TERMINATE;
        }
        logger.debug("found file " + file.toString()
        + " which is NOT a valid pointer. Continuing searching");
        return FileVisitResult.CONTINUE;
      }
    };

    try {
      Files.walkFileTree(eventualPointersFolder, fv);
      return found[0];
    } catch (IOException e) {
      throw new FileShareIOException("Error checking for active pointers in folder ["
          + eventualPointersFolder.toString() + "]: " + e.getMessage(), e);
    }

  }

  private <T> T read(Path source, Class<T> contentType) {

    try {
      return ObjectUtils.getDataMapper()
          .readValue(Files.readString(source, Constants.FILESHARE.FS_ENCODING), contentType);
    } catch (IOException e) {
      throw new FileShareIOException("Error reading file [" + source.toString() + "] for data ["
          + contentType.getSimpleName() + "]: " + e.getMessage(), e);
    }
  }

  private void writeMetadata(Path workingFolder, String fileUUID, UploadedFileMetadata metadata) {

    // in META_PATH si scrive il file fileUUID con tutti i metadati
    Path metaPath = getOrCreate(workingFolder, Constants.FILESHARE.META_PATH);
    Path targetFile = metaPath.resolve(fileUUID);

    logger.debug(
        "writing metadata for file [" + fileUUID + "] to location [" + targetFile.toString() + "]");

    try {
      String payload = ObjectUtils.getDataMapper().writeValueAsString(metadata);
      Files.write(targetFile, payload.getBytes(Constants.FILESHARE.FS_ENCODING),
          StandardOpenOption.CREATE);
    } catch (IOException e) {
      throw new FileShareIOException("Error writing metadata for file [" + fileUUID
          + "] to location [" + targetFile + "]: " + e.getMessage(), e);
    }
  }

  private void moveContentFromTemp(Path workingFolder, Path tempUploadContent, String contentUUID) {
    // si SPOSTA il contenuto da TEMP_UPLOAD_FOLDER/contentUUID a CONTENT_PATH/contentUUID

    Path effectiveContentPath = getOrCreate(workingFolder, Constants.FILESHARE.CONTENT_PATH);
    Path targetFile = effectiveContentPath.resolve(contentUUID);

    logger.debug("moving content from temporary location [" + tempUploadContent.toString()
    + "] to effective location [" + targetFile.toString() + "]");

    try {
      Files.move(tempUploadContent, targetFile);
    } catch (IOException e) {
      throw new FileShareIOException(
          "Error moving content from temporary location [" + tempUploadContent.toString()
          + "] to effective location [" + targetFile.toString() + "]: " + e.getMessage(),
          e);
    }
  }

  private void writeUploadHash(Path workingFolder, String uploadHash, String contentUUID) {

    // in UPLOAD_HASH_PATH si scrive il file uploadHash con contenuto contentUUID
    Path uploadHashPath = getOrCreate(workingFolder, Constants.FILESHARE.UPLOAD_HASH_PATH);
    Path targetFile = uploadHashPath.resolve(uploadHash);

    logger.debug(String.format("writing upload hash for content [ %s ] to file [ %s ]", contentUUID,
        targetFile));

    UploadHashFileContent content = new UploadHashFileContent();
    content.setContentUUID(contentUUID);

    try {
      String payload = ObjectUtils.getDataMapper().writeValueAsString(content);
      Files.write(targetFile, payload.getBytes(Constants.FILESHARE.FS_ENCODING),
          StandardOpenOption.CREATE);
    } catch (IOException e) {
      throw new FileShareIOException(
          String.format("Error writing upload hash for content [ %s ] to file [ %s ]: %s",
              contentUUID, targetFile, e.getMessage()),
          e);
    }
  }

  private void writePointer(Path workingFolder, String targetContentUUID, String sourceFileUUID) {

    Path pointerFolder = getOrCreate(workingFolder, Constants.FILESHARE.CONTENT_POINTERS_PATH);
    Path pointerForContentFolder = getOrCreate(pointerFolder, targetContentUUID);
    Path targetFile =
        pointerForContentFolder.resolve(Constants.FILESHARE.POINTER_FILE_PREFIX + sourceFileUUID);

    logger.debug("writing content pointer for content [" + targetContentUUID + "] to file ["
        + targetFile.toString() + "]");

    try {
      Files.write(targetFile, new byte[] {}, StandardOpenOption.CREATE);
    } catch (IOException e) {
      throw new FileShareIOException("Error writing pointer for content [" + targetContentUUID
          + "] to file [" + sourceFileUUID + "]: " + e.getMessage(), e);
    }
  }

  private String computeUploadHash(String contentHash, String principalIdentifier) {
    return principalIdentifier + "-" + contentHash;
  }

  private Path getWorkingFolder(Path root) {
    String subpath = ZonedDateTime.now()
        .format(DateTimeFormatter.ofPattern(Constants.FILESHARE.WORKING_FOLDER_FORMAT));
    try {
      return getOrCreate(root, subpath);
    } catch (Exception e) {
      throw new FileShareIOException(
          "Error creating working folder [" + subpath + "] for file upload: " + e.getMessage(), e);
    }
  }

  @Override
  public Path getRoot() {
    Path p = Paths.get(rootPath);
    p = p.normalize();
    if (!Files.exists(p)) {
      throw new FileShareBadConfigurationException(
          "FileShare root path is missing [" + p.toString() + "]");
    }

    return getOrCreate(p, Constants.FILESHARE.TRANSFER_PATH);
  }

  private UploadedFileMetadata writeAndHash(InputStream inputStream, Path target) {
    UploadedFileMetadata output = new UploadedFileMetadata();

    try {

      logger.debug("writing from input stream to file " + target.toString());

      OpenOption[] options = new OpenOption[] {StandardOpenOption.CREATE};
      int length;
      byte[] bytes = new byte[1024];
      long outputSize = 0;
      MessageDigest md = getHashDigest();

      try (OutputStream outputStream = Files.newOutputStream(target, options);
          DigestInputStream dis = new DigestInputStream(inputStream, md)) {

        // copy data from input stream to output stream
        while ((length = dis.read(bytes)) != -1) {
          outputStream.write(bytes, 0, length);
          outputSize += length;
        }

      }

      byte[] digest = md.digest();
      String hex = FilesUtils.bytesToHex(digest);
      logger.debug("computed hash of file " + target.toString() + " to be [" + hex + "]");

      output.setContentHash(hex);
      output.setContentSize(outputSize);

      if (outputSize == 0) {
        throw new BadRequestException("Il file è vuoto");
      }

      return output;

    } catch (Exception e) {
      throw new FileShareIOException(
          "Error writing and hashing content to [" + target.toString() + "]: " + e.getMessage(), e);
    }
  }

  private MessageDigest getHashDigest() {
    try {
      return MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new FileShareBadConfigurationException("MD5 codec is not available");
    }
  }

  private Path getOrCreate(Path root, String subpath) {
    try {
      return FilesUtils.getOrCreate(root, subpath);
    } catch (Exception e) {
      throw new FileShareIOException(
          "Error provisioning folder [" + subpath + "]: " + e.getMessage(), e);
    }
  }

  @Override
  public RetrievedContent get(String fileUUID) {
    return get(fileUUID, LocalDate.now());
  }

  @Override
  public RetrievedContent get(String fileUUID, LocalDate referenceDate) {

    var retrieved = getSingleFile(fileUUID, referenceDate, true);
    if (retrieved != null) {
      //@formatter:off
      return RetrievedFile.builder()
          .withContentStream(retrieved.getContentStream())
          .withMetadata(retrieved.getMetadata())
          .withWorkingFolder(retrieved.getWorkingFolder())
          .build();
      //@formatter:on
    }

    // try to fetch an upload session with that id
    var session = getCompletedUploadSession(fileUUID, referenceDate);
    if (session == null) {
      throw new NotFoundException("No uploaded content matching UUID " + fileUUID);
    }

    return session;
  }

  private RetrievedFile getSingleFile(String fileUUID, LocalDate referenceDate, boolean openFile) {
    if (referenceDate == null) {
      referenceDate = LocalDate.now();
    }
    Path workingFolder = findWorkingFolder(fileUUID, referenceDate);

    if (workingFolder == null) {
      return null;
    }

    UploadedFileMetadata metadata = getMetadata(fileUUID, workingFolder);

    if (metadata == null) {
      throw new FileShareRetrievalException("Invalid or broken metadata for file " + fileUUID
          + " in working folder " + workingFolder.toString());
    }

    InputStream stream = null;

    if (openFile) {
      stream = getContentStream(metadata.getContentUUID(), workingFolder);
      if (stream == null) {
        throw new FileShareRetrievalException("Invalid or broken content for file " + fileUUID
            + " in working folder " + workingFolder.toString());
      }
    }

    return RetrievedFile.builder().withContentStream(stream).withMetadata(metadata)
        .withWorkingFolder(workingFolder).build();
  }

  @Override
  public void delete(RetrievedContent file) {

    if (file instanceof RetrievedFile) {
      doDelete((RetrievedFile) file);
    } else {
      deleteUploadSession((RetrievedUploadSession) file);
    }
  }

  private synchronized void doDelete(RetrievedFile file) {
    logger.debug("deleting file " + file);

    deletePointer(file);
    deleteMetadata(file);

    // don't delete the metadata file for now

    scheduleContentForDeletion(file);
  }

  private void deleteMetadata(RetrievedFile file) {

    Path metadataFile = file.getWorkingFolder().resolve(Constants.FILESHARE.META_PATH)
        .resolve(file.getMetadata().getFileUUID());

    if (Files.notExists(metadataFile)) {
      logger.warn("requested deletion for not existing metadata file " + metadataFile.toString());
    } else {
      logger.debug("deleting metadata file " + metadataFile.toString());
      try {
        Files.delete(metadataFile);
      } catch (IOException e) {
        logger.error(
            "error deleting metadata file [" + metadataFile.toString() + "]: " + e.getMessage(), e);
      }
    }
  }

  private void deletePointer(RetrievedFile file) {

    Path pointersFolderForContent =
        file.getWorkingFolder().resolve(Constants.FILESHARE.CONTENT_POINTERS_PATH)
        .resolve(file.getMetadata().getContentUUID());

    Path pointerForFile = pointersFolderForContent
        .resolve(Constants.FILESHARE.POINTER_FILE_PREFIX + file.getMetadata().getFileUUID());

    if (Files.notExists(pointerForFile)) {
      logger.warn("requested deletion for not existing pointer " + file.getMetadata().getFileUUID()
          + " to content " + file.getMetadata().getContentUUID());
    } else {
      logger.debug("deleting pointer file " + pointerForFile.toString());
      try {
        Files.delete(pointerForFile);
      } catch (IOException e) {
        logger.error(
            "error deleting pointer file [" + pointerForFile.toString() + "]: " + e.getMessage(),
            e);
        logger.error("because of error occured deleting pointer file [" + pointerForFile.toString()
        + "], its related content WON'T PROBABLY BE PURGED FROM FILE SHARE");
      }
    }
  }

  private void scheduleContentForDeletion(RetrievedFile file) {

    logger.debug("scheduling content " + file.getMetadata().getContentUUID()
        + " for content deletion check");

    scheduledThreadPool.schedule(() -> checkContentDeletion(file), 15, TimeUnit.SECONDS);
  }

  private void checkContentDeletion(RetrievedFile file) {
    try {
      doCheckContentDeletion(file);
    } catch (Throwable t) { // NOSONAR
      logger.error("error checking content for deletion: " + t.getMessage(), t);
    }
  }

  private void doCheckContentDeletion(RetrievedFile file) {

    if (!hasActivePointers(file.getWorkingFolder(), file.getMetadata().getContentUUID())) {
      logger.info("there are no remaining active pointers for content, deleting content");
      deleteContent(file);
    } else {
      logger.debug("there are still active pointers for content, skipping deletion");
    }
  }

  private void deleteContent(RetrievedFile file) {

    // delete hash file
    Path hashFile = file.getWorkingFolder().resolve(Constants.FILESHARE.UPLOAD_HASH_PATH)
        .resolve(file.getMetadata().getUploadHash());
    if (Files.notExists(hashFile)) {
      logger.warn("could not delete hash marker because it is missing from " + hashFile.toString());
    } else {
      try {
        logger.info("deleting hash marker from [" + hashFile.toString() + "]");
        Files.delete(hashFile);
      } catch (IOException e) {
        logger.error(
            "error deleting hash marker from [" + hashFile.toString() + "]: " + e.getMessage(), e);
      }
    }

    // delete content file
    Path contentFile = file.getWorkingFolder().resolve(Constants.FILESHARE.CONTENT_PATH)
        .resolve(file.getMetadata().getContentUUID());
    if (Files.notExists(contentFile)) {
      logger.warn(
          "could not delete file content because it is missing from " + contentFile.toString());
    } else {
      try {
        logger.info("deleting file content from [" + contentFile.toString() + "]");
        Files.delete(contentFile);
      } catch (IOException e) {
        logger.error(
            "error deleting file content from [" + contentFile.toString() + "]: " + e.getMessage(),
            e);
      }
    }

    // delete pointers folder
    Path pointersFolder = file.getWorkingFolder().resolve(Constants.FILESHARE.CONTENT_POINTERS_PATH)
        .resolve(file.getMetadata().getContentUUID());
    if (Files.notExists(pointersFolder)) {
      logger.warn("could not delete content pointers folder because it is missing from "
          + pointersFolder.toString());
    } else {
      try {
        logger.info("deleting content pointers folder from [" + pointersFolder.toString() + "]");
        FilesUtils.deletePath(pointersFolder);
      } catch (IOException e) {
        logger.error("error deleting content pointers folder from [" + pointersFolder.toString()
        + "]: " + e.getMessage(), e);
      }
    }
  }

  private Path findWorkingFolder(String fileUUID, LocalDate referenceDate) {
    return Arrays.stream(getWorkingFolders(getRoot(), referenceDate))
        .filter(wf -> Files.exists(wf.resolve(Constants.FILESHARE.META_PATH).resolve(fileUUID)))
        .findFirst().orElse(null);
  }

  private Path findWorkingUploadSessionFolder(String sessionUUID, LocalDate referenceDate) {
    return Arrays.stream(getWorkingFolders(getRoot(), referenceDate))
        .filter(wf -> Files.exists(wf.resolve(Constants.FILESHARE.SESSIONS_PATH)
            .resolve(sessionUUID).resolve(Constants.FILESHARE.SESSIONS_METADATA_SUBPATH)))
        .findFirst().orElse(null);
  }

  private InputStream getContentStream(String contentUUID, Path workingFolder) {
    Path targetFile = workingFolder.resolve(Constants.FILESHARE.CONTENT_PATH).resolve(contentUUID);
    if (!Files.exists(targetFile)) {
      logger.warn("Could not locate content + " + contentUUID + " in its working folder");
      return null;
    }

    try {
      return new BufferedInputStream(Files.newInputStream(targetFile));
    } catch (IOException e) {
      throw new FileShareRetrievalException(
          "Error opening stream from content file: " + e.getMessage(), e);
    }
  }

  private UploadedFileMetadata getMetadata(String fileUUID, Path workingFolder) {

    Path targetFile = workingFolder.resolve(Constants.FILESHARE.META_PATH).resolve(fileUUID);
    if (!Files.exists(targetFile)) {
      logger.warn("metadata not found for file [" + fileUUID + "] in folder ["
          + workingFolder.toString() + "]");
      return null;
    }

    return read(targetFile, UploadedFileMetadata.class);
  }

  private UploadSessionMetadata getUploadSessionMetadata(String sessionUUID, Path workingFolder) {

    Path targetFile = workingFolder.resolve(Constants.FILESHARE.SESSIONS_PATH).resolve(sessionUUID)
        .resolve(Constants.FILESHARE.SESSIONS_METADATA_SUBPATH);
    if (!Files.exists(targetFile)) {
      logger.warn("metadata not found for upload session [" + sessionUUID + "] in folder ["
          + workingFolder.toString() + "]");
      return null;
    }

    return read(targetFile, UploadSessionMetadata.class);
  }

  private Path[] getWorkingFolders(Path root, LocalDate referenceDate) {
    DateTimeFormatter workingFolderPattern =
        DateTimeFormatter.ofPattern(Constants.FILESHARE.WORKING_FOLDER_FORMAT);


    try {
      return new Path[] {root.resolve(referenceDate.format(workingFolderPattern)),
          root.resolve(referenceDate.minusDays(1).format(workingFolderPattern)),
          root.resolve(referenceDate.minusDays(2).format(workingFolderPattern))};
    } catch (Exception e) {
      throw new FileShareIOException("Error getting working folders: " + e.getMessage(), e);
    }
  }

  @Override
  public void testResources() {
    // check: esistenza della root
    Path root = getRoot();

    if (!Files.exists(root)) {
      throw new FileShareBadConfigurationException(
          "Root folder " + root.toString() + " does not exist");
    }
  }

  @Override
  public CreateUploadSessionResponse createUploadSession(CreateUploadSessionRequest request) {
    ValidationUtils.require(request, "request");
    ValidationUtils.validaAnnotations(request);

    ValidationUtils.require(request.getFileName(), "request.fileName");
    ValidationUtils.require(request.getSize(), "request.size");

    String sessionUUID = UUID.randomUUID().toString();

    logger.debug("creating upload session [" + sessionUUID + "]");

    // create the /sessions/<uuid> and /sessions/<uuid>/parts folder

    Path workingFolder = getWorkingFolder(getRoot());

    Path sessionsFolder = getOrCreate(workingFolder, Constants.FILESHARE.SESSIONS_PATH);
    Path sessionFolder = getOrCreate(sessionsFolder, sessionUUID);
    getOrCreate(sessionFolder, Constants.FILESHARE.SESSIONS_PARTS_SUBPATH);

    // create metadata

    //@formatter:off
    UploadSessionMetadata metadata = UploadSessionMetadata.builder()
        .withFileName(request.getFileName())
        .withMimeType(request.getMimeType())
        .withSize(request.getSize())
        .withSessionUUID(sessionUUID)
        .withUploadedAt(ZonedDateTime.now())
        .build();
    //@formatter:on

    // save metadata to session folder
    Path metadataFile = sessionFolder.resolve(Constants.FILESHARE.SESSIONS_METADATA_SUBPATH);

    logger.debug("writing metadata for upload session [" + sessionUUID + "] to location ["
        + metadataFile.toString() + "]");

    try {
      String payload = ObjectUtils.getDataMapper().writeValueAsString(metadata);
      Files.write(metadataFile, payload.getBytes(Constants.FILESHARE.FS_ENCODING),
          StandardOpenOption.CREATE);
    } catch (IOException e) {
      throw new FileShareIOException("Error writing metadata for upload session [" + sessionUUID
          + "] to location [" + metadataFile + "]: " + e.getMessage(), e);
    }

    // return response
    return CreateUploadSessionResponse.builder().withSessionUUID(sessionUUID).build();
  }

  @Override
  public FileUploadResult handPartUpload(String sessionUUID, Long partNumber, InputStream stream,
      String principalIdentifier) {
    ValidationUtils.require(sessionUUID, "sessionUUID");
    ValidationUtils.require(stream, "stream");

    // fetch session
    var retrievedSession = requireRecentUploadSession(sessionUUID, LocalDate.now());

    if (Files.exists(retrievedSession.getCompletedMarkerFile())) {
      throw new ConflictException("upload session " + sessionUUID + " already completed");
    }

    // handle raw upload
    var uploaded = handleUpload(stream, "part" + partNumber,
        ContentType.APPLICATION_OCTET_STREAM.getMimeType(), principalIdentifier);

    // create a "pointer" for the part number to the uploaded file
    //@formatter:off
    UploadSessionPartMetadata partMetadata = UploadSessionPartMetadata.builder()
        .withFileUUID(uploaded.getMetadata().getFileUUID())
        .withPartNumber(partNumber)
        .withUploadedAt(ZonedDateTime.now())
        .build();
    //@formatter:on

    // write metadata to pointer file
    //@formatter:off
    Path pointerFile = retrievedSession.getPartsFolder()
        .resolve(partNumber.toString());
    //@formatter:on

    logger.debug("writing pointer for part [ " + partNumber + " ] for upload session ["
        + sessionUUID + "] to location [" + pointerFile.toString() + "]");

    try {
      String payload = ObjectUtils.getDataMapper().writeValueAsString(partMetadata);
      Files.write(pointerFile, payload.getBytes(Constants.FILESHARE.FS_ENCODING),
          StandardOpenOption.CREATE);
    } catch (IOException e) {
      throw new FileShareIOException(
          "Error writing pointer for part [ " + partNumber + " ] for upload session [" + sessionUUID
          + "] to location [" + pointerFile + "]: " + e.getMessage(),
          e);
    }

    return uploaded;
  }

  @Override
  public CompleteUploadSessionResponse completeUploadSession(CompleteUploadSessionRequest request) {
    ValidationUtils.require(request, "request");
    ValidationUtils.validaAnnotations(request);
    ValidationUtils.require(request.getSessionUUID(), "request.sessionUUID");

    var sessionUUID = request.getSessionUUID();

    var retrievedSession = requireRecentUploadSession(sessionUUID, LocalDate.now());

    if (Files.exists(retrievedSession.getCompletedMarkerFile())) {
      throw new ConflictException("upload session " + sessionUUID + " already completed");
    }

    // check that we have all parts
    retrieveAndValidateParts(retrievedSession, false);

    // write the 'finished' marker in the session folder
    try {
      Files.write(retrievedSession.getCompletedMarkerFile(),
          "1".getBytes(Constants.FILESHARE.FS_ENCODING), StandardOpenOption.CREATE);
    } catch (IOException e) {
      throw new FileShareIOException(
          "Error writing completed marker for upload session [" + sessionUUID + "] to location ["
              + retrievedSession.getCompletedMarkerFile().toString() + "]: " + e.getMessage(),
              e);
    }

    CompleteUploadSessionResponse output = new CompleteUploadSessionResponse();

    try {
      output.setLocation(new URI(Constants.FILESHARE.FILESHARE_PROTOCOL + "://" + sessionUUID));
    } catch (URISyntaxException e) {
      throw new FileShareBadConfigurationException("Error encoding output URI: " + e.getMessage(),
          e);
    }

    output.setMetadata(retrievedSession.getSession());

    return output;
  }

  private RetrievedUploadSession getCompletedUploadSession(String sessionUUID,
      LocalDate referenceDate) {

    var session = getRecentUploadSession(sessionUUID, referenceDate);
    if (session == null) {
      return null;
    }

    if (!Files.exists(session.getCompletedMarkerFile())) {
      throw new ConflictException("upload session " + sessionUUID + " not completed");
    }

    // retrieve parts
    var parts = retrieveAndValidateParts(session, true);
    session.setParts(parts);

    // create single input stream from parts

    var streams = new LinkedList<InputStream>();
    for (var part : parts) {
      streams.add(part.getPartFile().getContentStream());
    }

    var combinedStream = new SequenceInputStream(Collections.enumeration(streams));
    session.setContentStream(combinedStream);

    return session;
  }


  private RetrievedUploadSession requireRecentUploadSession(String sessionUUID,
      LocalDate referenceDate) {

    var result = getRecentUploadSession(sessionUUID, referenceDate);
    if (result == null) {
      throw new FileShareRetrievalException("Could not find upload session "
          + sessionUUID + " in any recent working folder");
    }
    return result;
  }

  private RetrievedUploadSession getRecentUploadSession(String sessionUUID,
      LocalDate referenceDate) {

    // fetch session
    Path workingFolder = findWorkingUploadSessionFolder(sessionUUID, referenceDate);
    if (workingFolder == null) {
      return null;
    }

    var session = getUploadSessionMetadata(sessionUUID, workingFolder);
    if (session == null) {
      throw new FileShareRetrievalException(
          "Session " + sessionUUID + " does not exist or is expired");
    }

    var sessionFolder =
        workingFolder.resolve(Constants.FILESHARE.SESSIONS_PATH).resolve(sessionUUID);

    var partsFolder = sessionFolder.resolve(Constants.FILESHARE.SESSIONS_PARTS_SUBPATH);

    var completedMarkerFile =
        sessionFolder.resolve(Constants.FILESHARE.SESSIONS_COMPLETED_MARKER_SUBPATH);

    return RetrievedUploadSession.builder().withSession(session).withWorkingFolder(workingFolder)
        .withSessionFolder(sessionFolder).withPartsFolder(partsFolder)
        .withCompletedMarkerFile(completedMarkerFile).build();
  }

  private LinkedList<RetrievedUploadSessionPart> retrieveAndValidateParts(
      RetrievedUploadSession retrievedSession, boolean openForReading) {
    ValidationUtils.require(retrievedSession, "retrievedSession");
    ValidationUtils.require(retrievedSession.getSession().getSessionUUID(),
        "retrievedSession.session.sessionUUID");

    var output = new LinkedList<RetrievedUploadSessionPart>();

    var sessionUUID = retrievedSession.getSession().getSessionUUID();

    logger.debug("validating uploaded parts for upload session [" + sessionUUID + "]");

    Long declaredSize = retrievedSession.getSession().getSize();
    Long partsSize = 0L;

    long i = 0;
    while (!partsSize.equals(declaredSize)) {
      var retrievedPart = getUploadSessionPart(retrievedSession, i, openForReading);
      if (retrievedPart == null) {
        throw new FileShareRetrievalException(
            "Missing part number " + i + " for upload session " + sessionUUID);
      }

      partsSize += retrievedPart.getPartFile().getMetadata().getContentSize();

      if (partsSize > declaredSize) {
        throw new BadRequestException(
            "too many bytes transferred for upload session " + sessionUUID);
      }

      output.add(retrievedPart);
      i++;
    }

    logger.debug("succesfully validated uploaded parts for upload session [" + sessionUUID + "]");

    return output;
  }

  private RetrievedUploadSessionPart getUploadSessionPart(RetrievedUploadSession retrievedSession,
      Long partNumber, boolean openForReading) {
    ValidationUtils.require(retrievedSession, "retrievedSession");

    var partFile = retrievedSession.getPartsFolder().resolve(partNumber.toString());
    if (!Files.exists(partFile, LinkOption.NOFOLLOW_LINKS)) {
      return null;
    }

    var partMetadata = read(partFile, UploadSessionPartMetadata.class);

    var retrievedFile = getSingleFile(partMetadata.getFileUUID(), null, openForReading);
    if (retrievedFile == null) {
      throw new FileShareRetrievalException("Missing part content number " + partNumber
          + " for upload session " + retrievedSession.getSession().getSessionUUID());
    }

    return RetrievedUploadSessionPart.builder().withPart(partMetadata).withPartFile(retrievedFile)
        .build();
  }

  @Override
  public void cancelUploadSession(String sessionUUID) {
    ValidationUtils.require(sessionUUID, "sessionUUID");

    var session = requireRecentUploadSession(sessionUUID, LocalDate.now());

    deleteUploadSession(session);
  }

  private void deleteUploadSession(RetrievedUploadSession retrievedSession) {
    ValidationUtils.require(retrievedSession, "retrievedSession");
    var sessionUUID = retrievedSession.getSession().getSessionUUID();

    logger.debug("deleting upload session [" + sessionUUID + "]");

    // recursively delete all session directory
    attemptCompleteFolderDelete(retrievedSession.getSessionFolder());

    logger.debug("deleting part files for session [" + sessionUUID + "]");

    // delete all part files
    for (var part : retrievedSession.getParts()) {
      logger.debug("deleting part file [ " + part.getPart().getPartNumber() + " ] for session ["
          + sessionUUID + "]");
      doDelete(part.getPartFile());
    }

    logger.debug("deleted upload session [" + sessionUUID + "]");
  }

  /**
   * Creates builder to build {@link DefaultFileShareManagerImpl}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link DefaultFileShareManagerImpl}.
   */
  public static final class Builder {
    private String rootPath;
    private String loggingPrefix;

    private Builder() {}

    public Builder withLoggingPrefix(String loggingPrefix) {
      this.loggingPrefix = loggingPrefix;
      return this;
    }

    public Builder withRootPath(String rootPath) {
      this.rootPath = rootPath;
      return this;
    }

    public DefaultFileShareManagerImpl build() {
      return new DefaultFileShareManagerImpl(this);
    }
  }

}
