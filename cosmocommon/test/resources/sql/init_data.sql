
INSERT INTO cosmo_c_configurazione (chiave, valore, descrizione, dt_inizio_val) VALUES
	('max.page.size', '70', 'hard limit per numero massimo elementi per pagina', CURRENT_TIMESTAMP()),
	('user.pref.version', '1.0', 'Versione attuale delle preferenze dell''utente', CURRENT_TIMESTAMP()),
	('ente.pref.maxsize', '10000', 'Grandezza massima del file caricato per il logo', CURRENT_TIMESTAMP()),
	('ente.pref.maxpix', '125', 'Numero massimo di pixels del file caricato per il logo', CURRENT_TIMESTAMP()),
	('ente.pref.fea.maxsize', '10000', 'Grandezza massima del file caricato per l''icona della firma fea', CURRENT_TIMESTAMP()),
	('ente.pref.fea.maxpix', '125', 'Numero massimo di pixels del file caricato per l''icona della firma fea', CURRENT_TIMESTAMP());

INSERT INTO cosmo_c_param_funz (id, id_funzionalita, nome, valore) VALUES
	(1, 1, 'param1', '28'),
	(2, 1, 'param2', 'pippo');

INSERT INTO cosmo_d_autorizzazione_fruitore (codice, descrizione, dt_inizio_val, dt_fine_val) VALUES
	('I_PRATICA', 'Possibilita'' di inserire una pratica', CURRENT_TIMESTAMP(), null),
	('G_NOTIFICA', 'Possibilita'' di ricevere una notifica', CURRENT_TIMESTAMP(), null),
	('I_NOTIFICA', 'Possibilita'' di inviare una notifica', CURRENT_TIMESTAMP(), null);

INSERT INTO cosmo_d_categoria_use_case (codice,descrizione,dt_inizio_val)
	VALUES ('ADMIN','Amministrazione',CURRENT_TIMESTAMP());

INSERT INTO cosmo_d_formato_file (codice, mime_type, descrizione, dt_inizio_val, dt_fine_val) VALUES 
	('application/xml', 'application/xml', 'File XML(prova)', CURRENT_TIMESTAMP(), NULL),
    ('application/xml1', 'application/xml1', 'File XML', CURRENT_TIMESTAMP(), NULL),
    ('application/xml2', 'application/xml2', 'File XML', CURRENT_TIMESTAMP(), NULL),
    ('application/xml3', 'application/xml3', 'File XML(prova 2)', CURRENT_TIMESTAMP(), NULL),
    ('application/xml4', 'application/xml4', 'File XML', CURRENT_TIMESTAMP(), NULL),
    ('application/xml5', 'application/xml5', 'File XML', CURRENT_TIMESTAMP(), NULL),
    ('application/xml6', 'application/xml6', 'File XML', CURRENT_TIMESTAMP(), NULL),
    ('application/xml7', 'application/xml7', 'File XML', CURRENT_TIMESTAMP(), NULL),
    ('application/xml8', 'application/xml8', 'File XML', CURRENT_TIMESTAMP(), NULL),
    ('application/xml9', 'application/xml9', 'File XML', CURRENT_TIMESTAMP(), NULL),
    ('application/xml10', 'application/xml10', 'File XML', CURRENT_TIMESTAMP(), NULL),
    ('application/xml11', 'application/xml11', 'File XML', CURRENT_TIMESTAMP(), NULL),
    ('application/rtf', 'application/rtf', 'Documento di testo (RTF)', CURRENT_TIMESTAMP(), NULL);
   

INSERT INTO cosmo_d_tipo_firma(codice, descrizione, dt_inizio_val) VALUES
	('XML','XML Firmato', CURRENT_TIMESTAMP());
	
INSERT INTO cosmo_d_tipo_documento(codice, descrizione, dt_inizio_val) VALUES
	('codice 1', 'esempio tipo documento 1', CURRENT_TIMESTAMP()),
	('codice 2', 'esempio tipo documento 2', CURRENT_TIMESTAMP());
	
INSERT INTO cosmo_d_use_case (codice,descrizione,codice_categoria,dt_inizio_val) VALUES
	('ADMIN PRIN','Amministrazione Principale','ADMIN',CURRENT_TIMESTAMP()),
	('ADMIN SEC','Amministrazione Secondaria','ADMIN',CURRENT_TIMESTAMP()),
	('ADMIN_PRAT','Amministrazione pratiche','ADMIN',CURRENT_TIMESTAMP());
	
INSERT INTO cosmo_t_ente (id, codice_ipa, nome,codice_fiscale,dt_inserimento,utente_inserimento) VALUES 
	(1, 'r_piemon', 'Regione Piemonte', '80087670016', CURRENT_TIMESTAMP(), 'SYSTEM'),
	(2, 'prov_to', 'Provincia di Torino', '80087670000', CURRENT_TIMESTAMP(), 'SYSTEM'),
	(3, 'cmto', 'Torino Citta Metropolitana', '01907990012', CURRENT_TIMESTAMP(), 'SYSTEM');	
	
INSERT INTO cosmo_d_ente_certificatore(codice, descrizione,codice_ca, codice_tsa, dt_inizio_val, provider)
VALUES ('CERT1', 'Ente certificatore di prova 1','1', 'codice_tsa 1', CURRENT_TIMESTAMP(), NULL),
	   ('CERT2', 'Ente certificatore di prova 2','2', 'codice_tsa 2', CURRENT_TIMESTAMP(), NULL),
	   ('CERT3', 'Ente certificatore di prova 3','3', 'codice_tsa 3', CURRENT_TIMESTAMP(), 'Provider3');	

INSERT INTO cosmo_d_tipo_pratica(codice, descrizione, process_definition_key, dt_inizio_val, id_ente, creabile_da_interfaccia, 
	                             creabile_da_servizio, responsabile_trattamento_stardas, override_responsabile_trattamento,
	                             override_fruitore_default, ente_certificatore) VALUES 
	('TP1', 'Tipo pratica 1','proc_1', CURRENT_TIMESTAMP(), 1, true, true, 'AAAAAA00A00A000A', false, false, 'CERT1'),
	('TP2', 'Tipo pratica 2','proc_2', CURRENT_TIMESTAMP(), 1, true, true, 'AAAAAA00A00A000B', false, false, null),
	('TP3', 'Tipo pratica 3','proc_3', CURRENT_TIMESTAMP(), 1, true, true, 'AAAAAA00A00A000A', false, false, 'CERT1'),
	('TP4', 'Tipo pratica 4','proc_4', CURRENT_TIMESTAMP(), 1, true, true, 'AAAAAA00A00A000A', false, false, 'CERT1'),
	('TP5', 'Tipo pratica 5','proc_5', CURRENT_TIMESTAMP(), 1, true, true, 'AAAAAA00A00A000A', false, false, 'CERT1'),
	('TP6', 'Tipo pratica 6','proc_6', CURRENT_TIMESTAMP(), 1, true, false, 'AAAAAA00A00A000A', false, false, 'CERT1'),
	('TP7', 'Tipo pratica 7','proc_7', CURRENT_TIMESTAMP(), 2, true, true, 'AAAAAA00A00A000A', false, false, 'CERT1'),
	('TP8', 'Tipo pratica 8','proc_8', CURRENT_TIMESTAMP(), 2, false, true, 'AAAAAA00A00A000A', false, false, 'CERT1'),
	('111', '111', '111', CURRENT_TIMESTAMP(), 1, true, true, 'AAAAAA00A00A000B', false, false, null);
	   
INSERT INTO cosmo_d_tipo_credenziali_firma (codice, descrizione, non_valido_in_apposizione, dt_inizio_val)
VALUES ('TIPOCREDFIRMA1', 'Tipo credenziale firma di prova 1', false, CURRENT_TIMESTAMP()),
	   ('TIPOCREDFIRMA2', 'Tipo credenziale firma di prova 2', false, CURRENT_TIMESTAMP()),
	   ('TIPOCREDFIRMA3', 'Tipo credenziale firma di prova 3', true, CURRENT_TIMESTAMP());
	   
