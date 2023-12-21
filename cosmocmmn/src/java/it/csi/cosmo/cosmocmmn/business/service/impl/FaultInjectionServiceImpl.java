/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.cosmocmmn.business.service.FaultInjectionService;

@Service
@Transactional
public class FaultInjectionServiceImpl implements FaultInjectionService {

  private Map<String, Supplier<Throwable>> faultRepository = new HashMap<>();

  @Override
  public Optional<Throwable> checkFault(String hookPoint) {
    var v = faultRepository.getOrDefault(hookPoint, null);
    if (v == null) {
      return Optional.empty();
    }

    return Optional.of(v.get());
  }

  @Override
  public void injectFault(String hookPoint, Supplier<Throwable> faultBuilder) {
    faultRepository.put(hookPoint, faultBuilder);
  }

  @Override
  public void clearFault(String hookPoint) {
    faultRepository.remove(hookPoint);
  }

  @Override
  public void clearAllFaults() {
    faultRepository.clear();
  }

  @Override
  public List<String> listAllFaults() {
    return faultRepository.keySet().stream().sorted().collect(Collectors.toList());
  }
}
