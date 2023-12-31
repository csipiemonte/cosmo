
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.7-b01-
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "DosignSignatureValidation", targetNamespace = "http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", wsdlLocation = "file:/C:/Users/grtommason/Work/viste/CSI_GIT/GitHub/cosmo/cosmosoap/src/resources/wsdl/DoSignRemoteValidation.wsdl")
public class DosignSignatureValidation_Service
    extends Service
{

    private final static URL DOSIGNSIGNATUREVALIDATION_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.DosignSignatureValidation_Service.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.DosignSignatureValidation_Service.class.getResource(".");
            url = new URL(baseUrl, "file:/C:/Users/grtommason/Work/viste/CSI_GIT/GitHub/cosmo/cosmosoap/src/resources/wsdl/DoSignRemoteValidation.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'file:/C:/Users/grtommason/Work/viste/CSI_GIT/GitHub/cosmo/cosmosoap/src/resources/wsdl/DoSignRemoteValidation.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        DOSIGNSIGNATUREVALIDATION_WSDL_LOCATION = url;
    }

    public DosignSignatureValidation_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public DosignSignatureValidation_Service() {
        super(DOSIGNSIGNATUREVALIDATION_WSDL_LOCATION, new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "DosignSignatureValidation"));
    }

    /**
     * 
     * @return
     *     returns DosignSignatureValidation
     */
    @WebEndpoint(name = "DosignSignatureValidationBeanPort")
    public DosignSignatureValidation getDosignSignatureValidationBeanPort() {
        return super.getPort(new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "DosignSignatureValidationBeanPort"), DosignSignatureValidation.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns DosignSignatureValidation
     */
    @WebEndpoint(name = "DosignSignatureValidationBeanPort")
    public DosignSignatureValidation getDosignSignatureValidationBeanPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/", "DosignSignatureValidationBeanPort"), DosignSignatureValidation.class, features);
    }

}
