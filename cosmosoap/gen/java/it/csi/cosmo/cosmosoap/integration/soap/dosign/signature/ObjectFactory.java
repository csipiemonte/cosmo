
package it.csi.cosmo.cosmosoap.integration.soap.dosign.signature;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.csi.cosmo.cosmosoap.integration.soap.dosign.signature package. 
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

    private final static QName _DosignInvalidPinException_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "DosignInvalidPinException");
    private final static QName _PushOtpResponse_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "pushOtpResponse");
    private final static QName _AddsignResponse_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "addsignResponse");
    private final static QName _StartTransactionResponse_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "startTransactionResponse");
    private final static QName _DosignCertificateChainNotFoundException_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "DosignCertificateChainNotFoundException");
    private final static QName _DosignInvalidOtpException_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "DosignInvalidOtpException");
    private final static QName _PdfsignResponse_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "pdfsignResponse");
    private final static QName _EndTransactionResponse_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "endTransactionResponse");
    private final static QName _DosignInvalidDataException_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "DosignInvalidDataException");
    private final static QName _TestResourcesResponse_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "testResourcesResponse");
    private final static QName _EndTransaction_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "endTransaction");
    private final static QName _DosignInvalidEncodingException_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "DosignInvalidEncodingException");
    private final static QName _SignResponse_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "signResponse");
    private final static QName _DosignPinLockedException_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "DosignPinLockedException");
    private final static QName _DosignInvalidEnvelopeException_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "DosignInvalidEnvelopeException");
    private final static QName _DosignException_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "DosignException");
    private final static QName _DosignInvalidAuthenticationException_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "DosignInvalidAuthenticationException");
    private final static QName _DosignInvalidSignerException_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "DosignInvalidSignerException");
    private final static QName _Sign_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "sign");
    private final static QName _Addsign_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "addsign");
    private final static QName _TestResources_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "testResources");
    private final static QName _StartTransaction_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "startTransaction");
    private final static QName _Pdfsign_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "pdfsign");
    private final static QName _DosignInvalidModeException_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "DosignInvalidModeException");
    private final static QName _PushOtp_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "pushOtp");
    private final static QName _DosignClientHttpErrorException_QNAME = new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", "DosignClientHttpErrorException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.csi.cosmo.cosmosoap.integration.soap.dosign.signature
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StartTransaction }
     * 
     */
    public StartTransaction createStartTransaction() {
        return new StartTransaction();
    }

    /**
     * Create an instance of {@link TestResources }
     * 
     */
    public TestResources createTestResources() {
        return new TestResources();
    }

    /**
     * Create an instance of {@link Addsign }
     * 
     */
    public Addsign createAddsign() {
        return new Addsign();
    }

    /**
     * Create an instance of {@link Sign }
     * 
     */
    public Sign createSign() {
        return new Sign();
    }

    /**
     * Create an instance of {@link DosignInvalidSignerException }
     * 
     */
    public DosignInvalidSignerException createDosignInvalidSignerException() {
        return new DosignInvalidSignerException();
    }

    /**
     * Create an instance of {@link DosignInvalidAuthenticationException }
     * 
     */
    public DosignInvalidAuthenticationException createDosignInvalidAuthenticationException() {
        return new DosignInvalidAuthenticationException();
    }

    /**
     * Create an instance of {@link DosignClientHttpErrorException }
     * 
     */
    public DosignClientHttpErrorException createDosignClientHttpErrorException() {
        return new DosignClientHttpErrorException();
    }

    /**
     * Create an instance of {@link PushOtp }
     * 
     */
    public PushOtp createPushOtp() {
        return new PushOtp();
    }

    /**
     * Create an instance of {@link DosignInvalidModeException }
     * 
     */
    public DosignInvalidModeException createDosignInvalidModeException() {
        return new DosignInvalidModeException();
    }

    /**
     * Create an instance of {@link Pdfsign }
     * 
     */
    public Pdfsign createPdfsign() {
        return new Pdfsign();
    }

    /**
     * Create an instance of {@link TestResourcesResponse }
     * 
     */
    public TestResourcesResponse createTestResourcesResponse() {
        return new TestResourcesResponse();
    }

    /**
     * Create an instance of {@link DosignInvalidDataException }
     * 
     */
    public DosignInvalidDataException createDosignInvalidDataException() {
        return new DosignInvalidDataException();
    }

    /**
     * Create an instance of {@link EndTransactionResponse }
     * 
     */
    public EndTransactionResponse createEndTransactionResponse() {
        return new EndTransactionResponse();
    }

    /**
     * Create an instance of {@link PdfsignResponse }
     * 
     */
    public PdfsignResponse createPdfsignResponse() {
        return new PdfsignResponse();
    }

    /**
     * Create an instance of {@link DosignInvalidOtpException }
     * 
     */
    public DosignInvalidOtpException createDosignInvalidOtpException() {
        return new DosignInvalidOtpException();
    }

    /**
     * Create an instance of {@link StartTransactionResponse }
     * 
     */
    public StartTransactionResponse createStartTransactionResponse() {
        return new StartTransactionResponse();
    }

    /**
     * Create an instance of {@link DosignCertificateChainNotFoundException }
     * 
     */
    public DosignCertificateChainNotFoundException createDosignCertificateChainNotFoundException() {
        return new DosignCertificateChainNotFoundException();
    }

    /**
     * Create an instance of {@link AddsignResponse }
     * 
     */
    public AddsignResponse createAddsignResponse() {
        return new AddsignResponse();
    }

    /**
     * Create an instance of {@link PushOtpResponse }
     * 
     */
    public PushOtpResponse createPushOtpResponse() {
        return new PushOtpResponse();
    }

    /**
     * Create an instance of {@link DosignInvalidPinException }
     * 
     */
    public DosignInvalidPinException createDosignInvalidPinException() {
        return new DosignInvalidPinException();
    }

    /**
     * Create an instance of {@link DosignException }
     * 
     */
    public DosignException createDosignException() {
        return new DosignException();
    }

    /**
     * Create an instance of {@link DosignInvalidEnvelopeException }
     * 
     */
    public DosignInvalidEnvelopeException createDosignInvalidEnvelopeException() {
        return new DosignInvalidEnvelopeException();
    }

    /**
     * Create an instance of {@link DosignPinLockedException }
     * 
     */
    public DosignPinLockedException createDosignPinLockedException() {
        return new DosignPinLockedException();
    }

    /**
     * Create an instance of {@link SignResponse }
     * 
     */
    public SignResponse createSignResponse() {
        return new SignResponse();
    }

    /**
     * Create an instance of {@link EndTransaction }
     * 
     */
    public EndTransaction createEndTransaction() {
        return new EndTransaction();
    }

    /**
     * Create an instance of {@link DosignInvalidEncodingException }
     * 
     */
    public DosignInvalidEncodingException createDosignInvalidEncodingException() {
        return new DosignInvalidEncodingException();
    }

    /**
     * Create an instance of {@link SingleRemoteSignatureDto }
     * 
     */
    public SingleRemoteSignatureDto createSingleRemoteSignatureDto() {
        return new SingleRemoteSignatureDto();
    }

    /**
     * Create an instance of {@link BaseRemoteSignatureDto }
     * 
     */
    public BaseRemoteSignatureDto createBaseRemoteSignatureDto() {
        return new BaseRemoteSignatureDto();
    }

    /**
     * Create an instance of {@link CommonDto }
     * 
     */
    public CommonDto createCommonDto() {
        return new CommonDto();
    }

    /**
     * Create an instance of {@link StartTransactionDto }
     * 
     */
    public StartTransactionDto createStartTransactionDto() {
        return new StartTransactionDto();
    }

    /**
     * Create an instance of {@link SingleRemotePdfSignatureDto }
     * 
     */
    public SingleRemotePdfSignatureDto createSingleRemotePdfSignatureDto() {
        return new SingleRemotePdfSignatureDto();
    }

    /**
     * Create an instance of {@link ContentDto }
     * 
     */
    public ContentDto createContentDto() {
        return new ContentDto();
    }

    /**
     * Create an instance of {@link CommonRemoteDataDto }
     * 
     */
    public CommonRemoteDataDto createCommonRemoteDataDto() {
        return new CommonRemoteDataDto();
    }

    /**
     * Create an instance of {@link TransactionDataDto }
     * 
     */
    public TransactionDataDto createTransactionDataDto() {
        return new TransactionDataDto();
    }

    /**
     * Create an instance of {@link RemoteDto }
     * 
     */
    public RemoteDto createRemoteDto() {
        return new RemoteDto();
    }

    /**
     * Create an instance of {@link RemoteSignatureDto }
     * 
     */
    public RemoteSignatureDto createRemoteSignatureDto() {
        return new RemoteSignatureDto();
    }

    /**
     * Create an instance of {@link ContinueTransactionDto }
     * 
     */
    public ContinueTransactionDto createContinueTransactionDto() {
        return new ContinueTransactionDto();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignInvalidPinException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignInvalidPinException")
    public JAXBElement<DosignInvalidPinException> createDosignInvalidPinException(DosignInvalidPinException value) {
        return new JAXBElement<DosignInvalidPinException>(_DosignInvalidPinException_QNAME, DosignInvalidPinException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PushOtpResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "pushOtpResponse")
    public JAXBElement<PushOtpResponse> createPushOtpResponse(PushOtpResponse value) {
        return new JAXBElement<PushOtpResponse>(_PushOtpResponse_QNAME, PushOtpResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddsignResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "addsignResponse")
    public JAXBElement<AddsignResponse> createAddsignResponse(AddsignResponse value) {
        return new JAXBElement<AddsignResponse>(_AddsignResponse_QNAME, AddsignResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartTransactionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "startTransactionResponse")
    public JAXBElement<StartTransactionResponse> createStartTransactionResponse(StartTransactionResponse value) {
        return new JAXBElement<StartTransactionResponse>(_StartTransactionResponse_QNAME, StartTransactionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignCertificateChainNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignCertificateChainNotFoundException")
    public JAXBElement<DosignCertificateChainNotFoundException> createDosignCertificateChainNotFoundException(DosignCertificateChainNotFoundException value) {
        return new JAXBElement<DosignCertificateChainNotFoundException>(_DosignCertificateChainNotFoundException_QNAME, DosignCertificateChainNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignInvalidOtpException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignInvalidOtpException")
    public JAXBElement<DosignInvalidOtpException> createDosignInvalidOtpException(DosignInvalidOtpException value) {
        return new JAXBElement<DosignInvalidOtpException>(_DosignInvalidOtpException_QNAME, DosignInvalidOtpException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PdfsignResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "pdfsignResponse")
    public JAXBElement<PdfsignResponse> createPdfsignResponse(PdfsignResponse value) {
        return new JAXBElement<PdfsignResponse>(_PdfsignResponse_QNAME, PdfsignResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EndTransactionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "endTransactionResponse")
    public JAXBElement<EndTransactionResponse> createEndTransactionResponse(EndTransactionResponse value) {
        return new JAXBElement<EndTransactionResponse>(_EndTransactionResponse_QNAME, EndTransactionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignInvalidDataException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignInvalidDataException")
    public JAXBElement<DosignInvalidDataException> createDosignInvalidDataException(DosignInvalidDataException value) {
        return new JAXBElement<DosignInvalidDataException>(_DosignInvalidDataException_QNAME, DosignInvalidDataException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestResourcesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "testResourcesResponse")
    public JAXBElement<TestResourcesResponse> createTestResourcesResponse(TestResourcesResponse value) {
        return new JAXBElement<TestResourcesResponse>(_TestResourcesResponse_QNAME, TestResourcesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EndTransaction }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "endTransaction")
    public JAXBElement<EndTransaction> createEndTransaction(EndTransaction value) {
        return new JAXBElement<EndTransaction>(_EndTransaction_QNAME, EndTransaction.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignInvalidEncodingException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignInvalidEncodingException")
    public JAXBElement<DosignInvalidEncodingException> createDosignInvalidEncodingException(DosignInvalidEncodingException value) {
        return new JAXBElement<DosignInvalidEncodingException>(_DosignInvalidEncodingException_QNAME, DosignInvalidEncodingException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "signResponse")
    public JAXBElement<SignResponse> createSignResponse(SignResponse value) {
        return new JAXBElement<SignResponse>(_SignResponse_QNAME, SignResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignPinLockedException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignPinLockedException")
    public JAXBElement<DosignPinLockedException> createDosignPinLockedException(DosignPinLockedException value) {
        return new JAXBElement<DosignPinLockedException>(_DosignPinLockedException_QNAME, DosignPinLockedException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignInvalidEnvelopeException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignInvalidEnvelopeException")
    public JAXBElement<DosignInvalidEnvelopeException> createDosignInvalidEnvelopeException(DosignInvalidEnvelopeException value) {
        return new JAXBElement<DosignInvalidEnvelopeException>(_DosignInvalidEnvelopeException_QNAME, DosignInvalidEnvelopeException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignException")
    public JAXBElement<DosignException> createDosignException(DosignException value) {
        return new JAXBElement<DosignException>(_DosignException_QNAME, DosignException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignInvalidAuthenticationException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignInvalidAuthenticationException")
    public JAXBElement<DosignInvalidAuthenticationException> createDosignInvalidAuthenticationException(DosignInvalidAuthenticationException value) {
        return new JAXBElement<DosignInvalidAuthenticationException>(_DosignInvalidAuthenticationException_QNAME, DosignInvalidAuthenticationException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignInvalidSignerException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignInvalidSignerException")
    public JAXBElement<DosignInvalidSignerException> createDosignInvalidSignerException(DosignInvalidSignerException value) {
        return new JAXBElement<DosignInvalidSignerException>(_DosignInvalidSignerException_QNAME, DosignInvalidSignerException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Sign }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "sign")
    public JAXBElement<Sign> createSign(Sign value) {
        return new JAXBElement<Sign>(_Sign_QNAME, Sign.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Addsign }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "addsign")
    public JAXBElement<Addsign> createAddsign(Addsign value) {
        return new JAXBElement<Addsign>(_Addsign_QNAME, Addsign.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestResources }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "testResources")
    public JAXBElement<TestResources> createTestResources(TestResources value) {
        return new JAXBElement<TestResources>(_TestResources_QNAME, TestResources.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartTransaction }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "startTransaction")
    public JAXBElement<StartTransaction> createStartTransaction(StartTransaction value) {
        return new JAXBElement<StartTransaction>(_StartTransaction_QNAME, StartTransaction.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Pdfsign }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "pdfsign")
    public JAXBElement<Pdfsign> createPdfsign(Pdfsign value) {
        return new JAXBElement<Pdfsign>(_Pdfsign_QNAME, Pdfsign.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignInvalidModeException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignInvalidModeException")
    public JAXBElement<DosignInvalidModeException> createDosignInvalidModeException(DosignInvalidModeException value) {
        return new JAXBElement<DosignInvalidModeException>(_DosignInvalidModeException_QNAME, DosignInvalidModeException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PushOtp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "pushOtp")
    public JAXBElement<PushOtp> createPushOtp(PushOtp value) {
        return new JAXBElement<PushOtp>(_PushOtp_QNAME, PushOtp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignClientHttpErrorException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignClientHttpErrorException")
    public JAXBElement<DosignClientHttpErrorException> createDosignClientHttpErrorException(DosignClientHttpErrorException value) {
        return new JAXBElement<DosignClientHttpErrorException>(_DosignClientHttpErrorException_QNAME, DosignClientHttpErrorException.class, null, value);
    }

}
