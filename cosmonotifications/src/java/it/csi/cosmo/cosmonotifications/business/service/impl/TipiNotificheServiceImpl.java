/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.cosmonotifications.business.service.TipiNotificheService;
import it.csi.cosmo.cosmonotifications.dto.rest.TipoNotifica;
import it.csi.cosmo.cosmonotifications.integration.mapper.CosmoDTipoNotificaMapper;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoTipoNotificaRepository;

/**
 *
 */

@Service
public class TipiNotificheServiceImpl implements TipiNotificheService {


  @Autowired
  private CosmoTipoNotificaRepository cosmoDTipoNotificaRepository;

  @Autowired
  private CosmoDTipoNotificaMapper cosmoDTipoNotificaMapper;

  @Override
  public List<TipoNotifica> getTipiNotifiche() {
    return cosmoDTipoNotificaRepository.findAllActive().stream()
        .map(elem -> cosmoDTipoNotificaMapper.toDTO(elem)).collect(Collectors.toList());
  }

}
