package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoCEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:00.632+0100")
@StaticMetamodel(CosmoCConfigurazioneEnte.class)
public class CosmoCConfigurazioneEnte_ extends CosmoCEntity_ {
	public static volatile SingularAttribute<CosmoCConfigurazioneEnte, CosmoCConfigurazioneEntePK> id;
	public static volatile SingularAttribute<CosmoCConfigurazioneEnte, String> descrizione;
	public static volatile SingularAttribute<CosmoCConfigurazioneEnte, String> valore;
	public static volatile SingularAttribute<CosmoCConfigurazioneEnte, CosmoTEnte> cosmoTEnte;
}
