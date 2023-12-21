package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.804+0100")
@StaticMetamodel(CosmoTApprovazione.class)
public class CosmoTApprovazione_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTApprovazione, Long> id;
	public static volatile SingularAttribute<CosmoTApprovazione, Timestamp> dtApprovazione;
	public static volatile SingularAttribute<CosmoTApprovazione, CosmoTAttivita> cosmoTAttivita;
	public static volatile SingularAttribute<CosmoTApprovazione, CosmoTDocumento> cosmoTDocumento;
	public static volatile SingularAttribute<CosmoTApprovazione, CosmoTUtente> cosmoTUtente;
}
