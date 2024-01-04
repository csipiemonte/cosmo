/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service;

import java.util.List;
import it.csi.cosmo.cosmosoap.dto.rest.AggiornaUnitaDocumentariaRequest;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.AddUdOutput;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.StiloAllegato;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.UpdUdOutput;
import it.eng.auriga.repository2.webservices.addunitadoc.dto.NewUD;
import it.eng.auriga.repository2.webservices.updunitadoc.dto.UDDaAgg;

/**
 *
 */

public interface StiloService {


  public AddUdOutput addUd(NewUD newUD, List<StiloAllegato> allegati);

  public UpdUdOutput updUd(UDDaAgg udDaAgg, List<StiloAllegato> allegati);

  public UpdUdOutput aggiornaUnitaDocumentaria(
      AggiornaUnitaDocumentariaRequest aggiornaUnitaDocumentariaRequest);

}
