/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest.impl;

import java.net.URI;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoecm.business.rest.DocumentiApi;
import it.csi.cosmo.cosmoecm.business.service.ContenutoDocumentoService;
import it.csi.cosmo.cosmoecm.business.service.DocumentoService;
import it.csi.cosmo.cosmoecm.business.service.DuplicaDocumentiService;
import it.csi.cosmo.cosmoecm.business.service.FruitoriService;
import it.csi.cosmo.cosmoecm.business.service.SigilloElettronicoService;
import it.csi.cosmo.cosmoecm.business.service.StardasService;
import it.csi.cosmo.cosmoecm.business.service.StiloService;
import it.csi.cosmo.cosmoecm.dto.ContenutoDocumentoResult;
import it.csi.cosmo.cosmoecm.dto.FileContent;
import it.csi.cosmo.cosmoecm.dto.rest.AggiornaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.ContenutiDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.Documenti;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentiResponse;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoInvioStiloRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistamentoResponse;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentiResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RelazioniDocumentoDuplicato;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediApposizioneSigilloRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediSmistamentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmoecm.util.CommonUtils;
import it.csi.cosmo.cosmoecm.util.FilesUtils;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

public class DocumentiApiImpl extends ParentApiImpl implements DocumentiApi {

  private static final String ID_CONTENUTO = "idContenuto";

  private static final String ID_DOCUMENTO = "idDocumento";

  private static final String ID_MESSAGGIO = "identificativoMessaggio";

