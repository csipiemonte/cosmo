package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.590+0100")
@StaticMetamodel(CosmoTTag.class)
public class CosmoTTag_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTTag, Long> id;
	public static volatile SingularAttribute<CosmoTTag, String> codice;
	public static volatile SingularAttribute<CosmoTTag, String> descrizione;
	public static volatile SingularAttribute<CosmoTTag, CosmoTEnte> cosmoTEnte;
	public static volatile SingularAttribute<CosmoTTag, CosmoDTipoTag> cosmoDTipoTag;
	public static volatile ListAttribute<CosmoTTag, CosmoRPraticaTag> cosmoRPraticaTags;
	public static volatile ListAttribute<CosmoTTag, CosmoRUtenteGruppoTag> cosmoRUtenteGruppoTags;
}
