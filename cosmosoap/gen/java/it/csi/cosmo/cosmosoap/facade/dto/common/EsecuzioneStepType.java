
package it.csi.cosmo.cosmosoap.facade.dto.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per EsecuzioneStepType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="EsecuzioneStepType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdEsecuzioneStep" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="CodiceEsito" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DettaglioEsito" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InformazioniAggiuntive" type="{http://www.csi.it/stardas/services/StardasCommonTypes}InformazioniAggiuntiveType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EsecuzioneStepType", propOrder = {
    "idEsecuzioneStep",
    "codiceEsito",
    "dettaglioEsito",
    "informazioniAggiuntive"
})
public class EsecuzioneStepType {

    @XmlElement(name = "IdEsecuzioneStep")
    protected long idEsecuzioneStep;
    @XmlElement(name = "CodiceEsito", required = true)
    protected String codiceEsito;
    @XmlElement(name = "DettaglioEsito", required = true)
    protected String dettaglioEsito;
    @XmlElement(name = "InformazioniAggiuntive")
    protected InformazioniAggiuntiveType informazioniAggiuntive;

    /**
     * Recupera il valore della proprietà idEsecuzioneStep.
     * 
     */
    public long getIdEsecuzioneStep() {
        return idEsecuzioneStep;
    }

    /**
     * Imposta il valore della proprietà idEsecuzioneStep.
     * 
     */
    public void setIdEsecuzioneStep(long value) {
        this.idEsecuzioneStep = value;
    }

    /**
     * Recupera il valore della proprietà codiceEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceEsito() {
        return codiceEsito;
    }

    /**
     * Imposta il valore della proprietà codiceEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceEsito(String value) {
        this.codiceEsito = value;
    }

    /**
     * Recupera il valore della proprietà dettaglioEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDettaglioEsito() {
        return dettaglioEsito;
    }

    /**
     * Imposta il valore della proprietà dettaglioEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDettaglioEsito(String value) {
        this.dettaglioEsito = value;
    }

    /**
     * Recupera il valore della proprietà informazioniAggiuntive.
     * 
     * @return
     *     possible object is
     *     {@link InformazioniAggiuntiveType }
     *     
     */
    public InformazioniAggiuntiveType getInformazioniAggiuntive() {
        return informazioniAggiuntive;
    }

    /**
     * Imposta il valore della proprietà informazioniAggiuntive.
     * 
     * @param value
     *     allowed object is
     *     {@link InformazioniAggiuntiveType }
     *     
     */
    public void setInformazioniAggiuntive(InformazioniAggiuntiveType value) {
        this.informazioniAggiuntive = value;
    }

}
