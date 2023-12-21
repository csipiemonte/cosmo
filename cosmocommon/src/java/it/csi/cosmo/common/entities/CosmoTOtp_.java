package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.388+0100")
@StaticMetamodel(CosmoTOtp.class)
public class CosmoTOtp_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTOtp, Long> id;
	public static volatile SingularAttribute<CosmoTOtp, Timestamp> dtScadenza;
	public static volatile SingularAttribute<CosmoTOtp, CosmoTUtente> utente;
	public static volatile SingularAttribute<CosmoTOtp, CosmoTEnte> cosmoTEnte;
	public static volatile SingularAttribute<CosmoTOtp, String> valore;
	public static volatile SingularAttribute<CosmoTOtp, Boolean> utilizzato;
}
