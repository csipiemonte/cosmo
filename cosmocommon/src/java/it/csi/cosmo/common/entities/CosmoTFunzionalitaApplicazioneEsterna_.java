package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.161+0100")
@StaticMetamodel(CosmoTFunzionalitaApplicazioneEsterna.class)
public class CosmoTFunzionalitaApplicazioneEsterna_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTFunzionalitaApplicazioneEsterna, Long> id;
	public static volatile SingularAttribute<CosmoTFunzionalitaApplicazioneEsterna, String> descrizione;
	public static volatile SingularAttribute<CosmoTFunzionalitaApplicazioneEsterna, Boolean> principale;
	public static volatile SingularAttribute<CosmoTFunzionalitaApplicazioneEsterna, String> url;
	public static volatile ListAttribute<CosmoTFunzionalitaApplicazioneEsterna, CosmoRUtenteFunzionalitaApplicazioneEsterna> cosmoRUtenteFunzionalitaApplicazioneEsternas;
	public static volatile SingularAttribute<CosmoTFunzionalitaApplicazioneEsterna, CosmoREnteApplicazioneEsterna> cosmoREnteApplicazioneEsterna;
	public static volatile ListAttribute<CosmoTFunzionalitaApplicazioneEsterna, CosmoREnteFunzionalitaApplicazioneEsterna> cosmoREnteFunzionalitaApplicazioneEsternas;
}
