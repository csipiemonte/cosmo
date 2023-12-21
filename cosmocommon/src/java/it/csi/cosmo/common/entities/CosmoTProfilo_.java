package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.479+0100")
@StaticMetamodel(CosmoTProfilo.class)
public class CosmoTProfilo_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTProfilo, Long> id;
	public static volatile SingularAttribute<CosmoTProfilo, String> codice;
	public static volatile SingularAttribute<CosmoTProfilo, String> descrizione;
	public static volatile SingularAttribute<CosmoTProfilo, Boolean> assegnabile;
	public static volatile ListAttribute<CosmoTProfilo, CosmoRUtenteProfilo> cosmoRUtenteProfilos;
	public static volatile ListAttribute<CosmoTProfilo, CosmoDUseCase> cosmoDUseCases;
}
