package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.990+0100")
@StaticMetamodel(CosmoRGruppoTipoPratica.class)
public class CosmoRGruppoTipoPratica_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRGruppoTipoPratica, CosmoRGruppoTipoPraticaPK> id;
	public static volatile SingularAttribute<CosmoRGruppoTipoPratica, CosmoDTipoPratica> cosmoDTipoPratica;
	public static volatile SingularAttribute<CosmoRGruppoTipoPratica, CosmoTGruppo> cosmoTGruppo;
	public static volatile SingularAttribute<CosmoRGruppoTipoPratica, Boolean> creatore;
	public static volatile SingularAttribute<CosmoRGruppoTipoPratica, Boolean> supervisore;
}
