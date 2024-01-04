
package it.eng.auriga.repository2.webservices.addunitadoc.dto;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per WSErrorType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="WSErrorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ErrorContext" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ErrorNumber" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="ErrorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WSErrorType", propOrder = {
    "errorContext",
    "errorNumber",
    "errorMessage"
})
@XmlSeeAlso({
    it.eng.auriga.repository2.webservices.addunitadoc.dto.BaseOutputWS.WSError.class
})
public class WSErrorType {

    @XmlElement(name = "ErrorContext")
    protected String errorContext;
    @XmlElement(name = "ErrorNumber")
    protected BigInteger errorNumber;
    @XmlElement(name = "ErrorMessage")
    protected String errorMessage;

    /**
     * Recupera il valore della proprietà errorContext.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorContext() {
        return errorContext;
    }

    /**
     * Imposta il valore della proprietà errorContext.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorContext(String value) {
        this.errorContext = value;
    }

    /**
     * Recupera il valore della proprietà errorNumber.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getErrorNumber() {
        return errorNumber;
    }

    /**
     * Imposta il valore della proprietà errorNumber.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setErrorNumber(BigInteger value) {
        this.errorNumber = value;
    }

    /**
     * Recupera il valore della proprietà errorMessage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Imposta il valore della proprietà errorMessage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

}
