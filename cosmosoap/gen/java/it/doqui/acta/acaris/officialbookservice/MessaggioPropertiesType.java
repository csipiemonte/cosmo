
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
 * <p>Classe Java per MessaggioPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MessaggioPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{officialbookservice.acaris.acta.doqui.it}OfficialBookPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="stato" type="{common.acaris.acta.doqui.it}DecodificaType"/>
 *         &lt;element name="tipo" type="{common.acaris.acta.doqui.it}DecodificaType"/>
 *         &lt;element name="ricevuto" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="idMessaggioINPA" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="dataRicezione" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataAcquisizione" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataInvio" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="inpaAggiornato" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="riservato" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="note" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="presenzaAllegati" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="oggetto" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="idRegistrazioneProtocollo" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idUtenteCreazione" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idAOO" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idCorrispondenteMessaggio" type="{common.acaris.acta.doqui.it}ObjectIdType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessaggioPropertiesType", propOrder = {
    "stato",
    "tipo",
    "ricevuto",
    "idMessaggioINPA",
    "dataRicezione",
    "dataAcquisizione",
    "dataInvio",
    "inpaAggiornato",
    "riservato",
    "note",
    "presenzaAllegati",
    "oggetto",
    "idRegistrazioneProtocollo",
    "idUtenteCreazione",
    "idAOO",
    "idCorrispondenteMessaggio"
})
public class MessaggioPropertiesType
    extends OfficialBookPropertiesType
{

    @XmlElement(required = true)
    protected DecodificaType stato;
    @XmlElement(required = true)
    protected DecodificaType tipo;
    protected boolean ricevuto;
    @XmlElement(required = true)
    protected String idMessaggioINPA;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataRicezione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataAcquisizione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataInvio;
    protected boolean inpaAggiornato;
    protected boolean riservato;
    @XmlElement(required = true)
    protected String note;
    protected boolean presenzaAllegati;
    @XmlElement(required = true)
    protected String oggetto;
    @XmlElement(required = true)
    protected ObjectIdType idRegistrazioneProtocollo;
    @XmlElement(required = true)
    protected ObjectIdType idUtenteCreazione;
    @XmlElement(required = true)
    protected ObjectIdType idAOO;
    @XmlElement(required = true)
    protected List<ObjectIdType> idCorrispondenteMessaggio;

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
     * Recupera il valore della proprietà tipo.
     * 
     * @return
     *     possible object is
     *     {@link DecodificaType }
     *     
     */
    public DecodificaType getTipo() {
        return tipo;
    }

    /**
     * Imposta il valore della proprietà tipo.
     * 
     * @param value
     *     allowed object is
     *     {@link DecodificaType }
     *     
     */
    public void setTipo(DecodificaType value) {
        this.tipo = value;
    }

    /**
     * Recupera il valore della proprietà ricevuto.
     * 
     */
    public boolean isRicevuto() {
        return ricevuto;
    }

    /**
     * Imposta il valore della proprietà ricevuto.
     * 
     */
    public void setRicevuto(boolean value) {
        this.ricevuto = value;
    }

    /**
     * Recupera il valore della proprietà idMessaggioINPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdMessaggioINPA() {
        return idMessaggioINPA;
    }

    /**
     * Imposta il valore della proprietà idMessaggioINPA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdMessaggioINPA(String value) {
        this.idMessaggioINPA = value;
    }

    /**
     * Recupera il valore della proprietà dataRicezione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataRicezione() {
        return dataRicezione;
    }

    /**
     * Imposta il valore della proprietà dataRicezione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataRicezione(XMLGregorianCalendar value) {
        this.dataRicezione = value;
    }

    /**
     * Recupera il valore della proprietà dataAcquisizione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataAcquisizione() {
        return dataAcquisizione;
    }

    /**
     * Imposta il valore della proprietà dataAcquisizione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataAcquisizione(XMLGregorianCalendar value) {
        this.dataAcquisizione = value;
    }

    /**
     * Recupera il valore della proprietà dataInvio.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInvio() {
        return dataInvio;
    }

    /**
     * Imposta il valore della proprietà dataInvio.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInvio(XMLGregorianCalendar value) {
        this.dataInvio = value;
    }

    /**
     * Recupera il valore della proprietà inpaAggiornato.
     * 
     */
    public boolean isInpaAggiornato() {
        return inpaAggiornato;
    }

    /**
     * Imposta il valore della proprietà inpaAggiornato.
     * 
     */
    public void setInpaAggiornato(boolean value) {
        this.inpaAggiornato = value;
    }

    /**
     * Recupera il valore della proprietà riservato.
     * 
     */
    public boolean isRiservato() {
        return riservato;
    }

    /**
     * Imposta il valore della proprietà riservato.
     * 
     */
    public void setRiservato(boolean value) {
        this.riservato = value;
    }

    /**
     * Recupera il valore della proprietà note.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNote() {
        return note;
    }

    /**
     * Imposta il valore della proprietà note.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

    /**
     * Recupera il valore della proprietà presenzaAllegati.
     * 
     */
    public boolean isPresenzaAllegati() {
        return presenzaAllegati;
    }

    /**
     * Imposta il valore della proprietà presenzaAllegati.
     * 
     */
    public void setPresenzaAllegati(boolean value) {
        this.presenzaAllegati = value;
    }

    /**
     * Recupera il valore della proprietà oggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOggetto() {
        return oggetto;
    }

    /**
     * Imposta il valore della proprietà oggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOggetto(String value) {
        this.oggetto = value;
    }

    /**
     * Recupera il valore della proprietà idRegistrazioneProtocollo.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdRegistrazioneProtocollo() {
        return idRegistrazioneProtocollo;
    }

    /**
     * Imposta il valore della proprietà idRegistrazioneProtocollo.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdRegistrazioneProtocollo(ObjectIdType value) {
        this.idRegistrazioneProtocollo = value;
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
     * Recupera il valore della proprietà idAOO.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdAOO() {
        return idAOO;
    }

    /**
     * Imposta il valore della proprietà idAOO.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdAOO(ObjectIdType value) {
        this.idAOO = value;
    }

    /**
     * Gets the value of the idCorrispondenteMessaggio property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idCorrispondenteMessaggio property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdCorrispondenteMessaggio().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectIdType }
     * 
     * 
     */
    public List<ObjectIdType> getIdCorrispondenteMessaggio() {
        if (idCorrispondenteMessaggio == null) {
            idCorrispondenteMessaggio = new ArrayList<ObjectIdType>();
        }
        return this.idCorrispondenteMessaggio;
    }

}
