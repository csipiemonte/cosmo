package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.028+0100")
@StaticMetamodel(CosmoDHelperPagina.class)
public class CosmoDHelperPagina_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDHelperPagina, String> codice;
	public static volatile SingularAttribute<CosmoDHelperPagina, String> descrizione;
	public static volatile ListAttribute<CosmoDHelperPagina, CosmoTHelper> cosmoTHelpers;
	public static volatile ListAttribute<CosmoDHelperPagina, CosmoDHelperTab> cosmoDHelperTabs;
	public static volatile ListAttribute<CosmoDHelperPagina, CosmoDHelperModale> cosmoDHelperModales;
}
