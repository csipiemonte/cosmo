/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.proto.CampiTecniciEntity;
import it.csi.cosmo.common.security.model.GruppoDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmopratiche.config.ErrorMessages;
import it.csi.cosmo.cosmopratiche.dto.rest.Attivita;
import it.csi.cosmo.cosmopratiche.dto.rest.AttivitaFruitore;
import it.csi.cosmo.cosmopratiche.dto.rest.FunzionalitaAttivita;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoAttivita;
import it.csi.cosmo.cosmopratiche.dto.rest.Task;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;

/**
 *
 */
@Component
public class CosmoTAttivitaMapper {

  @Autowired
  CosmoTPraticaRepository praticheRepo;

  @Autowired
  CosmoTAttivitaRepository attivitaRepo;

  @Autowired
  CosmoRAttivitaAssegnazioneMapper attAssegnazioneMapper;

  @Autowired
  AbstractMapper abstractMapper;

  @Autowired
  CampiTecniciMapper campiTecniciMapper;

  public Attivita toAttivita(CosmoTAttivita input, UserInfoDTO userInfo) {
    boolean perUtente = userInfo != null && userInfo.getId() != null;

    Attivita a = new Attivita();
    a.setDescrizione(input.getDescrizione());
    a.setId(input.getId().intValue());
    a.setIdPratica(input.getCosmoTPratica().getId().intValue());
    a.setLinkAttivita(input.getLinkAttivita());
    a.setLinkAttivitaEsterna(input.getLinkAttivitaEsterna());
    a.setNome(input.getNome());
    a.setDataCancellazione(abstractMapper.toISO8601(input.getDtCancellazione()));
    a.setParent(input.getParent() != null ? input.getParent().getId().toString() : null);
    a.setCampiTecnici(campiTecniciMapper.toDTO(input));

    var hasChildren = input.getSubtasks() != null && !input.getSubtasks().isEmpty() && !input.getSubtasks().stream()
        .filter(CampiTecniciEntity::nonCancellato)
        .collect(Collectors.toList()).isEmpty();
    a.setHasChildren(hasChildren);

    List<Long> gruppiId =
        perUtente ? nullSafe(userInfo.getGruppi()).stream().map(GruppoDTO::getId)
            .collect(Collectors.toList()) : Collections.emptyList();

    //@formatter:off
    a.setAssegnazione(nullSafe(input.getCosmoRAttivitaAssegnaziones()).stream()
        .filter(ass ->
        ass.valido() &&
        (
            !perUtente
            ||
            (
                (
                    Boolean.TRUE.equals(ass.getAssegnatario())
                    && ass.getIdUtente() != null
                    && ass.getIdUtente().equals(userInfo.getId().intValue())
                    )
                ||
                (
                    !Boolean.TRUE.equals(ass.getAssegnatario())
                    && ass.getIdGruppo() != null
                    && gruppiId.contains(Long.valueOf(ass.getIdGruppo()))
                    )
                )
            )
            )
        .map(e -> attAssegnazioneMapper.toAssegnazione(e))
        .collect(Collectors.toList()));
    //@formatter:on

    if (perUtente) {
      String postfixAssegnazioniGruppi = input.getCosmoRAttivitaAssegnaziones().stream()
          .filter(ass -> ass.valido() && ass.getIdGruppo() != null)
          .map(ass -> ass.getGruppo().getDescrizione()).collect(Collectors.joining(", "));

      if (!StringUtils.isBlank(postfixAssegnazioniGruppi)) {
        a.setNome(a.getNome());
        a.setGruppoAssegnatario(postfixAssegnazioniGruppi);
      }
    }

    a.setFormKey(input.getFormKey());
    if (input.getFormLogico() != null && input.getFormLogico().getDtCancellazione() == null) {
      var funzDtoList = new ArrayList<FunzionalitaAttivita>();
      for (var istanza : input.getFormLogico().getCosmoRFormLogicoIstanzaFunzionalitas()) {
        if (istanza.nonValido() || istanza.getCosmoTIstanzaFunzionalitaFormLogico() == null
            || istanza.getCosmoTIstanzaFunzionalitaFormLogico().cancellato()) {
          continue;
        }
        var dtoFunz = new FunzionalitaAttivita();
        dtoFunz.setIdIstanzaFormLogico(istanza.getCosmoTIstanzaFunzionalitaFormLogico().getId());
        dtoFunz.setCodiceFunzionalita(istanza.getCosmoTIstanzaFunzionalitaFormLogico()
            .getCosmoDFunzionalitaFormLogico().getCodice());
        dtoFunz.setEsecuzioneMassiva(istanza.getEseguibileMassivamente());
        funzDtoList.add(dtoFunz);
      }
      a.setFunzionalita(funzDtoList);
    }

    return a;
  }

