
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.7-b01-
 * Generated source version: 2.1
 * 
 */
@WebService(name = "DosignSignatureValidation", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface DosignSignatureValidation {


    /**
     * 
     * @param arg0
     * @return
     *     returns it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyReportDto
     * @throws DosignInvalidEnvelopeException_Exception
     * @throws DosignException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "verifyDigest", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyDigest")
    @ResponseWrapper(localName = "verifyDigestResponse", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyDigestResponse")
    public VerifyReportDto verifyDigest(
        @WebParam(name = "arg0", targetNamespace = "")
        VerifyDto arg0)
        throws DosignException_Exception, DosignInvalidEnvelopeException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.TimeStampedDataInfoDto
     * @throws DosignInvalidEnvelopeException_Exception
     * @throws DosignException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "verifyTimeStampedData", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyTimeStampedData")
    @ResponseWrapper(localName = "verifyTimeStampedDataResponse", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyTimeStampedDataResponse")
    public TimeStampedDataInfoDto verifyTimeStampedData(
        @WebParam(name = "arg0", targetNamespace = "")
        VerifyDto arg0)
        throws DosignException_Exception, DosignInvalidEnvelopeException_Exception
    ;

    /**
     * 
     * @return
     *     returns boolean
     * @throws DosignException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "testResources", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.TestResources")
    @ResponseWrapper(localName = "testResourcesResponse", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.TestResourcesResponse")
    public boolean testResources()
        throws DosignException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.SignerDto
     * @throws DosignInvalidCertificateException_Exception
     * @throws DosignInvalidDataException_Exception
     * @throws DosignException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "verifyCertificate", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyCertificate")
    @ResponseWrapper(localName = "verifyCertificateResponse", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyCertificateResponse")
    public SignerDto verifyCertificate(
        @WebParam(name = "arg0", targetNamespace = "")
        VerifyDto arg0)
        throws DosignException_Exception, DosignInvalidCertificateException_Exception, DosignInvalidDataException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.TimeStampedDataInfoDto
     * @throws DosignInvalidEnvelopeException_Exception
     * @throws DosignException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "verifyInfocertTimeStampedData", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyInfocertTimeStampedData")
    @ResponseWrapper(localName = "verifyInfocertTimeStampedDataResponse", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyInfocertTimeStampedDataResponse")
    public TimeStampedDataInfoDto verifyInfocertTimeStampedData(
        @WebParam(name = "arg0", targetNamespace = "")
        VerifyDto arg0)
        throws DosignException_Exception, DosignInvalidEnvelopeException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.TimeStampDto
     * @throws DosignInvalidAlgorithmException_Exception
     * @throws DosignInvalidEncodingException_Exception
     * @throws DosignInvalidDataException_Exception
     * @throws DosignException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "verifyTimeStamp", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyTimeStamp")
    @ResponseWrapper(localName = "verifyTimeStampResponse", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyTimeStampResponse")
    public TimeStampDto verifyTimeStamp(
        @WebParam(name = "arg0", targetNamespace = "")
        VerifyDto arg0)
        throws DosignException_Exception, DosignInvalidAlgorithmException_Exception, DosignInvalidDataException_Exception, DosignInvalidEncodingException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyReportDto
     * @throws DosignException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "xmlVerify", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.XmlVerify")
    @ResponseWrapper(localName = "xmlVerifyResponse", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.XmlVerifyResponse")
    public VerifyReportDto xmlVerify(
        @WebParam(name = "arg0", targetNamespace = "")
        VerifyDto arg0)
        throws DosignException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyReportDto
     * @throws DosignException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "verify", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.Verify")
    @ResponseWrapper(localName = "verifyResponse", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyResponse")
    public VerifyReportDto verify(
        @WebParam(name = "arg0", targetNamespace = "")
        VerifyDto arg0)
        throws DosignException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyReportDto
     * @throws DosignInvalidDataException_Exception
     * @throws DosignInvalidEnvelopeException_Exception
     * @throws DosignException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "pdfVerify", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.PdfVerify")
    @ResponseWrapper(localName = "pdfVerifyResponse", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.PdfVerifyResponse")
    public VerifyReportDto pdfVerify(
        @WebParam(name = "arg0", targetNamespace = "")
        VerifyDto arg0)
        throws DosignException_Exception, DosignInvalidDataException_Exception, DosignInvalidEnvelopeException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.TimeStampDto
     * @throws DosignInvalidAlgorithmException_Exception
     * @throws DosignInvalidEncodingException_Exception
     * @throws DosignInvalidDataException_Exception
     * @throws DosignException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "verifyTimeStampDigest", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyTimeStampDigest")
    @ResponseWrapper(localName = "verifyTimeStampDigestResponse", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", className = "it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyTimeStampDigestResponse")
    public TimeStampDto verifyTimeStampDigest(
        @WebParam(name = "arg0", targetNamespace = "")
        VerifyDto arg0)
        throws DosignException_Exception, DosignInvalidAlgorithmException_Exception, DosignInvalidDataException_Exception, DosignInvalidEncodingException_Exception
    ;

}
