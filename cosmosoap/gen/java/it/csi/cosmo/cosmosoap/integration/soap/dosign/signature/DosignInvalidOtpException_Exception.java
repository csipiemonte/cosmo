
package it.csi.cosmo.cosmosoap.integration.soap.dosign.signature;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.7-b01-
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "DosignInvalidOtpException", targetNamespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/")
public class DosignInvalidOtpException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private DosignInvalidOtpException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public DosignInvalidOtpException_Exception(String message, DosignInvalidOtpException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public DosignInvalidOtpException_Exception(String message, DosignInvalidOtpException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignInvalidOtpException
     */
    public DosignInvalidOtpException getFaultInfo() {
        return faultInfo;
    }

}
