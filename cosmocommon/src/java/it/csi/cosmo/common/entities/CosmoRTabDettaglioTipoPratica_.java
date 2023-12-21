package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.388+0100")
@StaticMetamodel(CosmoRTabDettaglioTipoPratica.class)
public class CosmoRTabDettaglioTipoPratica_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRTabDettaglioTipoPratica, CosmoRTabDettaglioTipoPraticaPK> id;
	public static volatile SingularAttribute<CosmoRTabDettaglioTipoPratica, Integer> ordine;
	public static volatile SingularAttribute<CosmoRTabDettaglioTipoPratica, CosmoDTabDettaglio> cosmoDTabDettaglio;
	public static volatile SingularAttribute<CosmoRTabDettaglioTipoPratica, CosmoDTipoPratica> cosmoDTipoPratica;
}
