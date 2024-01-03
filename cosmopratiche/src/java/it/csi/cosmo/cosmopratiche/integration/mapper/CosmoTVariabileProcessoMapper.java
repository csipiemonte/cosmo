/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.entities.CosmoDFiltroCampo;
import it.csi.cosmo.common.entities.CosmoDFormatoCampo;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoTVariabileProcesso;
import it.csi.cosmo.cosmopratiche.dto.rest.FormatoVariabileDiFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.VariabileDiFiltro;

/**
 *
 */

@Component
public class CosmoTVariabileProcessoMapper {

  @Autowired
  CosmoTEnteMapper cosmoTEnteMapper;

  public VariabileDiFiltro toVariabileDiFiltro(CosmoTVariabileProcesso variabileProcesso) {

    VariabileDiFiltro variabileDiFiltro = new VariabileDiFiltro();
    variabileDiFiltro.setAggiungereARisultatoRicerca(variabileProcesso.getVisualizzareInTabella());
    variabileDiFiltro.setFormato(toFormatoVariabileDiFiltro(variabileProcesso.getFormatoCampo()));
    variabileDiFiltro.setId(variabileProcesso.getId());
    variabileDiFiltro.setLabel(variabileProcesso.getNomeVariabile());
    variabileDiFiltro.setNome(variabileProcesso.getNomeVariabileFlowable());
    variabileDiFiltro
    .setTipoFiltro(toTipoFiltroVariabileDiFiltro(variabileProcesso.getFiltroCampo()));
    variabileDiFiltro.setTipoPratica(toTipoPratica(variabileProcesso.getTipoPratica()));


    return variabileDiFiltro;
  }

  private TipoFiltro toTipoFiltroVariabileDiFiltro(CosmoDFiltroCampo filtroCampo) {
    TipoFiltro tipoFiltro = new TipoFiltro();
    tipoFiltro.setCodice(filtroCampo.getCodice());
    tipoFiltro.setDescrizione(filtroCampo.getDescrizione());
    return tipoFiltro;
  }

  private FormatoVariabileDiFiltro toFormatoVariabileDiFiltro(CosmoDFormatoCampo formatoCampo) {
    FormatoVariabileDiFiltro formatoVariabileDiFiltro = new FormatoVariabileDiFiltro();
    formatoVariabileDiFiltro.setCodice(formatoCampo.getCodice());
    formatoVariabileDiFiltro.setDescrizione(formatoCampo.getDescrizione());
    return formatoVariabileDiFiltro;
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
    output.setEnte(this.cosmoTEnteMapper.toRiferimentoDTO(input.getCosmoTEnte()));
    return output;
  }



}
