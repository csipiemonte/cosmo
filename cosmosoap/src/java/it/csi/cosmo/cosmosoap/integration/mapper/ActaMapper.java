/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.mapper;

import java.io.IOException;
import java.util.List;
import javax.activation.DataHandler;
import org.apache.commons.io.IOUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmosoap.dto.rest.Classificazione;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoDocumentoFisico;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoFisico;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoSemplice;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaActa;
import it.csi.cosmo.cosmosoap.dto.rest.Protocollo;
import it.csi.cosmo.cosmosoap.dto.rest.RegistrazioneClassificazioni;
import it.csi.cosmo.cosmosoap.dto.rest.Titolario;
import it.csi.cosmo.cosmosoap.dto.rest.VoceTitolario;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ClassificazioneActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ContenutoDocumentoFisicoActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.DocumentoFisicoActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.DocumentoSempliceActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ProtocolloActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.RegistrazioneClassificazioniActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.TitolarioActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.VoceTitolarioActa;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
uses = {DateFormatsMapper.class, LocalDateTimeXmlGregorianCalendarMapper.class})
public interface ActaMapper {

  Classificazione toClassificazione(ClassificazioneActa input);

  List<Classificazione> toClassificazioni(List<ClassificazioneActa> input);

  DocumentoSemplice toDocumentoSemplice(DocumentoSempliceActa input);

  List<DocumentoSemplice> toDocumentiSemplici(List<DocumentoSempliceActa> input);

  DocumentoFisico toDocumentoFisico(DocumentoFisicoActa input);

  List<DocumentoFisico> toDocumentiFisici(List<DocumentoFisicoActa> input);

  Protocollo toProtocollo(ProtocolloActa input);

  Titolario toTitolario(TitolarioActa input);

  @Mapping(target = "dataFIne", source = "dataFine")
  @Mapping(target = "presenzaFascTemp", source = "presenzaFascicoloTemporaneo")
  @Mapping(target = "idProvvedimentoAutorizzatList", source = "idProvvedimentiAutorizzativi")
  @Mapping(target = "descrBreve", source = "descrizioneBreve")
  @Mapping(target = "presenzaFascRealeAnnualeNV", source = "presenzaFascicoloRealeAnnuale")
  @Mapping(target = "presenzaSerieFasc", source = "presenzaSerieFascicoli")
  @Mapping(target = "conservazioneGenerale", source = "durataConservazioneGenerale")
  @Mapping(target = "presenzaFascRealeLiberoNV", source = "presenzaFascicoloRealeLibero")
  @Mapping(target = "presenzaFascRealeLegislaturaNV", source = "presenzaFascicoloRealeLegislatura")
  @Mapping(target = "presenzaFascRealeContinuoNV", source = "presenzaFascicoloRealeContinuo")
  @Mapping(target = "conservazioneCorrente", source = "durataConservazioneCorrente")
  VoceTitolario toVoceTitolario(VoceTitolarioActa input);

  List<VoceTitolario> toVociTitolario(List<VoceTitolarioActa> input);

  RegistrazioneClassificazioni toRegistrazioneClassificazioni(
      RegistrazioneClassificazioniActa input);

  List<RegistrazioneClassificazioni> toRegistrazioniClassificazioni(
      List<RegistrazioneClassificazioniActa> input);

  @Mapping(target = "filename", source = "fileName")
  @Mapping(target = "mimetype", source = "mimeType")
  ContenutoDocumentoFisico toContenutoDocumentoFisico(ContenutoDocumentoFisicoActa input);

  IdentitaActa toIdentitaActa(
      it.csi.cosmo.cosmosoap.integration.soap.acta.model.IdentitaActa input);

  List<IdentitaActa> toIdentitaActas(
      List<it.csi.cosmo.cosmosoap.integration.soap.acta.model.IdentitaActa> input);

  @Mapping(target = "withId", source = "id")
  @Mapping(target = "withIdentificativoAOO", source = "identificativoAOO")
  @Mapping(target = "withCodiceAOO", source = "codiceAOO")
  @Mapping(target = "withDescrizioneAOO", source = "descrizioneAOO")
  @Mapping(target = "withIdentificativoNodo", source = "identificativoNodo")
  @Mapping(target = "withCodiceNodo", source = "codiceNodo")
  @Mapping(target = "withDescrizioneNodo", source = "descrizioneNodo")
  @Mapping(target = "withIdentificativoStruttura", source = "identificativoStruttura")
  @Mapping(target = "withCodiceStruttura", source = "codiceStruttura")
  @Mapping(target = "withDescrizioneStruttura", source = "descrizioneStruttura")
  it.csi.cosmo.cosmosoap.integration.soap.acta.model.IdentitaActa toIdentitaActa(
      IdentitaActa input);


  default byte[] toByte(DataHandler input) {

    try {
      return IOUtils.toByteArray(input.getInputStream());
    } catch (IOException e) {
      throw new InternalServerException("Impossibile repertire il contenuto del documento");

    }

  }
}
