package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.500+0100")
@StaticMetamodel(CosmoTRisorsaTemplateReport.class)
public class CosmoTRisorsaTemplateReport_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTRisorsaTemplateReport, Long> id;
	public static volatile SingularAttribute<CosmoTRisorsaTemplateReport, String> codiceTipoPratica;
	public static volatile SingularAttribute<CosmoTRisorsaTemplateReport, Long> idEnte;
	public static volatile SingularAttribute<CosmoTRisorsaTemplateReport, String> codiceTemplate;
	public static volatile SingularAttribute<CosmoTRisorsaTemplateReport, String> codice;
	public static volatile SingularAttribute<CosmoTRisorsaTemplateReport, byte[]> contenutoRisorsa;
	public static volatile SingularAttribute<CosmoTRisorsaTemplateReport, String> nodoRisorsa;
	public static volatile SingularAttribute<CosmoTRisorsaTemplateReport, CosmoDTipoPratica> tipoPratica;
	public static volatile SingularAttribute<CosmoTRisorsaTemplateReport, CosmoTEnte> ente;
}
