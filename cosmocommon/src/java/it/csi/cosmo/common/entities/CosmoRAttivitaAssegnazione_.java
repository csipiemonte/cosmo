package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.767+0100")
@StaticMetamodel(CosmoRAttivitaAssegnazione.class)
public class CosmoRAttivitaAssegnazione_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRAttivitaAssegnazione, Long> id;
	public static volatile SingularAttribute<CosmoRAttivitaAssegnazione, Boolean> assegnatario;
	public static volatile SingularAttribute<CosmoRAttivitaAssegnazione, Integer> idGruppo;
	public static volatile SingularAttribute<CosmoRAttivitaAssegnazione, Integer> idUtente;
	public static volatile SingularAttribute<CosmoRAttivitaAssegnazione, CosmoTAttivita> cosmoTAttivita;
	public static volatile SingularAttribute<CosmoRAttivitaAssegnazione, CosmoTGruppo> gruppo;
	public static volatile SingularAttribute<CosmoRAttivitaAssegnazione, CosmoTUtente> utente;
}
