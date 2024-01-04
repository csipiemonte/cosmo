
package it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import it.csi.cosmo.cosmosoap.facade.dto.common.DettaglioCompletoRichiestaType;
import it.csi.cosmo.cosmosoap.facade.dto.common.ResponseType;
import it.csi.cosmo.cosmosoap.facade.dto.common.ResultType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto package. 
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

    private final static QName _SmistaDocumentoRequest_QNAME = new QName("http://www.csi.it/stardas/wso2/StardasService", "SmistaDocumentoRequest");
    private final static QName _GetDettaglioRichiestaResponse_QNAME = new QName("http://www.csi.it/stardas/wso2/StardasService", "GetDettaglioRichiestaResponse");
    private final static QName _DownloadDettagliInvioSegnaturaRequest_QNAME = new QName("http://www.csi.it/stardas/wso2/StardasService", "DownloadDettagliInvioSegnaturaRequest");
    private final static QName _SmistaDocumentoResponse_QNAME = new QName("http://www.csi.it/stardas/wso2/StardasService", "SmistaDocumentoResponse");
    private final static QName _DownloadDettagliInvioSegnaturaResponse_QNAME = new QName("http://www.csi.it/stardas/wso2/StardasService", "DownloadDettagliInvioSegnaturaResponse");
    private final static QName _GetDettaglioRichiestaRequest_QNAME = new QName("http://www.csi.it/stardas/wso2/StardasService", "GetDettaglioRichiestaRequest");
    private final static QName _StardasServiceResponse_QNAME = new QName("http://www.csi.it/stardas/wso2/StardasService", "StardasServiceResponse");
    private final static QName _GetStatoRichiestaRequest_QNAME = new QName("http://www.csi.it/stardas/wso2/StardasService", "GetStatoRichiestaRequest");
    private final static QName _GetStatoRichiestaResponse_QNAME = new QName("http://www.csi.it/stardas/wso2/StardasService", "GetStatoRichiestaResponse");
    private final static QName _GetDettaglioRichiestaResponseTypeDettaglioRichiesta_QNAME = new QName("http://www.csi.it/stardas/wso2/StardasService", "DettaglioRichiesta");
    private final static QName _GetDettaglioRichiestaResponseTypeResult_QNAME = new QName("http://www.csi.it/stardas/wso2/StardasService", "Result");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetStatoRichiestaRequestType }
     * 
     */
    public GetStatoRichiestaRequestType createGetStatoRichiestaRequestType() {
        return new GetStatoRichiestaRequestType();
    }

    /**
     * Create an instance of {@link GetStatoRichiestaResponseType }
     * 
     */
    public GetStatoRichiestaResponseType createGetStatoRichiestaResponseType() {
        return new GetStatoRichiestaResponseType();
    }

    /**
     * Create an instance of {@link DownloadDettagliInvioSegnaturaResponseType }
     * 
     */
    public DownloadDettagliInvioSegnaturaResponseType createDownloadDettagliInvioSegnaturaResponseType() {
        return new DownloadDettagliInvioSegnaturaResponseType();
    }

    /**
     * Create an instance of {@link GetDettaglioRichiestaRequestType }
     * 
     */
    public GetDettaglioRichiestaRequestType createGetDettaglioRichiestaRequestType() {
        return new GetDettaglioRichiestaRequestType();
    }

    /**
     * Create an instance of {@link DownloadDettagliInvioSegnaturaRequestType }
     * 
     */
    public DownloadDettagliInvioSegnaturaRequestType createDownloadDettagliInvioSegnaturaRequestType() {
        return new DownloadDettagliInvioSegnaturaRequestType();
    }

    /**
     * Create an instance of {@link SmistaDocumentoResponseType }
     * 
     */
    public SmistaDocumentoResponseType createSmistaDocumentoResponseType() {
        return new SmistaDocumentoResponseType();
    }

    /**
     * Create an instance of {@link SmistaDocumentoRequestType }
     * 
     */
    public SmistaDocumentoRequestType createSmistaDocumentoRequestType() {
        return new SmistaDocumentoRequestType();
    }

    /**
     * Create an instance of {@link GetDettaglioRichiestaResponseType }
     * 
     */
    public GetDettaglioRichiestaResponseType createGetDettaglioRichiestaResponseType() {
        return new GetDettaglioRichiestaResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SmistaDocumentoRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "SmistaDocumentoRequest")
    public JAXBElement<SmistaDocumentoRequestType> createSmistaDocumentoRequest(SmistaDocumentoRequestType value) {
        return new JAXBElement<SmistaDocumentoRequestType>(_SmistaDocumentoRequest_QNAME, SmistaDocumentoRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDettaglioRichiestaResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "GetDettaglioRichiestaResponse")
    public JAXBElement<GetDettaglioRichiestaResponseType> createGetDettaglioRichiestaResponse(GetDettaglioRichiestaResponseType value) {
        return new JAXBElement<GetDettaglioRichiestaResponseType>(_GetDettaglioRichiestaResponse_QNAME, GetDettaglioRichiestaResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadDettagliInvioSegnaturaRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "DownloadDettagliInvioSegnaturaRequest")
    public JAXBElement<DownloadDettagliInvioSegnaturaRequestType> createDownloadDettagliInvioSegnaturaRequest(DownloadDettagliInvioSegnaturaRequestType value) {
        return new JAXBElement<DownloadDettagliInvioSegnaturaRequestType>(_DownloadDettagliInvioSegnaturaRequest_QNAME, DownloadDettagliInvioSegnaturaRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SmistaDocumentoResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "SmistaDocumentoResponse")
    public JAXBElement<SmistaDocumentoResponseType> createSmistaDocumentoResponse(SmistaDocumentoResponseType value) {
        return new JAXBElement<SmistaDocumentoResponseType>(_SmistaDocumentoResponse_QNAME, SmistaDocumentoResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadDettagliInvioSegnaturaResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "DownloadDettagliInvioSegnaturaResponse")
    public JAXBElement<DownloadDettagliInvioSegnaturaResponseType> createDownloadDettagliInvioSegnaturaResponse(DownloadDettagliInvioSegnaturaResponseType value) {
        return new JAXBElement<DownloadDettagliInvioSegnaturaResponseType>(_DownloadDettagliInvioSegnaturaResponse_QNAME, DownloadDettagliInvioSegnaturaResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDettaglioRichiestaRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "GetDettaglioRichiestaRequest")
    public JAXBElement<GetDettaglioRichiestaRequestType> createGetDettaglioRichiestaRequest(GetDettaglioRichiestaRequestType value) {
        return new JAXBElement<GetDettaglioRichiestaRequestType>(_GetDettaglioRichiestaRequest_QNAME, GetDettaglioRichiestaRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "StardasServiceResponse")
    public JAXBElement<ResponseType> createStardasServiceResponse(ResponseType value) {
        return new JAXBElement<ResponseType>(_StardasServiceResponse_QNAME, ResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStatoRichiestaRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "GetStatoRichiestaRequest")
    public JAXBElement<GetStatoRichiestaRequestType> createGetStatoRichiestaRequest(GetStatoRichiestaRequestType value) {
        return new JAXBElement<GetStatoRichiestaRequestType>(_GetStatoRichiestaRequest_QNAME, GetStatoRichiestaRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStatoRichiestaResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "GetStatoRichiestaResponse")
    public JAXBElement<GetStatoRichiestaResponseType> createGetStatoRichiestaResponse(GetStatoRichiestaResponseType value) {
        return new JAXBElement<GetStatoRichiestaResponseType>(_GetStatoRichiestaResponse_QNAME, GetStatoRichiestaResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DettaglioCompletoRichiestaType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "DettaglioRichiesta", scope = GetDettaglioRichiestaResponseType.class)
    public JAXBElement<DettaglioCompletoRichiestaType> createGetDettaglioRichiestaResponseTypeDettaglioRichiesta(DettaglioCompletoRichiestaType value) {
        return new JAXBElement<DettaglioCompletoRichiestaType>(_GetDettaglioRichiestaResponseTypeDettaglioRichiesta_QNAME, DettaglioCompletoRichiestaType.class, GetDettaglioRichiestaResponseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "Result", scope = GetDettaglioRichiestaResponseType.class)
    public JAXBElement<ResultType> createGetDettaglioRichiestaResponseTypeResult(ResultType value) {
        return new JAXBElement<ResultType>(_GetDettaglioRichiestaResponseTypeResult_QNAME, ResultType.class, GetDettaglioRichiestaResponseType.class, value);
    }

}
