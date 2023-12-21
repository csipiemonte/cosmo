package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.236+0100")
@StaticMetamodel(CosmoRPraticaPratica.class)
public class CosmoRPraticaPratica_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRPraticaPratica, CosmoRPraticaPraticaPK> id;
	public static volatile SingularAttribute<CosmoRPraticaPratica, CosmoDTipoRelazionePratica> cosmoDTipoRelazionePratica;
	public static volatile SingularAttribute<CosmoRPraticaPratica, CosmoTPratica> cosmoTPraticaA;
	public static volatile SingularAttribute<CosmoRPraticaPratica, CosmoTPratica> cosmoTPraticaDa;
}
