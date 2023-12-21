package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.271+0100")
@StaticMetamodel(CosmoRPraticaTag.class)
public class CosmoRPraticaTag_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRPraticaTag, CosmoRPraticaTagPK> id;
	public static volatile SingularAttribute<CosmoRPraticaTag, CosmoTPratica> cosmoTPratica;
	public static volatile SingularAttribute<CosmoRPraticaTag, CosmoTTag> cosmoTTag;
}
