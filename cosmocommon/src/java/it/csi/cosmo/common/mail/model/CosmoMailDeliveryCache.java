/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.mail.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;

/**
 *
 */

public class CosmoMailDeliveryCache {

  private static final int MAX_ELEMENTS = 100;

  LinkedHashMap<CosmoMail, CosmoMailDeliveryCache.SentMailRecord> cache = new LinkedHashMap<>() {
    /**
     *
     */
    private static final long serialVersionUID = -7461718503051603046L;

    @Override
    protected boolean removeEldestEntry(
        java.util.Map.Entry<CosmoMail, CosmoMailDeliveryCache.SentMailRecord> entry) {
      return size() > MAX_ELEMENTS;
    }

  };

  public CosmoMailDeliveryCache() {
    // NOP
  }

  public void add(CosmoMail mail) {
    SentMailRecord record = new SentMailRecord();
    this.cache.put(mail, record);
  }

  public boolean isCached(CosmoMail mail, int seconds) {
    SentMailRecord record = this.cache.getOrDefault(mail, null);
    if (record == null) {
      return false;
    }

    long passed = record.sentTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);
    return (passed <= seconds);
  }

  private class SentMailRecord {
    LocalDateTime sentTime;

    public SentMailRecord() {
      sentTime = LocalDateTime.now();
    }
  }

}
