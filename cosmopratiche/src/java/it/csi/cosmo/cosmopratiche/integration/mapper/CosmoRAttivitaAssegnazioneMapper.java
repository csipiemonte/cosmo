/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTGruppo_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.cosmopratiche.dto.rest.Assegnazione;
import it.csi.cosmo.cosmopratiche.dto.rest.CampiTecnici;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTGruppoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTUtenteRepository;

/**
 *
 */
@Component
public class CosmoRAttivitaAssegnazioneMapper {

  @Autowired
  CosmoTPraticaRepository praticheRepo;

  @Autowired
  CosmoTUtenteRepository utenteRepository;


  @Autowired
  CosmoTGruppoRepository gruppoRepository;


  public Assegnazione toAssegnazione(CosmoRAttivitaAssegnazione input) {
    Assegnazione ass = new Assegnazione();
    if (input.getIdGruppo() != null) {
      ass.setIdGruppo(input.getIdGruppo().toString());
    }
    if (input.getIdUtente() != null) {
      ass.setIdUtente(input.getIdUtente().toString());
    }

    ass.setAssegnatario(false);
    if (input.getAssegnatario() != null)
      ass.setAssegnatario(input.getAssegnatario());

    if (input.getDtInizioVal() != null || input.getDtFineVal() != null) {
      CampiTecnici campiTecnici = new CampiTecnici();

      campiTecnici.setDtIniVal(input.getDtInizioVal() == null ? null
          : OffsetDateTime.ofInstant(Instant.ofEpochMilli(input.getDtInizioVal().getTime()),
              ZoneId.systemDefault()));

      campiTecnici.setDtFineVal(input.getDtFineVal() == null ? null
          : OffsetDateTime.ofInstant(Instant.ofEpochMilli(input.getDtFineVal().getTime()),
              ZoneId.systemDefault()));

      ass.setCampiTecnici(campiTecnici);
    }

    return ass;
  }


  public CosmoRAttivitaAssegnazione toCosmoRAttivitaAssegnazione(Assegnazione input) {
    CosmoRAttivitaAssegnazione a = new CosmoRAttivitaAssegnazione();

    if (input.getIdGruppo() != null) {
      CosmoTGruppo gruppo =
          gruppoRepository.findOneByField(CosmoTGruppo_.codice, input.getIdGruppo()).orElse(null);
      if(gruppo != null) {
      a.setIdGruppo(Integer.parseInt(gruppo.getId().toString()));
      }
    }

    if (input.getIdUtente() != null) {
      CosmoTUtente utente = utenteRepository.findByCodiceFiscale(input.getIdUtente());
      if(utente != null) {
      a.setIdUtente(Integer.parseInt(utente.getId().toString()));
      }
    }

    a.setAssegnatario(input.isAssegnatario());

    return a;
  }

  public List<Assegnazione> toAssegnazione(List<CosmoRAttivitaAssegnazione> ass) {
    return ass.stream().map(p -> toAssegnazione((p))).collect(Collectors.toList());
  }

  public Set<CosmoRAttivitaAssegnazione> CosmoRAttivitaAssegnazione(List<Assegnazione> ass) {
    return ass.stream().map(p -> toCosmoRAttivitaAssegnazione((p))).collect(Collectors.toSet());
  }

}
