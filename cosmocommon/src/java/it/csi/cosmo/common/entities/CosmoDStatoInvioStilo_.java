package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.240+0100")
@StaticMetamodel(CosmoDStatoInvioStilo.class)
public class CosmoDStatoInvioStilo_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDStatoInvioStilo, String> codice;
	public static volatile SingularAttribute<CosmoDStatoInvioStilo, String> descrizione;
	public static volatile ListAttribute<CosmoDStatoInvioStilo, CosmoRInvioStiloDocumento> cosmoRInvioStiloDocumentos;
}
