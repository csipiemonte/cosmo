package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.696+0100")
@StaticMetamodel(CosmoRUtenteProfilo.class)
public class CosmoRUtenteProfilo_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRUtenteProfilo, Long> id;
	public static volatile SingularAttribute<CosmoRUtenteProfilo, CosmoTEnte> cosmoTEnte;
	public static volatile SingularAttribute<CosmoRUtenteProfilo, CosmoTUtente> cosmoTUtente;
	public static volatile SingularAttribute<CosmoRUtenteProfilo, CosmoTProfilo> cosmoTProfilo;
}
