/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.testbed.providers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoRStatoTipoPratica;
import it.csi.cosmo.common.entities.CosmoRUtenteEnte;
import it.csi.cosmo.common.entities.CosmoRUtenteEntePK;
import it.csi.cosmo.common.entities.CosmoRUtenteProfilo;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTProfilo;
import it.csi.cosmo.common.entities.CosmoTProfilo_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.security.model.CategoriaUseCaseDTO;
import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.common.security.model.ProfiloDTO;
import it.csi.cosmo.common.security.model.UseCaseDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmopratiche.config.Constants;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRStatoTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTUtenteRepository;
import it.csi.test.cosmo.cosmopratiche.testbed.repository.CosmoDStatoPraticaTestRepository;
import it.csi.test.cosmo.cosmopratiche.testbed.repository.CosmoDTipoPraticaTestRepository;
import it.csi.test.cosmo.cosmopratiche.testbed.repository.CosmoRUtenteEnteTestRepository;
import it.csi.test.cosmo.cosmopratiche.testbed.repository.CosmoRUtenteProfiloTestRepository;
import it.csi.test.cosmo.cosmopratiche.testbed.repository.CosmoTProfiloTestRepository;

/**
 *
 */

@Service
public class TestDataProvider {

  private static final String USER_SYSTEM = "SYSTEM";

  private static final String PROFILE_ADMIN = Constants.PROFILO.AMMINISTRATORE;

  private static final String PROFILE_OPERATOR = Constants.PROFILO.OPERATORE;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoTProfiloTestRepository cosmoTProfiloRepository;

  @Autowired
  private CosmoRUtenteEnteTestRepository cosmoRUtenteEnteRepository;

  @Autowired
  private CosmoRUtenteProfiloTestRepository cosmoRUtenteProfiloRepository;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoDStatoPraticaTestRepository cosmoDStatoPraticaTestRepository;

  @Autowired
  private CosmoDTipoPraticaTestRepository cosmoDTipoPraticaTestRepository;

  @Autowired
  private CosmoRStatoTipoPraticaRepository cosmoRStatoTipoPraticaRepository;

  private Long idProcessoCounter = 1000000L;

  private Long enteCounter = 0L;

  private Timestamp now() {
    return Timestamp.from(Instant.now());
  }

  public TestDataEnvironment givenTestData() {
    TestDataEnvironment out = new TestDataEnvironment();

    out.ente = givenEnte();

    out.amministratore = givenAmministratoreEnte(out.ente);

    out.operatori = Arrays.asList(givenOperatoreEnte(out.ente), givenOperatoreEnte(out.ente),
        givenOperatoreEnte(out.ente));

    out.tipiPratica = Arrays.asList(givenTipoPratica(out.ente), givenTipoPratica(out.ente),
        givenTipoPratica(out.ente));

    out.principalAmministratore = givenPrincipal(out.amministratore, out.ente, PROFILE_ADMIN);

    out.principalsOperatori = out.operatori.stream()
        .map(u -> givenPrincipal(u, out.ente, PROFILE_OPERATOR)).collect(Collectors.toList());

    return out;
  }

  public UserInfoDTO givenPrincipal(CosmoTUtente utente, CosmoTEnte ente, String codiceProfilo) {

    CosmoTProfilo profilo = utente.getCosmoRUtenteProfilos().stream()
        .filter(p -> p.getCosmoTProfilo().getCodice().equals(codiceProfilo)).findFirst()
        .map(CosmoRUtenteProfilo::getCosmoTProfilo).orElseThrow();

    //@formatter:off
    return UserInfoDTO.builder ()
        .withId(utente.getId())
        .withNome ( utente.getNome() )
        .withCognome ( utente.getCognome() )
        .withCodiceFiscale ( utente.getCodiceFiscale() )
        .withAnonimo(false)
        .withEnte(EnteDTO.builder()
            .withId(ente.getId())
            .withNome(ente.getNome())
            .withTenantId(ente.getCodiceIpa())
            .build())
        .withProfilo(ProfiloDTO.builder()
            .withId(profilo.getId())
            .withCodice(profilo.getCodice())
            .withDescrizione(profilo.getDescrizione())
            .withUseCases(profilo.getCosmoDUseCases().stream().map(uc ->
            UseCaseDTO.builder()
            .withCategoria(uc.getCosmoDCategoriaUseCase() != null ?
                CategoriaUseCaseDTO.builder()
                .withCodice(uc.getCosmoDCategoriaUseCase().getCodice())
                .withDescrizione(uc.getCosmoDCategoriaUseCase().getDescrizione())
                .build() : null
                )
            .withCodice(uc.getCodice())
            .withDescrizione(uc.getDescrizione())
            .build()
                ).collect(Collectors.toList()))
            .build())
        .build ();
    //@formatter:on
  }

