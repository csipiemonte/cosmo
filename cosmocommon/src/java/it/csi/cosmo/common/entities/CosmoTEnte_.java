package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.087+0100")
@StaticMetamodel(CosmoTEnte.class)
public class CosmoTEnte_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTEnte, Long> id;
	public static volatile SingularAttribute<CosmoTEnte, String> codiceIpa;
	public static volatile SingularAttribute<CosmoTEnte, String> codiceFiscale;
	public static volatile SingularAttribute<CosmoTEnte, byte[]> logo;
	public static volatile SingularAttribute<CosmoTEnte, String> nome;
	public static volatile ListAttribute<CosmoTEnte, CosmoCConfigurazioneEnte> cosmoCConfigurazioneEntes;
	public static volatile ListAttribute<CosmoTEnte, CosmoTPreferenzeEnte> cosmoTPreferenzeEntes;
	public static volatile ListAttribute<CosmoTEnte, CosmoRUtenteEnte> cosmoRUtenteEntes;
	public static volatile ListAttribute<CosmoTEnte, CosmoRUtenteProfilo> cosmoRUtenteProfilos;
	public static volatile ListAttribute<CosmoTEnte, CosmoTGruppo> cosmoTGruppos;
	public static volatile ListAttribute<CosmoTEnte, CosmoRFruitoreEnte> cosmoRFruitoreEntes;
	public static volatile ListAttribute<CosmoTEnte, CosmoTPratica> cosmoTPraticas;
	public static volatile ListAttribute<CosmoTEnte, CosmoREnteCertificatoreEnte> cosmoREnteCertificatoreEntes;
	public static volatile ListAttribute<CosmoTEnte, CosmoREnteApplicazioneEsterna> cosmoREnteApplicazioneEsternas;
	public static volatile ListAttribute<CosmoTEnte, CosmoREnteFunzionalitaApplicazioneEsterna> cosmoREnteFunzionalitaApplicazioneEsternas;
	public static volatile ListAttribute<CosmoTEnte, CosmoRNotificaUtenteEnte> cosmoRNotificaUtenteEntes;
	public static volatile ListAttribute<CosmoTEnte, CosmoTFormLogico> cosmoTFormLogicos;
	public static volatile ListAttribute<CosmoTEnte, CosmoTCaricamentoPratica> cosmoTCaricamentoPraticas;
	public static volatile ListAttribute<CosmoTEnte, CosmoTOtp> cosmoTOtps;
}
