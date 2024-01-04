/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.aspect;

import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexProperty;

/**
 *
 */

@IndexAspect(value = "cm:versionable")
public class VersionableAspect {

  @IndexProperty(value = "cm:versionLabel", required = false, readOnly = true)
  protected String versionLabel;

  @IndexProperty(value = "cm:initialVersion", required = false, readOnly = true)
  protected Boolean initialVersion;

  @IndexProperty(value = "cm:autoVersion", required = false, readOnly = true)
  protected Boolean autoVersion;

  public VersionableAspect() {
    // NOP
  }

  @JsonProperty("isVersioned")
  public boolean isVersioned() {
    return !StringUtils.isBlank(versionLabel);
  }

  public String getVersionLabel() {
    return versionLabel;
  }

  public void setVersionLabel(String versionLabel) {
    this.versionLabel = versionLabel;
  }

  public Boolean getInitialVersion() {
    return initialVersion;
  }

  public void setInitialVersion(Boolean initialVersion) {
    this.initialVersion = initialVersion;
  }

  public Boolean getAutoVersion() {
    return autoVersion;
  }

  public void setAutoVersion(Boolean autoVersion) {
    this.autoVersion = autoVersion;
  }

  @Override
  public String toString() {
    return "VersionableAspect [versionLabel=" + versionLabel + ", initialVersion=" + initialVersion
        + ", autoVersion=" + autoVersion + "]";
  }

}
