package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.108+0100")
@StaticMetamodel(CosmoDProfiloFeq.class)
public class CosmoDProfiloFeq_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDProfiloFeq, String> codice;
	public static volatile SingularAttribute<CosmoDProfiloFeq, String> descrizione;
	public static volatile SingularAttribute<CosmoDProfiloFeq, Boolean> nonValidoInApposizione;
	public static volatile ListAttribute<CosmoDProfiloFeq, CosmoTCertificatoFirma> cosmoTCertificatoFirmas;
	public static volatile ListAttribute<CosmoDProfiloFeq, CosmoRFormatoFileProfiloFeqTipoFirma> cosmoRFormatoFileProfiloFeqTipoFirmas;
	public static volatile ListAttribute<CosmoDProfiloFeq, CosmoDTipoPratica> cosmoDTipoPraticas;
}
