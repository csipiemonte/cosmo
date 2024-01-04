
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per verifyDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="verifyDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}signatureParameterDto">
 *       &lt;sequence>
 *         &lt;element name="envelopeArray" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="envelopeInputStream" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}inputStream" minOccurs="0"/>
 *         &lt;element name="length" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "verifyDto", propOrder = {
    "envelopeArray",
    "envelopeInputStream",
    "length"
})
public class VerifyDto
    extends SignatureParameterDto
{

    protected byte[] envelopeArray;
    protected InputStream envelopeInputStream;
    protected int length;

    /**
     * Recupera il valore della proprietà envelopeArray.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getEnvelopeArray() {
        return envelopeArray;
    }

    /**
     * Imposta il valore della proprietà envelopeArray.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setEnvelopeArray(byte[] value) {
        this.envelopeArray = value;
    }

    /**
     * Recupera il valore della proprietà envelopeInputStream.
     * 
     * @return
     *     possible object is
     *     {@link InputStream }
     *     
     */
    public InputStream getEnvelopeInputStream() {
        return envelopeInputStream;
    }

    /**
     * Imposta il valore della proprietà envelopeInputStream.
     * 
     * @param value
     *     allowed object is
     *     {@link InputStream }
     *     
     */
    public void setEnvelopeInputStream(InputStream value) {
        this.envelopeInputStream = value;
    }

    /**
     * Recupera il valore della proprietà length.
     * 
     */
    public int getLength() {
        return length;
    }

    /**
     * Imposta il valore della proprietà length.
     * 
     */
    public void setLength(int value) {
        this.length = value;
    }

}