INSERT INTO cosmo_d_tipo_otp (codice, descrizione, non_valido_in_apposizione, dt_inizio_val)
VALUES ('TIPOOTP1', 'Tipo OTP di prova 1', false, CURRENT_TIMESTAMP()),
	   ('TIPOOTP2', 'Tipo OTP di prova 2', false, CURRENT_TIMESTAMP()),
       ('TIPOOTP3', 'Tipo OTP di prova 3', true, CURRENT_TIMESTAMP());
       
INSERT INTO cosmo_d_profilo_feq (codice, descrizione, non_valido_in_apposizione, dt_inizio_val)
VALUES ('PROFILOFEQ1', 'Profilo FEQ 1', false, CURRENT_TIMESTAMP()),
	   ('PROFILOFEQ2', 'Profilo FEQ 2', false, CURRENT_TIMESTAMP()),
	   ('PROFILOFEQ3', 'Profilo FEQ 3', true, CURRENT_TIMESTAMP());
	   
INSERT INTO cosmo_d_scelta_marca_temporale (codice, descrizione, non_valido_in_apposizione, dt_inizio_val)
VALUES ('SI', 'S�', false, CURRENT_TIMESTAMP()),
	   ('NO', 'No', false, CURRENT_TIMESTAMP()),
	   ('SCEGLI', 'Scegli sempre', true, CURRENT_TIMESTAMP());

INSERT INTO cosmo_d_stato_documento (codice, descrizione, dt_inizio_val, dt_fine_val) VALUES
	('ACQUISITO', 'Il file e'' stato caricato ma non ancora elaborato', CURRENT_TIMESTAMP(), NULL),
	('IN_ELABORAZIONE', 'Il file e'' stato caricato ed e'' attualmente in fase di elaborazione', CURRENT_TIMESTAMP(), NULL),
	('ELABORATO', 'Il file e'' stato acquisito e correttamente elaborato', CURRENT_TIMESTAMP(), NULL),
	('ERR_ACQUISIZIONE', 'Si e'' verificato un errore in fase di acquisizione del file', CURRENT_TIMESTAMP(), NULL),
	('ERR_ANALISI', 'Si e'' verificato un errore in fase di analisi del contenuto del file', CURRENT_TIMESTAMP(), NULL),
	('ERR_SBUSTAMENTO', 'Si e'' verificato un errore in fase di estrazione del contenuto firmato', CURRENT_TIMESTAMP(), NULL);

INSERT INTO cosmo_d_tipo_contenuto_documento (codice, descrizione, dt_inizio_val) VALUES
	('TEMPORANEO', 'Contenuto temporaneo salvato su fileshare in attesa di caricamento', CURRENT_TIMESTAMP()),
	('ORIGINALE', 'Contenuto del file originale', CURRENT_TIMESTAMP()),
	('SBUSTATO', 'Contenuto sbustato dal file originale', CURRENT_TIMESTAMP()),
	('FIRMATO', 'Contenuto ottenuto firmando il file originale', CURRENT_TIMESTAMP());

INSERT INTO cosmo_d_stato_pratica(codice, descrizione, dt_inizio_val) VALUES 
	( 'BOZZA', 'Bozza', CURRENT_TIMESTAMP()),
	( 'PROVA', 'Test', CURRENT_TIMESTAMP());

INSERT INTO cosmo_d_tipo_condivisione_pratica (codice,descrizione,dt_inizio_val,dt_fine_val) VALUES 
	('preferita','pratica preferita','2020-09-11 12:27:43.618',NULL),
	('condivisa','pratica condivisa','2020-09-11 12:28:07.064',NULL);

INSERT INTO cosmo_d_tipo_relazione_pratica (codice, descrizione, dt_inizio_val, descrizione_inversa) VALUES 
	('DUPLICA', 'Duplica', CURRENT_TIMESTAMP(), 'Duplicato'),
   	('DIPENDE_DA', 'Dipende da', CURRENT_TIMESTAMP(), 'Padre di'),
    ('DIPENDENTE_DA', 'Dipendente da', CURRENT_TIMESTAMP(), 'Padre di');
    
INSERT INTO cosmo_t_profilo (id,codice,descrizione,dt_inizio_val,dt_inserimento,utente_inserimento,assegnabile) VALUES 
	(1,'ADMIN','Amministratore',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),'SYSTEM',true),
	(2,'OPER','Operatore',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),'SYSTEM',true),
	(3,'valore 1','valore 1',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),'SYSTEM',true),
	(4,'OPEN','open',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),'SYSTEM',false),
	(5,'valore 12','valore 12',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),'SYSTEM',true);

INSERT INTO cosmo_t_utente (id, codice_fiscale,nome,cognome,dt_inserimento,utente_inserimento,dt_cancellazione,utente_cancellazione) VALUES 
	(1, 'AAAAAA00B77B000F','DEMO 20','CSI PIEMONTE',CURRENT_TIMESTAMP(),'SYSTEM', null, null),
	(2, 'AAAAAA00A11B000J','DEMO 21','CSI PIEMONTE',CURRENT_TIMESTAMP(),'SYSTEM', null, null),
	(3, 'AAAAAA00A11C000K','DEMO 22','CSI PIEMONTE',CURRENT_TIMESTAMP(),'SYSTEM', null, null),
	(4, 'AAAAAA00A11C000X','DEMO 23','CSI PIEMONTE',CURRENT_TIMESTAMP(),'SYSTEM', null, null);

	
INSERT INTO cosmo_t_gruppo (id,codice,id_ente,nome,descrizione, dt_inserimento, utente_inserimento) VALUES 
	(1,'DEMO2021',1,'Demo 20 e 21','Gruppo con solo utenti demo 20 e demo 21', CURRENT_TIMESTAMP(),'SYSTEM'), 
	(2,'DEMO2122',1,'Demo 21 e 22','Gruppo con solo utenti demo 21 e demo 22', CURRENT_TIMESTAMP(),'SYSTEM');

INSERT INTO cosmo_t_preferenze_ente (id,id_ente,versione,valore,dt_inserimento, utente_inserimento) VALUES
	(1,1,'1.0','{"header": "Regione Piemonte"}', CURRENT_TIMESTAMP(),'SYSTEM');
	
INSERT INTO cosmo_t_preferenze_utente (id, id_utente,versione,valore,dt_inserimento, utente_inserimento) VALUES 
	(1,1,'1.0','{"maxPageSize":50}',CURRENT_TIMESTAMP(),'SYSTEM');

INSERT INTO cosmo_t_certificato_firma (id,descrizione,username,pin,password,id_utente,dt_scadenza,ente_certificatore,profilo_feq,scelta_temporale,tipo_credenziali_firma,tipo_otp,dt_inserimento,utente_inserimento,ultimo_utilizzato)
	VALUES ( 1, 'Certificato 1', 'QAuay0875GeZBu1zyVbfzQ==', 'Y8z0QFrkEmAaodoTqyDxjw==','Y8z0QFrkEmAaodoTqyDxjw==', 1, '5LOGTE078WRwdfrMMiaHidk6T9RGOmWozLBRmuCpB7Y=', 'CERT1', 'PROFILOFEQ1', 'SI', 'TIPOCREDFIRMA1', 'TIPOOTP1', CURRENT_TIMESTAMP(), 'SYSTEM', false ),
	       ( 2, 'Certificato 2', 'QAuay0875GeZBu1zyVbfzQ==', 'Y8z0QFrkEmAaodoTqyDxjw==','Y8z0QFrkEmAaodoTqyDxjw==', 2, '5LOGTE078WRwdfrMMiaHidk6T9RGOmWozLBRmuCpB7Y=', 'CERT1', 'PROFILOFEQ1', 'SI', 'TIPOCREDFIRMA1', 'TIPOOTP1', CURRENT_TIMESTAMP(), 'SYSTEM', false ),
	       ( 3, 'Certificato 3', 'QAuay0875GeZBu1zyVbfzQ==', 'Y8z0QFrkEmAaodoTqyDxjw==','Y8z0QFrkEmAaodoTqyDxjw==', 1, '5LOGTE078WRwdfrMMiaHidk6T9RGOmWozLBRmuCpB7Y=', 'CERT1', 'PROFILOFEQ1', 'SI', 'TIPOCREDFIRMA1', 'TIPOOTP1', CURRENT_TIMESTAMP(), 'SYSTEM', true );

