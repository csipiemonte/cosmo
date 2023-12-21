package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.686+0100")
@StaticMetamodel(CosmoDTrasformazioneDatiPratica.class)
public class CosmoDTrasformazioneDatiPratica_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDTrasformazioneDatiPratica, Long> id;
	public static volatile SingularAttribute<CosmoDTrasformazioneDatiPratica, String> codiceTipoPratica;
	public static volatile SingularAttribute<CosmoDTrasformazioneDatiPratica, CosmoDTipoPratica> tipoPratica;
	public static volatile SingularAttribute<CosmoDTrasformazioneDatiPratica, String> definizione;
	public static volatile SingularAttribute<CosmoDTrasformazioneDatiPratica, String> descrizione;
	public static volatile SingularAttribute<CosmoDTrasformazioneDatiPratica, Boolean> obbligatoria;
	public static volatile SingularAttribute<CosmoDTrasformazioneDatiPratica, Integer> ordine;
	public static volatile SingularAttribute<CosmoDTrasformazioneDatiPratica, String> codiceFase;
}
