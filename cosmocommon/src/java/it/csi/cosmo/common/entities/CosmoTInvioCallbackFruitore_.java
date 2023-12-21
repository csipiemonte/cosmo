package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.269+0100")
@StaticMetamodel(CosmoTInvioCallbackFruitore.class)
public class CosmoTInvioCallbackFruitore_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTInvioCallbackFruitore, Long> id;
	public static volatile SingularAttribute<CosmoTInvioCallbackFruitore, String> dettagliEsito;
	public static volatile SingularAttribute<CosmoTInvioCallbackFruitore, CosmoDEsitoInvioCallbackFruitore> esito;
	public static volatile SingularAttribute<CosmoTInvioCallbackFruitore, CosmoTCallbackFruitore> callback;
}
