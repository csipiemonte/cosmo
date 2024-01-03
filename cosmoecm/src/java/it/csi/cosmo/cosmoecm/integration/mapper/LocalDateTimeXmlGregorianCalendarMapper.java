/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmoecm.integration.mapper;

import java.time.LocalDateTime;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.mapstruct.Mapper;

/**
 * Mapper astratto per la trasformazione da Boolean a String e viceversa.
 */
@Mapper
public interface LocalDateTimeXmlGregorianCalendarMapper {

  default LocalDateTime parseLocalDate(XMLGregorianCalendar source) {
    return source != null ? source.toGregorianCalendar().toZonedDateTime().toLocalDateTime() : null;
  }

  default XMLGregorianCalendar parseXMLGregorianCalendar(LocalDateTime source) throws DatatypeConfigurationException {
    return source != null ? DatatypeFactory.newInstance().newXMLGregorianCalendar(source.toString()) : null;
  }
}
