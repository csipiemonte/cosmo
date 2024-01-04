
package it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.csi.cosmo.cosmosoap.facade.dto.common.ResponseType;


/**
 * <p>Classe Java per SmistaDocumentoResponseType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="SmistaDocumentoResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.csi.it/stardas/services/StardasCommonTypes}ResponseType">
 *       &lt;sequence>
 *         &lt;element name="MessageUUID" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SmistaDocumentoResponseType", propOrder = {
    "messageUUID"
})
public class SmistaDocumentoResponseType
    extends ResponseType
{

    @XmlElement(name = "MessageUUID", required = true)
    protected String messageUUID;

    /**
     * Recupera il valore della proprietà messageUUID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageUUID() {
        return messageUUID;
    }

    /**
     * Imposta il valore della proprietà messageUUID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageUUID(String value) {
        this.messageUUID = value;
    }

}
