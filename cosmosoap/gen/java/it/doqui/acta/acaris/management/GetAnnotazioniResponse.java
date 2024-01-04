
package it.doqui.acta.acaris.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.AnnotazioniPropertiesType;


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
 *         &lt;element name="annotazioni" type="{common.acaris.acta.doqui.it}AnnotazioniPropertiesType" minOccurs="0"/>
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
    "annotazioni"
})
@XmlRootElement(name = "getAnnotazioniResponse")
public class GetAnnotazioniResponse {

    protected AnnotazioniPropertiesType annotazioni;

    /**
     * Recupera il valore della proprietà annotazioni.
     * 
     * @return
     *     possible object is
     *     {@link AnnotazioniPropertiesType }
     *     
     */
    public AnnotazioniPropertiesType getAnnotazioni() {
        return annotazioni;
    }

    /**
     * Imposta il valore della proprietà annotazioni.
     * 
     * @param value
     *     allowed object is
     *     {@link AnnotazioniPropertiesType }
     *     
     */
    public void setAnnotazioni(AnnotazioniPropertiesType value) {
        this.annotazioni = value;
    }

}
