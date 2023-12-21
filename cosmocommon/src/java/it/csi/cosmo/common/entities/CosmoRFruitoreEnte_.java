package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.944+0100")
@StaticMetamodel(CosmoRFruitoreEnte.class)
public class CosmoRFruitoreEnte_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRFruitoreEnte, CosmoRFruitoreEntePK> id;
	public static volatile SingularAttribute<CosmoRFruitoreEnte, CosmoTEnte> cosmoTEnte;
	public static volatile SingularAttribute<CosmoRFruitoreEnte, CosmoTFruitore> cosmoTFruitore;
}
