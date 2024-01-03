/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.testbed.service;

import java.io.IOException;
import it.csi.cosmo.common.entities.CosmoRSmistamentoDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTPratica;

/**
 *
 */

public interface StardasTestbedService {

  CosmoTPratica salvaPratica(boolean salvaDocumento) throws IOException;

  CosmoTPratica recuperaPratica(Long id);

  void deletePratica(Long id);

  void aggiornaDocumento(CosmoTDocumento documento);

  void salvaRelazioneSmistamento(CosmoRSmistamentoDocumento relazioneSmistamento);

}
