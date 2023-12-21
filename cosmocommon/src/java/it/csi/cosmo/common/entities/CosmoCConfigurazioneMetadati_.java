package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoCEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:00.749+0100")
@StaticMetamodel(CosmoCConfigurazioneMetadati.class)
public class CosmoCConfigurazioneMetadati_ extends CosmoCEntity_ {
	public static volatile SingularAttribute<CosmoCConfigurazioneMetadati, String> chiave;
	public static volatile SingularAttribute<CosmoCConfigurazioneMetadati, String> descrizione;
	public static volatile SingularAttribute<CosmoCConfigurazioneMetadati, String> valore;
	public static volatile SingularAttribute<CosmoCConfigurazioneMetadati, CosmoDTipoPratica> cosmoDTipoPratica;
}
