/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.testbed.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDFormatoFile;
import it.csi.cosmo.common.entities.CosmoDFormatoFile_;
import it.csi.cosmo.common.entities.CosmoDStatoDocumento;
import it.csi.cosmo.common.entities.CosmoDStatoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoRSmistamentoDocumento;
import it.csi.cosmo.common.entities.CosmoRTipodocTipopratica;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDFormatoFileRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDStatoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRSmistamentoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTContenutoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoDFormatoFileTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoDStatoDocumentoTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoDTipoContenutoDocumentoTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoDTipoDocumentoTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoDTipoPraticaTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoTEnteTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.repository.CosmoTPraticaTestbedRepository;
import it.csi.test.cosmo.cosmoecm.testbed.service.StardasTestbedService;

/**
 *
 */
@Service
@Transactional
public class StardasTestbedServiceImpl implements StardasTestbedService {

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

  @Autowired
  private CosmoRSmistamentoDocumentoRepository cosmoRSmistamentoDocumentoRepository;

  private static final String CF_ENTE = "97603810017";

  private static Timestamp now = Timestamp.valueOf(LocalDateTime.now());

  @Override
  public CosmoTPratica salvaPratica(boolean salvaDocumento) throws IOException {

    // tipo documento
    var tipoDocumento =
        tipoDocumentoRepository.findOneActiveByField(CosmoDTipoDocumento_.codice, "doc_stardas")
        .orElseGet(this::createCosmoDTipoDocumento);

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

    if (salvaDocumento) {
      // relazione documento <-> pratica
      var relazioneTipoDocTipoPratica = new CosmoRTipodocTipopratica();
      relazioneTipoDocTipoPratica.setCosmoDTipoPratica(tipoPratica);
      relazioneTipoDocTipoPratica.setCosmoDTipoDocumento(tipoDocumento);
      relazioneTipoDocTipoPratica.setDtInizioVal(now);

      var relazioniDocPratica = new ArrayList<CosmoRTipodocTipopratica>();
      relazioniDocPratica.add(relazioneTipoDocTipoPratica);
      tipoPratica.setCosmoRTipodocTipopraticas(relazioniDocPratica);

      // tipo contenuto
      var tipoContenuto = new CosmoDTipoContenutoDocumento();
      tipoContenuto.setCodice(TipoContenutoDocumento.ORIGINALE.toString());
      tipoContenuto.setDtInizioVal(now);
      tipoContenuto = tipoContenutoDocumentoRepository.saveAndFlush(tipoContenuto);

      // formato file
      var formato =
          formatoFileRepository.findOneActiveByField(CosmoDFormatoFile_.codice,
              "pdf")
          .orElse(createCosmoDFormatoFile());

      // stato documento
      var statoDocumento =
          statoDocumentoRepository.findOneActiveByField(CosmoDStatoDocumento_.codice, "stato_test")
          .orElse(createCosmoDStatoDocumento());

      // documento
      var documento = new CosmoTDocumento();
      documento.setUtenteInserimento("TEST_USER");
      documento.setDtInserimento(now);
      documento.setStato(statoDocumento);
      documento.setPratica(pratica);
      documento.setTipo(tipoDocumento);
      var contenuti = new ArrayList<CosmoTContenutoDocumento>();
      documento.setContenuti(contenuti);
      documento = documentoRepository.saveAndFlush(documento);

      var documenti = new ArrayList<CosmoTDocumento>();
      documenti.add(documento);
      pratica.setDocumenti(documenti);
      // contenuto documento
      Entity documentoIndex = buildEntity();
      var contenutoDocumento = new CosmoTContenutoDocumento();
      contenutoDocumento.setTipo(tipoContenuto);
      contenutoDocumento.setFormatoFile(formato);
      contenutoDocumento.setUuidNodo(documentoIndex.getNodeUUID());
      contenutoDocumento.setNomeFile(documentoIndex.getFilename());
      contenutoDocumento.setDtInserimento(now);
      contenutoDocumento.setDocumentoPadre(documento);
      contenuti.add(contenutoDocumentoRepository.saveAndFlush(contenutoDocumento));
    }

    return pratica;
  }

  private CosmoDTipoDocumento createCosmoDTipoDocumento() {
    var tipoDocumentoInt = new CosmoDTipoDocumento();
    tipoDocumentoInt.setCodice("TEST");
    tipoDocumentoInt.setCodiceStardas("MOD_DIP"); // serve per smistamento
    tipoDocumentoInt.setFirmabile(false);
    tipoDocumentoInt.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
    return tipoDocumentoTestbedRepository.saveAndFlush(tipoDocumentoInt);

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

  private CosmoDFormatoFile createCosmoDFormatoFile() {
    CosmoDFormatoFile formatoInt = new CosmoDFormatoFile();
    formatoInt.setCodice("formato_test");
    formatoInt.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
    formatoInt.setSupportaAnteprima(false);
    formatoInt.setSupportaSbustamento(false);
    formatoInt.setUploadConsentito(false);
    formatoInt.setMimeType("application/pdf");
    return formatoFileTestbedRepository.saveAndFlush(formatoInt);
  }

  private CosmoTEnte createCosmoTEnte() {
    var enteInt = new CosmoTEnte();
    enteInt.setCodiceFiscale(CF_ENTE);
    enteInt.setCodiceIpa("test");
    enteInt.setNome("CF_ENTE");
    enteInt.setLogo(new byte[] {-98, 12, 35});
    return enteRepository.saveAndFlush(enteInt);
  }

  private CosmoDStatoDocumento createCosmoDStatoDocumento() {
    CosmoDStatoDocumento statoDocumentoInt = new CosmoDStatoDocumento();
    statoDocumentoInt.setCodice("stato_test");
    statoDocumentoInt.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
    return statoDocumentoTestbedRepository.saveAndFlush(statoDocumentoInt);
  }

  private Entity buildEntity() throws IOException {
    Entity documento = new Entity();

    documento.setFilename("pdf-extracted.pdf");
    documento
    .setMimeType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    InputStream is =
        new ClassPathResource("/samplefiles/pdf-extracted.pdf").getInputStream();
    documento.setContent(is.readAllBytes());
    documento.setDescrizione("smistamento a stardas - documento 1");
    documento.setIdDocumento(1L);
    documento.setTipoDocumento("P");
    documento.setNodeUUID(UUID.randomUUID().toString());

    return documento;
  }

  @Override
  public CosmoTPratica recuperaPratica(Long id) {
    return praticaRepository.findOne(id);
  }

  @Override
  public void aggiornaDocumento(CosmoTDocumento documento) {
    documentoRepository.save(documento);
  }

  @Override
  public void salvaRelazioneSmistamento(CosmoRSmistamentoDocumento relazioneSmistamento) {
    cosmoRSmistamentoDocumentoRepository.saveAndFlush(relazioneSmistamento);
  }



  private CosmoTPratica createCosmoTPratica() {
    var pratica = new CosmoTPratica();
    pratica.setDtInserimento(now);
    pratica.setDataCreazionePratica(now);
    pratica.setUtenteCreazionePratica("DVNLRD99A01L219J");// serve per smistamento
    pratica.setOggetto("pratica_test");
    return pratica;
  }

  @Override
  public void deletePratica(Long id) {
    praticaRepository.delete(id);

  }

}
