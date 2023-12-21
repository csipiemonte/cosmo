/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.service.impl;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.CategoriaUseCaseDTO;
import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.common.security.model.GruppoDTO;
import it.csi.cosmo.common.security.model.IdentitaDTO;
import it.csi.cosmo.common.security.model.ProfiloDTO;
import it.csi.cosmo.common.security.model.UseCaseDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmo.business.service.ProfilazioneService;
import it.csi.cosmo.cosmo.integration.rest.CosmoAuthorizationUtentiClient;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;
import it.csi.cosmo.cosmoauthorization.dto.rest.AssociazioneEnteUtente;
import it.csi.cosmo.cosmoauthorization.dto.rest.AssociazioneUtenteProfilo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Gruppo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Profilo;
import it.csi.cosmo.cosmoauthorization.dto.rest.UseCase;
import it.csi.cosmo.cosmoauthorization.dto.rest.Utente;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtenteResponse;

/**
 * Implementazione del servizio per la gestione dell'utente, della profilazione e della sessione
 */
@Service
@Transactional
public class ProfilazioneServiceImpl implements ProfilazioneService {

  private static final String SYSTEM = "SYSTEM";

  private static final String GUEST = "GUEST";

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "ProfilazioneServiceImpl");

  //@formatter:off
  private static UserInfoDTO utenteAnonimo = UserInfoDTO.builder ()
      .withNome ( GUEST )
      .withCognome ( GUEST )
      .withCodiceFiscale ( GUEST )
      .withAnonimo ( true )
      .build ();
  //@formatter:on

  //@formatter:off
  private static UserInfoDTO utenteSistema = UserInfoDTO.builder ()
      .withNome ( SYSTEM )
      .withCognome ( SYSTEM )
      .withCodiceFiscale ( SYSTEM )
      .withAnonimo ( true )
      .build ();
  //@formatter:on

  public static UserInfoDTO getUtenteAnonimo() {
    return utenteAnonimo;
  }

  public static UserInfoDTO getUtenteSistema() {
    return utenteSistema;
  }

  @Autowired
  public CosmoAuthorizationUtentiClient authorizationUtentiClient;

  @Override
  public UserInfoDTO caricaUserInfo(HttpServletRequest request, IdentitaDTO identita, Long idEnte,
      Long idProfilo) {

    String methodName = "caricaUserInfo";
    logger.debug(methodName, "carico user info");

    boolean enteSelezionato = idEnte != null;
    boolean profiloSelezionato = idProfilo != null;

    Utente utente = getUtenteFromCodiceFiscale(identita.getCodFiscale());

    AssociazioneEnteUtente associazioneEnte =
        enteSelezionato ? getEnteSelezionatoFromUtente(utente, idEnte) : null;

    Profilo profilo =
        profiloSelezionato ? getProfiloSelezionatoFromUtente(utente, associazioneEnte, idProfilo)
            : null;

    List<Gruppo> gruppi =
        associazioneEnte != null ? getGruppiPerEnteFromUtente(utente, idEnte) : null;

    //@formatter:off
            return map(utente, associazioneEnte, profilo, gruppi)
                .withIdentita(identita)
                .build();
            //@formatter:on
  }

  private OffsetDateTime getFineValidita(AssociazioneEnteUtente associazioneEnte, Profilo profilo) {

    List<OffsetDateTime> candidates = new ArrayList<>();

    /*
     * if (associazioneEnte != null) {
     * candidates.add(associazioneEnte.getCampiTecnici().getDtFineVal()); }
     * 
     * if (associazioneEnte != null && associazioneEnte.getEnte() != null) {
     * candidates.add(associazioneEnte.getEnte().getCampiTecnici().getDtFineVal()); }
     */
    if (profilo != null) {
      candidates.add(getFineValiditaProfilo(profilo));
    }

    return candidates.stream().filter(Objects::nonNull).sorted().findFirst().orElse(null);
  }

  private OffsetDateTime getFineValiditaProfilo(Profilo profilo) {

    List<OffsetDateTime> candidates = new ArrayList<>();

    if (profilo != null) {
      /*
       * if (profilo.getCampiTecnici() != null) {
       * candidates.add(profilo.getCampiTecnici().getDtFineVal()); }
       * 
       * for (UseCase uc : safe(profilo.getUseCases())) { if (uc != null && uc.getCampiTecnici() !=
       * null) { candidates.add(uc.getCampiTecnici().getDtFineVal()); }
       * 
       * if (uc != null && uc.getCodiceCategoria() != null) {
       * candidates.add(uc.getCodiceCategoria().getCampiTecnici().getDtFineVal()); } }
       */
    }

    return candidates.stream().filter(Objects::nonNull).sorted().findFirst().orElse(null);
  }

  private UserInfoDTO.Builder map(Utente utente, AssociazioneEnteUtente associazioneEnte,
      Profilo profilo, List<Gruppo> gruppi) {

    //@formatter:off
    return UserInfoDTO.builder()
        .withId(utente.getId())
        .withNome(utente.getNome())
        .withCognome(utente.getCognome())
        .withCodiceFiscale(utente.getCodiceFiscale())
        .withAnonimo(false)
        .withEmail(associazioneEnte != null ? associazioneEnte.getEmail() : null)
        .withTelefono(associazioneEnte != null ? associazioneEnte.getTelefono() : null)
        .withEnte(map(associazioneEnte))
        .withProfilo(map(profilo))
        .withFineValidita(getFineValidita(associazioneEnte, profilo))
        .withGruppi(safe(gruppi).stream()
            .map(this::map)
            .collect(Collectors.toList()));
    //@formatter:on
  }

  private GruppoDTO map(Gruppo dto) {
    //@formatter:off
    return GruppoDTO.builder()
        .withId(dto.getId())
        .withCodice(dto.getCodice())
        .withNome(dto.getNome())
        .withDescrizione(dto.getDescrizione())
        .build();
    //@formatter:on
  }

  private ProfiloDTO map(Profilo profilo) {
    //@formatter:off
    return profilo != null ? ProfiloDTO.builder()
        .withId(profilo.getId())
        .withCodice(profilo.getCodice())
        .withDescrizione(profilo.getDescrizione())
        .withUseCases(safe(profilo.getUseCases()).stream()
            /*
            .filter(dto -> dto.getCampiTecnici() != null &&
            ValidationUtils.inPeriodoValido(dto.getCampiTecnici().getDtIniVal(), dto.getCampiTecnici().getDtFineVal()))
             */
            .map(this::map)
            .collect(Collectors.toList()))
        .build() : null;
        //@formatter:on
  }

  private UseCaseDTO map(UseCase dto) {
    //@formatter:off
    return UseCaseDTO.builder()
        .withCodice(dto.getCodice())
        .withDescrizione(dto.getDescrizione())
        .withCategoria(dto.getCodiceCategoria() != null ? CategoriaUseCaseDTO.builder()
            .withCodice(dto.getCodiceCategoria().getCodice())
            .withDescrizione(dto.getCodiceCategoria().getDescrizione())
            .build() : null)
        .build();
    //@formatter:on
  }

  private EnteDTO map(AssociazioneEnteUtente associazioneEnte) {
    //@formatter:off
    return associazioneEnte != null ? EnteDTO.builder()
        .withId(associazioneEnte.getEnte().getId())
        .withNome(associazioneEnte.getEnte().getNome())
        .withTenantId(associazioneEnte.getEnte().getCodiceIpa())
        .build() : null;
        //@formatter:on
  }

  private List<Gruppo> getGruppiPerEnteFromUtente(Utente utente, Long idEnte) {
    if (idEnte == null || utente == null) {
      return Collections.emptyList();
    }
    return safe(utente.getGruppi()).stream()
        .filter(gruppo -> gruppo.getEnte() != null && gruppo.getEnte().getId().equals(idEnte))
        .collect(Collectors.toList());
  }

  private Profilo getProfiloSelezionatoFromUtente(Utente utente,
      AssociazioneEnteUtente associazioneEnte, Long idProfilo) {
    if (idProfilo == null) {
      return null;
    }

    return utente.getProfili().stream()
        .filter(associazione -> (associazioneEnte == null && associazione.getEnte() == null)
            || (associazioneEnte != null && associazione.getEnte() != null
                && associazione.getEnte().getId().equals(associazioneEnte.getEnte().getId())))
        .filter(associazione -> associazione.getProfilo() != null
            && associazione.getProfilo().getId().equals(idProfilo))
        /*
         * .filter(associazione -> associazione.getProfilo().getCampiTecnici() != null &&
         * ValidationUtils.inPeriodoValido(
         * associazione.getProfilo().getCampiTecnici().getDtIniVal(),
         * associazione.getProfilo().getCampiTecnici().getDtFineVal()))
         */
        .reduce((a, b) -> {
          throw new InternalServerException(
              "Profili multipli corrispondenti allo stesso codice ID");
        }).map(AssociazioneUtenteProfilo::getProfilo)
        .orElseThrow(() -> new UnauthorizedException("Profilo non registrato o disabilitato."));
  }

  private AssociazioneEnteUtente getEnteSelezionatoFromUtente(Utente utente, Long idEnte) {
    if (idEnte == null) {
      return null;
    }

    return utente.getEnti().stream().filter(ente -> ente.getEnte().getId().equals(idEnte))
        /*
         * .filter(ente -> ente.getCampiTecnici() != null && ente.getEnte().getCampiTecnici() !=
         * null && ValidationUtils.inPeriodoValido(ente.getEnte().getCampiTecnici().getDtIniVal(),
         * ente.getEnte().getCampiTecnici().getDtFineVal()) &&
         * ValidationUtils.inPeriodoValido(ente.getCampiTecnici().getDtIniVal(),
         * ente.getCampiTecnici().getDtFineVal()))
         */
        .reduce((a, b) -> {
          throw new InternalServerException("Enti multipli corrispondenti allo stesso codice ID");
        }).orElseThrow(() -> new UnauthorizedException("Ente non registrato o disabilitato."));
  }

  private Utente getUtenteFromCodiceFiscale(String codiceFiscale) {

    UtenteResponse result = authorizationUtentiClient.getUtentiCodiceFiscale(codiceFiscale);

    return Optional.ofNullable(result.getUtente())
        .orElseThrow(() -> new UnauthorizedException("Utente non registrato o disabilitato."));
  }

  private <T> Collection<T> safe(Collection<T> source) {
    return source == null ? Collections.emptyList() : source;
  }
}
