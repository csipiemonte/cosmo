
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per RegistrazioneAPI complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RegistrazioneAPI">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoRegistrazione" type="{officialbookservice.acaris.acta.doqui.it}enumTipoAPI"/>
 *         &lt;element name="infoCreazione" type="{officialbookservice.acaris.acta.doqui.it}InfoCreazioneRegistrazione"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistrazioneAPI", propOrder = {
    "tipoRegistrazione",
    "infoCreazione"
})
@XmlSeeAlso({
    RegistrazioneInterna.class,
    RegistrazionePartenza.class,
    RegistrazioneArrivo.class
})
public class RegistrazioneAPI {

    @XmlElement(required = true)
    protected EnumTipoAPI tipoRegistrazione;
    @XmlElement(required = true)
    protected InfoCreazioneRegistrazione infoCreazione;

    /**
     * Recupera il valore della proprietà tipoRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipoAPI }
     *     
     */
    public EnumTipoAPI getTipoRegistrazione() {
        return tipoRegistrazione;
    }

    /**
     * Imposta il valore della proprietà tipoRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipoAPI }
     *     
     */
    public void setTipoRegistrazione(EnumTipoAPI value) {
        this.tipoRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà infoCreazione.
     * 
     * @return
     *     possible object is
     *     {@link InfoCreazioneRegistrazione }
     *     
     */
    public InfoCreazioneRegistrazione getInfoCreazione() {
        return infoCreazione;
    }

    /**
     * Imposta il valore della proprietà infoCreazione.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoCreazioneRegistrazione }
     *     
     */
    public void setInfoCreazione(InfoCreazioneRegistrazione value) {
        this.infoCreazione = value;
    }

}
