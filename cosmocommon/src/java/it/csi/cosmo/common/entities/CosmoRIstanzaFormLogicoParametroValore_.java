package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.100+0100")
@StaticMetamodel(CosmoRIstanzaFormLogicoParametroValore.class)
public class CosmoRIstanzaFormLogicoParametroValore_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRIstanzaFormLogicoParametroValore, CosmoRIstanzaFormLogicoParametroValorePK> id;
	public static volatile SingularAttribute<CosmoRIstanzaFormLogicoParametroValore, String> valoreParametro;
	public static volatile SingularAttribute<CosmoRIstanzaFormLogicoParametroValore, CosmoDChiaveParametroFunzionalitaFormLogico> cosmoDChiaveParametroFunzionalitaFormLogico;
	public static volatile SingularAttribute<CosmoRIstanzaFormLogicoParametroValore, CosmoTIstanzaFunzionalitaFormLogico> cosmoTIstanzaFunzionalitaFormLogico;
}
