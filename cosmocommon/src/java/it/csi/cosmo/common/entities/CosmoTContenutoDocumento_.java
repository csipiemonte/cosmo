package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.956+0100")
@StaticMetamodel(CosmoTContenutoDocumento.class)
public class CosmoTContenutoDocumento_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTContenutoDocumento, Long> id;
	public static volatile SingularAttribute<CosmoTContenutoDocumento, Long> dimensione;
	public static volatile SingularAttribute<CosmoTContenutoDocumento, String> nomeFile;
	public static volatile SingularAttribute<CosmoTContenutoDocumento, String> uuidNodo;
	public static volatile SingularAttribute<CosmoTContenutoDocumento, String> urlDownload;
	public static volatile SingularAttribute<CosmoTContenutoDocumento, CosmoDTipoFirma> tipoFirma;
	public static volatile SingularAttribute<CosmoTContenutoDocumento, CosmoDFormatoFile> formatoFile;
	public static volatile SingularAttribute<CosmoTContenutoDocumento, CosmoDTipoContenutoDocumento> tipo;
	public static volatile SingularAttribute<CosmoTContenutoDocumento, CosmoTContenutoDocumento> contenutoSorgente;
	public static volatile SingularAttribute<CosmoTContenutoDocumento, CosmoTDocumento> documentoPadre;
	public static volatile SingularAttribute<CosmoTContenutoDocumento, CosmoDEsitoVerificaFirma> esitoVerificaFirma;
	public static volatile SingularAttribute<CosmoTContenutoDocumento, Timestamp> dataVerificaFirma;
	public static volatile ListAttribute<CosmoTContenutoDocumento, CosmoTInfoVerificaFirma> infoVerificaFirme;
	public static volatile ListAttribute<CosmoTContenutoDocumento, CosmoTAnteprimaContenutoDocumento> anteprime;
	public static volatile SingularAttribute<CosmoTContenutoDocumento, String> shaFile;
	public static volatile SingularAttribute<CosmoTContenutoDocumento, CosmoDTipoContenutoFirmato> tipoContenutoFirmato;
}
