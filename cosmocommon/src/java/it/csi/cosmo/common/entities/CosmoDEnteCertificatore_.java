package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:00.857+0100")
@StaticMetamodel(CosmoDEnteCertificatore.class)
public class CosmoDEnteCertificatore_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDEnteCertificatore, String> codice;
	public static volatile SingularAttribute<CosmoDEnteCertificatore, String> descrizione;
	public static volatile SingularAttribute<CosmoDEnteCertificatore, String> codiceCa;
	public static volatile SingularAttribute<CosmoDEnteCertificatore, String> codiceTsa;
	public static volatile ListAttribute<CosmoDEnteCertificatore, CosmoTCertificatoFirma> cosmoTCertificatoFirmas;
	public static volatile ListAttribute<CosmoDEnteCertificatore, CosmoREnteCertificatoreEnte> cosmoREnteCertificatoreEntes;
	public static volatile SingularAttribute<CosmoDEnteCertificatore, String> provider;
	public static volatile ListAttribute<CosmoDEnteCertificatore, CosmoDTipoPratica> cosmoDTipoPraticas;
}