INSERT INTO cosmo_t_applicazione_esterna (id, descrizione, dt_inserimento, utente_inserimento) VALUES 
	(1, 'Applicazione test 1',CURRENT_TIMESTAMP(),'SYSTEM'),
	(2, 'Applicazione test 2',CURRENT_TIMESTAMP(),'SYSTEM'),
	(3, 'Applicazione test 3',CURRENT_TIMESTAMP(),'SYSTEM'),
	(4, 'Applicazione test 4',CURRENT_TIMESTAMP(),'SYSTEM'),
	(5, 'Applicazione test 5',CURRENT_TIMESTAMP(),'SYSTEM');
	
INSERT INTO cosmo_r_ente_applicazione_esterna (id_ente,id_applicazione_esterna,dt_inizio_val) VALUES
	(1,1,CURRENT_TIMESTAMP()),
	(2,1,CURRENT_TIMESTAMP()),
	(1,2,CURRENT_TIMESTAMP()),
	(2,3,CURRENT_TIMESTAMP()),
	(1,5,CURRENT_TIMESTAMP());

INSERT INTO cosmo_t_funzionalita_applicazione_esterna (id,descrizione,url,principale,id_ente_applicazione_esterna,dt_inserimento,utente_inserimento) VALUES 
	(1,'Funzionalita test 1 di app 1 per ente 1','www.google.com',true,1,CURRENT_TIMESTAMP(),'SYSTEM'),
	(2,'Funzionalita test 2 di app 1 per ente 1','www.google.com',false,1,CURRENT_TIMESTAMP(),'SYSTEM'),
	(3,'Funzionalita test 1 di app 2 per ente 2','www.google.com',true,2,CURRENT_TIMESTAMP(),'SYSTEM'),
	(4,'Funzionalita test 2 di app 2 per ente 2','www.google.com',false,2,CURRENT_TIMESTAMP(),'SYSTEM'),
	(5,'Funzionalita test 1 di app 2 per ente 1','www.google.com',true,3,CURRENT_TIMESTAMP(),'SYSTEM'),
	(6,'Funzionalita test 3 di app 1 per ente 1','www.google.com',false,1,CURRENT_TIMESTAMP(),'SYSTEM');

INSERT INTO cosmo_t_fruitore (id, api_manager_id, url, dt_inserimento, utente_inserimento, nome_app, dt_cancellazione, utente_cancellazione) VALUES 
	(1, 'FruitoreTest1', 'http://www.fruitore1.it',CURRENT_TIMESTAMP(),'SYSTEM', 'Test1', null, null),
	(2, 'FruitoreTest2', 'http://www.fruitore2.it',CURRENT_TIMESTAMP(),'SYSTEM', 'Test2', null, null),
	(3, 'FruitoreTest3', 'http://www.fruitore3.it','2020-09-21 17:16:57.482','SYSTEM', 'Test3', '2022-09-21 17:16:57.482', 'SYSTEM'),
	(4, 'FruitoreTest4', 'http://www.fruitore4.it',CURRENT_TIMESTAMP(),'SYSTEM', 'Test4', null, null);	
	
INSERT INTO cosmo_t_pratica (id, id_ente, link_pratica, dt_inserimento, utente_inserimento, oggetto, uuid_nodo, id_fruitore, id_pratica_ext, codice_tipo_pratica, utente_creazione_pratica, metadati) VALUES
	(1, 1, '/pratiche/1', CURRENT_TIMESTAMP(), 'SYSTEM', 'Oggetto pratica', null, 1, '1', 'TP1', 'AAAAAA00B77B000F', '{"emailRichiedente":"email@email.it"}'),
	(2, 1, '/pratiche/2', CURRENT_TIMESTAMP(), 'SYSTEM', 'oggetto 2', null, 1, '2','TP1', 'AAAAAA00B77B000F', '{"emailRichiedente":"email@email.it"}'),
	(3, 2, '/pratiche/3', CURRENT_TIMESTAMP(), 'SYSTEM', 'oggetto 3', null, 2, '3','TP1', 'AAAAAA00B77B000F', null),
	(4, 1, '/pratiche/4', CURRENT_TIMESTAMP(), 'SYSTEM', 'oggetto 4', null, null, '4','TP1', 'AAAAAA00B77B000F', '["emailRichiedente":"email@email.it"]'),
	(5, 1, '/pratiche/5', CURRENT_TIMESTAMP(), 'SYSTEM', 'oggetto 5', null, 1, '5','TP1', 'AAAAAA00B77B000F', '{"oggetto":{"colore": "blu","forma":[{"primo" : "cerchio"}]}}'),
	(6, 2, null, CURRENT_TIMESTAMP(), 'SYSTEM', 'oggetto 6', null, 1, '6','TP1', 'AAAAAA00B77B000F', null),
	(7, 1, '/pratiche/7', CURRENT_TIMESTAMP(), 'SYSTEM', 'oggetto 7', null, 1, '7',null, 'AAAAAA00B77B000F', '{"emailRichiedente":"email@email.it"}'),
	(8, 1, '/pratiche/8', CURRENT_TIMESTAMP(), 'SYSTEM', 'oggetto 8', null, 1, '8','TP2', 'AAAAAA00B77B000F', '{"emailRichiedente":"email@email.it"}'),
	(9, 1, '', CURRENT_TIMESTAMP(), 'SYSTEM', 'oggetto 9', null, 1, '9','TP1', 'AAAAAA00B77B000F', '{"emailRichiedente":"email@email.it"}'),
	(10, 1, null, CURRENT_TIMESTAMP(), 'SYSTEM', 'oggetto 10', null, 1, '10','TP1', 'AAAAAA00B77B000F', '{"emailRichiedente":"email@email.it"}'),
	(11, 1, '/pratiche/', CURRENT_TIMESTAMP(), 'SYSTEM', 'oggetto 11', null, 1, '11','TP1', 'AAAAAA00B77B000F', '{"emailRichiedente":"email@email.it"}'),
	(12, 1, '/pratiche/12', CURRENT_TIMESTAMP(), 'SYSTEM', 'oggetto 12', null, null, '12','TP1', 'AAAAAA00B77B000F', '[{"emailRichiedente":{"valoreEmail":"email@email.it"}}]'),
	(13, 1, '/pratiche/13', CURRENT_TIMESTAMP(), 'SYSTEM', 'Oggetto pratica 13', null, 1, '13', 'TP1', 'AAAAAA00A11B000J', '{"emailRichiedente":"email@email.it"}');
	
INSERT INTO cosmo_t_documento (id, codice_stato_documento, titolo, uuid_doc, id_pratica, utente_inserimento, dt_inserimento, parent_id, codice_tipo_documento, codice_tipo_firma)  VALUES
	( 1, 'ELABORATO', 'Documento 1', 'a4010513-db0d-4f59-bda5-c7b80f228400', 1, 'SYSTEM',CURRENT_TIMESTAMP(), null, 'codice 1', 'XML'),
	( 2, 'ELABORATO', 'Documento 2', 'a4010513-db0d-4f59-bda5-c7b80f228401', 1, 'SYSTEM', CURRENT_TIMESTAMP(), null, 'codice 1', null),
	( 3, 'ELABORATO', 'Documento 3', 'a4010513-db0d-4f59-bda5-c7b80f228402', 1,  'SYSTEM', CURRENT_TIMESTAMP(), 1, 'codice 1', null),
	( 4, 'ELABORATO', 'Documento 4', 'a4010513-db0d-4f59-bda5-c7b80f228403', 2, 'SYSTEM', CURRENT_TIMESTAMP(), null, 'codice 2', null);
	
