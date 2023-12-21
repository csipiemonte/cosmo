package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.702+0100")
@StaticMetamodel(CosmoDUseCase.class)
public class CosmoDUseCase_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDUseCase, String> codice;
	public static volatile SingularAttribute<CosmoDUseCase, String> descrizione;
	public static volatile SingularAttribute<CosmoDUseCase, CosmoDCategoriaUseCase> cosmoDCategoriaUseCase;
	public static volatile ListAttribute<CosmoDUseCase, CosmoTProfilo> cosmoTProfilos;
}
