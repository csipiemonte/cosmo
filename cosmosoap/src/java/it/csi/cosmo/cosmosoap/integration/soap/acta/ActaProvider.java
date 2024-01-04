/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.cosmosoap.integration.soap.acta.backoffice.BackOfficeService;
import it.csi.cosmo.cosmosoap.integration.soap.acta.backoffice.BackOfficeServicePort;
import it.csi.cosmo.cosmosoap.integration.soap.acta.document.DocumentService;
import it.csi.cosmo.cosmosoap.integration.soap.acta.document.DocumentServicePort;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaContextBuildingException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.management.ManagementService;
import it.csi.cosmo.cosmosoap.integration.soap.acta.management.ManagementServicePort;
import it.csi.cosmo.cosmosoap.integration.soap.acta.navigation.NavigationService;
import it.csi.cosmo.cosmosoap.integration.soap.acta.navigation.NavigationServicePort;
import it.csi.cosmo.cosmosoap.integration.soap.acta.object.ObjectService;
import it.csi.cosmo.cosmosoap.integration.soap.acta.object.ObjectServicePort;
import it.csi.cosmo.cosmosoap.integration.soap.acta.officialbook.OfficialBookService;
import it.csi.cosmo.cosmosoap.integration.soap.acta.officialbook.OfficialBookServicePort;
import it.csi.cosmo.cosmosoap.integration.soap.acta.relationships.RelationshipsService;
import it.csi.cosmo.cosmosoap.integration.soap.acta.relationships.RelationshipsServicePort;
import it.csi.cosmo.cosmosoap.integration.soap.acta.repository.RepositoryService;
import it.csi.cosmo.cosmosoap.integration.soap.acta.repository.RepositoryServicePort;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;
import it.csi.wso2.apiman.oauth2.helper.GenericWrapperFactoryBean;
import it.csi.wso2.apiman.oauth2.helper.OauthHelper;
import it.csi.wso2.apiman.oauth2.helper.TokenRetryManager;
import it.csi.wso2.apiman.oauth2.helper.extra.cxf.CxfImpl;
import it.doqui.acta.acaris.common.AcarisFaultType;

/**
 *
 */

public class ActaProvider implements Closeable {

  protected static final CosmoLogger log = LoggerFactory
      .getLogger(LogCategory.BUSINESS_LOG_CATEGORY, ActaProvider.class.getSimpleName());

  private Boolean useApiManager;
  private String tokenEndpoint;
  private String apiManagerConsumerKey;
  private String apiManagerConsumerSecret;

  private String repositoryEndpoint;
  private String backofficeEndpoint;
  private String objectEndpoint;
  private String managementEndpoint;
  private String documentEndpoint;
  private String navigationEndpoint;
  private String relationshipsEndpoint;
  private String officialBookEndpoint;
  private String rootEndpoint;

  private RepositoryServicePort repositoryServicePort;
  private BackOfficeServicePort backofficeServicePort;
  private ObjectServicePort objectServicePort;
  private ManagementServicePort managementServicePort;
  private DocumentServicePort documentServicePort;
  private NavigationServicePort navigationServicePort;
  private RelationshipsServicePort relationshipsServicePort;
  private OfficialBookServicePort officialBookServicePort;

  private ActaProvider(Builder builder) {
    this.useApiManager = builder.useApiManager;
    this.tokenEndpoint = builder.tokenEndpoint;
    this.apiManagerConsumerKey = builder.apiManagerConsumerKey;
    this.apiManagerConsumerSecret = builder.apiManagerConsumerSecret;
    this.repositoryEndpoint = builder.repositoryEndpoint;
    this.backofficeEndpoint = builder.backofficeEndpoint;
    this.objectEndpoint = builder.objectEndpoint;
    this.managementEndpoint = builder.managementEndpoint;
    this.documentEndpoint = builder.documentEndpoint;
    this.navigationEndpoint = builder.navigationEndpoint;
    this.relationshipsEndpoint = builder.relationshipsEndpoint;
    this.officialBookEndpoint = builder.officialBookEndpoint;
    this.rootEndpoint = builder.rootEndpoint;
  }

  public ActaProvider() {
    // NOP
  }

  @Override
  public void close() throws IOException {
    // NOP
  }

