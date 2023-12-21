package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:00.835+0100")
@StaticMetamodel(CosmoDCustomFormFormio.class)
public class CosmoDCustomFormFormio_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDCustomFormFormio, String> codice;
	public static volatile SingularAttribute<CosmoDCustomFormFormio, String> customForm;
	public static volatile SingularAttribute<CosmoDCustomFormFormio, String> descrizione;
	public static volatile SingularAttribute<CosmoDCustomFormFormio, CosmoDTipoPratica> cosmoDTipoPratica;
	public static volatile ListAttribute<CosmoDCustomFormFormio, CosmoTHelper> cosmoTHelpers;
}
