
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per IndirizzoTelematicoType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="IndirizzoTelematicoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="indirizzo" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="tipo" type="{officialbookservice.acaris.acta.doqui.it}enumTipoIndirizzoTelematico"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndirizzoTelematicoType", propOrder = {
    "indirizzo",
    "tipo"
})
public class IndirizzoTelematicoType {

    @XmlElement(required = true)
    protected String indirizzo;
    @XmlElement(required = true)
    protected EnumTipoIndirizzoTelematico tipo;

    /**
     * Recupera il valore della proprietà indirizzo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * Imposta il valore della proprietà indirizzo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzo(String value) {
        this.indirizzo = value;
    }

    /**
     * Recupera il valore della proprietà tipo.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipoIndirizzoTelematico }
     *     
     */
    public EnumTipoIndirizzoTelematico getTipo() {
        return tipo;
    }

    /**
     * Imposta il valore della proprietà tipo.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipoIndirizzoTelematico }
     *     
     */
    public void setTipo(EnumTipoIndirizzoTelematico value) {
        this.tipo = value;
    }

}