INSERT INTO cosmo_t_contenuto_documento (id, id_documento, id_contenuto_sorgente, codice_tipo_contenuto, codice_formato_file, codice_tipo_firma, nome_file, utente_inserimento, dt_inserimento, sha_file) 
	VALUES( 1, 1, null, 'ORIGINALE', 'application/xml', 'XML', 'testfile-content.xml', 'SYSTEM',CURRENT_TIMESTAMP(), null);
	
INSERT INTO cosmo_t_form_logico (id, codice, descrizione, dt_inserimento, utente_inserimento, id_ente, eseguibile_massivamente) VALUES
	(1, 'form1', 'form1', '2020-09-21 17:16:57.482', 'SYSTEM', null, false),
	(2, 'form2', 'form2', '2020-09-21 17:17:06.488', 'SYSTEM', null, false),
	(3, 'form3', 'form3', '2020-09-21 17:16:57.482', 'SYSTEM', 1, false),
	(4, 'form4', 'form4', '2020-09-21 17:16:57.482', 'SYSTEM', 2, false),
	(5, 'form5', 'form5', '2020-09-21 17:16:57.482', 'SYSTEM', 1, false),
	(6, 'form6', 'form6', '2020-09-21 17:16:57.482', 'SYSTEM', 1, true);

INSERT INTO cosmo_t_funzionalita (id, nome_funzionalita, dt_inizio_val, dt_fine_val) VALUES(1, 'funzione1', '2020-09-21 17:15:41.259', NULL);
INSERT INTO cosmo_t_funzionalita (id, nome_funzionalita, dt_inizio_val, dt_fine_val) VALUES(2, 'funzione2', '2020-09-10 17:16:16.000', NULL);

INSERT INTO cosmo_r_tipodoc_tipopratica (codice_tipo_documento,	codice_tipo_pratica, dt_inizio_val, dt_fine_val) VALUES
	('codice 1', 'TP1', '2020-09-21 17:15:41.259', NULL ),
	('codice 2', 'TP1', '2020-09-21 17:15:41.259', NULL );
	
INSERT INTO cosmo_r_profilo_use_case (id_profilo,codice_use_case)
	VALUES (1,'ADMIN PRIN');

INSERT INTO cosmo_r_utente_ente (id_utente,id_ente,telefono,email,dt_inizio_val) VALUES 
	(1,1,'011000001','test1@test.it',CURRENT_TIMESTAMP()),
	(2,1,'011000002','test2@test.it',CURRENT_TIMESTAMP()),
	(3,1,'011000003','test3@test.it',CURRENT_TIMESTAMP());

INSERT INTO cosmo_t_utente_gruppo (id,id_utente,id_gruppo, utente_inserimento, dt_inserimento, dt_cancellazione, utente_cancellazione) VALUES 
	(1,1,1,'SYSTEM', now(), null, null),
	(2,2,1,'SYSTEM', now(), null, null),
	(3,2,2,'SYSTEM', now(), null, null),
	(4,3,2,'SYSTEM', now(), null, null);

INSERT INTO cosmo_r_utente_profilo (id,id_utente,id_profilo, id_ente,dt_inizio_val)
	VALUES (1,1,1,1,CURRENT_TIMESTAMP());
	
INSERT INTO cosmo_r_fruitore_ente (id_fruitore,id_ente,dt_inizio_val)
	VALUES (1,1,CURRENT_TIMESTAMP());

INSERT INTO cosmo_r_fruitore_autorizzazione (id_fruitore,codice) VALUES
	(1,'I_PRATICA'), 
	(1,'G_NOTIFICA');
	
INSERT INTO cosmo_r_ente_funzionalita_applicazione_esterna (id_ente,id_funzionalita_applicazione_esterna,dt_inizio_val, dt_fine_val) VALUES 
	(1,1,CURRENT_TIMESTAMP(),null),
	(1,2,CURRENT_TIMESTAMP(),null),
	(2,3,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP()),
	(2,4,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP()),
	(1,5,CURRENT_TIMESTAMP(),null),
	(1,6,CURRENT_TIMESTAMP(),null);
	
INSERT INTO cosmo_r_utente_funzionalita_applicazione_esterna (id_utente,id_funzionalita_applicazione_esterna,dt_inizio_val,dt_fine_val,posizione) VALUES
	(1,1,CURRENT_TIMESTAMP(),null,0),
	(1,2,CURRENT_TIMESTAMP(),null,0),
	(1,3,CURRENT_TIMESTAMP(),null,1),
	(2,1,CURRENT_TIMESTAMP(),null,0),
	(2,2,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),0),
	(1,6,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),0);
	
INSERT INTO cosmo_r_stato_tipo_pratica(id,codice_stato_pratica, codice_tipo_pratica, dt_inizio_val) VALUES ( 1, 'BOZZA', 'TP1', CURRENT_TIMESTAMP());
INSERT INTO cosmo_r_stato_tipo_pratica(id,codice_stato_pratica, codice_tipo_pratica, dt_inizio_val) VALUES ( 2, 'PROVA', 'TP1', CURRENT_TIMESTAMP());

INSERT INTO cosmo_r_funzionalita_form_logico (id_form_logico, id_funzionalita, dt_inizio_val, dt_fine_val) VALUES(1, 1, '2020-09-21 17:17:29.464', NULL);
INSERT INTO cosmo_r_funzionalita_form_logico (id_form_logico, id_funzionalita, dt_inizio_val, dt_fine_val) VALUES(1, 2, '2020-09-21 17:17:31.201', NULL);
INSERT INTO cosmo_r_funzionalita_form_logico (id_form_logico, id_funzionalita, dt_inizio_val, dt_fine_val) VALUES(2, 1, '2020-09-21 17:17:39.289', NULL);

INSERT INTO cosmo_d_funzionalita_form_logico (codice,descrizione,eseguibile_massivamente, dt_inizio_val) VALUES
	('COMMENTI','Commenti',false,'2020-01-01 00:00:00.000'),
	('CONSULTAZIONE-DOCUMENTI','Consultazione documenti',false,'2020-01-01 00:00:00.000'),
	('GESTIONE-DOCUMENTI','Gestione e consultazione documenti',false,'2020-01-01 00:00:00.000'),
	('APPROVAZIONE','Approvazione / Rifiuto',true,'2020-01-01 00:00:00.000'),
	('FIRMA-DOCUMENTI','Firma Documenti',true,'2020-01-01 00:00:00.000'),
	('ASSOCIAZIONE-PRATICHE','Associazione pratiche',false,'2020-01-01 00:00:00.000'),
	('PROVA-DOCUMENTI','Prova Documenti',false,'2020-01-01 00:00:00.000');

INSERT INTO cosmo_d_chiave_parametro_funzionalita_form_logico (codice,descrizione,dt_inizio_val,tipo) VALUES
	('O_APPROVAZIONE','Output dell''approvazione','2020-01-01 00:00:00.000','object'),
	('ATT_ASSEGNABILI','Elenco delle attivita assegnabili','2020-01-01 00:00:00.000','object'),
	('O_ASSEGNAZIONE','Output dell''assegnazione attivita','2020-01-01 00:00:00.000','object'),
	('APPOSIZIONE_FIRMA','Indica se e'' possibile apporre la firma ai documenti','2021-01-29 14:39:07.442','boolean'),
	('TIPI_DOC_OBBLIGATORI','Elenco tipi documento obbligatori','2020-01-01 00:00:00.000','object'),
	('TIPI_DOC_DA_FIRMARE','Elenco tipi documento da firmare obbligatori','2020-01-01 00:00:00.000','object');

