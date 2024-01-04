/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.dto.dosign;

import java.io.Serializable;

public class VerifyInput implements Serializable {
	private static final long serialVersionUID = 1L;

	private byte[] content;
	private boolean verifyStrongSignature;
	private boolean verifyCrl;
	private boolean extractData;

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public boolean isVerifyStrongSignature() {
		return verifyStrongSignature;
	}

	public void setVerifyStrongSignature(boolean verifyStrongSignature) {
		this.verifyStrongSignature = verifyStrongSignature;
	}

	public boolean isVerifyCrl() {
		return verifyCrl;
	}

	public void setVerifyCrl(boolean verifyCrl) {
		this.verifyCrl = verifyCrl;
	}

	public boolean isExtractData() {
		return extractData;
	}

	public void setExtractData(boolean extractData) {
		this.extractData = extractData;
	}
}
