package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.entities.proto.CosmoLEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.749+0100")
@StaticMetamodel(CosmoLStoricoPratica.class)
public class CosmoLStoricoPratica_ extends CosmoLEntity_ {
	public static volatile SingularAttribute<CosmoLStoricoPratica, Long> id;
	public static volatile SingularAttribute<CosmoLStoricoPratica, TipoEventoStoricoPratica> codiceTipoEvento;
	public static volatile SingularAttribute<CosmoLStoricoPratica, String> descrizioneEvento;
	public static volatile SingularAttribute<CosmoLStoricoPratica, CosmoTPratica> pratica;
	public static volatile SingularAttribute<CosmoLStoricoPratica, CosmoTAttivita> attivita;
	public static volatile SingularAttribute<CosmoLStoricoPratica, CosmoTUtente> utente;
	public static volatile SingularAttribute<CosmoLStoricoPratica, CosmoTFruitore> fruitore;
	public static volatile SingularAttribute<CosmoLStoricoPratica, CosmoTUtente> utenteCoinvolto;
	public static volatile SingularAttribute<CosmoLStoricoPratica, CosmoTGruppo> gruppoCoinvolto;
}
