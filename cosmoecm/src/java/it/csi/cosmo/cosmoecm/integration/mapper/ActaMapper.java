/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.cosmoecm.dto.rest.ClassificazioneActa;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentoFisicoActa;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentoSempliceActa;
import it.csi.cosmo.cosmoecm.dto.rest.IdentitaUtente;
import it.csi.cosmo.cosmoecm.dto.rest.IdentitaUtenteResponse;
import it.csi.cosmo.cosmoecm.dto.rest.ImportaDocumentiActaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.ProtocolloActa;
import it.csi.cosmo.cosmoecm.dto.rest.TitolarioActa;
import it.csi.cosmo.cosmoecm.dto.rest.VoceTitolarioActa;
import it.csi.cosmo.cosmosoap.dto.rest.Classificazione;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoFisico;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoSemplice;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaActa;
import it.csi.cosmo.cosmosoap.dto.rest.ImportaDocumentiRequest;
import it.csi.cosmo.cosmosoap.dto.rest.Protocollo;
import it.csi.cosmo.cosmosoap.dto.rest.Titolario;
import it.csi.cosmo.cosmosoap.dto.rest.VoceTitolario;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
uses = {DateFormatsMapper.class})
public interface ActaMapper {

  ImportaDocumentiRequest toImportaDocumentiRequest(ImportaDocumentiActaRequest input);

  @Mapping(target = "documentiFisici", ignore = true)
  @Mapping(target = "protocolli", ignore = true)
  @Mapping(target = "classificazioni", ignore = true)
  DocumentoSempliceActa toDocumentoSempliceActa(DocumentoSemplice input);

  List<DocumentoSempliceActa> toDocumentiSempliciActa(List<DocumentoSemplice> input);

  DocumentoFisicoActa toDocumentoFisicoActa(DocumentoFisico input);

  List<DocumentoFisicoActa> toDocumentiFisiciActa(List<DocumentoFisico> input);

  List<ClassificazioneActa> acta2ClassificazioneActaDTOList(List<Classificazione> input);

  List<ProtocolloActa> acta2ProtocolloActaDTOList(List<Protocollo> input);

  IdentitaUtente acta2DTO(IdentitaActa input);

  IdentitaUtenteResponse acta2DTOs(List<IdentitaActa> input);

  TitolarioActa acta2TitolarioActa(Titolario input);

  @Mapping(target = "dataFine", source = "dataFIne")
  @Mapping(target = "presenzaFascicoloRealeAnnualeNV", source = "presenzaFascRealeAnnualeNV")
  VoceTitolarioActa acta2VoceTitolarioActa(VoceTitolario input);

  List<VoceTitolarioActa> acta2VociTitolario(List<VoceTitolario> input);

}
