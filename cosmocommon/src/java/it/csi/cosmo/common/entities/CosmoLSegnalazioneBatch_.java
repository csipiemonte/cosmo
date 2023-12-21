package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.batch.model.BatchReportedEventLevel;
import it.csi.cosmo.common.entities.proto.CosmoLEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.734+0100")
@StaticMetamodel(CosmoLSegnalazioneBatch.class)
public class CosmoLSegnalazioneBatch_ extends CosmoLEntity_ {
	public static volatile SingularAttribute<CosmoLSegnalazioneBatch, Long> id;
	public static volatile SingularAttribute<CosmoLSegnalazioneBatch, CosmoLEsecuzioneBatch> esecuzione;
	public static volatile SingularAttribute<CosmoLSegnalazioneBatch, BatchReportedEventLevel> livello;
	public static volatile SingularAttribute<CosmoLSegnalazioneBatch, String> messaggio;
	public static volatile SingularAttribute<CosmoLSegnalazioneBatch, String> dettagli;
}
