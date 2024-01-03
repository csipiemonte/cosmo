/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTOtp;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.cosmoecm.business.service.OtpService;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTOtpRepository;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
@Transactional
public class OtpServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private OtpService otpService;

  @Autowired
  private CosmoTOtpRepository cosmoTOtpRepository;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }


  @Test(expected = BadRequestException.class)
  public void richiediOtp() {
    otpService.richiediOtp();
  }

  @Test(expected = BadRequestException.class)
  public void richiediOtpEsistenteUtilizzato() {
    var otp = new CosmoTOtp();
    otp.setValore("valore");
    CosmoTUtente utente = new CosmoTUtente();
    utente.setId(1L);
    otp.setIdUtente(utente);
    otp.setDtScadenza(Timestamp.from(Instant.now()));
    otp.setDtInserimento(Timestamp.from(Instant.now()));
    otp.setUtenteInserimento("test");
    CosmoTEnte ente = new CosmoTEnte();
    ente.setId(1L);
    otp.setCosmoTEnte(ente);
    otp.setUtilizzato(true);
    cosmoTOtpRepository.saveAndFlush(otp);
    otpService.richiediOtp();
  }

  @Test(expected = BadRequestException.class)
  public void richiediOtpEsistenteNonUtilizzato() {
    var otp = new CosmoTOtp();
    otp.setValore("valore");
    CosmoTUtente utente = new CosmoTUtente();
    utente.setId(1L);
    otp.setIdUtente(utente);
    otp.setDtScadenza(Timestamp.from(Instant.now()));
    otp.setDtInserimento(Timestamp.from(Instant.now()));
    otp.setUtenteInserimento("test");
    CosmoTEnte ente = new CosmoTEnte();
    ente.setId(1L);
    otp.setCosmoTEnte(ente);
    otp.setUtilizzato(false);
    cosmoTOtpRepository.saveAndFlush(otp);
    otpService.richiediOtp();
  }

  @Test
  public void creaOtp() {
    otpService.createOtp();
  }



}
