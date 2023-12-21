package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.697+0100")
@StaticMetamodel(CosmoTUtenteGruppo.class)
public class CosmoTUtenteGruppo_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTUtenteGruppo, Long> id;
	public static volatile SingularAttribute<CosmoTUtenteGruppo, Long> idGruppo;
	public static volatile SingularAttribute<CosmoTUtenteGruppo, Long> idUtente;
	public static volatile SingularAttribute<CosmoTUtenteGruppo, CosmoTGruppo> gruppo;
	public static volatile SingularAttribute<CosmoTUtenteGruppo, CosmoTUtente> utente;
	public static volatile ListAttribute<CosmoTUtenteGruppo, CosmoRUtenteGruppoTag> cosmoRUtenteGruppoTags;
}
