/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.dto.dosign;

import java.io.Serializable;

public class XmlDataObjectFormat implements Serializable {
	private static final long serialVersionUID = 1L;

	private String encoding;
	private String description;
	private String mimetype;
	private XmlObjectIdentifier objectIdentifier;

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public XmlObjectIdentifier getObjectIdentifier() {
		return objectIdentifier;
	}

	public void setObjectIdentifier(XmlObjectIdentifier objectIdentifier) {
		this.objectIdentifier = objectIdentifier;
	}
}