  private String getEndpoint(String specific, String postfix) {
    if (!StringUtils.isBlank(specific)) {
      return specific;
    }
    if (StringUtils.isBlank(postfix)) {
      throw new IllegalArgumentException();
    }
    if (StringUtils.isBlank(rootEndpoint)) {
      throw new ActaContextBuildingException("No rootUrl specified and no endpoint specified");
    }
    if (postfix.startsWith("http://") || postfix.startsWith("https://")) {
      return postfix;
    }
    return this.rootEndpoint + "/" + postfix;
  }

  private boolean doUseApiManager() {
    if (this.useApiManager != null) {
      return this.useApiManager.booleanValue();
    }
    return !StringUtils.isBlank(this.apiManagerConsumerKey)
        && !StringUtils.isBlank(this.apiManagerConsumerSecret);
  }

  public synchronized OfficialBookServicePort getOfficialBookService() {
    if (this.officialBookServicePort == null) {
      final var method = "getOfficialBookServicePort";

      try {
        String endpoint = getEndpoint(this.officialBookEndpoint, "DOC_ACTA_officialbook-T/1.0");
        QName qname = new QName("officialbookservice.acaris.acta.doqui.it", "OfficialBookService");
        OfficialBookServicePort servicePort;

        if (this.doUseApiManager()) {

          OfficialBookService service = new OfficialBookService(
              new ClassPathResource("/wsdl/ActaOfficialBook.wsdl").getURL(), qname);

          servicePort = service.getOfficialBookServicePort();
          servicePort = wrap(servicePort, endpoint, OfficialBookServicePort.class);

        } else {
          OfficialBookService service = new OfficialBookService(new URL(endpoint), qname);
          servicePort = service.getOfficialBookServicePort();
        }

        this.officialBookServicePort = servicePort;

      } catch (Exception e) {
        log.error(method, "Error building acta OfficialBook service", e);
        throw ExceptionUtils.toChecked(e);
      }
    }
    return this.officialBookServicePort;
  }

  public synchronized RelationshipsServicePort getRelationshipsService() {
    if (this.relationshipsServicePort == null) {
      final var method = "getRelationshipsServicePort";

      try {
        String endpoint = getEndpoint(this.relationshipsEndpoint, "DOC_ACTA_relationships-T/1.0");
        QName qname =
            new QName("relationshipsservice.acaris.acta.doqui.it", "RelationshipsService");
        RelationshipsServicePort servicePort;

        if (this.doUseApiManager()) {

          RelationshipsService service = new RelationshipsService(
              new ClassPathResource("/wsdl/ActaRelationships.wsdl").getURL(), qname);

          servicePort = service.getRelationshipsServicePort();
          servicePort = wrap(servicePort, endpoint, RelationshipsServicePort.class);

        } else {
          RelationshipsService service = new RelationshipsService(new URL(endpoint), qname);
          servicePort = service.getRelationshipsServicePort();
        }

        this.relationshipsServicePort = servicePort;

      } catch (Exception e) {
        log.error(method, "Error building acta relationships service", e);
        throw ExceptionUtils.toChecked(e);
      }
    }
    return this.relationshipsServicePort;
  }

  public synchronized NavigationServicePort getNavigationService() {
    if (this.navigationServicePort == null) {
      final var method = "getNavigationServicePort";

      try {
        String endpoint = getEndpoint(this.navigationEndpoint, "DOC_ACTA_navigation-T/1.0");
        QName qname = new QName("navigationservice.acaris.acta.doqui.it", "NavigationService");
        NavigationServicePort servicePort;

        if (this.doUseApiManager()) {

          NavigationService service = new NavigationService(
              new ClassPathResource("/wsdl/ActaNavigation.wsdl").getURL(), qname);

          servicePort = service.getNavigationServicePort();
          servicePort = wrap(servicePort, endpoint, NavigationServicePort.class);

        } else {
          NavigationService service = new NavigationService(new URL(endpoint), qname);
          servicePort = service.getNavigationServicePort();
        }

        this.navigationServicePort = servicePort;

      } catch (Exception e) {
        log.error(method, "Error building acta navigation service", e);
        throw ExceptionUtils.toChecked(e);
      }
    }
    return this.navigationServicePort;
  }

  public synchronized ManagementServicePort getManagementService() {
    if (this.managementServicePort == null) {
      final var method = "getManagementServicePort";

      try {
        String endpoint = getEndpoint(this.managementEndpoint, "DOC_ACTA_management-T/1.0");
        QName qname = new QName("managementservice.acaris.acta.doqui.it", "ManagementService");
        ManagementServicePort servicePort;

        if (this.doUseApiManager()) {

          ManagementService service = new ManagementService(
              new ClassPathResource("/wsdl/ActaManagement.wsdl").getURL(), qname);

          servicePort = service.getManagementServicePort();
          servicePort = wrap(servicePort, endpoint, ManagementServicePort.class);

        } else {
          ManagementService service =
              new ManagementService(new URL(endpoint), qname);
          servicePort = service.getManagementServicePort();
        }

        this.managementServicePort = servicePort;

      } catch (Exception e) {
        log.error(method, "Error building acta management service", e);
        throw ExceptionUtils.toChecked(e);
      }
    }
    return this.managementServicePort;
  }

