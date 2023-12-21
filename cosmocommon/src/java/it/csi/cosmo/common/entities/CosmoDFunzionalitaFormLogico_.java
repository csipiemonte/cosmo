package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:00.983+0100")
@StaticMetamodel(CosmoDFunzionalitaFormLogico.class)
public class CosmoDFunzionalitaFormLogico_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDFunzionalitaFormLogico, String> codice;
	public static volatile SingularAttribute<CosmoDFunzionalitaFormLogico, String> descrizione;
	public static volatile SingularAttribute<CosmoDFunzionalitaFormLogico, Boolean> eseguibileMassivamente;
	public static volatile SingularAttribute<CosmoDFunzionalitaFormLogico, String> handler;
	public static volatile SingularAttribute<CosmoDFunzionalitaFormLogico, Boolean> multiIstanza;
	public static volatile ListAttribute<CosmoDFunzionalitaFormLogico, CosmoRFunzionalitaParametroFormLogico> associazioniParametri;
}
