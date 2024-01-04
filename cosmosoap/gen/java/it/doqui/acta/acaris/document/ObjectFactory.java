
package it.doqui.acta.acaris.document;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import it.doqui.acta.acaris.common.AcarisContentStreamType;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.common.PrincipalIdType;
import it.doqui.acta.acaris.common.PropertiesType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.doqui.acta.acaris.document package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldComposizioneId_QNAME = new QName("", "composizioneId");
    private final static QName _TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldMultiplo_QNAME = new QName("", "multiplo");
    private final static QName _TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldDocumentoFisico_QNAME = new QName("", "documentoFisico");
    private final static QName _TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldTipoDocFisicoId_QNAME = new QName("", "tipoDocFisicoId");
    private final static QName _TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldContentStream_QNAME = new QName("", "contentStream");
    private final static QName _TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldContenutoFisico_QNAME = new QName("", "contenutoFisico");
    private final static QName _TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldRimandareOperazioneSbustamento_QNAME = new QName("", "rimandareOperazioneSbustamento");
    private final static QName _TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldRepositoryId_QNAME = new QName("", "repositoryId");
    private final static QName _TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldPlaceHolderId_QNAME = new QName("", "placeHolderId");
    private final static QName _TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldPrincipalId_QNAME = new QName("", "principalId");
    private final static QName _TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldAnnotazione_QNAME = new QName("", "annotazione");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.doqui.acta.acaris.document
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UploadContenutoFisicoResponse }
     * 
     */
    public UploadContenutoFisicoResponse createUploadContenutoFisicoResponse() {
        return new UploadContenutoFisicoResponse();
    }

    /**
     * Create an instance of {@link TrasformaDocumentoPlaceholderInDocumentoElettronico }
     * 
     */
    public TrasformaDocumentoPlaceholderInDocumentoElettronico createTrasformaDocumentoPlaceholderInDocumentoElettronico() {
        return new TrasformaDocumentoPlaceholderInDocumentoElettronico();
    }

    /**
     * Create an instance of {@link CreaDocumentoFisicoInDocumentRootResponse }
     * 
     */
    public CreaDocumentoFisicoInDocumentRootResponse createCreaDocumentoFisicoInDocumentRootResponse() {
        return new CreaDocumentoFisicoInDocumentRootResponse();
    }

    /**
     * Create an instance of {@link GetRappresentazioneDocumentoResponse }
     * 
     */
    public GetRappresentazioneDocumentoResponse createGetRappresentazioneDocumentoResponse() {
        return new GetRappresentazioneDocumentoResponse();
    }

    /**
     * Create an instance of {@link UploadContenutoFisico }
     * 
     */
    public UploadContenutoFisico createUploadContenutoFisico() {
        return new UploadContenutoFisico();
    }

    /**
     * Create an instance of {@link CreaDocumentoFisicoInDocumentRoot }
     * 
     */
    public CreaDocumentoFisicoInDocumentRoot createCreaDocumentoFisicoInDocumentRoot() {
        return new CreaDocumentoFisicoInDocumentRoot();
    }

    /**
     * Create an instance of {@link TrasformaDocumentoPlaceholderInDocumentoElettronicoResponse }
     * 
     */
    public TrasformaDocumentoPlaceholderInDocumentoElettronicoResponse createTrasformaDocumentoPlaceholderInDocumentoElettronicoResponse() {
        return new TrasformaDocumentoPlaceholderInDocumentoElettronicoResponse();
    }

    /**
     * Create an instance of {@link CreaDocumentoResponse }
     * 
     */
    public CreaDocumentoResponse createCreaDocumentoResponse() {
        return new CreaDocumentoResponse();
    }

    /**
     * Create an instance of {@link CreaDocumento }
     * 
     */
    public CreaDocumento createCreaDocumento() {
        return new CreaDocumento();
    }

    /**
     * Create an instance of {@link GetRappresentazioneDocumento }
     * 
     */
    public GetRappresentazioneDocumento createGetRappresentazioneDocumento() {
        return new GetRappresentazioneDocumento();
    }

    /**
     * Create an instance of {@link TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld }
     * 
     */
    public TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld createTrasformaDocumentoPlaceHolderInDocumentoElettronicoOld() {
        return new TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "composizioneId", scope = TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class)
    public JAXBElement<Integer> createTrasformaDocumentoPlaceHolderInDocumentoElettronicoOldComposizioneId(Integer value) {
        return new JAXBElement<Integer>(_TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldComposizioneId_QNAME, Integer.class, TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "multiplo", scope = TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class)
    public JAXBElement<Boolean> createTrasformaDocumentoPlaceHolderInDocumentoElettronicoOldMultiplo(Boolean value) {
        return new JAXBElement<Boolean>(_TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldMultiplo_QNAME, Boolean.class, TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertiesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "documentoFisico", scope = TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class)
    public JAXBElement<PropertiesType> createTrasformaDocumentoPlaceHolderInDocumentoElettronicoOldDocumentoFisico(PropertiesType value) {
        return new JAXBElement<PropertiesType>(_TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldDocumentoFisico_QNAME, PropertiesType.class, TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "tipoDocFisicoId", scope = TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class)
    public JAXBElement<Integer> createTrasformaDocumentoPlaceHolderInDocumentoElettronicoOldTipoDocFisicoId(Integer value) {
        return new JAXBElement<Integer>(_TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldTipoDocFisicoId_QNAME, Integer.class, TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AcarisContentStreamType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "contentStream", scope = TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class)
    public JAXBElement<AcarisContentStreamType> createTrasformaDocumentoPlaceHolderInDocumentoElettronicoOldContentStream(AcarisContentStreamType value) {
        return new JAXBElement<AcarisContentStreamType>(_TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldContentStream_QNAME, AcarisContentStreamType.class, TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertiesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "contenutoFisico", scope = TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class)
    public JAXBElement<PropertiesType> createTrasformaDocumentoPlaceHolderInDocumentoElettronicoOldContenutoFisico(PropertiesType value) {
        return new JAXBElement<PropertiesType>(_TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldContenutoFisico_QNAME, PropertiesType.class, TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "rimandareOperazioneSbustamento", scope = TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class)
    public JAXBElement<Boolean> createTrasformaDocumentoPlaceHolderInDocumentoElettronicoOldRimandareOperazioneSbustamento(Boolean value) {
        return new JAXBElement<Boolean>(_TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldRimandareOperazioneSbustamento_QNAME, Boolean.class, TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectIdType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "repositoryId", scope = TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class)
    public JAXBElement<ObjectIdType> createTrasformaDocumentoPlaceHolderInDocumentoElettronicoOldRepositoryId(ObjectIdType value) {
        return new JAXBElement<ObjectIdType>(_TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldRepositoryId_QNAME, ObjectIdType.class, TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectIdType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "placeHolderId", scope = TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class)
    public JAXBElement<ObjectIdType> createTrasformaDocumentoPlaceHolderInDocumentoElettronicoOldPlaceHolderId(ObjectIdType value) {
        return new JAXBElement<ObjectIdType>(_TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldPlaceHolderId_QNAME, ObjectIdType.class, TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrincipalIdType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "principalId", scope = TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class)
    public JAXBElement<PrincipalIdType> createTrasformaDocumentoPlaceHolderInDocumentoElettronicoOldPrincipalId(PrincipalIdType value) {
        return new JAXBElement<PrincipalIdType>(_TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldPrincipalId_QNAME, PrincipalIdType.class, TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertiesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "annotazione", scope = TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class)
    public JAXBElement<PropertiesType> createTrasformaDocumentoPlaceHolderInDocumentoElettronicoOldAnnotazione(PropertiesType value) {
        return new JAXBElement<PropertiesType>(_TrasformaDocumentoPlaceHolderInDocumentoElettronicoOldAnnotazione_QNAME, PropertiesType.class, TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld.class, value);
    }

}
