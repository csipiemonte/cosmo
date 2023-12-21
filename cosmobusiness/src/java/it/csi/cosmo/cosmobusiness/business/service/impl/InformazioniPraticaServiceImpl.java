/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.enums.EsitoVerificaFirma;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.InformazioniPraticaService;
import it.csi.cosmo.cosmobusiness.business.service.MotoreJsonDinamicoService;
import it.csi.cosmo.cosmobusiness.business.service.StatoPraticaService;
import it.csi.cosmo.cosmobusiness.config.ErrorMessages;
import it.csi.cosmo.cosmobusiness.dto.rest.DocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InformazioniPratica;
import it.csi.cosmo.cosmobusiness.dto.rest.UtenteFruitore;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.SandboxFactory;
import it.csi.cosmo.cosmobusiness.integration.mapper.CallbackFruitoriMapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class InformazioniPraticaServiceImpl extends StatoPraticaWrapper
implements InformazioniPraticaService {

  private static final String FIELD_ID_DOCUMENTO = "idDocumento";
  private static final String FIELD_ID_PRATICA = "idPratica";

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  @Autowired
  private CallbackFruitoriMapper callbackFruitoriMapper;

  @Autowired
  private MotoreJsonDinamicoService motoreJsonDinamicoService;

  @Autowired
  private StatoPraticaService statoPraticaService;

  @Override
  @Transactional(readOnly = true)
  public InformazioniPratica getInfoPratica(String idPratica, String idDocumento) {
    final var method = "getInfoPratica";
    logger.debug(method, "richiesto info pratica {}", idPratica);

    // verifico i dati di input
    ValidationUtils.require(idPratica, FIELD_ID_PRATICA);
    ValidationUtils.require(idDocumento, FIELD_ID_DOCUMENTO);

    if (!StringUtils.isNumeric(idPratica)) {
      final String message =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, FIELD_ID_PRATICA);
      logger.error(method, message);
      throw new BadRequestException(message);
    }

    if (!StringUtils.isNumeric(idDocumento)) {
      final String message =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, FIELD_ID_DOCUMENTO);
      logger.error(method, message);
      throw new BadRequestException(message);
    }
    CosmoTPratica pratica =
        cosmoTPraticaRepository.findOneByField(CosmoTPratica_.id, Long.valueOf(idPratica))
        .orElseThrow(() -> new NotFoundException("Pratica non trovata"));

    CosmoTDocumento documento =
        cosmoTDocumentoRepository.findOneByField(CosmoTDocumento_.id, Long.valueOf(idDocumento))
        .orElseThrow(() -> new NotFoundException("Documento non trovato"));

    // costruisco il payload
    return getInfoPratica(pratica, documento);
  }

  private InformazioniPratica getInfoPratica(CosmoTPratica pratica, CosmoTDocumento documento) {
    InformazioniPratica payload = callbackFruitoriMapper.toInformazioniPraticaDTO(pratica);

    if (pratica.getMetadati() != null && !pratica.getMetadati().isBlank()) {
      try {
        Object metadati =
            ObjectUtils.getDataMapper().readValue(pratica.getMetadati(), Object.class);
        payload.setMetadati(metadati);
      } catch (IOException e) {
        final String message = "Errore nel parsing dei metadati della pratica";
        logger.error("getInfoPratica", message);
        throw new InternalServerException(message);
      }
    }

    // fetch attivita nell ordine desiderato
    Pageable pageableAttivita = new PageRequest(0, 9999,
        new Sort(new Order(Direction.DESC, CosmoTEntity_.dtInserimento.getName()),
            new Order(Direction.DESC, CosmoTEntity_.dtCancellazione.getName())));

    Page<CosmoTAttivita> attivita = cosmoTAttivitaRepository
        .findByField(CosmoTAttivita_.cosmoTPratica, pratica, pageableAttivita);

    payload.setAttivita(attivita.getContent().stream().filter(a -> a.getParent() == null)
        .map(this::toAttivita).collect(Collectors.toCollection(LinkedList::new)));

    payload.setDocumentoPerSmistamento(toDocumentoPerSmistamento(documento));

    // fetch dei commenti sulla pratica
    payload.setCommenti(fetchMessaggiPratica(pratica.getProcessInstanceId()));

    // arrichisco utenti che al momento hanno solo il codice fiscale con una singola query massiva
    enrichUtenti(payload);

    return payload;
  }

  private void enrichUtenti(InformazioniPratica payload) {
    Collection<UtenteFruitore> utenti = new ArrayList<>();

    if (payload.getAttivita() != null) {
      payload.getAttivita().forEach(attivita -> utenti.addAll(this.estraiUtentiAttivita(attivita)));
    }

    if (payload.getCommenti() != null) {
      payload.getCommenti().forEach(u -> {
        if (u.getUtente() != null) {
          utenti.add(u.getUtente());
        }
      });
    }

    if (payload.getUtenteCreazione() != null) {
      utenti.add(payload.getUtenteCreazione());
    }

    enrichUtenti(utenti);
  }

  private DocumentoFruitore toDocumentoPerSmistamento(CosmoTDocumento documento) {
    if (documento == null) {
      return null;
    }
    DocumentoFruitore mapped = callbackFruitoriMapper.toDocumentoSbustatoDTO(documento);

    CosmoTContenutoDocumento contenuto = getContenutoDocumentoEffettivo(documento);
    if (contenuto != null) {
      if (contenuto.getFormatoFile() != null) {
        mapped.setMimeType(contenuto.getFormatoFile().getMimeType());
      }

      Timestamp now = Timestamp.from(Instant.now());
      mapped.setFirme(safe(contenuto.getInfoVerificaFirme()).stream()
          .filter(f -> f.getEsito() != null
          && f.getEsito().getCodice().equals(EsitoVerificaFirma.VALIDA.name()))
          .sorted((dto1, dto2) -> ObjectUtils.coalesce(dto1.getDtApposizione(), now)
              .compareTo(ObjectUtils.coalesce(dto2.getDtApposizione(), now)))
          .map(this::toFirma).collect(Collectors.toCollection(LinkedList::new)));
    }

    mapped.setIdCosmo(documento.getId().toString());

    return mapped;
  }

  @Override
  public Object elaboraInformazioniPratica(Long id, GetElaborazionePraticaRequest body) {
    final var method = "elaboraInformazioniPratica";

    ValidationUtils.require(id, "id");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    var pratica = this.cosmoTPraticaRepository.findOne(id);
    if (pratica == null) {
      throw new NotFoundException("Pratica non trovata");
    }

    // creo sandbox
    var sandboxedVirtualNode =
        SandboxFactory.buildStatoPraticaSandbox(pratica, statoPraticaService);

    // applica la trasformazione
    JsonNode compiled =
        motoreJsonDinamicoService.eseguiMappatura(body.getMappatura(), sandboxedVirtualNode);

    if (logger.isDebugEnabled()) {
      logger.debug(method, "risultato trasformazione: {}", compiled.toString());
    }

    return compiled;
  }

  @Override
  public Object getContestoInformazioniPratica(Long id) {

    ValidationUtils.require(id, "id");

    var pratica = this.cosmoTPraticaRepository.findOne(id);
    if (pratica == null) {
      throw new NotFoundException("Pratica non trovata");
    }

    // creo sandbox
    var root = SandboxFactory.buildStatoPraticaSandbox(pratica, statoPraticaService);
    callAllGetters(root, new ArrayList<>());
    return root;
  }

  private void callAllGetters(Object bean, List<Object> alreadyPassed) {
    if (bean == null) {
      return;
    }
    for (var o : alreadyPassed) {
      if (o == bean) {
        return;
      }
    }

    alreadyPassed.add(bean);

    final var method = "callAllGetters";
    try {
      Arrays
      .asList(org.springframework.beans.BeanUtils.getPropertyDescriptors(bean.getClass()))
      .stream()
      // filter out properties with setters only
      .filter(pd -> pd.getReadMethod() != null && pd.getReadMethod().canAccess(bean))
      .forEach(pd -> {
        if (pd.getPropertyType().equals(Class.class)) {
          return;
        }
        // var isCollection = Collection.class.isAssignableFrom(pd.getPropertyType())
        // || Map.class.isAssignableFrom(pd.getPropertyType());
        // var isOwnType = pd.getPropertyType().getName().startsWith("it.csi");
        // if (!isCollection && !isOwnType) {
        // return;
        // }
        // bean property name
        try {
          var readMethod = pd.getReadMethod();
          if (readMethod.isAnnotationPresent(Deprecated.class)) {
            this.logger.debug(method, "skipped reading deprecated field "
                + bean.getClass().getSimpleName() + "." + pd.getDisplayName());
            return;
          }
          this.logger.debug(method,
              "reading " + bean.getClass().getSimpleName() + "." + pd.getDisplayName());
          var value = readMethod.invoke(bean);
          if (value instanceof Collection<?>) {
            ((Collection<?>) value).forEach(sv -> callAllGetters(sv, alreadyPassed));
          } else if (value instanceof Map<?, ?>) {
            ((Map<?, ?>) value).entrySet()
            .forEach(sv -> callAllGetters(sv.getValue(), alreadyPassed));
          } else {
            callAllGetters(value, alreadyPassed);
          }
        } catch (Exception e) {
          // replace this with better error handling
          this.logger.warn(method, "cannot read field " + pd.getName(), e);
        }
      });
    } catch (Exception e) {
      throw new InternalServerException(e.getMessage(), e);
    }
  }

}
