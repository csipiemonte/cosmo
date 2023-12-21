package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.339+0100")
@StaticMetamodel(CosmoTNotifica.class)
public class CosmoTNotifica_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTNotifica, Long> id;
	public static volatile SingularAttribute<CosmoTNotifica, Timestamp> arrivo;
	public static volatile SingularAttribute<CosmoTNotifica, String> descrizione;
	public static volatile SingularAttribute<CosmoTNotifica, String> classe;
	public static volatile SingularAttribute<CosmoTNotifica, Timestamp> scadenza;
	public static volatile SingularAttribute<CosmoTNotifica, CosmoDTipoNotifica> cosmoDTipoNotifica;
	public static volatile SingularAttribute<CosmoTNotifica, String> url;
	public static volatile SingularAttribute<CosmoTNotifica, String> urlDescrizione;
	public static volatile ListAttribute<CosmoTNotifica, CosmoRNotificaUtenteEnte> cosmoRNotificaUtenteEntes;
	public static volatile SingularAttribute<CosmoTNotifica, CosmoTFruitore> cosmoTFruitore;
	public static volatile SingularAttribute<CosmoTNotifica, CosmoTPratica> cosmoTPratica;
}
