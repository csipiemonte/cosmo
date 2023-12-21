package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.249+0100")
@StaticMetamodel(CosmoTInfoVerificaFirma.class)
public class CosmoTInfoVerificaFirma_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTInfoVerificaFirma, Long> id;
	public static volatile SingularAttribute<CosmoTInfoVerificaFirma, String> cfFirmatario;
	public static volatile SingularAttribute<CosmoTInfoVerificaFirma, Timestamp> dtVerificaFirma;
	public static volatile SingularAttribute<CosmoTInfoVerificaFirma, Timestamp> dtApposizione;
	public static volatile SingularAttribute<CosmoTInfoVerificaFirma, Timestamp> dtApposizioneMarcaturaTemporale;
	public static volatile SingularAttribute<CosmoTInfoVerificaFirma, String> firmatario;
	public static volatile SingularAttribute<CosmoTInfoVerificaFirma, String> organizzazione;
	public static volatile SingularAttribute<CosmoTInfoVerificaFirma, String> codiceErrore;
	public static volatile SingularAttribute<CosmoTInfoVerificaFirma, CosmoTContenutoDocumento> contenutoDocumentoPadre;
	public static volatile SingularAttribute<CosmoTInfoVerificaFirma, CosmoDEsitoVerificaFirma> esito;
	public static volatile SingularAttribute<CosmoTInfoVerificaFirma, CosmoTInfoVerificaFirma> infoVerificaFirmaPadre;
	public static volatile ListAttribute<CosmoTInfoVerificaFirma, CosmoTInfoVerificaFirma> infoVerificaFirmeFiglie;
}
