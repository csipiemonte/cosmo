/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.mapper;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.cosmosoap.dto.rest.AggiornaUnitaDocumentariaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.AggiornaUnitaDocumentariaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.CaricaUnitaDocumentariaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.CaricaUnitaDocumentariaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.AddUdOutput;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.StiloAllegato;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.UpdUdOutput;
import it.eng.auriga.repository2.webservices.addunitadoc.dto.AttributoAddizionaleType;
import it.eng.auriga.repository2.webservices.addunitadoc.dto.NewUD;
import it.eng.auriga.repository2.webservices.addunitadoc.dto.NewUD.RegistrazioneDaDare;
import it.eng.auriga.repository2.webservices.addunitadoc.dto.OggDiTabDiSistemaType;
import it.eng.auriga.repository2.webservices.updunitadoc.dto.EstremiXIdentificazioneUDType;
import it.eng.auriga.repository2.webservices.updunitadoc.dto.NuovoAllegatoUDType;
import it.eng.auriga.repository2.webservices.updunitadoc.dto.UDDaAgg;
import it.eng.auriga.repository2.webservices.updunitadoc.dto.VersioneElettronicaType;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {DateFormatsMapper.class},
componentModel = "spring")
public interface StiloMapper {

  @Mapping(source = "outputUD", target = "outputUnitaDocumentaria")
  @Mapping(source = "baseOutputWS", target = "baseOutputUnitaDocumentaria")
  @Mapping(source = "baseOutputWS.WSError", target = "baseOutputUnitaDocumentaria.wsError")
  @Mapping(source = "baseOutputWS.WSResult", target = "baseOutputUnitaDocumentaria.wsResult")
  CaricaUnitaDocumentariaResponse toCaricaUnitaDocumentariaResponse(AddUdOutput addUdOutput);


  @Mapping(source = "outputUD", target = "outputUnitaDocumentaria")
  @Mapping(source = "baseOutputWS", target = "baseOutputUnitaDocumentaria")
  @Mapping(source = "baseOutputWS.WSError", target = "baseOutputUnitaDocumentaria.wsError")
  @Mapping(source = "baseOutputWS.WSResult", target = "baseOutputUnitaDocumentaria.wsResult")
  AggiornaUnitaDocumentariaResponse toAggiornaUnitaDocumentariaResponse(UpdUdOutput addUdOutput);


  default NewUD mapCaricaUnitaDocumentariaRequestToNewUD(
      CaricaUnitaDocumentariaRequest caricaUnitaDocumentariaRequest) {

    if (caricaUnitaDocumentariaRequest == null)
      return null;

    NewUD newUD = new NewUD();

    RegistrazioneDaDare registrazioneDaDare = new RegistrazioneDaDare();
    registrazioneDaDare.setSiglaReg(caricaUnitaDocumentariaRequest.getRegistrazioneStilo());
    registrazioneDaDare.setCategoriaReg("I");
    newUD.getRegistrazioneDaDare().add(registrazioneDaDare);

    OggDiTabDiSistemaType tipoDoc = new OggDiTabDiSistemaType();
    tipoDoc.setDecodificaNome(caricaUnitaDocumentariaRequest.getTipoDocumentoStilo());
    newUD.setTipoDoc(tipoDoc);

    newUD.setOggettoUD(caricaUnitaDocumentariaRequest.getOggetto());

    newUD.setNroAllegati(
        BigInteger.valueOf(caricaUnitaDocumentariaRequest.getNroAllegati().longValue()));

    newUD.setNoteUD(caricaUnitaDocumentariaRequest.getNote());

    if (caricaUnitaDocumentariaRequest.getAttributi() != null) {
      caricaUnitaDocumentariaRequest.getAttributi().stream().forEach(tempAttributo -> {
        AttributoAddizionaleType attributoAddizionaleType = new AttributoAddizionaleType();
        attributoAddizionaleType.setNome(tempAttributo.getChiave());
        attributoAddizionaleType.setValoreSemplice(tempAttributo.getValore());
        newUD.getAttributoAddUD().add(attributoAddizionaleType);
      });

    }

    return newUD;
  }


  default UDDaAgg mapAggiornaUnitaDocumentariaRequestToUDDaAgg(
      AggiornaUnitaDocumentariaRequest aggiornaUnitaDocumentariaRequest,
      CosmoTContenutoDocumento contenutoDocumento, Entity documentoIndex,
      RetrievedContent retrievedFile) {
    if (aggiornaUnitaDocumentariaRequest == null)
      return null;


    UDDaAgg udDaAgg = new UDDaAgg();

    EstremiXIdentificazioneUDType estremiXIdentificazioneUDType =
        new EstremiXIdentificazioneUDType();
    estremiXIdentificazioneUDType
    .setIdUD(new BigInteger(aggiornaUnitaDocumentariaRequest.getIdUd()));
    udDaAgg.setEstremiXIdentificazioneUD(estremiXIdentificazioneUDType);


    if (contenutoDocumento != null) {
      NuovoAllegatoUDType nuovoAllegatoUDType = new NuovoAllegatoUDType();
      nuovoAllegatoUDType.setDesAllegato(documentoIndex != null ?
          StringUtils.isNotEmpty(documentoIndex.getDescrizione()) ? documentoIndex.getDescrizione()
              : StringUtils.isNotEmpty(documentoIndex.getFilename()) ? documentoIndex.getFilename()
                  : contenutoDocumento.getNomeFile()
                  : retrievedFile.getFilename());


      VersioneElettronicaType versioneElettronicaType = new VersioneElettronicaType();
      versioneElettronicaType.setNroAttachmentAssociato(new BigInteger("1"));
      versioneElettronicaType.setNomeFile(documentoIndex != null ?
          StringUtils.isNotEmpty(documentoIndex.getFilename()) ? documentoIndex.getFilename()
              : contenutoDocumento.getNomeFile()
          : retrievedFile.getFilename());
      nuovoAllegatoUDType.setVersioneElettronica(versioneElettronicaType);


      udDaAgg.getNuovoAllegatoUD().add(nuovoAllegatoUDType);
    }

    if (aggiornaUnitaDocumentariaRequest.getAttributi() != null) {
      aggiornaUnitaDocumentariaRequest.getAttributi().stream().forEach(tempAttributo -> {
        it.eng.auriga.repository2.webservices.updunitadoc.dto.AttributoAddizionaleType attributoAddizionaleType =
            new it.eng.auriga.repository2.webservices.updunitadoc.dto.AttributoAddizionaleType();
        attributoAddizionaleType.setNome(tempAttributo.getChiave());
        attributoAddizionaleType.setValoreSemplice(tempAttributo.getValore());
        udDaAgg.getAttributoAddUD().add(attributoAddizionaleType);
      });

    }



    return udDaAgg;


  }


  default StiloAllegato mapStiloAllegato(CosmoTContenutoDocumento contenutoDocumento,
      Entity documentoIndex, RetrievedContent retrievedFile) {

    StiloAllegato stiloAllegato = new StiloAllegato();
    stiloAllegato
    .setContent(documentoIndex != null ? new ByteArrayInputStream(documentoIndex.getContent())
        : retrievedFile.getContentStream());
    stiloAllegato.setContentType(
        documentoIndex != null ? documentoIndex.getMimeType() : retrievedFile.getContentType());
    stiloAllegato.setFileName(documentoIndex != null ?
        (StringUtils.isNotEmpty(documentoIndex.getFilename()) ? documentoIndex.getFilename()
            : contenutoDocumento.getNomeFile())
        : retrievedFile.getFilename());


    return stiloAllegato;


  }



}
