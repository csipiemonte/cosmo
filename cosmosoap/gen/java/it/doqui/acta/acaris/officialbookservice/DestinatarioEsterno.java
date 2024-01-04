
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DestinatarioEsterno complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DestinatarioEsterno">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="corrispondente" type="{officialbookservice.acaris.acta.doqui.it}InfoCreazioneCorrispondente"/>
 *         &lt;element name="idRuoloCorrispondente" type="{common.acaris.acta.doqui.it}IDDBType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DestinatarioEsterno", propOrder = {
    "corrispondente",
    "idRuoloCorrispondente"
})
public class DestinatarioEsterno {

    @XmlElement(required = true)
    protected InfoCreazioneCorrispondente corrispondente;
    protected long idRuoloCorrispondente;

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

    /**
     * Recupera il valore della proprietà idRuoloCorrispondente.
     * 
     */
    public long getIdRuoloCorrispondente() {
        return idRuoloCorrispondente;
    }

    /**
     * Imposta il valore della proprietà idRuoloCorrispondente.
     * 
     */
    public void setIdRuoloCorrispondente(long value) {
        this.idRuoloCorrispondente = value;
    }

}
