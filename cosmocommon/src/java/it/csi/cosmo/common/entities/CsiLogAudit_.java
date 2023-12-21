package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.767+0100")
@StaticMetamodel(CsiLogAudit.class)
public class CsiLogAudit_ {
	public static volatile SingularAttribute<CsiLogAudit, Long> id;
	public static volatile SingularAttribute<CsiLogAudit, Timestamp> dataOra;
	public static volatile SingularAttribute<CsiLogAudit, String> idApp;
	public static volatile SingularAttribute<CsiLogAudit, String> ipAddress;
	public static volatile SingularAttribute<CsiLogAudit, String> keyOper;
	public static volatile SingularAttribute<CsiLogAudit, String> oggOper;
	public static volatile SingularAttribute<CsiLogAudit, String> operazione;
	public static volatile SingularAttribute<CsiLogAudit, String> utente;
}
