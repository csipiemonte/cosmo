package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.909+0100")
@StaticMetamodel(CosmoTCaricamentoPratica.class)
public class CosmoTCaricamentoPratica_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTCaricamentoPratica, Long> id;
	public static volatile SingularAttribute<CosmoTCaricamentoPratica, String> descrizioneEvento;
	public static volatile SingularAttribute<CosmoTCaricamentoPratica, String> errore;
	public static volatile SingularAttribute<CosmoTCaricamentoPratica, String> identificativoPratica;
	public static volatile SingularAttribute<CosmoTCaricamentoPratica, String> nomeFile;
	public static volatile SingularAttribute<CosmoTCaricamentoPratica, String> pathFile;
	public static volatile SingularAttribute<CosmoTCaricamentoPratica, CosmoDStatoCaricamentoPratica> cosmoDStatoCaricamentoPratica;
	public static volatile SingularAttribute<CosmoTCaricamentoPratica, CosmoTEnte> cosmoTEnte;
	public static volatile SingularAttribute<CosmoTCaricamentoPratica, CosmoTPratica> cosmoTPratica;
	public static volatile SingularAttribute<CosmoTCaricamentoPratica, CosmoTCaricamentoPratica> cosmoTCaricamentoPratica;
	public static volatile SingularAttribute<CosmoTCaricamentoPratica, CosmoTUtente> cosmoTUtente;
	public static volatile ListAttribute<CosmoTCaricamentoPratica, CosmoTCaricamentoPratica> cosmoTCaricamentoPraticas;
}
