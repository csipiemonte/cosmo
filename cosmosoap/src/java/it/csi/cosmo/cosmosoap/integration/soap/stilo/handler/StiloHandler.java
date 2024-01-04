/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.stilo.handler;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.activation.DataHandler;
import javax.ws.rs.InternalServerErrorException;
import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.soap.util.mime.ByteArrayDataSource;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.StiloAllegato;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;

/**
 *
 */

public class StiloHandler implements SOAPHandler<SOAPMessageContext> {


  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.SOAP_LOG_CATEGORY, "StiloHandler");

  private List<StiloAllegato> allegati;
  private StiloAllegato allegatoResponse;

  @Override
  public boolean handleMessage(SOAPMessageContext context) {

    Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    SOAPMessage soapMsg = context.getMessage();

    // if this is a request, true for outbound messages, false for inbound
    if (Boolean.TRUE.equals(isRequest)) {


      this.collectionAsStream(this.allegati).forEach(allegatoTemp -> {
        AttachmentPart attachmentPart = soapMsg.createAttachmentPart();
        ByteArrayDataSource source = null;
        try {
          source =
              new ByteArrayDataSource(allegatoTemp.getContent(), allegatoTemp.getContentType());
        } catch (IOException e) {
          logger.error("handleMessage",
              "Problemi nella conversione del documento " + allegatoTemp.getFileName());
          throw new InternalServerErrorException(
              "Problemi nella conversione del documento " + allegatoTemp.getFileName(), e);
        }

        DataHandler handler = new DataHandler(source);
        attachmentPart.setContentId(allegatoTemp.getContentId());
        attachmentPart.setDataHandler(handler);
        String fileName =
            StringUtils.isNotEmpty(allegatoTemp.getFileName()) ? allegatoTemp.getFileName()
                : allegatoTemp.getContentId();
        String contentDisposition =
            "Content-Disposition: attachment; name=\"" + fileName + "\"";
        attachmentPart.addMimeHeader("Content-Disposition", contentDisposition);
        soapMsg.addAttachmentPart(attachmentPart);

        try {
          soapMsg.saveChanges();
        } catch (SOAPException e) {
          logger.error("handleMessage",
              "Problemi nella creazione degli allegati del messaggio soap", e);
          throw new InternalServerErrorException(
              "Problemi nella creazione degli allegati del messaggio soap", e);
        }
      });

    }

    else {

      AttachmentPart responseAttachment = (AttachmentPart) soapMsg.getAttachments().next();
      StiloAllegato responseStiloAllegato = new StiloAllegato();
      responseStiloAllegato.setContentId(responseAttachment.getContentId());
      responseStiloAllegato.setContentType(responseAttachment.getContentType());
      try {
        responseStiloAllegato.setContent(responseAttachment.getDataHandler().getInputStream());
        responseStiloAllegato.setFileName(responseAttachment.getDataHandler().getName());
      } catch (IOException | SOAPException e) {
        logger.error("handleMessage", "Problemi nella ricezione degli allegati del messaggio soap",
            e);
        throw new InternalServerErrorException(
            "Problemi nella ricezione degli allegati del messaggio soap", e);
      }


      this.allegatoResponse = responseStiloAllegato;

    }

    return true;
  }

  @Override
  public boolean handleFault(SOAPMessageContext context) {
    return true;
  }

  @Override
  public void close(MessageContext context) {
  }

  @Override
  public Set<QName> getHeaders() {
    return Collections.emptySet();
  }

  public List<StiloAllegato> getAllegati() {
    return allegati;
  }

  public void setAllegati(List<StiloAllegato> allegati) {
    this.allegati = allegati;
  }


  public StiloAllegato getAllegatoResponse() {
    return this.allegatoResponse;
  }


  private Stream<StiloAllegato> collectionAsStream(Collection<StiloAllegato> collection) {
    return Optional.ofNullable(collection)
        .map(Collection::stream)
        .orElseGet(Stream::empty);
  }
}