  private static final String ID_ALIAS = "identificativoAlias";

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "DocumentiApiImpl");

  @Autowired
  private DocumentoService documentoService;

  @Autowired
  private StardasService stardasService;

  @Autowired
  private FruitoriService fruitoriService;

  @Autowired
  private ContenutoDocumentoService contenutoDocumentoService;

  @Autowired
  private DuplicaDocumentiService duplicaDocumentoService;

  @Autowired
  private StiloService stiloService;

  @Autowired
  private SigilloElettronicoService sigilloElettronicoService;

  @Override
  public Response deleteDocumentoId(Integer id, SecurityContext securityContext) {
    Documento documentoDaCancellare = documentoService.cancellaDocumento(id);
    if (documentoDaCancellare != null) {
      return Response.ok(documentoDaCancellare).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response getDocumento(String filter, Boolean export, SecurityContext securityContext) {
    DocumentiResponse documenti = documentoService.getDocumenti(filter, export);
    return Response.ok(documenti).build();
  }

  @Override
  public Response getDocumentoId(Integer id, SecurityContext securityContext) {
    Documento documento = documentoService.getDocumento(id);
    if (documento != null) {
      return Response.ok(documento).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response deleteContenutoDocumentoId(Integer idDocumento, Integer idContenuto,
      SecurityContext securityContext) {

    CommonUtils.require(idDocumento, ID_DOCUMENTO);
    CommonUtils.require(idContenuto, ID_CONTENUTO);

    contenutoDocumentoService.cancellaById(idDocumento.longValue(), idContenuto.longValue(), false);

    return Response.noContent().build();
  }

  @Override
  public Response getContenutoId(Integer idDocumento, Integer idContenuto, Boolean preview,
      SecurityContext securityContext) {
    CommonUtils.require(idDocumento, ID_DOCUMENTO);
    CommonUtils.require(idContenuto, ID_CONTENUTO);

    if (contenutoDocumentoService.isTemporaneo(idDocumento.longValue(), idContenuto.longValue())) {
      // temporaneo, scarico direttamente
      FileContent content = contenutoDocumentoService.getContenutoFisico(idDocumento.longValue(),
          idContenuto.longValue());

      if (Boolean.TRUE.equals(preview)) {
        return FilesUtils.toPreviewResponse(content);
      } else {
        return FilesUtils.toDownloadResponse(content);
      }

    } else {
      // su index, scarico con link diretto
      URI directDownloadLink = contenutoDocumentoService.getLinkDownloadDiretto(
          idDocumento.longValue(), idContenuto.longValue(), Boolean.TRUE.equals(preview));

      //@formatter:off
      return Response.status(Status.FOUND)
          .header("Location", directDownloadLink)
          .header(Constants.HEADERS_PREFIX + "DNF", Boolean.TRUE.toString())
          .build();
      //@formatter:on
    }
  }

  @Override
  public Response postDocumento(CreaDocumentiRequest documento, Long idPratica,
      SecurityContext securityContext) {
    Documenti documentiDaCreare = documentoService.inserisciDocumenti(idPratica, documento);
    if (documentiDaCreare != null) {
      return Response.ok(documentiDaCreare).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response putDocumentoId(Integer id, AggiornaDocumentoRequest body,
      SecurityContext securityContext) {
    Documento documentoDaAggiornare = documentoService.modificaDocumento(body, id);
    if (documentoDaAggiornare != null) {
      return Response.ok(documentoDaAggiornare).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response getContenuti(Integer idDocumento, SecurityContext securityContext) {
    CommonUtils.require(idDocumento, ID_DOCUMENTO);

    ContenutiDocumento content =
        contenutoDocumentoService.getByIdDocumento(idDocumento.longValue());

    return Response.ok(content).build();
  }

  @Override
  public Response postDocumentiIdPraticaRichiediSmistamento(String idPratica,
      RichiediSmistamentoRequest body, SecurityContext securityContext) {
    CommonUtils.require(body, ID_MESSAGGIO);
    stardasService.richiediSmistamento(idPratica, body);
    return Response.ok().build();
  }

  @Override
  public Response postDocumentiIdDocumentoSmistamento(String idDocumento,
      EsitoSmistaDocumentoRequest body, SecurityContext securityContext) {
    logger.info("postDocumentiIdDocumentoSmistamento", "POST called. Printing body");
    CommonUtils.require(idDocumento, ID_DOCUMENTO);
    logger.info("postDocumentiIdDocumentoSmistamento", body.toString());
    try {
      stardasService.impostaEsitoSmistamento(Long.parseLong(idDocumento), body, true);
    } catch (Exception e) {
      logger.error("postDocumentiIdDocumentoSmistamento",
          "Si e' verificato un errore durante l'impostazione dell'esito smistamento", e);
      throw e;
    }
    var esitoResponse = new EsitoSmistamentoResponse();
    try {
      esitoResponse =
          stardasService.creaEsitoSmistamento(Long.parseLong(idDocumento), body.getMessageUUID());
    } catch (Exception e) {
      logger.error("postDocumentiIdDocumentoSmistamento",
          "Si e' verificato un errore durante la creazione dell'esito smistamento", e);
      throw e;
    }
    return Response
        .ok(esitoResponse)
        .build();
  }

  @Override
  public Response putDocumentiIdDocumentoSmistamento(String idDocumento,
      EsitoSmistaDocumentoRequest body, SecurityContext securityContext) {
    logger.info("putDocumentiIdDocumentoSmistamento", "PUT called. Printing body");
    stardasService.impostaEsitoSmistamento(Long.parseLong(idDocumento), body, false);
    CommonUtils.require(idDocumento, ID_DOCUMENTO);
    logger.info("putDocumentiIdDocumentoSmistamento", body.toString());
    return Response
        .ok(stardasService.creaEsitoSmistamento(Long.parseLong(idDocumento), body.getMessageUUID()))
        .build();
  }


  @Override
  public Response getContenutoIdSigned(String idDocumentoExt, String signatureKey, String signature,
      SecurityContext securityContext) {

    ContenutoDocumentoResult contenuto =
        fruitoriService.getContenutoFruitoreSigned(idDocumentoExt, signatureKey, signature);
    if (null != contenuto.getLinkDownloadDiretto()) {
      //@formatter:off
      return Response.status(Status.FOUND)
          .header("Location", contenuto.getLinkDownloadDiretto())
          .header(Constants.HEADERS_PREFIX + "DNF", Boolean.TRUE.toString())
          .build();
      //@formatter:on
    } else {
      return FilesUtils.toDownloadResponse(contenuto);
    }
  }

  @Override
  public Response duplicaDocumenti(Integer idPraticaDa, Integer idPraticaA,
      SecurityContext securityContext) {
    RiferimentoOperazioneAsincrona response =
        duplicaDocumentoService.duplicaDocumenti(idPraticaDa.longValue(), idPraticaA.longValue());
    return Response.status(201).entity(response).build();
  }

  @Override
  public Response duplicaDocumento(Integer idDocumento, Integer idPraticaA,
      RelazioniDocumentoDuplicato body, SecurityContext securityContext) {
    RelazioniDocumentoDuplicato response = duplicaDocumentoService
        .duplicaDocumento(idPraticaA.longValue(),
            idDocumento.longValue(), body);
    return Response.status(201).entity(response).build();
  }
  @Override
  public Response preparaDuplicazione(Integer idPraticaDa, Integer idPraticaA,
      Boolean restituisciDocumenti,
      SecurityContext securityContext) {

    Documenti response = duplicaDocumentoService.preparaDuplicazione(idPraticaDa.longValue(),
        idPraticaA.longValue(), restituisciDocumenti);

    if (restituisciDocumenti != null && Boolean.TRUE.equals(restituisciDocumenti)) {
      return Response.status(200).entity(response).build();
    } else {
      return Response.status(200).build();
    }
  }

  @Override
  public Response postDocumentiIdPraticaEsposizione(Long idPratica,
      PreparaEsposizioneDocumentiRequest body, SecurityContext securityContext) {

    PreparaEsposizioneDocumentiResponse response =
        documentoService.preparaEsposizioneDocumenti(idPratica, body);

    return Response.status(201).entity(response).build();
  }

  @Override
  public Response getTipologieDocumentiSalvati(String tipologieDocumenti,
      SecurityContext securityContext) {
    var response = documentoService.getTipologieDocumentiSalvati(tipologieDocumenti);
    return Response.ok(response).build();
  }

  @Override
  public Response getDocumentiInvioStilo(String filter,
      SecurityContext securityContext) {

    List<Documento> documenti =
        stiloService.getDocumentiInvioStilo(filter);
    return Response.ok(documenti).build();
  }

  @Override
  public Response postDocumentiIdDocumentoEsitoInvioStilo(String idDocumento,
      EsitoInvioStiloRequest body, SecurityContext securityContext) {
    stiloService.impostaEsitoInvioStilo(idDocumento, body);
    return Response.ok().build();
  }

  @Override
  public Response postDocumentiIdDocumentoInvioDocumentoStilo(String idDocumento, Long idUd,
      SecurityContext securityContext) {
    stiloService.impostaInvioDocumentoStilo(idDocumento, idUd);
    return Response.ok().build();
  }

  @Override
  public Response getIndexContent(Integer idDocumento, Integer idContenuto,
      SecurityContext securityContext) {
    CommonUtils.require(idDocumento, ID_DOCUMENTO);
    CommonUtils.require(idContenuto, ID_CONTENUTO);

    if (contenutoDocumentoService.isTemporaneo(idDocumento.longValue(), idContenuto.longValue())) {
      // temporaneo, scarico direttamente
      FileContent content = contenutoDocumentoService.getContenutoFisico(idDocumento.longValue(),
          idContenuto.longValue());

      return Response.ok(content.getContent()).build();

    } else {
      // su index, scarico con link diretto
      var content = contenutoDocumentoService.getContenutoIndex(idDocumento.longValue(), idContenuto.longValue());

      return Response.ok(content).build();
    }
  }

  @Override
  public Response postDocumentiIdPraticaRichiediApposizioneSigillo(String idPratica,
      RichiediApposizioneSigilloRequest body, SecurityContext securityContext) {
    CommonUtils.require(body, ID_MESSAGGIO);
    CommonUtils.require(body, ID_ALIAS);
    sigilloElettronicoService.richiediApposizioneSigillo(idPratica, body);
    return Response.ok().build();
  }



}
