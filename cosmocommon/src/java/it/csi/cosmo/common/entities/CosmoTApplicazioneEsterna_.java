package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.781+0100")
@StaticMetamodel(CosmoTApplicazioneEsterna.class)
public class CosmoTApplicazioneEsterna_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTApplicazioneEsterna, Long> id;
	public static volatile SingularAttribute<CosmoTApplicazioneEsterna, String> descrizione;
	public static volatile SingularAttribute<CosmoTApplicazioneEsterna, byte[]> icona;
	public static volatile ListAttribute<CosmoTApplicazioneEsterna, CosmoREnteApplicazioneEsterna> cosmoREnteApplicazioneEsternas;
}
