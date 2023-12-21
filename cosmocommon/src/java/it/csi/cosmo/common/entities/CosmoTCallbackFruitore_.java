package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.886+0100")
@StaticMetamodel(CosmoTCallbackFruitore.class)
public class CosmoTCallbackFruitore_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTCallbackFruitore, Long> id;
	public static volatile SingularAttribute<CosmoTCallbackFruitore, CosmoDStatoCallbackFruitore> stato;
	public static volatile SingularAttribute<CosmoTCallbackFruitore, CosmoTEndpointFruitore> endpoint;
	public static volatile ListAttribute<CosmoTCallbackFruitore, CosmoTInvioCallbackFruitore> tentativiInvio;
	public static volatile SingularAttribute<CosmoTCallbackFruitore, String> codiceSegnale;
}
