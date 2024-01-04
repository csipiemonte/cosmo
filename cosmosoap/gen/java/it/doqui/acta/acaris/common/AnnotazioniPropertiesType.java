
package it.doqui.acta.acaris.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per AnnotazioniPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="AnnotazioniPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{common.acaris.acta.doqui.it}CommonPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="objectTypeId" type="{common.acaris.acta.doqui.it}enumCommonObjectType"/>
 *         &lt;element name="descrizione" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="idUtente" type="{common.acaris.acta.doqui.it}IdUtenteType"/>
 *         &lt;element name="data" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="annotazioneFormale" type="{common.acaris.acta.doqui.it}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnnotazioniPropertiesType", propOrder = {
    "objectTypeId",
    "descrizione",
    "idUtente",
    "data",
    "annotazioneFormale"
})
public class AnnotazioniPropertiesType
    extends CommonPropertiesType
{

    @XmlElement(required = true)
    protected EnumCommonObjectType objectTypeId;
    @XmlElement(required = true)
    protected String descrizione;
    @XmlElement(required = true)
    protected IdUtenteType idUtente;
    @XmlElement(required = true)
    protected XMLGregorianCalendar data;
    protected boolean annotazioneFormale;

    /**
     * Recupera il valore della proprietà objectTypeId.
     * 
     * @return
     *     possible object is
     *     {@link EnumCommonObjectType }
     *     
     */
    public EnumCommonObjectType getObjectTypeId() {
        return objectTypeId;
    }

    /**
     * Imposta il valore della proprietà objectTypeId.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumCommonObjectType }
     *     
     */
    public void setObjectTypeId(EnumCommonObjectType value) {
        this.objectTypeId = value;
    }

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
     * Recupera il valore della proprietà idUtente.
     * 
     * @return
     *     possible object is
     *     {@link IdUtenteType }
     *     
     */
    public IdUtenteType getIdUtente() {
        return idUtente;
    }

    /**
     * Imposta il valore della proprietà idUtente.
     * 
     * @param value
     *     allowed object is
     *     {@link IdUtenteType }
     *     
     */
    public void setIdUtente(IdUtenteType value) {
        this.idUtente = value;
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

}
