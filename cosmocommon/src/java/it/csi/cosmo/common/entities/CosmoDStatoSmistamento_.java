package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.312+0100")
@StaticMetamodel(CosmoDStatoSmistamento.class)
public class CosmoDStatoSmistamento_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDStatoSmistamento, String> codice;
	public static volatile SingularAttribute<CosmoDStatoSmistamento, String> descrizione;
	public static volatile ListAttribute<CosmoDStatoSmistamento, CosmoRSmistamentoDocumento> cosmoRSmistamentoDocumentos;
}
