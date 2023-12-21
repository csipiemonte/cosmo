package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.557+0100")
@StaticMetamodel(CosmoDTipoOtp.class)
public class CosmoDTipoOtp_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDTipoOtp, String> codice;
	public static volatile SingularAttribute<CosmoDTipoOtp, String> descrizione;
	public static volatile SingularAttribute<CosmoDTipoOtp, Boolean> nonValidoInApposizione;
	public static volatile ListAttribute<CosmoDTipoOtp, CosmoTCertificatoFirma> cosmoTCertificatoFirmas;
	public static volatile ListAttribute<CosmoDTipoOtp, CosmoDTipoPratica> cosmoDTipoPraticas;
}
