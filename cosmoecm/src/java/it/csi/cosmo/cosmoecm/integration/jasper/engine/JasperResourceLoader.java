/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.jasper.engine;

import java.io.InputStream;
import it.csi.cosmo.cosmoecm.dto.jasper.ContestoCreazioneDocumento;

/**
 *
 */
public interface JasperResourceLoader {

  InputStream findResourceFromDB(String parentCode, String templateCode, String resourceCode,
      ContestoCreazioneDocumento context);

  InputStream findTemplateFromDB(String parentCode, String templateCode,
      ContestoCreazioneDocumento context);

  InputStream findTemplateSourceFromDB(String parentCode, String templateCode,
      ContestoCreazioneDocumento context);

  void saveCompiledTemplateToDB(String parentCode, String templateCode, byte[] compiledTemplate,
      ContestoCreazioneDocumento context);

}
