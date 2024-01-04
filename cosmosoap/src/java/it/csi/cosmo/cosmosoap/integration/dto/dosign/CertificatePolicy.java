/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.dto.dosign;

import java.io.Serializable;
import java.util.List;

public class CertificatePolicy implements Serializable {
	private static final long serialVersionUID = 1L;

	private String oid;
	private int policyInfoCount;
	private List<PolicyInfo> policyInfos;

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public int getPolicyInfoCount() {
		return policyInfoCount;
	}

	public void setPolicyInfoCount(int policyInfoCount) {
		this.policyInfoCount = policyInfoCount;
	}

	public List<PolicyInfo> getPolicyInfos() {
		return policyInfos;
	}

	public void setPolicyInfos(List<PolicyInfo> policyInfos) {
		this.policyInfos = policyInfos;
	}
}
