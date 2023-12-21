package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.744+0100")
@StaticMetamodel(CosmoTVariabileProcesso.class)
public class CosmoTVariabileProcesso_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTVariabileProcesso, Long> id;
	public static volatile SingularAttribute<CosmoTVariabileProcesso, CosmoDTipoPratica> tipoPratica;
	public static volatile SingularAttribute<CosmoTVariabileProcesso, String> nomeVariabile;
	public static volatile SingularAttribute<CosmoTVariabileProcesso, String> nomeVariabileFlowable;
	public static volatile SingularAttribute<CosmoTVariabileProcesso, Boolean> visualizzareInTabella;
	public static volatile SingularAttribute<CosmoTVariabileProcesso, CosmoDFiltroCampo> filtroCampo;
	public static volatile SingularAttribute<CosmoTVariabileProcesso, CosmoDFormatoCampo> formatoCampo;
}
