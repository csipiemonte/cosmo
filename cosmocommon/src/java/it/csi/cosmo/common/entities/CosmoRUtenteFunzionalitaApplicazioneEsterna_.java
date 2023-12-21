package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.546+0100")
@StaticMetamodel(CosmoRUtenteFunzionalitaApplicazioneEsterna.class)
public class CosmoRUtenteFunzionalitaApplicazioneEsterna_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRUtenteFunzionalitaApplicazioneEsterna, CosmoRUtenteFunzionalitaApplicazioneEsternaPK> id;
	public static volatile SingularAttribute<CosmoRUtenteFunzionalitaApplicazioneEsterna, CosmoTFunzionalitaApplicazioneEsterna> cosmoTFunzionalitaApplicazioneEsterna;
	public static volatile SingularAttribute<CosmoRUtenteFunzionalitaApplicazioneEsterna, CosmoTUtente> cosmoTUtente;
	public static volatile SingularAttribute<CosmoRUtenteFunzionalitaApplicazioneEsterna, Integer> posizione;
}
