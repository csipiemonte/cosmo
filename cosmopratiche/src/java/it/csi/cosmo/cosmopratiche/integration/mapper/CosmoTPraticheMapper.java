/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoRPraticaUtenteGruppo;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTVariabile;
import it.csi.cosmo.common.entities.enums.RelazionePraticaUtente;
import it.csi.cosmo.common.entities.proto.CampiTecniciEntity;
import it.csi.cosmo.common.security.model.GruppoDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmopratiche.config.ErrorMessages;
import it.csi.cosmo.cosmopratiche.dto.rest.AttivitaFruitore;
import it.csi.cosmo.cosmopratiche.dto.rest.CondivisionePratica;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaPraticheDTO;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheFruitore;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoGruppo;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoUtente;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.Variabile;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmopratiche.security.UseCase;

@Component
public class CosmoTPraticheMapper extends AbstractMapper {

  @Autowired
  AbstractMapper abstractMapper;

  @Autowired
  CosmoTFruitoreMapper mapperFruitore;

  @Autowired
  CosmoTAttivitaMapper attivitaMapper;

  @Autowired
  CosmoRPraticaTagsMapper tagsMapper;

  @Autowired
  CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  CosmoTPraticheFruitoreMapper cosmoTPraticheFruitoreMapper;

  public Pratica toPractice(CosmoTPratica pratiche, FiltroRicercaPraticheDTO filtroRicercaPraticheDTO, UserInfoDTO userInfo) {
    boolean perUtente = userInfo != null && userInfo.getId() != null;

    Pratica p = new Pratica();
    p.setId(pratiche.getId().intValue());
    p.setFruitore(mapperFruitore.toRiferimentoFruitore(pratiche.getFruitore()));
    if (pratiche.getTipo() != null)
      p.setTipo(toTipoPratica(pratiche.getTipo()));
    if (pratiche.getEnte() != null)
      p.setCodiceIpaEnte(pratiche.getEnte().getCodiceIpa());
    p.setDataCambioStato(abstractMapper.toISO8601(pratiche.getDataCambioStato()));
    p.setDataCreazionePratica(abstractMapper.toISO8601(pratiche.getDataCreazionePratica()));
    p.setDataAggiornamentoPratica(abstractMapper.toISO8601(pratiche.getDtUltimaModifica()));
    p.setDataFinePratica(abstractMapper.toISO8601(pratiche.getDataFinePratica()));
    p.setIdPraticaExt(pratiche.getIdPraticaExt());
    p.setLinkPratica(pratiche.getLinkPratica());
    p.setLinkPraticaEsterna(pratiche.getLinkPraticaEsterna());
    p.setEsterna(pratiche.getEsterna());
    p.setOggetto(pratiche.getOggetto());
    p.setUuidNodo(pratiche.getUuidNodo());
    p.setDataCancellazione(abstractMapper.toISO8601(pratiche.getDtCancellazione()));
    p.setMetadati(pratiche.getMetadati());
    p.setStato(toStatoPratica(pratiche.getStato()));
    p.setRiassunto(pratiche.getRiassunto());

    List<CosmoRPraticaUtenteGruppo> condivisioniVisibili =
        nullSafe(pratiche.getAssociazioneUtentiGruppi());

    if (perUtente) {
      p.setPreferita(nullSafe(pratiche.getAssociazioneUtentiGruppi()).stream()
          .anyMatch(pratUtente -> pratUtente.valido()
              && pratUtente.getCosmoTUtente() != null
              && pratUtente.getCosmoTUtente().getId().equals(userInfo.getId())
              && pratUtente.getCosmoDTipoCondivisionePratica().getCodice()
              .equalsIgnoreCase(RelazionePraticaUtente.PREFERITA.getCodice())));

      //@formatter:off
      condivisioniVisibili = nullSafe(pratiche.getAssociazioneUtentiGruppi()).stream()
          .filter(e ->
          e.valido()
          && e.getCosmoDTipoCondivisionePratica().getCodice().equalsIgnoreCase(RelazionePraticaUtente.CONDIVISA.getCodice())
          && (
              (
                  (e.getCosmoTUtente() != null && e.getCosmoTUtente().getId().equals(userInfo.getId()))
                  ||
                  (e.getCosmoTGruppo() != null && userInfo.getGruppi().stream().anyMatch(gruppo -> e.getCosmoTGruppo().getId().equals(gruppo.getId())))
                  )
              ||
              (
                  e.getCosmoTUtenteCondivisore().getId() != null && e.getCosmoTUtenteCondivisore().getId().equals(userInfo.getId())
                  )
              )
              )
          .collect(Collectors.toList());
      //@formatter:on
    }

    //@formatter:off
    p.setAttivita(
        nullSafe(pratiche.getAttivita()).stream()
        .filter(CampiTecniciEntity::nonCancellato)
        .map(e -> attivitaMapper.toAttivita(e, userInfo))
        .filter(a -> !a.getAssegnazione().isEmpty())
        .collect(Collectors.toList())
        );
    //@formatter:on

    //@formatter:off
    p.setCondivisioni(
        condivisioniVisibili.stream()
        .map(this::toCondivisionePratica)
        .collect(Collectors.toList())
        );

    //@formatter:on

    if (filtroRicercaPraticheDTO != null && filtroRicercaPraticheDTO.getTipologia() != null
        && filtroRicercaPraticheDTO.getVariabiliProcesso() != null
        && !filtroRicercaPraticheDTO.getVariabiliProcesso().isEmpty()) {

      List<CosmoTVariabile> variabili = nullSafe(pratiche.getVariabili());
      p.setVariabiliProcesso(
          variabili.stream().filter(temp -> {
            return (temp.nonCancellato() && filtroRicercaPraticheDTO.getVariabiliProcesso().stream()
                .anyMatch(variabileTemp -> variabileTemp.getNomeVariabile()
                    .equals(temp.getNome())));
          }).map(this::toVariabileProcesso)

          .collect(Collectors.toList()));


    }


    if (perUtente) {
      p.setVisibilita(calcolaCondizioniVisibilitaSoddisfatte(pratiche, userInfo));
    }

    p.setAssociata((pratiche.getCosmoRPraticaPraticasA()!=null && !pratiche.getCosmoRPraticaPraticasA().isEmpty())
        || (pratiche.getCosmoRPraticaPraticasDa()!=null && !pratiche.getCosmoRPraticaPraticasDa().isEmpty()));
    return p;
  }

