package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.230+0100")
@StaticMetamodel(CosmoTInfoAggiuntiveSmistamento.class)
public class CosmoTInfoAggiuntiveSmistamento_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTInfoAggiuntiveSmistamento, Long> id;
	public static volatile SingularAttribute<CosmoTInfoAggiuntiveSmistamento, String> codInformazione;
	public static volatile SingularAttribute<CosmoTInfoAggiuntiveSmistamento, String> valore;
	public static volatile SingularAttribute<CosmoTInfoAggiuntiveSmistamento, CosmoRSmistamentoDocumento> cosmoRSmistamentoDocumento;
}
