/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface FaultInjectionService {

  public Optional<Throwable> checkFault(String hookPoint);

  public void injectFault(String hookPoint, Supplier<Throwable> faultBuilder);

  public void clearFault(String hookPoint);

  public void clearAllFaults();

  public List<String> listAllFaults();
}
