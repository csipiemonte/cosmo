/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service;

import java.util.List;
import it.csi.cosmo.cosmopratiche.dto.rest.FormatoVariabileDiFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.VariabileDiFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.VariabiliDiFiltroResponse;

/**
 *
 */

public interface VariabiliFiltroService {

  void deleteVariabiliFiltroId(String id);

  VariabileDiFiltro getVariabiliFiltroId(String id);

  VariabileDiFiltro postVariabiliFiltro(VariabileDiFiltro body);

  VariabileDiFiltro putVariabiliFiltroId(String id, VariabileDiFiltro body);

  VariabiliDiFiltroResponse getVariabiliFiltro(String filter);

  List<FormatoVariabileDiFiltro> getVariabiliFiltroFormati();

  List<TipoFiltro> getVariabiliFiltroTipiFiltro();

  List<VariabileDiFiltro> getVariabiliFiltroTipoPratica(String codice);

}
