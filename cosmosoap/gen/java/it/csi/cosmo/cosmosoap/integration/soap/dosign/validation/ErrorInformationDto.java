
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per errorInformationDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="errorInformationDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}commonDto">
 *       &lt;sequence>
 *         &lt;element name="error" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="errorMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hexErrorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "errorInformationDto", propOrder = {
    "error",
    "errorMsg",
    "hexErrorCode"
})
@XmlSeeAlso({
    VerifyReportDto.class,
    TimeStampDto.class,
    VerifyInfoDto.class,
    SignerDto.class,
    TimeStampedDataInfoDto.class
})
public class ErrorInformationDto
    extends CommonDto
{

    protected int error;
    protected String errorMsg;
    protected String hexErrorCode;

    /**
     * Recupera il valore della proprietà error.
     * 
     */
    public int getError() {
        return error;
    }

    /**
     * Imposta il valore della proprietà error.
     * 
     */
    public void setError(int value) {
        this.error = value;
    }

    /**
     * Recupera il valore della proprietà errorMsg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Imposta il valore della proprietà errorMsg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMsg(String value) {
        this.errorMsg = value;
    }

    /**
     * Recupera il valore della proprietà hexErrorCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHexErrorCode() {
        return hexErrorCode;
    }

    /**
     * Imposta il valore della proprietà hexErrorCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHexErrorCode(String value) {
        this.hexErrorCode = value;
    }

}