  public CosmoRStatoTipoPratica givenAssociazioneStatoTipoPratica(CosmoDStatoPratica stato,
      CosmoDTipoPratica tipo) {

    CosmoRStatoTipoPratica entity = new CosmoRStatoTipoPratica();

    entity.setCosmoDStatoPratica(stato);
    entity.setCosmoDTipoPratica(tipo);
    entity.setDtInizioVal(stato.getDtInizioVal());

    entity = cosmoRStatoTipoPraticaRepository.save(entity);

    stato.getCosmoRStatoTipoPraticas().add(entity);
    tipo.getCosmoRStatoTipoPraticas().add(entity);

    return entity;
  }

  public CosmoDStatoPratica givenStatoPratica() {
    return givenStatoPratica(null, null);
  }

  public CosmoDStatoPratica givenStatoPratica(CosmoDTipoPratica tipo) {
    return givenStatoPratica(tipo, null);
  }

  public CosmoDStatoPratica givenStatoPratica(Consumer<CosmoDStatoPratica> builder) {
    return givenStatoPratica(null, builder);
  }

  public CosmoDStatoPratica givenStatoPratica(CosmoDTipoPratica tipo,
      Consumer<CosmoDStatoPratica> builder) {
    CosmoDStatoPratica entity = new CosmoDStatoPratica();

    String uuid = UUID.randomUUID().toString();

    entity.setCodice(uuid.substring(10));
    entity.setDescrizione("Stato pratica " + uuid);
    entity.setDtInizioVal(now());
    entity.setClasse("primary");

    entity.setCosmoRStatoTipoPraticas(new ArrayList<>());

    if (builder != null) {
      builder.accept(entity);
    }

    entity = cosmoDStatoPraticaTestRepository.save(entity);

    if (tipo != null) {
      givenAssociazioneStatoTipoPratica(entity, tipo);
    }

    return entity;
  }

  public CosmoDTipoPratica givenTipoPratica(CosmoTEnte ente) {
    return givenTipoPratica(ente, null);
  }

  public CosmoDTipoPratica givenTipoPratica(CosmoTEnte ente, Consumer<CosmoDTipoPratica> builder) {
    CosmoDTipoPratica entity = new CosmoDTipoPratica();

    String uuid = UUID.randomUUID().toString();

    entity.setCosmoTEnte(ente);
    entity.setCaseDefinitionKey(null);
    entity.setCodice(uuid);
    entity.setCodiceApplicazioneStardas(uuid);
    entity.setProcessDefinitionKey(uuid);
    entity.setDescrizione("Tipo pratica " + uuid);
    entity.setDtInizioVal(now());
    entity.setCreabileDaInterfaccia(true);
    entity.setCreabileDaServizio(true);
    entity.setOverrideFruitoreDefault(false);
    entity.setAnnullabile(true);
    entity.setAssegnabile(false);
    entity.setCondivisibile(true);

    entity.setCosmoCConfigurazioneMetadatis(new ArrayList<>());
    entity.setCosmoRStatoTipoPraticas(new ArrayList<>());
    entity.setCosmoRTipodocTipopraticas(new ArrayList<>());
    entity.setCosmoTPraticas(new ArrayList<>());

    if (builder != null) {
      builder.accept(entity);
    }

    entity = cosmoDTipoPraticaTestRepository.save(entity);

    for (int i = 0; i < 3; i++) {
      givenStatoPratica(entity);
    }

    return entity;
  }

  public CosmoRUtenteEnte givenAssociazioneUtenteEnte(CosmoTUtente utente, CosmoTEnte ente,
      String codiceProfilo) {

    CosmoRUtenteEnte entity = new CosmoRUtenteEnte();
    entity.setCosmoTEnte(ente);
    entity.setCosmoTUtente(utente);
    entity.setDtInizioVal(utente.getDtInserimento());
    entity.setEmail(utente.getCodiceFiscale() + "@" + ente.getCodiceFiscale() + ".it");
    entity.setTelefono("339100" + utente.getId());
    CosmoRUtenteEntePK id = new CosmoRUtenteEntePK();
    id.setIdEnte(ente.getId());
    id.setIdUtente(utente.getId());
    entity.setId(id);

    entity = cosmoRUtenteEnteRepository.save(entity);

    ente.getCosmoRUtenteEntes().add(entity);
    utente.getCosmoRUtenteEntes().add(entity);

    // add profile
    CosmoTProfilo profilo =
        cosmoTProfiloRepository.findOneByField(CosmoTProfilo_.codice, codiceProfilo).orElseThrow();

    CosmoRUtenteProfilo rUtenteProfilo = new CosmoRUtenteProfilo();
    rUtenteProfilo.setCosmoTEnte(ente);
    rUtenteProfilo.setCosmoTUtente(utente);
    rUtenteProfilo.setCosmoTProfilo(profilo);
    rUtenteProfilo.setDtInizioVal(utente.getDtInserimento());
    rUtenteProfilo.setId(null);
    rUtenteProfilo = cosmoRUtenteProfiloRepository.save(rUtenteProfilo);

    ente.getCosmoRUtenteProfilos().add(rUtenteProfilo);
    utente.getCosmoRUtenteProfilos().add(rUtenteProfilo);
    profilo.getCosmoRUtenteProfilos().add(rUtenteProfilo);

    return entity;
  }

