
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="identificazioneCreazione" type="{officialbookservice.acaris.acta.doqui.it}IdentificazioneAnnotazioneOB"/>
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
    "identificazioneCreazione"
})
@XmlRootElement(name = "creaAnnotazioneOBResponse")
public class CreaAnnotazioneOBResponse {

    @XmlElement(required = true)
    protected IdentificazioneAnnotazioneOB identificazioneCreazione;

    /**
     * Recupera il valore della proprietà identificazioneCreazione.
     * 
     * @return
     *     possible object is
     *     {@link IdentificazioneAnnotazioneOB }
     *     
     */
    public IdentificazioneAnnotazioneOB getIdentificazioneCreazione() {
        return identificazioneCreazione;
    }

    /**
     * Imposta il valore della proprietà identificazioneCreazione.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificazioneAnnotazioneOB }
     *     
     */
    public void setIdentificazioneCreazione(IdentificazioneAnnotazioneOB value) {
        this.identificazioneCreazione = value;
    }

}
