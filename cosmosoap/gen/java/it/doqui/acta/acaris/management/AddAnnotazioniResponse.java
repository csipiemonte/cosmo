
package it.doqui.acta.acaris.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.IdAnnotazioniType;


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
 *         &lt;element name="idAnnotazioni" type="{common.acaris.acta.doqui.it}IdAnnotazioniType" minOccurs="0"/>
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
    "idAnnotazioni"
})
@XmlRootElement(name = "addAnnotazioniResponse")
public class AddAnnotazioniResponse {

    protected IdAnnotazioniType idAnnotazioni;

    /**
     * Recupera il valore della proprietà idAnnotazioni.
     * 
     * @return
     *     possible object is
     *     {@link IdAnnotazioniType }
     *     
     */
    public IdAnnotazioniType getIdAnnotazioni() {
        return idAnnotazioni;
    }

    /**
     * Imposta il valore della proprietà idAnnotazioni.
     * 
     * @param value
     *     allowed object is
     *     {@link IdAnnotazioniType }
     *     
     */
    public void setIdAnnotazioni(IdAnnotazioniType value) {
        this.idAnnotazioni = value;
    }

}
