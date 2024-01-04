/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.soap.acta;

import java.security.InvalidParameterException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.dto.FiltroRicercaDocumentiActaDTO;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaQueryFailedException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaRuntimeException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaServiceConnectionException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.ActaClientContext;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.IdentitaActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.AggregazioneActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ClassificazioneActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ContenutoDocumentoFisicoActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.DocumentoActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.DocumentoFisicoActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.DocumentoSempliceActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.EntitaActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.FascicoloRealeAnnualeActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ProtocolloActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.RegistrazioneClassificazioniActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.SerieFascicoliActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.SerieTipologicaDocumenti;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.TitolarioActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.VoceTitolarioActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.VolumeSerieTipologicaDocumenti;
import it.csi.cosmo.cosmosoap.integration.soap.acta.object.AcarisException;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;
import it.doqui.acta.acaris.common.EnumQueryOperator;


public class ActaClientImpl implements ActaClient {

  protected static final CosmoLogger log = LoggerFactory
      .getLogger(LogCategory.BUSINESS_LOG_CATEGORY, ActaClientImpl.class.getSimpleName());

  private ActaClientContext contesto;

  private ActaProvider facadeProvider;

  private ActaClientExecutor executor;

  private ActaClientImpl(Builder builder) {
    this.init(builder);
  }

  private synchronized void init(Builder builder) {

    this.facadeProvider = builder.facadeProvider;

    //@formatter:off
    contesto = ActaClientHelper.loadContext(
        this.facadeProvider,
        builder.repositoryName,
        builder.codiceProtocollista,
        builder.codiceStruttura,
        builder.codiceAreaOrganizzativaOmogenea,
        builder.codiceNodo,
        builder.codiceApplicazione
    );
    //@formatter:on

    //@formatter:off
    executor =
        ActaClientExecutor.builder()
        .withContesto(contesto)
        .withFacade(facadeProvider)
        .build();
    //@formatter:on
  }

  public ActaClientImpl() {}

  @Override
  public ActaProvider getProvider() {
    return this.facadeProvider;
  }

  @Override
  public void close() throws Exception {
    if (facadeProvider != null) {
      facadeProvider.close();
    }
  }

  @Override
  public boolean testResouces() throws ActaServiceConnectionException {

    try {
      return ((facadeProvider.getObjectService() != null)
          && (facadeProvider.getRepositoryService() != null));
    } catch (Exception e) {
      log.error("testConnectionToServices", "Impossibile testare il layer: " + e, e);
      throw new ActaServiceConnectionException("Test del layer fallito: " + e, e);
    }
  }

  @Override
  public VoceTitolarioActa findVoceTitolarioByClassificazioneEstesa(String classificazione) {
    return executor.findVoceTitolarioByClassificazioneEstesa(classificazione);
  }

  @Override
  public SerieFascicoliActa findSerieFascicoliByCodice(String codice) {
    return executor.findSerieFascicoliByCodice(codice);
  }

