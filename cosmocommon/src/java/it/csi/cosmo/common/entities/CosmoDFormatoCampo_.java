package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:00.936+0100")
@StaticMetamodel(CosmoDFormatoCampo.class)
public class CosmoDFormatoCampo_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDFormatoCampo, String> codice;
	public static volatile SingularAttribute<CosmoDFormatoCampo, String> descrizione;
	public static volatile ListAttribute<CosmoDFormatoCampo, CosmoTVariabileProcesso> cosmoTVariabileProcessos;
}
