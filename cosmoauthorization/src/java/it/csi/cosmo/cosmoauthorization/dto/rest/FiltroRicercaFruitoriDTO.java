/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.dto.rest;

import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaFruitoriDTO {

  private LongFilter id;
  private StringFilter apiManagerId;
  private StringFilter nomeApp;
  private StringFilter url;
  private StringFilter fullText;

  public LongFilter getId() {
    return id;
  }

  public void setId(LongFilter id) {
    this.id = id;
  }

  public StringFilter getApiManagerId() {
    return apiManagerId;
  }

  public void setApiManagerId(StringFilter apiManagerId) {
    this.apiManagerId = apiManagerId;
  }

  public StringFilter getNomeApp() {
    return nomeApp;
  }

  public void setNomeApp(StringFilter nomeApp) {
    this.nomeApp = nomeApp;
  }

  public StringFilter getUrl() {
    return url;
  }

  public void setUrl(StringFilter url) {
    this.url = url;
  }

  public StringFilter getFullText() {
    return fullText;
  }

  public void setFullText(StringFilter fullText) {
    this.fullText = fullText;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((apiManagerId == null) ? 0 : apiManagerId.hashCode());
    result = prime * result + ((fullText == null) ? 0 : fullText.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((nomeApp == null) ? 0 : nomeApp.hashCode());
    result = prime * result + ((url == null) ? 0 : url.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FiltroRicercaFruitoriDTO other = (FiltroRicercaFruitoriDTO) obj;
    if (apiManagerId == null) {
      if (other.apiManagerId != null)
        return false;
    } else if (!apiManagerId.equals(other.apiManagerId))
      return false;
    if (fullText == null) {
      if (other.fullText != null)
        return false;
    } else if (!fullText.equals(other.fullText))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (nomeApp == null) {
      if (other.nomeApp != null)
        return false;
    } else if (!nomeApp.equals(other.nomeApp))
      return false;
    if (url == null) {
      if (other.url != null)
        return false;
    } else if (!url.equals(other.url))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FiltroRicercaFruitoriDTO [id=" + id + ", apiManagerId=" + apiManagerId + ", nomeApp="
        + nomeApp + ", url=" + url + ", fullText=" + fullText + "]";
  }

}
