
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per dosignDataDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="dosignDataDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}contentDto">
 *       &lt;sequence>
 *         &lt;element name="encoding" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="hashAlgorithm" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="signedDataMimeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dosignDataDto", propOrder = {
    "encoding",
    "hashAlgorithm",
    "signedDataMimeType"
})
@XmlSeeAlso({
    SignatureParameterDto.class
})
public class DosignDataDto
    extends ContentDto
{

    protected int encoding;
    protected int hashAlgorithm;
    protected String signedDataMimeType;

    /**
     * Recupera il valore della proprietà encoding.
     * 
     */
    public int getEncoding() {
        return encoding;
    }

    /**
     * Imposta il valore della proprietà encoding.
     * 
     */
    public void setEncoding(int value) {
        this.encoding = value;
    }

    /**
     * Recupera il valore della proprietà hashAlgorithm.
     * 
     */
    public int getHashAlgorithm() {
        return hashAlgorithm;
    }

    /**
     * Imposta il valore della proprietà hashAlgorithm.
     * 
     */
    public void setHashAlgorithm(int value) {
        this.hashAlgorithm = value;
    }

    /**
     * Recupera il valore della proprietà signedDataMimeType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignedDataMimeType() {
        return signedDataMimeType;
    }

    /**
     * Imposta il valore della proprietà signedDataMimeType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignedDataMimeType(String value) {
        this.signedDataMimeType = value;
    }

}