  public Pratica toPracticeLight(CosmoTPratica pratiche,
      FiltroRicercaPraticheDTO filtroRicercaPraticheDTO) {

    Pratica p = new Pratica();
    p.setId(pratiche.getId().intValue());
    p.setFruitore(mapperFruitore.toRiferimentoFruitore(pratiche.getFruitore()));
    if (pratiche.getTipo() != null)
      p.setTipo(toTipoPraticaLight(pratiche.getTipo()));
    if (pratiche.getEnte() != null)
      p.setCodiceIpaEnte(pratiche.getEnte().getCodiceIpa());
    p.setDataCambioStato(abstractMapper.toISO8601(pratiche.getDataCambioStato()));
    p.setDataCreazionePratica(abstractMapper.toISO8601(pratiche.getDataCreazionePratica()));
    p.setDataAggiornamentoPratica(abstractMapper.toISO8601(pratiche.getDtUltimaModifica()));
    p.setDataFinePratica(abstractMapper.toISO8601(pratiche.getDataFinePratica()));
    p.setIdPraticaExt(pratiche.getIdPraticaExt());
    p.setLinkPratica(pratiche.getLinkPratica());
    p.setLinkPraticaEsterna(pratiche.getLinkPraticaEsterna());
    p.setEsterna(pratiche.getEsterna());
    p.setOggetto(pratiche.getOggetto());
    p.setRiassunto(pratiche.getRiassunto());
    p.setStato(toStatoPratica(pratiche.getStato()));
    p.setUtenteCreazionePratica(pratiche.getUtenteCreazionePratica());
    p.setUuidNodo(pratiche.getUuidNodo());
    p.setDataCancellazione(abstractMapper.toISO8601(pratiche.getDtCancellazione()));
    p.setMetadati(pratiche.getMetadati());

    if (filtroRicercaPraticheDTO != null && filtroRicercaPraticheDTO.getTipologia() != null
        && filtroRicercaPraticheDTO.getVariabiliProcesso() != null
        && !filtroRicercaPraticheDTO.getVariabiliProcesso().isEmpty()) {

      List<CosmoTVariabile> variabili = nullSafe(pratiche.getVariabili());
      p.setVariabiliProcesso(variabili.stream().filter(temp -> {
        return (temp.nonCancellato() && filtroRicercaPraticheDTO.getVariabiliProcesso().stream()
            .anyMatch(variabileTemp -> variabileTemp.getNomeVariabile().equals(temp.getNome())));
      }).map(this::toVariabileProcesso).collect(Collectors.toList()));


    }

    return p;
  }

