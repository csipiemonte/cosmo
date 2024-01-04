
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per AnnotazioneOBPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="AnnotazioneOBPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{officialbookservice.acaris.acta.doqui.it}OfficialBookPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="descrizione" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="data" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="annotazioneFormale" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="idUtenteCreazione" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idRegistrazione" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idRegistro" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnnotazioneOBPropertiesType", propOrder = {
    "descrizione",
    "data",
    "annotazioneFormale",
    "idUtenteCreazione",
    "idRegistrazione",
    "idRegistro"
})
public class AnnotazioneOBPropertiesType
    extends OfficialBookPropertiesType
{

    @XmlElement(required = true)
    protected String descrizione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar data;
    protected boolean annotazioneFormale;
    @XmlElement(required = true)
    protected ObjectIdType idUtenteCreazione;
    @XmlElement(required = true)
    protected ObjectIdType idRegistrazione;
    @XmlElement(required = true)
    protected ObjectIdType idRegistro;

    /**
     * Recupera il valore della proprietà descrizione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta il valore della proprietà descrizione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizione(String value) {
        this.descrizione = value;
    }

    /**
     * Recupera il valore della proprietà data.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getData() {
        return data;
    }

    /**
     * Imposta il valore della proprietà data.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setData(XMLGregorianCalendar value) {
        this.data = value;
    }

    /**
     * Recupera il valore della proprietà annotazioneFormale.
     * 
     */
    public boolean isAnnotazioneFormale() {
        return annotazioneFormale;
    }

    /**
     * Imposta il valore della proprietà annotazioneFormale.
     * 
     */
    public void setAnnotazioneFormale(boolean value) {
        this.annotazioneFormale = value;
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
     * Recupera il valore della proprietà idRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdRegistrazione() {
        return idRegistrazione;
    }

    /**
     * Imposta il valore della proprietà idRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdRegistrazione(ObjectIdType value) {
        this.idRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà idRegistro.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdRegistro() {
        return idRegistro;
    }

    /**
     * Imposta il valore della proprietà idRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdRegistro(ObjectIdType value) {
        this.idRegistro = value;
    }

}
