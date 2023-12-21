/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDTipoSegnalibro;
import it.csi.cosmo.cosmoauthorization.business.service.SegnalibroService;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipiSegnalibro;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoDTipoSegnalibroMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDTipoSegnalibroRepository;

/**
 *
 */
@Service
@Transactional
public class SegnalibroServiceImpl implements SegnalibroService {

  @Autowired
  private CosmoDTipoSegnalibroRepository cosmoDTipoSegnalibroRepository;

  @Autowired
  private CosmoDTipoSegnalibroMapper cosmoDTipoSegnalibroMapper;
  @Override
  public TipiSegnalibro getTipiSegnalibri() {
    TipiSegnalibro output = new TipiSegnalibro();

    List<CosmoDTipoSegnalibro> tipi = cosmoDTipoSegnalibroRepository.findAllActive();
    output.setTipiSegnalibro(cosmoDTipoSegnalibroMapper.toDTO(tipi));

    return output;
  }

}
