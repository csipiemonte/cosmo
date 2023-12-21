package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.567+0100")
@StaticMetamodel(CosmoTSmistamento.class)
public class CosmoTSmistamento_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTSmistamento, Long> id;
	public static volatile SingularAttribute<CosmoTSmistamento, CosmoTPratica> cosmoTPratica;
	public static volatile SingularAttribute<CosmoTSmistamento, String> identificativoEvento;
	public static volatile SingularAttribute<CosmoTSmistamento, Boolean> utilizzato;
	public static volatile ListAttribute<CosmoTSmistamento, CosmoRSmistamentoDocumento> cosmoRSmistamentoDocumentos;
}
