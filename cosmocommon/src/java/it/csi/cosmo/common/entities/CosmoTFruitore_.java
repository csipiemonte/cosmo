package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.138+0100")
@StaticMetamodel(CosmoTFruitore.class)
public class CosmoTFruitore_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTFruitore, Long> id;
	public static volatile SingularAttribute<CosmoTFruitore, String> apiManagerId;
	public static volatile SingularAttribute<CosmoTFruitore, String> nomeApp;
	public static volatile SingularAttribute<CosmoTFruitore, String> url;
	public static volatile ListAttribute<CosmoTFruitore, CosmoTNotifica> cosmoTNotificas;
	public static volatile ListAttribute<CosmoTFruitore, CosmoTPratica> cosmoTPraticas;
	public static volatile ListAttribute<CosmoTFruitore, CosmoDAutorizzazioneFruitore> cosmoDAutorizzazioneFruitores;
	public static volatile ListAttribute<CosmoTFruitore, CosmoRFruitoreEnte> cosmoRFruitoreEntes;
	public static volatile ListAttribute<CosmoTFruitore, CosmoTSchemaAutenticazioneFruitore> schemiAutenticazione;
	public static volatile ListAttribute<CosmoTFruitore, CosmoTEndpointFruitore> endpoints;
}
