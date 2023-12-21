/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.components;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.jboss.resteasy.spi.ReaderException;
import org.jboss.resteasy.spi.WriterException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpStatusCodeException;
import it.csi.cosmo.common.dto.common.ErrorMessageDTO;
import it.csi.cosmo.common.exception.ManagedException;
import it.csi.cosmo.common.feignclient.exception.FeignClientClientErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientRestException;
import it.csi.cosmo.common.feignclient.exception.FeignClientServerErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientStatusCodeException;
import it.csi.cosmo.common.feignclient.model.FeignClientRequestContext;
import it.csi.cosmo.common.fileshare.exceptions.FileShareUploadExcelException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;

/**
 * Classe per gestire gli errori
 */
@SuppressWarnings("deprecation")
public abstract class CosmoErrorHandler implements ExceptionMapper<Exception> {

  private CosmoLogger logger;

  public CosmoErrorHandler(String loggingCategory) {
    logger = new CosmoLogger(loggingCategory, "CosmoErrorHandler");
  }

  @Produces("application/json")
  @Override
  public Response toResponse(Exception e) {
    return convert(e);
  }

  /**
   * Converte l'eccezione lanciata dall'applicativo nella risposta da restituire a chi ha effettuato
   * la chiamata
   *
   * @param e e' l'eccezione che viene restituita
   * @return una risposta che contiene il corretto stato ed un messaggio significativo
   */
  protected Response convert(Exception e) {
    logger.error("convert", "handling exception", e);

    ErrorMessageDTO converted;
    if (e instanceof ManagedException) {
      var convertedME = (ManagedException) e;
      if (convertedME.getResponse() != null) {

        return Response.status(convertedME.getStatus()).entity(convertedME.getResponse()).build();
      }
    }

    converted = convertException(e);
    return buildResponse(converted);
  }

  public ErrorMessageDTO convertException(Throwable e) { // NOSONAR

    if (e instanceof ManagedException) {
      return convertManagedException((ManagedException) e);

    } else if (e instanceof DataIntegrityViolationException) {
      return convertDataIntegrityViolationException((DataIntegrityViolationException) e);

    } else if (e instanceof MethodConstraintViolationException) { // NOSONAR
      return convertMethodConstraintViolationException((MethodConstraintViolationException) e); // NOSONAR

    } else if (e instanceof BadRequestException) {
      return convertBadRequestException((BadRequestException) e);

    } else if (e instanceof NotAuthorizedException) {
      return convertUnauthorizedException((NotAuthorizedException) e);

    } else if (e instanceof NotFoundException) {
      return convertNotFoundException((NotFoundException) e);

    } else if (e instanceof InternalServerErrorException) {
      return convertInternalServerErrorException((InternalServerErrorException) e);

    } else if (e instanceof FeignClientClientErrorException) {
      return convertFeignClientClientErrorException((FeignClientClientErrorException) e);

    } else if (e instanceof FeignClientServerErrorException) {
      return convertFeignClientServerErrorException((FeignClientServerErrorException) e);

    } else if (e instanceof FeignClientStatusCodeException) {
      return convertFeignClientStatusCodeException((FeignClientStatusCodeException) e);

    } else if (e instanceof FeignClientRestException) {
      return convertFeignClientRestException((FeignClientRestException) e);

    } else if (e instanceof HttpStatusCodeException) {
      return convertHttpStatusCodeException((HttpStatusCodeException) e);

    } else if (e instanceof NotAllowedException) {
      return convertMethodNotAllowedException((NotAllowedException) e);

    } else if (e instanceof NotAcceptableException) {
      return convertNotAcceptableException((NotAcceptableException) e);

    } else if (e instanceof ReaderException) {
      return convertReaderException((ReaderException) e);

    } else if (e instanceof WriterException) {
      return convertWriterException((WriterException) e);

    }
    else if (e instanceof FileShareUploadExcelException) {
        return convertFileShareUploadExcelException((FileShareUploadExcelException) e);

      }
    else {
      return convertOtherException(e);
    }
  }

  
  /**
   * Gestisce un'eccezione di tipo FileShareUploadExcelException
   *
   * @param e e' l'eccezione di tipo DataIntegrityViolationException
   * @return una risposta con stato 400 e messaggio riportante l'informazione dell'errore
   */

