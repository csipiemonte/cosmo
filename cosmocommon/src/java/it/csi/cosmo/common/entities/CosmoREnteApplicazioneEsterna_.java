package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.783+0100")
@StaticMetamodel(CosmoREnteApplicazioneEsterna.class)
public class CosmoREnteApplicazioneEsterna_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoREnteApplicazioneEsterna, Long> id;
	public static volatile SingularAttribute<CosmoREnteApplicazioneEsterna, CosmoTApplicazioneEsterna> cosmoTApplicazioneEsterna;
	public static volatile SingularAttribute<CosmoREnteApplicazioneEsterna, CosmoTEnte> cosmoTEnte;
	public static volatile ListAttribute<CosmoREnteApplicazioneEsterna, CosmoTFunzionalitaApplicazioneEsterna> cosmoTFunzionalitaApplicazioneEsternas;
}
