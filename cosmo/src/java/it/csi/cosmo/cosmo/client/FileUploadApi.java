/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import it.csi.cosmo.cosmo.dto.rest.fileshare.FileUploadResponse;


@Path("/file")
public interface FileUploadApi  {

  @POST
  @Path("/file/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces({"application/json"})
  public FileUploadResponse upload();

}
