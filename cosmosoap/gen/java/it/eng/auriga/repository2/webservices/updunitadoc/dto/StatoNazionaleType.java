
package it.eng.auriga.repository2.webservices.updunitadoc.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per StatoNazionaleType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="StatoNazionaleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="CodISTATStato">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9]{3}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NomeStato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatoNazionaleType", propOrder = {
    "codISTATStato",
    "nomeStato"
})
public class StatoNazionaleType {

    @XmlElement(name = "CodISTATStato")
    protected String codISTATStato;
    @XmlElement(name = "NomeStato")
    protected String nomeStato;

    /**
     * Recupera il valore della proprietà codISTATStato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodISTATStato() {
        return codISTATStato;
    }

    /**
     * Imposta il valore della proprietà codISTATStato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodISTATStato(String value) {
        this.codISTATStato = value;
    }

    /**
     * Recupera il valore della proprietà nomeStato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeStato() {
        return nomeStato;
    }

    /**
     * Imposta il valore della proprietà nomeStato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeStato(String value) {
        this.nomeStato = value;
    }

}
