package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:00.791+0100")
@StaticMetamodel(CosmoDCategoriaUseCase.class)
public class CosmoDCategoriaUseCase_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDCategoriaUseCase, String> codice;
	public static volatile SingularAttribute<CosmoDCategoriaUseCase, String> descrizione;
	public static volatile ListAttribute<CosmoDCategoriaUseCase, CosmoDUseCase> cosmoDUseCases;
}
