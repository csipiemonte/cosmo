/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.internal;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.ConformitaParametriInput;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.FormatoFirma;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.IndexSignature;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.IndexSignatureVerificationError;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.IndexVerifyReport;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.TipoCertificato;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.TipoFirma;
import it.doqui.index.ecmengine.mtom.dto.Signature;
import it.doqui.index.ecmengine.mtom.dto.VerifyReport;

/**
 *
 */

public class IndexMapper {

  public IndexMapper() {
    // NOP
  }

  public IndexVerifyReport map(VerifyReport input) {
    if (input == null) {
      return null;
    }

    //@formatter:off
    return IndexVerifyReport.builder()
        .withChild(map(input.getChild()))
        .withConformitaParametriInput(conformitaParametriInput(input.getConformitaParametriInput()))
        .withErrorCode(errorCode(input.getErrorCode()))
        .withFormatoFirma(formatoFirma(input.getFormatoFirma()))
        .withNumCertificatiFirma(input.getNumCertificatiFirma())
        .withNumCertificatiMarca(input.getNumCertificatiMarca())
        .withSignature(
            safe(input.getSignature()).stream()
            .map(this::signature)
            .collect(Collectors.toCollection(LinkedList::new)))
        .withTipoFirma(tipoFirma(input.getTipoFirma()))
        .withValida(input.getErrorCode() == 0)
        .build();
    //@formatter:on
  }

  private IndexSignature signature(Signature input) {
    if (input == null) {
      return null;
    }

    //@formatter:off
    return IndexSignature.builder()
        .withCert(input.getCert())
        .withCertificateAuthority(clean(input.getCa()))
        .withCodiceFiscale(clean(input.getCodiceFiscale()))
        .withDataOra(offsetDateTime(input.getDataOra()))
        .withDataOraVerifica(localDateTimeFromDMYHMS(input.getDataOraVerifica()))
        .withDipartimento(clean(input.getDipartimento()))
        .withDnQualifier(clean(input.getDnQualifier()))
        .withErrorCode(errorCode(input.getErrorCode()))
        .withFineValidita(clean(input.getFineValidita()))
        .withFirmatario(clean(input.getFirmatario()))
        .withGivenName(clean(input.getGivenname()))
        .withSurname(clean(input.getSurname()))
        .withInizioValidita(clean(input.getInizioValidita()))
        .withNominativoFirmatario(clean(input.getNominativoFirmatario()))
        .withNumeroControfirme(input.getNumeroControfirme())
        .withOrganizzazione(clean(input.getOrganizzazione()))
        .withPaese(clean(input.getPaese()))
        .withSerialNumber(clean(input.getSerialNumber()))
        .withSignature(
            safe(input.getSignature()).stream()
            .map(this::signature)
            .collect(Collectors.toCollection(LinkedList::new)))
        .withTipoCertificato(tipoCertificato(input.getTipoCertificato()))
        .withTipoFirma(tipoFirma((int)input.getTipoFirma()))
        .withTimestamped(input.isTimestamped())
        .withValida(input.getErrorCode() == 0)
        .build();
    //@formatter:on
  }

  private String clean(String input) {
    if (input == null) {
      return input;
    }
    input = input.trim();
    if (StringUtils.isBlank(input)) {
      return null;
    }
    return input;
  }

  private LocalDateTime localDateTimeFromDMYHMS(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    // "12/11/2020 16:14:04"
    return LocalDateTime.parse(raw, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
  }

  private OffsetDateTime offsetDateTime(Date dataOra) {
    if (dataOra == null) {
      return null;
    }
    return dataOra.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
  }

  private TipoCertificato tipoCertificato(int tipoCertificato) {
    return TipoCertificato.byValue(tipoCertificato);
  }

  private <T> List<T> safe(T[] input) {
    if (input == null) {
      return new LinkedList<>();
    }
    return Arrays.asList(input);
  }

  private FormatoFirma formatoFirma(int formatoFirma) {
    return FormatoFirma.byValue(formatoFirma);
  }

  private ConformitaParametriInput conformitaParametriInput(int conformitaParametriInput) {
    return ConformitaParametriInput.byValue(conformitaParametriInput);
  }

  private IndexSignatureVerificationError errorCode(int errorCode) {
    if (errorCode == 0) {
      return null;
    }
    IndexSignatureVerificationError candidate = IndexSignatureVerificationError.byValue(errorCode);
    if (candidate == null) {
      candidate = IndexSignatureVerificationError.IGNOTO;
    }
    return candidate;
  }

  private TipoFirma tipoFirma(int raw) {
    return TipoFirma.byValue(raw);
  }

}
