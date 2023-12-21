package it.csi.cosmo.common.entities.proto;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.880+0100")
@StaticMetamodel(CosmoTEntity.class)
public class CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTEntity, Timestamp> dtInserimento;
	public static volatile SingularAttribute<CosmoTEntity, String> utenteInserimento;
	public static volatile SingularAttribute<CosmoTEntity, Timestamp> dtUltimaModifica;
	public static volatile SingularAttribute<CosmoTEntity, String> utenteUltimaModifica;
	public static volatile SingularAttribute<CosmoTEntity, Timestamp> dtCancellazione;
	public static volatile SingularAttribute<CosmoTEntity, String> utenteCancellazione;
}
