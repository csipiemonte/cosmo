/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.rest.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.fileshare.model.FileUploadResult;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmo.business.rest.FileApi;
import it.csi.cosmo.cosmo.business.rest.proto.ParentApiImpl;
import it.csi.cosmo.cosmo.business.service.FileShareService;
import it.csi.cosmo.cosmo.dto.EsitoEnum;
import it.csi.cosmo.cosmo.dto.rest.CompleteUploadDocumentiZipSessionRequest;
import it.csi.cosmo.cosmo.dto.rest.CompleteUploadSessionRequest;
import it.csi.cosmo.cosmo.dto.rest.CreateUploadSessionRequest;
import it.csi.cosmo.cosmo.dto.rest.Esito;
import it.csi.cosmo.cosmo.dto.rest.FileDocumentiZipUnzipRequest;
import it.csi.cosmo.cosmo.dto.rest.FileUploadPraticheResult;
import it.csi.cosmo.cosmo.security.SecurityUtils;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;



public class FileShareApiImpl extends ParentApiImpl implements FileApi {

  @Autowired
  private FileShareService fileShareService;

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.REST_LOG_CATEGORY, "FileShareApiImpl");


  @Override
  public Response postFileUpload(MultipartFormDataInput input, String xRequestId,
      SecurityContext securityContext) {

    String fileName = "";
    String contentType = "";
    FileUploadResult output = null;

    Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
    List<InputPart> inputParts = uploadForm.get("payload");

    for (InputPart inputPart : inputParts) {
      if (output != null) {
        throw new BadRequestException("Multiple file upload is not supported");
      }

      MultivaluedMap<String, String> header = inputPart.getHeaders();
      fileName = getFileName(header);
      contentType = inputPart.getMediaType().toString();


      // convert the uploaded file to inputstream
      InputStream inputStream;
      try {
        inputStream = inputPart.getBody(InputStream.class, null);
      } catch (IOException e) {
        throw new InternalServerException("Error reading request stream", e);
      }

      output = fileShareService.handleUpload(inputStream, fileName, contentType,
          SecurityUtils.getUtenteCorrente());
    }

    if (output == null) {
      return Response.noContent().build();
    }

    return Response.ok(output).build();
  }

  private String getFileName(MultivaluedMap<String, String> header) {

    String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

    for (String filename : contentDisposition) {
      if ((filename.trim().startsWith("filename"))) {

        String[] name = filename.split("=");
        // TODO: trovare un altro modo per ricevere correttamente il nome del file anche con
        // caratteri speciali
        String decoded = URLDecoder.decode(
            new String(name[1].trim().substring(1, name[1].trim().length() - 1)
                .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8),
            StandardCharsets.UTF_8);
        return decoded.replaceAll("^\\.+", "_").replaceAll("[\\\\/:*?\"<>|]", "_").replaceAll("__",
            "_");
      }
    }


    return "unknown";
  }

  @Secured(testOnly = true)
  public Response cleanup() {
    fileShareService.cleanup();
    return Response.noContent().build();
  }

  @Override
  public Response postFileUploadSession(CreateUploadSessionRequest body,
      SecurityContext securityContext) {

    //@formatter:off
    var req = it.csi.cosmo.common.fileshare.model.CreateUploadSessionRequest.builder()
        .withFileName(body.getFileName())
        .withMimeType(body.getMimeType())
        .withSize(body.getSize())
        .build();
    //@formatter:on

    var res = fileShareService.createUploadSession(req);

    var mapped = new it.csi.cosmo.cosmo.dto.rest.CreateUploadSessionResponse();
    mapped.setSessionUUID(res.getSessionUUID());

    return Response.status(201).entity(mapped).build();
  }

  @Override
  public Response postFileUploadSessionPart(MultipartFormDataInput input, String sessionUUID,
      Long partNumber, String xRequestId, SecurityContext securityContext) {

    Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
    List<InputPart> inputParts = uploadForm.get("payload");

    for (InputPart inputPart : inputParts) {
      // convert the uploaded file to inputstream
      InputStream inputStream;
      try {
        inputStream = inputPart.getBody(InputStream.class, null);
      } catch (IOException e) {
        throw new InternalServerException("Error reading request stream", e);
      }

      fileShareService.handPartUpload(sessionUUID, partNumber, inputStream,
          SecurityUtils.getUtenteCorrente());
    }

    return Response.noContent().build();
  }

  @Override
  public Response postFileUploadSessionComplete(String sessionUUID,
      CompleteUploadSessionRequest body, SecurityContext securityContext) {

    //@formatter:off
    var req = it.csi.cosmo.common.fileshare.model.CompleteUploadSessionRequest.builder()
        .withSessionUUID(sessionUUID)
        .withHashes(body.getHashes())
        .build();
    //@formatter:on

    var res = fileShareService.completeUploadSession(req);

    var mapped = new it.csi.cosmo.cosmo.dto.rest.CompleteUploadSessionResponse();
    mapped.setUploadUUID(res.getMetadata().getSessionUUID());

    return Response.status(201).entity(mapped).build();
  }

  @Override
  public Response deleteFileUploadSessionCancel(String sessionUUID,
      SecurityContext securityContext) {

    fileShareService.cancelUploadSession(sessionUUID);

    return Response.noContent().build();
  }

  @Override
  public Response getFileTemp(String uploadUUID, SecurityContext securityContext) {

    var found = fileShareService.get(uploadUUID);
    if (found == null) {
      throw new NotFoundException("no temporary file with uuid " + uploadUUID);
    }

    StreamingOutput stream = new StreamingOutput() {
      @Override
      public void write(OutputStream os) throws IOException {
        found.getContentStream().transferTo(os);
      }
    };

    ResponseBuilder response = Response.status(HttpStatus.OK.value()).entity(stream);

    response.header("Content-Disposition",
        "inline; filename=" + getFileNameSafe(found.getFilename()));

    if (StringUtils.isNotBlank(found.getContentType())) {
      response.header("Content-Type", sanitizeMimeType(found.getContentType()));
    } else {
      response.header("Content-Type", "application/octet-stream");
    }

    return response.build();
  }

  // TODO move in common utils
  public static String sanitizeMimeType(String mimeType) {
    if (mimeType == null) {
      return null;
    }

    mimeType = mimeType.strip();
    if (mimeType.contains(";")) {
      mimeType = mimeType.substring(0, mimeType.indexOf(';'));
    }
    if (!mimeType.contains("/") && !mimeType.contains("application")) {
      mimeType = "application/" + mimeType;
    }
    return mimeType.strip();
  }

  // TODO move in common utils
  public static String getFileNameSafe(String fileName) {
    if (fileName == null || fileName.trim().isEmpty()) {
      return null;
    } else {
      return fileName.trim().replaceAll("[^a-zA-Z0-9_\\.]+", "_");
    }
  }

  @Override
  @Secured("LOAD_PRATICHE")
  public Response postFileDocumentiZipUploadSession(CreateUploadSessionRequest body,
      SecurityContext securityContext) {

    ValidationUtils.require(body, "body");
    ValidationUtils.require(body.getFileName(), "fileName");
    ValidationUtils.require(body.getSize(), "size");
    ValidationUtils.require(body.getMimeType(), "mimeType");

    this.fileShareService.verifyFileDocumentiZipUploadSession(body);

    return this.postFileUploadSession(body, securityContext);
  }

  @Override
  @Secured("LOAD_PRATICHE")
  public Response postFileDocumentiZipUploadSessionPart(MultipartFormDataInput input,
      String sessionUUID, Long partNumber, String xRequestId, SecurityContext securityContext) {
    return this.postFileUploadSessionPart(input, sessionUUID, partNumber, xRequestId,
        securityContext);
  }

  @Override
  @Secured("LOAD_PRATICHE")
  public Response postFileDocumentiZipUploadSessionComplete(String sessionUUID,
      CompleteUploadDocumentiZipSessionRequest body, SecurityContext securityContext) {

    ValidationUtils.require(body, "body");
    ValidationUtils.require(body.getFolderName(), "folderName");

    var req = it.csi.cosmo.common.fileshare.model.CompleteUploadSessionRequest.builder()
        .withSessionUUID(sessionUUID).withHashes(body.getHashes()).build();
    //@formatter:on

    fileShareService.completeUploadSession(req);

    EsitoEnum esito = this.fileShareService.manageZipFile(sessionUUID, body.getFolderName());


    var mapped = new it.csi.cosmo.cosmo.dto.rest.CompleteUploadDocumentiZipSessionResponse();
    mapped.setEsito(esito.toString());

    return Response.status(201).entity(mapped).build();

  }



  @Secured("LOAD_PRATICHE")
  @Override
  public Response postFilePraticheUpload(MultipartFormDataInput input, String xRequestId,
      SecurityContext securityContext) {
    String fileName = "";

    FileUploadPraticheResult result = new FileUploadPraticheResult();
    Esito esito = new Esito();

    Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
    List<InputPart> inputParts = uploadForm.get("payload");

    if (inputParts == null || inputParts.isEmpty()) {
      inputParts = uploadForm.get("file");
    }

    if (inputParts == null || inputParts.isEmpty()) {
      throw new BadRequestException("Nessun contenuto fornito. "
          + "Accertati di utilizzare il content type 'multipart/form-data' "
          + "e di inviare il contenuto del file utilizzando il name 'payload' oppure 'file'");
    }

    if (inputParts.size() > 1) {
      throw new BadRequestException(
          "Il caricamento di file multipli non e' consentito. " + "Invia un solo file alla volta.");
    }

    for (InputPart inputPart : inputParts) {
      MultivaluedMap<String, String> header = inputPart.getHeaders();
      fileName = getFileName(header);


      try {
        boolean validated =
            fileShareService.validateExcel(inputPart.getBody(InputStream.class, null));

        String pathFile = fileShareService
            .handleUploadExcel(inputPart.getBody(InputStream.class, null), fileName);

        result.setFolderName(pathFile);

        esito.setCode("OK");
        esito.setStatus(200);
        esito.setTitle(validated ? EsitoEnum.CON_VALIDAZIONE.toString()
            : EsitoEnum.SENZA_VALIDAZIONE.toString());

        result.setEsito(esito);


      } catch (IOException e) {
        throw new InternalServerException("Error reading request stream", e);
      }

    }


    return Response.ok(result).build();
  }

  @Override
  public Response postFilePraticheUnzipFile(FileDocumentiZipUnzipRequest body,
      SecurityContext securityContext) {
    var output = fileShareService.unzipFile(body);
    if (output == null) {
      return Response.noContent().build();
    }
    var result = new it.csi.cosmo.cosmo.dto.rest.FileUploadResult();
    result.setUploadUUID(output.getMetadata().getFileUUID());

    return Response.ok(result).build();
  }



  @Override
  @Secured("LOAD_PRATICHE")
  public Response getFilePratiche(String pathFile, SecurityContext securityContext) {
    return Response.ok(fileShareService.getFilePratiche(pathFile)).build();
  }

  @Override
  public Response deleteFilePratiche(String pathFile, SecurityContext securityContext) {

    fileShareService.deletePratichePath(pathFile);

    return Response.noContent().build();
  }


}
