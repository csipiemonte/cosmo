
package it.csi.cosmo.cosmosoap.facade.dto.common;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per DettaglioCompletoRichiestaType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DettaglioCompletoRichiestaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodEnte" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StatoRichiesta" type="{http://www.csi.it/stardas/services/StardasCommonTypes}StatoRichiestaType"/>
 *         &lt;element name="CodTipoApplicazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MessageUuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CodTipoDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdDocFruitore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NomeFile" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdMymeType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TipoTrattamento" type="{http://www.csi.it/stardas/services/StardasCommonTypes}TipoTrattamentoType"/>
 *         &lt;element name="MessageStore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataInserimento" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="DataUltimaVariazione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="DataCallback" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="DocumentoFirmato" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CodiceEsito" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DettaglioEsito" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CodFruitore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ElencoAllSteps" type="{http://www.csi.it/stardas/services/StardasCommonTypes}EsecuzioneStepType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ElencoSoloUltimiStep" type="{http://www.csi.it/stardas/services/StardasCommonTypes}EsecuzioneStepType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DettaglioCompletoRichiestaType", propOrder = {
    "codEnte",
    "statoRichiesta",
    "codTipoApplicazione",
    "messageUuid",
    "codTipoDocumento",
    "idDocFruitore",
    "nomeFile",
    "idMymeType",
    "tipoTrattamento",
    "messageStore",
    "dataInserimento",
    "dataUltimaVariazione",
    "dataCallback",
    "documentoFirmato",
    "codiceEsito",
    "dettaglioEsito",
    "codFruitore",
    "elencoAllSteps",
    "elencoSoloUltimiStep"
})
public class DettaglioCompletoRichiestaType {