  public synchronized RepositoryServicePort getRepositoryService() {
    if (this.repositoryServicePort == null) {
      final var method = "getRepositoryServicePort";
      try {
        String endpoint = getEndpoint(this.repositoryEndpoint, "DOC_ACTA_repository-T/1.0");
        QName qname = new QName("repositoryservice.acaris.acta.doqui.it", "RepositoryService");
        RepositoryServicePort servicePort;

        if (this.doUseApiManager()) {

          RepositoryService service = new RepositoryService(
              new ClassPathResource("/wsdl/ActaRepository.wsdl").getURL(), qname);

          servicePort = service.getRepositoryServicePort();
          servicePort = wrap(servicePort, endpoint, RepositoryServicePort.class);

        } else {
          RepositoryService service =
              new RepositoryService(new URL(endpoint), qname);
          servicePort = service.getRepositoryServicePort();
        }

        this.repositoryServicePort = servicePort;

      } catch (Exception e) {
        log.error(method, "Error building acta repository service", e);
        throw ExceptionUtils.toChecked(e);
      }
    }
    return this.repositoryServicePort;
  }

  public synchronized BackOfficeServicePort getBackofficeService() {
    if (this.backofficeServicePort == null) {
      final var method = "getBackOfficeServicePort";
      try {
        String endpoint = getEndpoint(this.backofficeEndpoint, "DOC_ACTA_backoffice-T/1.0");
        QName qname = new QName("backofficeservice.acaris.acta.doqui.it", "BackOfficeService");
        BackOfficeServicePort servicePort;

        if (this.doUseApiManager()) {

          BackOfficeService service = new BackOfficeService(
              new ClassPathResource("/wsdl/ActaBackoffice.wsdl").getURL(), qname);

          servicePort = service.getBackOfficeServicePort();
          servicePort = wrap(servicePort, endpoint, BackOfficeServicePort.class);

        } else {
          BackOfficeService service =
              new BackOfficeService(new URL(endpoint), qname);
          servicePort = service.getBackOfficeServicePort();
        }

        this.backofficeServicePort = servicePort;

      } catch (Exception e) {
        log.error(method, "Error building acta backoffice service", e);
        throw ExceptionUtils.toChecked(e);
      }
    }
    return this.backofficeServicePort;
  }

  public synchronized ObjectServicePort getObjectService() {
    if (this.objectServicePort == null) {
      final var method = "getObjectServicePort";
      try {
        String endpoint = getEndpoint(this.objectEndpoint, "DOC_ACTA_object-T/1.0");
        QName qname = new QName("objectservice.acaris.acta.doqui.it", "ObjectService");
        ObjectServicePort servicePort;

        if (this.doUseApiManager()) {

          ObjectService service =
              new ObjectService(new ClassPathResource("/wsdl/ActaObject.wsdl").getURL(), qname);

          servicePort = service.getObjectServicePort();
          servicePort = wrap(servicePort, endpoint, ObjectServicePort.class);

        } else {
          ObjectService service = new ObjectService(new URL(endpoint), qname);
          servicePort = service.getObjectServicePort();
        }

        this.objectServicePort = servicePort;

      } catch (Exception e) {
        log.error(method, "Error building acta object service", e);
        throw ExceptionUtils.toChecked(e);
      }
    }
    return this.objectServicePort;
  }

  public synchronized DocumentServicePort getDocumentService() {
    if (this.documentServicePort == null) {
      final var method = "getDocumentServicePort";
      try {
        String endpoint = getEndpoint(this.documentEndpoint, "DOC_ACTA_document-T/1.0");
        QName qname = new QName("documentservice.acaris.acta.doqui.it", "DocumentService");
        DocumentServicePort servicePort;

        if (this.doUseApiManager()) {

          DocumentService service =
              new DocumentService(new ClassPathResource("/wsdl/ActaDocument.wsdl").getURL(), qname);

          servicePort = service.getDocumentServicePort();
          servicePort = wrap(servicePort, endpoint, DocumentServicePort.class);

        } else {
          DocumentService service = new DocumentService(new URL(endpoint), qname);
          servicePort = service.getDocumentServicePort();
        }

        this.documentServicePort = servicePort;

      } catch (Exception e) {
        log.error(method, "Error building acta document service", e);
        throw ExceptionUtils.toChecked(e);
      }
    }
    return this.documentServicePort;
  }

