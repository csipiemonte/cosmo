/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.dto.dosign;

import java.io.Serializable;
import java.util.List;

public class XmlObjectIdentifier implements Serializable {
	private static final long serialVersionUID = 1L;

	private String identifier;
	private String description;
	private int qualifier;
	private List<String> documentationReferences;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQualifier() {
		return qualifier;
	}

	public void setQualifier(int qualifier) {
		this.qualifier = qualifier;
	}

	public List<String> getDocumentationReferences() {
		return documentationReferences;
	}

	public void setDocumentationReferences(List<String> documentationReferences) {
		this.documentationReferences = documentationReferences;
	}
}
