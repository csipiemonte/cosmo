package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.187+0100")
@StaticMetamodel(CosmoDStatoCaricamentoPratica.class)
public class CosmoDStatoCaricamentoPratica_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDStatoCaricamentoPratica, String> codice;
	public static volatile SingularAttribute<CosmoDStatoCaricamentoPratica, String> descrizione;
	public static volatile ListAttribute<CosmoDStatoCaricamentoPratica, CosmoTCaricamentoPratica> cosmoTCaricamentoPraticas;
}
