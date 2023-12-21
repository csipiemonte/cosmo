/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business.delegates;

import java.lang.reflect.Method;
import org.flowable.common.engine.impl.el.AbstractFlowableFunctionDelegate;

/**
 *
 *
 */
public class CosmoGetUtenteCorrenteDelegate extends AbstractFlowableFunctionDelegate {

  @Override
  public String localName() {
    return "getUtenteCorrente";
  }

  @Override
  public String prefix() {
    return "cosmo";
  }

  @Override
  public Class<?> functionClass() {
    return CosmoDelegateImpl.class;
  }

  @Override
  public Method functionMethod() {
    return getNoParameterMethod("getUtenteCorrente");
  }
}
