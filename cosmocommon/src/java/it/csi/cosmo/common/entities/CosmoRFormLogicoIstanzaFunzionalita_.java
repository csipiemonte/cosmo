package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.916+0100")
@StaticMetamodel(CosmoRFormLogicoIstanzaFunzionalita.class)
public class CosmoRFormLogicoIstanzaFunzionalita_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRFormLogicoIstanzaFunzionalita, CosmoRFormLogicoIstanzaFunzionalitaPK> id;
	public static volatile SingularAttribute<CosmoRFormLogicoIstanzaFunzionalita, Long> ordine;
	public static volatile SingularAttribute<CosmoRFormLogicoIstanzaFunzionalita, Boolean> eseguibileMassivamente;
	public static volatile SingularAttribute<CosmoRFormLogicoIstanzaFunzionalita, CosmoTFormLogico> cosmoTFormLogico;
	public static volatile SingularAttribute<CosmoRFormLogicoIstanzaFunzionalita, CosmoTIstanzaFunzionalitaFormLogico> cosmoTIstanzaFunzionalitaFormLogico;
}
