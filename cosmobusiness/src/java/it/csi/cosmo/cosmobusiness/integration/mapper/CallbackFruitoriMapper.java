/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.mapper;

import java.time.OffsetDateTime;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTCommento;
import it.csi.cosmo.common.entities.CosmoTCredenzialiAutenticazioneFruitore;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTInfoVerificaFirma;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTSchemaAutenticazioneFruitore;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AttivitaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.CommentoResponseWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.CredenzialiAutenticazioneFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.DocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.FirmaDocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.GetPraticaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.InformazioniPratica;
import it.csi.cosmo.cosmobusiness.dto.rest.MessaggioFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.SchemaAutenticazioneFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.SottoAttivitaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.StatoPraticaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.TipoPraticaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.UtenteFruitore;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
uses = {DateFormatsMapper.class})
public interface CallbackFruitoriMapper {

  GetPraticaFruitoreResponse toGetPraticaFruitoreResponse(
      AggiornaStatoPraticaRequest contenutoCallback);

  @Mapping(target = "id", source = "idPraticaExt")
  @Mapping(target = "codiceIpaEnte", source = "ente.codiceIpa")
  @Mapping(target = "tipo", source = "tipo")
  @Mapping(target = "oggetto", source = "oggetto")
  @Mapping(target = "riassunto", source = "riassunto")
  @Mapping(target = "utenteCreazione", source = "utenteCreazionePratica")
  @Mapping(target = "dataCreazione", source = "dataCreazionePratica")
  @Mapping(target = "dataAggiornamento", source = "dtUltimaModifica")
  @Mapping(target = "dataCambioStato", source = "dataCambioStato")
  @Mapping(target = "dataFine", source = "dataFinePratica")
  @Mapping(target = "stato", source = "stato")
  @Mapping(target = "attivita", ignore = true)
  @Mapping(target = "documenti", ignore = true)
  @Mapping(target = "commenti", ignore = true)
  @Mapping(target = "metadati", ignore = true)
  AggiornaStatoPraticaRequest toDTO(CosmoTPratica pratica);

  @Mapping(target = "codiceIpaEnte", source = "ente.codiceIpa")
  @Mapping(target = "descrizione", source = "ente.nome")
  @Mapping(target = "tipo", source = "tipo")
  @Mapping(target = "oggetto", source = "oggetto")
  @Mapping(target = "stato", source = "stato")
  @Mapping(target = "riassunto", source = "riassunto")
  @Mapping(target = "utenteCreazione", source = "utenteCreazionePratica")
  @Mapping(target = "dataCreazione", source = "dataCreazionePratica")
  @Mapping(target = "dataFine", source = "dataFinePratica")
  @Mapping(target = "dataCambioStato", source = "dataCambioStato")
  @Mapping(target = "dataAggiornamento", source = "dtUltimaModifica")
  @Mapping(target = "attivita", ignore = true)
  @Mapping(target = "documentoPerSmistamento", ignore = true)
  @Mapping(target = "commenti", ignore = true)
  @Mapping(target = "metadati", ignore = true)
  InformazioniPratica toInformazioniPraticaDTO(CosmoTPratica input);

  @Mapping(target = "codice", source = "codice")
  @Mapping(target = "descrizione", source = "descrizione")
  TipoPraticaFruitore toDTO(CosmoDTipoPratica tipo);

  @Mapping(target = "codice", source = "codice")
  @Mapping(target = "descrizione", source = "descrizione")
  StatoPraticaFruitore toDTO(CosmoDStatoPratica stato);

  @Mapping(target = "nome", source = "nome")
  @Mapping(target = "descrizione", source = "descrizione")
  @Mapping(target = "dataInizio", source = "dtInserimento")
  @Mapping(target = "dataFine", source = "dtCancellazione")
  @Mapping(target = "assegnazione", ignore = true)
  @Mapping(target = "sottoAttivita", ignore = true)
  @Mapping(target = "messaggiCollaboratori", ignore = true)
  AttivitaFruitore toDTO(CosmoTAttivita attivita);

  @Mapping(target = "nome", source = "nome")
  @Mapping(target = "descrizione", source = "descrizione")
  @Mapping(target = "dataInizio", source = "dtInserimento")
  @Mapping(target = "dataFine", source = "dtCancellazione")
  @Mapping(target = "assegnazione", ignore = true)
  SottoAttivitaFruitore toSottoAttivitaDTO(CosmoTAttivita attivita);

