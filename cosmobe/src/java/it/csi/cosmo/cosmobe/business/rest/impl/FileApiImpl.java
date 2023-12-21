/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.business.rest.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.fileshare.model.FileUploadResult;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmobe.business.rest.FileApi;
import it.csi.cosmo.cosmobe.business.service.FileShareService;
import it.csi.cosmo.cosmobe.dto.EsitoEnum;
import it.csi.cosmo.cosmobe.dto.rest.CompleteUploadSessionRequest;
import it.csi.cosmo.cosmobe.dto.rest.CreateUploadSessionRequest;
import it.csi.cosmo.cosmobe.dto.rest.FileUploadExcelResult;
import it.csi.cosmo.cosmobe.security.SecurityUtils;

public class FileApiImpl extends ParentApiImpl implements FileApi {

  @Autowired
  private FileShareService fileShareService;


  @Secured("AP_PRAT")
  @Override
  public Response postFileUpload(MultipartFormDataInput input, String xRequestId, String xForwardedFor,
      SecurityContext securityContext) {

    String fileName = "";
    String contentType = "";
    it.csi.cosmo.cosmobe.dto.rest.FileUploadResult output = null;

    Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
    List<InputPart> inputParts = uploadForm.get("payload");

    if (inputParts == null || inputParts.isEmpty()) {
      inputParts = uploadForm.get("file");
    }

    if (inputParts == null || inputParts.isEmpty()) {
      throw new BadRequestException(
          "Nessun contenuto fornito. " + "Accertati di utilizzare il content type 'multipart/form-data' "
              + "e di inviare il contenuto del file utilizzando il name 'payload' oppure 'file'");
    }

    if (inputParts.size() > 1) {
      throw new BadRequestException(
          "Il caricamento di file multipli non e' consentito. " + "Invia un solo file alla volta.");
    }

    for (InputPart inputPart : inputParts) {
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

      FileUploadResult uploadResult = fileShareService.handleUpload(inputStream, fileName, contentType,
          SecurityUtils.getClientCorrente());

      output = new it.csi.cosmo.cosmobe.dto.rest.FileUploadResult();
      output.setUploadUUID(uploadResult.getMetadata().getFileUUID());
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

  @Secured("AP_PRAT")
  @Override
  public Response createUploadSession(String xRequestId, String xForwardedFor, CreateUploadSessionRequest body,
      SecurityContext securityContext) {

    //@formatter:off
    var req = it.csi.cosmo.common.fileshare.model.CreateUploadSessionRequest.builder()
        .withFileName(body.getFileName())
        .withMimeType(body.getMimeType())
        .withSize(body.getSize())
        .build();
    //@formatter:on

    var res = fileShareService.createUploadSession(req);

    var mapped = new it.csi.cosmo.cosmobe.dto.rest.CreateUploadSessionResponse();
    mapped.setSessionUUID(res.getSessionUUID());

    return Response.status(201).entity(mapped).build();
  }

  @Secured("AP_PRAT")
  @Override
  public Response uploadSessionPart(MultipartFormDataInput input, String sessionUUID, Long partNumber,
      String xRequestId, String xForwardedFor, SecurityContext securityContext) {

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

      fileShareService.handPartUpload(sessionUUID, partNumber, inputStream, SecurityUtils.getClientCorrente());
    }

    return Response.noContent().build();
  }

  @Secured("AP_PRAT")
  @Override
  public Response completeUploadSession(String sessionUUID, CompleteUploadSessionRequest body, String xRequestId,
      String xForwardedFor, SecurityContext securityContext) {

    //@formatter:off
    var req = it.csi.cosmo.common.fileshare.model.CompleteUploadSessionRequest.builder()
        .withSessionUUID(sessionUUID)
        .withHashes(body.getHashes())
        .build();
    //@formatter:on

    var res = fileShareService.completeUploadSession(req);

    var mapped = new it.csi.cosmo.cosmobe.dto.rest.CompleteUploadSessionResponse();
    mapped.setUploadUUID(res.getMetadata().getSessionUUID());

    return Response.status(201).entity(mapped).build();
  }

  @Secured("AP_PRAT")
  @Override
  public Response cancelUploadSession(String sessionUUID, String xRequestId, String xForwardedFor,
      SecurityContext securityContext) {

    fileShareService.cancelUploadSession(sessionUUID);

    return Response.noContent().build();
  }

  @Override
  public Response postUploadFileUtenti(MultipartFormDataInput input, String xRequestId,
      String xForwardedFor,
      SecurityContext securityContext) {

    String fileName = "";
    FileUploadExcelResult uploadResult = new FileUploadExcelResult();

    Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
    List<InputPart> inputParts = uploadForm.get("payload");

    if (inputParts == null || inputParts.isEmpty()) {
      inputParts = uploadForm.get("file");
    }

    if (inputParts == null || inputParts.isEmpty()) {
      throw new BadRequestException(
          "Nessun contenuto fornito. " + "Accertati di utilizzare il content type 'multipart/form-data' "
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

        boolean validated = fileShareService.validateExcel(inputPart.getBody(InputStream.class, null));
        fileShareService.handleUploadExcel(inputPart.getBody(InputStream.class, null), fileName);

        EsitoEnum esitoEnum = validated?EsitoEnum.CON_VALIDAZIONE:EsitoEnum.SENZA_VALIDAZIONE;
        uploadResult.setEsito(esitoEnum.toString());

      } catch (IOException e) {
        throw new InternalServerException("Error reading request stream", e);
      }

    }

    return Response.ok(uploadResult).build();

  }

}
