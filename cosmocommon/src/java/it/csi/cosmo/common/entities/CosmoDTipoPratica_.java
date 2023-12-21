package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.580+0100")
@StaticMetamodel(CosmoDTipoPratica.class)
public class CosmoDTipoPratica_ extends CosmoDEntity_ {
	public static volatile SingularAttribute<CosmoDTipoPratica, String> codice;
	public static volatile SingularAttribute<CosmoDTipoPratica, Boolean> annullabile;
	public static volatile SingularAttribute<CosmoDTipoPratica, Boolean> assegnabile;
	public static volatile SingularAttribute<CosmoDTipoPratica, String> caseDefinitionKey;
	public static volatile SingularAttribute<CosmoDTipoPratica, String> codiceApplicazioneStardas;
	public static volatile SingularAttribute<CosmoDTipoPratica, Boolean> condivisibile;
	public static volatile SingularAttribute<CosmoDTipoPratica, Boolean> creabileDaInterfaccia;
	public static volatile SingularAttribute<CosmoDTipoPratica, Boolean> creabileDaServizio;
	public static volatile SingularAttribute<CosmoDTipoPratica, String> descrizione;
	public static volatile SingularAttribute<CosmoDTipoPratica, Boolean> overrideResponsabileTrattamento;
	public static volatile SingularAttribute<CosmoDTipoPratica, String> processDefinitionKey;
	public static volatile SingularAttribute<CosmoDTipoPratica, String> responsabileTrattamentoStardas;
	public static volatile SingularAttribute<CosmoDTipoPratica, Boolean> overrideFruitoreDefault;
	public static volatile SingularAttribute<CosmoDTipoPratica, String> codiceFruitoreStardas;
	public static volatile SingularAttribute<CosmoDTipoPratica, String> registrazioneStilo;
	public static volatile SingularAttribute<CosmoDTipoPratica, String> tipoUnitaDocumentariaStilo;
	public static volatile SingularAttribute<CosmoDTipoPratica, byte[]> icona;
	public static volatile SingularAttribute<CosmoDTipoPratica, CosmoDEnteCertificatore> cosmoDEnteCertificatore;
	public static volatile SingularAttribute<CosmoDTipoPratica, CosmoDProfiloFeq> cosmoDProfiloFeq;
	public static volatile SingularAttribute<CosmoDTipoPratica, CosmoDSceltaMarcaTemporale> cosmoDSceltaMarcaTemporale;
	public static volatile SingularAttribute<CosmoDTipoPratica, CosmoDTipoCredenzialiFirma> cosmoDTipoCredenzialiFirma;
	public static volatile SingularAttribute<CosmoDTipoPratica, CosmoDTipoOtp> cosmoDTipoOtp;
	public static volatile SingularAttribute<CosmoDTipoPratica, CosmoTEnte> cosmoTEnte;
	public static volatile ListAttribute<CosmoDTipoPratica, CosmoCConfigurazioneMetadati> cosmoCConfigurazioneMetadatis;
	public static volatile ListAttribute<CosmoDTipoPratica, CosmoRTipodocTipopratica> cosmoRTipodocTipopraticas;
	public static volatile ListAttribute<CosmoDTipoPratica, CosmoTPratica> cosmoTPraticas;
	public static volatile ListAttribute<CosmoDTipoPratica, CosmoRStatoTipoPratica> cosmoRStatoTipoPraticas;
	public static volatile ListAttribute<CosmoDTipoPratica, CosmoDTrasformazioneDatiPratica> trasformazioni;
	public static volatile ListAttribute<CosmoDTipoPratica, CosmoRGruppoTipoPratica> cosmoRGruppoTipoPraticas;
	public static volatile ListAttribute<CosmoDTipoPratica, CosmoDCustomFormFormio> cosmoDCustomFormFormios;
	public static volatile ListAttribute<CosmoDTipoPratica, CosmoRTabDettaglioTipoPratica> cosmoRTabDettaglioTipoPraticas;
	public static volatile ListAttribute<CosmoDTipoPratica, CosmoTVariabileProcesso> cosmoTVariabileProcessos;
	public static volatile ListAttribute<CosmoDTipoPratica, CosmoRTipoDocumentoTipoDocumento> cosmoRTipoDocumentoTipoDocumentos;
	public static volatile ListAttribute<CosmoDTipoPratica, CosmoTTemplateFea> cosmoTTemplateFeas;
}
