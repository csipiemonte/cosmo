package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.342+0100")
@StaticMetamodel(CosmoRSmistamentoDocumento.class)
public class CosmoRSmistamentoDocumento_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoRSmistamentoDocumento, Long> id;
	public static volatile SingularAttribute<CosmoRSmistamentoDocumento, String> codiceEsitoSmistamento;
	public static volatile SingularAttribute<CosmoRSmistamentoDocumento, String> messageUuid;
	public static volatile SingularAttribute<CosmoRSmistamentoDocumento, String> messaggioEsitoSmistamento;
	public static volatile SingularAttribute<CosmoRSmistamentoDocumento, CosmoDStatoSmistamento> cosmoDStatoSmistamento;
	public static volatile SingularAttribute<CosmoRSmistamentoDocumento, CosmoTSmistamento> cosmoTSmistamento;
	public static volatile ListAttribute<CosmoRSmistamentoDocumento, CosmoTInfoAggiuntiveSmistamento> cosmoTInfoAggiuntiveSmistamentos;
	public static volatile SingularAttribute<CosmoRSmistamentoDocumento, CosmoTDocumento> cosmoTDocumento;
	public static volatile SingularAttribute<CosmoRSmistamentoDocumento, Integer> numeroRetry;
}
