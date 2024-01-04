
package it.doqui.acta.acaris.backoffice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectResponseType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="object" type="{common.acaris.acta.doqui.it}ObjectResponseType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "object"
})
@XmlRootElement(name = "getPropertiesResponse")
public class GetPropertiesResponse {

    protected ObjectResponseType object;

    /**
     * Recupera il valore della proprietà object.
     * 
     * @return
     *     possible object is
     *     {@link ObjectResponseType }
     *     
     */
    public ObjectResponseType getObject() {
        return object;
    }

    /**
     * Imposta il valore della proprietà object.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectResponseType }
     *     
     */
    public void setObject(ObjectResponseType value) {
        this.object = value;
    }

}
