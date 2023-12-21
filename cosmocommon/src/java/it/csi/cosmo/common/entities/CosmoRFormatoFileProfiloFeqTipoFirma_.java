package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.858+0100")
@StaticMetamodel(CosmoRFormatoFileProfiloFeqTipoFirma.class)
public class CosmoRFormatoFileProfiloFeqTipoFirma_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRFormatoFileProfiloFeqTipoFirma, CosmoRFormatoFileProfiloFeqTipoFirmaPK> id;
	public static volatile SingularAttribute<CosmoRFormatoFileProfiloFeqTipoFirma, CosmoDFormatoFile> cosmoDFormatoFile;
	public static volatile SingularAttribute<CosmoRFormatoFileProfiloFeqTipoFirma, CosmoDProfiloFeq> cosmoDProfiloFeq;
	public static volatile SingularAttribute<CosmoRFormatoFileProfiloFeqTipoFirma, CosmoDTipoFirma> cosmoDTipoFirma;
}
