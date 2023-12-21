package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.302+0100")
@StaticMetamodel(CosmoRPraticaUtenteGruppo.class)
public class CosmoRPraticaUtenteGruppo_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRPraticaUtenteGruppo, Long> id;
	public static volatile SingularAttribute<CosmoRPraticaUtenteGruppo, CosmoDTipoCondivisionePratica> cosmoDTipoCondivisionePratica;
	public static volatile SingularAttribute<CosmoRPraticaUtenteGruppo, CosmoTGruppo> cosmoTGruppo;
	public static volatile SingularAttribute<CosmoRPraticaUtenteGruppo, CosmoTPratica> cosmoTPratica;
	public static volatile SingularAttribute<CosmoRPraticaUtenteGruppo, CosmoTUtente> cosmoTUtente;
	public static volatile SingularAttribute<CosmoRPraticaUtenteGruppo, CosmoTUtente> cosmoTUtenteCondivisore;
}
