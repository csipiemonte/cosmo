package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.198+0100")
@StaticMetamodel(CosmoRNotificaUtenteEnte.class)
public class CosmoRNotificaUtenteEnte_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRNotificaUtenteEnte, CosmoRNotificaUtenteEntePK> id;
	public static volatile SingularAttribute<CosmoRNotificaUtenteEnte, Timestamp> dataLettura;
	public static volatile SingularAttribute<CosmoRNotificaUtenteEnte, CosmoTEnte> cosmoTEnte;
	public static volatile SingularAttribute<CosmoRNotificaUtenteEnte, CosmoTNotifica> cosmoTNotifica;
	public static volatile SingularAttribute<CosmoRNotificaUtenteEnte, CosmoTUtente> cosmoTUtente;
	public static volatile SingularAttribute<CosmoRNotificaUtenteEnte, Boolean> invioMail;
	public static volatile SingularAttribute<CosmoRNotificaUtenteEnte, String> statoInvioMail;
}
