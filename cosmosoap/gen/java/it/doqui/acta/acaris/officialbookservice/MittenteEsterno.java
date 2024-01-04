
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per MittenteEsterno complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MittenteEsterno">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="corrispondente" type="{officialbookservice.acaris.acta.doqui.it}InfoCreazioneCorrispondente"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MittenteEsterno", propOrder = {
    "corrispondente"
})
public class MittenteEsterno {

    @XmlElement(required = true)
    protected InfoCreazioneCorrispondente corrispondente;

    /**
     * Recupera il valore della proprietà corrispondente.
     * 
     * @return
     *     possible object is
     *     {@link InfoCreazioneCorrispondente }
     *     
     */
    public InfoCreazioneCorrispondente getCorrispondente() {
        return corrispondente;
    }

    /**
     * Imposta il valore della proprietà corrispondente.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoCreazioneCorrispondente }
     *     
     */
    public void setCorrispondente(InfoCreazioneCorrispondente value) {
        this.corrispondente = value;
    }

}
