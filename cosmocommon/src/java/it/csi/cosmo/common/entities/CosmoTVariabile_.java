package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.721+0100")
@StaticMetamodel(CosmoTVariabile.class)
public class CosmoTVariabile_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTVariabile, Long> id;
	public static volatile SingularAttribute<CosmoTVariabile, byte[]> bytearrayValue;
	public static volatile SingularAttribute<CosmoTVariabile, Double> doubleValue;
	public static volatile SingularAttribute<CosmoTVariabile, CosmoTPratica> cosmoTPratica;
	public static volatile SingularAttribute<CosmoTVariabile, Long> longValue;
	public static volatile SingularAttribute<CosmoTVariabile, String> nome;
	public static volatile SingularAttribute<CosmoTVariabile, String> textValue;
	public static volatile SingularAttribute<CosmoTVariabile, String> typeName;
	public static volatile SingularAttribute<CosmoTVariabile, String> jsonValue;
}
