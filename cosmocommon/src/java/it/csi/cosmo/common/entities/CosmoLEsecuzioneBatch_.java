package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoLEntity_;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.718+0100")
@StaticMetamodel(CosmoLEsecuzioneBatch.class)
public class CosmoLEsecuzioneBatch_ extends CosmoLEntity_ {
	public static volatile SingularAttribute<CosmoLEsecuzioneBatch, Long> id;
	public static volatile SingularAttribute<CosmoLEsecuzioneBatch, String> codiceBatch;
	public static volatile SingularAttribute<CosmoLEsecuzioneBatch, Timestamp> dataAvvio;
	public static volatile SingularAttribute<CosmoLEsecuzioneBatch, Timestamp> dataFine;
	public static volatile SingularAttribute<CosmoLEsecuzioneBatch, String> codiceEsito;
	public static volatile ListAttribute<CosmoLEsecuzioneBatch, CosmoLSegnalazioneBatch> segnalazioni;
}
