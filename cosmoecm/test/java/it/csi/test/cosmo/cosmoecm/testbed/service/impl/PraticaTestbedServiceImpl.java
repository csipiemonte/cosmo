/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.testbed.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDFormatoFileRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDStatoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTContenutoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoDFormatoFileTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoDStatoDocumentoTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoDTipoContenutoDocumentoTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoDTipoDocumentoTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoDTipoPraticaTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoTEnteTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoTPraticaTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.service.PraticaTestbedService;

/**
 *
 */
@Service
@Transactional
public class PraticaTestbedServiceImpl implements PraticaTestbedService {

  @Autowired
  CosmoTPraticaTestbedRepository praticaRepository;

  @Autowired
  CosmoDTipoDocumentoTestbedRepository tipoDocumentoTestbedRepository;

  @Autowired
  CosmoDTipoDocumentoRepository tipoDocumentoRepository;

  @Autowired
  CosmoTContenutoDocumentoRepository contenutoDocumentoRepository;

  @Autowired
  CosmoDStatoDocumentoRepository statoDocumentoRepository;

  @Autowired
  CosmoDStatoDocumentoTestbedRepository statoDocumentoTestbedRepository;

  @Autowired
  CosmoTDocumentoRepository documentoRepository;

  @Autowired
  CosmoDTipoPraticaRepository tipoPraticaRepository;

  @Autowired
  CosmoDTipoPraticaTestbedRepository tipoPraticaTestbedRepository;

  @Autowired
  CosmoDFormatoFileTestbedRepository formatoFileTestbedRepository;

  @Autowired
  CosmoDFormatoFileRepository formatoFileRepository;

  @Autowired
  CosmoDTipoContenutoDocumentoTestbedRepository tipoContenutoDocumentoRepository;

  @Autowired
  CosmoTEnteTestbedRepository enteRepository;

  private static final String CF_ENTE = "97603810017";

  private static Timestamp now = Timestamp.valueOf(LocalDateTime.now());

  @Override
  public CosmoTPratica salvaPratica() throws IOException {

    // tipo pratica
    var tipoPratica = Optional.ofNullable(tipoPraticaRepository.findOne("ITER_APPFIR_CRP"))
        .orElseGet(this::createCosmoCTipoPratica);

    // ente
    var ente = enteRepository.findOneNotDeletedByField(CosmoTEnte_.codiceFiscale, CF_ENTE)
        .orElseGet(this::createCosmoTEnte);

    // pratica
    var pratica = this.createCosmoTPratica();
    pratica.setTipo(tipoPratica);
    pratica.setEnte(ente);
    pratica = praticaRepository.saveAndFlush(pratica);


    return pratica;
  }

  private CosmoDTipoPratica createCosmoCTipoPratica() {
    var tipoPraticaInt = new CosmoDTipoPratica();
    var ente = new CosmoTEnte();
    ente.setId(1L);
    tipoPraticaInt.setCodice("ITER_APPFIR_CRP");
    tipoPraticaInt.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
    tipoPraticaInt.setProcessDefinitionKey("ITER_APPFIR_CRP");
    tipoPraticaInt.setCosmoTEnte(ente);
    tipoPraticaInt.setCreabileDaInterfaccia(true);
    tipoPraticaInt.setCreabileDaServizio(true);
    tipoPraticaInt.setAnnullabile(true);
    tipoPraticaInt.setCondivisibile(true);
    tipoPraticaInt.setAssegnabile(true);
    tipoPraticaInt.setOverrideFruitoreDefault(false);
    tipoPraticaInt.setOverrideResponsabileTrattamento(false);
    return tipoPraticaTestbedRepository.saveAndFlush(tipoPraticaInt);
  }

  private CosmoTEnte createCosmoTEnte() {
    var enteInt = new CosmoTEnte();
    enteInt.setCodiceFiscale(CF_ENTE);
    enteInt.setCodiceIpa("test");
    enteInt.setNome("CF_ENTE");
    enteInt.setLogo(new byte[] {-98, 12, 35});
    return enteRepository.saveAndFlush(enteInt);
  }

  private CosmoTPratica createCosmoTPratica() {
    var pratica = new CosmoTPratica();
    pratica.setDtInserimento(now);
    pratica.setDataCreazionePratica(now);
    pratica.setUtenteCreazionePratica("DVNLRD99A01L219J");// serve per smistamento
    pratica.setOggetto("pratica_test");
    return pratica;
  }

}
