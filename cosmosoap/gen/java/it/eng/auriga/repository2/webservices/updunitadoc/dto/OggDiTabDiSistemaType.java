
package it.eng.auriga.repository2.webservices.updunitadoc.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Rappresenta un oggetto censito in una tabella di
 * 				sistema del sistema documentale
 * 
 * <p>Classe Java per OggDiTabDiSistemaType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="OggDiTabDiSistemaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="CodId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Decodifica_Nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OggDiTabDiSistemaType", propOrder = {
    "codId",
    "decodificaNome"
})
public class OggDiTabDiSistemaType {

    @XmlElement(name = "CodId")
    protected String codId;
    @XmlElement(name = "Decodifica_Nome")
    protected String decodificaNome;

    /**
     * Recupera il valore della proprietà codId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodId() {
        return codId;
    }

    /**
     * Imposta il valore della proprietà codId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodId(String value) {
        this.codId = value;
    }

    /**
     * Recupera il valore della proprietà decodificaNome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDecodificaNome() {
        return decodificaNome;
    }

    /**
     * Imposta il valore della proprietà decodificaNome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDecodificaNome(String value) {
        this.decodificaNome = value;
    }

}
