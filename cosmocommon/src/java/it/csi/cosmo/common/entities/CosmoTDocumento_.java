package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.039+0100")
@StaticMetamodel(CosmoTDocumento.class)
public class CosmoTDocumento_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTDocumento, Long> id;
	public static volatile SingularAttribute<CosmoTDocumento, String> autore;
	public static volatile SingularAttribute<CosmoTDocumento, String> descrizione;
	public static volatile SingularAttribute<CosmoTDocumento, String> idDocParentExt;
	public static volatile SingularAttribute<CosmoTDocumento, String> idDocumentoExt;
	public static volatile SingularAttribute<CosmoTDocumento, String> titolo;
	public static volatile SingularAttribute<CosmoTDocumento, Integer> numeroTentativiAcquisizione;
	public static volatile SingularAttribute<CosmoTDocumento, CosmoDStatoDocumento> stato;
	public static volatile ListAttribute<CosmoTDocumento, CosmoTContenutoDocumento> contenuti;
	public static volatile SingularAttribute<CosmoTDocumento, CosmoDTipoDocumento> tipo;
	public static volatile SingularAttribute<CosmoTDocumento, CosmoTDocumento> documentoPadre;
	public static volatile ListAttribute<CosmoTDocumento, CosmoTDocumento> documentiFigli;
	public static volatile SingularAttribute<CosmoTDocumento, CosmoTPratica> pratica;
	public static volatile ListAttribute<CosmoTDocumento, CosmoRSmistamentoDocumento> cosmoRSmistamentoDocumentos;
	public static volatile ListAttribute<CosmoTDocumento, CosmoRInvioStiloDocumento> cosmoRInvioStiloDocumentos;
	public static volatile ListAttribute<CosmoTDocumento, CosmoTApprovazione> cosmoTApprovaziones;
	public static volatile ListAttribute<CosmoTDocumento, CosmoRSigilloDocumento> cosmoRSigilloDocumentos;
}
