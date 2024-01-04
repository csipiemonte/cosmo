/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.dto.dosign;

import java.io.Serializable;
import java.util.List;

public class VerifyResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<VerifyInfo> verifyInfos;

	public List<VerifyInfo> getVerifyInfos() {
		return verifyInfos;
	}

	public void setVerifyInfos(List<VerifyInfo> verifyInfos) {
		this.verifyInfos = verifyInfos;
	}
}
