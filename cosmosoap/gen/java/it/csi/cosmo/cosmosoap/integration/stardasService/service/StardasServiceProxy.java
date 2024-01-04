
package it.csi.cosmo.cosmosoap.integration.stardasService.service;

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
@WebServiceClient(name = "StardasServiceProxy", targetNamespace = "http://www.csi.it/stardas/wso2/StardasService", wsdlLocation = "file:/C:/Users/grtommason/Work/viste/CSI_GIT/GitHub/cosmo/cosmosoap/src/resources/wsdl/StardasService.wsdl")
public class StardasServiceProxy
    extends Service
{

    private final static URL STARDASSERVICEPROXY_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(it.csi.cosmo.cosmosoap.integration.stardasService.service.StardasServiceProxy.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = it.csi.cosmo.cosmosoap.integration.stardasService.service.StardasServiceProxy.class.getResource(".");
            url = new URL(baseUrl, "file:/C:/Users/grtommason/Work/viste/CSI_GIT/GitHub/cosmo/cosmosoap/src/resources/wsdl/StardasService.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'file:/C:/Users/grtommason/Work/viste/CSI_GIT/GitHub/cosmo/cosmosoap/src/resources/wsdl/StardasService.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        STARDASSERVICEPROXY_WSDL_LOCATION = url;
    }

    public StardasServiceProxy(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public StardasServiceProxy() {
        super(STARDASSERVICEPROXY_WSDL_LOCATION, new QName("http://www.csi.it/stardas/wso2/StardasService", "StardasServiceProxy"));
    }

    /**
     * 
     * @return
     *     returns StardasServiceProxyPortType
     */
    @WebEndpoint(name = "StardasServiceProxyHttpSoap11Endpoint")
    public StardasServiceProxyPortType getStardasServiceProxyHttpSoap11Endpoint() {
        return super.getPort(new QName("http://www.csi.it/stardas/wso2/StardasService", "StardasServiceProxyHttpSoap11Endpoint"), StardasServiceProxyPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns StardasServiceProxyPortType
     */
    @WebEndpoint(name = "StardasServiceProxyHttpSoap11Endpoint")
    public StardasServiceProxyPortType getStardasServiceProxyHttpSoap11Endpoint(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.csi.it/stardas/wso2/StardasService", "StardasServiceProxyHttpSoap11Endpoint"), StardasServiceProxyPortType.class, features);
    }

    /**
     * 
     * @return
     *     returns StardasServiceProxyPortType
     */
    @WebEndpoint(name = "StardasServiceProxyHttpsSoap11Endpoint")
    public StardasServiceProxyPortType getStardasServiceProxyHttpsSoap11Endpoint() {
        return super.getPort(new QName("http://www.csi.it/stardas/wso2/StardasService", "StardasServiceProxyHttpsSoap11Endpoint"), StardasServiceProxyPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns StardasServiceProxyPortType
     */
    @WebEndpoint(name = "StardasServiceProxyHttpsSoap11Endpoint")
    public StardasServiceProxyPortType getStardasServiceProxyHttpsSoap11Endpoint(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.csi.it/stardas/wso2/StardasService", "StardasServiceProxyHttpsSoap11Endpoint"), StardasServiceProxyPortType.class, features);
    }

}