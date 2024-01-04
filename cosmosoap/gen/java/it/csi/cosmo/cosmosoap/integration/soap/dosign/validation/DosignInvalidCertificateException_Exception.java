
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.7-b01-
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "DosignInvalidCertificateException", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/")
public class DosignInvalidCertificateException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private DosignInvalidCertificateException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public DosignInvalidCertificateException_Exception(String message, DosignInvalidCertificateException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public DosignInvalidCertificateException_Exception(String message, DosignInvalidCertificateException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.DosignInvalidCertificateException
     */
    public DosignInvalidCertificateException getFaultInfo() {
        return faultInfo;
    }

}