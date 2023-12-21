package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.007+0100")
@StaticMetamodel(CosmoDHelperModale.class)
public class CosmoDHelperModale_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDHelperModale, String> codice;
	public static volatile SingularAttribute<CosmoDHelperModale, String> descrizione;
	public static volatile ListAttribute<CosmoDHelperModale, CosmoTHelper> cosmoTHelpers;
	public static volatile SingularAttribute<CosmoDHelperModale, CosmoDHelperPagina> helperPagina;
	public static volatile SingularAttribute<CosmoDHelperModale, CosmoDHelperTab> helperTab;
}
