package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:00.917+0100")
@StaticMetamodel(CosmoDFiltroCampo.class)
public class CosmoDFiltroCampo_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDFiltroCampo, String> codice;
	public static volatile SingularAttribute<CosmoDFiltroCampo, String> descrizione;
	public static volatile ListAttribute<CosmoDFiltroCampo, CosmoTVariabileProcesso> cosmoTVariabileProcessos;
}
