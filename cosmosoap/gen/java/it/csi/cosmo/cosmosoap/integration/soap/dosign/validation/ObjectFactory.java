
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.csi.cosmo.cosmosoap.integration.soap.dosign.validation package. 
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

    private final static QName _VerifyDigest_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verifyDigest");
    private final static QName _DosignException_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "DosignException");
    private final static QName _DosignInvalidEnvelopeException_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "DosignInvalidEnvelopeException");
    private final static QName _VerifyInfocertTimeStampedDataResponse_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verifyInfocertTimeStampedDataResponse");
    private final static QName _VerifyTimeStampedDataResponse_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verifyTimeStampedDataResponse");
    private final static QName _DosignInvalidEncodingException_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "DosignInvalidEncodingException");
    private final static QName _DosignInvalidDataException_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "DosignInvalidDataException");
    private final static QName _TestResourcesResponse_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "testResourcesResponse");
    private final static QName _VerifyCertificate_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verifyCertificate");
    private final static QName _XmlVerify_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "xmlVerify");
    private final static QName _PdfVerify_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "pdfVerify");
    private final static QName _PdfVerifyResponse_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "pdfVerifyResponse");
    private final static QName _VerifyTimeStampedData_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verifyTimeStampedData");
    private final static QName _DosignInvalidAlgorithmException_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "DosignInvalidAlgorithmException");
    private final static QName _VerifyTimeStamp_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verifyTimeStamp");
    private final static QName _VerifyTimeStampDigestResponse_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verifyTimeStampDigestResponse");
    private final static QName _VerifyResponse_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verifyResponse");
    private final static QName _Verify_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verify");
    private final static QName _VerifyTimeStampDigest_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verifyTimeStampDigest");
    private final static QName _DosignInvalidCertificateException_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "DosignInvalidCertificateException");
    private final static QName _TestResources_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "testResources");
    private final static QName _VerifyTimeStampResponse_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verifyTimeStampResponse");
    private final static QName _XmlVerifyResponse_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "xmlVerifyResponse");
    private final static QName _VerifyInfocertTimeStampedData_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verifyInfocertTimeStampedData");
    private final static QName _VerifyDigestResponse_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verifyDigestResponse");
    private final static QName _VerifyCertificateResponse_QNAME = new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "verifyCertificateResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.csi.cosmo.cosmosoap.integration.soap.dosign.validation
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DosignInvalidCertificateException }
     * 
     */
    public DosignInvalidCertificateException createDosignInvalidCertificateException() {
        return new DosignInvalidCertificateException();
    }

    /**
     * Create an instance of {@link TestResources }
     * 
     */
    public TestResources createTestResources() {
        return new TestResources();
    }

    /**
     * Create an instance of {@link VerifyTimeStampResponse }
     * 
     */
    public VerifyTimeStampResponse createVerifyTimeStampResponse() {
        return new VerifyTimeStampResponse();
    }

    /**
     * Create an instance of {@link XmlVerifyResponse }
     * 
     */
    public XmlVerifyResponse createXmlVerifyResponse() {
        return new XmlVerifyResponse();
    }

    /**
     * Create an instance of {@link VerifyInfocertTimeStampedData }
     * 
     */
    public VerifyInfocertTimeStampedData createVerifyInfocertTimeStampedData() {
        return new VerifyInfocertTimeStampedData();
    }

    /**
     * Create an instance of {@link VerifyDigestResponse }
     * 
     */
    public VerifyDigestResponse createVerifyDigestResponse() {
        return new VerifyDigestResponse();
    }

    /**
     * Create an instance of {@link VerifyCertificateResponse }
     * 
     */
    public VerifyCertificateResponse createVerifyCertificateResponse() {
        return new VerifyCertificateResponse();
    }

    /**
     * Create an instance of {@link VerifyTimeStampedData }
     * 
     */
    public VerifyTimeStampedData createVerifyTimeStampedData() {
        return new VerifyTimeStampedData();
    }

    /**
     * Create an instance of {@link DosignInvalidAlgorithmException }
     * 
     */
    public DosignInvalidAlgorithmException createDosignInvalidAlgorithmException() {
        return new DosignInvalidAlgorithmException();
    }

    /**
     * Create an instance of {@link VerifyResponse }
     * 
     */
    public VerifyResponse createVerifyResponse() {
        return new VerifyResponse();
    }

    /**
     * Create an instance of {@link Verify }
     * 
     */
    public Verify createVerify() {
        return new Verify();
    }

    /**
     * Create an instance of {@link VerifyTimeStamp }
     * 
     */
    public VerifyTimeStamp createVerifyTimeStamp() {
        return new VerifyTimeStamp();
    }

    /**
     * Create an instance of {@link VerifyTimeStampDigestResponse }
     * 
     */
    public VerifyTimeStampDigestResponse createVerifyTimeStampDigestResponse() {
        return new VerifyTimeStampDigestResponse();
    }

    /**
     * Create an instance of {@link VerifyTimeStampDigest }
     * 
     */
    public VerifyTimeStampDigest createVerifyTimeStampDigest() {
        return new VerifyTimeStampDigest();
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
     * Create an instance of {@link VerifyCertificate }
     * 
     */
    public VerifyCertificate createVerifyCertificate() {
        return new VerifyCertificate();
    }

    /**
     * Create an instance of {@link XmlVerify }
     * 
     */
    public XmlVerify createXmlVerify() {
        return new XmlVerify();
    }

    /**
     * Create an instance of {@link PdfVerify }
     * 
     */
    public PdfVerify createPdfVerify() {
        return new PdfVerify();
    }

    /**
     * Create an instance of {@link PdfVerifyResponse }
     * 
     */
    public PdfVerifyResponse createPdfVerifyResponse() {
        return new PdfVerifyResponse();
    }

    /**
     * Create an instance of {@link VerifyDigest }
     * 
     */
    public VerifyDigest createVerifyDigest() {
        return new VerifyDigest();
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
     * Create an instance of {@link VerifyInfocertTimeStampedDataResponse }
     * 
     */
    public VerifyInfocertTimeStampedDataResponse createVerifyInfocertTimeStampedDataResponse() {
        return new VerifyInfocertTimeStampedDataResponse();
    }

    /**
     * Create an instance of {@link VerifyTimeStampedDataResponse }
     * 
     */
    public VerifyTimeStampedDataResponse createVerifyTimeStampedDataResponse() {
        return new VerifyTimeStampedDataResponse();
    }

    /**
     * Create an instance of {@link DosignInvalidEncodingException }
     * 
     */
    public DosignInvalidEncodingException createDosignInvalidEncodingException() {
        return new DosignInvalidEncodingException();
    }

    /**
     * Create an instance of {@link VerifyReportDto }
     * 
     */
    public VerifyReportDto createVerifyReportDto() {
        return new VerifyReportDto();
    }

    /**
     * Create an instance of {@link CommonDto }
     * 
     */
    public CommonDto createCommonDto() {
        return new CommonDto();
    }

    /**
     * Create an instance of {@link ErrorInformationDto }
     * 
     */
    public ErrorInformationDto createErrorInformationDto() {
        return new ErrorInformationDto();
    }

    /**
     * Create an instance of {@link TimeStampDto }
     * 
     */
    public TimeStampDto createTimeStampDto() {
        return new TimeStampDto();
    }

    /**
     * Create an instance of {@link XmlReferenceDto }
     * 
     */
    public XmlReferenceDto createXmlReferenceDto() {
        return new XmlReferenceDto();
    }

    /**
     * Create an instance of {@link XmlReferenceDataDto }
     * 
     */
    public XmlReferenceDataDto createXmlReferenceDataDto() {
        return new XmlReferenceDataDto();
    }

    /**
     * Create an instance of {@link XmlObjectIdentifierDto }
     * 
     */
    public XmlObjectIdentifierDto createXmlObjectIdentifierDto() {
        return new XmlObjectIdentifierDto();
    }

    /**
     * Create an instance of {@link XmlPropertyDto }
     * 
     */
    public XmlPropertyDto createXmlPropertyDto() {
        return new XmlPropertyDto();
    }

    /**
     * Create an instance of {@link PolicyInfoDto }
     * 
     */
    public PolicyInfoDto createPolicyInfoDto() {
        return new PolicyInfoDto();
    }

    /**
     * Create an instance of {@link XmlDataObjectFormatDto }
     * 
     */
    public XmlDataObjectFormatDto createXmlDataObjectFormatDto() {
        return new XmlDataObjectFormatDto();
    }

    /**
     * Create an instance of {@link SignatureValidationDto }
     * 
     */
    public SignatureValidationDto createSignatureValidationDto() {
        return new SignatureValidationDto();
    }

    /**
     * Create an instance of {@link VerifyInfoDto }
     * 
     */
    public VerifyInfoDto createVerifyInfoDto() {
        return new VerifyInfoDto();
    }

    /**
     * Create an instance of {@link VerifyDto }
     * 
     */
    public VerifyDto createVerifyDto() {
        return new VerifyDto();
    }

    /**
     * Create an instance of {@link XmlSignatureProductionPlaceDto }
     * 
     */
    public XmlSignatureProductionPlaceDto createXmlSignatureProductionPlaceDto() {
        return new XmlSignatureProductionPlaceDto();
    }

    /**
     * Create an instance of {@link CertificateDto }
     * 
     */
    public CertificateDto createCertificateDto() {
        return new CertificateDto();
    }

    /**
     * Create an instance of {@link SignerDto }
     * 
     */
    public SignerDto createSignerDto() {
        return new SignerDto();
    }

    /**
     * Create an instance of {@link ContentDto }
     * 
     */
    public ContentDto createContentDto() {
        return new ContentDto();
    }

    /**
     * Create an instance of {@link XmlTransformationDto }
     * 
     */
    public XmlTransformationDto createXmlTransformationDto() {
        return new XmlTransformationDto();
    }

    /**
     * Create an instance of {@link TimeStampedDataInfoDto }
     * 
     */
    public TimeStampedDataInfoDto createTimeStampedDataInfoDto() {
        return new TimeStampedDataInfoDto();
    }

    /**
     * Create an instance of {@link SignatureParameterDto }
     * 
     */
    public SignatureParameterDto createSignatureParameterDto() {
        return new SignatureParameterDto();
    }

    /**
     * Create an instance of {@link DosignDataDto }
     * 
     */
    public DosignDataDto createDosignDataDto() {
        return new DosignDataDto();
    }

    /**
     * Create an instance of {@link CertificatePolicyDto }
     * 
     */
    public CertificatePolicyDto createCertificatePolicyDto() {
        return new CertificatePolicyDto();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyDigest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verifyDigest")
    public JAXBElement<VerifyDigest> createVerifyDigest(VerifyDigest value) {
        return new JAXBElement<VerifyDigest>(_VerifyDigest_QNAME, VerifyDigest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignException")
    public JAXBElement<DosignException> createDosignException(DosignException value) {
        return new JAXBElement<DosignException>(_DosignException_QNAME, DosignException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignInvalidEnvelopeException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignInvalidEnvelopeException")
    public JAXBElement<DosignInvalidEnvelopeException> createDosignInvalidEnvelopeException(DosignInvalidEnvelopeException value) {
        return new JAXBElement<DosignInvalidEnvelopeException>(_DosignInvalidEnvelopeException_QNAME, DosignInvalidEnvelopeException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyInfocertTimeStampedDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verifyInfocertTimeStampedDataResponse")
    public JAXBElement<VerifyInfocertTimeStampedDataResponse> createVerifyInfocertTimeStampedDataResponse(VerifyInfocertTimeStampedDataResponse value) {
        return new JAXBElement<VerifyInfocertTimeStampedDataResponse>(_VerifyInfocertTimeStampedDataResponse_QNAME, VerifyInfocertTimeStampedDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyTimeStampedDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verifyTimeStampedDataResponse")
    public JAXBElement<VerifyTimeStampedDataResponse> createVerifyTimeStampedDataResponse(VerifyTimeStampedDataResponse value) {
        return new JAXBElement<VerifyTimeStampedDataResponse>(_VerifyTimeStampedDataResponse_QNAME, VerifyTimeStampedDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignInvalidEncodingException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignInvalidEncodingException")
    public JAXBElement<DosignInvalidEncodingException> createDosignInvalidEncodingException(DosignInvalidEncodingException value) {
        return new JAXBElement<DosignInvalidEncodingException>(_DosignInvalidEncodingException_QNAME, DosignInvalidEncodingException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignInvalidDataException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignInvalidDataException")
    public JAXBElement<DosignInvalidDataException> createDosignInvalidDataException(DosignInvalidDataException value) {
        return new JAXBElement<DosignInvalidDataException>(_DosignInvalidDataException_QNAME, DosignInvalidDataException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestResourcesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "testResourcesResponse")
    public JAXBElement<TestResourcesResponse> createTestResourcesResponse(TestResourcesResponse value) {
        return new JAXBElement<TestResourcesResponse>(_TestResourcesResponse_QNAME, TestResourcesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyCertificate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verifyCertificate")
    public JAXBElement<VerifyCertificate> createVerifyCertificate(VerifyCertificate value) {
        return new JAXBElement<VerifyCertificate>(_VerifyCertificate_QNAME, VerifyCertificate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlVerify }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "xmlVerify")
    public JAXBElement<XmlVerify> createXmlVerify(XmlVerify value) {
        return new JAXBElement<XmlVerify>(_XmlVerify_QNAME, XmlVerify.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PdfVerify }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "pdfVerify")
    public JAXBElement<PdfVerify> createPdfVerify(PdfVerify value) {
        return new JAXBElement<PdfVerify>(_PdfVerify_QNAME, PdfVerify.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PdfVerifyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "pdfVerifyResponse")
    public JAXBElement<PdfVerifyResponse> createPdfVerifyResponse(PdfVerifyResponse value) {
        return new JAXBElement<PdfVerifyResponse>(_PdfVerifyResponse_QNAME, PdfVerifyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyTimeStampedData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verifyTimeStampedData")
    public JAXBElement<VerifyTimeStampedData> createVerifyTimeStampedData(VerifyTimeStampedData value) {
        return new JAXBElement<VerifyTimeStampedData>(_VerifyTimeStampedData_QNAME, VerifyTimeStampedData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignInvalidAlgorithmException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignInvalidAlgorithmException")
    public JAXBElement<DosignInvalidAlgorithmException> createDosignInvalidAlgorithmException(DosignInvalidAlgorithmException value) {
        return new JAXBElement<DosignInvalidAlgorithmException>(_DosignInvalidAlgorithmException_QNAME, DosignInvalidAlgorithmException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyTimeStamp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verifyTimeStamp")
    public JAXBElement<VerifyTimeStamp> createVerifyTimeStamp(VerifyTimeStamp value) {
        return new JAXBElement<VerifyTimeStamp>(_VerifyTimeStamp_QNAME, VerifyTimeStamp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyTimeStampDigestResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verifyTimeStampDigestResponse")
    public JAXBElement<VerifyTimeStampDigestResponse> createVerifyTimeStampDigestResponse(VerifyTimeStampDigestResponse value) {
        return new JAXBElement<VerifyTimeStampDigestResponse>(_VerifyTimeStampDigestResponse_QNAME, VerifyTimeStampDigestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verifyResponse")
    public JAXBElement<VerifyResponse> createVerifyResponse(VerifyResponse value) {
        return new JAXBElement<VerifyResponse>(_VerifyResponse_QNAME, VerifyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Verify }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verify")
    public JAXBElement<Verify> createVerify(Verify value) {
        return new JAXBElement<Verify>(_Verify_QNAME, Verify.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyTimeStampDigest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verifyTimeStampDigest")
    public JAXBElement<VerifyTimeStampDigest> createVerifyTimeStampDigest(VerifyTimeStampDigest value) {
        return new JAXBElement<VerifyTimeStampDigest>(_VerifyTimeStampDigest_QNAME, VerifyTimeStampDigest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DosignInvalidCertificateException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "DosignInvalidCertificateException")
    public JAXBElement<DosignInvalidCertificateException> createDosignInvalidCertificateException(DosignInvalidCertificateException value) {
        return new JAXBElement<DosignInvalidCertificateException>(_DosignInvalidCertificateException_QNAME, DosignInvalidCertificateException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestResources }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "testResources")
    public JAXBElement<TestResources> createTestResources(TestResources value) {
        return new JAXBElement<TestResources>(_TestResources_QNAME, TestResources.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyTimeStampResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verifyTimeStampResponse")
    public JAXBElement<VerifyTimeStampResponse> createVerifyTimeStampResponse(VerifyTimeStampResponse value) {
        return new JAXBElement<VerifyTimeStampResponse>(_VerifyTimeStampResponse_QNAME, VerifyTimeStampResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlVerifyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "xmlVerifyResponse")
    public JAXBElement<XmlVerifyResponse> createXmlVerifyResponse(XmlVerifyResponse value) {
        return new JAXBElement<XmlVerifyResponse>(_XmlVerifyResponse_QNAME, XmlVerifyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyInfocertTimeStampedData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verifyInfocertTimeStampedData")
    public JAXBElement<VerifyInfocertTimeStampedData> createVerifyInfocertTimeStampedData(VerifyInfocertTimeStampedData value) {
        return new JAXBElement<VerifyInfocertTimeStampedData>(_VerifyInfocertTimeStampedData_QNAME, VerifyInfocertTimeStampedData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyDigestResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verifyDigestResponse")
    public JAXBElement<VerifyDigestResponse> createVerifyDigestResponse(VerifyDigestResponse value) {
        return new JAXBElement<VerifyDigestResponse>(_VerifyDigestResponse_QNAME, VerifyDigestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyCertificateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", name = "verifyCertificateResponse")
    public JAXBElement<VerifyCertificateResponse> createVerifyCertificateResponse(VerifyCertificateResponse value) {
        return new JAXBElement<VerifyCertificateResponse>(_VerifyCertificateResponse_QNAME, VerifyCertificateResponse.class, null, value);
    }

}
