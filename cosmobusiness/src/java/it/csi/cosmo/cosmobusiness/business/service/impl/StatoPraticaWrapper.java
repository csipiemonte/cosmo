/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoRSmistamentoDocumento;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTCommento;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento_;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTInfoVerificaFirma;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.enums.EsitoVerificaFirma;
import it.csi.cosmo.common.entities.enums.InfoSmistamento;
import it.csi.cosmo.common.entities.enums.StatoSmistamento;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.ArchiviazioneDocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnazioneFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.AttivitaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.DocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.FirmaDocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.MessaggioFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.ProtocolloDocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.SottoAttivitaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.UtenteFruitore;
import it.csi.cosmo.cosmobusiness.integration.mapper.CallbackFruitoriMapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTGruppoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTUtenteRepository;

/**
 *
 */

public class StatoPraticaWrapper {

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoTGruppoRepository cosmoTGruppoRepository;

  @Autowired
  private CallbackFruitoriMapper callbackFruitoriMapper;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;


  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  protected AggiornaStatoPraticaRequest fetchPratica(CosmoTPratica pratica) {
    var mapped = callbackFruitoriMapper.toDTO(pratica);
    if (mapped != null && pratica != null) {
      mapped.setMetadati(pratica.getMetadati());
    }
    return mapped;
  }