  private ErrorMessageDTO convertFileShareUploadExcelException(FileShareUploadExcelException e) {
	  return ErrorMessageDTO.builder().withErrore(exceptionToString(e))
		        .withStatus(HttpStatus.BAD_REQUEST.value()).withCodice(HttpStatus.BAD_REQUEST.name())
		        .withMessaggio(costruisciMessaggio(e.getMessage()))
		        .build();
	  }

/**
   * Gestisce un'eccezione di tipo DataIntegrityViolationException
   *
   * @param e e' l'eccezione di tipo DataIntegrityViolationException
   * @return una risposta con stato 400 e messaggio riportante l'informazione dell'errore
   */
  private ErrorMessageDTO convertDataIntegrityViolationException(
      DataIntegrityViolationException e) {
    return ErrorMessageDTO.builder().withErrore(exceptionToString(e))
        .withStatus(HttpStatus.BAD_REQUEST.value()).withCodice(HttpStatus.BAD_REQUEST.name())
        .withMessaggio(costruisciMessaggio("I dati inviati non sono validi.", e.getMessage()))
        .build();
  }

  /**
   * Gestisce un'eccezione di tipo MethodConstraintViolationException
   *
   * @param e e' l'eccezione di tipo MethodConstraintViolationException
   * @return una risposta con stato 400 e messaggio riportante l'informazione dell'errore
   */
  private ErrorMessageDTO convertMethodConstraintViolationException(
      MethodConstraintViolationException e) { // NOSONAR
    return ErrorMessageDTO.builder().withErrore(exceptionToString(e))
        .withStatus(HttpStatus.BAD_REQUEST.value()).withCodice(HttpStatus.BAD_REQUEST.name())
        .withMessaggio(costruisciMessaggio("I dati inviati non sono validi.", e.getMessage()))
        .build();
  }

  /**
   * Gestisce un'eccezione di tipo ManagedException
   *
   * @param e e' l'eccezione di tipo ManagedException
   * @return una risposta il cui stato ed il messaggio dell'errore sono quelli definiti
   *         dall'eccezione
   */
  private ErrorMessageDTO convertManagedException(ManagedException e) {

    return ErrorMessageDTO.builder().withErrore(exceptionToString(e)).withStatus(e.getStatus())
        .withCodice(e.getCodice()).withMessaggio(e.getMessage()).build();
  }

  /**
   * Gestisce un'eccezione di tipo BadRequestException
   *
   * @param e e' l'eccezione di tipo BadRequestException
   * @return una risposta con stato 400 e messaggio riportante l'informazione dell'errore
   */
  private ErrorMessageDTO convertBadRequestException(BadRequestException e) {
    return ErrorMessageDTO.builder().withErrore(exceptionToString(e))
        .withStatus(HttpStatus.BAD_REQUEST.value()).withCodice(HttpStatus.BAD_REQUEST.name())
        .withMessaggio(costruisciMessaggio("La richiesta inoltrata non e' formalmente corretta.",
            e.getMessage()))
        .build();
  }

  /**
   * Gestisce un'eccezione di tipo NotAuthorizedException
   *
   * @param e e' l'eccezione di tipo NotAuthorizedException
   * @return una risposta con stato 401 e messaggio riportante l'informazione dell'errore
   */
  private ErrorMessageDTO convertUnauthorizedException(NotAuthorizedException e) {
    return
        ErrorMessageDTO.builder().withErrore(exceptionToString(e))
            .withStatus(HttpStatus.UNAUTHORIZED.value()).withCodice(HttpStatus.UNAUTHORIZED.name())
            .withMessaggio(costruisciMessaggio(
                "Per compiere l'operazione richiesta occorre autenticarsi.", e.getMessage()))
        .build();
  }

