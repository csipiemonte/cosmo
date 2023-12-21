package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.614+0100")
@StaticMetamodel(CosmoTTemplateFea.class)
public class CosmoTTemplateFea_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTTemplateFea, Long> id;
	public static volatile SingularAttribute<CosmoTTemplateFea, Double> coordinataX;
	public static volatile SingularAttribute<CosmoTTemplateFea, Double> coordinataY;
	public static volatile SingularAttribute<CosmoTTemplateFea, String> descrizione;
	public static volatile SingularAttribute<CosmoTTemplateFea, CosmoTEnte> ente;
	public static volatile SingularAttribute<CosmoTTemplateFea, Long> pagina;
	public static volatile SingularAttribute<CosmoTTemplateFea, Boolean> caricatoDaTemplate;
	public static volatile SingularAttribute<CosmoTTemplateFea, CosmoDTipoDocumento> tipologiaDocumento;
	public static volatile SingularAttribute<CosmoTTemplateFea, CosmoDTipoPratica> tipologiaPratica;
	public static volatile SingularAttribute<CosmoTTemplateFea, CosmoTPratica> cosmoTPratica;
}
