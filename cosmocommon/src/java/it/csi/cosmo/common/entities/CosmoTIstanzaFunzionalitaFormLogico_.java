package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.289+0100")
@StaticMetamodel(CosmoTIstanzaFunzionalitaFormLogico.class)
public class CosmoTIstanzaFunzionalitaFormLogico_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTIstanzaFunzionalitaFormLogico, Long> id;
	public static volatile SingularAttribute<CosmoTIstanzaFunzionalitaFormLogico, String> descrizione;
	public static volatile ListAttribute<CosmoTIstanzaFunzionalitaFormLogico, CosmoRFormLogicoIstanzaFunzionalita> cosmoRFormLogicoIstanzaFunzionalitas;
	public static volatile ListAttribute<CosmoTIstanzaFunzionalitaFormLogico, CosmoRIstanzaFormLogicoParametroValore> cosmoRIstanzaFormLogicoParametroValores;
	public static volatile SingularAttribute<CosmoTIstanzaFunzionalitaFormLogico, CosmoDFunzionalitaFormLogico> cosmoDFunzionalitaFormLogico;
}
