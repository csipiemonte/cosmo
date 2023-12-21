/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.business.rest.impl;

import java.io.IOException;
import java.io.OutputStream;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmobe.business.rest.TestApi;
import it.csi.cosmo.cosmobe.business.service.FileShareService;



public class TestApiImpl extends ParentApiImpl implements TestApi {

  @Autowired
  private FileShareService fileShareService;

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

}
