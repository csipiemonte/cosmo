package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.502+0100")
@StaticMetamodel(CosmoDTipoFirma.class)
public class CosmoDTipoFirma_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDTipoFirma, String> codice;
	public static volatile SingularAttribute<CosmoDTipoFirma, String> descrizione;
	public static volatile SingularAttribute<CosmoDTipoFirma, Boolean> estraibile;
	public static volatile SingularAttribute<CosmoDTipoFirma, String> mimeType;
	public static volatile ListAttribute<CosmoDTipoFirma, CosmoRFormatoFileProfiloFeqTipoFirma> cosmoRFormatoFileProfiloFeqTipoFirmas;
}