  @Mapping(target = "id", source = "idDocumentoExt")
  @Mapping(target = "idPadre", source = "idDocParentExt")
  @Mapping(target = "titolo", source = "titolo")
  @Mapping(target = "descrizione", source = "descrizione")
  @Mapping(target = "autore", source = "autore")
  @Mapping(target = "mimeType", ignore = true)
  @Mapping(target = "firme", ignore = true)
  @Mapping(target = "archiviazione", ignore = true)
  @Mapping(target = "tipo", source = "tipo")
  @Mapping(target = "stato", source = "stato")
  @Mapping(target = "refURL", ignore = true)
  @Mapping(target = "dimensione", ignore = true)
  @Mapping(target = "nomeFile", ignore = true)
  @Mapping(target = "idCosmo", ignore = true)
  DocumentoFruitore toDTO(CosmoTDocumento documento);

  @Mapping(target = "idPadre", source = "documentoPadre.id")
  @Mapping(target = "titolo", source = "titolo")
  @Mapping(target = "descrizione", source = "descrizione")
  @Mapping(target = "autore", source = "autore")
  @Mapping(target = "mimeType", ignore = true)
  @Mapping(target = "firme", ignore = true)
  @Mapping(target = "tipo", source = "tipo")
  @Mapping(target = "stato", source = "stato")
  @Mapping(target = "refURL", ignore = true)
  @Mapping(target = "archiviazione", ignore = true)
  @Mapping(target = "dimensione", ignore = true)
  @Mapping(target = "nomeFile", ignore = true)
  @Mapping(target = "idCosmo", ignore = true)
  DocumentoFruitore toDocumentoSbustatoDTO(CosmoTDocumento documento);

  @Mapping(target = "nome", source = "nome")
  @Mapping(target = "cognome", source = "cognome")
  @Mapping(target = "codiceFiscale", source = "codiceFiscale")
  UtenteFruitore toDTO(CosmoTUtente utente);

  @Mapping(target = "utente", source = "author")
  @Mapping(target = "messaggio", source = "message")
  @Mapping(target = "timestamp", source = "time")
  MessaggioFruitore toDTO(CommentoResponseWrapper messaggio);


  @Mapping(target = "utente", source = "utenteCreatore")
  @Mapping(target = "messaggio", source = "messaggio")
  @Mapping(target = "timestamp", source = "dataCreazione")
  MessaggioFruitore toDTO(CosmoTCommento commento);

  @Mapping(target = "data", source = "dtApposizione")
  @Mapping(target = "firmatario", source = "firmatario")
  @Mapping(target = "organizzazione", source = "organizzazione")
  FirmaDocumentoFruitore toDTO(CosmoTInfoVerificaFirma infoVerificaFirma);


  @Mapping(source = "credenziali", target = "credenziali",
      qualifiedByName = "credenzialiValideUnivoche")
  SchemaAutenticazioneFruitore toDTO(CosmoTSchemaAutenticazioneFruitore input);


  @Mapping(source = "password", target = "password", qualifiedByName = "obfuscate")
  @Mapping(source = "clientSecret", target = "clientSecret", qualifiedByName = "obfuscate")
  CredenzialiAutenticazioneFruitore toDTO(CosmoTCredenzialiAutenticazioneFruitore input);

  @Named("credenzialiValideUnivoche")
  default CredenzialiAutenticazioneFruitore toDTOCredenzialiValide(
      List<CosmoTCredenzialiAutenticazioneFruitore> input) {
    if (input == null || input.isEmpty()) {
      return null;
    }
    CosmoTCredenzialiAutenticazioneFruitore found = null;
    for (CosmoTCredenzialiAutenticazioneFruitore candidate : input) {
      if (candidate.nonCancellato()) {
        if (found != null) {
          throw new InternalServerException(
              "Credenziali valide multiple per uno schema di autenticazione");
        } else {
          found = candidate;
        }
      }
    }
    return toDTO(found);
  }

  @Named("obfuscate")
  default String obfuscate(String input) {
    if (StringUtils.isBlank(input)) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      sb.append("*");
    }
    return sb.toString();
  }

  default UtenteFruitore toUtente(String codiceFiscale) {
    if (StringUtils.isBlank(codiceFiscale)) {
      return null;
    }
    UtenteFruitore output = new UtenteFruitore();
    output.setCodiceFiscale(codiceFiscale);
    return output;
  }

  default OffsetDateTime toOffsetDateTime(String iso) {
    if (StringUtils.isBlank(iso)) {
      return null;
    }
    return OffsetDateTime.parse(iso);
  }

}
