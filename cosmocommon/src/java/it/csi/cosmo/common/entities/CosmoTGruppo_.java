package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.187+0100")
@StaticMetamodel(CosmoTGruppo.class)
public class CosmoTGruppo_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTGruppo, Long> id;
	public static volatile SingularAttribute<CosmoTGruppo, String> codice;
	public static volatile SingularAttribute<CosmoTGruppo, String> nome;
	public static volatile SingularAttribute<CosmoTGruppo, String> descrizione;
	public static volatile ListAttribute<CosmoTGruppo, CosmoTUtenteGruppo> associazioniUtenti;
	public static volatile ListAttribute<CosmoTGruppo, CosmoRGruppoTipoPratica> cosmoRGruppoTipoPraticas;
	public static volatile SingularAttribute<CosmoTGruppo, CosmoTEnte> ente;
	public static volatile ListAttribute<CosmoTGruppo, CosmoRPraticaUtenteGruppo> cosmoRPraticaUtenteGruppos;
}