  public CosmoTUtente givenUtente() {
    return givenUtente(null);
  }

  public CosmoTUtente givenAmministratoreEnte(CosmoTEnte ente) {
    return givenAmministratoreEnte(ente, null);
  }

  public CosmoTUtente givenAmministratoreEnte(CosmoTEnte ente, Consumer<CosmoTUtente> builder) {
    CosmoTUtente utente = givenUtente(u -> {
      if (builder != null) {
        builder.accept(u);
      }
    });

    givenAssociazioneUtenteEnte(utente, ente, PROFILE_ADMIN);

    return utente;
  }

  public CosmoTUtente givenOperatoreEnte(CosmoTEnte ente) {
    return givenOperatoreEnte(ente, null);
  }

  public CosmoTUtente givenOperatoreEnte(CosmoTEnte ente, Consumer<CosmoTUtente> builder) {
    CosmoTUtente utente = givenUtente(u -> {
      if (builder != null) {
        builder.accept(u);
      }
    });

    givenAssociazioneUtenteEnte(utente, ente, PROFILE_OPERATOR);

    return utente;
  }

  public CosmoTUtente givenUtente(Consumer<CosmoTUtente> builder) {
    String uuid = UUID.randomUUID().toString();

    Timestamp anHourAgo = Timestamp.from(Instant.now().minusSeconds(3600));
    Timestamp now = Timestamp.from(Instant.now());

    Long cnt = ++enteCounter;

    CosmoTUtente entity = new CosmoTUtente();

    entity.setId(null);
    entity.setUtenteInserimento(USER_SYSTEM);
    entity.setUtenteUltimaModifica(USER_SYSTEM);
    entity.setUtenteCancellazione(null);
    entity.setDtInserimento(anHourAgo);
    entity.setDtUltimaModifica(now);
    entity.setDtCancellazione(null);
    entity.setCodiceFiscale(String.format("%016d", cnt));
    entity.setCognome(uuid);
    entity.setNome("Utente");

    entity.setCosmoRNotificaUtenteEntes(new ArrayList<>());
    entity.setCosmoRPraticaUtenteGruppos(new ArrayList<>());
    entity.setCosmoRUtenteEntes(new ArrayList<>());
    entity.setCosmoRUtenteFunzionalitaApplicazioneEsternas(new ArrayList<>());
    entity.setCosmoRUtenteProfilos(new ArrayList<>());
    entity.setCosmoTCertificatoFirmas(new ArrayList<>());
    entity.setCosmoTGruppos(new ArrayList<>());
    entity.setCosmoTPreferenzeUtentes(new ArrayList<>());

    if (builder != null) {
      builder.accept(entity);
    }

    return cosmoTUtenteRepository.save(entity);
  }

  public CosmoTEnte givenEnte() {
    return givenEnte(null);
  }

