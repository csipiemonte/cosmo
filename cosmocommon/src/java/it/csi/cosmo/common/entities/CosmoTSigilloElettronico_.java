package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.545+0100")
@StaticMetamodel(CosmoTSigilloElettronico.class)
public class CosmoTSigilloElettronico_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTSigilloElettronico, Long> id;
	public static volatile SingularAttribute<CosmoTSigilloElettronico, CosmoTPratica> cosmoTPratica;
	public static volatile SingularAttribute<CosmoTSigilloElettronico, String> identificativoEvento;
	public static volatile SingularAttribute<CosmoTSigilloElettronico, String> identificativoAlias;
	public static volatile SingularAttribute<CosmoTSigilloElettronico, Boolean> utilizzato;
	public static volatile ListAttribute<CosmoTSigilloElettronico, CosmoRSigilloDocumento> cosmoRSigilloDocumentos;
}
