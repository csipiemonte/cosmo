package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.370+0100")
@StaticMetamodel(CosmoDTipoCondivisionePratica.class)
public class CosmoDTipoCondivisionePratica_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDTipoCondivisionePratica, String> codice;
	public static volatile SingularAttribute<CosmoDTipoCondivisionePratica, String> descrizione;
	public static volatile ListAttribute<CosmoDTipoCondivisionePratica, CosmoRPraticaUtenteGruppo> cosmoRPraticaUtenteGruppos;
}
