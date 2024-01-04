/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.dto.dosign;

import java.io.Serializable;
import java.util.List;

public class PolicyInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String cpsUri;
	private String explicitText;
	private List<Integer> noticeNumbers;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCpsUri() {
		return cpsUri;
	}

	public void setCpsUri(String cpsUri) {
		this.cpsUri = cpsUri;
	}

	public String getExplicitText() {
		return explicitText;
	}

	public void setExplicitText(String explicitText) {
		this.explicitText = explicitText;
	}

	public List<Integer> getNoticeNumbers() {
		return noticeNumbers;
	}

	public void setNoticeNumbers(List<Integer> noticeNumbers) {
		this.noticeNumbers = noticeNumbers;
	}
}
