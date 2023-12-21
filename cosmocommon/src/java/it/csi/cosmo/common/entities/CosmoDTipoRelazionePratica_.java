package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.608+0100")
@StaticMetamodel(CosmoDTipoRelazionePratica.class)
public class CosmoDTipoRelazionePratica_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDTipoRelazionePratica, String> codice;
	public static volatile SingularAttribute<CosmoDTipoRelazionePratica, String> descrizione;
	public static volatile SingularAttribute<CosmoDTipoRelazionePratica, String> descrizioneInversa;
	public static volatile ListAttribute<CosmoDTipoRelazionePratica, CosmoRPraticaPratica> cosmoRPraticaPraticas;
}
