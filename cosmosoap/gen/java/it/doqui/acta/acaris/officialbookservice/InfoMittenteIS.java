
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per InfoMittenteIS complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="InfoMittenteIS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identificatore" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="casella" type="{common.acaris.acta.doqui.it}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InfoMittenteIS", propOrder = {
    "identificatore",
    "casella"
})
public class InfoMittenteIS {

    @XmlElement(required = true)
    protected ObjectIdType identificatore;
    @XmlElement(required = true)
    protected String casella;

    /**
     * Recupera il valore della proprietà identificatore.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdentificatore() {
        return identificatore;
    }

    /**
     * Imposta il valore della proprietà identificatore.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdentificatore(ObjectIdType value) {
        this.identificatore = value;
    }

    /**
     * Recupera il valore della proprietà casella.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCasella() {
        return casella;
    }

    /**
     * Imposta il valore della proprietà casella.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCasella(String value) {
        this.casella = value;
    }

}
