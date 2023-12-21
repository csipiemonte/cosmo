package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.523+0100")
@StaticMetamodel(CosmoTSchemaAutenticazioneFruitore.class)
public class CosmoTSchemaAutenticazioneFruitore_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTSchemaAutenticazioneFruitore, Long> id;
	public static volatile SingularAttribute<CosmoTSchemaAutenticazioneFruitore, CosmoTFruitore> fruitore;
	public static volatile ListAttribute<CosmoTSchemaAutenticazioneFruitore, CosmoTCredenzialiAutenticazioneFruitore> credenziali;
	public static volatile SingularAttribute<CosmoTSchemaAutenticazioneFruitore, CosmoDTipoSchemaAutenticazione> tipo;
	public static volatile SingularAttribute<CosmoTSchemaAutenticazioneFruitore, Boolean> inIngresso;
	public static volatile SingularAttribute<CosmoTSchemaAutenticazioneFruitore, String> tokenEndpoint;
	public static volatile SingularAttribute<CosmoTSchemaAutenticazioneFruitore, String> mappaturaRichiestaToken;
	public static volatile SingularAttribute<CosmoTSchemaAutenticazioneFruitore, String> mappaturaOutputToken;
	public static volatile SingularAttribute<CosmoTSchemaAutenticazioneFruitore, String> metodoRichiestaToken;
	public static volatile SingularAttribute<CosmoTSchemaAutenticazioneFruitore, String> contentTypeRichiestaToken;
	public static volatile SingularAttribute<CosmoTSchemaAutenticazioneFruitore, String> nomeHeader;
	public static volatile SingularAttribute<CosmoTSchemaAutenticazioneFruitore, String> formatoHeader;
}
