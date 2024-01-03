/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.cosmoecm.integration.dto.DoSignMassivePayloadDTO;
import it.csi.cosmo.cosmoecm.integration.dto.DosignPayloadDTO;
import it.csi.cosmo.cosmosoap.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiPayload;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiPraticaPayload;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiTask;
import it.csi.cosmo.cosmosoap.dto.rest.Documento;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface DosignMapper {

  DocumentiPayload toDocumentiPayload(DosignPayloadDTO input);

  List<DocumentiPayload> toDocumentiPayloads(List<DosignPayloadDTO> input);

  DocumentiPraticaPayload toDocumentiPraticaPayload(DoSignMassivePayloadDTO input);

  List<DocumentiPraticaPayload> toDocumentiPraticaPayloads(List<DoSignMassivePayloadDTO> input);

  AttivitaEseguibileMassivamente toAttvitaEseguibileMassivamenteSoap(it.csi.cosmo.cosmoecm.dto.rest.AttivitaEseguibileMassivamente input);

  List<AttivitaEseguibileMassivamente> toAttvitaEseguibileMassivamenteSoap(
      List<it.csi.cosmo.cosmoecm.dto.rest.AttivitaEseguibileMassivamente> input);

  List<DocumentiTask> toDocumentiTaskSoap(
      List<it.csi.cosmo.cosmoecm.dto.rest.DocumentiTask> input);

  List<Documento> toDocumentiSoap(List<it.csi.cosmo.cosmoecm.dto.rest.Documento> documenti);

  @Mapping(ignore = true, target = "approvazioni")
  Documento toDocumentoSoap(it.csi.cosmo.cosmoecm.dto.rest.Documento documento);
}
