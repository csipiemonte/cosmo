package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.498+0100")
@StaticMetamodel(CosmoRUtenteEnte.class)
public class CosmoRUtenteEnte_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRUtenteEnte, CosmoRUtenteEntePK> id;
	public static volatile SingularAttribute<CosmoRUtenteEnte, String> email;
	public static volatile SingularAttribute<CosmoRUtenteEnte, String> telefono;
	public static volatile SingularAttribute<CosmoRUtenteEnte, CosmoTEnte> cosmoTEnte;
	public static volatile SingularAttribute<CosmoRUtenteEnte, CosmoTUtente> cosmoTUtente;
}
