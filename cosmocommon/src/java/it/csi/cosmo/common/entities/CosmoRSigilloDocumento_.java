package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.323+0100")
@StaticMetamodel(CosmoRSigilloDocumento.class)
public class CosmoRSigilloDocumento_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRSigilloDocumento, Long> id;
	public static volatile SingularAttribute<CosmoRSigilloDocumento, String> codiceEsitoSigillo;
	public static volatile SingularAttribute<CosmoRSigilloDocumento, String> messaggioEsitoSigillo;
	public static volatile SingularAttribute<CosmoRSigilloDocumento, CosmoDStatoSigilloElettronico> cosmoDStatoSigilloElettronico;
	public static volatile SingularAttribute<CosmoRSigilloDocumento, CosmoTSigilloElettronico> cosmoTSigilloElettronico;
	public static volatile SingularAttribute<CosmoRSigilloDocumento, CosmoTDocumento> cosmoTDocumento;
}
