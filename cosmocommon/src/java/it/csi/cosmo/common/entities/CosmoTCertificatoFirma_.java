package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.933+0100")
@StaticMetamodel(CosmoTCertificatoFirma.class)
public class CosmoTCertificatoFirma_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTCertificatoFirma, Long> id;
	public static volatile SingularAttribute<CosmoTCertificatoFirma, String> descrizione;
	public static volatile SingularAttribute<CosmoTCertificatoFirma, String> pin;
	public static volatile SingularAttribute<CosmoTCertificatoFirma, String> password;
	public static volatile SingularAttribute<CosmoTCertificatoFirma, String> dtScadenza;
	public static volatile SingularAttribute<CosmoTCertificatoFirma, String> username;
	public static volatile SingularAttribute<CosmoTCertificatoFirma, CosmoDEnteCertificatore> cosmoDEnteCertificatore;
	public static volatile SingularAttribute<CosmoTCertificatoFirma, CosmoDProfiloFeq> cosmoDProfiloFeq;
	public static volatile SingularAttribute<CosmoTCertificatoFirma, CosmoDSceltaMarcaTemporale> cosmoDSceltaMarcaTemporale;
	public static volatile SingularAttribute<CosmoTCertificatoFirma, CosmoDTipoCredenzialiFirma> cosmoDTipoCredenzialiFirma;
	public static volatile SingularAttribute<CosmoTCertificatoFirma, CosmoDTipoOtp> cosmoDTipoOtp;
	public static volatile SingularAttribute<CosmoTCertificatoFirma, CosmoTUtente> cosmoTUtente;
	public static volatile SingularAttribute<CosmoTCertificatoFirma, Boolean> ultimoUtilizzato;
}