  public CosmoTEnte givenEnte(Consumer<CosmoTEnte> builder) {
    String uuid = UUID.randomUUID().toString();

    Timestamp anHourAgo = Timestamp.from(Instant.now().minusSeconds(3600));
    Timestamp now = now();

    Long cnt = ++enteCounter;

    CosmoTEnte entity = new CosmoTEnte();

    entity.setId(null);
    entity.setUtenteInserimento(USER_SYSTEM);
    entity.setUtenteUltimaModifica(USER_SYSTEM);
    entity.setUtenteCancellazione(null);
    entity.setDtInserimento(anHourAgo);
    entity.setDtUltimaModifica(now);
    entity.setDtCancellazione(null);
    entity.setCodiceFiscale(String.format("%016d", cnt));
    entity.setCodiceIpa("TE" + String.format("%08d", cnt));
    entity.setNome("Ente " + uuid);

    entity.setCosmoREnteApplicazioneEsternas(new ArrayList<>());
    entity.setCosmoREnteCertificatoreEntes(new ArrayList<>());
    entity.setCosmoREnteFunzionalitaApplicazioneEsternas(new ArrayList<>());
    entity.setCosmoRFruitoreEntes(new ArrayList<>());
    entity.setCosmoRNotificaUtenteEntes(new ArrayList<>());
    entity.setCosmoRUtenteEntes(new ArrayList<>());
    entity.setCosmoRUtenteProfilos(new ArrayList<>());
    entity.setCosmoTGruppos(new ArrayList<>());
    entity.setCosmoTPraticas(new ArrayList<>());
    entity.setCosmoTPreferenzeEntes(new ArrayList<>());

    if (builder != null) {
      builder.accept(entity);
    }

    return cosmoTEnteRepository.save(entity);
  }

  public CosmoTPratica givenPratica() {
    return givenPratica(null);
  }

  public CosmoTPratica givenPratica(Consumer<CosmoTPratica> builder) {
    String uuid = UUID.randomUUID().toString();

    Timestamp anHourAgo = Timestamp.from(Instant.now().minusSeconds(3600));
    Timestamp now = now();

    CosmoTEnte ente = cosmoTPraticaRepository.reference(CosmoTEnte.class, 1L);
    CosmoDStatoPratica stato = cosmoTPraticaRepository.reference(CosmoDStatoPratica.class, "BOZZA");
    CosmoDTipoPratica tipo = cosmoTPraticaRepository.reference(CosmoDTipoPratica.class, "TP1");

    CosmoTPratica entity = new CosmoTPratica();
    entity.setId(null);
    entity.setAssociazioneUtentiGruppi(new ArrayList<>());
    entity.setAttivita(new ArrayList<>());
    entity.setDataCreazionePratica(anHourAgo);
    entity.setDataCambioStato(now);
    entity.setDataFinePratica(null);
    entity.setDocumenti(new ArrayList<>());
    entity.setDtCancellazione(null);
    entity.setDtInserimento(anHourAgo);
    entity.setDtUltimaModifica(now);
    entity.setEnte(ente);
    entity.setFruitore(null);
    entity.setIdPraticaExt(null);
    entity.setLinkPratica("/pratiche/" + (++idProcessoCounter));
    entity.setMetadati(null);
    entity.setNotifiche(new ArrayList<>());
    entity.setOggetto("Pratica di test " + uuid);
    entity.setRiassunto("Riassunto della pratica " + uuid);
    entity.setStato(stato);
    entity.setTipo(tipo);
    entity.setUtenteCancellazione(null);
    entity.setUtenteCreazionePratica(USER_SYSTEM);
    entity.setUtenteInserimento(USER_SYSTEM);
    entity.setUtenteUltimaModifica(USER_SYSTEM);
    entity.setUuidNodo(uuid);

    if (builder != null) {
      builder.accept(entity);
    }

    return cosmoTPraticaRepository.save(entity);
  }

  public static class TestDataEnvironment {

    CosmoTEnte ente;
    CosmoTUtente amministratore;
    List<CosmoTUtente> operatori;
    List<CosmoDTipoPratica> tipiPratica;
    UserInfoDTO principalAmministratore;
    List<UserInfoDTO> principalsOperatori;

    public CosmoTEnte getEnte() {
      return ente;
    }

    public void setEnte(CosmoTEnte ente) {
      this.ente = ente;
    }

    public CosmoTUtente getAmministratore() {
      return amministratore;
    }

    public void setAmministratore(CosmoTUtente amministratore) {
      this.amministratore = amministratore;
    }

    public List<CosmoTUtente> getOperatori() {
      return operatori;
    }

    public void setOperatori(List<CosmoTUtente> operatori) {
      this.operatori = operatori;
    }

    public List<CosmoDTipoPratica> getTipiPratica() {
      return tipiPratica;
    }

    public void setTipiPratica(List<CosmoDTipoPratica> tipiPratica) {
      this.tipiPratica = tipiPratica;
    }

    public UserInfoDTO getPrincipalAmministratore() {
      return principalAmministratore;
    }

    public void setPrincipalAmministratore(UserInfoDTO principalAmministratore) {
      this.principalAmministratore = principalAmministratore;
    }

    public List<UserInfoDTO> getPrincipalsOperatori() {
      return principalsOperatori;
    }

    public void setPrincipalsOperatori(List<UserInfoDTO> principalsOperatori) {
      this.principalsOperatori = principalsOperatori;
    }
  }
}