INSERT INTO cosmo_t_istanza_funzionalita_form_logico (id, codice_funzionalita,descrizione,dt_inserimento,utente_inserimento, dt_cancellazione, utente_cancellazione) VALUES 
	(1,'COMMENTI','Tab dei commenti nel task di approvazione','2020-01-01 00:00:00.000','SYSTEM', null, null),
	(2,'APPROVAZIONE','Tab di approvazione','2020-01-01 00:00:00.000','SYSTEM', null, null),
	(3,'GESTIONE-DOCUMENTI','Tab di gestione documenti in fase di selezione approvatori','2020-01-01 00:00:00.000','SYSTEM', null, null),
	(4,'CONSULTAZIONE-DOCUMENTI','Tab di consultazione documenti in approvazione','2020-01-01 00:00:00.000','SYSTEM', null, null),
	(5,'PROVA-DOCUMENTI','Tab di consultazione documenti in approvazione','2020-01-01 00:00:00.000','SYSTEM', '2022-01-01 00:00:00.000', 'SYSTEM'),
	(6,'FIRMA-DOCUMENTI','Tab di consultazione documenti in approvazione','2020-01-01 00:00:00.000','SYSTEM', null, null);

INSERT INTO cosmo_t_attivita (id,id_pratica,link_attivita,nome,descrizione,dt_inserimento,utente_inserimento,parent,form_key,id_form_logico) VALUES 
	(1,1,'tasks/877504','Richiesta di collaborazione','Richiesta di collaborazione',now(),'COSMO_CLIENT',null,'form1',1),
	(2,1,'tasks/877501','Richiesta di collaborazione','Richiesta di collaborazione',now(),'COSMO_CLIENT',null,'form1',1),
	(3,1,'tasks/295012','Selezione Approvatori','Si seleziona gli utenti che dovranno effettuare l''approvazione',now(),'SYSTEM', 1,'form1',1),
	(4,1,'tasks/197539','Selezione Approvatori','Si seleziona gli utenti che dovranno effettuare l''approvazione',now(),'SYSTEM', 2,'form1',1),
	(5,1,'tasks/295013','Selezione Approvatori','Si seleziona gli utenti che dovranno effettuare l''approvazione',now(),'SYSTEM', 1,'form1',5),
	(6,1,'tasks/877507','Richiesta di collaborazione','Richiesta di collaborazione',now(),'AAAAAA00B77B000F',null,'form1',1),
	(7,3,'tasks/295014','Selezione Approvatori','Si seleziona gli utenti che dovranno effettuare l''approvazione',now(),'SYSTEM', 1,'form1',6),
	(8,6,'tasks/887509','Richiesta di collaborazione','Richiesta di collaborazione',now(),'COSMO_CLIENT',null,'form1',1);

INSERT INTO cosmo_d_esito_verifica_firma (codice,descrizione,dt_inizio_val)
	VALUES ('VALIDA','Firma valida',now());
	
INSERT INTO cosmo_t_info_verifica_firma (id,id_contenuto_documento,codice_esito,firmatario,cf_firmatario,organizzazione,dt_apposizione,dt_inserimento,utente_inserimento,dt_verifica_firma)
	VALUES (1,1,'VALIDA','SERRATRICE GABRIELLA','SRRGRL59D68L219Y','REGIONE PIEMONTE',now(),now(),'COSMOECM',now());

INSERT INTO cosmo_r_attivita_assegnazione (id_utente,id_attivita,assegnatario,dt_inizio_val, id_gruppo) VALUES 
	(1,1,true,now(), null),
	(1,2,true,now(), null),
	(1,3,true,now(), null),
	(1,4,true,now(), null),
	(1,5,true,now(), 1),
	(1,7,true,now(), null);

INSERT INTO cosmo_r_pratica_pratica (id_pratica_da,id_pratica_a,codice_tipo_relazione,dt_inizio_val, dt_fine_val)
	VALUES (2,1,'DIPENDE_DA',now(), null),
	(1,2,'DIPENDENTE_DA',now(), null),
	(1,5,'DIPENDENTE_DA','2020-01-01 00:00:00.000', '2022-01-01 00:00:00.000'),
	(1,13,'DIPENDE_DA',now(), null);
	
INSERT INTO cosmo_t_template_report (id_ente, codice_tipo_pratica, codice, sorgente_template, dt_inserimento, utente_inserimento, dt_cancellazione) VALUES
    (1, 'TP1', 'codice1', ' ', now(), 'MC', null),
    (1, 'TP1', 'codice2', ' ', now(), 'MC', now()),
    (1, 'TP2', 'codice1', ' ', now(), 'MC', null);

INSERT INTO cosmo_d_custom_form_formio (codice,descrizione,custom_form,dt_inizio_val, codice_tipo_pratica) VALUES 
	('custom-1','Esempio di custom form 1','{"_id":"60f53d654be877dcf01cf366","type":"form","tags":["common"],"owner":"60f190aa6cba25e54c0bfa97"}', now(), null),
	('custom-2','Esempio di custom form 2','{"_id":"60f53d654be877dcf01cf367","type":"form","tags":["common"],"owner":"60f190aa6cba25e54c0bfa98"}', now(), null),
	('custom-3','Esempio di custom form 3','{"_id":"60f53d654be877dcf01cf367","type":"form","tags":["common"],"owner":"60f190aa6cba25e54c0bfa98"}', now(), 'TP4'),
	('custom-4','Esempio di custom form 4','{"_id":"60f53d654be877dcf01cf367","type":"form","tags":["common"],"owner":"60f190aa6cba25e54c0bfa98"}', now(), 'TP5'),
	('custom-5','Esempio di custom form 5','{"_id":"60f53d654be877dcf01cf367","type":"form","tags":["common"],"owner":"60f190aa6cba25e54c0bfa98"}', now(), 'TP5'),
	('custom-6','Esempio di custom form 6','{"_id":"60f53d654be877dcf01cf367","type":"form","tags":["common"],"owner":"60f190aa6cba25e54c0bfa98"}', now(), 'TP1');
	

INSERT INTO cosmo_d_operazione_fruitore (codice, descrizione, dt_inizio_val, dt_fine_val, personalizzabile)VALUES
	('CUSTOM', 'Callback personalizzabile', now(), NULL, true),
	('CUSTOM2', 'Callback personalizzabile', now(), NULL, false);
	
INSERT INTO cosmo_d_tipo_schema_autenticazione (codice, descrizione, dt_inizio_val, dt_fine_val) VALUES 
    ('BASIC', 'basic', '2020-09-21 17:16:57.482', null),
    ('NONE', 'none', '2020-09-21 17:16:57.482', null);
    
INSERT INTO cosmo_t_schema_autenticazione_fruitore (id, codice_tipo_schema, dt_inserimento, utente_inserimento, dt_cancellazione, utente_cancellazione, id_fruitore, in_ingresso) VALUES 
	(1, 'BASIC', '2021-02-08 10:19:17.887', 'SYSTEM', null, null, 1, true),
	(2, 'NONE', '2021-02-08 10:19:17.887', 'SYSTEM', null, null, 1, true),
	(3, 'BASIC', '2021-02-08 10:19:17.887', 'SYSTEM', null, null, 2, true),
	(4, 'BASIC', '2021-02-08 10:19:17.887', 'SYSTEM', null, null, 4, true);
	
INSERT INTO cosmo_t_credenziali_autenticazione_fruitore (id, id_schema, username, password, client_id, client_secret, dt_inserimento, utente_inserimento, dt_cancellazione, utente_cancellazione) VALUES 
	(1, 1, 'utente1', 'pass1', null, null, '2021-02-08 10:19:17.887', 'SYSTEM', null, null),
	(2, 2, 'utente2', 'pass2', null, null, '2021-02-08 10:19:17.887', 'SYSTEM', '2022-02-08 10:19:17.887', 'SYSTEM'),
	(3, 3, 'utente1', 'pass1', null, null, '2021-02-08 10:19:17.887', 'SYSTEM', null, null),
	(4, 4, 'utente4', 'pass4', null, null, '2021-02-08 10:19:17.887', 'SYSTEM', null, null);

INSERT INTO cosmo_t_endpoint_fruitore (id, id_fruitore, id_schema_autenticazione_fruitore, codice_operazione_fruitore, codice_tipo_endpoint, endpoint, dt_inserimento, utente_inserimento, metodo_http, codice_descrittivo) VALUES
	(1, 1, NULL, 'CUSTOM', 'REST', '/endpoint', now(), 'SYSTEM', 'GET', 'Codice descrittivo'),
	(2, 1, NULL, 'CUSTOM', 'REST', '/endpoint/{$esempio$}', now(), 'SYSTEM', 'GET', 'Codice descrittivo - placeholder'),
	(3, 2, 3, 'CUSTOM', 'REST', '/endpoint/{$esempio$}', now(), 'SYSTEM', 'GET', 'Codice descrittivo - placeholder');
	
