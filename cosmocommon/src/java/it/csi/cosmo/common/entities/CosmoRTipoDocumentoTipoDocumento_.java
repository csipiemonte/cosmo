package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.463+0100")
@StaticMetamodel(CosmoRTipoDocumentoTipoDocumento.class)
public class CosmoRTipoDocumentoTipoDocumento_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRTipoDocumentoTipoDocumento, CosmoRTipoDocumentoTipoDocumentoPK> id;
	public static volatile SingularAttribute<CosmoRTipoDocumentoTipoDocumento, String> codiceStardasAllegato;
	public static volatile SingularAttribute<CosmoRTipoDocumentoTipoDocumento, CosmoDTipoDocumento> cosmoDTipoDocumentoAllegato;
	public static volatile SingularAttribute<CosmoRTipoDocumentoTipoDocumento, CosmoDTipoDocumento> cosmoDTipoDocumentoPadre;
	public static volatile SingularAttribute<CosmoRTipoDocumentoTipoDocumento, CosmoDTipoPratica> cosmoDTipoPratica;
}
