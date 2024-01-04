/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.parser;

import org.apache.commons.lang3.StringUtils;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.ContenutoDocumentoFisicoActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ContenutoDocumentoFisicoActa;
import it.doqui.acta.acaris.common.AcarisContentStreamType;

/**
 *
 */

public interface ActaContentConverter {

  public static ContenutoDocumentoFisicoActa acarisContentStreamTypeToContenutoFisico(
      AcarisContentStreamType input) {
    if (input == null) {
      return null;
    }
    
    String filename = null;
    String mimeType = null;

    var stream = input.getStreamMTOM();

    if (input.getStreamMTOM() != null) {

      filename = stream.getName();
      mimeType = stream.getContentType();
    }

    if (StringUtils.isBlank(filename)) {
      filename = input.getFilename();
    }
    if (StringUtils.isBlank(mimeType)) {
      mimeType = input.getMimeType() != null ? input.getMimeType().value() : null;
    }

    return ContenutoDocumentoFisicoActaDefaultImpl.builder()
        .withFileName(filename)
        .withMimeType(mimeType)
        .withStream(input.getStreamMTOM())
        .build();
  }

}
