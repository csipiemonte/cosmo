package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.637+0100")
@StaticMetamodel(CosmoTTemplateReport.class)
public class CosmoTTemplateReport_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTTemplateReport, Long> id;
	public static volatile SingularAttribute<CosmoTTemplateReport, String> codice;
	public static volatile SingularAttribute<CosmoTTemplateReport, String> codiceTemplatePadre;
	public static volatile SingularAttribute<CosmoTTemplateReport, String> codiceTipoPratica;
	public static volatile SingularAttribute<CosmoTTemplateReport, Long> idEnte;
	public static volatile SingularAttribute<CosmoTTemplateReport, byte[]> sorgenteTemplate;
	public static volatile SingularAttribute<CosmoTTemplateReport, byte[]> templateCompilato;
	public static volatile SingularAttribute<CosmoTTemplateReport, CosmoDTipoPratica> tipoPratica;
	public static volatile SingularAttribute<CosmoTTemplateReport, CosmoTEnte> ente;
}