  public CondivisionePratica toCondivisionePratica(CosmoRPraticaUtenteGruppo input) {
    if (input == null) {
      return null;
    }
    CondivisionePratica output = new CondivisionePratica();
    output.setId(input.getId());
    output.setCondivisaAUtente(riferimentoUtente(input.getCosmoTUtente()));
    output.setCondivisaAGruppo(riferimentoGruppo(input.getCosmoTGruppo()));
    output.setCondivisaDa(riferimentoUtente(input.getCosmoTUtenteCondivisore()));
    return output;
  }

  public Variabile toVariabileProcesso(CosmoTVariabile input) {
    if (input == null) {
      return null;
    }
    Variabile output = new Variabile();
    output.setNome(input.getNome());
    String valore = input.getTextValue() != null ? input.getTextValue()
        : input.getBytearrayValue() != null ? new String(input.getBytearrayValue())
            : input.getDoubleValue() != null ? input.getDoubleValue().toString()
                : input.getLongValue() != null ? input.getLongValue().toString()
                : input.getJsonValue() != null ? input.getJsonValue() : null;
    output.setValore(valore);
    return output;
  }


  private RiferimentoUtente riferimentoUtente(CosmoTUtente input) {
    if (input == null) {
      return null;
    }
    RiferimentoUtente output = new RiferimentoUtente();
    output.setCodiceFiscale(input.getCodiceFiscale());
    output.setCognome(input.getCognome());
    output.setNome(input.getNome());
    output.setId(input.getId());
    return output;
  }

  private RiferimentoGruppo riferimentoGruppo(CosmoTGruppo input) {
    if (input == null) {
      return null;
    }
    RiferimentoGruppo output = new RiferimentoGruppo();
    output.setCodice(input.getCodice());
    output.setDescrizione(input.getDescrizione());
    output.setNome(input.getNome());
    output.setId(input.getId());
    return output;
  }

  private StatoPratica toStatoPratica(CosmoDStatoPratica input) {
    if (input == null) {
      return null;
    }
    StatoPratica output = new StatoPratica();
    output.setCodice(input.getCodice());
    output.setDescrizione(input.getDescrizione());
    output.setClasse(input.getClasse());
    return output;
  }

  private TipoPratica toTipoPratica(CosmoDTipoPratica input) {
    if (input == null) {
      return null;
    }
    TipoPratica output = new TipoPratica();
    output.setCodice(input.getCodice());
    output.setDescrizione(input.getDescrizione());
    output.setAssegnabile(input.getAssegnabile());
    output.setAnnullabile(input.getAnnullabile());
    output.setCondivisibile(input.getCondivisibile());
    output.setRegistrazioneStilo(input.getRegistrazioneStilo());
    output.setTipoUnitaDocumentariaStilo(input.getTipoUnitaDocumentariaStilo());
    output.setImmagine(input.getIcona() == null ? null : new String(input.getIcona()));
    return output;
  }

  private TipoPratica toTipoPraticaLight(CosmoDTipoPratica input) {
    if (input == null) {
      return null;
    }
    TipoPratica output = new TipoPratica();
    output.setCodice(input.getCodice());
    output.setDescrizione(input.getDescrizione());
    output.setAssegnabile(input.getAssegnabile());
    output.setAnnullabile(input.getAnnullabile());
    output.setCondivisibile(input.getCondivisibile());
    output.setRegistrazioneStilo(input.getRegistrazioneStilo());
    output.setTipoUnitaDocumentariaStilo(input.getTipoUnitaDocumentariaStilo());
    return output;
  }

