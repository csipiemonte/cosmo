package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.138+0100")
@StaticMetamodel(CosmoDSceltaMarcaTemporale.class)
public class CosmoDSceltaMarcaTemporale_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDSceltaMarcaTemporale, String> codice;
	public static volatile SingularAttribute<CosmoDSceltaMarcaTemporale, String> descrizione;
	public static volatile SingularAttribute<CosmoDSceltaMarcaTemporale, Boolean> nonValidoInApposizione;
	public static volatile ListAttribute<CosmoDSceltaMarcaTemporale, CosmoDTipoPratica> cosmoDTipoPraticas;
	public static volatile ListAttribute<CosmoDSceltaMarcaTemporale, CosmoTCertificatoFirma> cosmoTCertificatoFirmas;
}
