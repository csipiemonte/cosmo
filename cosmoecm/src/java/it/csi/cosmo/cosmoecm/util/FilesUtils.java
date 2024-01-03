/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmoecm.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.http.HttpStatus;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoecm.dto.FileContent;
import it.csi.cosmo.cosmoecm.dto.ResponseHeaderDTO;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;



/**
 *
 */
public abstract class FilesUtils {

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.UTIL_LOG_CATEGORY, "FilesUtils");

  private FilesUtils() {}

  /**
   * Metodo per ottenere il nome del file che viene inviato
   *
   * @param multipart il file inviato da cui ottenere il nome
   * @return una stringa contenente il nome del file del sistema con anche l'estensione
   */
  public static String getMimetype(byte[] data) {
    String mimeType = null;
    try {
      InputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
      mimeType = URLConnection.guessContentTypeFromStream(is);
      if (mimeType == null) {
        mimeType = "application/octet-stream";
      }
    } catch (IOException e) {
      LOGGER.error("getMimetype", "getMimetype caused IOException", e);
    }
    return mimeType;
  }

  /**
   * Metodo per ottenere il nome del file che viene inviato
   *
   * @param multipart il file inviato da cui ottenere il nome
   * @return una stringa contenente il nome del file del sistema con anche l'estensione
   */
  public static String getFileName(MultipartFormDataInput multipart) {

    Map<String, List<InputPart>> uploadForm = multipart.getFormDataMap();

    List<InputPart> inputPartsFilename = uploadForm.get("filename");
    if (inputPartsFilename != null && !inputPartsFilename.isEmpty()) {

      String filenameFromParts;
      try {
        filenameFromParts = inputPartsFilename.get(0).getBodyAsString();
      } catch (IOException e) {
        LOGGER.error("getFileName", "getFileName caused IOException: ", e);
        throw new InternalServerException("error reading filename", e);
      }
      if (!StringUtils.isBlank(filenameFromParts)) {
        return filenameFromParts;
      }
    }

    List<InputPart> inputParts = uploadForm.get("file");

    for (InputPart inputPart : inputParts) {
      MultivaluedMap<String, String> header = inputPart.getHeaders();
      String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

      for (String filename : contentDisposition) {
        if ((filename.trim().startsWith("filename"))) {

          String[] name = filename.split("=");
          String decoded = URLDecoder.decode(
              new String(name[1].trim().substring(1, name[1].trim().length() - 1)
                  .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8),
              StandardCharsets.UTF_8);
          return decoded.replaceAll("^\\.+", "_").replaceAll("[\\\\/:*?\"<>|]", "_")
              .replaceAll("__", "_");
        }
      }
    }
    return "";
  }

  public static String getPart(MultipartFormDataInput multipart, String partName) {

    Map<String, List<InputPart>> uploadForm = multipart.getFormDataMap();

    List<InputPart> foundPart = uploadForm.get(partName);
    if (foundPart != null && !foundPart.isEmpty()) {

      String valueFromParts;
      try {
        valueFromParts = foundPart.get(0).getBodyAsString();
      } catch (IOException e) {
        LOGGER.error("getPart", "getPart caused IOException: ", e);
        throw new InternalServerException("Error reading multipart", e);
      }
      if (!StringUtils.isBlank(valueFromParts)) {
        return valueFromParts;
      }
    }

    return null;
  }

  public static String requirePart(MultipartFormDataInput multipart, String partName) {
    String partValue = getPart(multipart, partName);
    if (StringUtils.isBlank(partValue)) {
      LOGGER.error("requirePart", "Part " + partName + " is required but was not found");
      throw new InternalServerException("Part " + partName + " is required but was not found");
    } else {
      return partValue;
    }
  }

  public static byte[] requireFileContent(MultipartFormDataInput multipart) throws IOException {

    InputStream inputStream = null;

    if (multipart.getParts().size() == 1) {
      InputPart filePart = multipart.getParts().iterator().next();
      inputStream = filePart.getBody(InputStream.class, null);
    } else {
      inputStream = multipart.getFormDataPart("file", InputStream.class, null);
    }

    if (inputStream == null) {
      LOGGER.error("requireFileContent", "Can't find a valid 'file' part in the multipart request");
      throw new IllegalArgumentException("Can't find a valid 'file' part in the multipart request");
    }

    return IOUtils.toByteArray(inputStream);
  }

  /**
   * Metodo che crea una response a partire da un oggetto con contentuto da scaricare
   *
   * @param input DTO contenente le informazioni da scaricare
   * @return response da restituire da un servizio REST
   */
  public static Response toDownloadResponse(FileContent input) {
    return toResponse(input, false);
  }

  /**
   * Metodo che crea una response a partire da un oggetto con contentuto da visualizzare in
   * anteprima
   *
   * @param input DTO contenente le informazioni da visualizzare in anteprima
   * @return response da restituire da un servizio REST
   */
  public static Response toPreviewResponse(FileContent input) {
    return toResponse(input, true);
  }

  private static Response toResponse(FileContent input, boolean preview) {
    if (input == null) {
      throw new NotFoundException("File content not found");
    }

    if (StringUtils.isBlank(input.getFileName())) {
      throw new InvalidParameterException();
    }

    ResponseBuilder response = Response.status(HttpStatus.OK.value()).entity(input.getContent());

    response.header("Content-Disposition",
        (preview ? "inline" : "attachment") + "; filename=" + getFileNameSafe(input.getFileName()));

    if (StringUtils.isNotBlank(input.getMimeType())) {
      response.header("Content-Type", sanitizeMimeType(input.getMimeType()));
    } else {
      response.header("Content-Type", "application/octet-stream");
    }

    return response.build();
  }

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

  public static String getFileNameSafe(String fileName) {
    if (fileName == null || fileName.trim().isEmpty()) {
      return null;
    } else {
      return fileName.trim().replaceAll("[^a-zA-Z0-9_\\.]+", "_");
    }
  }

  /**
   * Metodo per ottenere gli http response header
   *
   * @param link - stringa che rappresenta il link al documento
   * @return una Map contenente tutti i campi header
   */

  public static ResponseHeaderDTO getResponseHeaderInfo(String link) {
    String method = "responseHeaderInfo";
    ResponseHeaderDTO rh = new ResponseHeaderDTO();

    try {
      URL obj = new URL(link);
      HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
      rh.setSize(conn.getContentLengthLong());
      rh.setMimeType(conn.getContentType());
      String filename= conn.getHeaderField("Content-Disposition");
      if (filename == null) {
        filename = UUID.randomUUID().toString();
      } rh.setFilename(filename);
    } catch (Exception e) {
      LOGGER.error(method, "Errore durante la creazione delle info relative al file a partire dal response header", e); }
    return rh;
  }


}
