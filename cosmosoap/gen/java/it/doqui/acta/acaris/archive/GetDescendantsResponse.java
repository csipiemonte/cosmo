
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.PagingResponseType;


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
 *         &lt;element name="objects" type="{common.acaris.acta.doqui.it}PagingResponseType" minOccurs="0"/>
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
    "objects"
})
@XmlRootElement(name = "getDescendantsResponse")
public class GetDescendantsResponse {

    protected PagingResponseType objects;

    /**
     * Recupera il valore della proprietà objects.
     * 
     * @return
     *     possible object is
     *     {@link PagingResponseType }
     *     
     */
    public PagingResponseType getObjects() {
        return objects;
    }

    /**
     * Imposta il valore della proprietà objects.
     * 
     * @param value
     *     allowed object is
     *     {@link PagingResponseType }
     *     
     */
    public void setObjects(PagingResponseType value) {
        this.objects = value;
    }

}