  public RiferimentoAttivita toRiferimentoAttivita(CosmoTAttivita input) {

    RiferimentoAttivita a = new RiferimentoAttivita();
    a.setDescrizione(input.getDescrizione());
    a.setId(input.getId().intValue());
    a.setNome(input.getNome());
    a.setDataCancellazione(abstractMapper.toISO8601(input.getDtCancellazione()));

    return a;
  }

  public CosmoTAttivita toCosmoTAttivita(Attivita input) {
    CosmoTAttivita a = new CosmoTAttivita();
    if (input.getIdPratica() != null) {
      CosmoTPratica p;
      try {
        p = praticheRepo.findOne(Long.parseLong(input.getIdPratica().toString()));
      } catch (Exception e) {
        throw new BadRequestException(e.getMessage());
      }
      if (p == null) {
        throw new NotFoundException(ErrorMessages.PRATICA_NON_TROVATA);
      }
      a.setCosmoTPratica(p);
    }
    a.setDescrizione(input.getDescrizione());
    if (input.getId() != null) {
      a.setId(Long.parseLong(input.getId().toString()));
    }
    a.setLinkAttivita(input.getLinkAttivita());
    a.setLinkAttivitaEsterna(input.getLinkAttivitaEsterna());
    a.setNome(input.getNome());

    if (!StringUtils.isBlank(input.getParent())) {
      var parent = attivitaRepo
          .findOneNotDeletedByField(CosmoTAttivita_.linkAttivita, "tasks/" + input.getParent())
          .orElseThrow(() -> new it.csi.cosmo.common.exception.NotFoundException(
              "Attivita' parent non trovata o non attiva"));
      a.setParent(parent);
    }

    List<CosmoRAttivitaAssegnazione> assegnazione = new ArrayList<>();

    input.getAssegnazione()
    .forEach(ass -> assegnazione.add(attAssegnazioneMapper.toCosmoRAttivitaAssegnazione(ass)));

    if (!CollectionUtils.isEmpty(assegnazione)) {
      a.setCosmoRAttivitaAssegnaziones(assegnazione);
    }

    return a;
  }

  public List<Attivita> toAttivita(List<CosmoTAttivita> att, UserInfoDTO userInfo) {
    return att.stream().map(p -> toAttivita(p, userInfo)).collect(Collectors.toList());
  }

  public Set<CosmoTAttivita> toCosmoTAttivita(List<Attivita> att) {
    return att.stream().map(p -> toCosmoTAttivita((p))).collect(Collectors.toSet());
  }

  public Task toTask(CosmoTAttivita a, CosmoTUtente assegnatario) {
    Task res = new Task();
    res.setDescription(a.getDescrizione());
    if (a.getLinkAttivita() != null) {
      res.setId(a.getLinkAttivita().replace("tasks/", ""));
    }
    res.setName(a.getNome());
    res.setCreateTime(
        a.getDtInserimento().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    if (a.getDtCancellazione() != null) {
      res.setCancellationDate(
          a.getDtCancellazione().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    } else {
      res.setCancellationDate(null);
    }

    if (assegnatario != null) {
      res.setAssignee(assegnatario.getCodiceFiscale());
    }

    return res;
  }

  private <T> List<T> nullSafe(List<T> input) {
    if (input == null) {
      return new ArrayList<>();
    }
    return input;
  }

  public AttivitaFruitore toAttivitaFruitore(CosmoTAttivita input, UserInfoDTO utenteCorrente) {
    boolean perUtente = utenteCorrente != null && utenteCorrente.getId() != null;

    AttivitaFruitore a = new AttivitaFruitore();
    a.setDescrizione(input.getDescrizione());
    a.setNome(input.getNome());
    a.setDataInserimento(input.getDtInserimento().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    if (perUtente) {
      String postfixAssegnazioniGruppi = input.getCosmoRAttivitaAssegnaziones().stream()
          .filter(ass -> ass.valido() && ass.getIdGruppo() != null)
          .map(ass -> ass.getGruppo().getDescrizione()).collect(Collectors.joining(", "));
      a.setNomeGruppoAssegnatario(postfixAssegnazioniGruppi);
    }
    return a;
  }
}
