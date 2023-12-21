package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.029+0100")
@StaticMetamodel(CosmoRInvioStiloDocumento.class)
public class CosmoRInvioStiloDocumento_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRInvioStiloDocumento, CosmoRInvioStiloDocumentoPK> id;
	public static volatile SingularAttribute<CosmoRInvioStiloDocumento, String> codiceEsitoInvioStilo;
	public static volatile SingularAttribute<CosmoRInvioStiloDocumento, String> messaggioEsitoInvioStilo;
	public static volatile SingularAttribute<CosmoRInvioStiloDocumento, Integer> numeroRetry;
	public static volatile SingularAttribute<CosmoRInvioStiloDocumento, CosmoDStatoInvioStilo> cosmoDStatoInvioStilo;
	public static volatile SingularAttribute<CosmoRInvioStiloDocumento, CosmoTDocumento> cosmoTDocumento;
}
