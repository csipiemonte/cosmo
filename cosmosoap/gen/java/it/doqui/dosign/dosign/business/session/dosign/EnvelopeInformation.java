
package it.doqui.dosign.dosign.business.session.dosign;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per envelopeInformation complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="envelopeInformation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://dosign.session.business.dosign.dosign.doqui.it/}dosignDto">
 *       &lt;sequence>
 *         &lt;element name="formatoDiInput" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="formatoDiOutput" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "envelopeInformation", propOrder = {
    "formatoDiInput",
    "formatoDiOutput"
})
@XmlSeeAlso({
    EnvelopedBuffer.class
})
public class EnvelopeInformation
    extends DosignDto
{

    protected String formatoDiInput;
    protected String formatoDiOutput;

    /**
     * Recupera il valore della proprietà formatoDiInput.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormatoDiInput() {
        return formatoDiInput;
    }

    /**
     * Imposta il valore della proprietà formatoDiInput.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormatoDiInput(String value) {
        this.formatoDiInput = value;
    }

    /**
     * Recupera il valore della proprietà formatoDiOutput.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormatoDiOutput() {
        return formatoDiOutput;
    }

    /**
     * Imposta il valore della proprietà formatoDiOutput.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormatoDiOutput(String value) {
        this.formatoDiOutput = value;
    }

}
