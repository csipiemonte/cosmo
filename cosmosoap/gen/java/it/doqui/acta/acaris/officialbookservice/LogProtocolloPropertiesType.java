
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per LogProtocolloPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="LogProtocolloPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{officialbookservice.acaris.acta.doqui.it}OfficialBookPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="dataModifica" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="vecchioValore" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="idCampo" type="{common.acaris.acta.doqui.it}integer"/>
 *         &lt;element name="idRegistroProtocollo" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idRegistrazione" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idUtenteCreazione" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LogProtocolloPropertiesType", propOrder = {
    "dataModifica",
    "vecchioValore",
    "idCampo",
    "idRegistroProtocollo",
    "idRegistrazione",
    "idUtenteCreazione"
})
public class LogProtocolloPropertiesType
    extends OfficialBookPropertiesType
{

    @XmlElement(required = true)
    protected XMLGregorianCalendar dataModifica;
    @XmlElement(required = true)
    protected String vecchioValore;
    protected int idCampo;
    @XmlElement(required = true)
    protected ObjectIdType idRegistroProtocollo;
    @XmlElement(required = true)
    protected ObjectIdType idRegistrazione;
    @XmlElement(required = true)
    protected ObjectIdType idUtenteCreazione;

    /**
     * Recupera il valore della proprietà dataModifica.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataModifica() {
        return dataModifica;
    }

    /**
     * Imposta il valore della proprietà dataModifica.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataModifica(XMLGregorianCalendar value) {
        this.dataModifica = value;
    }

    /**
     * Recupera il valore della proprietà vecchioValore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVecchioValore() {
        return vecchioValore;
    }

    /**
     * Imposta il valore della proprietà vecchioValore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVecchioValore(String value) {
        this.vecchioValore = value;
    }

    /**
     * Recupera il valore della proprietà idCampo.
     * 
     */
    public int getIdCampo() {
        return idCampo;
    }

    /**
     * Imposta il valore della proprietà idCampo.
     * 
     */
    public void setIdCampo(int value) {
        this.idCampo = value;
    }

    /**
     * Recupera il valore della proprietà idRegistroProtocollo.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdRegistroProtocollo() {
        return idRegistroProtocollo;
    }

    /**
     * Imposta il valore della proprietà idRegistroProtocollo.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdRegistroProtocollo(ObjectIdType value) {
        this.idRegistroProtocollo = value;
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

}