  public CosmoTPratica toCosmoTPratica(Pratica input) {
    CosmoTPratica p = new CosmoTPratica();

    CosmoTEnte ente = cosmoTEnteRepository
        .findOneNotDeletedByField(CosmoTEnte_.codiceIpa, input.getCodiceIpaEnte()).orElse(null);

    if (ente == null) {
      String message =
          String.format(ErrorMessages.CODICE_IPA_ENTE_NON_TROVATA, input.getCodiceIpaEnte());
      throw new NotFoundException(message);
    }
    p.setEnte(ente);
    p.setFruitore(mapperFruitore.toCosmoTFruitore(input.getFruitore()));
    p.setDataCambioStato(abstractMapper.toTimestamp(input.getDataCambioStato()));
    p.setDataCreazionePratica(abstractMapper.toTimestamp(input.getDataCreazionePratica()));
    p.setDtUltimaModifica(abstractMapper.toTimestamp(input.getDataAggiornamentoPratica()));
    p.setDataFinePratica(abstractMapper.toTimestamp(input.getDataFinePratica()));
    p.setId(input.getId() == null ? null : input.getId().longValue());
    p.setIdPraticaExt(input.getIdPraticaExt());
    p.setLinkPratica(input.getLinkPratica());
    p.setLinkPraticaEsterna(input.getLinkPraticaEsterna());
    p.setOggetto(input.getOggetto());
    p.setRiassunto(input.getRiassunto());

    p.setUtenteCreazionePratica(input.getUtenteCreazionePratica());
    p.setUuidNodo(input.getUuidNodo());

    p.setUtenteCreazionePratica(input.getUtenteCreazionePratica());
    p.setStato(input.getStato() != null && !StringUtils.isEmpty(input.getStato().getCodice())
        ? cosmoTEnteRepository.reference(CosmoDStatoPratica.class, input.getStato().getCodice())
            : null);
    p.setTipo(input.getTipo() != null && !StringUtils.isEmpty(input.getTipo().getCodice())
        ? cosmoTEnteRepository.reference(CosmoDTipoPratica.class, input.getTipo().getCodice())
            : null);

    if (input.getAttivita() != null && !input.getAttivita().isEmpty()) {
      if (p.getId() != null)
        input.getAttivita().forEach(a -> a.setIdPratica(Integer.parseInt("" + p.getId())));
      p.setAttivita(attivitaMapper.toCosmoTAttivita(input.getAttivita()).stream()
          .collect(Collectors.toList()));

    }
    return p;
  }

  private <T> List<T> nullSafe(List<T> input) {
    if (input == null) {
      return new ArrayList<>();
    }
    return input;
  }

