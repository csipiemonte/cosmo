/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model;

import java.util.Arrays;

/**
 *
 */

public class IndexRawProperty {

  private String prefixedName;

  private String[] values;

  private boolean multivalue;

  public String getPrefixedName() {
    return prefixedName;
  }

  public void setPrefixedName(String prefixedName) {
    this.prefixedName = prefixedName;
  }

  public String[] getValues() {
    return values;
  }

  public void setValues(String[] values) {
    this.values = values;
  }

  public boolean isMultivalue() {
    return multivalue;
  }

  public void setMultivalue(boolean multivalue) {
    this.multivalue = multivalue;
  }

  @Override
  public String toString() {
    return "IndexRawProperty [prefixedName=" + prefixedName + ", values=" + Arrays.toString(values)
    + ", multivalue=" + multivalue + "]";
  }

}
