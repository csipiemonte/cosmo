package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.439+0100")
@StaticMetamodel(CosmoDTipoCredenzialiFirma.class)
public class CosmoDTipoCredenzialiFirma_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDTipoCredenzialiFirma, String> codice;
	public static volatile SingularAttribute<CosmoDTipoCredenzialiFirma, String> descrizione;
	public static volatile SingularAttribute<CosmoDTipoCredenzialiFirma, Boolean> nonValidoInApposizione;
	public static volatile ListAttribute<CosmoDTipoCredenzialiFirma, CosmoTCertificatoFirma> cosmoTCertificatoFirmas;
	public static volatile ListAttribute<CosmoDTipoCredenzialiFirma, CosmoDTipoPratica> cosmoDTipoPraticas;
}