  /**
   * Gestisce un'eccezione di tipo InternalServerErrorException
   *
   * @param e e' l'eccezione di tipo InternalServerErrorException
   * @return una risposta con stato 500 e messaggio riportante l'informazione dell'errore
   */
  private ErrorMessageDTO convertInternalServerErrorException(
      InternalServerErrorException e) {
    return ErrorMessageDTO.builder().withErrore(exceptionToString(e))
            .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).withCodice("INTERNAL")
            .withMessaggio(
                costruisciMessaggio("Si e' verificato un errore imprevisto.", e.getMessage()))
        .build();
  }

  /**
   * Gestisce un'eccezione di tipo NotAllowedException
   *
   * @param e e' l'eccezione di tipo NotAllowedException
   * @return una risposta con stato 405 e messaggio riportante l'informazione dell'errore
   */
  private ErrorMessageDTO convertMethodNotAllowedException(NotAllowedException e) {
    return ErrorMessageDTO.builder().withErrore(exceptionToString(e))
        .withStatus(HttpStatus.METHOD_NOT_ALLOWED.value()).withCodice("METHOD_NOT_ALLOWED")
        .withMessaggio(costruisciMessaggio(
            "L'operazione richiesta non e' disponibile per la risorsa desiderata.", e.getMessage()))
        .build();
  }

  /**
   * Gestisce un'eccezione di tipo NotAcceptableException
   *
   * @param e e' l'eccezione di tipo NotAcceptableException
   * @return una risposta con stato 406 e messaggio riportante l'informazione dell'errore
   */
  private ErrorMessageDTO convertNotAcceptableException(NotAcceptableException e) {
    return ErrorMessageDTO.builder().withErrore(exceptionToString(e))
        .withStatus(HttpStatus.NOT_ACCEPTABLE.value()).withCodice("NOT_ACCEPTABLE")
        .withMessaggio(costruisciMessaggio(
            "Il server non puo' produrre una risposta nel formato richiesto.", e.getMessage()))
        .build();
  }

  /**
   * Gestisce un'eccezione di tipo NotFoundException
   *
   * @param e e' l'eccezione di tipo NotFoundException
   * @return una risposta con stato 404 e messaggio riportante l'informazione dell'errore
   */
  private ErrorMessageDTO convertNotFoundException(NotFoundException e) {
    return ErrorMessageDTO.builder().withErrore(exceptionToString(e))
        .withStatus(HttpStatus.NOT_FOUND.value()).withCodice("NOT_FOUND")
        .withMessaggio(
            costruisciMessaggio("La risorsa richiesta non e' stata trovata.", e.getMessage()))
        .build();
  }

  /**
   * Gestisce un'eccezione di tipo ReaderException
   *
   * @param e e' l'eccezione di tipo ReaderException
   * @return una risposta con stato 400 e messaggio riportante l'informazione dell'errore
   */
  private ErrorMessageDTO convertReaderException(ReaderException e) {
    return ErrorMessageDTO.builder().withErrore(exceptionToString(e))
        .withStatus(HttpStatus.BAD_REQUEST.value()).withCodice("READ_ERROR")
        .withMessaggio(costruisciMessaggio(
            "Il server non puo' interpretare la richiesta nel formato specificato.",
            e.getMessage()))
        .build();
  }

  /**
   * Gestisce un'eccezione di tipo WriterException
   *
   * @param e e' l'eccezione di tipo WriterException
   * @return una risposta con stato 500 e messaggio riportante l'informazione dell'errore
   */
  private ErrorMessageDTO convertWriterException(WriterException e) {
    return ErrorMessageDTO.builder().withErrore(exceptionToString(e))
        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).withCodice("WRITE_ERROR")
        .withMessaggio(costruisciMessaggio(
            "Il server non e' riuscito ad inoltrare la risposta nel formato specificato.",
            e.getMessage()))
        .build();
  }

  /**
   * Gestisce un'eccezione di tipo Exception
   *
   * @param e e' l'eccezione di tipo Exception
   * @return una risposta con stato 500 e messaggio riportante l'informazione dell'errore
   */
  private ErrorMessageDTO convertOtherException(Throwable e) {
    return ErrorMessageDTO.builder().withErrore(exceptionToString(e))
            .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).withCodice("INTERNAL")
            .withMessaggio(
                costruisciMessaggio("Si e' verificato un errore imprevisto.", e.getMessage()))
        .build();
  }

  private ErrorMessageDTO convertFeignClientRestException(FeignClientRestException e) {
  //@formatter:off
    return 
        ErrorMessageDTO.builder().withErrore(exceptionToString(e))
            .withStatus(HttpStatus.SERVICE_UNAVAILABLE.value()).withCodice("SERVICE_UNAVAILABLE")
            .withMessaggio(
                costruisciMessaggio("Si e' verificato un errore imprevisto in una chiamata interna.", e.getMessage()))
            .build();
    //@formatter:on
  }

  private ErrorMessageDTO convertFeignClientClientErrorException(
      FeignClientClientErrorException e) {
    return convertVariousFeignClientStatusCodeException(e.getInnerException(),
        e.getRequestContext());
  }

  private ErrorMessageDTO convertFeignClientServerErrorException(
      FeignClientServerErrorException e) {
    return convertVariousFeignClientStatusCodeException(e.getInnerException(),
        e.getRequestContext());
  }

  private ErrorMessageDTO convertFeignClientStatusCodeException(
      FeignClientStatusCodeException e) {
    return convertVariousFeignClientStatusCodeException(e.getInnerException(),
        e.getRequestContext());
  }

  private ErrorMessageDTO convertVariousFeignClientStatusCodeException(
      HttpStatusCodeException e,
      FeignClientRequestContext rq) {
    final var method = "handleFeignClientStatusCodeException";
    var decoded = attemptDecode(e.getResponseBodyAsString(), e);
    var responseIsJson = MediaType.APPLICATION_JSON.equals(e.getResponseHeaders().getContentType());

    if (decoded != null) {
      logger.error(method,
          "Errore " + e.getStatusCode().value() + " nella chiamata interna in " + rq.getMethod()
              + " a " + rq.getUri() + ". Il server remoto ha risposto: "
              + ObjectUtils.represent(decoded));

      //@formatter:off
      return ErrorMessageDTO.builder()
          .withErrore(exceptionToString(e, decoded.getErrore()))
          .withStatus(decoded.getStatus())
          .withCodice(decoded.getCode())
          .withMessaggio(costruisciMessaggio(
              "Errore " + e.getStatusCode().value() + " nella chiamata interna in " + rq.getMethod() + " a " + rq.getUri() + ": " + decoded.getTitle(), e.getMessage()))
          .build();
      //@formatter:on
    } else if (responseIsJson) {
      logger.error(method,
          "Errore " + e.getStatusCode().value() + " nella chiamata interna in " + rq.getMethod()
              + " a " + rq.getUri() + ". Il server remoto ha risposto: "
              + e.getResponseBodyAsString());

      //@formatter:off
      return ErrorMessageDTO.builder()
          .withErrore(exceptionToString(e, e.getResponseBodyAsString()))
          .withStatus(e.getStatusCode().value())
          .withCodice(e.getStatusCode().name().toUpperCase())
          .withMessaggio(costruisciMessaggio(
              "Errore " + e.getStatusCode().value() + " nella chiamata interna a " + rq.getUri(), e.getMessage()))
          .build();
      //@formatter:on
    } else {
      logger.error(method, "Errore " + e.getStatusCode().value() + " nella chiamata interna in "
          + rq.getMethod() + " a " + rq.getUri() + ". Nessun dettaglio di errore ricavabile.");

      //@formatter:off
      return ErrorMessageDTO.builder()
          .withErrore(exceptionToString(e))
          .withStatus(e.getStatusCode().value())
          .withCodice(e.getStatusCode().name().toUpperCase())
          .withMessaggio(costruisciMessaggio(
              "Errore " + e.getStatusCode().value() + " nella chiamata interna a " + rq.getUri(), e.getMessage()))
          .build();
      //@formatter:on
    }
  }

  private ErrorMessageDTO convertHttpStatusCodeException(HttpStatusCodeException e) {
    final var method = "handleHttpStatusCodeException";
    var decoded = attemptDecode(e.getResponseBodyAsString(), e);
    var responseIsJson = MediaType.APPLICATION_JSON.equals(e.getResponseHeaders().getContentType());
    if (decoded != null) {
      logger.error(method,
          "Errore " + e.getStatusCode().value()
              + " nella chiamata HTTP. Il server remoto ha risposto: "
              + ObjectUtils.represent(decoded));

      //@formatter:off
      return ErrorMessageDTO.builder()
          .withErrore(exceptionToString(e, decoded.getErrore()))
          .withStatus(decoded.getStatus())
          .withCodice(decoded.getCode())
          .withMessaggio(costruisciMessaggio(
              "Errore " + e.getStatusCode().value() + " nella chiamata HTTP: " + decoded.getTitle(), e.getMessage()))
          .build();
      //@formatter:on
    } else if (responseIsJson) {
      logger.error(method, "Errore " + e.getStatusCode().value()
          + " nella chiamata HTTP. Il server remoto ha risposto: " + e.getResponseBodyAsString());

      //@formatter:off
      return ErrorMessageDTO.builder()
          .withErrore(exceptionToString(e, e.getResponseBodyAsString()))
          .withStatus(e.getStatusCode().value())
          .withCodice(e.getStatusCode().name().toUpperCase())
          .withMessaggio(costruisciMessaggio(
              "Errore " + e.getStatusCode().value() + " nella chiamata HTTP", e.getMessage()))
          .build();
      //@formatter:on
    } else {
      logger.error(method, "Errore " + e.getStatusCode().value()
          + " nella chiamata HTTP. Nessun dettaglio di errore ricavabile.");

      //@formatter:off
      return ErrorMessageDTO.builder()
          .withErrore(exceptionToString(e))
          .withStatus(e.getStatusCode().value())
          .withCodice(e.getStatusCode().name().toUpperCase())
          .withMessaggio(costruisciMessaggio(
              "Errore " + e.getStatusCode().value() + " nella chiamata HTTP", e.getMessage()))
          .build();
      //@formatter:on
    }
  }

  private ErrorMessageDTO attemptDecode(String raw,
      HttpStatusCodeException originalException) {
    // attempt decode as cosmo error
    try {
      ErrorMessageDTO decoded = ObjectUtils.fromJson(raw, ErrorMessageDTO.class);
      if (!StringUtils.isEmpty(decoded.getCode()) && !StringUtils.isEmpty(decoded.getTitle())
          && decoded.getStatus() != null && decoded.getStatus() >= 400) {
        return decoded;
      }
    } catch (Exception e) {
      // l'oggetto di risposta non e' probabilmente un ErrorMessageDTO
    }

    // attempt decode as flowable error
    try {
      @SuppressWarnings("unchecked")
      Map<String, String> decoded = ObjectUtils.fromJson(raw, HashMap.class);

      if (decoded != null && !StringUtils.isEmpty(decoded.get("message"))
          && !StringUtils.isEmpty(decoded.get("exception"))) {
        return ErrorMessageDTO.builder().withCodice(decoded.get("message").toUpperCase())
            .withMessaggio(decoded.get("exception"))
            .withStatus(originalException.getRawStatusCode()).build();
      }
    } catch (Exception e) {
      // l'oggetto di risposta non e' probabilmente un errore di flowable
    }

    return null;
  }

  /**
   * Crea la risposta da restituire in base al dto che e' stato creato dall'eccezione
   *
   * @param errorMessage e' il dto generato con le informazioni dell'eccezione restituita
   * @return la risposta da restituire al chiamante con stato, codice e messaggio definiti dal dto
   */
  private Response buildResponse(ErrorMessageDTO errorMessage) {

    String messaggio = resolvePlaceholders(errorMessage.getTitle());

    return Response.status(errorMessage.getStatus())
        .entity(ErrorMessageDTO.builder().withErrore(errorMessage.getErrore())
            .withStatus(errorMessage.getStatus()).withCodice(errorMessage.getCode())
            .withMessaggio(messaggio).build())
        .type("application/json").build();

  }

  /**
   * Costruisce il messaggio dell'errore in base al messaggio dell'eccezione, aggiungendo se
   * necessario, altre informazioni
   *
   * @param input e' l'insieme delle informazioni che devono costituire il messaggio finale
   * @return e' il messaggio finale
   */
  private String costruisciMessaggio(String... input) {
    return Arrays.stream(input).filter(o -> !StringUtils.isEmpty(o)).findFirst().orElse(null);
  }

  /**
   * Se presente un placeholder, sostituisce il placeholder con il nominativo dell'utente o con
   * sconosciuto
   *
   * @param messaggio e' il messaggio che puo' contenere il placeholder o meno
   * @return messaggio di input, con il placeholder opportunamente valorizzato
   */
  protected String resolvePlaceholders(String messaggio) {
    // To be overridden

    return messaggio;
  }

  protected abstract boolean exposeExceptions();

  private String exceptionToString(Throwable e, String... additionals) {
    if (e == null) {
      return null;
    }

    if (!exposeExceptions()) {
      return null;
    }

    StringBuilder output = new StringBuilder();

    //@formatter:off
    output
    .append(e.getClass().getName() + ": " + e.getMessage())
    .append("\n")
    .append(ExceptionUtils.getStackTrace(e))
    .append("\n");
    //@formatter:on

    int cap = 10;
    while (e.getCause() != null && cap-- > 0) {
      e = e.getCause();

      //@formatter:off
      output
      .append("\n--- CAUSED BY: ").append(e.getClass().getName() + ": " + e.getMessage())
      .append("\n")
      .append(ExceptionUtils.getStackTrace(e))
      .append("\n");
      //@formatter:on
    }

    for (String additional : additionals) {
      if (StringUtils.isEmpty(additional)) {
        continue;
      }

      //@formatter:off
      output
      .append("\n--- REMOTE ERROR: ")
      .append("\n")
      .append(additional)
      .append("\n");
      //@formatter:on
    }

    return output.toString();
  }
}


