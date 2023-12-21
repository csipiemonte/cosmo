/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.entities.CosmoDEnteCertificatore;
import it.csi.cosmo.common.entities.CosmoDProfiloFeq;
import it.csi.cosmo.common.entities.CosmoDSceltaMarcaTemporale;
import it.csi.cosmo.common.entities.CosmoDTipoCredenzialiFirma;
import it.csi.cosmo.common.entities.CosmoDTipoOtp;
import it.csi.cosmo.cosmoauthorization.business.service.ImpostazioniFirmaService;
import it.csi.cosmo.cosmoauthorization.dto.rest.ImpostazioniFirma;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoDEnteCertificatoreMapper;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoDProfiloFeqMapper;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoDSceltaMarcaTemporaleMapper;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoDTipoCredenzialiFirmaMapper;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoDTipoOtpMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDEnteCertificatoreRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDProfiloFeqRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDSceltaMarcaTemporaleRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDTipoCredenzialiFirmaRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDTipoOtpRepository;

@Service
public class ImpostazioniFirmaServiceImpl implements ImpostazioniFirmaService {

  @Autowired
  private CosmoDEnteCertificatoreRepository cosmoDEnteCertificatoreRepository;

  @Autowired
  private CosmoDEnteCertificatoreMapper cosmoDEnteCertificatoreMapper;

  @Autowired
  private CosmoDTipoCredenzialiFirmaRepository cosmoDTipoCredenzialiFirmaRepository;

  @Autowired
  private CosmoDTipoCredenzialiFirmaMapper cosmoDTipoCredenzialiFirmaMapper;

  @Autowired
  private CosmoDTipoOtpRepository cosmoDTipoOtpRepository;

  @Autowired
  private CosmoDTipoOtpMapper cosmoDTipoOtpMapper;

  @Autowired
  private CosmoDProfiloFeqRepository cosmoDProfiloFeqRepository;

  @Autowired
  private CosmoDProfiloFeqMapper cosmoDProfiloFeqMapper;

  @Autowired
  private CosmoDSceltaMarcaTemporaleRepository cosmoDSceltaMarcaTemporaleRepository;

  @Autowired
  private CosmoDSceltaMarcaTemporaleMapper cosmoDSceltaMarcaTemporaleMapper;

  @Override
  public ImpostazioniFirma getImpostazioniFirma() {
    List<String> orderByFields = new LinkedList<>();
    orderByFields.add("nonValidoInApposizione");
    orderByFields.add("descrizione");
    Sort sortImpostazioniFirma = new Sort(Sort.Direction.ASC, orderByFields);

    List<CosmoDEnteCertificatore> entiCertificatoriRecords = cosmoDEnteCertificatoreRepository.findAllActive(new Sort(Sort.Direction.ASC, "descrizione"));
    List<CosmoDTipoCredenzialiFirma> tipiCredenzialiFirmaRecords = cosmoDTipoCredenzialiFirmaRepository.findAllActive(sortImpostazioniFirma);
    List<CosmoDTipoOtp> tipiOTPRecords = cosmoDTipoOtpRepository.findAllActive(sortImpostazioniFirma);
    List<CosmoDProfiloFeq> profiliFEQRecords = cosmoDProfiloFeqRepository.findAllActive(sortImpostazioniFirma);
    List<CosmoDSceltaMarcaTemporale> scelteMarcaTemporaleRecords = cosmoDSceltaMarcaTemporaleRepository.findAllActive(sortImpostazioniFirma);

    ImpostazioniFirma impostazioniFirma = new ImpostazioniFirma();
    impostazioniFirma.setEntiCertificatori(cosmoDEnteCertificatoreMapper.toDTO(entiCertificatoriRecords));
    impostazioniFirma.setTipiCredenzialiFirma(cosmoDTipoCredenzialiFirmaMapper.toDTO(tipiCredenzialiFirmaRecords));
    impostazioniFirma.setTipiOTP(cosmoDTipoOtpMapper.toDTO(tipiOTPRecords));
    impostazioniFirma.setProfiliFEQ(cosmoDProfiloFeqMapper.toDTO(profiliFEQRecords));
    impostazioniFirma.setScelteMarcaTemporale(cosmoDSceltaMarcaTemporaleMapper.toDTO(scelteMarcaTemporaleRecords));

    return impostazioniFirma;
  }

}