  @SuppressWarnings("unchecked")
  private <T> T wrap(Object servicePort, String endpoint, Class<T> wrappedClass)
      throws ClassNotFoundException {

    OauthHelper oauth = new OauthHelper(this.tokenEndpoint, this.apiManagerConsumerKey,
        this.apiManagerConsumerSecret);
    TokenRetryManager trm = new TokenRetryManager();
    trm.setOauthHelper(oauth);

    CxfImpl cxfImpl = new CxfImpl();
    trm.setWsProvider(cxfImpl);

    GenericWrapperFactoryBean gwfb = new GenericWrapperFactoryBean();
    gwfb.setEndPoint(endpoint);
    gwfb.setWrappedInterface(wrappedClass);
    gwfb.setPort(servicePort);
    gwfb.setTokenRetryManager(trm);

    return (T) gwfb.create();
  }

  protected void logAcarisFault(String nomeMetodo, AcarisFaultType fault) {
    log.error(nomeMetodo, "AcarisFault - Error code: " + fault.getErrorCode());
    log.error(nomeMetodo, "AcarisFault - Property name: " + fault.getPropertyName());
    log.error(nomeMetodo, "AcarisFault - Technical info: " + fault.getTechnicalInfo());
    log.error(nomeMetodo, "AcarisFault - Class name: " + fault.getClassName());
    log.error(nomeMetodo, "AcarisFault - Exception type: " + fault.getExceptionType());
    log.error(nomeMetodo, "AcarisFault - Object id: " + fault.getObjectId());
  }

  /**
   * Creates builder to build {@link ActaProvider}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link ActaProvider}.
   */
  public static final class Builder {
    private Boolean useApiManager;
    private String tokenEndpoint;
    private String apiManagerConsumerKey;
    private String apiManagerConsumerSecret;
    private String repositoryEndpoint;
    private String backofficeEndpoint;
    private String objectEndpoint;
    private String managementEndpoint;
    private String documentEndpoint;
    private String navigationEndpoint;
    private String relationshipsEndpoint;
    private String officialBookEndpoint;
    private String rootEndpoint;

    private Builder() {}

    public Builder withUseApiManager(Boolean useApiManager) {
      this.useApiManager = useApiManager;
      return this;
    }

    public Builder withTokenEndpoint(String tokenEndpoint) {
      this.tokenEndpoint = tokenEndpoint;
      return this;
    }

    public Builder withApiManagerConsumerKey(String apiManagerConsumerKey) {
      this.apiManagerConsumerKey = apiManagerConsumerKey;
      return this;
    }

    public Builder withApiManagerConsumerSecret(String apiManagerConsumerSecret) {
      this.apiManagerConsumerSecret = apiManagerConsumerSecret;
      return this;
    }

    public Builder withOfficialBookEndpoint(String officialBookEndpoint) {
      this.officialBookEndpoint = officialBookEndpoint;
      return this;
    }

    public Builder withRepositoryEndpoint(String repositoryEndpoint) {
      this.repositoryEndpoint = repositoryEndpoint;
      return this;
    }

    public Builder withBackofficeEndpoint(String backofficeEndpoint) {
      this.backofficeEndpoint = backofficeEndpoint;
      return this;
    }

    public Builder withObjectEndpoint(String objectEndpoint) {
      this.objectEndpoint = objectEndpoint;
      return this;
    }

    public Builder withManagementEndpoint(String managementEndpoint) {
      this.managementEndpoint = managementEndpoint;
      return this;
    }

    public Builder withDocumentEndpoint(String documentEndpoint) {
      this.documentEndpoint = documentEndpoint;
      return this;
    }

    public Builder withNavigationEndpoint(String navigationEndpoint) {
      this.navigationEndpoint = navigationEndpoint;
      return this;
    }

    public Builder withRelationshipsEndpoint(String relationshipsEndpoint) {
      this.relationshipsEndpoint = relationshipsEndpoint;
      return this;
    }

    public Builder withRootEndpoint(String rootEndpoint) {
      this.rootEndpoint = rootEndpoint;
      return this;
    }

    public ActaProvider build() {
      return new ActaProvider(this);
    }
  }

}