  protected List<AttivitaFruitore> fetchAttivita(CosmoTPratica pratica) {
    Pageable pageableAttivita = new PageRequest(0, 9999,
        new Sort(new Order(Direction.ASC, CosmoTEntity_.dtInserimento.getName()),
            new Order(Direction.DESC, CosmoTEntity_.dtCancellazione.getName())));

    Page<CosmoTAttivita> attivita = cosmoTAttivitaRepository
        .findByField(CosmoTAttivita_.cosmoTPratica, pratica, pageableAttivita);

    return attivita.getContent().stream().filter(a -> a.getParent() == null).map(this::toAttivita)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  protected List<DocumentoFruitore> fetchDocumenti(CosmoTPratica pratica) {
    Pageable pageableDocumenti = new PageRequest(0, 9999,
        new Sort(new Order(Direction.ASC, CosmoTDocumento_.titolo.getName()),
            new Order(Direction.DESC, CosmoTEntity_.dtInserimento.getName())));

    Page<CosmoTDocumento> documenti =
        cosmoTDocumentoRepository.findNotDeletedByField(CosmoTDocumento_.pratica, pratica,
            pageableDocumenti);

    return documenti.getContent().stream().map(this::toDocumento)
        .collect(Collectors.toCollection(LinkedList::new));

  }

  protected DocumentoFruitore toDocumento(CosmoTDocumento documento) {
    if (documento == null) {
      return null;
    }
    DocumentoFruitore mapped = callbackFruitoriMapper.toDTO(documento);

    ValidationUtils.assertNotNull(documento.getIdDocumentoExt(), "IdDocumentoExt");

    CosmoTContenutoDocumento contenuto = getContenutoDocumentoEffettivo(documento);
    if (contenuto != null) {
      if (contenuto.getFormatoFile() != null) {
        mapped.setMimeType(contenuto.getFormatoFile().getMimeType());
      }

      mapped.setDimensione(contenuto.getDimensione());
      mapped.setNomeFile(contenuto.getNomeFile());

      Timestamp now = Timestamp.from(Instant.now());
      mapped.setFirme(safe(contenuto.getInfoVerificaFirme()).stream()
          .filter(f -> f.getEsito() != null
          && f.getEsito().getCodice().equals(EsitoVerificaFirma.VALIDA.name()))
          .sorted((dto1, dto2) -> ObjectUtils.coalesce(dto1.getDtApposizione(), now)
              .compareTo(ObjectUtils.coalesce(dto2.getDtApposizione(), now)))
          .map(this::toFirma).collect(Collectors.toCollection(LinkedList::new)));
    }

    CosmoRSmistamentoDocumento risultatoSmistamentoRiuscito =
        safe(documento.getCosmoRSmistamentoDocumentos()).stream()
        .filter(
            rSmistamentoDocumento -> rSmistamentoDocumento.getCosmoDStatoSmistamento() != null
            && StatoSmistamento.SMISTATO.getCodice()
            .equals(rSmistamentoDocumento.getCosmoDStatoSmistamento().getCodice()))
        .findFirst().orElse(null);

    mapped.setArchiviazione(toArchiviazioneDocumento(risultatoSmistamentoRiuscito));

    mapped.setRefURL(toRefUrlDocumento(documento));

    mapped.setIdCosmo(documento.getId().toString());

    return mapped;
  }

  protected String toRefUrlDocumento(CosmoTDocumento documento) {
    if (StringUtils.isBlank(documento.getIdDocumentoExt())) {
      return null;
    }
    return "/documenti/" + documento.getIdDocumentoExt() + "/contenuto";
  }

  protected ArchiviazioneDocumentoFruitore toArchiviazioneDocumento(
      CosmoRSmistamentoDocumento risultatoSmistamentoRiuscito) {
    if (risultatoSmistamentoRiuscito == null) {
      return null;
    }

    ArchiviazioneDocumentoFruitore mapped = new ArchiviazioneDocumentoFruitore();

    mapped.setProtocollo(toDocumentoProtocollo(risultatoSmistamentoRiuscito));
    mapped.setClassificazione(risultatoSmistamentoRiuscito
        .getInfoAggiuntiva(InfoSmistamento.INDICE_CLASS_ESTESO).orElse(null));

    return mapped;
  }

  protected ProtocolloDocumentoFruitore toDocumentoProtocollo(
      CosmoRSmistamentoDocumento risultatoSmistamentoRiuscito) {
    if (risultatoSmistamentoRiuscito == null) {
      return null;
    }

    ProtocolloDocumentoFruitore mapped = new ProtocolloDocumentoFruitore();

    mapped.setNumero(risultatoSmistamentoRiuscito
        .getInfoAggiuntiva(InfoSmistamento.NUMERO_REG_PROTOCOLLO).orElse(null));
    mapped.setData(risultatoSmistamentoRiuscito
        .getInfoAggiuntiva(InfoSmistamento.DATA_REG_PROTOCOLLO).orElse(null));

    return mapped;
  }

  protected FirmaDocumentoFruitore toFirma(CosmoTInfoVerificaFirma infoVerificaFirma) {
    if (infoVerificaFirma == null) {
      return null;
    }

    return callbackFruitoriMapper.toDTO(infoVerificaFirma);
  }

  protected CosmoTContenutoDocumento getContenutoDocumentoEffettivo(CosmoTDocumento documento) {
    if (documento == null || documento.getContenuti() == null
        || documento.getContenuti().isEmpty()) {
      return null;
    }
    CosmoTContenutoDocumento contenuto = documento.findContenuto(TipoContenutoDocumento.FIRMATO);
    if (contenuto != null) {
      return contenuto;
    }
    contenuto = documento.findContenuto(TipoContenutoDocumento.ORIGINALE);
    if (contenuto != null) {
      return contenuto;
    }
    contenuto = documento.findContenuto(TipoContenutoDocumento.TEMPORANEO);
    return contenuto;
  }

  protected AttivitaFruitore toAttivita(CosmoTAttivita attivita) {
    if (attivita == null) {
      return null;
    }
    AttivitaFruitore mapped = callbackFruitoriMapper.toDTO(attivita);
    mapped.setAssegnazione(toAssegnazioneList(attivita.getCosmoRAttivitaAssegnaziones()));
    mapped.setSottoAttivita(toSottoAttivitaList(attivita.getSubtasks()));
    mapped.setMessaggiCollaboratori(fetchMessaggiTask(attivita));

    return mapped;
  }

  protected List<MessaggioFruitore> fetchMessaggiPratica(String idProcessInstance) {
    if (StringUtils.isEmpty(idProcessInstance)) {
      return Collections.emptyList();
    }

    // trovare la pratica tramite task id idProcessInstance
    Optional<CosmoTPratica> pratica = this.cosmoTPraticaRepository
        .findOneByField(CosmoTPratica_.linkPratica, "/pratiche/" + idProcessInstance);


    if (!pratica.isPresent()) {
      throw new NotFoundException();
    }


    List<CosmoTCommento> commenti = pratica.get().getCommenti().stream()
        .filter(temp -> temp.getDtCancellazione() == null && temp.getUtenteCancellazione() == null)
        .collect(Collectors.toList());

    return commenti.stream().map(callbackFruitoriMapper::toDTO)
        .sorted((dto1, dto2) -> dto1.getTimestamp().compareTo(dto2.getTimestamp()))
        .collect(Collectors.toCollection(LinkedList::new));
  }

  protected List<MessaggioFruitore> fetchMessaggiTask(CosmoTAttivita attivita) {

    List<CosmoTCommento> commenti = attivita.getCommenti();

    return commenti.stream().map(callbackFruitoriMapper::toDTO)
        .sorted((dto1, dto2) -> dto1.getTimestamp().compareTo(dto2.getTimestamp()))
        .collect(Collectors.toCollection(LinkedList::new));
  }

  protected List<AssegnazioneFruitore> toAssegnazioneList(
      List<CosmoRAttivitaAssegnazione> assegnazioni) {
    if (assegnazioni == null || assegnazioni.isEmpty()) {
      return Collections.emptyList();
    }

    return assegnazioni.stream().filter(CosmoREntity::valido)
        .sorted((a1, a2) -> a1.getDtInizioVal().compareTo(a2.getDtInizioVal()))
        .map(this::toAssegnazione).collect(Collectors.toCollection(LinkedList::new));
  }

  protected AssegnazioneFruitore toAssegnazione(CosmoRAttivitaAssegnazione assegnazioneValida) {
    if (assegnazioneValida == null) {
      return null;
    }

    AssegnazioneFruitore output = new AssegnazioneFruitore();
    if (assegnazioneValida.getIdUtente() != null) {
      output.setUtente(Optional
          .ofNullable(cosmoTUtenteRepository.findOne(assegnazioneValida.getIdUtente().longValue()))
          .map(callbackFruitoriMapper::toDTO).orElse(null));
    }
    if (assegnazioneValida.getIdGruppo() != null) {
      output.setGruppo(Optional
          .ofNullable(cosmoTGruppoRepository.findOne(assegnazioneValida.getIdGruppo().longValue()))
          .map(CosmoTGruppo::getDescrizione).orElse(null));
    }

    return output;
  }

  protected List<SottoAttivitaFruitore> toSottoAttivitaList(List<CosmoTAttivita> sottoAttivita) {
    if (sottoAttivita == null || sottoAttivita.isEmpty()) {
      return Collections.emptyList();
    }

    return sottoAttivita.stream()
        .sorted((a1, a2) -> a1.getDtInserimento().compareTo(a2.getDtInserimento()))
        .map(this::toSottoAttivita).collect(Collectors.toCollection(LinkedList::new));
  }

  protected SottoAttivitaFruitore toSottoAttivita(CosmoTAttivita sottoAttivita) {
    if (sottoAttivita == null) {
      return null;
    }

    SottoAttivitaFruitore mapped = callbackFruitoriMapper.toSottoAttivitaDTO(sottoAttivita);
    mapped.setAssegnazione(toAssegnazioneList(sottoAttivita.getCosmoRAttivitaAssegnaziones()));
    return mapped;
  }

  protected Collection<UtenteFruitore> estraiUtentiAttivita(AttivitaFruitore attivita) {
    Collection<UtenteFruitore> utenti = new ArrayList<>();

    if (attivita.getAssegnazione() != null) {
      attivita.getAssegnazione().forEach(ass -> {
        if (ass.getUtente() != null) {
          utenti.add(ass.getUtente());
        }
      });
    }

    if (attivita.getSottoAttivita() != null) {
      attivita.getSottoAttivita().forEach(sa -> {
        if (sa.getAssegnazione() != null) {
          sa.getAssegnazione().forEach(ass -> {
            if (ass.getUtente() != null) {
              utenti.add(ass.getUtente());
            }
          });
        }
      });
    }

    if (attivita.getSottoAttivita() != null && attivita.getMessaggiCollaboratori() != null) {
      attivita.getMessaggiCollaboratori().forEach(m -> utenti.add(m.getUtente()));
    }

    return utenti;
  }

  protected void enrichUtenti(Collection<UtenteFruitore> utenti) {
    utenti = utenti.stream()
        .filter(u -> u != null && !StringUtils.isBlank(u.getCodiceFiscale()) && u.getNome() == null)
        .collect(Collectors.toList());

    if (utenti == null || utenti.isEmpty()) {
      return;
    }

    Set<String> codiciFiscali =
        utenti.stream().map(UtenteFruitore::getCodiceFiscale).collect(Collectors.toSet());

    if (codiciFiscali.isEmpty()) {
      return;
    }

    Map<String, CosmoTUtente> utentiEntities =
        cosmoTUtenteRepository.findByCodiceFiscaleIn(codiciFiscali).stream()
        .collect(Collectors.toMap(CosmoTUtente::getCodiceFiscale, Function.identity()));

    utenti.forEach(u -> Optional.ofNullable(utentiEntities.getOrDefault(u.getCodiceFiscale(), null))
        .ifPresent(entity -> {
          u.setNome(entity.getNome());
          u.setCognome(entity.getCognome());
        }));
  }

  protected void enrichUtenti(AggiornaStatoPraticaRequest payload) {
    Collection<UtenteFruitore> utenti = new ArrayList<>();

    if (payload.getAttivita() != null) {
      payload.getAttivita().forEach(attivita -> utenti.addAll(this.estraiUtentiAttivita(attivita)));
    }

    if (payload.getCommenti() != null) {
      payload.getCommenti().forEach(u -> {
        if (u.getUtente() != null) {
          utenti.add(u.getUtente());
        }
      });
    }

    if (payload.getUtenteCreazione() != null) {
      utenti.add(payload.getUtenteCreazione());
    }

    enrichUtenti(utenti);
  }

  protected void enrichUtentiMessaggi(List<MessaggioFruitore> payload) {
    Collection<UtenteFruitore> utenti = new ArrayList<>();

    if (payload != null) {
      payload.forEach(u -> {
        if (u.getUtente() != null) {
          utenti.add(u.getUtente());
        }
      });
    }

    enrichUtenti(utenti);
  }

  protected void enrichUtentiAttivita(List<AttivitaFruitore> payload) {
    Collection<UtenteFruitore> utenti = new ArrayList<>();

    if (payload != null) {
      payload.forEach(attivita -> utenti.addAll(this.estraiUtentiAttivita(attivita)));
    }

    enrichUtenti(utenti);
  }

  protected void enrichUtentiPratica(AggiornaStatoPraticaRequest payload) {
    Collection<UtenteFruitore> utenti = new ArrayList<>();

    if (payload.getUtenteCreazione() != null) {
      utenti.add(payload.getUtenteCreazione());
    }

    enrichUtenti(utenti);
  }

  protected <T> Collection<T> safe(Collection<T> raw) {
    if (raw == null) {
      return Collections.emptyList();
    }
    return raw;
  }

}
