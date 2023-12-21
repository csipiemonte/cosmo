package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.462+0100")
@StaticMetamodel(CosmoDTipoDocumento.class)
public class CosmoDTipoDocumento_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDTipoDocumento, String> codice;
	public static volatile SingularAttribute<CosmoDTipoDocumento, String> descrizione;
	public static volatile SingularAttribute<CosmoDTipoDocumento, Boolean> firmabile;
	public static volatile SingularAttribute<CosmoDTipoDocumento, String> codiceStardas;
	public static volatile SingularAttribute<CosmoDTipoDocumento, Long> dimensioneMassima;
	public static volatile ListAttribute<CosmoDTipoDocumento, CosmoRTipodocTipopratica> cosmoRTipodocTipopraticas;
	public static volatile ListAttribute<CosmoDTipoDocumento, CosmoTDocumento> cosmoTDocumentos;
	public static volatile ListAttribute<CosmoDTipoDocumento, CosmoRFormatoFileTipoDocumento> cosmoRFormatoFileTipoDocumentos;
	public static volatile ListAttribute<CosmoDTipoDocumento, CosmoRTipoDocumentoTipoDocumento> cosmoRTipoDocumentoTipoDocumentosAllegato;
	public static volatile ListAttribute<CosmoDTipoDocumento, CosmoRTipoDocumentoTipoDocumento> cosmoRTipoDocumentoTipoDocumentosPadre;
	public static volatile ListAttribute<CosmoDTipoDocumento, CosmoTTemplateFea> cosmoTTemplateFeas;
}
