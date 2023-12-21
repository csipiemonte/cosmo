DROP ALL OBJECTS;

CREATE TABLE cosmo_c_configurazione
(
   chiave         char(100)       NOT NULL,
   valore         varchar(255)   NOT NULL,
   descrizione    varchar(500),
   dt_inizio_val  timestamp      NOT NULL,
   dt_fine_val    timestamp,
   CONSTRAINT pk_cosmo_c_configurazione PRIMARY KEY (chiave),
   CONSTRAINT uq_cosmo_c_configurazione_chiave UNIQUE (chiave)
);

CREATE TABLE cosmo_d_operazione_fruitore (
	codice varchar(100) NOT NULL,
	descrizione varchar(255) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	personalizzabile boolean NULL,
	CONSTRAINT "PK_cosmo_d_operazione_fruitore" PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_tipo_schema_autenticazione (
	codice varchar(100) NOT NULL,
	descrizione varchar(255) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT "PK_cosmo_d_tipo_schema_autenticazione" PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_stato_pratica (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	classe varchar(100) NULL,
	CONSTRAINT pk_cosmo_d_stato_pratica PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_tipo_condivisione_pratica (
	codice varchar(10) NOT NULL,
	descrizione varchar(100) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT "PK_cosmo_d_tipo_condivisione_pratica" PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_categoria_use_case
(
   codice         char(10)       NOT NULL,
   descrizione    varchar(100)   NOT NULL,
   dt_inizio_val  timestamp      NOT NULL,
   dt_fine_val    timestamp,
   CONSTRAINT pk_cosmo_d_categoria_use_case PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_use_case
(
   codice            char(10)       NOT NULL,
   descrizione       varchar(100)   NOT NULL,
   codice_categoria  char(10)       NOT NULL,
   dt_inizio_val     timestamp      NOT NULL,
   dt_fine_val       timestamp,
   CONSTRAINT pk_cosmo_d_use_case PRIMARY KEY (codice),
   CONSTRAINT fk_cosmo_d_use_case_cw_d_categoria_use_case FOREIGN KEY (codice_categoria) REFERENCES cosmo_d_categoria_use_case (codice)
);

CREATE TABLE cosmo_d_funzionalita_form_logico (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	handler varchar(1000) NULL,
	multi_istanza boolean NOT NULL default false,
	eseguibile_massivamente boolean NOT NULL default false,
	CONSTRAINT "PK_cosmo_d_funzionalita" PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_tipo_relazione_pratica (
	codice varchar(100) NOT NULL,
	descrizione varchar(255) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	descrizione_inversa varchar(255) NULL,
	CONSTRAINT PK_cosmo_d_tipo_relazione_pratica PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_tipo_notifica (
    codice varchar(100) NOT NULL,
	descrizione varchar(100) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT PK_cosmo_d_tipo_notifica PRIMARY KEY (codice)
);

CREATE TABLE cosmo_t_profilo
(
   id                      bigserial      NOT NULL,
   codice                  varchar(10)    NOT NULL,
   descrizione             varchar(100),
   assegnabile 			   bool NOT NULL DEFAULT false,
   dt_inizio_val           timestamp      NOT NULL,
   dt_fine_val             timestamp,
   dt_inserimento          timestamp      NOT NULL,
   utente_inserimento      varchar(50)    NOT NULL,
   dt_ultima_modifica      timestamp,
   utente_ultima_modifica  varchar(50),
   dt_cancellazione        timestamp,
   utente_cancellazione    varchar(50),
   CONSTRAINT pk_cosmo_t_profilo PRIMARY KEY (id),
   CONSTRAINT uq_cosmo_t_profilo_codice UNIQUE (codice),
);
CREATE SEQUENCE COSMO_T_PROFILO_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_r_profilo_use_case
(
   id_profilo       bigint     NOT NULL,
   codice_use_case  char(10)   NOT NULL,
   CONSTRAINT pk_cosmo_r_profilo_use_case PRIMARY KEY (id_profilo, codice_use_case),
   CONSTRAINT fk_cosmo_r_profilo_use_case_cosmo_d_use_case FOREIGN KEY (codice_use_case) REFERENCES cosmo_d_use_case (codice) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_cosmo_r_profilo_use_case_cosmo_t_profilo FOREIGN KEY (id_profilo) REFERENCES cosmo_t_profilo (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE cosmo_t_utente
(
   id                      bigserial      NOT NULL,
   codice_fiscale          char(16)       NOT NULL,
   nome                    varchar(100)   NOT NULL,
   cognome                 varchar(100)   NOT NULL,
   dt_inserimento          timestamp      NOT NULL,
   utente_inserimento      varchar(50)    NOT NULL,
   dt_ultima_modifica      timestamp,
   utente_ultima_modifica  varchar(50),
   dt_cancellazione        timestamp,
   utente_cancellazione    varchar(50),
   CONSTRAINT pk_cosmo_t_utente PRIMARY KEY (id),
   CONSTRAINT uq_cosmo_t_utente_codice_fiscale UNIQUE (codice_fiscale),
);
CREATE SEQUENCE COSMO_T_UTENTE_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_preferenze_utente (
	id bigserial NOT NULL,
	id_utente int8 NOT NULL,
	versione varchar(12) NOT NULL,
	valore varchar(1000) NOT NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT pk_cosmo_t_preferenze_utente PRIMARY KEY (id),
	CONSTRAINT uq_cosmo_t_preferenze_utente_1 UNIQUE (id_utente, versione, dt_cancellazione),
	CONSTRAINT "FK_cosmo_t_preferenze_utente_cosmo_t_utente" FOREIGN KEY (id_utente) REFERENCES cosmo_t_utente(id)
);
CREATE SEQUENCE COSMO_T_PREFERENZE_UTENTE_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_ente
(
   id                      bigserial      NOT NULL,
   codice_ipa              varchar(10),
   nome                    varchar(255)   NOT NULL,
   codice_fiscale          varchar(16)   NOT NULL,
   logo                    bytea,
   dt_inserimento          timestamp      NOT NULL,
   utente_inserimento      varchar(50)    NOT NULL,
   dt_ultima_modifica      timestamp,
   utente_ultima_modifica  varchar(50),
   dt_cancellazione        timestamp,
   utente_cancellazione    varchar(50),
   CONSTRAINT pk_cosmo_t_ente PRIMARY KEY (id),
   CONSTRAINT uq_cosmo_t_ente_nome UNIQUE (nome)
);
CREATE SEQUENCE COSMO_T_ENTE_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_preferenze_ente (
	id bigserial NOT NULL,
	id_ente int8 NOT NULL,
	versione varchar(12) NOT NULL,
	valore varchar(1000) NOT NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT pk_cosmo_t_preferenze_ente PRIMARY KEY (id),
	CONSTRAINT uq_cosmo_t_preferenze_ente_1 UNIQUE (id_ente, versione, dt_cancellazione),
	CONSTRAINT "FK_cosmo_t_preferenze_ente_cosmo_t_ente" FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id)
);
CREATE SEQUENCE COSMO_T_PREFERENZE_ENTE_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_r_utente_ente (
	id_utente bigint NOT NULL,
	id_ente bigint NOT NULL,
	telefono varchar(30) NULL,
	email varchar(50) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_r_utente_ente PRIMARY KEY (id_ente, id_utente),
	CONSTRAINT fk_cosmo_r_utente_ente_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id),
	CONSTRAINT fk_cosmo_r_utente_ente_cosmo_t_utente FOREIGN KEY (id_utente) REFERENCES cosmo_t_utente(id)
);

CREATE TABLE cosmo_r_utente_profilo (
	id bigserial NOT NULL,
	id_utente bigint NOT NULL,
	id_profilo bigint NOT NULL,
	id_ente bigint NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_r_utente_profilo_ente PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_r_utente_profilo_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id),
	CONSTRAINT fk_cosmo_r_utente_profilo_cosmo_t_profilo FOREIGN KEY (id_profilo) REFERENCES cosmo_t_profilo(id),
	CONSTRAINT fk_cosmo_r_utente_profilo_cosmo_t_utente FOREIGN KEY (id_utente) REFERENCES cosmo_t_utente(id)
);
CREATE SEQUENCE COSMO_R_UTENTE_PROFILO_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE csi_log_audit
(
   id          bigserial      NOT NULL,
   data_ora    timestamp      NOT NULL,
   id_app      varchar(100)   NOT NULL,
   ip_address  varchar(40),
   utente      varchar(100)   NOT NULL,
   operazione  varchar(11)    NOT NULL,
   ogg_oper    varchar(150),
   key_oper    varchar(500),
   CONSTRAINT pk_csi_log_audit PRIMARY KEY (id)
);
CREATE SEQUENCE CSI_LOG_AUDIT_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_d_ente_certificatore (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	codice_ca varchar(1) NULL,
	codice_tsa varchar(20) NULL,
	provider varchar(20) NULL,
	CONSTRAINT pk_cosmo_d_ente_certificatore PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_tipo_credenziali_firma (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
    non_valido_in_apposizione boolean NOT NULL DEFAULT FALSE,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_tipo_credenziali_firma PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_tipo_otp (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
    non_valido_in_apposizione boolean NOT NULL DEFAULT FALSE,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_tipo_otp PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_profilo_feq (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
    non_valido_in_apposizione boolean NOT NULL DEFAULT FALSE,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_profilo_feq PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_scelta_marca_temporale (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
    non_valido_in_apposizione boolean NOT NULL DEFAULT FALSE,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_scelta_marca_temporale PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_tipo_pratica (
	codice varchar(255) NOT NULL,
	descrizione varchar(500) NULL,
	process_definition_key varchar(500) NULL,
	case_definition_key varchar(500) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	codice_applicazione_stardas varchar(50) NULL,
	id_ente int8 NOT NULL,
	creabile_da_interfaccia boolean NOT NULL default true,
	creabile_da_servizio boolean NOT NULL default true,
	responsabile_trattamento_stardas varchar(100),
	override_responsabile_trattamento boolean,
	annullabile bool NOT NULL DEFAULT true,
	condivisibile bool NOT NULL DEFAULT true,
	assegnabile bool NOT NULL DEFAULT true,
	codice_fruitore_stardas varchar(100) NULL,
	override_fruitore_default boolean NOT NULL DEFAULT false,
	registrazione_stilo varchar(10) NULL,	
	tipo_unita_documentaria_stilo varchar(100) NULL,
	icona bytea NULL,
	ente_certificatore varchar(100) NULL,
	tipo_credenziali_firma varchar(100) NULL,
	tipo_otp varchar(100) NULL,
	profilo_feq varchar(100) NULL,
	scelta_marca_temporale varchar(100) NULL,
	CONSTRAINT "CHK_cosmo_d_tipo_pratica_1" CHECK ((((process_definition_key IS NOT NULL) AND (case_definition_key IS NULL)) OR ((process_definition_key IS NULL) AND (case_definition_key IS NOT NULL)))),
	CONSTRAINT "CHK_cosmo_d_tipo_pratica_2" CHECK (creabile_da_interfaccia OR creabile_da_servizio),
	CONSTRAINT "PK_cosmo_d_tipo_pratica" PRIMARY KEY (codice),
	CONSTRAINT "fk_cosmo_d_tipo_pratica_cosmo_t_ente" FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id),
	CONSTRAINT "fk_cosmo_d_tipo_pratica_cosmo_d_ente_certificatore" FOREIGN KEY (ente_certificatore) REFERENCES cosmo_d_ente_certificatore(codice),
	CONSTRAINT "fk_cosmo_d_tipo_pratica_cosmo_d_profilo_feq" FOREIGN KEY (profilo_feq) REFERENCES cosmo_d_profilo_feq(codice),
	CONSTRAINT "fk_cosmo_d_tipo_pratica_cosmo_d_scelta_marca_temporale" FOREIGN KEY (scelta_marca_temporale) REFERENCES cosmo_d_scelta_marca_temporale(codice),
	CONSTRAINT "fk_cosmo_d_tipo_pratica_cosmo_d_tipo_credenziali_firma" FOREIGN KEY (tipo_credenziali_firma) REFERENCES cosmo_d_tipo_credenziali_firma(codice),
	CONSTRAINT "fk_cosmo_d_tipo_pratica_cosmo_d_tipo_otp" FOREIGN KEY (tipo_otp) REFERENCES cosmo_d_tipo_otp(codice),
);

CREATE TABLE cosmo_d_tipo_documento (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	codice_stardas varchar(100) NULL,
	firmabile bool NOT NULL DEFAULT false,
	dimensione_massima int8 NULL,
	CONSTRAINT "PK_cosmo_d_tipo_documento" PRIMARY KEY (codice)
);

CREATE TABLE cosmo_r_tipodoc_tipopratica (
	codice_tipo_documento varchar(30) NOT NULL,
	codice_tipo_pratica varchar(255) NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT "PK_cosmo_r_tipodoc_tipopratica" PRIMARY KEY (codice_tipo_documento, codice_tipo_pratica),
	CONSTRAINT "FK_cosmo_r_tipodoc_tipopratica_cosmo_d_tipo_pratiche" FOREIGN KEY (codice_tipo_pratica) REFERENCES cosmo_d_tipo_pratica(codice),
	CONSTRAINT "FK_cosmo_r_tipodoc_tipopratica_cosmo_d_tipo_documento" FOREIGN KEY (codice_tipo_documento) REFERENCES cosmo_d_tipo_documento(codice)
);

CREATE TABLE cosmo_d_tipo_firma (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
	estraibile bool NOT NULL DEFAULT false,
	mime_type varchar(255) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_tipo_firma PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_stato_documento (
	codice varchar(30) NOT NULL,
	descrizione varchar(200) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT PK_cosmo_d_stato_documento PRIMARY KEY (codice)
);

CREATE TABLE cosmo_t_fruitore (
	id bigserial NOT NULL,
	nome_app varchar(50) NULL,
	url varchar(4000) NULL,
	api_manager_id varchar(50) NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_inserimento timestamp NOT NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_ultima_modifica timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	CONSTRAINT "PK_cosmo_c_source" PRIMARY KEY (id),
	CONSTRAINT uq_cosmo_t_fruitore_api_manager_id UNIQUE (api_manager_id)
);
CREATE SEQUENCE COSMO_T_FRUITORE_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_d_autorizzazione_fruitore (
	codice varchar(10) NOT NULL,
	descrizione varchar(100) NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT "PK_cosmo_d_autorizzazione_fruitore" PRIMARY KEY (codice)
);

CREATE TABLE cosmo_r_fruitore_ente (
	id_fruitore int8 NOT NULL,
	id_ente int8 NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_r_fruitore_ente PRIMARY KEY (id_ente, id_fruitore),
	CONSTRAINT "FK_cosmo_r_fruitore_ente_cosmo_t_ente" FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id),
	CONSTRAINT "FK_cosmo_r_fruitore_ente_cosmo_t_fruitore" FOREIGN KEY (id_fruitore) REFERENCES cosmo_t_fruitore(id)
);

CREATE TABLE cosmo_r_fruitore_autorizzazione (
	codice varchar(10) NOT NULL,
	id_fruitore int8 NOT NULL,
	CONSTRAINT "PK_cosmo_r_fruitore_autorizzazione" PRIMARY KEY (codice, id_fruitore),
	CONSTRAINT "FK_cosmo_r_fruitore_autorizzazione_cosmo_d_autorizzazione_fruit" FOREIGN KEY (codice) REFERENCES cosmo_d_autorizzazione_fruitore(codice),
	CONSTRAINT "FK_cosmo_r_fruitore_autorizzazione_cosmo_t_fruitore" FOREIGN KEY (id_fruitore) REFERENCES cosmo_t_fruitore(id)
);

CREATE TABLE cosmo_t_pratica (
	id bigserial NOT NULL,
	id_ente int8 NOT NULL,
	dt_inserimento timestamp NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_inserimento varchar(50) NOT NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	oggetto varchar(255) NOT NULL,
	uuid_nodo varchar(50) NULL,
	id_fruitore int8 NULL,
	codice_tipo_pratica varchar(255) NULL,
	stato varchar(255) NULL,
	riassunto text NULL,
	riassunto_testuale text NULL,
	id_pratica_ext varchar(255) NULL,
	link_pratica varchar(1000) NULL,
	link_pratica_esterna varchar(1000) NULL,
	utente_creazione_pratica varchar(50) NULL,
	data_creazione_pratica timestamp NULL,
	data_fine_pratica timestamp NULL,
	data_cambio_stato timestamp NULL,
	metadati text NULL,
	esterna bool NOT NULL default false,
	CONSTRAINT "PK_cosmo_t_pratica" PRIMARY KEY (id),
	CONSTRAINT "Unique_id_fruitore_id_pratica_esterna" UNIQUE (id_fruitore, id_pratica_ext),
	CONSTRAINT "FK_cosmo_t_pratica_cosmo_t_fruitore" FOREIGN KEY (id_fruitore) REFERENCES cosmo_t_fruitore(id),
	CONSTRAINT fk_cosmo_t_pratica_cosmo_d_tipo_pratica FOREIGN KEY (codice_tipo_pratica) REFERENCES cosmo_d_tipo_pratica(codice),
	CONSTRAINT fk_cosmo_t_pratica_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id)
);
CREATE SEQUENCE COSMO_T_PRATICA_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_documento (
	id bigserial NOT NULL,
	uuid_doc varchar(255) NULL,
	id_pratica int8 NOT NULL,
	id_documento_ext varchar(255) NULL,
	utente_inserimento varchar(50) NOT NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_inserimento timestamp NOT NULL,
	dt_ultima_modifica timestamp NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	parent_id int8 NULL,
	titolo varchar(255) NULL,
	descrizione text NULL,
	codice_tipo_documento varchar(30) NULL,
	nome_file varchar(255) NULL,
	id_path varchar(10) NULL,
	id_doc_parent_ext varchar(50) NULL,
	dimensione int8 NULL,
	autore varchar(100) NULL,
	codice_tipo_firma varchar(30) NULL,
	codice_stato_documento varchar(30) NULL,
	uuid_doc_estratto varchar(255) NULL,
	mime_type varchar(50) NULL,
	descrizione_formato_file varchar(255) NULL,
	nome_file_estratto varchar(255) NULL,
	mime_type_estratto varchar(50) NULL,
	descrizione_formato_file_estratto varchar(255) NULL,
	numero_tentativi_acquisizione int4 NULL,
	CONSTRAINT "PK_cosmo_t_ecm" PRIMARY KEY (id),
	CONSTRAINT "FK_cosmo_t_documento_cosmo_t_documento" FOREIGN KEY (parent_id) REFERENCES cosmo_t_documento(id),
	CONSTRAINT "FK_cosmo_t_documento_cosmo_t_pratiche" FOREIGN KEY (id_pratica) REFERENCES cosmo_t_pratica(id),
	CONSTRAINT fk_cosmo_t_documento_cosmo_d_tipo_documento FOREIGN KEY (codice_tipo_documento) REFERENCES cosmo_d_tipo_documento(codice),
	CONSTRAINT fk_cosmo_t_documento_cosmo_d_tipo_firma FOREIGN KEY (codice_tipo_firma) REFERENCES cosmo_d_tipo_firma(codice),
	CONSTRAINT fk_cosmo_t_documento_cosmo_d_stato_documento FOREIGN KEY (codice_stato_documento) REFERENCES cosmo_d_stato_documento(codice)
);
CREATE SEQUENCE COSMO_T_DOCUMENTO_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_d_tipo_contenuto_documento (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
	dt_inizio_val timestamp NOT NULL default NOW(),
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_tipo_contenuto_documento PRIMARY KEY (codice)
);
CREATE SEQUENCE COSMO_D_TIPO_CONTENUTO_DOCUMENTO_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_d_formato_file (
	codice varchar(255) NOT NULL,
	mime_type varchar(255) NULL,
	descrizione varchar(255),
	icona varchar(100) NULL,
	estensione_default varchar(100),
	upload_consentito boolean NOT NULL default true,
	supporta_anteprima boolean NOT NULL default false,
	supporta_sbustamento boolean NOT NULL default false,
	dt_inizio_val timestamp NOT NULL default NOW(),
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_formato_file PRIMARY KEY (codice)
);
CREATE SEQUENCE COSMO_D_FORMATO_FILE_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_d_tipo_contenuto_firmato (
	codice varchar(100) NOT NULL,
	descrizione varchar(255) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_tipo_contenuto_firmato PRIMARY KEY (codice)
);

CREATE TABLE cosmo_t_contenuto_documento (
	id bigserial NOT NULL,
	id_documento int8 not NULL,
	id_contenuto_sorgente int8 NULL,

	codice_tipo_contenuto varchar(30) not NULL,
	codice_formato_file varchar(255) not NULL,
	codice_tipo_firma varchar(30) NULL,
	nome_file varchar(255) not NULL,
	dimensione int8 null,
	uuid_nodo varchar(255) NULL,
	url_download varchar(2000) NULL,
	
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	dt_verifica_firma timestamp NULL,
	codice_esito_verifica_firma varchar(30) NULL,
	sha_file varchar(1000) NULL,
	tipo_contenuto_firmato varchar(100) NULL,

	CONSTRAINT "PK_cosmo_t_contenuto_documento" PRIMARY KEY (id),
	CONSTRAINT "FK_cosmo_t_contenuto_documento_cosmo_t_documento" FOREIGN KEY (id_documento) REFERENCES cosmo_t_documento(id),
	CONSTRAINT "FK_cosmo_t_contenuto_documento_cosmo_d_tipo_contenuto_documento" FOREIGN KEY (codice_tipo_contenuto) REFERENCES cosmo_d_tipo_contenuto_documento(codice),
	CONSTRAINT "FK_cosmo_t_contenuto_documento_cosmo_d_formato_file" FOREIGN KEY (codice_formato_file) REFERENCES cosmo_d_formato_file(codice),
	CONSTRAINT "FK_cosmo_t_contenuto_documento_cosmo_d_tipo_firma" FOREIGN KEY (codice_tipo_firma) REFERENCES cosmo_d_tipo_firma(codice),
	CONSTRAINT "FK_cosmo_t_contenuto_documento_cosmo_t_contenuto_documento" FOREIGN KEY (id_contenuto_sorgente) REFERENCES cosmo_t_contenuto_documento(id),
	CONSTRAINT "FK_cosmo_t_contenuto_documento_cosmo_d_tipo_contenuto_firmato" FOREIGN KEY (tipo_contenuto_firmato) REFERENCES cosmo_d_tipo_contenuto_firmato(codice)
);
CREATE SEQUENCE COSMO_T_CONTENUTO_DOCUMENTO_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_anteprima_contenuto_documento (
	id bigserial NOT NULL,
	id_contenuto int8 NULL,
	codice_formato_file varchar(255) NOT NULL,
	nome_file varchar(255) NOT NULL,
	dimensione int8 NULL,
	uuid_nodo varchar(255) NULL,
	share_url varchar(2000) NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT "PK_cosmo_t_anteprima_contenuto_documento" PRIMARY KEY (id),
	CONSTRAINT "FK_cosmo_t_anteprima_contenuto_documento_cosmo_d_formato_file" FOREIGN KEY (codice_formato_file) REFERENCES cosmo_d_formato_file(codice),
	CONSTRAINT "FK_cosmo_t_anteprima_contenuto_documento_cosmo_t_contenuto_docu" FOREIGN KEY (id_contenuto) REFERENCES cosmo_t_contenuto_documento(id)
);

CREATE SEQUENCE COSMO_T_ANTEPRIMA_CONTENUTO_DOCUMENTO_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_d_esito_verifica_firma (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_esito_verifica_firma PRIMARY KEY (codice)
);

CREATE TABLE cosmo_t_certificato_firma (
	id bigserial NOT NULL,
	descrizione varchar(100) NOT NULL,
	username varchar(100) NOT NULL,
	pin varchar(100) NOT NULL,
	id_utente int8 NOT NULL,
	dt_scadenza varchar(100) NULL,
	ente_certificatore varchar(30) NOT NULL,
	profilo_feq varchar(30) NOT NULL,
	scelta_temporale varchar(30) NOT NULL,
	tipo_credenziali_firma varchar(30) NOT NULL,
	tipo_otp varchar(30) NOT NULL,
	dt_inserimento timestamp NOT NULL DEFAULT now(),
	utente_inserimento varchar(50) NOT NULL DEFAULT 'SYSTEM'::character varying,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	password varchar(100) NULL,
	ultimo_utilizzato boolean NOT NULL DEFAULT false,
	CONSTRAINT pk_cosmo_t_certificato_firma PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_certificato_firma_cosmo_d_ente_certificatore FOREIGN KEY (ente_certificatore) REFERENCES cosmo_d_ente_certificatore(codice),
	CONSTRAINT fk_cosmo_t_certificato_firma_cosmo_d_profilo_feq FOREIGN KEY (profilo_feq) REFERENCES cosmo_d_profilo_feq(codice),
	CONSTRAINT fk_cosmo_t_certificato_firma_cosmo_d_scelta_marca_temporale FOREIGN KEY (scelta_temporale) REFERENCES cosmo_d_scelta_marca_temporale(codice),
	CONSTRAINT fk_cosmo_t_certificato_firma_cosmo_d_tipo_credenziali_firma FOREIGN KEY (tipo_credenziali_firma) REFERENCES cosmo_d_tipo_credenziali_firma(codice),
	CONSTRAINT fk_cosmo_t_certificato_firma_cosmo_d_tipo_otp FOREIGN KEY (tipo_otp) REFERENCES cosmo_d_tipo_otp(codice),
	CONSTRAINT fk_cosmo_t_certificato_firma_cosmo_t_utente FOREIGN KEY (id_utente) REFERENCES cosmo_t_utente(id)
);
CREATE SEQUENCE COSMO_T_CERTIFICATO_FIRMA_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_credenziali_firma (
	id bigserial NOT NULL,
	descrizione varchar(100) NOT NULL,
	username varchar(100) NOT NULL,
	password varchar(100) NOT NULL,
	id_utente int8 NOT NULL,
	dt_scadenza varchar(100) NULL,
	ente_certificatore varchar(30) NOT NULL,
	profilo_feq varchar(30) NOT NULL,
	scelta_temporale varchar(30) NOT NULL,
	tipo_credenziali_firma varchar(30) NOT NULL,
	tipo_otp varchar(30) NOT NULL,
	dt_inserimento timestamp NOT NULL DEFAULT now(),
	utente_inserimento varchar(50) NOT NULL DEFAULT 'SYSTEM'::character varying,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT pk_cosmo_t_credenziali_firma PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_credenziali_firma_cosmo_d_ente_certificatore FOREIGN KEY (ente_certificatore) REFERENCES cosmo_d_ente_certificatore(codice),
	CONSTRAINT fk_cosmo_t_credenziali_firma_cosmo_d_profilo_feq FOREIGN KEY (profilo_feq) REFERENCES cosmo_d_profilo_feq(codice),
	CONSTRAINT fk_cosmo_t_credenziali_firma_cosmo_d_scelta_marca_temporale FOREIGN KEY (scelta_temporale) REFERENCES cosmo_d_scelta_marca_temporale(codice),
	CONSTRAINT fk_cosmo_t_credenziali_firma_cosmo_d_tipo_credenziali_firma FOREIGN KEY (tipo_credenziali_firma) REFERENCES cosmo_d_tipo_credenziali_firma(codice),
	CONSTRAINT fk_cosmo_t_credenziali_firma_cosmo_d_tipo_otp FOREIGN KEY (tipo_otp) REFERENCES cosmo_d_tipo_otp(codice),
	CONSTRAINT fk_cosmo_t_credenziali_firma_cosmo_t_utente FOREIGN KEY (id_utente) REFERENCES cosmo_t_utente(id)
);

CREATE SEQUENCE COSMO_T_CREDENZIALI_FIRMA_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_r_ente_certificatore_ente (
	codice_ente_certificatore varchar(30) NOT NULL,
	id_ente int8 NOT NULL,
	anno int8 NOT NULL,
	numero_marche_temporali int8 NOT NULL DEFAULT 0,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT PK_cosmo_r_ente_certificatore_ente PRIMARY KEY (codice_ente_certificatore, id_ente, anno),
	CONSTRAINT FK_cosmo_r_ente_certificatore_ente_cosmo_d_ente_certificatore FOREIGN KEY (codice_ente_certificatore) REFERENCES cosmo_d_ente_certificatore(codice),
	CONSTRAINT FK_cosmo_r_ente_certificatore_ente_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id)
);

CREATE TABLE cosmo_r_formato_file_profilo_feq_tipo_firma (
	codice_formato_file varchar(255) NOT NULL,
	codice_profilo_feq varchar(30) NOT NULL,
	codice_tipo_firma varchar(30) NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_r_formato_file_profilo_feq_tipo_firma PRIMARY KEY (codice_formato_file, codice_profilo_feq, codice_tipo_firma),
	CONSTRAINT fk_cosmo_r_formato_file_profilo_feq_tipo_firma_cosmo_d_formato_ FOREIGN KEY (codice_formato_file) REFERENCES cosmo_d_formato_file(codice),
	CONSTRAINT fk_cosmo_r_formato_file_profilo_feq_tipo_firma_cosmo_d_profilo_ FOREIGN KEY (codice_profilo_feq) REFERENCES cosmo_d_profilo_feq(codice),
	CONSTRAINT fk_cosmo_r_formato_file_profilo_feq_tipo_firma_cosmo_d_tipo_fir FOREIGN KEY (codice_tipo_firma) REFERENCES cosmo_d_tipo_firma(codice)
);

CREATE TABLE cosmo_t_info_verifica_firma (
	id bigserial NOT NULL,
	id_padre int8 NULL,
	id_contenuto_documento int8 NULL,
	codice_esito varchar(30) NOT NULL,
	firmatario varchar(255) NOT NULL,
	cf_firmatario varchar(16) NULL,
	organizzazione varchar(255) NULL,
	dt_apposizione timestamp NULL,
	dt_apposizione_marcatura_temporale timestamp NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	dt_verifica_firma timestamp NULL,
	codice_errore varchar(100) NULL,
	CONSTRAINT "PK_cosmo_t_info_verifica_firma" PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_info_verifica_firma_cosmo_d_esito_verifica_firma FOREIGN KEY (codice_esito) REFERENCES cosmo_d_esito_verifica_firma(codice),
	CONSTRAINT fk_cosmo_t_info_verifica_firma_cosmo_t_contenuto_documento FOREIGN KEY (id_contenuto_documento) REFERENCES cosmo_t_contenuto_documento(id),
	CONSTRAINT fk_cosmo_t_info_verifica_firma_cosmo_t_info_verifica_firma FOREIGN KEY (id_padre) REFERENCES cosmo_t_info_verifica_firma(id)
);
CREATE SEQUENCE COSMO_T_INFO_VERIFICA_FIRMA_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_smistamento (
	id bigserial NOT NULL,
	id_pratica int8 NOT NULL,
	identificativo_evento varchar(200) NULL,
	utente_inserimento varchar(50) NOT NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_inserimento timestamp NOT NULL,
	dt_ultima_modifica timestamp NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	utilizzato bool NOT NULL DEFAULT false,
	CONSTRAINT pk_cosmo_t_smistamento PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_smistamento_cosmo_t_pratica FOREIGN KEY (id_pratica) REFERENCES cosmo_t_pratica(id)
);
CREATE SEQUENCE COSMO_T_SMISTAMENTO_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_r_smistamento_documento (
	id bigserial NOT NULL,
	id_smistamento int8 NOT NULL,
	id_documento int8 NOT NULL,
	codice_stato_smistamento varchar(30) NOT NULL,
	codice_esito_smistamento varchar(3) NULL,
	messaggio_esito_smistamento varchar(400) NULL,
	message_uuid varchar(50) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	numero_retry int4 NULL,
	CONSTRAINT cosmo_r_smistamento_documento_id_smistamento_id_documento_key UNIQUE (id_smistamento, id_documento),
	CONSTRAINT pk_cosmo_r_smistamento_documento PRIMARY KEY (id)
);

CREATE TABLE cosmo_t_info_aggiuntive_smistamento (
	id bigserial NOT NULL,
	id_smistamento_documento int8 NOT NULL,
	cod_informazione varchar(255) NOT NULL,
	valore varchar(1500) NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_inserimento timestamp NOT NULL,
	dt_ultima_modifica timestamp NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT pk_cosmo_t_info_aggiuntive_smistamento PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_info_aggiuntive_smistamento_cosmo_r_smistamento_docu FOREIGN KEY (id_smistamento_documento) REFERENCES cosmo_r_smistamento_documento(id)
);
CREATE SEQUENCE COSMO_T_INFO_AGGIUNTIVE_SMISTAMENTO_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_operazione_asincrona (
	id bigint NOT NULL,
	uuid varchar(50) NOT NULL,
	codice_stato varchar(50) NOT NULL,
	versione int8 NOT NULL,
	data_avvio timestamp NULL,
	data_fine timestamp NULL,
	metadati text NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT PK_cosmo_t_operazione_asincrona PRIMARY KEY (id),
	CONSTRAINT UQ_cosmo_t_operazione_asincrona_uuid UNIQUE (uuid)
);
CREATE SEQUENCE COSMO_T_OPERAZIONE_ASINCRONA_ID_SEQ START WITH 1000 INCREMENT BY 1;




CREATE TABLE cosmo_t_form_logico (
	id bigserial NOT NULL,
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
	wizard bool NOT NULL DEFAULT false,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	eseguibile_massivamente bool NULL,
	id_ente int8 NULL,
	CONSTRAINT pk_cosmo_t_form_logico PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_form_logico_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id)
);

CREATE SEQUENCE COSMO_T_FORM_LOGICO_ID_SEQ START WITH 1000 INCREMENT 1; 


CREATE TABLE cosmo_t_attivita (
	id bigserial NOT NULL,
	id_pratica int8 NULL,
	link_attivita varchar(255) NULL,
	nome varchar(255) NULL,
	descrizione text NULL,
	dt_inserimento timestamp NOT NULL DEFAULT now(),
	utente_inserimento varchar(50) NOT NULL DEFAULT 'SYSTEM'::character varying,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	parent int8 NULL,
	form_key varchar(255) NULL,
	link_attivita_esterna varchar(1000) NULL,
	id_form_logico int8 NULL,
	CONSTRAINT "PK_cosmo_t_attivita" PRIMARY KEY (id),
	CONSTRAINT uq_cosmo_t_attivita_link_attivita UNIQUE (link_attivita),
	CONSTRAINT "FK_cosmo_t_attivita_cosmo_t_pratica" FOREIGN KEY (id_pratica) REFERENCES cosmo_t_pratica(id),
	CONSTRAINT cosmo_t_attivita_fk FOREIGN KEY (id_form_logico) REFERENCES cosmo_t_form_logico(id)
);
CREATE SEQUENCE COSMO_T_ATTIVITA_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_gruppo (
	id bigint NOT NULL,
	id_ente int8 NOT NULL,
	codice varchar(100) NOT NULL,
	nome varchar(100) NOT NULL,
	descrizione varchar(1000) NULL,
	dt_inserimento timestamp NOT NULL DEFAULT now(),
	utente_inserimento varchar(50) NOT NULL DEFAULT 'SYSTEM'::character varying,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT pk_cosmo_t_gruppo PRIMARY KEY (id),
	CONSTRAINT uq_cosmo_t_gruppo_codice UNIQUE (codice),
	CONSTRAINT fk_cosmo_t_gruppo_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id)
);
CREATE SEQUENCE COSMO_T_GRUPPO_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_utente_gruppo
(
   id bigserial NOT NULL,
   id_utente int8 NOT NULL,
   id_gruppo int8 NOT NULL,
   dt_inserimento timestamp NOT NULL,
   utente_inserimento varchar(50) NOT NULL,	
   dt_ultima_modifica timestamp NULL,
   utente_ultima_modifica varchar(50) NULL,
   dt_cancellazione timestamp NULL,
   utente_cancellazione varchar(50) NULL,
   CONSTRAINT pk_cosmo_t_utente_gruppo PRIMARY KEY (id),
   CONSTRAINT fk_cosmo_t_utente_gruppo_cosmo_t_utente FOREIGN KEY (id_utente) REFERENCES cosmo_t_utente(id),
   CONSTRAINT fk_cosmo_t_utente_gruppo_cosmo_t_gruppo FOREIGN KEY (id_gruppo) REFERENCES cosmo_t_gruppo(id)
);

CREATE TABLE cosmo_l_storico_pratica (
	id bigint NOT NULL,
	id_pratica int8 NOT NULL,
	id_attivita int8 NULL,
	codice_tipo_evento varchar(255) NOT NULL,
	descrizione_evento varchar(1000) NULL,
	dt_evento timestamp NOT NULL,
	id_utente int8 NULL,
	id_fruitore int8 NULL,
	id_utente_coinvolto int8 NULL,
	id_gruppo_coinvolto int8 NULL,
	CONSTRAINT "PK_cosmo_l_storico_pratica" PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_l_storico_pratica_cosmo_t_attivita FOREIGN KEY (id_attivita) REFERENCES cosmo_t_attivita(id),
	CONSTRAINT fk_cosmo_l_storico_pratica_cosmo_t_fruitore FOREIGN KEY (id_fruitore) REFERENCES cosmo_t_fruitore(id),
	CONSTRAINT fk_cosmo_l_storico_pratica_cosmo_t_gruppo FOREIGN KEY (id_gruppo_coinvolto) REFERENCES cosmo_t_gruppo(id),
 	CONSTRAINT fk_cosmo_l_storico_pratica_cosmo_t_pratica FOREIGN KEY (id_pratica) REFERENCES cosmo_t_pratica(id),
 	CONSTRAINT fk_cosmo_l_storico_pratica_cosmo_t_utente FOREIGN KEY (id_utente) REFERENCES cosmo_t_utente(id),
 	CONSTRAINT fk_cosmo_l_storico_pratica_cosmo_t_utente_2 FOREIGN KEY (id_utente_coinvolto) REFERENCES cosmo_t_utente(id)
);
CREATE SEQUENCE COSMO_L_STORICO_PRATICA_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_applicazione_esterna (
	id bigserial NOT NULL,
	descrizione varchar(255) NOT NULL,
	icona bytea NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT PK_cosmo_t_applicazione_esterna PRIMARY KEY (id)
);
CREATE SEQUENCE COSMO_T_APPLICAZIONE_ESTERNA_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_r_ente_applicazione_esterna (
	id bigserial NOT NULL,
	id_ente int8 NOT NULL,
	id_applicazione_esterna int8 NOT NULL,
	dt_inizio_val timestamp NOT NULL DEFAULT now(),
	dt_fine_val timestamp NULL,
	CONSTRAINT PK_cosmo_r_ente_applicazione_esterna PRIMARY KEY (id),
	CONSTRAINT UNIQUE_cosmo_r_ente_applicazione_esterna UNIQUE (id_ente, id_applicazione_esterna),
	CONSTRAINT FK_cosmo_r_ente_applicazione_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id),
	CONSTRAINT FK_cosmo_r_ente_applicazione_esterna_cosmo_t_applicazione_esterna FOREIGN KEY (id_applicazione_esterna) REFERENCES cosmo_t_applicazione_esterna(id)
);
CREATE SEQUENCE COSMO_R_ENTE_APPLICAZIONE_ESTERNA_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_funzionalita_applicazione_esterna (
	id bigserial NOT NULL,
	descrizione varchar(255) NOT NULL,
	url varchar(1000) NOT NULL,
	principale boolean NOT null default false,
	id_ente_applicazione_esterna int8 NOT NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT PK_cosmo_t_funzionalita_applicazione_esterna PRIMARY KEY (id),
	CONSTRAINT FK_cosmo_t_funzionalita_applicazione_esterna_cosmo_r_ente_applicazione_esterna FOREIGN KEY (id_ente_applicazione_esterna) REFERENCES cosmo_r_ente_applicazione_esterna(id)
);
CREATE SEQUENCE COSMO_T_FUNZIONALITA_APPLICAZIONE_ESTERNA_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_r_utente_funzionalita_applicazione_esterna (
	id_utente int8 NOT NULL,
	id_funzionalita_applicazione_esterna int8 NOT NULL,
	dt_inizio_val timestamp NOT NULL DEFAULT now(),
	dt_fine_val timestamp NULL,
	posizione int4 NOT NULL DEFAULT 0,
	CONSTRAINT PK_cosmo_r_utente_funzionalita_applicazione_esterna PRIMARY KEY (id_utente, id_funzionalita_applicazione_esterna),
	CONSTRAINT FK_cosmo_r_utente_funzionalita_applicazione_cosmo_t_utente FOREIGN KEY (id_utente) REFERENCES cosmo_t_utente(id),
	CONSTRAINT FK_cosmo_r_utente_funzionalita_applicazione_esterna_cosmo_t_funzionalita_applicazione_esterna FOREIGN KEY (id_funzionalita_applicazione_esterna) REFERENCES cosmo_t_funzionalita_applicazione_esterna(id)
);

CREATE TABLE cosmo_t_notifica (
	id bigserial NOT NULL,
	id_fruitore int8 NULL,
	descrizione text NULL,
	arrivo timestamp NOT NULL,
	scadenza timestamp NULL,
	id_pratica int8 NULL,
	dt_inserimento timestamp NOT NULL DEFAULT now(),
	utente_inserimento varchar (50) NOT NULL DEFAULT 'SYSTEM',
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar (50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar (50) NULL,
	classe varchar (100) NULL,
	tipo_notifica varchar (100) NULL,
	url varchar (1000) NULL,
	url_descrizione text NULL,
	CONSTRAINT pk_cosmo_t_notifica PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_notifica_cosmo_t_pratica FOREIGN KEY (id_pratica) REFERENCES cosmo_t_pratica(id),
 	CONSTRAINT fk_cosmo_t_notifica_cosmo_t_fruitore FOREIGN KEY (id_fruitore) REFERENCES cosmo_t_fruitore(id),
 	CONSTRAINT fk_cosmo_t_notifica_cosmo_d_tipo_notifica FOREIGN KEY (tipo_notifica) REFERENCES cosmo_d_tipo_notifica(codice)
 );
CREATE SEQUENCE COSMO_T_NOTIFICA_ID_SEQ START WITH 1000 INCREMENT 1; 

CREATE TABLE cosmo_r_notifica_utente (
	id_utente int8 NOT NULL,
	id_notifica int8 NOT NULL,
	data_lettura timestamp NULL
);

CREATE TABLE cosmo_r_pratica_utente_gruppo (
	id bigserial NOT NULL,
	id_utente int8 NULL,
	id_pratica int8 NOT NULL,
	id_utente_cond int8 NULL,
	codice_tipo_condivisione varchar(10) NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	id_gruppo int8 NULL,
	CONSTRAINT fk_cosmo_r_pratica_utente_gruppo_cosmo_t_pratica FOREIGN KEY (id_pratica) REFERENCES cosmo_t_pratica(id),	
	CONSTRAINT fk_cosmo_r_pratica_utente_gruppo_cosmo_t_utente FOREIGN KEY (id_utente) REFERENCES cosmo_t_utente(id),	
	CONSTRAINT fk_cosmo_r_pratica_utente_gruppo_cosmo_t_gruppo FOREIGN KEY (id_gruppo) REFERENCES cosmo_t_gruppo(id),	
	CONSTRAINT fk_cosmo_r_pratica_utente_gruppo_cosmo_t_utente_2 FOREIGN KEY (id_utente_cond) REFERENCES cosmo_t_utente(id),	
	CONSTRAINT "CHK_cosmo_r_pratica_utente_gruppo_1" CHECK ( codice_tipo_condivisione IN ('condivisa', 'preferita') ),
	CONSTRAINT "CHK_cosmo_r_pratica_utente_gruppo_2" CHECK (
		(codice_tipo_condivisione = 'condivisa' AND id_utente_cond IS NOT NULL) OR (codice_tipo_condivisione != 'condivisa' AND id_utente_cond IS NULL)
	),
	CONSTRAINT pk_cosmo_r_pratica_utente_gruppo PRIMARY KEY (id),
);
CREATE SEQUENCE COSMO_R_PRATICA_UTENTE_GRUPPO_ID_SEQ START WITH 1000 INCREMENT 1; 


CREATE TABLE cosmo_t_funzionalita (
	id bigserial NOT NULL,
	nome_funzionalita varchar(255) NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
);
CREATE SEQUENCE COSMO_T_FUNZIONALITA_ID_SEQ START WITH 1000 INCREMENT 1; 

CREATE TABLE cosmo_r_funzionalita_form_logico (
	id_form_logico int8 NOT NULL,
	id_funzionalita int8 NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
);

CREATE TABLE cosmo_c_param_funz (
	id bigserial NOT NULL,
	id_funzionalita int8 NULL,
	nome varchar(50) NULL,
	valore text NULL,
);
CREATE SEQUENCE COSMO_C_PARAM_FUNZ_ID_SEQ START WITH 1000 INCREMENT 1; 

CREATE TABLE cosmo_r_attivita_assegnazione (
	id bigserial NOT NULL,
	id_gruppo int4 NULL,
	id_utente int4 NULL,
	id_attivita int4 NOT NULL,
	assegnatario bool NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT cosmo_r_attivita_assegnazione_check CHECK (((id_utente IS NOT NULL) OR (id_gruppo IS NOT NULL))),
	CONSTRAINT cosmo_r_attivita_assegnazione_pk PRIMARY KEY (id)
);
CREATE SEQUENCE COSMO_R_ATTIVITA_ASSEGNAZIONE_ID_SEQ START WITH 1000 INCREMENT 1; 

CREATE TABLE cosmo_r_stato_tipo_pratica (
	id bigserial NOT NULL,
	codice_stato_pratica varchar(30) NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	codice_tipo_pratica VARCHAR(255) NULL,
	CONSTRAINT pk_cosmo_r_stato_tipo_pratica PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_r_stato_tipo_pratica_cosmo_d_tipo_pratica FOREIGN KEY (codice_tipo_pratica) REFERENCES cosmo_d_tipo_pratica(codice),
	CONSTRAINT fk_cosmo_r_stato_tipo_pratica_cosmo_d_stato_pratica FOREIGN KEY (codice_stato_pratica) REFERENCES cosmo_d_stato_pratica(codice),
	CONSTRAINT uq_cosmo_r_stato_tipo_pratica_codice_stato_pratica_id_tipo_prat UNIQUE (codice_stato_pratica, codice_tipo_pratica)
);
CREATE SEQUENCE COSMO_R_STATO_TIPO_PRATICA_ID_SEQ START WITH 1000 INCREMENT 1; 

CREATE TABLE cosmo_t_istanza_funzionalita_form_logico (
	id bigserial NOT NULL,
	codice_funzionalita varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT pk_cosmo_t_istanza_funzionalita_form_logico PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_istanza_funzionalita_form_logico_cosmo_d_funzionalit FOREIGN KEY (codice_funzionalita) REFERENCES cosmo_d_funzionalita_form_logico(codice)
);

CREATE SEQUENCE COSMO_T_ISTANZA_FUNZIONALITA_FORM_LOGICO_ID_SEQ START WITH 1000 INCREMENT 1; 

CREATE TABLE cosmo_r_form_logico_istanza_funzionalita (
	id_form_logico int8 NOT NULL,
	id_istanza_funzionalita int8 NOT NULL,
	ordine int4 NOT NULL DEFAULT 1,
	eseguibile_massivamente boolean NOT NULL default false,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_r_form_logico_istanza PRIMARY KEY (id_form_logico, id_istanza_funzionalita),
	CONSTRAINT "FK_cosmo_r_form_logico_istanza_cosmo_t_form_logico" FOREIGN KEY (id_form_logico) REFERENCES cosmo_t_form_logico(id),
	CONSTRAINT "FK_cosmo_r_form_logico_istanza_cosmo_t_istanza_funzionalita_for" FOREIGN KEY (id_istanza_funzionalita) REFERENCES cosmo_t_istanza_funzionalita_form_logico(id)
);

CREATE TABLE cosmo_r_ente_funzionalita_applicazione_esterna (
	id_ente int8 NOT NULL,
	id_funzionalita_applicazione_esterna int8 NOT NULL,
	dt_inizio_val timestamp NOT NULL DEFAULT now(),
	dt_fine_val timestamp NULL,
	CONSTRAINT PK_cosmo_r_ente_funzionalita_applicazione_esterna PRIMARY KEY (id_ente, id_funzionalita_applicazione_esterna),
	CONSTRAINT FK_cosmo_r_ente_funzionalita_applicazione_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id),
	CONSTRAINT FK_cosmo_r_ente_funzionalita_applicazione_esterna_cosmo_t_funzionalita_applicazione_esterna FOREIGN KEY (id_funzionalita_applicazione_esterna) REFERENCES cosmo_t_funzionalita_applicazione_esterna(id)
);

CREATE TABLE cosmo_t_schema_autenticazione_fruitore (
	id bigserial NOT NULL,
	codice_tipo_schema varchar(100) NOT NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	id_fruitore int8 NOT NULL,
	in_ingresso bool NULL,
	endpoint_token VARCHAR(1000) NULL,
	mappatura_richiesta_token VARCHAR(2000) NULL,
	mappatura_output_token VARCHAR(2000) NULL,		
	metodo_richiesta_token VARCHAR(100) NULL,
	contenttype_richiesta_token VARCHAR(200) NULL,
	nome_header VARCHAR(200) NULL,
	formato_header VARCHAR(200) NULL,
	CONSTRAINT PK_cosmo_t_schema_autenticazione_fruitore PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_schema_autenticazione_fruitore_cosmo_d_tipo_schema_a FOREIGN KEY (codice_tipo_schema) REFERENCES cosmo_d_tipo_schema_autenticazione(codice),
	CONSTRAINT fk_cosmo_t_schema_autenticazione_fruitore_cosmo_t_fruitore FOREIGN KEY (id_fruitore) REFERENCES cosmo_t_fruitore(id)
);
CREATE SEQUENCE COSMO_T_SCHEMA_AUTENTICAZIONE_FRUITORE_ID_SEQ START WITH 1000 INCREMENT 1;

CREATE TABLE cosmo_t_credenziali_autenticazione_fruitore (
	id bigserial NOT NULL,
	id_schema int8 NOT NULL,
	username varchar(1000) NULL,
	password varchar(1000) NULL,
	client_id varchar(1000) NULL,
	client_secret varchar(1000) NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT CHK_cosmo_t_credenziali_autenticazione_fruitore_1 CHECK ((((username IS NOT NULL) AND (password IS NOT NULL) AND ((client_id IS NULL) AND (client_secret IS NULL))) OR ((client_id IS NOT NULL) AND (client_secret IS NOT NULL) AND ((username IS NULL) AND (password IS NULL))))),
	CONSTRAINT PK_cosmo_t_credenziali_autenticazione_fruitore PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_credenziali_autenticazione_fruitore_cosmo_t_schema_a FOREIGN KEY (id_schema) REFERENCES cosmo_t_schema_autenticazione_fruitore(id)
);
CREATE SEQUENCE COSMO_T_CREDENZIALI_AUTENTICAZIONE_FRUITORE_ID_SEQ START WITH 1000 INCREMENT 1;

CREATE TABLE cosmo_t_endpoint_fruitore (
	id bigserial NOT NULL,
	id_fruitore int8 NOT NULL,
	id_schema_autenticazione_fruitore int8 NULL,
	codice_operazione_fruitore varchar(100) NOT NULL,
	codice_tipo_endpoint varchar(100) NOT NULL,
	endpoint varchar(1000) NOT NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	metodo_http varchar(100) NULL,
	codice_descrittivo varchar(100) NULL,
	CONSTRAINT PK_cosmo_t_endpoint_fruitore PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_endpoint_fruitore_cosmo_d_operazione_fruitore FOREIGN KEY (codice_operazione_fruitore) REFERENCES cosmo_d_operazione_fruitore(codice),
	CONSTRAINT fk_cosmo_t_endpoint_fruitore_cosmo_t_fruitore FOREIGN KEY (id_fruitore) REFERENCES cosmo_t_fruitore(id),
	CONSTRAINT fk_cosmo_t_endpoint_fruitore_cosmo_t_schema_autenticazione_frui FOREIGN KEY (id_schema_autenticazione_fruitore) REFERENCES cosmo_t_schema_autenticazione_fruitore(id)
);
CREATE SEQUENCE COSMO_T_ENDPOINT_FRUITORE_ID_SEQ START WITH 1000 INCREMENT 1; 
 
CREATE TABLE cosmo_d_chiave_parametro_funzionalita_form_logico (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
	tipo varchar(100) NOT NULL,
	schema_json text NULL,
	valore_default text NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT PK_cosmo_d_chiave_parametro_funzionalita_form_logico PRIMARY KEY (codice)
);

CREATE TABLE cosmo_r_istanza_form_logico_parametro_valore (
	id_istanza int8 NOT NULL,
	codice_chiave_parametro varchar(30) NOT NULL,
	valore_parametro varchar(5000) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_r_istanza_form_logico_parametro_valore PRIMARY KEY (id_istanza,codice_chiave_parametro),
	CONSTRAINT FK_cosmo_r_istanza_form_logico_parametro_valore_cosmo_d_chiave_ FOREIGN KEY (codice_chiave_parametro) REFERENCES cosmo_d_chiave_parametro_funzionalita_form_logico(codice),
	CONSTRAINT FK_cosmo_r_istanza_form_logico_parametro_valore_cosmo_t_istanza FOREIGN KEY (id_istanza) REFERENCES cosmo_t_istanza_funzionalita_form_logico(id)
);

CREATE TABLE cosmo_r_funzionalita_parametro_form_logico (
	id bigserial NOT NULL,
	codice_funzionalita varchar(30) NOT NULL,
	codice_parametro varchar(30) NOT NULL,
	obbligatorio bool NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT cosmo_r_funzionalita_parametro_form_logico_pk PRIMARY KEY (id),
	CONSTRAINT cosmo_r_funzionalita_parametro_form_logico_codice_funzionalita_fk 
		FOREIGN KEY (codice_funzionalita) REFERENCES cosmo_d_funzionalita_form_logico(codice),
	CONSTRAINT cosmo_r_funzionalita_parametro_form_logico_codice_parametro_fk 
		FOREIGN KEY (codice_parametro) REFERENCES cosmo_d_chiave_parametro_funzionalita_form_logico(codice)
);
CREATE SEQUENCE COSMO_R_FUNZIONALITA_PARAMETRO_FORM_LOGICO_ID_SEQ START WITH 1000 INCREMENT 1; 

CREATE TABLE cosmo_t_lock (
	id bigserial NOT NULL,
	codice_risorsa varchar(255) NOT NULL,
	codice_owner varchar(255) NOT NULL,
	dt_scadenza timestamp NOT NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	codice_risorsa_not_deleted varchar(255) as (case when dt_cancellazione is null then codice_risorsa else null end) unique,
	CONSTRAINT "PK_cosmo_t_lock" PRIMARY KEY (id)
);
CREATE SEQUENCE COSMO_T_LOCK_ID_SEQ START WITH 1000 INCREMENT 1; 

CREATE TABLE cosmo_t_template_report (
	id bigserial NOT NULL,
	id_ente int8 NULL,
	codice_tipo_pratica varchar(255) NULL,
	codice_template_padre varchar(255) NULL,
	codice varchar(255) NOT NULL,
	sorgente_template bytea NOT NULL,
	template_compilato bytea NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT pk_cosmo_t_template_report PRIMARY KEY (id)
);
CREATE SEQUENCE COSMO_T_TEMPLATE_REPORT_ID_SEQ START WITH 1000 INCREMENT 1; 

CREATE TABLE cosmo_t_risorsa_template_report (
	id bigserial NOT NULL,
	id_ente int8 NULL,
	codice_tipo_pratica varchar(255) NULL,
	codice_template varchar(255) NOT NULL,
	codice varchar(255) NOT NULL,
	contenuto_risorsa bytea NULL,
	nodo_risorsa varchar(255) NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT pk_cosmo_t_risorsa_template_report PRIMARY KEY (id)
);
CREATE SEQUENCE COSMO_T_RISORSA_TEMPLATE_REPORT_ID_SEQ START WITH 1000 INCREMENT 1; 

CREATE TABLE cosmo_r_pratica_pratica  (
	id_pratica_da int8 NOT NULL,
	id_pratica_a  int8 NOT NULL,
	codice_tipo_relazione varchar(100) NOT NULL,
	dt_inizio_val timestamp NOT NULL DEFAULT now(),
	dt_fine_val timestamp NULL,
	CONSTRAINT PK_cosmo_r_pratica_pratica PRIMARY KEY (id_pratica_da, id_pratica_a, codice_tipo_relazione),
	CONSTRAINT FK_cosmo_r_pratica_pratica_cosmo_t_pratica_da FOREIGN KEY (id_pratica_da) REFERENCES cosmo_t_pratica(id),
	CONSTRAINT FK_cosmo_r_pratica_pratica_cosmo_t_pratica_a FOREIGN KEY (id_pratica_a) REFERENCES cosmo_t_pratica(id),
	CONSTRAINT FK_cosmo_r_pratica_pratica_cosmo_d_tipo_relazione_pratica FOREIGN KEY (codice_tipo_relazione) REFERENCES cosmo_d_tipo_relazione_pratica(codice),
	CONSTRAINT pratiche_diverse CHECK (id_pratica_da <> id_pratica_a)
);

CREATE TABLE cosmo_d_custom_form_formio (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
	custom_form text NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	codice_tipo_pratica varchar(255) NULL,
	CONSTRAINT pk_cosmo_d_custom_form_formio PRIMARY KEY (codice),
	CONSTRAINT fk_cosmo_d_custom_form_formio_cosmo_d_tipo_pratica FOREIGN KEY (codice_tipo_pratica) REFERENCES cosmo_d_tipo_pratica(codice)
);


CREATE TABLE cosmo_d_trasformazione_dati_pratica (
	id bigserial NOT NULL,
	codice_tipo_pratica varchar(255) NOT NULL,
	codice_fase varchar(255) NOT NULL,
	ordine int8 NULL,
	obbligatoria bool NOT NULL,
	descrizione varchar(500) NULL,
	definizione text NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT fk_cosmo_d_trasformazione_dati_pratica_cosmo_d_tipo_pratica FOREIGN KEY (codice_tipo_pratica) 
		REFERENCES cosmo_d_tipo_pratica(codice),
	CONSTRAINT "CHK_cosmo_d_trasformazione_dati_pratica_1" CHECK ((((codice_fase)::text = 'beforeProcessStart'::text) OR ((codice_fase)::text = 'afterProcessStart'::text))),
	CONSTRAINT "PK_cosmo_d_trasformazione_dati_pratica" PRIMARY KEY (id)
);
CREATE SEQUENCE COSMO_D_TRASFORMAZIONE_DATI_PRATICA_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_d_helper_pagina (
	codice varchar(100) NOT NULL,
	descrizione varchar(255) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_helper_pagina PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_helper_tab (
	codice varchar(100) NOT NULL,
	descrizione varchar(255) NULL,
        codice_pagina varchar(100) NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_helper_tab PRIMARY KEY (codice),
        CONSTRAINT fk_cosmo_d_helper_tab_cosmo_d_helper_pagina FOREIGN KEY (codice_pagina) REFERENCES cosmo_d_helper_pagina(codice)
);

CREATE TABLE cosmo_d_helper_modale (
	codice varchar(100) NOT NULL,
    descrizione varchar(255) NULL,
    codice_pagina varchar(100) NULL,
    codice_tab varchar(100) NULL,
    dt_inizio_val timestamp NOT NULL,
    dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_helper_modale PRIMARY KEY (codice),
	CONSTRAINT fk_cosmo_d_helper_modale_cosmo_d_helper_pagina FOREIGN KEY (codice_pagina) REFERENCES cosmo_d_helper_pagina(codice),
	CONSTRAINT fk_cosmo_d_helper_modale_cosmo_d_helper_tab FOREIGN KEY (codice_tab) REFERENCES cosmo_d_helper_tab(codice)
);

CREATE TABLE cosmo_t_helper (
	id bigserial NOT NULL,
	codice_pagina varchar(100) NOT NULL,
    codice_tab varchar(100) NULL,
    codice_form varchar(100) NULL,
	html text NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,	
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	codice_modale varchar(100) NULL,
	CONSTRAINT pk_cosmo_t_helper PRIMARY KEY (id),
	CONSTRAINT FK_cosmo_t_helper_cosmo_d_helper_pagina FOREIGN KEY (codice_pagina) REFERENCES cosmo_d_helper_pagina(codice),
	CONSTRAINT FK_cosmo_t_helper_cosmo_d_helper_tab FOREIGN KEY (codice_tab) REFERENCES cosmo_d_helper_tab(codice),
	CONSTRAINT FK_cosmo_t_helper_cosmo_d_custom_form_formio FOREIGN KEY (codice_form) REFERENCES cosmo_d_custom_form_formio(codice),
	CONSTRAINT FK_cosmo_t_helper_cosmo_d_helper_modale FOREIGN KEY (codice_modale) REFERENCES cosmo_d_helper_modale(codice)
);
CREATE SEQUENCE COSMO_T_HELPER_ID_SEQ START WITH 10 INCREMENT BY 1;

CREATE TABLE cosmo_c_configurazione_ente (
	chiave varchar(100) NOT NULL,
	valore varchar(255) NOT NULL,
	descrizione varchar(255) NULL,
	id_ente int8 NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_c_configurazione_ente PRIMARY KEY (chiave, id_ente),
	CONSTRAINT fk_cosmo_c_configurazione_ente_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id)
);

CREATE TABLE cosmo_r_gruppo_tipo_pratica (
	id_gruppo int8 NOT NULL,
	codice_tipo_pratica varchar(255) NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	creatore boolean NULL,
	supervisore boolean NULL,
	CONSTRAINT fk_cosmo_r_gruppo_pratica_cosmo_d_tipo_pratica FOREIGN KEY (codice_tipo_pratica) REFERENCES cosmo_d_tipo_pratica(codice),
	CONSTRAINT fk_cosmo_r_gruppo_pratica_cosmo_t_gruppo FOREIGN KEY (id_gruppo) REFERENCES cosmo_t_gruppo(id)
);

CREATE TABLE cosmo_d_tipo_tag (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
	label varchar(100) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_tipo_tag PRIMARY KEY (codice)
);

CREATE TABLE cosmo_t_tag (
	id bigserial NOT NULL,
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
    codice_tipo_tag varchar(30) NOT NULL,
    id_ente int8 NOT NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,	
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT pk_cosmo_t_tag PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_tag_cosmo_d_tipo_tag FOREIGN KEY (codice_tipo_tag) REFERENCES cosmo_d_tipo_tag(codice),
	CONSTRAINT fk_cosmo_t_tag_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id)
);
CREATE SEQUENCE COSMO_T_TAG_ID_SEQ START WITH 10 INCREMENT BY 1;

CREATE TABLE cosmo_r_pratica_tag(
    id_pratica int8 NOT NULL,
    id_tag int8 NOT NULL,
    dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_r_pratica_tag PRIMARY KEY (id_pratica, id_tag),
	CONSTRAINT fk_cosmo_r_pratica_tag_cosmo_t_pratica FOREIGN KEY (id_pratica) REFERENCES cosmo_t_pratica(id),
	CONSTRAINT fk_cosmo_r_pratica_tag_cosmo_t_tag FOREIGN KEY (id_tag) REFERENCES cosmo_t_tag(id)
);

CREATE TABLE cosmo_r_utente_gruppo_tag(
	id_utente_gruppo int8 NOT NULL,
	id_tag int8 NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_r_utente_gruppo_tag PRIMARY KEY (id_utente_gruppo, id_tag),
	CONSTRAINT fk_cosmo_r_utente_gruppo_tag_cosmo_t_utente_gruppo FOREIGN KEY (id_utente_gruppo) REFERENCES cosmo_t_utente_gruppo(id),
	CONSTRAINT fk_cosmo_r_utente_gruppo_tag_cosmo_t_tag FOREIGN KEY (id_tag) REFERENCES cosmo_t_tag(id)
);
CREATE SEQUENCE COSMO_T_UTENTE_GRUPPO_ID_SEQ START WITH 10 INCREMENT BY 1;

CREATE TABLE cosmo_d_tab_dettaglio (
	codice varchar(100) NOT NULL,
	descrizione varchar(225) NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_tab_dettaglio PRIMARY KEY (codice)
);

CREATE TABLE cosmo_r_tab_dettaglio_tipo_pratica (
	codice_tipo_pratica varchar(255) NULL,
	codice_tab_dettaglio varchar(100) NULL,
	ordine int4 NOT NULL DEFAULT 1,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_r_tab_dettaglio_tipo_pratica PRIMARY KEY (codice_tipo_pratica, codice_tab_dettaglio),
	CONSTRAINT fk_cosmo_r_tab_dettaglio_tipo_pratica_cosmo_d_tab_dettaglio FOREIGN KEY (codice_tab_dettaglio) REFERENCES cosmo_d_tab_dettaglio(codice),
	CONSTRAINT fk_cosmo_r_tab_dettaglio_tipo_pratica_cosmo_d_tipo_pratica FOREIGN KEY (codice_tipo_pratica) REFERENCES cosmo_d_tipo_pratica(codice)
);

CREATE TABLE cosmo_r_formato_file_tipo_documento(
	codice_formato_file VARCHAR(255) NOT NULL,
	codice_tipo_documento VARCHAR(100) NOT NULL,
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT PK_cosmo_r_formato_file_tipo_documento PRIMARY KEY (codice_formato_file, codice_tipo_documento),
	CONSTRAINT FK_cosmo_r_formato_file_tipo_documento_cosmo_d_formato_file FOREIGN KEY (codice_formato_file) REFERENCES cosmo_d_formato_file(codice),
	CONSTRAINT FK_cosmo_r_formato_file_tipo_documento_cosmo_d_tipo_documento FOREIGN KEY (codice_tipo_documento) REFERENCES cosmo_d_tipo_documento(codice)
);



CREATE TABLE cosmo_l_esecuzione_batch
(
   id bigint PRIMARY KEY NOT NULL,
   codice_batch varchar(255) NOT NULL,
   dt_evento timestamp NOT NULL,
   dt_avvio timestamp NOT NULL,
   dt_fine timestamp NOT NULL,
   codice_esito varchar(255) NOT NULL
);

CREATE SEQUENCE COSMO_L_ESECUZIONE_BATCH_ID_SEQ START WITH 1000 INCREMENT BY 1;


CREATE TABLE cosmo_l_segnalazione_batch(
   id bigint PRIMARY KEY NOT NULL,
   id_esecuzione bigint NOT NULL,
   dt_evento timestamp NOT NULL,
   livello varchar(255) NOT NULL,
   messaggio text NOT NULL,
   dettagli text,
   CONSTRAINT fk_cosmo_l_segnalazione_batch_cosmo_l_esecuzione_batch FOREIGN KEY (id_esecuzione) REFERENCES cosmo_l_esecuzione_batch(id)
);

CREATE SEQUENCE COSMO_L_SEGNALAZIONE_BATCH_ID_SEQ START WITH 1000 INCREMENT BY 1;


CREATE TABLE cosmo_d_stato_invio_stilo (
	codice varchar(100) NOT NULL,
	descrizione varchar(100) NULL,
	dt_inizio_val timestamp NOT NULL DEFAULT now(),
	dt_fine_val timestamp NULL,
	CONSTRAINT cosmo_d_stato_invio_stilo_pk PRIMARY KEY (codice)
);

CREATE TABLE cosmo_r_invio_stilo_documento (
	id_ud int8 NOT NULL,
	id_documento int8 NOT NULL,
	codice_stato_invio_stilo varchar(100) NOT NULL,
	codice_esito_invio_stilo varchar(3) NULL,
	messaggio_esito_invio_stilo varchar(400) NULL,
	dt_inizio_val timestamp NOT NULL DEFAULT now(),
	dt_fine_val timestamp NULL,
	numero_retry int4 NULL,
	CONSTRAINT cosmo_r_invio_stilo_documento_pk PRIMARY KEY (id_ud, id_documento),
	CONSTRAINT cosmo_stato_invio_stilo_fk FOREIGN KEY (codice_stato_invio_stilo) REFERENCES cosmo_d_stato_invio_stilo(codice),
	CONSTRAINT id_documento_fk FOREIGN KEY (id_documento) REFERENCES cosmo_t_documento(id)
);

CREATE TABLE cosmo_d_tipo_commento (
	codice varchar(10) NOT NULL,
    descrizione varchar(100) NULL,
    dt_inizio_val timestamp NOT NULL,
    dt_fine_val timestamp NULL,
	CONSTRAINT PK_cosmo_d_tipo_commento PRIMARY KEY (codice)
);

CREATE TABLE cosmo_t_commento (
    id bigserial NOT NULL,
    codice_tipo varchar(10) NOT NULL,
    data_creazione timestamp NOT NULL,
    utente_creatore varchar(50)NOT NULL,
    id_pratica int8,
    id_attivita int8,
    messaggio text NULL,
    dt_inserimento timestamp NOT NULL,
    dt_ultima_modifica timestamp NULL,
    utente_inserimento varchar(50) NOT NULL,
    utente_ultima_modifica varchar(50) NULL,
    dt_cancellazione timestamp NULL,
    utente_cancellazione varchar(50) NULL,

	CONSTRAINT PK_cosmo_t_commento PRIMARY KEY (id),
	CONSTRAINT FK_cosmo_t_commento_cosmo_d_tipo_commento FOREIGN KEY (codice_tipo) REFERENCES cosmo_d_tipo_commento(codice),
	CONSTRAINT FK_cosmo_t_commento_cosmo_t_pratica FOREIGN KEY (id_pratica) REFERENCES cosmo_t_pratica(id),
	CONSTRAINT FK_cosmo_t_commento_cosmo_t_attivita FOREIGN KEY (id_attivita) REFERENCES cosmo_t_attivita(id)
);

CREATE SEQUENCE COSMO_T_COMMENTO_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_d_filtro_campo (
   codice varchar(25) NOT NULL,
   descrizione varchar(100),
   dt_inizio_val timestamp NOT NULL,
   dt_fine_val timestamp,
   CONSTRAINT "PK_cosmo_d_filtro_campo" PRIMARY KEY (codice)
);

CREATE TABLE cosmo_d_formato_campo (
   codice varchar(25) NOT NULL,
   descrizione varchar(100),
   dt_inizio_val timestamp NOT NULL,
   dt_fine_val timestamp,
   CONSTRAINT "PK_cosmo_d_formato_campo" PRIMARY KEY (codice)
);


CREATE TABLE cosmo_t_variabile_processo (
   id bigserial PRIMARY KEY NOT NULL,
   nome_variabile varchar(100) NOT NULL,
   nome_variabile_flowable varchar(100) NOT NULL,
   codice_formato_campo varchar(25) NOT NULL,
   codice_filtro_campo varchar(25) NOT NULL,
   visualizzare_in_tabella bool,
   codice_tipo_pratica varchar(255) NOT NULL,
   dt_inserimento timestamp NOT NULL,
   dt_ultima_modifica timestamp,
   utente_inserimento varchar(50) NOT NULL,
   utente_ultima_modifica varchar(50),
   dt_cancellazione timestamp,
   utente_cancellazione varchar(50),

	CONSTRAINT FK_cosmo_t_variabile_processo_cosmo_d_formato_campo FOREIGN KEY (codice_formato_campo) REFERENCES cosmo_d_formato_campo(codice),
	CONSTRAINT FK_cosmo_t_variabile_processo_cosmo_d_filtro_campo FOREIGN KEY (codice_filtro_campo) REFERENCES cosmo_d_filtro_campo(codice),
	CONSTRAINT FK_cosmo_t_variabile_processo_cosmo_d_tipo_pratica FOREIGN KEY (codice_tipo_pratica) REFERENCES cosmo_d_tipo_pratica(codice),
	CONSTRAINT uq_cosmo_t_variabile_processo UNIQUE (nome_variabile, codice_tipo_pratica, dt_cancellazione),
	CONSTRAINT uq_flowable_cosmo_t_variabile_processo UNIQUE (nome_variabile_flowable, codice_tipo_pratica, dt_cancellazione)
);

CREATE SEQUENCE COSMO_T_VARIABILE_PROCESSO_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_variabile
(
   id bigserial PRIMARY KEY NOT NULL,
   type_name varchar(255) not null,
   nome varchar(255) not null,
   id_pratica int8 NOT NULL,
   double_value float8 NULL,
   long_value int8 NULL,
   text_value varchar(4000) NULL,
   bytearray_value bytea null,
   dt_inserimento timestamp NOT NULL,
   dt_ultima_modifica timestamp,
   utente_inserimento varchar(50) NOT NULL,
   utente_ultima_modifica varchar(50),
   dt_cancellazione timestamp,
   utente_cancellazione varchar(50),
   json_value text NULL,

CONSTRAINT FK_cosmo_t_variabile_cosmo_t_pratica FOREIGN KEY (id_pratica) REFERENCES cosmo_t_pratica(id),
CONSTRAINT uq_cosmo_t_variabile UNIQUE (nome, id_pratica, dt_cancellazione)

)
;
CREATE SEQUENCE COSMO_T_VARIABILE_ID_SEQ START WITH 1000 INCREMENT BY 1;


CREATE TABLE cosmo_d_stato_caricamento_pratica (
	codice varchar(100) NOT NULL,
	descrizione varchar(100) NULL,
	dt_inizio_val timestamp NOT NULL DEFAULT now(),
	dt_fine_val timestamp NULL,
	CONSTRAINT "cosmo_d_stato_caricamento_pratica_pk" PRIMARY KEY (codice)
);

CREATE TABLE cosmo_t_caricamento_pratica (
	id bigserial NOT NULL,
	nome_file varchar(255) NOT NULL,
	path_file varchar(255) NULL,
	identificativo_pratica varchar(255) NULL,
	codice_stato_caricamento_pratica varchar(100) NOT NULL,
	id_pratica int8 NULL,
	descrizione_evento varchar(255) NULL,
	errore varchar(255) NULL,
	id_ente int8 NOT NULL,
	id_utente int8 NOT NULL,
	id_parent int8 NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT pk_cosmo_t_caricamento_pratica PRIMARY KEY (id),
	CONSTRAINT "FK_cosmo_t_caricamento_pratica_cosmo_t_caricamento_pratica" FOREIGN KEY (id_parent) REFERENCES cosmo_t_caricamento_pratica(id),
	CONSTRAINT fk_cosmo_t_caricamento_pratica_cosmo_d_stato_caricamento_pratic FOREIGN KEY (codice_stato_caricamento_pratica) REFERENCES cosmo_d_stato_caricamento_pratica(codice),
	CONSTRAINT fk_cosmo_t_caricamento_pratica_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id),
	CONSTRAINT fk_cosmo_t_caricamento_pratica_cosmo_t_pratica FOREIGN KEY (id_pratica) REFERENCES cosmo_t_pratica(id),
	CONSTRAINT fk_cosmo_t_caricamento_pratica_cosmo_t_utente FOREIGN KEY (id_utente) REFERENCES cosmo_t_utente(id)
);

CREATE SEQUENCE COSMO_T_CARICAMENTO_PRATICA_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_r_tipo_documento_tipo_documento (
	codice_padre varchar(100) NOT NULL,
	codice_allegato varchar(100) NOT NULL,
	codice_tipo_pratica varchar(100) NOT NULL,
	codice_stardas_allegato varchar(100) NULL,
	dt_inizio_val timestamp NOT NULL DEFAULT now(),
	dt_fine_val timestamp NULL,
	CONSTRAINT cosmo_r_tipo_documento_tipo_documento_pk PRIMARY KEY (codice_padre, codice_allegato, codice_tipo_pratica),
	CONSTRAINT FK_cosmo_r_tipo_documento_tipo_documento_cosmo_d_tipo_doc_padre FOREIGN KEY (codice_padre) REFERENCES cosmo_d_tipo_documento(codice),
	CONSTRAINT FK_cosmo_r_tipo_documento_tipo_documento_cosmo_d_tipo_doc_alleg FOREIGN KEY (codice_allegato) REFERENCES cosmo_d_tipo_documento(codice),
	CONSTRAINT FK_cosmo_r_tipo_documento_tipo_documento_cosmo_d_tipo_pratica FOREIGN KEY (codice_tipo_pratica) REFERENCES cosmo_d_tipo_pratica(codice)
);

CREATE TABLE cosmo_t_otp (
	id bigserial NOT NULL,
	valore varchar(255) NOT NULL,
	id_utente int8 NOT NULL,
	dt_scadenza timestamp NOT NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	id_ente int8 NOT NULL,
	utilizzato bool NOT NULL DEFAULT false,
	CONSTRAINT pk_cosmo_t_otp PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_otp_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id),
	CONSTRAINT fk_cosmo_t_otp_cosmo_t_utente FOREIGN KEY (id_utente) REFERENCES cosmo_t_utente(id),
	CONSTRAINT uq_cosmo_t_otp_cosmo_t_utente_cosmo_t_ente UNIQUE (id_utente, id_ente, dt_cancellazione)
);

CREATE SEQUENCE COSMO_T_OTP_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_template_fea (
	id bigserial NOT NULL,
	descrizione varchar(255) NOT NULL,
	id_ente int8 NOT NULL,
	tipologia_pratica varchar(255) NOT NULL,
	tipologia_documento varchar(100) NOT NULL,
	coordinata_x float8 NOT NULL,
	coordinata_y float8 NOT NULL,
	pagina int8 NOT NULL,
	caricato_da_template boolean NOT NULL DEFAULT true,
	id_pratica int8 NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT pk_cosmo_t_template_fea PRIMARY KEY (id),
	CONSTRAINT cosmo_t_template_fea_fk FOREIGN KEY (id_pratica) REFERENCES cosmo_t_pratica(id),
	CONSTRAINT FK_cosmo_t_template_fea_cosmo_d_tipo_pratica FOREIGN KEY (tipologia_pratica) REFERENCES cosmo_d_tipo_pratica(codice),
    CONSTRAINT FK_cosmo_t_template_fea_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id),
	CONSTRAINT FK_cosmo_t_template_fea_cosmo_d_tipo_documento FOREIGN KEY (tipologia_documento) REFERENCES cosmo_d_tipo_documento(codice),
	CONSTRAINT UQ_cosmo_t_template_fea_tipo_pratica_tipo_doc UNIQUE (tipologia_pratica, tipologia_documento, id_pratica, dt_cancellazione)
);

CREATE SEQUENCE COSMO_T_TEMPLATE_FEA_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_approvazione (
	id bigserial NOT NULL,
	id_utente int8 NOT NULL,
	id_attivita int8 NOT NULL,
	id_documento int8 NOT NULL,
	dt_approvazione timestamp NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT pk_cosmo_t_approvazione PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_t_approvazione_cosmo_t_utente FOREIGN KEY (id_utente) REFERENCES cosmo_t_utente(id),
	CONSTRAINT fk_cosmo_t_approvazione_cosmo_t_attivita FOREIGN KEY (id_attivita) REFERENCES cosmo_t_attivita(id),
	CONSTRAINT fk_cosmo_t_approvazione_cosmo_t_documento FOREIGN KEY (id_documento) REFERENCES cosmo_t_documento(id),
	UNIQUE(id_utente, id_attivita, id_documento)
);
	
CREATE SEQUENCE COSMO_T_APPROVAZIONE_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_t_credenziali_sigillo_elettronico (
	id bigserial NOT NULL,
	delegated_domain varchar(100) NOT NULL,
	delegated_user varchar(100) NOT NULL,
	delegated_password varchar(100) NOT NULL,
	otp_pwd varchar(100) NOT NULL,
	tipo_hsm varchar(100) NOT NULL,
	tipo_otp_auth varchar(100) NOT NULL,
	utente varchar(100) NOT NULL,
	alias varchar(100) NOT NULL,
	dt_inserimento timestamp NOT NULL,
	utente_inserimento varchar(50) NOT NULL,
	dt_ultima_modifica timestamp NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	CONSTRAINT pk_cosmo_t_credenziali_sigillo_elettronico PRIMARY KEY (id),
	CONSTRAINT uq_cosmo_t_credenziali_sigillo_elettronico_alias UNIQUE (alias, dt_cancellazione)
);

CREATE SEQUENCE COSMO_T_CREDENZIALI_SIGILLO_ELETTRONICO_ID_SEQ START WITH 1000 INCREMENT BY 1;


CREATE TABLE cosmo_t_sigillo_elettronico (
	id bigserial NOT NULL,
	id_pratica int8 NOT null,
	identificativo_evento varchar(200),
	identificativo_alias varchar(100),
	utente_inserimento varchar(50) NOT NULL,
	utente_ultima_modifica varchar(50) NULL,
	dt_inserimento timestamp NOT NULL,
	dt_ultima_modifica timestamp NULL,
	dt_cancellazione timestamp NULL,
	utente_cancellazione varchar(50) NULL,
	utilizzato bool NOT NULL DEFAULT false,
	CONSTRAINT pk_cosmo_t_sigillo_elettronico PRIMARY KEY (id),
	CONSTRAINT fK_cosmo_t_sigillo_elettronico_cosmo_t_pratica FOREIGN KEY (id_pratica) REFERENCES cosmo_t_pratica(id),
	CONSTRAINT UQ_cosmo_t_sigillo_id_pratica_identificativo_evento UNIQUE (id_pratica, identificativo_evento, utilizzato)
);

CREATE SEQUENCE COSMO_T_SIGILLO_ELETTRONICO_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_d_stato_sigillo_elettronico (
	codice varchar(30) NOT NULL,
	descrizione varchar(100) NULL,
	dt_inizio_val timestamp NOT NULL DEFAULT now(),
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_stato_sigillo_elettronico PRIMARY KEY (codice)
);
CREATE SEQUENCE COSMO_D_STATO_SIGILLO_ELETTRONICO_ID_SEQ START WITH 1000 INCREMENT BY 1;


CREATE TABLE cosmo_r_sigillo_documento (
	id bigserial NOT NULL,
	id_sigillo int8 NOT NULL,
	id_documento int8 NOT NULL,
	codice_stato_sigillo varchar(100) NOT NULL,
	codice_esito_sigillo varchar(3),
	messaggio_esito_sigillo varchar(400),
	dt_inizio_val timestamp NOT NULL,
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_r_sigillo_documento PRIMARY KEY (id),
	CONSTRAINT fk_cosmo_r_sigillo_documento_cosmo_t_sigillo_elettronico FOREIGN KEY (id_sigillo) REFERENCES cosmo_t_sigillo_elettronico(id),
	CONSTRAINT fk_cosmo_r_sigillo_documento_cosmo_t_documento FOREIGN KEY (id_documento) REFERENCES cosmo_t_documento(id),
	CONSTRAINT fk_cosmo_r_sigillo_documento_cosmo_d_stato_sigillo_elettronico FOREIGN KEY (codice_stato_sigillo) REFERENCES cosmo_d_stato_sigillo_elettronico(codice),
	CONSTRAINT uq_cosmo_r_sigillo_documento_id_sigillo_id_documento UNIQUE (id_sigillo, id_documento)
);

CREATE SEQUENCE COSMO_R_SIGILLO_DOCUMENTO_ID_SEQ START WITH 1000 INCREMENT BY 1;

CREATE TABLE cosmo_c_configurazione_metadati (
	chiave varchar(100) NOT NULL,
	valore varchar(255) NOT NULL,
	descrizione varchar(255) NULL,
	dt_inizio_val timestamp NOT NULL DEFAULT now(),
	dt_fine_val timestamp NULL,
	codice_tipo_pratica varchar(255) NOT NULL,
	CONSTRAINT pk_cosmo_c_configurazione_metadati PRIMARY KEY (chiave, codice_tipo_pratica),
	CONSTRAINT fk_cosmo_c_configurazione_metadati_cosmo_d_tipo_pratica FOREIGN KEY (codice_tipo_pratica) REFERENCES cosmo_d_tipo_pratica(codice)
);

CREATE TABLE cosmo_d_tipo_segnalibro (
	codice varchar(100) NOT NULL,
	descrizione varchar(255) NOT NULL,
	dt_inizio_val timestamp NOT NULL DEFAULT now(),
	dt_fine_val timestamp NULL,
	CONSTRAINT pk_cosmo_d_tipo_segnalibro PRIMARY KEY (codice)
);

CREATE TABLE cosmo_r_notifica_utente_ente (
	id_utente int8 NOT NULL,
	id_notifica int8 NOT NULL,
	data_lettura timestamp NULL,
	dt_inizio_val timestamp NOT NULL DEFAULT now(),
	dt_fine_val timestamp NULL,
	id_ente int8 NOT NULL,
	invio_mail bool NULL,
	stato_invio_mail varchar (100) NULL,
	CONSTRAINT pk_cosmo_r_notifica_utente_ente PRIMARY KEY (id_utente, id_notifica, id_ente),
	CONSTRAINT fk_cosmo_r_notifica_utente_ente_cosmo_t_notifica FOREIGN KEY (id_notifica) REFERENCES cosmo_t_notifica(id),
	CONSTRAINT fk_cosmo_r_notifica_utente_ente_cosmo_t_ente FOREIGN KEY (id_ente) REFERENCES cosmo_t_ente(id),
	CONSTRAINT fk_cosmo_r_notifica_utente_ente_cosmo_t_utente FOREIGN KEY (id_utente) REFERENCES cosmo_t_utente(id)
);