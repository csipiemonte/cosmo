
package it.csi.cosmo.cosmosoap.integration.soap.acta.management;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import it.doqui.acta.acaris.common.AnnotazioniPropertiesType;
import it.doqui.acta.acaris.common.IdAnnotazioniType;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.common.PrincipalIdType;
import it.doqui.acta.acaris.management.VitalRecordCodeType;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.7-b01-
 * Generated source version: 2.1
 * 
 */
@WebService(name = "ManagementServicePort", targetNamespace = "managementservice.acaris.acta.doqui.it")
@XmlSeeAlso({
    it.doqui.acta.acaris.common.ObjectFactory.class,
    it.doqui.acta.acaris.management.ObjectFactory.class
})
public interface ManagementServicePort {


    /**
     * 
     * @param repositoryId
     * @return
     *     returns java.util.List<it.doqui.acta.acaris.management.VitalRecordCodeType>
     * @throws AcarisException
     */
    @WebMethod
    @WebResult(name = "vitalRecordCode", targetNamespace = "")
    @RequestWrapper(localName = "getVitalRecordCode", targetNamespace = "management.acaris.acta.doqui.it", className = "it.doqui.acta.acaris.management.GetVitalRecordCode")
    @ResponseWrapper(localName = "getVitalRecordCodeResponse", targetNamespace = "management.acaris.acta.doqui.it", className = "it.doqui.acta.acaris.management.GetVitalRecordCodeResponse")
    public List<VitalRecordCodeType> getVitalRecordCode(
        @WebParam(name = "repositoryId", targetNamespace = "")
        ObjectIdType repositoryId)
        throws AcarisException
    ;

    /**
     * 
     * @param annotazioni
     * @param repositoryId
     * @param principalId
     * @param objectId
     * @return
     *     returns it.doqui.acta.acaris.common.IdAnnotazioniType
     * @throws AcarisException
     */
    @WebMethod
    @WebResult(name = "idAnnotazioni", targetNamespace = "")
    @RequestWrapper(localName = "addAnnotazioni", targetNamespace = "management.acaris.acta.doqui.it", className = "it.doqui.acta.acaris.management.AddAnnotazioni")
    @ResponseWrapper(localName = "addAnnotazioniResponse", targetNamespace = "management.acaris.acta.doqui.it", className = "it.doqui.acta.acaris.management.AddAnnotazioniResponse")
    public IdAnnotazioniType addAnnotazioni(
        @WebParam(name = "repositoryId", targetNamespace = "")
        ObjectIdType repositoryId,
        @WebParam(name = "objectId", targetNamespace = "")
        ObjectIdType objectId,
        @WebParam(name = "principalId", targetNamespace = "")
        PrincipalIdType principalId,
        @WebParam(name = "annotazioni", targetNamespace = "")
        AnnotazioniPropertiesType annotazioni)
        throws AcarisException
    ;

}