  private List<String> calcolaCondizioniVisibilitaSoddisfatte(CosmoTPratica pratica, // NOSONAR
      UserInfoDTO userInfo) {
    List<String> output = new ArrayList<>();
    boolean perUtente =
        userInfo != null && userInfo.getId() != null && Boolean.FALSE.equals(userInfo.getAnonimo());

    if (!perUtente) {
      return Collections.emptyList();
    }

    if (userInfo.hasAuthority(UseCase.ADMIN_PRAT_CODE)) {
      output.add("ADMIN");
    }

    /*
     * A - ente: la pratica deve essere gestita dall'ente selezionato durante l'accesso (ente
     * associato al profilo utente);
     */
    if (pratica.getEnte().getId() != null && userInfo.getEnte() != null
        && userInfo.getEnte().getId() != null
        && pratica.getEnte().getId().equals(userInfo.getEnte().getId())) {
      output.add("A");
    }

    /*
     * B1 - creatore pratica: l'utente ha inserito la pratica (cosmo_t_pratica.utente_inserimento
     * utente_crezione_pratica);
     */
    if (!StringUtils.isBlank(pratica.getUtenteCreazionePratica())
        && pratica.getUtenteCreazionePratica().equals(userInfo.getCodiceFiscale())) {
      output.add("B1");
    }

    /*
     * B2 - utente assegnatario: l'utente / stato assegnatario di un task della pratica (esistenza
     * per l'id_utente di un record di cosmo_r_attivita_assegnazione con assegnatario = true, non
     * considerare la condizione di validit dell'assegnazione e del task);
     */
    if (pratica.getAttivita() != null && pratica.getAttivita().stream()
        .anyMatch(attivita -> attivita.getCosmoRAttivitaAssegnaziones() != null
        && attivita.getCosmoRAttivitaAssegnaziones().stream()
        .anyMatch(assegnazione -> Boolean.TRUE.equals(assegnazione.getAssegnatario())
            && assegnazione.getIdUtente() != null
            && userInfo.getId().equals(Long.valueOf(assegnazione.getIdUtente()))))) {
      output.add("B2");
    }

    /*
     * B3 - gruppo assegnatario: l'utente fa parte/ha fatto parte di un gruppo assegnatario della
     * pratica (esistenza di un record di cosmo_r_attivita_assegnazione, non considerare la
     * condizione di validit dell'assegnazione e del task, per i gruppi di appartenenza
     * dell'id_utente, cosmo_t_utente_gruppo, non considerare la condizione di validit);
     */
    var idGruppi = userInfo.getGruppi() != null
        ? userInfo.getGruppi().stream().map(GruppoDTO::getId).collect(Collectors.toSet())
            : Collections.emptySet();

    if (userInfo.getGruppi() != null && !userInfo.getGruppi().isEmpty()
        && pratica.getAttivita() != null
        && pratica.getAttivita().stream()
        .anyMatch(attivita -> attivita.getCosmoRAttivitaAssegnaziones() != null
        && attivita.getCosmoRAttivitaAssegnaziones().stream()
        .anyMatch(assegnazione -> assegnazione.getIdGruppo() != null
        && idGruppi.contains(Long.valueOf(assegnazione.getIdGruppo()))))) {
      output.add("B3");
    }

    /*
     * C - preferenza: l'utente ha inserito la pratica tra le sue preferite (per l'id_utente esiste
     * un record di cosmo_r_pratica_utente con tipo_rel = 'preferita');
     */
    if (pratica.getAssociazioneUtentiGruppi() != null
        && !pratica.getAssociazioneUtentiGruppi().isEmpty()
        && pratica.getAssociazioneUtentiGruppi().stream()
        .anyMatch(associazione -> associazione.valido()
            && associazione.getCosmoTUtente() != null
            && associazione.getCosmoTUtente().getId() != null
            && associazione.getCosmoTUtente().getId().equals(userInfo.getId())
            && associazione.getCosmoDTipoCondivisionePratica().getCodice()
            .equals(RelazionePraticaUtente.PREFERITA.getCodice()))) {
      output.add("C");
    }

    /*
     * D - evidenza: la pratica stata posta in evidenza per l'utente (per l'id_utente o i gruppo
     * dell'utente esiste un record valido di cosmo_r_pratica_utente con tipo_rel = 'condivisa' e
     * dt_fine_val nulla);
     */
    if (pratica.getAssociazioneUtentiGruppi() != null
        && !pratica.getAssociazioneUtentiGruppi().isEmpty()
        && pratica.getAssociazioneUtentiGruppi().stream()
        .anyMatch(associazione -> associazione.valido()
            && ((associazione.getCosmoTUtente() != null
            && associazione.getCosmoTUtente().getId() != null
            && associazione.getCosmoTUtente().getId().equals(userInfo.getId()))
                || (associazione.getCosmoTGruppo() != null
                && associazione.getCosmoTGruppo().getId() != null
                && userInfo.getGruppi().stream()
                .anyMatch(gruppo -> associazione.getCosmoTGruppo().getId()
                    .equals(gruppo.getId()))))

            && associazione.getCosmoDTipoCondivisionePratica().getCodice()
            .equals(RelazionePraticaUtente.CONDIVISA.getCodice()))) {
      output.add("D");
    }

    /*
     * E1 - utente assegnatario corrente: l'utente assegnatario di un task della pratica (esistenza
     * per l'id_utente di un record valido di cosmo_r_attivita_assegnazione con assegnatario = true
     * e cosmo_t_attivita valido ovvero con dt_cancellazione nulla);
     */
    if (pratica.getAttivita() != null && pratica.getAttivita().stream()
        .anyMatch(attivita -> attivita.nonCancellato()
            && attivita.getCosmoRAttivitaAssegnaziones() != null
            && attivita.getCosmoRAttivitaAssegnaziones().stream()
            .anyMatch(assegnazione -> assegnazione.valido()
                && Boolean.TRUE.equals(assegnazione.getAssegnatario())
                && assegnazione.getIdUtente() != null
                && userInfo.getId().equals(Long.valueOf(assegnazione.getIdUtente()))))) {
      output.add("E1");
    }

    /*
     * E2 - gruppo assegnatario corrente: l'utente appartiene a un gruppo assegnatario della pratica
     * (esistenza di un record valido di cosmo_r_attivita_assegnazione per i gruppi di appartenenza
     * dell'id_utente, cosmo_t_utente_gruppo validi e cosmo_t_attivita valido ovvero con
     * dt_cancellazione nulla).
     */
    if (userInfo.getGruppi() != null && !userInfo.getGruppi().isEmpty()
        && pratica.getAttivita() != null
        && pratica.getAttivita().stream()
        .anyMatch(
            attivita -> attivita.nonCancellato()
            && attivita.getCosmoRAttivitaAssegnaziones() != null
            && attivita.getCosmoRAttivitaAssegnaziones().stream()
            .anyMatch(assegnazione -> assegnazione.valido()
                && assegnazione.getIdGruppo() != null
                && idGruppi.contains(Long.valueOf(assegnazione.getIdGruppo()))))) {
      output.add("E2");
    }

    return output;
  }

