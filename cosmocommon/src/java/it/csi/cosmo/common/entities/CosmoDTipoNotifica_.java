package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.531+0100")
@StaticMetamodel(CosmoDTipoNotifica.class)
public class CosmoDTipoNotifica_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDTipoNotifica, String> codice;
	public static volatile SingularAttribute<CosmoDTipoNotifica, String> descrizione;
	public static volatile ListAttribute<CosmoDTipoNotifica, CosmoTNotifica> cosmoTNotificas;
}
