/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;


@Generated(value = "Dali", date = "2023-02-10T11:42:27.758+0100")
@StaticMetamodel(CosmoTCommento.class)
public class CosmoTCommento_ extends CosmoTEntity_ {
  public static volatile SingularAttribute<CosmoTCommento, Long> id;
  public static volatile SingularAttribute<CosmoTCommento, Timestamp> dataCreazione;
  public static volatile SingularAttribute<CosmoTCommento, CosmoTPratica> pratica;
  public static volatile SingularAttribute<CosmoTCommento, CosmoTAttivita> attivita;
  public static volatile SingularAttribute<CosmoTCommento, String> messaggio;
  public static volatile SingularAttribute<CosmoTCommento, String> utenteCreatore;
  public static volatile SingularAttribute<CosmoTCommento, CosmoDTipoCommento> cosmoDTipoCommento;
}
