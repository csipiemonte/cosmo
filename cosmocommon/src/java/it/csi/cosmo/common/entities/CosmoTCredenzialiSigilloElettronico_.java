package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.015+0100")
@StaticMetamodel(CosmoTCredenzialiSigilloElettronico.class)
public class CosmoTCredenzialiSigilloElettronico_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTCredenzialiSigilloElettronico, Long> id;
	public static volatile SingularAttribute<CosmoTCredenzialiSigilloElettronico, String> alias;
	public static volatile SingularAttribute<CosmoTCredenzialiSigilloElettronico, String> delegatedDomain;
	public static volatile SingularAttribute<CosmoTCredenzialiSigilloElettronico, String> delegatedPassword;
	public static volatile SingularAttribute<CosmoTCredenzialiSigilloElettronico, String> delegatedUser;
	public static volatile SingularAttribute<CosmoTCredenzialiSigilloElettronico, String> otpPwd;
	public static volatile SingularAttribute<CosmoTCredenzialiSigilloElettronico, String> tipoHsm;
	public static volatile SingularAttribute<CosmoTCredenzialiSigilloElettronico, String> tipoOtpAuth;
	public static volatile SingularAttribute<CosmoTCredenzialiSigilloElettronico, String> utente;
}
