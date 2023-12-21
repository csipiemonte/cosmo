package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.208+0100")
@StaticMetamodel(CosmoTHelper.class)
public class CosmoTHelper_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTHelper, Long> id;
	public static volatile SingularAttribute<CosmoTHelper, String> html;
	public static volatile SingularAttribute<CosmoTHelper, CosmoDHelperPagina> helperPagina;
	public static volatile SingularAttribute<CosmoTHelper, CosmoDHelperTab> helperTab;
	public static volatile SingularAttribute<CosmoTHelper, CosmoDCustomFormFormio> helperForm;
	public static volatile SingularAttribute<CosmoTHelper, CosmoDHelperModale> helperModale;
}
