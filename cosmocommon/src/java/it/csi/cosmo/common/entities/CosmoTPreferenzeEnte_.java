package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.dto.PreferenzeEnteEntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.436+0100")
@StaticMetamodel(CosmoTPreferenzeEnte.class)
public class CosmoTPreferenzeEnte_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTPreferenzeEnte, Long> id;
	public static volatile SingularAttribute<CosmoTPreferenzeEnte, PreferenzeEnteEntity> valore;
	public static volatile SingularAttribute<CosmoTPreferenzeEnte, String> versione;
	public static volatile SingularAttribute<CosmoTPreferenzeEnte, CosmoTEnte> cosmoTEnte;
}
