package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.428+0100")
@StaticMetamodel(CosmoRTipodocTipopratica.class)
public class CosmoRTipodocTipopratica_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRTipodocTipopratica, CosmoRTipodocTipopraticaPK> id;
	public static volatile SingularAttribute<CosmoRTipodocTipopratica, CosmoDTipoPratica> cosmoDTipoPratica;
	public static volatile SingularAttribute<CosmoRTipodocTipopratica, CosmoDTipoDocumento> cosmoDTipoDocumento;
}
