
package it.csi.cosmo.cosmosoap.facade.dto.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DocumentoElettronicoType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DocumentoElettronicoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NomeFile" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String1000Type"/>
 *         &lt;choice>
 *           &lt;element name="ContenutoBinario" type="{http://www.csi.it/stardas/services/StardasCommonTypes}EmbeddedBinaryType"/>
 *           &lt;element name="RiferimentoECM" type="{http://www.csi.it/stardas/services/StardasCommonTypes}RiferimentoECMType"/>
 *         &lt;/choice>
 *         &lt;element name="DocumentoFirmato" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="MimeType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentoElettronicoType", propOrder = {
    "nomeFile",
    "contenutoBinario",
    "riferimentoECM",
    "documentoFirmato",
    "mimeType"
})
public class DocumentoElettronicoType {

    @XmlElement(name = "NomeFile", required = true)
    protected String nomeFile;
    @XmlElement(name = "ContenutoBinario")
    protected EmbeddedBinaryType contenutoBinario;
    @XmlElement(name = "RiferimentoECM")
    protected RiferimentoECMType riferimentoECM;
    @XmlElement(name = "DocumentoFirmato")
    protected boolean documentoFirmato;
    @XmlElement(name = "MimeType", required = true)
    protected String mimeType;

    /**
     * Recupera il valore della proprietà nomeFile.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeFile() {
        return nomeFile;
    }

    /**
     * Imposta il valore della proprietà nomeFile.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeFile(String value) {
        this.nomeFile = value;
    }

    /**
     * Recupera il valore della proprietà contenutoBinario.
     * 
     * @return
     *     possible object is
     *     {@link EmbeddedBinaryType }
     *     
     */
    public EmbeddedBinaryType getContenutoBinario() {
        return contenutoBinario;
    }

    /**
     * Imposta il valore della proprietà contenutoBinario.
     * 
     * @param value
     *     allowed object is
     *     {@link EmbeddedBinaryType }
     *     
     */
    public void setContenutoBinario(EmbeddedBinaryType value) {
        this.contenutoBinario = value;
    }

    /**
     * Recupera il valore della proprietà riferimentoECM.
     * 
     * @return
     *     possible object is
     *     {@link RiferimentoECMType }
     *     
     */
    public RiferimentoECMType getRiferimentoECM() {
        return riferimentoECM;
    }

    /**
     * Imposta il valore della proprietà riferimentoECM.
     * 
     * @param value
     *     allowed object is
     *     {@link RiferimentoECMType }
     *     
     */
    public void setRiferimentoECM(RiferimentoECMType value) {
        this.riferimentoECM = value;
    }

    /**
     * Recupera il valore della proprietà documentoFirmato.
     * 
     */
    public boolean isDocumentoFirmato() {
        return documentoFirmato;
    }

    /**
     * Imposta il valore della proprietà documentoFirmato.
     * 
     */
    public void setDocumentoFirmato(boolean value) {
        this.documentoFirmato = value;
    }

    /**
     * Recupera il valore della proprietà mimeType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Imposta il valore della proprietà mimeType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMimeType(String value) {
        this.mimeType = value;
    }

}
