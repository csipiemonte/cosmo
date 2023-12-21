package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.753+0100")
@StaticMetamodel(CosmoTAnteprimaContenutoDocumento.class)
public class CosmoTAnteprimaContenutoDocumento_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTAnteprimaContenutoDocumento, Long> id;
	public static volatile SingularAttribute<CosmoTAnteprimaContenutoDocumento, Long> dimensione;
	public static volatile SingularAttribute<CosmoTAnteprimaContenutoDocumento, String> nomeFile;
	public static volatile SingularAttribute<CosmoTAnteprimaContenutoDocumento, String> uuidNodo;
	public static volatile SingularAttribute<CosmoTAnteprimaContenutoDocumento, String> shareUrl;
	public static volatile SingularAttribute<CosmoTAnteprimaContenutoDocumento, CosmoDFormatoFile> formatoFile;
	public static volatile SingularAttribute<CosmoTAnteprimaContenutoDocumento, CosmoTContenutoDocumento> contenuto;
}
