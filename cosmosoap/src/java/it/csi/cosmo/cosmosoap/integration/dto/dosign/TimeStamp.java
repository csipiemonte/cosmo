/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.dto.dosign;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TimeStamp implements Serializable {
	private static final long serialVersionUID = 1L;

	private int compliance;
	private int complianceCode;
	private String complianceInfo;
	private byte[] encodedTst;
	private LocalDateTime genTime;
	private int hashAlgorithm;
	private byte[] hashValue;
	private String policyId;
	private String serialNumber;
	private Signer signer;
	private int tsTokenLength;

	public int getCompliance() {
		return compliance;
	}

	public void setCompliance(int compliance) {
		this.compliance = compliance;
	}

	public int getComplianceCode() {
		return complianceCode;
	}

	public void setComplianceCode(int complianceCode) {
		this.complianceCode = complianceCode;
	}

	public String getComplianceInfo() {
		return complianceInfo;
	}

	public void setComplianceInfo(String complianceInfo) {
		this.complianceInfo = complianceInfo;
	}

	public byte[] getEncodedTst() {
		return encodedTst;
	}

	public void setEncodedTst(byte[] encodedTst) {
		this.encodedTst = encodedTst;
	}

	public LocalDateTime getGenTime() {
		return genTime;
	}

	public void setGenTime(LocalDateTime genTime) {
		this.genTime = genTime;
	}

	public int getHashAlgorithm() {
		return hashAlgorithm;
	}

	public void setHashAlgorithm(int hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}

	public byte[] getHashValue() {
		return hashValue;
	}

	public void setHashValue(byte[] hashValue) {
		this.hashValue = hashValue;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Signer getSigner() {
		return signer;
	}

	public void setSigner(Signer signer) {
		this.signer = signer;
	}

	public int getTsTokenLength() {
		return tsTokenLength;
	}

	public void setTsTokenLength(int tsTokenLength) {
		this.tsTokenLength = tsTokenLength;
	}
}
