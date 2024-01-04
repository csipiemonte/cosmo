
package it.doqui.acta.acaris.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="movimentazione" type="{management.acaris.acta.doqui.it}MovimentazioneType" minOccurs="0"/>
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
    "movimentazione"
})
@XmlRootElement(name = "getMovimentazioneResponse")
public class GetMovimentazioneResponse {

    protected MovimentazioneType movimentazione;

    /**
     * Recupera il valore della proprietà movimentazione.
     * 
     * @return
     *     possible object is
     *     {@link MovimentazioneType }
     *     
     */
    public MovimentazioneType getMovimentazione() {
        return movimentazione;
    }

    /**
     * Imposta il valore della proprietà movimentazione.
     * 
     * @param value
     *     allowed object is
     *     {@link MovimentazioneType }
     *     
     */
    public void setMovimentazione(MovimentazioneType value) {
        this.movimentazione = value;
    }

}
