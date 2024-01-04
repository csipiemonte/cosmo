
package it.doqui.acta.acaris.officialbookservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import it.doqui.acta.acaris.common.DecodificaType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per RegistroProtocolloPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RegistroProtocolloPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{officialbookservice.acaris.acta.doqui.it}OfficialBookPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="anno" type="{common.acaris.acta.doqui.it}integer"/>
 *         &lt;element name="stato" type="{common.acaris.acta.doqui.it}DecodificaType"/>
 *         &lt;element name="storico" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="codiceUltimaRegistrazione" type="{common.acaris.acta.doqui.it}IDDBType"/>
 *         &lt;element name="dataUltimaRegistrazione" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataCreazione" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataApertura" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataChiusura" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataArchiviazione" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="idAOOProtocollante" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idAOOResponsabile" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idStrutturaResponsabile" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idNodoResponsabile" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idUtenteCreazione" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idAnnotazioneOB" type="{common.acaris.acta.doqui.it}ObjectIdType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idClassificazioneDocStampaDefinitiva" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idAggregazioneDocStampaDefinitiva" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistroProtocolloPropertiesType", propOrder = {
    "anno",
    "stato",
    "storico",
    "codiceUltimaRegistrazione",
    "dataUltimaRegistrazione",
    "dataCreazione",
    "dataApertura",
    "dataChiusura",
    "dataArchiviazione",
    "idAOOProtocollante",
    "idAOOResponsabile",
    "idStrutturaResponsabile",
    "idNodoResponsabile",
    "idUtenteCreazione",
    "idAnnotazioneOB",
    "idClassificazioneDocStampaDefinitiva",
    "idAggregazioneDocStampaDefinitiva"
})
public class RegistroProtocolloPropertiesType
    extends OfficialBookPropertiesType
{

    protected int anno;
    @XmlElement(required = true)
    protected DecodificaType stato;
    protected boolean storico;
    protected long codiceUltimaRegistrazione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataUltimaRegistrazione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataCreazione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataApertura;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataChiusura;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataArchiviazione;
    @XmlElement(required = true)
    protected ObjectIdType idAOOProtocollante;
    @XmlElement(required = true)
    protected ObjectIdType idAOOResponsabile;
    @XmlElement(required = true)
    protected ObjectIdType idStrutturaResponsabile;
    @XmlElement(required = true)
    protected ObjectIdType idNodoResponsabile;
    @XmlElement(required = true)
    protected ObjectIdType idUtenteCreazione;
    protected List<ObjectIdType> idAnnotazioneOB;
    @XmlElement(required = true)
    protected ObjectIdType idClassificazioneDocStampaDefinitiva;
    @XmlElement(required = true)
    protected ObjectIdType idAggregazioneDocStampaDefinitiva;

    /**
     * Recupera il valore della proprietà anno.
     * 
     */
    public int getAnno() {
        return anno;
    }

    /**
     * Imposta il valore della proprietà anno.
     * 
     */
    public void setAnno(int value) {
        this.anno = value;
    }

    /**
     * Recupera il valore della proprietà stato.
     * 
     * @return
     *     possible object is
     *     {@link DecodificaType }
     *     
     */
    public DecodificaType getStato() {
        return stato;
    }

    /**
     * Imposta il valore della proprietà stato.
     * 
     * @param value
     *     allowed object is
     *     {@link DecodificaType }
     *     
     */
    public void setStato(DecodificaType value) {
        this.stato = value;
    }

    /**
     * Recupera il valore della proprietà storico.
     * 
     */
    public boolean isStorico() {
        return storico;
    }

    /**
     * Imposta il valore della proprietà storico.
     * 
     */
    public void setStorico(boolean value) {
        this.storico = value;
    }

    /**
     * Recupera il valore della proprietà codiceUltimaRegistrazione.
     * 
     */
    public long getCodiceUltimaRegistrazione() {
        return codiceUltimaRegistrazione;
    }

    /**
     * Imposta il valore della proprietà codiceUltimaRegistrazione.
     * 
     */
    public void setCodiceUltimaRegistrazione(long value) {
        this.codiceUltimaRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà dataUltimaRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataUltimaRegistrazione() {
        return dataUltimaRegistrazione;
    }

    /**
     * Imposta il valore della proprietà dataUltimaRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataUltimaRegistrazione(XMLGregorianCalendar value) {
        this.dataUltimaRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà dataCreazione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataCreazione() {
        return dataCreazione;
    }

    /**
     * Imposta il valore della proprietà dataCreazione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataCreazione(XMLGregorianCalendar value) {
        this.dataCreazione = value;
    }

    /**
     * Recupera il valore della proprietà dataApertura.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataApertura() {
        return dataApertura;
    }

    /**
     * Imposta il valore della proprietà dataApertura.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataApertura(XMLGregorianCalendar value) {
        this.dataApertura = value;
    }

    /**
     * Recupera il valore della proprietà dataChiusura.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataChiusura() {
        return dataChiusura;
    }

    /**
     * Imposta il valore della proprietà dataChiusura.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataChiusura(XMLGregorianCalendar value) {
        this.dataChiusura = value;
    }

    /**
     * Recupera il valore della proprietà dataArchiviazione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataArchiviazione() {
        return dataArchiviazione;
    }

    /**
     * Imposta il valore della proprietà dataArchiviazione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataArchiviazione(XMLGregorianCalendar value) {
        this.dataArchiviazione = value;
    }

    /**
     * Recupera il valore della proprietà idAOOProtocollante.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdAOOProtocollante() {
        return idAOOProtocollante;
    }

    /**
     * Imposta il valore della proprietà idAOOProtocollante.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdAOOProtocollante(ObjectIdType value) {
        this.idAOOProtocollante = value;
    }

    /**
     * Recupera il valore della proprietà idAOOResponsabile.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdAOOResponsabile() {
        return idAOOResponsabile;
    }

    /**
     * Imposta il valore della proprietà idAOOResponsabile.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdAOOResponsabile(ObjectIdType value) {
        this.idAOOResponsabile = value;
    }

    /**
     * Recupera il valore della proprietà idStrutturaResponsabile.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdStrutturaResponsabile() {
        return idStrutturaResponsabile;
    }

    /**
     * Imposta il valore della proprietà idStrutturaResponsabile.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdStrutturaResponsabile(ObjectIdType value) {
        this.idStrutturaResponsabile = value;
    }

    /**
     * Recupera il valore della proprietà idNodoResponsabile.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdNodoResponsabile() {
        return idNodoResponsabile;
    }

    /**
     * Imposta il valore della proprietà idNodoResponsabile.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdNodoResponsabile(ObjectIdType value) {
        this.idNodoResponsabile = value;
    }

    /**
     * Recupera il valore della proprietà idUtenteCreazione.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdUtenteCreazione() {
        return idUtenteCreazione;
    }

    /**
     * Imposta il valore della proprietà idUtenteCreazione.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdUtenteCreazione(ObjectIdType value) {
        this.idUtenteCreazione = value;
    }

    /**
     * Gets the value of the idAnnotazioneOB property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idAnnotazioneOB property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdAnnotazioneOB().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectIdType }
     * 
     * 
     */
    public List<ObjectIdType> getIdAnnotazioneOB() {
        if (idAnnotazioneOB == null) {
            idAnnotazioneOB = new ArrayList<ObjectIdType>();
        }
        return this.idAnnotazioneOB;
    }

    /**
     * Recupera il valore della proprietà idClassificazioneDocStampaDefinitiva.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdClassificazioneDocStampaDefinitiva() {
        return idClassificazioneDocStampaDefinitiva;
    }

    /**
     * Imposta il valore della proprietà idClassificazioneDocStampaDefinitiva.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdClassificazioneDocStampaDefinitiva(ObjectIdType value) {
        this.idClassificazioneDocStampaDefinitiva = value;
    }

    /**
     * Recupera il valore della proprietà idAggregazioneDocStampaDefinitiva.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdAggregazioneDocStampaDefinitiva() {
        return idAggregazioneDocStampaDefinitiva;
    }

    /**
     * Imposta il valore della proprietà idAggregazioneDocStampaDefinitiva.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdAggregazioneDocStampaDefinitiva(ObjectIdType value) {
        this.idAggregazioneDocStampaDefinitiva = value;
    }

}