  public PraticheFruitore toPracticeFruitori(CosmoTPratica praticaDB, UserInfoDTO utenteCorrente, Set<?> idGruppi) {
    if (praticaDB == null) {
      return null;
    }

    PraticheFruitore praticheFruitore = cosmoTPraticheFruitoreMapper.toDto(praticaDB);
    List<AttivitaFruitore> listaAttivitaFruitore = new ArrayList<>();

    var utenteAssegnatarioCorrente = praticaDB.getAttivita().stream().filter(attivita ->
      attivita.nonCancellato() && attivita.getCosmoRAttivitaAssegnaziones() != null
          && attivita.getCosmoRAttivitaAssegnaziones().stream()
          .anyMatch(assegnazione -> assegnazione
              .valido()
              && Boolean.TRUE.equals(assegnazione.getAssegnatario())
              && assegnazione.getIdUtente() != null
                && utenteCorrente.getId().equals(Long.valueOf(assegnazione.getIdUtente()))))
        .collect(Collectors.toList());

    utenteAssegnatarioCorrente.stream().forEach(attivita -> listaAttivitaFruitore
            .add(attivitaMapper.toAttivitaFruitore(attivita, utenteCorrente)));


    if (utenteCorrente.getGruppi() != null && !utenteCorrente.getGruppi().isEmpty()) {
      var gruppoAssegnatarioCorrente = praticaDB.getAttivita().stream().filter(attivita -> attivita
          .nonCancellato()
          && attivita.getCosmoRAttivitaAssegnaziones() != null
          && attivita.getCosmoRAttivitaAssegnaziones().stream()
              .anyMatch(assegnazione -> assegnazione.valido() && assegnazione.getIdGruppo() != null
                  && idGruppi.contains(Long.valueOf(assegnazione.getIdGruppo()))))
          .collect(Collectors.toList());

      gruppoAssegnatarioCorrente.stream().forEach(attivita -> listaAttivitaFruitore
          .add(attivitaMapper.toAttivitaFruitore(attivita, utenteCorrente)));
    }

    praticheFruitore.setAttivita(listaAttivitaFruitore);
    praticheFruitore.setTag(nullSafe(praticaDB.getCosmoRPraticaTags()).stream().filter(input ->input.valido()).map(e -> tagsMapper.toDTOTagRidotto(e)).collect(Collectors.toList()));

    return praticheFruitore;
  }
}
