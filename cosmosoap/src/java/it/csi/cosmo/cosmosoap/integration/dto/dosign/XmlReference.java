/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.dto.dosign;

import java.io.Serializable;
import java.util.List;

public class XmlReference implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private XmlDataObjectFormat dataObjectFormat;
	private String insertData;
	private XmlReferenceData referenceData;
	private String type;
	private String uri;
	private List<XmlTransformation> transformations;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public XmlDataObjectFormat getDataObjectFormat() {
		return dataObjectFormat;
	}

	public void setDataObjectFormat(XmlDataObjectFormat dataObjectFormat) {
		this.dataObjectFormat = dataObjectFormat;
	}

	public String getInsertData() {
		return insertData;
	}

	public void setInsertData(String insertData) {
		this.insertData = insertData;
	}

	public XmlReferenceData getReferenceData() {
		return referenceData;
	}

	public void setReferenceData(XmlReferenceData referenceData) {
		this.referenceData = referenceData;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public List<XmlTransformation> getTransformations() {
		return transformations;
	}

	public void setTransformations(List<XmlTransformation> transformations) {
		this.transformations = transformations;
	}
}
