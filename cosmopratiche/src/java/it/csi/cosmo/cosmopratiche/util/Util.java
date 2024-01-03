/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.util;

import java.util.function.Predicate;
import javax.ws.rs.BadRequestException;
import org.springframework.data.domain.Page;
import it.csi.cosmo.cosmopratiche.dto.rest.PageInfo;

public abstract class Util {

  private Util() {
    // private
  }

  public static synchronized PageInfo pageInfo(Page<?> p ) {
    PageInfo pi = new PageInfo();
    pi.setPage(p.getNumber());
    pi.setPageSize(p.getSize());
    pi.setTotalElements((int) p.getTotalElements());
    pi.setTotalPages(p.getTotalPages());
    return pi;
  }

  public static synchronized int parseNumber(String n, int defaultValue, Predicate<Integer> p2,
      String message) {
    try {
      return parseNumber(Integer.parseInt(n), defaultValue, p2, message);
    } catch (NumberFormatException e) {
      throw new BadRequestException(String.format(message, n));
    }
  }

  public static synchronized int parseNumber(Integer n, int defaultValue, Predicate<Integer> p2,
      String message) {
    int parsed = n == null ? defaultValue : n;
    if (!p2.test(parsed)) {
      throw new BadRequestException(String.format(message, n));
    }
    return parsed;
  }

}
