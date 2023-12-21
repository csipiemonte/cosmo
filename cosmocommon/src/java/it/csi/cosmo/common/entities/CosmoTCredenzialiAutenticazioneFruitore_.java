package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.991+0100")
@StaticMetamodel(CosmoTCredenzialiAutenticazioneFruitore.class)
public class CosmoTCredenzialiAutenticazioneFruitore_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTCredenzialiAutenticazioneFruitore, Long> id;
	public static volatile SingularAttribute<CosmoTCredenzialiAutenticazioneFruitore, String> clientId;
	public static volatile SingularAttribute<CosmoTCredenzialiAutenticazioneFruitore, String> clientSecret;
	public static volatile SingularAttribute<CosmoTCredenzialiAutenticazioneFruitore, String> password;
	public static volatile SingularAttribute<CosmoTCredenzialiAutenticazioneFruitore, String> username;
	public static volatile SingularAttribute<CosmoTCredenzialiAutenticazioneFruitore, CosmoTSchemaAutenticazioneFruitore> schemaAutenticazione;
}
