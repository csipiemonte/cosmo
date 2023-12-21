package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:03.671+0100")
@StaticMetamodel(CosmoTUtente.class)
public class CosmoTUtente_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTUtente, Long> id;
	public static volatile SingularAttribute<CosmoTUtente, String> codiceFiscale;
	public static volatile SingularAttribute<CosmoTUtente, String> cognome;
	public static volatile SingularAttribute<CosmoTUtente, String> nome;
	public static volatile ListAttribute<CosmoTUtente, CosmoTPreferenzeUtente> cosmoTPreferenzeUtentes;
	public static volatile ListAttribute<CosmoTUtente, CosmoRNotificaUtenteEnte> cosmoRNotificaUtenteEntes;
	public static volatile ListAttribute<CosmoTUtente, CosmoRUtenteEnte> cosmoRUtenteEntes;
	public static volatile ListAttribute<CosmoTUtente, CosmoRUtenteProfilo> cosmoRUtenteProfilos;
	public static volatile ListAttribute<CosmoTUtente, CosmoTCertificatoFirma> cosmoTCertificatoFirmas;
	public static volatile ListAttribute<CosmoTUtente, CosmoRPraticaUtenteGruppo> cosmoRPraticaUtenteGruppos;
	public static volatile ListAttribute<CosmoTUtente, CosmoRUtenteFunzionalitaApplicazioneEsterna> cosmoRUtenteFunzionalitaApplicazioneEsternas;
	public static volatile ListAttribute<CosmoTUtente, CosmoTGruppo> cosmoTGruppos;
	public static volatile ListAttribute<CosmoTUtente, CosmoTUtenteGruppo> cosmoTUtenteGruppos;
	public static volatile ListAttribute<CosmoTUtente, CosmoTCaricamentoPratica> cosmoTCaricamentoPraticas;
}
