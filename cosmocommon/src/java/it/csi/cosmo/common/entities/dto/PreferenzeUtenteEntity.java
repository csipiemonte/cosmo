/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities.dto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.dto.rest.ImpostazioniFirmaDTO;
import it.csi.cosmo.common.util.ObjectUtils;

/**
 *
 */

public class PreferenzeUtenteEntity implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -7987518371664019948L;

  private Integer maxPageSize;

  private String posizioneToast;

  private transient JsonNode home;

  private transient JsonNode ricezioneNotifiche;

  private transient ImpostazioniFirmaDTO impostazioniFirma;

  private transient JsonNode segnalibri;

  public PreferenzeUtenteEntity() {
    // EMPTY
  }

  public Integer getMaxPageSize() {
    return maxPageSize;
  }

  public void setMaxPageSize(Integer maxPageSize) {
    this.maxPageSize = maxPageSize;
  }

  public String getPosizioneToast() {
    return posizioneToast;
  }

  public void setPosizioneToast(String posizioneToast) {
    this.posizioneToast = posizioneToast;
  }

  public JsonNode getHome() {
    return home;
  }

  public void setHome(JsonNode home) {
    this.home = home;
  }

  public JsonNode getRicezioneNotifiche() {
    return ricezioneNotifiche;
  }

  public void setRicezioneNotifiche(JsonNode ricezioneNotifiche) {
    this.ricezioneNotifiche = ricezioneNotifiche;
  }

  public ImpostazioniFirmaDTO getImpostazioniFirma() {
    return impostazioniFirma;
  }

  public void setImpostazioniFirma(ImpostazioniFirmaDTO impostazioniFirma) {
    this.impostazioniFirma = impostazioniFirma;
  }

  public JsonNode getSegnalibri() {
    return segnalibri;
  }

  public void setSegnalibri(JsonNode segnalibri) {
    this.segnalibri = segnalibri;
  }

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();

    oos.writeObject(ObjectUtils.toJson(home));
    oos.writeObject(ObjectUtils.toJson(ricezioneNotifiche));
    oos.writeObject(ObjectUtils.toJson(impostazioniFirma));
    oos.writeObject(ObjectUtils.toJson(segnalibri));

  }

  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();

    String serialized = (String) ois.readObject();
    if (serialized != null) {
      home = ObjectUtils.fromJson(serialized, JsonNode.class);
      ricezioneNotifiche = ObjectUtils.fromJson(serialized, JsonNode.class);
      segnalibri = ObjectUtils.fromJson(serialized, JsonNode.class);
    }
  }
}
