/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.activation.DataHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.soap.util.mime.ByteArrayDataSource;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.cosmosoap.integration.soap.acta.ActaClientHelper;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.ActaClientContext;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.DocumentoActa;
import it.doqui.acta.acaris.archive.ClassificazionePropertiesType;
import it.doqui.acta.acaris.archive.ContenutoFisicoPropertiesType;
import it.doqui.acta.acaris.archive.DocumentoFisicoPropertiesType;
import it.doqui.acta.acaris.archive.GruppoAllegatiPropertiesType;
import it.doqui.acta.acaris.common.AcarisContentStreamType;
import it.doqui.acta.acaris.common.EnumMimeTypeType;
import it.doqui.acta.acaris.common.EnumStreamId;
import it.doqui.acta.acaris.common.PropertiesType;
import it.doqui.acta.acaris.documentservice.ContenutoFisicoIRC;
import it.doqui.acta.acaris.documentservice.DocumentoArchivisticoIRC;
import it.doqui.acta.acaris.documentservice.DocumentoFisicoIRC;
import it.doqui.acta.acaris.documentservice.InfoRichiestaCreazione;


/**
 *
 */

public abstract class ActaPayloadMapper {

  public static InfoRichiestaCreazione map(String parentId, DocumentoActa input,
      PropertiesType propertiesActa, ActaClientContext contesto) {
    DocumentoArchivisticoIRC output = new DocumentoArchivisticoIRC();

    ClassificazionePropertiesType associativeObjectProperties = new ClassificazionePropertiesType();
    DocumentoFisicoIRC[] documenti = new DocumentoFisicoIRC[1];

    output.setTipoDocumento(input.getTipoDocumento());
    output.setAllegato(bool(input.getAllegato()));

    associativeObjectProperties.setCopiaCartacea(bool(input.getCopiaCartacea()));
    associativeObjectProperties.setCollocazioneCartacea(input.getCollocazioneCartacea());
    associativeObjectProperties.setCartaceo(bool(input.getCartaceo()));

    documenti[0] = new DocumentoFisicoIRC();
    DocumentoFisicoPropertiesType documentoFisicoProperty = new DocumentoFisicoPropertiesType();
    documentoFisicoProperty.setDescrizione(input.getFileName());
    documentoFisicoProperty
        .setDataMemorizzazione(toXMLGregorianCalendar(input.getDataMemorizzazione()));
    documentoFisicoProperty.setDocMimeTypes(
        input.getMimeType() != null ? "" + EnumMimeTypeType.fromValue(input.getMimeType()).ordinal()
            : null);
    documentoFisicoProperty.setProgressivoPerDocumento(integer(input.getProgressivoPerDocumento()));
    documenti[0].setPropertiesDocumentoFisico(documentoFisicoProperty);

    ContenutoFisicoIRC[] contenuti = new ContenutoFisicoIRC[1];
    contenuti[0] = new ContenutoFisicoIRC();
    ContenutoFisicoPropertiesType contenutoFisicoPropertiesType =
        new ContenutoFisicoPropertiesType();
    contenutoFisicoPropertiesType.setDocPrimario(true);
    contenutoFisicoPropertiesType.setContentStreamFilename(input.getFileName());
    contenutoFisicoPropertiesType.setContentStreamMimeType(
        input.getMimeType() != null ? EnumMimeTypeType.fromValue(input.getMimeType()) : null);
    contenutoFisicoPropertiesType.setModificabile(bool(input.getContenutoFisicoModificabile()));
    contenutoFisicoPropertiesType.setSbustamento(bool(input.getContenutoFisicoSbustamento()));
    contenutoFisicoPropertiesType.setWorkingCopy(bool(input.getContenutoFisicoWorkingCopy()));
    contenuti[0].setPropertiesContenutoFisico(contenutoFisicoPropertiesType);

    AcarisContentStreamType contentStream = new AcarisContentStreamType();
    ByteArrayDataSource dataDs = new ByteArrayDataSource(input.getContent(), input.getMimeType());
    DataHandler dataHandler = new DataHandler(dataDs);
    contentStream.setStreamMTOM(dataHandler);
    contentStream.setFilename(input.getFileName());
    contentStream.setMimeType(
        input.getMimeType() != null ? EnumMimeTypeType.fromValue(input.getMimeType()) : null);
    contenuti[0].setStream(contentStream);
    contenuti[0].setTipo(EnumStreamId.PRIMARY);

    for (ContenutoFisicoIRC contenuto : contenuti) {
      documenti[0].getContenutiFisici().add(contenuto);
    }

    output.setPropertiesClassificazione(associativeObjectProperties);

    for (DocumentoFisicoIRC documento : documenti) {
      output.getDocumentiFisici().add(documento);
    }

    if (input.getGruppoAllegati().getNumeroAllegati() > 0) {
      GruppoAllegatiPropertiesType allegati = input.getGruppoAllegati();
      output.setGruppoAllegati(allegati);
    }
    output.setPropertiesDocumento(propertiesActa);
    if (!output.isAllegato()) {
      output.setParentFolderId(ActaClientHelper.wrapId(parentId));
    } else {
      output.setClassificazionePrincipale(ActaClientHelper.wrapId(parentId));
    }
    return output;
  }

  private static Date date(LocalDate input) {
    return input == null ? null : Date.from(input.atStartOfDay().toInstant(ZoneOffset.UTC));
  }

  private static XMLGregorianCalendar toXMLGregorianCalendar(LocalDate input) {
    GregorianCalendar gcal = GregorianCalendar.from(input.atStartOfDay(ZoneId.systemDefault()));
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
    } catch (DatatypeConfigurationException e) {
      throw ExceptionUtils.toChecked(e);
    }
  }

  private static boolean bool(Boolean input) {
    if (input == null) {
      return false;
    } else {
      return input.booleanValue();
    }
  }

  private static int integer(Integer input) {
    if (input == null) {
      return 0;
    } else {
      return input.intValue();
    }
  }
}