INSERT INTO cosmo_d_helper_pagina (codice, descrizione, dt_inizio_val) VALUES
    ('home', 'helper pagina principale', now()),
    ('preferenze_ente', 'helper preferenze ente', now()),
    ('lavorazione_pratica', 'helper lavorazione pratica', now());
    
INSERT INTO cosmo_d_helper_tab (codice, descrizione, codice_pagina, dt_inizio_val) VALUES
    ('tab1', 'test tab', 'preferenze_ente',now()),
    ('custom-form', 'Custom form', 'lavorazione_pratica',now());

INSERT INTO cosmo_d_helper_modale (codice, descrizione, codice_pagina, codice_tab, dt_inizio_val, dt_fine_val) VALUES
    ('anteprima-documento', 'anteprima-documento', 'lavorazione_pratica','custom-form',now(),NULL),
    ('anteprima-documento2', 'anteprima-documento2', 'preferenze_ente','tab1',now(),NULL);
    
INSERT INTO cosmo_t_helper (id, codice_pagina, codice_tab, codice_form, html, dt_inserimento, utente_inserimento, dt_cancellazione, utente_cancellazione, codice_modale) VALUES
    (1, 'home', NULL,NULL, '<p>helper della home</p>', now(), 'MC', NULL, NULL, 'anteprima-documento'),
    (2, 'home',NULL,NULL, '<p>helper della home</p>', now(), 'MC', now(), 'MC', 'anteprima-documento'),
    (3, 'preferenze_ente','tab1',NULL, '<p>helper preferenze ente</p>', now(), 'MC', NULL,NULL, 'anteprima-documento'),
    (4, 'lavorazione_pratica','custom-form','custom-1','<p>Helper custom form</p>', now(), 'MC', NULL, NULL, 'anteprima-documento');
    
INSERT INTO cosmo_c_configurazione_ente (chiave,valore,descrizione,id_ente,dt_inizio_val, dt_fine_val) VALUES
	 ('chiave 1','valore 1','Esempio di chiave con valore - valido', 1, now(), null),
	 ('chiave 2','valore 2','Esempio di chiave con valore - non valido', 1, now(), now()),
	 ('chiave 3','valore 3','Esempio di chiave con valore - valido', 1, now(), null),
	 ('profilo.utente.default','valore 12','Esempio di chiave con valore - valido', 1, now(), null);
	 
INSERT INTO cosmo_d_tipo_tag (codice, descrizione, label, dt_inizio_val, dt_fine_val) VALUES
	('DIR', 'Direzione', 'Direzione Generale', now(), NULL),
	('AOO', 'AOO', '144-1', now(), NULL),
	('SEG', 'Segreteria', 'Segreteria Generale', now(), NULL);
	
INSERT INTO cosmo_t_tag (id, codice, descrizione, codice_tipo_tag, id_ente, dt_inserimento, utente_inserimento) VALUES
	(1, 'DIR-ACQ', 'Direttore acquisti', 'DIR', 1, now(), 'SYSTEM'),
	(2, 'DIR-VEN', 'Direttore vendite', 'DIR', 2, now(), 'SYSTEM'),
	(3, 'AOO-TO', 'Area Organizzativa Omogenea Torino', 'AOO', 1, now(), 'SYSTEM'),
	(4, 'AOO-MI', 'Area Organizzativa Omogenea Milano', 'AOO', 1, now(), 'SYSTEM'),
	(5, 'AOO-VE', 'Area Organizzativa Omogenea Venezia', 'AOO', 1, now(), 'SYSTEM');
	
INSERT INTO cosmo_r_pratica_tag(id_pratica, id_tag, dt_inizio_val) VALUES
	(1, 1, now()),
	(1, 2, now()),
	(2, 4, now());
	
INSERT INTO cosmo_r_utente_gruppo_tag(id_utente_gruppo, id_tag, dt_inizio_val) VALUES
    (1, 1 ,now());

INSERT INTO cosmo_d_tab_dettaglio (codice,descrizione,dt_inizio_val) VALUES 
	('documenti','Documenti', now()),
	('riassunto','Riassunto',now()),
	('commenti','Commenti',now()),
	('attivita','Attivit�',now()),
	('workflow','Workflow',now());
	
INSERT INTO cosmo_r_tab_dettaglio_tipo_pratica (codice_tipo_pratica,codice_tab_dettaglio,ordine,dt_inizio_val,dt_fine_val) VALUES 
	('TP1','riassunto', 1, now(), NULL),
	('TP2','documenti', 1, now(), NULL);

INSERT INTO cosmo_r_gruppo_tipo_pratica (id_gruppo, codice_tipo_pratica, dt_inizio_val, dt_fine_val, creatore, supervisore) VALUES
	(1, 'TP1', now(), NULL, true, false),
	(2, 'TP3', '2022-03-01 11:27:59.282', NULL, true, true);
	

INSERT INTO cosmo_d_stato_invio_stilo (codice,descrizione,dt_inizio_val) VALUES 
	('DA_INVIARE','Documento da inviare',now()),
	('IN_FASE_DI_INVIO','Documento in fase di invio',now()),
    ('INVIATO','Documento inviato',now()),
    ('ERR_INVIO','Documento non inviato a causa di errori',now()),
    ('NON_INVIATO','Non inviato',now());
    
INSERT INTO cosmo_r_invio_stilo_documento (id_ud, id_documento, codice_stato_invio_stilo, codice_esito_invio_stilo, messaggio_esito_invio_stilo, dt_inizio_val) VALUES
	(1,1,'INVIATO','111','Inviato con successo',now()),
	(2,1,'IN_FASE_DI_INVIO','222','Invio in corso',now()),
	(2,2,'ERR_INVIO','333','Si � verificato un errore',now()),
	(3,3,'DA_INVIARE','444','Documento da inviare',now());
   
INSERT INTO cosmo_d_tipo_commento (codice,descrizione,dt_inizio_val,dt_fine_val) VALUES
	 ('comment','Commento',now(),NULL),
	 ('event','Evento',now(),NULL);

	 
INSERT INTO cosmo_t_commento (id,codice_tipo,data_creazione,utente_creatore,id_pratica,id_attivita,utente_inserimento,dt_inserimento,messaggio) VALUES
	(1, 'comment', now(),'AAAAAA00B77B000F', 1, null, 'AAAAAA00B77B000F',now(), 'prova commento');

INSERT INTO cosmo_d_filtro_campo (codice,descrizione,dt_inizio_val,dt_fine_val) VALUES 
	('range','Range (Da / a)',now(),NULL),
 	('singolo','Singolo Campo',now(),NULL);
 	

INSERT INTO cosmo_d_formato_campo(codice,descrizione,dt_inizio_val,dt_fine_val) VALUES
	('stringa','Stringa',now(),NULL),
 	('numerico','Numerico',now(),NULL),
 	('booleano','Booleano',now(),NULL),
 	('data','Data',now(),NULL);

INSERT INTO cosmo_t_variabile_processo (id,nome_variabile,nome_variabile_flowable,codice_formato_campo,codice_filtro_campo,visualizzare_in_tabella,dt_inserimento,utente_inserimento,codice_tipo_pratica) VALUES 
	(1,'prova','prova flowable','stringa','range',false,now(),'AAAAAA00B77B000F','TP1'),
	(2,'prova2','prova flowable2','numerico','range',false,now(),'AAAAAA00B77B000F','TP2'),
	(3,'prova3','prova flowable3','numerico','singolo',true,now(),'AAAAAA00B77B000F','TP1');



