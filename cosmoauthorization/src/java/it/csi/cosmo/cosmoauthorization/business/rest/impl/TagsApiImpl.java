/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoauthorization.business.rest.TagsApi;
import it.csi.cosmo.cosmoauthorization.business.service.TagsService;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaTagRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaTagRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.TagResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.TagsResponse;

/**
 *
 */

public class TagsApiImpl extends ParentApiImpl implements TagsApi {

  @Autowired
  private TagsService tagsService;

  @Override
  public Response deleteTagsId(Long id, SecurityContext securityContext) {
    tagsService.deleteTag(id);
    return Response.noContent().build();
  }

  @Override
  public Response getTags(String filter, SecurityContext securityContext) {
    TagsResponse tagsResponse = tagsService.getTags(filter);
    return Response.ok(tagsResponse).build();
  }

  @Override
  public Response getTagsId(Long id, SecurityContext securityContext) {
    TagResponse tagResponse = tagsService.getTagById(id);
    return Response.ok(tagResponse).build();
  }

  @Override
  public Response postTags(CreaTagRequest body, SecurityContext securityContext) {
    TagResponse tagResponse = tagsService.postTag(body);
    return Response.status(201).entity(tagResponse).build();
  }

  @Override
  public Response putTagsId(Long id, AggiornaTagRequest body, SecurityContext securityContext) {
    TagResponse tagResponse = tagsService.updateTag(id, body);
    return Response.ok(tagResponse).build();
  }

}
