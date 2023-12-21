package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.669+0100")
@StaticMetamodel(CosmoDTipoTag.class)
public class CosmoDTipoTag_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDTipoTag, String> codice;
	public static volatile SingularAttribute<CosmoDTipoTag, String> descrizione;
	public static volatile SingularAttribute<CosmoDTipoTag, String> label;
	public static volatile ListAttribute<CosmoDTipoTag, CosmoTTag> cosmoTTags;
}
