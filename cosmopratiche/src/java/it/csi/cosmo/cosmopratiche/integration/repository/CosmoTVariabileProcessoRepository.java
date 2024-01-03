/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository;

import java.util.Optional;
import it.csi.cosmo.common.entities.CosmoTVariabileProcesso;
import it.csi.cosmo.common.repository.CosmoTRepository;

public interface CosmoTVariabileProcessoRepository
extends CosmoTRepository<CosmoTVariabileProcesso, Long> {

  CosmoTVariabileProcesso findByIdAndDtCancellazioneIsNull(Long id);

  Optional<CosmoTVariabileProcesso> findByNomeVariabileAndTipoPraticaCodiceAndDtCancellazioneIsNull(
      String nomeVariabile, String codiceTipoPratica);
}
