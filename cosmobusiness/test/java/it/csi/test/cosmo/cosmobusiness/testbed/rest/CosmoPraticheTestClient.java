/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoPraticheFeignClient;
import it.csi.cosmo.cosmopratiche.dto.rest.Attivita;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.CondivisionePratica;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaCondivisionePraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.PaginaTask;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticaInRelazione;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheNoLinkResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.RiassuntoStatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.SimpleForm;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoCaricamentoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoRelazionePraticaPratica;


/**
 *
 */

@Component
public class CosmoPraticheTestClient extends ParentTestClient
implements CosmoPraticheFeignClient {

  @Override
  public void deletePraticheIdPratica(String arg0) {
    notMocked();

  }


  @Override
  public Pratica getPraticheIdPratica(String arg0, Boolean arg1) {
    notMocked();
    return null;
  }

  @Override
  public RiassuntoStatoPratica getPraticheStatoIdPraticaExt(String arg0) { // NOSONAR
    notMocked();
    return null;
  }

  @Override
  public PaginaTask getPraticheTaskIdTaskSubtasks(String arg0) { // NOSONAR
    notMocked();
    return null;
  }

  @Override
  public Pratica postPratiche(CreaPraticaRequest arg0) {
    notMocked();
    return null;
  }

  @Override
  public Pratica putPraticheIdPratica(String arg0, Boolean arg1, Pratica arg2) {
    notMocked();
    return null;
  }

  @Override
  public Pratica getVisibilitaPraticaById(Long arg0) {
    notMocked();
    return null;
  }

  @Override
  public Pratica getVisibilitaPraticaByTask(String arg0) {
    notMocked();
    return null;
  }

  @Override
  public List<StatoPratica> getPraticheStato(String arg0) {
    notMocked();
    return null;
  }

  @Override
  public StoricoPratica getPraticheIdPraticaStorico(Integer arg0) {
    notMocked();
    return null;
  }

  @Override
  public List<Attivita> getAttivitaIdPratica(String arg0) {
    notMocked();
    return null;
  }

  @Override
  public void deletePraticheIdPraticaCondivisioneIdCondivisione(Long arg0, Long arg1) {
    notMocked();
  }

  @Override
  public CondivisionePratica postPraticheIdPraticaCondivisioni(Long arg0,
      CreaCondivisionePraticaRequest arg1) {
    notMocked();
    return null;
  }

  @Override
  public SimpleForm getPraticheTaskIdTaskForm(String arg0) {
    notMocked();
    return null;
  }

  @Override
  public void getPraticheIdPraticaDiagramma(Integer arg0) {
    notMocked();
  }

  @Override
  public List<PraticaInRelazione> getPraticheIdPraticaRelazioni(Integer arg0) {
    notMocked();
    return null;
  }

  @Override
  public List<TipoRelazionePraticaPratica> getPraticheTipiRelazionePraticaPratica() {
    notMocked();
    return null;
  }

  @Override
  public Pratica putPraticheInRelazione(String arg0, String arg1, List<BigDecimal> arg2) {
    notMocked();
    return null;
  }

  @Override
  public PraticheResponse getPratiche(String arg0, Boolean arg1) {
    notMocked();
    return null;
  }


  @Override
  public void postPraticheIdPraticaStorico(Integer arg0, StoricoPraticaRequest arg1) {
    // TODO Auto-generated method stub
    notMocked();
  }


  @Override
  public void deletePraticheFileId(String arg0) {
    // TODO Auto-generated method stub

  }


  @Override
  public CaricamentoPraticheResponse getPraticheFile(String arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> getPraticheFilePath(String arg0) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public StatoCaricamentoPratica getPraticheFileStatiCaricamento() {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public CaricamentoPratica postPraticheFile(CaricamentoPraticaRequest arg0) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public CaricamentoPratica putPraticheFileId(String arg0, CaricamentoPraticaRequest arg1) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public void putPraticheFilePathCancellato(String arg0) {
    // TODO Auto-generated method stub

  }


  @Override
  public CaricamentoPraticheResponse getPraticheFileCaricamentoInBozza(String arg0) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public CaricamentoPraticheResponse getPraticheFileId(String arg0, String arg1, Boolean arg2) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public PraticheNoLinkResponse getPraticheNoLink() {
    // TODO Auto-generated method stub
    return null;
  }
}
