
package it.csi.cosmo.cosmosoap.facade.dto.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DatiSmistaDocumentoType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DatiSmistaDocumentoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResponsabileTrattamento" type="{http://www.csi.it/stardas/services/StardasCommonTypes}CodiceFiscaleType"/>
 *         &lt;element name="IdDocumentoFruitore" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String200Type"/>
 *         &lt;element name="DocumentoElettronico" type="{http://www.csi.it/stardas/services/StardasCommonTypes}DocumentoElettronicoType"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="DatiDocumentoXML" type="{http://www.csi.it/stardas/services/StardasCommonTypes}EmbeddedXMLType"/>
 *           &lt;element name="DatiDocumentoJSON" type="{http://www.csi.it/stardas/services/StardasCommonTypes}EmbeddedJSONType"/>
 *         &lt;/choice>
 *         &lt;element name="Metadati" type="{http://www.csi.it/stardas/services/StardasCommonTypes}MetadatiType" minOccurs="0"/>
 *         &lt;element name="MessageUUIDPrincipale" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type" minOccurs="0"/>
 *         &lt;element name="NumAllegati" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="NumCopieMulticlassificazione" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="IndiceClassificazioneEsteso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="NodoArchiviazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiSmistaDocumentoType", propOrder = {
    "responsabileTrattamento",
    "idDocumentoFruitore",
    "documentoElettronico",
    "datiDocumentoXML",
    "datiDocumentoJSON",
    "metadati",
    "messageUUIDPrincipale",
    "numAllegati",
    "numCopieMulticlassificazione",
    "indiceClassificazioneEsteso",
    "nodoArchiviazione"
})
public class DatiSmistaDocumentoType {

    @XmlElement(name = "ResponsabileTrattamento", required = true)
    protected String responsabileTrattamento;
    @XmlElement(name = "IdDocumentoFruitore", required = true)
    protected String idDocumentoFruitore;
    @XmlElement(name = "DocumentoElettronico", required = true)
    protected DocumentoElettronicoType documentoElettronico;
    @XmlElement(name = "DatiDocumentoXML")
    protected EmbeddedXMLType datiDocumentoXML;
    @XmlElement(name = "DatiDocumentoJSON")
    protected EmbeddedJSONType datiDocumentoJSON;
    @XmlElement(name = "Metadati")
    protected MetadatiType metadati;
    @XmlElement(name = "MessageUUIDPrincipale")
    protected String messageUUIDPrincipale;
    @XmlElement(name = "NumAllegati")
    protected Integer numAllegati;
    @XmlElement(name = "NumCopieMulticlassificazione")
    protected Integer numCopieMulticlassificazione;
    @XmlElement(name = "IndiceClassificazioneEsteso")
    protected String indiceClassificazioneEsteso;
    @XmlElement(name = "NodoArchiviazione")
    protected String nodoArchiviazione;

    /**
     * Recupera il valore della proprietà responsabileTrattamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponsabileTrattamento() {
        return responsabileTrattamento;
    }

    /**
     * Imposta il valore della proprietà responsabileTrattamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponsabileTrattamento(String value) {
        this.responsabileTrattamento = value;
    }

    /**
     * Recupera il valore della proprietà idDocumentoFruitore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocumentoFruitore() {
        return idDocumentoFruitore;
    }

    /**
     * Imposta il valore della proprietà idDocumentoFruitore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocumentoFruitore(String value) {
        this.idDocumentoFruitore = value;
    }

    /**
     * Recupera il valore della proprietà documentoElettronico.
     * 
     * @return
     *     possible object is
     *     {@link DocumentoElettronicoType }
     *     
     */
    public DocumentoElettronicoType getDocumentoElettronico() {
        return documentoElettronico;
    }

    /**
     * Imposta il valore della proprietà documentoElettronico.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentoElettronicoType }
     *     
     */
    public void setDocumentoElettronico(DocumentoElettronicoType value) {
        this.documentoElettronico = value;
    }

    /**
     * Recupera il valore della proprietà datiDocumentoXML.
     * 
     * @return
     *     possible object is
     *     {@link EmbeddedXMLType }
     *     
     */
    public EmbeddedXMLType getDatiDocumentoXML() {
        return datiDocumentoXML;
    }

    /**
     * Imposta il valore della proprietà datiDocumentoXML.
     * 
     * @param value
     *     allowed object is
     *     {@link EmbeddedXMLType }
     *     
     */
    public void setDatiDocumentoXML(EmbeddedXMLType value) {
        this.datiDocumentoXML = value;
    }

    /**
     * Recupera il valore della proprietà datiDocumentoJSON.
     * 
     * @return
     *     possible object is
     *     {@link EmbeddedJSONType }
     *     
     */
    public EmbeddedJSONType getDatiDocumentoJSON() {
        return datiDocumentoJSON;
    }

    /**
     * Imposta il valore della proprietà datiDocumentoJSON.
     * 
     * @param value
     *     allowed object is
     *     {@link EmbeddedJSONType }
     *     
     */
    public void setDatiDocumentoJSON(EmbeddedJSONType value) {
        this.datiDocumentoJSON = value;
    }

    /**
     * Recupera il valore della proprietà metadati.
     * 
     * @return
     *     possible object is
     *     {@link MetadatiType }
     *     
     */
    public MetadatiType getMetadati() {
        return metadati;
    }

    /**
     * Imposta il valore della proprietà metadati.
     * 
     * @param value
     *     allowed object is
     *     {@link MetadatiType }
     *     
     */
    public void setMetadati(MetadatiType value) {
        this.metadati = value;
    }

    /**
     * Recupera il valore della proprietà messageUUIDPrincipale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageUUIDPrincipale() {
        return messageUUIDPrincipale;
    }

    /**
     * Imposta il valore della proprietà messageUUIDPrincipale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageUUIDPrincipale(String value) {
        this.messageUUIDPrincipale = value;
    }

    /**
     * Recupera il valore della proprietà numAllegati.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumAllegati() {
        return numAllegati;
    }

    /**
     * Imposta il valore della proprietà numAllegati.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumAllegati(Integer value) {
        this.numAllegati = value;
    }

    /**
     * Recupera il valore della proprietà numCopieMulticlassificazione.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumCopieMulticlassificazione() {
        return numCopieMulticlassificazione;
    }

    /**
     * Imposta il valore della proprietà numCopieMulticlassificazione.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumCopieMulticlassificazione(Integer value) {
        this.numCopieMulticlassificazione = value;
    }

    /**
     * Recupera il valore della proprietà indiceClassificazioneEsteso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndiceClassificazioneEsteso() {
        return indiceClassificazioneEsteso;
    }

    /**
     * Imposta il valore della proprietà indiceClassificazioneEsteso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndiceClassificazioneEsteso(String value) {
        this.indiceClassificazioneEsteso = value;
    }

    /**
     * Recupera il valore della proprietà nodoArchiviazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodoArchiviazione() {
        return nodoArchiviazione;
    }

    /**
     * Imposta il valore della proprietà nodoArchiviazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodoArchiviazione(String value) {
        this.nodoArchiviazione = value;
    }

}