  @Override
  public FascicoloRealeAnnualeActa findFascicoloRealeAnnualeById(String id) {
    return executor.findFascicoloRealeAnnualeById(id);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends EntitaActa> T create(String parentId, T entity) {
    if (entity == null) {
      throw new InvalidParameterException("Entity is required");
    }

    if (entity instanceof DocumentoActa) {
      return (T) executor.create(parentId, (DocumentoActa) entity);
    } else if (entity instanceof AggregazioneActa) {
      return (T) executor.create(parentId, (AggregazioneActa) entity);
    } else {
      throw new RuntimeException("unrecognized ACTA entity " + entity.getClass().getName());
    }
  }

  @Override
  public <T extends EntitaActa> T find(String id, Class<T> targetClass) {
    return executor.find(id, targetClass);
  }

  @Override
  public <T extends EntitaActa> T findBy(String property, String value, Class<T> targetClass) {
    return executor.findBy(property, value, targetClass);
  }

  @Override
  public <T extends EntitaActa> T findBy(String property, EnumQueryOperator operator, String value,
      Class<T> targetClass) {
    return executor.findBy(property, operator, value, targetClass);
  }

  @Override
  public <T extends EntitaActa> T update(T entity) {
    return executor.update(entity);
  }

  @Override
  public ActaClientContext getContesto() {
    return contesto;
  }

  @Override
  public SerieTipologicaDocumenti findSerieTipologicaDocumentiByCodice(String codice) {
    return executor.findSerieTipologicaDocumentiByCodice(codice);
  }

  @Override
  public VolumeSerieTipologicaDocumenti findVolumeSerieTipologicaDocumentiByParoleChiave(
      String paroleChiave) {
    return executor.findVolumeSerieTipologicaDocumentiByParoleChiave(paroleChiave);
  }

  @Override
  public List<DocumentoFisicoActa> findDocumentiFisiciByIdDocumentoSemplice(String id) {
    try {
      return executor.findDocumentiFisiciByIdDocumentoSemplice(id);
    } catch (Exception e) {
      throw new ActaQueryFailedException("Error getting physicals document by id", e);
    }
  }

  @Override
  public ContenutoDocumentoFisicoActa findContenutoPrimarioByIdDocumentoFisico(String id) {
    try {
      return executor.findContenutoPrimarioByIdDocumentoFisico(id);
    } catch (Exception e) {
      throw new ActaQueryFailedException("Error getting physical content by physical document id",
          e);
    }
  }

  @Override
  public Page<DocumentoSempliceActa> findDocumentiSemplici(FiltroRicercaDocumentiActaDTO filtri,
      Pageable pageable) {
    try {
      return executor.findDocumentiSemplici(filtri, pageable);
    } catch (ActaRuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new ActaQueryFailedException("Error getting documents", e);
    }
  }

  @Override
  public List<DocumentoSempliceActa> findDocumentiSempliciByParolaChiave(String paroleChiave,
      int maxItems, int skipCount) {
    try {
      return executor.findDocumentiSempliciByParolaChiave(paroleChiave, maxItems, skipCount);
    } catch (ActaRuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new ActaQueryFailedException("Error getting documents by parole chiave", e);
    }
  }

  @Override
  public List<IdentitaActa> findIdentitaByCodiceFiscaleUtente(String codiceFiscale) {
    try {
      return executor.findIdentitaByCodiceFiscaleUtente(codiceFiscale);
    } catch (Exception e) {
      throw new ActaQueryFailedException("Error getting user identities by CF", e);
    }
  }

  @Override
  public List<ClassificazioneActa> findClassificazioniByIdDocumentoSemplice(String id) {
    try {
      return executor.findClassificazioniByIdDocumentoSemplice(id);
    } catch (Exception e) {
      throw new ActaQueryFailedException("Error getting ClassificazioniByIdDocumentoSemplice", e);
    }
  }


  @Override
  public ProtocolloActa findProtocolloById(String id) {
    try {
      return executor.findProtocolloById(id);
    } catch (Exception e) {
      throw new ActaQueryFailedException("Error getting protocollo by id", e);
    }
  }

  @Override
  public Page<RegistrazioneClassificazioniActa> findRegistrazioni(
      FiltroRicercaDocumentiActaDTO filtri, Pageable pageable) {
    try {
      return executor.findRegistrazioni(filtri, pageable);
    } catch (Exception e) {
      throw new ActaQueryFailedException("Error getting Registrazioni", e);
    }
  }

  @Override
  public List<DocumentoSempliceActa> findDocumentiSempliciByIdClassificazione(String id) {
    try {
      return executor.findDocumentiSempliciByIdClassificazione(id);
    } catch (Exception e) {
      throw new ActaQueryFailedException("Error getting DocumentiFisiciByIdClassificazione", e);
    }
  }

  @Override
  public String findObjectIDStrutturaAggregativa(String indiceClassificazioneEstesa) {
    try {
      return executor.findObjectIDStrutturaAggregativa(indiceClassificazioneEstesa);
    } catch (Exception e) {
      throw new ActaQueryFailedException("Error getting findObjectIDStrutturaAggregativa", e);
    }
  }

  @Override
  public TitolarioActa findTitolario(String codice) {
    try {
      return executor.findTitolario(codice);
    } catch (AcarisException e) {
      throw new ActaQueryFailedException("Error getting findTitolario", e);
    }
  }

  @Override
  public Page<VoceTitolarioActa> findVociTitolarioInAlberatura(String chiaveTitolario, String chiavePadre, Pageable pageable) {
    try {
      return executor.findVociTitolarioInAlberatura(chiaveTitolario, chiavePadre, pageable);
    } catch (AcarisException e) {
      throw new ActaQueryFailedException("Error getting findTitolario", e);
    }
  }

  /**
   * Creates builder to build {@link ActaClientImpl}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link ActaClientImpl}.
   */
  public static final class Builder {
    private ActaProvider facadeProvider;
    private String repositoryName;
    private String codiceProtocollista;
    private String codiceStruttura;
    private String codiceAreaOrganizzativaOmogenea;
    private String codiceNodo;
    private String codiceApplicazione;

    private Builder() {}

    public Builder withFacadeProvider(ActaProvider facadeProvider) {
      this.facadeProvider = facadeProvider;
      return this;
    }

    public Builder withRepositoryName(String repositoryName) {
      this.repositoryName = repositoryName;
      return this;
    }

    public Builder withCodiceProtocollista(String codiceProtocollista) {
      this.codiceProtocollista = codiceProtocollista;
      return this;
    }

    public Builder withCodiceStruttura(String codiceStruttura) {
      this.codiceStruttura = codiceStruttura;
      return this;
    }

    public Builder withCodiceAreaOrganizzativaOmogenea(String codiceAreaOrganizzativaOmogenea) {
      this.codiceAreaOrganizzativaOmogenea = codiceAreaOrganizzativaOmogenea;
      return this;
    }

    public Builder withCodiceNodo(String codiceNodo) {
      this.codiceNodo = codiceNodo;
      return this;
    }

    public Builder withCodiceApplicazione(String codiceApplicazione) {
      this.codiceApplicazione = codiceApplicazione;
      return this;
    }

    public ActaClientImpl build() {
      return new ActaClientImpl(this);
    }
  }

}
