package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.263+0100")
@StaticMetamodel(CosmoDStatoPratica.class)
public class CosmoDStatoPratica_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDStatoPratica, String> codice;
	public static volatile SingularAttribute<CosmoDStatoPratica, String> descrizione;
	public static volatile SingularAttribute<CosmoDStatoPratica, String> classe;
	public static volatile ListAttribute<CosmoDStatoPratica, CosmoRStatoTipoPratica> cosmoRStatoTipoPraticas;
}
