
package it.doqui.acta.acaris.backoffice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per RelazioniStrutturaPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RelazioniStrutturaPropertiesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idStrutturaPartenza" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="ruoloStrutturaPartenza" type="{backoffice.acaris.acta.doqui.it}enumRuoloInGerarchiaType"/>
 *         &lt;element name="idStrutturaRelazionata" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idParentStrutturaRelazionata" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idParentStrutturaRelazionataInChiaro" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="level" type="{common.acaris.acta.doqui.it}integer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RelazioniStrutturaPropertiesType", propOrder = {
    "idStrutturaPartenza",
    "ruoloStrutturaPartenza",
    "idStrutturaRelazionata",
    "idParentStrutturaRelazionata",
    "idParentStrutturaRelazionataInChiaro",
    "level"
})
public class RelazioniStrutturaPropertiesType {

    @XmlElement(required = true)
    protected ObjectIdType idStrutturaPartenza;
    @XmlElement(required = true)
    protected EnumRuoloInGerarchiaType ruoloStrutturaPartenza;
    @XmlElement(required = true)
    protected ObjectIdType idStrutturaRelazionata;
    @XmlElement(required = true)
    protected ObjectIdType idParentStrutturaRelazionata;
    @XmlElement(required = true)
    protected String idParentStrutturaRelazionataInChiaro;
    protected int level;

    /**
     * Recupera il valore della proprietà idStrutturaPartenza.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdStrutturaPartenza() {
        return idStrutturaPartenza;
    }

    /**
     * Imposta il valore della proprietà idStrutturaPartenza.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdStrutturaPartenza(ObjectIdType value) {
        this.idStrutturaPartenza = value;
    }

    /**
     * Recupera il valore della proprietà ruoloStrutturaPartenza.
     * 
     * @return
     *     possible object is
     *     {@link EnumRuoloInGerarchiaType }
     *     
     */
    public EnumRuoloInGerarchiaType getRuoloStrutturaPartenza() {
        return ruoloStrutturaPartenza;
    }

    /**
     * Imposta il valore della proprietà ruoloStrutturaPartenza.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumRuoloInGerarchiaType }
     *     
     */
    public void setRuoloStrutturaPartenza(EnumRuoloInGerarchiaType value) {
        this.ruoloStrutturaPartenza = value;
    }

    /**
     * Recupera il valore della proprietà idStrutturaRelazionata.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdStrutturaRelazionata() {
        return idStrutturaRelazionata;
    }

    /**
     * Imposta il valore della proprietà idStrutturaRelazionata.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdStrutturaRelazionata(ObjectIdType value) {
        this.idStrutturaRelazionata = value;
    }

    /**
     * Recupera il valore della proprietà idParentStrutturaRelazionata.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdParentStrutturaRelazionata() {
        return idParentStrutturaRelazionata;
    }

    /**
     * Imposta il valore della proprietà idParentStrutturaRelazionata.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdParentStrutturaRelazionata(ObjectIdType value) {
        this.idParentStrutturaRelazionata = value;
    }

    /**
     * Recupera il valore della proprietà idParentStrutturaRelazionataInChiaro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdParentStrutturaRelazionataInChiaro() {
        return idParentStrutturaRelazionataInChiaro;
    }

    /**
     * Imposta il valore della proprietà idParentStrutturaRelazionataInChiaro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdParentStrutturaRelazionataInChiaro(String value) {
        this.idParentStrutturaRelazionataInChiaro = value;
    }

    /**
     * Recupera il valore della proprietà level.
     * 
     */
    public int getLevel() {
        return level;
    }

    /**
     * Imposta il valore della proprietà level.
     * 
     */
    public void setLevel(int value) {
        this.level = value;
    }

}
