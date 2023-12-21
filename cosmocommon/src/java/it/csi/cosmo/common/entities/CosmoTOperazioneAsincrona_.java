package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.async.model.LongTask.LongTaskStatus;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.366+0100")
@StaticMetamodel(CosmoTOperazioneAsincrona.class)
public class CosmoTOperazioneAsincrona_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTOperazioneAsincrona, Long> id;
	public static volatile SingularAttribute<CosmoTOperazioneAsincrona, String> uuid;
	public static volatile SingularAttribute<CosmoTOperazioneAsincrona, LongTaskStatus> stato;
	public static volatile SingularAttribute<CosmoTOperazioneAsincrona, Timestamp> dataAvvio;
	public static volatile SingularAttribute<CosmoTOperazioneAsincrona, Timestamp> dataFine;
	public static volatile SingularAttribute<CosmoTOperazioneAsincrona, Long> versione;
}
