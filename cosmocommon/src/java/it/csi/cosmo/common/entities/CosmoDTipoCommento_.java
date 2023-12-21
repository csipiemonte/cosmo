/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import it.csi.cosmo.common.entities.proto.CosmoDEntity_;

@Generated(value = "Dali", date = "2023-02-10T11:42:27.531+0100")
@StaticMetamodel(CosmoDTipoCommento.class)
public class CosmoDTipoCommento_ extends CosmoDEntity_ {
  public static volatile SingularAttribute<CosmoDTipoCommento, String> codice;
  public static volatile SingularAttribute<CosmoDTipoCommento, String> descrizione;
  public static volatile ListAttribute<CosmoDTipoCommento, CosmoTCommento> cosmoTCommentos;
}
