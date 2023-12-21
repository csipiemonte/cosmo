package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.886+0100")
@StaticMetamodel(CosmoRFormatoFileTipoDocumento.class)
public class CosmoRFormatoFileTipoDocumento_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRFormatoFileTipoDocumento, CosmoRFormatoFileTipoDocumentoPK> id;
	public static volatile SingularAttribute<CosmoRFormatoFileTipoDocumento, CosmoDFormatoFile> cosmoDFormatoFile;
	public static volatile SingularAttribute<CosmoRFormatoFileTipoDocumento, CosmoDTipoDocumento> cosmoDTipoDocumento;
}
