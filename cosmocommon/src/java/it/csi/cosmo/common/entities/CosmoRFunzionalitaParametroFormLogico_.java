package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.975+0100")
@StaticMetamodel(CosmoRFunzionalitaParametroFormLogico.class)
public class CosmoRFunzionalitaParametroFormLogico_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRFunzionalitaParametroFormLogico, Long> id;
	public static volatile SingularAttribute<CosmoRFunzionalitaParametroFormLogico, Boolean> obbligatorio;
	public static volatile SingularAttribute<CosmoRFunzionalitaParametroFormLogico, String> codiceFunzionalita;
	public static volatile SingularAttribute<CosmoRFunzionalitaParametroFormLogico, String> codiceParametro;
	public static volatile SingularAttribute<CosmoRFunzionalitaParametroFormLogico, CosmoDFunzionalitaFormLogico> funzionalita;
	public static volatile SingularAttribute<CosmoRFunzionalitaParametroFormLogico, CosmoDChiaveParametroFunzionalitaFormLogico> parametro;
}