    @XmlElement(name = "CodEnte", required = true)
    protected String codEnte;
    @XmlElement(name = "StatoRichiesta", required = true)
    protected StatoRichiestaType statoRichiesta;
    @XmlElement(name = "CodTipoApplicazione", required = true)
    protected String codTipoApplicazione;
    @XmlElement(name = "MessageUuid", required = true)
    protected String messageUuid;
    @XmlElement(name = "CodTipoDocumento", required = true)
    protected String codTipoDocumento;
    @XmlElement(name = "IdDocFruitore", required = true)
    protected String idDocFruitore;
    @XmlElement(name = "NomeFile", required = true)
    protected String nomeFile;
    @XmlElement(name = "IdMymeType", required = true)
    protected String idMymeType;
    @XmlElement(name = "TipoTrattamento", required = true)
    protected TipoTrattamentoType tipoTrattamento;
    @XmlElement(name = "MessageStore", required = true)
    protected String messageStore;
    @XmlElement(name = "DataInserimento", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataInserimento;
    @XmlElement(name = "DataUltimaVariazione", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataUltimaVariazione;
    @XmlElement(name = "DataCallback", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataCallback;
    @XmlElement(name = "DocumentoFirmato")
    protected boolean documentoFirmato;
    @XmlElement(name = "CodiceEsito", required = true)
    protected String codiceEsito;
    @XmlElement(name = "DettaglioEsito", required = true)
    protected String dettaglioEsito;
    @XmlElement(name = "CodFruitore", required = true)
    protected String codFruitore;
    @XmlElement(name = "ElencoAllSteps")
    protected List<EsecuzioneStepType> elencoAllSteps;
    @XmlElement(name = "ElencoSoloUltimiStep")
    protected List<EsecuzioneStepType> elencoSoloUltimiStep;

    /**
     * Recupera il valore della proprietà codEnte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodEnte() {
        return codEnte;
    }

    /**
     * Imposta il valore della proprietà codEnte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodEnte(String value) {
        this.codEnte = value;
    }

    /**
     * Recupera il valore della proprietà statoRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link StatoRichiestaType }
     *     
     */
    public StatoRichiestaType getStatoRichiesta() {
        return statoRichiesta;
    }

    /**
     * Imposta il valore della proprietà statoRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link StatoRichiestaType }
     *     
     */
    public void setStatoRichiesta(StatoRichiestaType value) {
        this.statoRichiesta = value;
    }

    /**
     * Recupera il valore della proprietà codTipoApplicazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoApplicazione() {
        return codTipoApplicazione;
    }

    /**
     * Imposta il valore della proprietà codTipoApplicazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoApplicazione(String value) {
        this.codTipoApplicazione = value;
    }

    /**
     * Recupera il valore della proprietà messageUuid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageUuid() {
        return messageUuid;
    }

    /**
     * Imposta il valore della proprietà messageUuid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageUuid(String value) {
        this.messageUuid = value;
    }

    /**
     * Recupera il valore della proprietà codTipoDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoDocumento() {
        return codTipoDocumento;
    }

    /**
     * Imposta il valore della proprietà codTipoDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoDocumento(String value) {
        this.codTipoDocumento = value;
    }

    /**
     * Recupera il valore della proprietà idDocFruitore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocFruitore() {
        return idDocFruitore;
    }

    /**
     * Imposta il valore della proprietà idDocFruitore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocFruitore(String value) {
        this.idDocFruitore = value;
    }

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
     * Recupera il valore della proprietà idMymeType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdMymeType() {
        return idMymeType;
    }

    /**
     * Imposta il valore della proprietà idMymeType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdMymeType(String value) {
        this.idMymeType = value;
    }

    /**
     * Recupera il valore della proprietà tipoTrattamento.
     * 
     * @return
     *     possible object is
     *     {@link TipoTrattamentoType }
     *     
     */
    public TipoTrattamentoType getTipoTrattamento() {
        return tipoTrattamento;
    }

    /**
     * Imposta il valore della proprietà tipoTrattamento.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoTrattamentoType }
     *     
     */
    public void setTipoTrattamento(TipoTrattamentoType value) {
        this.tipoTrattamento = value;
    }

    /**
     * Recupera il valore della proprietà messageStore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageStore() {
        return messageStore;
    }

    /**
     * Imposta il valore della proprietà messageStore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageStore(String value) {
        this.messageStore = value;
    }

    /**
     * Recupera il valore della proprietà dataInserimento.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInserimento() {
        return dataInserimento;
    }

    /**
     * Imposta il valore della proprietà dataInserimento.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInserimento(XMLGregorianCalendar value) {
        this.dataInserimento = value;
    }

    /**
     * Recupera il valore della proprietà dataUltimaVariazione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataUltimaVariazione() {
        return dataUltimaVariazione;
    }

    /**
     * Imposta il valore della proprietà dataUltimaVariazione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataUltimaVariazione(XMLGregorianCalendar value) {
        this.dataUltimaVariazione = value;
    }

    /**
     * Recupera il valore della proprietà dataCallback.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataCallback() {
        return dataCallback;
    }

    /**
     * Imposta il valore della proprietà dataCallback.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataCallback(XMLGregorianCalendar value) {
        this.dataCallback = value;
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
     * Recupera il valore della proprietà codFruitore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodFruitore() {
        return codFruitore;
    }

    /**
     * Imposta il valore della proprietà codFruitore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodFruitore(String value) {
        this.codFruitore = value;
    }

    /**
     * Gets the value of the elencoAllSteps property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoAllSteps property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoAllSteps().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EsecuzioneStepType }
     * 
     * 
     */
    public List<EsecuzioneStepType> getElencoAllSteps() {
        if (elencoAllSteps == null) {
            elencoAllSteps = new ArrayList<EsecuzioneStepType>();
        }
        return this.elencoAllSteps;
    }

    /**
     * Gets the value of the elencoSoloUltimiStep property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoSoloUltimiStep property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoSoloUltimiStep().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EsecuzioneStepType }
     * 
     * 
     */
    public List<EsecuzioneStepType> getElencoSoloUltimiStep() {
        if (elencoSoloUltimiStep == null) {
            elencoSoloUltimiStep = new ArrayList<EsecuzioneStepType>();
        }
        return this.elencoSoloUltimiStep;
    }

}
