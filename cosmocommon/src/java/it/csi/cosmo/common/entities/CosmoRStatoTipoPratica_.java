package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.368+0100")
@StaticMetamodel(CosmoRStatoTipoPratica.class)
public class CosmoRStatoTipoPratica_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRStatoTipoPratica, Long> id;
	public static volatile SingularAttribute<CosmoRStatoTipoPratica, CosmoDTipoPratica> cosmoDTipoPratica;
	public static volatile SingularAttribute<CosmoRStatoTipoPratica, CosmoDStatoPratica> cosmoDStatoPratica;
}
