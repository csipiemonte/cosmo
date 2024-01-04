/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.dto.dosign;

import java.io.Serializable;

public class XmlTransformation implements Serializable {
	private static final long serialVersionUID = 1L;

	private String algorithm;
	private byte[] data;
	private String expression;
	private String filter;
	private VerifyInfo verifyInfo;

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public VerifyInfo getVerifyInfo() {
		return verifyInfo;
	}

	public void setVerifyInfo(VerifyInfo verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
}
