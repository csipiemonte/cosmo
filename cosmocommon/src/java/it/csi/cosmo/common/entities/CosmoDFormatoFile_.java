package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:00.957+0100")
@StaticMetamodel(CosmoDFormatoFile.class)
public class CosmoDFormatoFile_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDFormatoFile, String> codice;
	public static volatile SingularAttribute<CosmoDFormatoFile, String> descrizione;
	public static volatile SingularAttribute<CosmoDFormatoFile, String> estensioneDefault;
	public static volatile SingularAttribute<CosmoDFormatoFile, String> icona;
	public static volatile SingularAttribute<CosmoDFormatoFile, String> mimeType;
	public static volatile SingularAttribute<CosmoDFormatoFile, Boolean> supportaAnteprima;
	public static volatile SingularAttribute<CosmoDFormatoFile, Boolean> supportaSbustamento;
	public static volatile SingularAttribute<CosmoDFormatoFile, Boolean> uploadConsentito;
	public static volatile ListAttribute<CosmoDFormatoFile, CosmoRFormatoFileProfiloFeqTipoFirma> cosmoRFormatoFileProfiloFeqTipoFirmas;
}
