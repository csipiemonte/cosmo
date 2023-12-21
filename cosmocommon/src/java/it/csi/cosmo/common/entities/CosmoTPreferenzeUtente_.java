package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.dto.PreferenzeUtenteEntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.457+0100")
@StaticMetamodel(CosmoTPreferenzeUtente.class)
public class CosmoTPreferenzeUtente_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTPreferenzeUtente, Long> id;
	public static volatile SingularAttribute<CosmoTPreferenzeUtente, PreferenzeUtenteEntity> valore;
	public static volatile SingularAttribute<CosmoTPreferenzeUtente, String> versione;
	public static volatile SingularAttribute<CosmoTPreferenzeUtente, CosmoTUtente> cosmoTUtente;
}