INSERT INTO cosmo_t_variabile (id,type_name,nome,id_pratica,double_value,long_value,text_value,
bytearray_value,dt_inserimento,dt_ultima_modifica,utente_inserimento,utente_ultima_modifica,dt_cancellazione,utente_cancellazione,json_value)
VALUES (1,'string' ,'oggetto',1,null,null,'Test1',null,now(),null,'GUEST',null,null,null,null);
INSERT INTO cosmo_t_variabile (id,type_name,nome,id_pratica,double_value,long_value,text_value,
bytearray_value,dt_inserimento,dt_ultima_modifica,utente_inserimento,utente_ultima_modifica,dt_cancellazione,utente_cancellazione,json_value)
VALUES (2,'string' ,'oggetto',2,null,null,'Test2',null,now(),null,'GUEST',null,null,null,null);
INSERT INTO cosmo_t_variabile (id,type_name,nome,id_pratica,double_value,long_value,text_value,
bytearray_value,dt_inserimento,dt_ultima_modifica,utente_inserimento,utente_ultima_modifica,dt_cancellazione,utente_cancellazione,json_value)
VALUES (3,'integer' ,'numerovariabile',4,2,2,null,null,now(),null,'GUEST',null,null,null,null);
INSERT INTO cosmo_t_variabile (id,type_name,nome,id_pratica,double_value,long_value,text_value,
bytearray_value,dt_inserimento,dt_ultima_modifica,utente_inserimento,utente_ultima_modifica,dt_cancellazione,utente_cancellazione,json_value)
VALUES (4,'string' ,'testdata',4,null,null,'2023-03-09T14:32:34.943093+01:00',null,now(),null,'GUEST',null,null,null,null);
INSERT INTO cosmo_t_variabile (id,type_name,nome,id_pratica,double_value,long_value,text_value,
bytearray_value,dt_inserimento,dt_ultima_modifica,utente_inserimento,utente_ultima_modifica,dt_cancellazione,utente_cancellazione,json_value)
VALUES (5,'string' ,'oggetto',4,null,null,null,X'5465737433',now(),null,'GUEST',null,null,null,null);

INSERT INTO cosmo_d_stato_caricamento_pratica (codice,descrizione,dt_inizio_val) VALUES 
	('CARICAMENTO_IN_BOZZA','Caricamento in bozza',now()),
	('IN_ATTESA_DI_ELABORAZIONE','In attesa di elaborazione',now()),
    ('ELABORAZIONE_INIZIATA','Elaborazione iniziata',now()),
    ('PRATICA_CREATA','Pratica creata',now()),
    ('PRATICA_IN_ERRORE','Pratica in errore',now()),
    ('PRATICA_CREATA_CON_ERRORE','Pratica creata con errore',now()),
    ('CARICAMENTO_DOCUMENTI','Caricamento documenti',now()),
    ('CARICAMENTO_DOCUMENTI_IN_ERRORE','Caricamento documenti in errore',now()),
    ('PROCESSO_AVVIATO','Processo avviato',now()),
    ('PROCESSO_IN_ERRORE','Processo in errore',now()),
    ('ERRORE_ELABORAZIONE','Elaborazione in errore',now()),
    ('ELABORAZIONE_COMPLETATA','Elaborazione completata',now()),
    ('ELABORAZIONE_COMPLETATA_CON_ERRORE','Elaborazione completata con errore',now());
    
INSERT INTO cosmo_r_tipo_documento_tipo_documento (codice_padre, codice_allegato, codice_tipo_pratica, codice_stardas_allegato, dt_inizio_val) VALUES
    ('codice 1', 'codice 2', 'TP1', 'MOD', now());
    
INSERT INTO cosmo_d_tipo_contenuto_firmato (codice, descrizione, dt_inizio_val) VALUES 
	('firma_digitale','Firma digitale',now()),
    ('firma_elettronica_avanzata','Firma elettronica avanzata',now()),
    ('sigillo_elettronico','Sigillo elettronico',now());
    
INSERT INTO cosmo_t_template_fea (id, descrizione, id_ente, tipologia_pratica, tipologia_documento, coordinata_x, coordinata_y, pagina, caricato_da_template, id_pratica, utente_inserimento,dt_inserimento) VALUES 
	(1,'Lettera accordo Maggio 2023', 1, 'TP1', 'codice 1', 11.1, 12.1, 1, true, 1, 'GUEST', now()),
    (2,'Lettera accordo Giugno 2023', 1, 'TP2', 'codice 2', 111.12, 132.12, 100, false,  null, 'GUEST', now()),
    (3,'Lettera accordo Luglio 2023', 1, '111', 'codice 1', 51.1, 142.1, 12, true, null, 'GUEST', now());
    
INSERT INTO cosmo_t_credenziali_sigillo_elettronico(id, alias, delegated_domain, delegated_password, delegated_user, otp_pwd, tipo_hsm, tipo_otp_auth, utente, dt_inserimento, utente_inserimento) VALUES 
    (1, 'Test1', 'faCSI', 'Dct9dFFgAA7D', 'applicativodwd', 'dsign', 'COSIGN', 'faCSI', 'LCUDNC68D06F112Z', now(), 'GUEST'),
    (2, 'Test2', 'fbCSI', 'Fct9dFFgAA7D', 'applicativodwd', 'dsign', 'COSIGN', 'fbCSI', 'LCUDNC68D06F112Z', now(), 'GUEST'),
    (3, 'Test3', 'fdCSI', 'Gct9dFFgAA7D', 'applicativodwd', 'dsign', 'COSIGN', 'fdCSI', 'LCUDNC68D06F112Z', now(), 'GUEST');
    
 INSERT INTO cosmo_r_formato_file_tipo_documento(codice_formato_file, codice_tipo_documento, dt_inizio_val) VALUES 
    ('application/rtf', 'codice 1', now());
    
 INSERT INTO cosmo_d_stato_sigillo_elettronico (codice,descrizione,dt_inizio_val) VALUES
	 ('NON_SIGILLATO','Documento non sigillato',now()),
	 ('DA_SIGILLARE','Documento da sigillare',now()),
	 ('SIGILLO_WIP','Documento in fase di apposizione sigillo elettronico',now()),
	 ('SIGILLATO','Documento sigillato',now()),
	 ('ERR_SIGILLO','Apposizione sigillo su documento fallita a causa di errori',now());
	 
 INSERT INTO cosmo_t_sigillo_elettronico 
 (id_pratica, identificativo_evento, identificativo_alias, utilizzato, dt_inserimento, dt_ultima_modifica, utente_inserimento, utente_ultima_modifica, dt_cancellazione, utente_cancellazione) VALUES
 (1, 'SIGILLATO', 'Test', true, now(), null, 'GUEST', null, null, null);
 
 INSERT INTO cosmo_r_sigillo_documento 
 (id_sigillo, id_documento, codice_stato_sigillo, codice_esito_sigillo, messaggio_esito_sigillo, dt_inizio_val) VALUES
 (1, 1, 'SIGILLATO', '000', 'OK', now());

INSERT INTO cosmo_d_trasformazione_dati_pratica (id, codice_tipo_pratica, codice_fase, ordine, obbligatoria, descrizione, definizione, dt_inizio_val, dt_fine_val) VALUES 
	(1, 'TP1', 'beforeProcessStart', 1, true, 'prova1', 'prova1', '2021-10-22 15:54:56.933', null);

INSERT INTO cosmo_r_formato_file_tipo_documento (codice_formato_file, codice_tipo_documento, dt_inizio_val, dt_fine_val) VALUES 
	('application/xml', 'codice 1', '2021-10-22 15:54:56.933', null),
	('application/xml2', 'codice 2', '2021-10-22 15:54:56.933', null),
	('application/xml', 'codice 2', '2021-10-22 15:54:56.933', null),
	('application/xml2', 'codice 1', '2021-10-22 15:54:56.933', null);
	
