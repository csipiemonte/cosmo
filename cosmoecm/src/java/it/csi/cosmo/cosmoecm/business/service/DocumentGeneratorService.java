/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.service;

import java.util.Collection;
import java.util.Map;
import it.csi.cosmo.cosmoecm.dto.jasper.ContestoCreazioneDocumento;
import it.csi.cosmo.cosmoecm.integration.jasper.model.ExportFormat;


/**
 *
 */

public interface DocumentGeneratorService {

  byte[] render(Collection<?> data, ContestoCreazioneDocumento context);

  byte[] render(Object data, ContestoCreazioneDocumento context);

  byte[] render(Collection<?> data, Map<String, Object> parameters,
      ContestoCreazioneDocumento context);

  byte[] render(Object data, Map<String, Object> parameters, ContestoCreazioneDocumento context);

  byte[] render(Collection<?> data, Map<String, Object> parameters, ExportFormat outputFormat,
      ContestoCreazioneDocumento context);

  byte[] render(Object data, Map<String, Object> parameters, ExportFormat outputFormat,
      ContestoCreazioneDocumento context);

  byte[] render(String templateCode, Collection<?> data, ContestoCreazioneDocumento context);

  byte[] render(String templateCode, Object data, ContestoCreazioneDocumento context);

  byte[] render(String templateCode, Map<String, Object> parameters,
      ContestoCreazioneDocumento context);

  byte[] render(String templateCode, Collection<?> data, Map<String, Object> parameters,
      ContestoCreazioneDocumento context);

  byte[] render(String templateCode, Object data, Map<String, Object> parameters,
      ContestoCreazioneDocumento context);

  byte[] render(String templateCode, Collection<?> data, Map<String, Object> parameters,
      ExportFormat outputFormat, ContestoCreazioneDocumento context);

  byte[] render(String templateCode, Object data, Map<String, Object> parameters,
      ExportFormat outputFormat, ContestoCreazioneDocumento context);

  Object loadImage(String parentCode, String templateCode, String resourceCode,
      ContestoCreazioneDocumento context);

  Object loadSubreport(String parentCode, String resourceCode, ContestoCreazioneDocumento context);

  Map<String, Object> loadRisorsePerTemplate(String parentCode, String codiceTemplate,
      ContestoCreazioneDocumento context);

  String getMimeTypeByFormato(ExportFormat format);

}
