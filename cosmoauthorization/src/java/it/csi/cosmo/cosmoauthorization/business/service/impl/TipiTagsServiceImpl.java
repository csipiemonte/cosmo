/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDTipoTag;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoauthorization.business.service.TipiTagsService;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoTag;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoDTipoTagMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDTipoTagRepository;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class TipiTagsServiceImpl implements TipiTagsService {

  private static final String CLASS_NAME = TipiTagsServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoDTipoTagRepository cosmoDTipoTagRepository;

  @Autowired
  private CosmoDTipoTagMapper cosmoDTipoTagMapper;

  @Override
  public List<TipoTag> getTipiTags() {
    String methodName = "getTipiTags";
    List<CosmoDTipoTag> tipiTag = cosmoDTipoTagRepository.findAllActive();
    logger.info(methodName, "Tipi tag validi trovati: {}", tipiTag.size());

    return cosmoDTipoTagMapper.toDTOs(tipiTag);
  }

}
