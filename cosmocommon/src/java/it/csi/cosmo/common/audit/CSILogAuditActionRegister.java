/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.audit;

public enum CSILogAuditActionRegister {

  INSERT ( "001" ),
  UPDATE ( "002" ),
  DELETE ( "003" ),
  SELECT ( "004" ),
  ACCESS ( "010" ),
  NO_ACCESS ( "011" ),
  UNKNOWN ( "000" );

  public static CSILogAuditActionRegister getById ( String id ) {
    for ( CSILogAuditActionRegister e: values () ) {
      if ( e.id.equals ( id ) ) {
        return e;
      }
    }
    return UNKNOWN;
  }

  private String id;

  CSILogAuditActionRegister ( String id ) {
    this.id = id;
  }

  public String getId () {
    return id;
  }

}
