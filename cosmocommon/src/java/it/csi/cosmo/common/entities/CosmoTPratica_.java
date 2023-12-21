package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.410+0100")
@StaticMetamodel(CosmoTPratica.class)
public class CosmoTPratica_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTPratica, Long> id;
	public static volatile SingularAttribute<CosmoTPratica, Timestamp> dataCambioStato;
	public static volatile SingularAttribute<CosmoTPratica, Timestamp> dataCreazionePratica;
	public static volatile SingularAttribute<CosmoTPratica, Timestamp> dataFinePratica;
	public static volatile SingularAttribute<CosmoTPratica, String> idPraticaExt;
	public static volatile SingularAttribute<CosmoTPratica, String> linkPratica;
	public static volatile SingularAttribute<CosmoTPratica, String> linkPraticaEsterna;
	public static volatile SingularAttribute<CosmoTPratica, String> metadati;
	public static volatile SingularAttribute<CosmoTPratica, String> oggetto;
	public static volatile SingularAttribute<CosmoTPratica, String> riassunto;
	public static volatile SingularAttribute<CosmoTPratica, String> riassuntoTestuale;
	public static volatile SingularAttribute<CosmoTPratica, String> utenteCreazionePratica;
	public static volatile SingularAttribute<CosmoTPratica, String> uuidNodo;
	public static volatile SingularAttribute<CosmoTPratica, Boolean> esterna;
	public static volatile ListAttribute<CosmoTPratica, CosmoRPraticaUtenteGruppo> associazioneUtentiGruppi;
	public static volatile ListAttribute<CosmoTPratica, CosmoTAttivita> attivita;
	public static volatile ListAttribute<CosmoTPratica, CosmoTDocumento> documenti;
	public static volatile ListAttribute<CosmoTPratica, CosmoTCommento> commenti;
	public static volatile ListAttribute<CosmoTPratica, CosmoTNotifica> notifiche;
	public static volatile SingularAttribute<CosmoTPratica, CosmoTFruitore> fruitore;
	public static volatile SingularAttribute<CosmoTPratica, CosmoDTipoPratica> tipo;
	public static volatile SingularAttribute<CosmoTPratica, CosmoDStatoPratica> stato;
	public static volatile SingularAttribute<CosmoTPratica, CosmoTEnte> ente;
	public static volatile ListAttribute<CosmoTPratica, CosmoRPraticaPratica> cosmoRPraticaPraticasA;
	public static volatile ListAttribute<CosmoTPratica, CosmoRPraticaPratica> cosmoRPraticaPraticasDa;
	public static volatile ListAttribute<CosmoTPratica, CosmoRPraticaTag> cosmoRPraticaTags;
	public static volatile ListAttribute<CosmoTPratica, CosmoTVariabile> variabili;
	public static volatile ListAttribute<CosmoTPratica, CosmoTCaricamentoPratica> cosmoTCaricamentoPraticas;
	public static volatile ListAttribute<CosmoTPratica, CosmoTTemplateFea> cosmoTTemplateFeas;
}
