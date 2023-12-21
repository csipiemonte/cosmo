package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.113+0100")
@StaticMetamodel(CosmoTFormLogico.class)
public class CosmoTFormLogico_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTFormLogico, Long> id;
	public static volatile SingularAttribute<CosmoTFormLogico, String> codice;
	public static volatile SingularAttribute<CosmoTFormLogico, String> descrizione;
	public static volatile SingularAttribute<CosmoTFormLogico, Boolean> wizard;
	public static volatile ListAttribute<CosmoTFormLogico, CosmoRFormLogicoIstanzaFunzionalita> cosmoRFormLogicoIstanzaFunzionalitas;
	public static volatile SingularAttribute<CosmoTFormLogico, Boolean> eseguibileMassivamente;
	public static volatile SingularAttribute<CosmoTFormLogico, CosmoTEnte> cosmoTEnte;
	public static volatile ListAttribute<CosmoTFormLogico, CosmoTAttivita> cosmoTAttivitas;
}
