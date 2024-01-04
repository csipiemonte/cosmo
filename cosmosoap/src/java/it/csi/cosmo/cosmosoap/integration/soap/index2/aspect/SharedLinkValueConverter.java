/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.aspect;

import java.time.OffsetDateTime;
import org.apache.commons.lang3.StringUtils;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.converter.IndexValueConverter;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexShareScope;

/**
 *
 */

public class SharedLinkValueConverter implements IndexValueConverter<IndexShare> {
  /*
   * <hash>|internet|cm:content|cm:creator|2020-01-01T00:00:00.000+01:00|2020-12-01T00:00:00.000+01:
   * 00
   *
   * <hash>|internet|cm:content|cm:creator|2020-01-01T00:00:00.000+01:00|2020-12-01T00:00:00.000+01:
   * 00
   *
   * <hash>|internet|cm:content|null|2020-01-01T00:00:00.000+01:00|2020-12-01T00:00:00.000+01:00
   *
   * <hash>|internet|cm:content|null||2020-12-01T00:00:00.000+01:00
   *
   * <hash>|internet|cm:content|null||
   */

  @Override
  public IndexShare parse(String raw) {
    return parseIndexShare(raw);
  }

  public static IndexShare parseIndexShare(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    raw = raw.trim();

    String[] splitted = raw.split("\\|");
    IndexShare output = new IndexShare();
    output.setEntry(raw);

    if (splitted.length >= 1) {
      output.setContentHash(clean(splitted[0]));
    }
    if (splitted.length >= 2) {
      output.setSource(scope(splitted[1]));
    }
    if (splitted.length >= 3) {
      output.setContentPropertyPrefixedName(clean(splitted[2]));
    }
    if (splitted.length >= 4) {
      output.setResultPropertyPrefixedName(clean(splitted[3]));
    }
    if (splitted.length >= 5) {
      output.setContentDisposition(clean(splitted[4]));
    }
    if (splitted.length >= 6) {
      output.setFromDate(offsetDateTime(splitted[5]));
    }
    if (splitted.length >= 7) {
      output.setToDate(offsetDateTime(splitted[6]));
    }

    return output;
  }

  public static IndexShareScope scope(String clean) {
    clean = clean(clean);
    if (StringUtils.isBlank(clean)) {
      return null;
    }
    try {
      return IndexShareScope.valueOf(clean.toUpperCase());
    } catch (Exception e) {
      return null;
    }
  }

  public static OffsetDateTime offsetDateTime(String raw) {
    String cleaned = clean(raw);
    if (cleaned == null) {
      return null;
    }
    try {
      return OffsetDateTime.parse(raw);
    } catch (Exception e) {
      return null;
    }
  }

  public static String clean(String raw) {
    if (StringUtils.isBlank(raw) || ("null").equals(raw)) {
      return null;
    }
    return raw.trim();
  }

  @Override
  public String serialize(IndexShare entity) {
    throw new UnsupportedOperationException(
        "Scrittura diretta degli CreatedSharedLink non consentita");
  }

}
