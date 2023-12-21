package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.064+0100")
@StaticMetamodel(CosmoTEndpointFruitore.class)
public class CosmoTEndpointFruitore_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTEndpointFruitore, Long> id;
	public static volatile SingularAttribute<CosmoTEndpointFruitore, CosmoTFruitore> fruitore;
	public static volatile SingularAttribute<CosmoTEndpointFruitore, String> codiceDescrittivo;
	public static volatile SingularAttribute<CosmoTEndpointFruitore, String> codiceTipoEndpoint;
	public static volatile SingularAttribute<CosmoTEndpointFruitore, String> endpoint;
	public static volatile SingularAttribute<CosmoTEndpointFruitore, String> metodoHttp;
	public static volatile ListAttribute<CosmoTEndpointFruitore, CosmoTCallbackFruitore> callbacks;
	public static volatile SingularAttribute<CosmoTEndpointFruitore, CosmoDOperazioneFruitore> operazione;
	public static volatile SingularAttribute<CosmoTEndpointFruitore, CosmoTSchemaAutenticazioneFruitore> schemaAutenticazione;
}
