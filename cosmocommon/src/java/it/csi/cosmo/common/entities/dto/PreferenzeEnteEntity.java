/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities.dto;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import it.csi.cosmo.common.dto.rest.ImpostazioniFirmaDTO;
import it.csi.cosmo.common.util.ObjectUtils;

/**
 *
 */

public class PreferenzeEnteEntity implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -7987518371664019948L;

  private String header;

  private String logo;

  private String logoFooter;

  private String logoFooterCentrale;

  private String logoFooterDestra;

  private Long dimensioneMassimaAllegatiEmail;

  private String[] widgets;

  private Boolean isWidgetModificabile;

  private String iconaFea;


  private transient ImpostazioniFirmaDTO impostazioniFirma;

  public PreferenzeEnteEntity() {
    // EMPTY
  }

  public Long getDimensioneMassimaAllegatiEmail() {
    return dimensioneMassimaAllegatiEmail;
  }

  public void setDimensioneMassimaAllegatiEmail(Long dimensioneMassimaAllegatiEmail) {
    this.dimensioneMassimaAllegatiEmail = dimensioneMassimaAllegatiEmail;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public String getLogoFooter() {
    return logoFooter;
  }

  public void setLogoFooter(String logoFooter) {
    this.logoFooter = logoFooter;
  }

  public String getLogoFooterCentrale() {
    return logoFooterCentrale;
  }

  public void setLogoFooterCentrale(String logoFooterCentrale) {
    this.logoFooterCentrale = logoFooterCentrale;
  }

  public String getLogoFooterDestra() {
    return logoFooterDestra;
  }

  public void setLogoFooterDestra(String logoFooterDestra) {
    this.logoFooterDestra = logoFooterDestra;
  }


  public ImpostazioniFirmaDTO getImpostazioniFirma() {
    return impostazioniFirma;
  }

  public void setImpostazioniFirma(ImpostazioniFirmaDTO impostazioniFirma) {
    this.impostazioniFirma = impostazioniFirma;
  }

  public String[] getWidgets() {
    return widgets;
  }

  public void setWidgets(String[] widgets) {
    this.widgets = widgets;
  }

  public Boolean getIsWidgetModificabile() {
    return isWidgetModificabile;
  }

  public void setIsWidgetModificabile(Boolean isWidgetModificabile) {
    this.isWidgetModificabile = isWidgetModificabile;
  }

  public String getIconaFea() {
    return iconaFea;
  }

  public void setIconaFea(String iconaFea) {
    this.iconaFea = iconaFea;
  }

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();

    oos.writeObject(ObjectUtils.toJson(impostazioniFirma));

  }


}
