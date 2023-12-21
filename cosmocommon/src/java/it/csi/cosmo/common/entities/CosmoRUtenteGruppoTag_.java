package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.615+0100")
@StaticMetamodel(CosmoRUtenteGruppoTag.class)
public class CosmoRUtenteGruppoTag_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRUtenteGruppoTag, CosmoRUtenteGruppoTagPK> id;
	public static volatile SingularAttribute<CosmoRUtenteGruppoTag, CosmoTTag> cosmoTTag;
	public static volatile SingularAttribute<CosmoRUtenteGruppoTag, CosmoTUtenteGruppo> cosmoTUtenteGruppo;
}