INSERT INTO cosmo_l_storico_pratica (id, id_pratica, id_attivita, codice_tipo_evento, descrizione_evento, dt_evento, id_utente, 
id_fruitore, id_utente_coinvolto, id_gruppo_coinvolto) VALUES 
	(1, 1, 1, 'ATTIVITA_COMPLETATA', 'completata', '2021-02-08 10:19:17.887', 1, 1, 1, 1),
	(2, 1, 1, 'ATTIVITA_ANNULLATA', 'annullata', '2021-02-08 10:19:17.887', 1, 1, 1, 1),
	(3, 2, 1, 'ATTIVITA_CREATA', 'creata', '2021-02-08 10:19:17.887', 1, 1, 1, 1),
	(4, 1, 1, 'ATTIVITA_LAVORATA', 'lavorata', '2021-02-08 10:19:17.887', 2, 1, 2, 1),
	(5, 1, 1, 'ATTIVITA_LAVORATA', 'lavorata', '2021-02-08 10:19:17.887', 3, 1, 3, 1),
	(6, 13, 1, 'ATTIVITA_LAVORATA', 'lavorata', '2021-02-08 10:19:17.887', 4, 1, 4, 1);
	
INSERT INTO cosmo_r_pratica_utente_gruppo (id_utente, id_pratica, dt_inizio_val, dt_fine_val, codice_tipo_condivisione, id_utente_cond, id, id_gruppo) VALUES 
	(1, 1, '2021-02-08 10:19:17.887', null, 'preferita', null, 1, 1),
	(1, 1, '2021-02-08 10:19:17.887', null, 'condivisa', 1, 2, 1),
	(null, 1, '2021-02-08 10:19:17.887', null, 'condivisa', 1, 3, 1),
	(1, 1, '2021-02-08 10:19:17.887', null, 'condivisa', 2, 4, 1),
	(null, 1, '2021-02-08 10:19:17.887', null, 'condivisa', 2, 5, 1),
	(null, 1, '2021-02-08 10:19:17.887', null, 'condivisa', 2, 6, null);
	
INSERT INTO cosmo_r_form_logico_istanza_funzionalita (id_form_logico, id_istanza_funzionalita, ordine, dt_inizio_val, dt_fine_val, eseguibile_massivamente) VALUES 
	(3, 5, 1, '2021-02-08 10:19:17.887', null, false),
	(4, 1, 1, '2021-02-08 10:19:17.887', '2022-02-08 10:19:17.887', false),
	(5, 4, 1, '2021-02-08 10:19:17.887', null, false),
	(6, 6, 1, '2021-02-08 10:19:17.887', null, false);

INSERT INTO cosmo_r_istanza_form_logico_parametro_valore (id_istanza, codice_chiave_parametro, valore_parametro, dt_inizio_val, dt_fine_val) VALUES 
	(4, 'O_APPROVAZIONE', 'approvazione', '2021-02-08 10:19:17.887', null);
	
INSERT INTO cosmo_c_configurazione_metadati (chiave, valore, descrizione, dt_inizio_val, dt_fine_val, codice_tipo_pratica) VALUES 
	('emailRichiedente', 'emailRichiedente', 'email richiedente', '2021-02-08 10:19:17.887', null, 'TP1'),
	('coloreOggetto', 'oggetto.colore', 'colore oggetto', '2021-02-08 10:19:17.887', null, 'TP1'),
	('formaOggetto', 'oggetto.forma', 'forma oggetto', '2021-02-08 10:19:17.887', null, 'TP1'),
	('valoreEmail', 'emailRichiedente.valore', 'valore mail', '2021-02-08 10:19:17.887', null, 'TP1');
	
INSERT INTO cosmo_t_caricamento_pratica (id, nome_file, path_file, identificativo_pratica, codice_stato_caricamento_pratica, id_pratica, descrizione_evento, errore, id_ente, dt_inserimento, utente_inserimento, dt_ultima_modifica, utente_ultima_modifica, dt_cancellazione, utente_cancellazione, id_parent, id_utente) VALUES 
	(1, 'provafile', null, null, 'PROCESSO_AVVIATO', 1, null, null, 1, '2020-09-21 17:16:57.482', 'SYSTEM', null, null, null, null, null, 1),
	(2, 'provafile', null, null, 'CARICAMENTO_IN_BOZZA', 1, null, null, 1, '2020-09-21 17:16:57.482', 'SYSTEM', null, null, null, null, null, 1),
	(3, 'provafile', null, null, 'PROCESSO_AVVIATO', 1, null, null, 1, '2020-09-21 17:16:57.482', 'SYSTEM', null, null, null, null, 1, 1),
	(4, 'provafile', null, null, 'CARICAMENTO_IN_BOZZA', null, null, null, 1, '2020-09-21 17:16:57.482', 'SYSTEM', null, null, null, null, 3, 1),
	(5, 'provafile', 'provafile', null, 'ELABORAZIONE_COMPLETATA_CON_ERRORE', null, null, null, 1, '2020-09-21 17:16:57.482', 'SYSTEM', null, null, null, null, 3, 1),
	(6, 'provafile', null, null, 'IN_ATTESA_DI_ELABORAZIONE', 1, null, null, 1, '2020-09-21 17:16:57.482', 'SYSTEM', null, null, null, null, null, 1),
	(7, 'pratiche_unit_test.xlsx','caricamento', null, 'PRATICA_IN_ERRORE', 1, null, null, 1, '2020-09-21 17:16:57.482', 'SYSTEM', null, null, null, null, null, 1),
	(8, 'file_vuoto.xlsx','caricamento', null, 'PRATICA_CREATA_CON_ERRORE', 1, null, null, 1, '2020-09-21 17:16:57.482', 'SYSTEM', null, null, null, null, 7, 1),
	(9, 'validazione_righe_fallita.xlsx','caricamento', null, 'PROCESSO_IN_ERRORE', 1, null, null, 1, '2020-09-21 17:16:57.482', 'SYSTEM', null, null, null, 10, 1, 1),
	(10, 'identificativo_pratica_errato.xlsx','caricamento', null, 'CARICAMENTO_DOCUMENTI_IN_ERRORE', 1, null, null, 1, '2020-09-21 17:16:57.482', 'SYSTEM', null, null, null, null, null, 1),
	(11, 'errore_tag.xlsx','caricamento', 'idPratica5', 'IN_ATTESA_DI_ELABORAZIONE', 1, null, null, 1, '2020-09-21 17:16:57.482', 'SYSTEM', null, null, null, null, 1, 1);

INSERT INTO cosmo_d_tipo_segnalibro (codice, descrizione, dt_inizio_val, dt_fine_val) VALUES 
    ('collocazione-acta', 'Collocazione ACTA', '2020-09-21 17:16:57.482', null),
    ('collocazione-acta2', 'Collocazione ACTA2', '2020-09-21 17:16:57.482', null);
    
INSERT INTO cosmo_d_tipo_notifica (codice, descrizione, dt_inizio_val, dt_fine_val) VALUES 
    ('INFO', 'info', '2021-09-21 17:16:57.482', null);

INSERT INTO cosmo_t_notifica(id, id_fruitore, descrizione, arrivo, scadenza, id_pratica, dt_inserimento, utente_inserimento, dt_ultima_modifica, utente_ultima_modifica, dt_cancellazione, utente_cancellazione, classe, tipo_notifica, url, url_descrizione) VALUES 
    (1, 1, 'notifica1', '2021-09-21 17:16:57.482', null, 1, '2021-09-21 17:16:57.482', 'SYSTEM', null, null, null, null, 'classe1', 'INFO', 'url1', 'url descrizione1'),
    (2, 1, 'notifica2', '2021-09-21 17:16:57.482', null, 1, '2021-09-21 17:16:57.482', 'SYSTEM', null, null, null, null, 'INFO', 'INFO', null, null);

INSERT INTO cosmo_r_notifica_utente_ente (id_utente, id_notifica, data_lettura, dt_inizio_val, dt_fine_val, id_ente, invio_mail, stato_invio_mail) VALUES 
    (1, 1, '2021-09-21 17:16:57.482', '2020-09-21 17:16:57.482', null, 1, true, 'inviato'),
    (1, 2, '2021-09-21 17:16:57.482', '2020-09-21 17:16:57.482', null, 1, true, 'DA_INVIARE');