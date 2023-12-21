package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.076+0100")
@StaticMetamodel(CosmoDOperazioneFruitore.class)
public class CosmoDOperazioneFruitore_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDOperazioneFruitore, String> codice;
	public static volatile SingularAttribute<CosmoDOperazioneFruitore, String> descrizione;
	public static volatile SingularAttribute<CosmoDOperazioneFruitore, Boolean> personalizzabile;
}
