package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.313+0100")
@StaticMetamodel(CosmoTLock.class)
public class CosmoTLock_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTLock, Long> id;
	public static volatile SingularAttribute<CosmoTLock, String> codiceRisorsa;
	public static volatile SingularAttribute<CosmoTLock, String> codiceOwner;
	public static volatile SingularAttribute<CosmoTLock, Timestamp> dtScadenza;
}
