/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.dto.dosign;

import java.io.Serializable;
import java.util.List;

public class VerifyInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private byte[] data;
	private int envelopeCompliance;
	private int envelopeComplianceCode;
	private String envelopeComplianceInfo;
	private int invalidSignCount;
	private List<Signer> signers;
	private int signerCount;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getEnvelopeCompliance() {
		return envelopeCompliance;
	}

	public void setEnvelopeCompliance(int envelopeCompliance) {
		this.envelopeCompliance = envelopeCompliance;
	}

	public int getEnvelopeComplianceCode() {
		return envelopeComplianceCode;
	}

	public void setEnvelopeComplianceCode(int envelopeComplianceCode) {
		this.envelopeComplianceCode = envelopeComplianceCode;
	}

	public String getEnvelopeComplianceInfo() {
		return envelopeComplianceInfo;
	}

	public void setEnvelopeComplianceInfo(String envelopeComplianceInfo) {
		this.envelopeComplianceInfo = envelopeComplianceInfo;
	}

	public int getInvalidSignCount() {
		return invalidSignCount;
	}

	public void setInvalidSignCount(int invalidSignCount) {
		this.invalidSignCount = invalidSignCount;
	}

	public List<Signer> getSigners() {
		return signers;
	}

	public void setSigners(List<Signer> signers) {
		this.signers = signers;
	}

	public int getSignerCount() {
		return signerCount;
	}

	public void setSignerCount(int signerCount) {
		this.signerCount = signerCount;
	}
}
