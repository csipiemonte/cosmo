/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.config;

import java.sql.Types;
import org.hibernate.dialect.PostgreSQL9Dialect;

/**
 *
 */

public class PostgreSQLDialect extends PostgreSQL9Dialect {

  public PostgreSQLDialect() {
    super();
    this.registerColumnType(Types.JAVA_OBJECT, "json");
  }

}
