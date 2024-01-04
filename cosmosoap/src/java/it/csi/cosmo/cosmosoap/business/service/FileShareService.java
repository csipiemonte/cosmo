/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service;

import java.time.LocalDate;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;

/**
 *
 */

public interface FileShareService {

  RetrievedContent get(String fileUUID, LocalDate referenceDate);

}
