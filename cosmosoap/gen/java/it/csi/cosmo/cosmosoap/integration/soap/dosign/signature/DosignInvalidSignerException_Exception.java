
package it.csi.cosmo.cosmosoap.integration.soap.dosign.signature;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.7-b01-
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "DosignInvalidSignerException", targetNamespace = "http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/")
public class DosignInvalidSignerException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private DosignInvalidSignerException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public DosignInvalidSignerException_Exception(String message, DosignInvalidSignerException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public DosignInvalidSignerException_Exception(String message, DosignInvalidSignerException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignInvalidSignerException
     */
    public DosignInvalidSignerException getFaultInfo() {
        return faultInfo;
    }

}