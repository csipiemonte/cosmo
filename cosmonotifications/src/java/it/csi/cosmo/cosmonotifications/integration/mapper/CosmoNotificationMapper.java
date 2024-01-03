/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmonotifications.integration.mapper;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoRNotificaUtenteEnte;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTNotifica;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.cosmonotifications.dto.rest.Fruitore;
import it.csi.cosmo.cosmonotifications.dto.rest.Notifica;
import it.csi.cosmo.cosmonotifications.dto.rest.Pratica;
import it.csi.cosmo.cosmonotifications.dto.rest.RiferimentoFruitore;
import it.csi.cosmo.cosmonotifications.dto.rest.StatoPratica;
import it.csi.cosmo.cosmonotifications.dto.rest.TipoPratica;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoTipoPraticaRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoUtenteRepository;

@Component
public class CosmoNotificationMapper extends AbstractMapper {

  @Autowired
  CosmoUtenteRepository utenteRepo;

  @Autowired
  CosmoTipoPraticaRepository cosmoTipoPraticaRepository;

  public Pratica toPratica(CosmoTPratica src) {
    if (src == null) {
      return null;
    }

    Pratica res = new Pratica();
    if (src.getEnte() != null) {
      res.setCodiceIpaEnte(src.getEnte().getCodiceIpa());
    }
    res.setDataCambioStato(AbstractMapper.toISO8601(src.getDataCambioStato()));
    res.setDataCreazionePratica(AbstractMapper.toISO8601(src.getDataCreazionePratica()));
    res.setDataFinePratica(AbstractMapper.toISO8601(src.getDataFinePratica()));
    res.setFruitore(toRiferimentoFruitore(src.getFruitore()));
    res.setId(src.getId().intValue());
    res.setIdPraticaExt(src.getIdPraticaExt());
    res.setLinkPratica(src.getLinkPratica());
    res.setLinkPraticaEsterna(src.getLinkPraticaEsterna());
    res.setEsterna(src.getEsterna());
    res.setOggetto(src.getOggetto());
    res.setRiassunto(src.getRiassunto());
    res.setStato(toStatoPratica(src.getStato()));
    if (src.getTipo() != null)
      res.setTipo(toTipoPratica(src.getTipo()));
    res.setUtenteCreazionePratica(src.getUtenteCreazionePratica());
    res.setUuidNodo(src.getUuidNodo());
    return res;
  }

  private StatoPratica toStatoPratica(CosmoDStatoPratica input) {
    if (input == null) {
      return null;
    }
    StatoPratica output = new StatoPratica();
    output.setCodice(input.getCodice());
    output.setDescrizione(input.getDescrizione());
    return output;
  }

  private TipoPratica toTipoPratica(CosmoDTipoPratica input) {
    if (input == null) {
      return null;
    }
    TipoPratica output = new TipoPratica();
    output.setCodice(input.getCodice());
    output.setDescrizione(input.getDescrizione());
    return output;
  }

  public CosmoTPratica toCosmoTPratica(Pratica input) {
    if (input == null || input.getId() == null) {
      return null;
    }
    CosmoTPratica res = new CosmoTPratica();
    res.setId(input.getId().longValue());
    return res;
  }

  public Fruitore toFruitore(CosmoTFruitore src) {
    if (src == null) {
      return null;
    }
    Fruitore res = new Fruitore();
    res.setId(src.getId() != null ? Long.parseLong(src.getId().toString()) : null);
    res.setNomeApp(src.getNomeApp());
    res.setUrl(src.getUrl());

    return res;
  }

  public RiferimentoFruitore toRiferimentoFruitore(CosmoTFruitore src) {
    if (src == null) {
      return null;
    }
    RiferimentoFruitore res = new RiferimentoFruitore();
    res.setId(src.getId());
    res.setNomeApp(src.getNomeApp());
    res.setApiManagerId(src.getApiManagerId());
    return res;
  }

  public CosmoTFruitore toCosmoTFruitore(Fruitore src) {
    if (src == null) {
      return null;
    }
    CosmoTFruitore res = new CosmoTFruitore();
    res.setId(src.getId() != null ? (long) src.getId() : null);
    res.setNomeApp(src.getNomeApp());
    res.setUrl(src.getUrl());

    return res;
  }


  public Notifica toNotification(CosmoTNotifica input, CosmoRNotificaUtenteEnte crnu) {
    Notifica res = new Notifica();

    if (input == null) {
      return null;
    }

    res.setId(input.getId() != null ? input.getId().intValue() : null);

    if (crnu != null) {
      Long idUtente = crnu.getId().getIdUtente();
      res.setCodiceFiscale(utenteRepo.findOne(idUtente).getCodiceFiscale());
      res.setLettura(AbstractMapper.toISO8601(crnu.getDataLettura()));
    }

    res.setArrivo(AbstractMapper.toISO8601(input.getArrivo()));
    res.setDescrizione(input.getDescrizione());
    res.setPratica(toPratica(input.getCosmoTPratica()));
    res.setFruitore(toFruitore(input.getCosmoTFruitore()));
    res.setUrl(input.getUrl());
    res.setUrlDescrizione(input.getUrlDescrizione());

    List<String> destinatari = input.getCosmoRNotificaUtenteEntes().stream()
        .map(rel -> rel.getCosmoTUtente().getCodiceFiscale()).collect(Collectors.toList());
    res.setDestinatari(destinatari);


    res.setScadenza(AbstractMapper.toISO8601(input.getScadenza()));
    return res;
  }

}
