/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.util.Base64;
import javax.swing.ImageIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoCConfigurazione;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTPreferenzeEnte;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoauthorization.business.service.PreferenzeEnteService;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.config.ParametriApplicativo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTPreferenzeEnteMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoCConfigurazioneRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTPreferenzeEnteRepository;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;

@Service
public class PreferenzeEnteServiceImpl implements PreferenzeEnteService {

  private static final String CLASS_NAME = PreferenzeEnteServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTPreferenzeEnteRepository cosmoTPreferenzeEnteRepository;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoTPreferenzeEnteMapper cosmoTPreferenzeEnteMapper;

  @Autowired
  private CosmoCConfigurazioneRepository cosmoCConfigurazioneRepository;

  @Override
  public Preferenza getPreferenzeEnte(Integer idEnte, String versione) {
    final String methodName = "getPreferenzeEnte";
    CosmoTEnte cosmoTEnte = cosmoTEnteRepository.findOne(idEnte.longValue());
    if (null == cosmoTEnte) {
      logger.error(methodName, String.format(ErrorMessages.E_ENTE_NON_TROVATO, idEnte));
      throw new NotFoundException(String.format(ErrorMessages.E_ENTE_NON_TROVATO, idEnte));
    }
    CosmoTPreferenzeEnte preferenzeEnte =
        cosmoTPreferenzeEnteRepository.findBycosmoTEnteAndVersione(cosmoTEnte, versione);

    return cosmoTPreferenzeEnteMapper.toDTO(preferenzeEnte);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Preferenza updatePreferenzeEnte(Integer idEnte, Preferenza preferenzeEnte) {
    final String methodName = "updatePreferenzeEnte";
    CosmoTEnte cosmoTEnte =
        cosmoTEnteRepository.findOne(idEnte.longValue());
    if (null == cosmoTEnte) {
      logger.error(methodName,
          String.format(ErrorMessages.E_ENTE_NON_TROVATO, idEnte));
      throw new NotFoundException(
          String.format(ErrorMessages.E_ENTE_NON_TROVATO, idEnte));
    }
    CosmoTPreferenzeEnte preferenzeEnteDaAggiornare = cosmoTPreferenzeEnteRepository
        .findBycosmoTEnteAndVersione(cosmoTEnte, preferenzeEnte.getVersione());

    if (preferenzeEnteDaAggiornare == null) {
      logger.error(methodName, String.format(
          ErrorMessages.PE_PREFERENZE_VERSIONE_ENTE_NON_TROVATE, preferenzeEnte.getVersione()));
      throw new NotFoundException(String.format(
          ErrorMessages.PE_PREFERENZE_VERSIONE_ENTE_NON_TROVATE, preferenzeEnte.getVersione()));
    }
    checkLogo(methodName, preferenzeEnte);
    checkIconaFea(methodName, preferenzeEnte);
    preferenzeEnteDaAggiornare
    .setValore(cosmoTPreferenzeEnteMapper.toRecord(preferenzeEnte).getValore());
    preferenzeEnteDaAggiornare = cosmoTPreferenzeEnteRepository.save(preferenzeEnteDaAggiornare);
    return cosmoTPreferenzeEnteMapper.toDTO(preferenzeEnteDaAggiornare);
  }

  @Override
  public Preferenza createPreferenzeEnte(Integer idEnte, Preferenza preferenzeEnte) {
    final String methodName = "createPreferenzeEnte";
    CosmoTEnte cosmoTEnte = cosmoTEnteRepository.findOne(idEnte.longValue());
    if (null == cosmoTEnte) {
      logger.error(methodName, String.format(ErrorMessages.E_ENTE_NON_TROVATO, idEnte));
      throw new NotFoundException(String.format(ErrorMessages.E_ENTE_NON_TROVATO, idEnte));
    }
    CosmoTPreferenzeEnte preferenzeEnteDaSalvare = cosmoTPreferenzeEnteRepository
        .findBycosmoTEnteAndVersione(cosmoTEnte, preferenzeEnte.getVersione());

    if (preferenzeEnteDaSalvare != null) {
      logger.error(methodName,
          String.format(ErrorMessages.PE_PREFERENZE_VERSIONE_ENTE_GIA_ESISTENTI,
              preferenzeEnteDaSalvare.getVersione()));
      throw new ConflictException(
          String.format(ErrorMessages.PE_PREFERENZE_VERSIONE_ENTE_GIA_ESISTENTI,
              preferenzeEnteDaSalvare.getVersione()));
    }
    checkLogo(methodName, preferenzeEnte);
    checkIconaFea(methodName, preferenzeEnte);
    preferenzeEnteDaSalvare = cosmoTPreferenzeEnteMapper.toRecord(preferenzeEnte);
    preferenzeEnteDaSalvare.setCosmoTEnte(cosmoTEnte);
    // preferenzeEnteDaSalvare.setDtInizioVal(new Timestamp(System.currentTimeMillis()));
    preferenzeEnteDaSalvare = cosmoTPreferenzeEnteRepository.save(preferenzeEnteDaSalvare);

    return cosmoTPreferenzeEnteMapper.toDTO(preferenzeEnteDaSalvare);
  }

  /**
   * Verifica che sia per il logo dell'header che per il logo del footer in inserendo/aggiornando
   * sulle preferenze ente, la grandezza e il numero di pixels rientri nel range previsto
   * dall'ultima versione, presente sulla tabella delle configurazioni
   *
   * @param: methodName
   * @param: preferenzeEnte
   */
  private void checkLogo(String methodName, Preferenza preferenzeEnte) {
    CosmoCConfigurazione confLogoMaxSize = cosmoCConfigurazioneRepository.findOne(ParametriApplicativo.MAXSIZE_PREF_ENTE.getCodice());
    if (null == confLogoMaxSize || null == confLogoMaxSize.getValore()) {
      logger.error(methodName,
          String.format(ErrorMessages.C_CONFIGURAZIONE_DECODED_NON_TROVATA, ParametriApplicativo.MAXSIZE_PREF_ENTE.getCodice()));
      throw new NotFoundException(
          String.format(ErrorMessages.C_CONFIGURAZIONE_DECODED_NON_TROVATA, ParametriApplicativo.MAXSIZE_PREF_ENTE.getCodice()));
    }
    CosmoCConfigurazione confLogoMaxPixels = cosmoCConfigurazioneRepository.findOne(ParametriApplicativo.MAXPIX_PREF_ENTE.getCodice());
    if (null == confLogoMaxPixels || null == confLogoMaxPixels.getValore()) {
      logger.error(methodName,
          String.format(ErrorMessages.C_CONFIGURAZIONE_DECODED_NON_TROVATA, ParametriApplicativo.MAXPIX_PREF_ENTE.getCodice()));
      throw new NotFoundException(
          String.format(ErrorMessages.C_CONFIGURAZIONE_DECODED_NON_TROVATA, ParametriApplicativo.MAXPIX_PREF_ENTE.getCodice()));
    }
    byte[] decodeLogo = null;
    try {
      decodeLogo = Base64.getDecoder()
          .decode(cosmoTPreferenzeEnteMapper.toRecord(preferenzeEnte).getValore().getLogo());
    } catch (NullPointerException e) {
      logger.error(methodName,
          ErrorMessages.PE_PREFERENZE_CONVERSIONE_LOGO_NON_RIUSCITA);
      throw new BadRequestException(
          ErrorMessages.PE_PREFERENZE_CONVERSIONE_LOGO_NON_RIUSCITA);
    }
    if (decodeLogo.length > Integer.valueOf(confLogoMaxSize.getValore())) {
      logger.error(methodName,
          String.format(ErrorMessages.PE_PREFERENZE_GRANDEZZA_LOGO_SUPERATA, Integer.valueOf(confLogoMaxSize.getValore()) / 1000));
      throw new BadRequestException(
          String.format(ErrorMessages.PE_PREFERENZE_GRANDEZZA_LOGO_SUPERATA, Integer.valueOf(confLogoMaxSize.getValore()) / 1000));
    }
    ImageIcon logoImgIcon = new ImageIcon(decodeLogo);
    if (logoImgIcon.getIconHeight() > Integer.valueOf(confLogoMaxPixels.getValore())
        || logoImgIcon.getIconWidth() > Integer.valueOf(confLogoMaxPixels.getValore())) {
      logger.error(methodName,
          String.format(ErrorMessages.PE_PREFERENZE_GRANDEZZA_PIXELS_LOGO_SUPERATA,
              Integer.valueOf(confLogoMaxPixels.getValore())));
      throw new BadRequestException(
          String.format(ErrorMessages.PE_PREFERENZE_GRANDEZZA_PIXELS_LOGO_SUPERATA, confLogoMaxPixels.getValore()));
    }


    var logoFooter =
        cosmoTPreferenzeEnteMapper.toRecord(preferenzeEnte).getValore().getLogoFooter();
    if (logoFooter != null && !logoFooter.isEmpty()) {
      checkLogoFormat(methodName, logoFooter, confLogoMaxSize, confLogoMaxPixels);
    }

    var logoFooterCentrale =
        cosmoTPreferenzeEnteMapper.toRecord(preferenzeEnte).getValore().getLogoFooterCentrale();
    if (logoFooterCentrale != null && !logoFooterCentrale.isEmpty()) {
      checkLogoFormat(methodName, logoFooterCentrale, confLogoMaxSize, confLogoMaxPixels);
    }

    var logoFooterDestra =
        cosmoTPreferenzeEnteMapper.toRecord(preferenzeEnte).getValore().getLogoFooterDestra();
    if (logoFooterDestra != null && !logoFooterDestra.isEmpty()) {
      checkLogoFormat(methodName, logoFooterDestra, confLogoMaxSize, confLogoMaxPixels);
    }


  }

  private void checkLogoFormat(String methodName, String logo, CosmoCConfigurazione confLogoMaxSize,
      CosmoCConfigurazione confLogoMaxPixels) {
    byte[] decodeLogo = null;
    try {
      decodeLogo = Base64.getDecoder().decode(logo);
    } catch (NullPointerException e) {
      logger.error(methodName, ErrorMessages.PE_PREFERENZE_CONVERSIONE_LOGO_NON_RIUSCITA);
      throw new BadRequestException(ErrorMessages.PE_PREFERENZE_CONVERSIONE_LOGO_NON_RIUSCITA);
    }
    if (decodeLogo.length > Integer.valueOf(confLogoMaxSize.getValore())) {
      logger.error(methodName, String.format(ErrorMessages.PE_PREFERENZE_GRANDEZZA_LOGO_SUPERATA,
          Integer.valueOf(confLogoMaxSize.getValore()) / 1000));
      throw new BadRequestException(
          String.format(ErrorMessages.PE_PREFERENZE_GRANDEZZA_LOGO_SUPERATA,
              Integer.valueOf(confLogoMaxSize.getValore()) / 1000));
    }
    ImageIcon logoImgIcon = new ImageIcon(decodeLogo);
    if (logoImgIcon.getIconHeight() > Integer.valueOf(confLogoMaxPixels.getValore())
        || logoImgIcon.getIconWidth() > Integer.valueOf(confLogoMaxPixels.getValore())) {
      logger.error(methodName,
          String.format(ErrorMessages.PE_PREFERENZE_GRANDEZZA_PIXELS_LOGO_SUPERATA,
              Integer.valueOf(confLogoMaxPixels.getValore())));
      throw new BadRequestException(
          String.format(ErrorMessages.PE_PREFERENZE_GRANDEZZA_PIXELS_LOGO_SUPERATA,
              confLogoMaxPixels.getValore()));
    }
  }

  private void checkIconaFea(String methodName, Preferenza preferenzaEnte) {
    CosmoCConfigurazione confFeaMaxSize = cosmoCConfigurazioneRepository
        .findOne(ParametriApplicativo.MAXSIZE_PREF_ENTE_FEA.getCodice());
    if (null == confFeaMaxSize || null == confFeaMaxSize.getValore()) {
      logger.error(methodName, String.format(ErrorMessages.C_CONFIGURAZIONE_DECODED_NON_TROVATA,
          ParametriApplicativo.MAXSIZE_PREF_ENTE_FEA.getCodice()));
      throw new NotFoundException(String.format(ErrorMessages.C_CONFIGURAZIONE_DECODED_NON_TROVATA,
          ParametriApplicativo.MAXSIZE_PREF_ENTE_FEA.getCodice()));
    }
    CosmoCConfigurazione confFeaMaxPixels = cosmoCConfigurazioneRepository
        .findOne(ParametriApplicativo.MAXPIX_PREF_ENTE_FEA.getCodice());
    if (null == confFeaMaxPixels || null == confFeaMaxPixels.getValore()) {
      logger.error(methodName, String.format(ErrorMessages.C_CONFIGURAZIONE_DECODED_NON_TROVATA,
          ParametriApplicativo.MAXPIX_PREF_ENTE_FEA.getCodice()));
      throw new NotFoundException(String.format(ErrorMessages.C_CONFIGURAZIONE_DECODED_NON_TROVATA,
          ParametriApplicativo.MAXPIX_PREF_ENTE_FEA.getCodice()));
    }
    byte[] decodeLogo = null;
    try {
      decodeLogo = Base64.getDecoder()
          .decode(cosmoTPreferenzeEnteMapper.toRecord(preferenzaEnte).getValore().getIconaFea());
    } catch (NullPointerException e) {
      logger.error(methodName, ErrorMessages.PE_PREFERENZE_CONVERSIONE_ICONA_FEA_NON_RIUSCITA);
      throw new BadRequestException(ErrorMessages.PE_PREFERENZE_CONVERSIONE_ICONA_FEA_NON_RIUSCITA);
    }
    if (decodeLogo.length > Integer.valueOf(confFeaMaxSize.getValore())) {
      logger.error(methodName,
          String.format(ErrorMessages.PE_PREFERENZE_GRANDEZZA_ICONA_FEA_SUPERATA,
          Integer.valueOf(confFeaMaxSize.getValore()) / 1000));
      throw new BadRequestException(
          String.format(ErrorMessages.PE_PREFERENZE_GRANDEZZA_ICONA_FEA_SUPERATA,
              Integer.valueOf(confFeaMaxSize.getValore()) / 1000));
    }
    ImageIcon logoImgIcon = new ImageIcon(decodeLogo);
    if (logoImgIcon.getIconHeight() > Integer.valueOf(confFeaMaxPixels.getValore())
        || logoImgIcon.getIconWidth() > Integer.valueOf(confFeaMaxPixels.getValore())) {
      logger.error(methodName,
          String.format(ErrorMessages.PE_PREFERENZE_GRANDEZZA_PIXELS_ICONA_FEA_SUPERATA,
              Integer.valueOf(confFeaMaxPixels.getValore())));
      throw new BadRequestException(
          String.format(ErrorMessages.PE_PREFERENZE_GRANDEZZA_PIXELS_ICONA_FEA_SUPERATA,
              confFeaMaxPixels.getValore()));
    }
  }
}
