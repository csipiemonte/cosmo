package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.053+0100")
@StaticMetamodel(CosmoDHelperTab.class)
public class CosmoDHelperTab_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDHelperTab, String> codice;
	public static volatile SingularAttribute<CosmoDHelperTab, String> descrizione;
	public static volatile ListAttribute<CosmoDHelperTab, CosmoTHelper> cosmoTHelpers;
	public static volatile ListAttribute<CosmoDHelperTab, CosmoDHelperModale> cosmoDHelperModales;
	public static volatile SingularAttribute<CosmoDHelperTab, CosmoDHelperPagina> cosmoDHelperPagina;
}
