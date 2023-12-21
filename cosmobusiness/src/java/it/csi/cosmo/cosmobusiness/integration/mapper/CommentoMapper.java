/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.mapper;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.entities.CosmoTCommento;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.cosmobusiness.dto.rest.Commento;
import it.csi.cosmo.cosmobusiness.dto.rest.CommentoResponseWrapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTUtenteRepository;

/**
 *
 */
@Component
public class CommentoMapper {

  @Autowired
  CosmoTUtenteRepository utenteR;

  @Autowired
  private DateFormatsMapper dateFormatsMapper;


  public Commento toCommento(CommentoResponseWrapper input) {
    Commento c = new Commento();

    c.setCfAutore(input.getAuthor());
    c.setMessaggio(input.getMessage());
    CosmoTUtente u = utenteR.findByCodiceFiscale(c.getCfAutore());
    if (u != null) {
      c.setCognomeAutore( u.getCognome());
      c.setNomeAutore(u.getNome());
    }
    c.setId(input.getId());
    OffsetDateTime parse =
        OffsetDateTime.parse(input.getTime(), DateTimeFormatter.ISO_ZONED_DATE_TIME);
    c.setTimestamp(parse);
    return c;
  }


  public Commento toCommento(Map<String, String> o) {

    String cf = o.get("author");
    String cognome = "";
    String nome = "";

    CosmoTUtente u = utenteR.findByCodiceFiscale(cf);
    if (u != null) {
      cognome = u.getCognome();
      nome = u.getNome();
    }
    Commento c = new Commento();
    c.setCfAutore(cf);
    c.setCognomeAutore(cognome);
    c.setNomeAutore(nome);
    c.setId(o.get("id"));
    c.setMessaggio(o.get("message"));
    String data = o.get("time");
    OffsetDateTime parse = OffsetDateTime.parse(data, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    c.setTimestamp(parse);

    return c;
  }

  public Commento toCommento(CosmoTCommento commento) {

    String cf = commento.getUtenteCreatore();
    String cognome = "";
    String nome = "";

    CosmoTUtente u = utenteR.findByCodiceFiscale(cf);
    if (u != null) {
      cognome = u.getCognome();
      nome = u.getNome();
    }
    Commento c = new Commento();
    c.setCfAutore(cf);
    c.setCognomeAutore(cognome);
    c.setNomeAutore(nome);
    c.setId(commento.getId().toString());
    c.setMessaggio(commento.getMessaggio());
    c.setTimestamp(dateFormatsMapper.mapOffsetDateTime(commento.getDataCreazione()));

    return c;
  }
}
