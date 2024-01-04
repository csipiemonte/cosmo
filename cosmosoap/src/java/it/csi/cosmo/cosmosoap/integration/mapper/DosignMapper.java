/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.cosmosoap.dto.rest.CommonRemoteData;
import it.csi.cosmo.cosmosoap.dto.rest.ContinueTransaction;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiPayload;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiPraticaPayload;
import it.csi.cosmo.cosmosoap.dto.rest.DosignEsito;
import it.csi.cosmo.cosmosoap.dto.rest.FirmaMassivaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.StartTransaction;
import it.csi.cosmo.cosmosoap.integration.dto.DoSignMassivePayloadDTO;
import it.csi.cosmo.cosmosoap.integration.dto.DosignOutcomeDTO;
import it.csi.cosmo.cosmosoap.integration.dto.DosignPayloadDTO;
import it.csi.cosmo.cosmosoap.integration.dto.DosignSignedContentDTO;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.CommonRemoteDataDto;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.ContinueTransactionDto;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.StartTransactionDto;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface DosignMapper {

  @Mapping(source = "originalFilename", target = "withOriginalFilename")
  @Mapping(source = "originalContent", target = "withOriginalContent")
  @Mapping(source = "signedFilename", target = "withSignedFilename")
  @Mapping(source = "signedContent", target = "withSignedContent")
  @Mapping(source = "id", target = "withId")
  @Mapping(source = "primaFirma", target = "withPrimaFirma")
  @Mapping(ignore = true, target = "contenuto")
  @Mapping(ignore = true, target = "withContenuto")
  DosignPayloadDTO toDosignPayload(DocumentiPayload input);

  List<DosignPayloadDTO> toDosignPayloads(List<DocumentiPayload> input);

  FirmaMassivaResponse toFirmaMassiva(DosignSignedContentDTO input);

  DosignEsito toEsito(DosignOutcomeDTO input);

  CommonRemoteData toCommonRemoteData(CommonRemoteDataDto input);

  StartTransactionDto toStartTransactionDTO(StartTransaction input);

  ContinueTransactionDto toContinueTransactionDTO(ContinueTransaction input);

  @Mapping(source = "idPratica", target = "withIdPratica")
  @Mapping(source = "idAttivita", target = "withIdAttivita")
  @Mapping(source = "documentiDaFirmare", target = "withDocumentiDaFirmare")
  DoSignMassivePayloadDTO toDosignMassivePayload(DocumentiPraticaPayload input);

  List<DoSignMassivePayloadDTO> toDosignMassivePayloads(List<DocumentiPraticaPayload> list);

  @Mapping(ignore = true, target = "idFunzionalita")
  DocumentiPraticaPayload toDocumentiPraticaPayload(DoSignMassivePayloadDTO input);

  List<DocumentiPraticaPayload> toDocumentiPraticaPayloads(List<DoSignMassivePayloadDTO> input);

}
