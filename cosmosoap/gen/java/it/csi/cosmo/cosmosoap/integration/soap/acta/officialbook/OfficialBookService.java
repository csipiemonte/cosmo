
package it.csi.cosmo.cosmosoap.integration.soap.acta.officialbook;

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
@WebServiceClient(name = "OfficialBookService", targetNamespace = "officialbookservice.acaris.acta.doqui.it", wsdlLocation = "file:/C:/Users/grtommason/Work/viste/CSI_GIT/GitHub/cosmo/cosmosoap/src/resources/wsdl/ActaOfficialBook.wsdl")
public class OfficialBookService
    extends Service
{

    private final static URL OFFICIALBOOKSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(it.csi.cosmo.cosmosoap.integration.soap.acta.officialbook.OfficialBookService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = it.csi.cosmo.cosmosoap.integration.soap.acta.officialbook.OfficialBookService.class.getResource(".");
            url = new URL(baseUrl, "file:/C:/Users/grtommason/Work/viste/CSI_GIT/GitHub/cosmo/cosmosoap/src/resources/wsdl/ActaOfficialBook.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'file:/C:/Users/grtommason/Work/viste/CSI_GIT/GitHub/cosmo/cosmosoap/src/resources/wsdl/ActaOfficialBook.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        OFFICIALBOOKSERVICE_WSDL_LOCATION = url;
    }

    public OfficialBookService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public OfficialBookService() {
        super(OFFICIALBOOKSERVICE_WSDL_LOCATION, new QName("officialbookservice.acaris.acta.doqui.it", "OfficialBookService"));
    }

    /**
     * 
     * @return
     *     returns OfficialBookServicePort
     */
    @WebEndpoint(name = "OfficialBookServicePort")
    public OfficialBookServicePort getOfficialBookServicePort() {
        return super.getPort(new QName("officialbookservice.acaris.acta.doqui.it", "OfficialBookServicePort"), OfficialBookServicePort.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns OfficialBookServicePort
     */
    @WebEndpoint(name = "OfficialBookServicePort")
    public OfficialBookServicePort getOfficialBookServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("officialbookservice.acaris.acta.doqui.it", "OfficialBookServicePort"), OfficialBookServicePort.class, features);
    }

}
