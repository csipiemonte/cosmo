
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per xmlReferenceDataDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="xmlReferenceDataDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}xmlPropertyDto">
 *       &lt;sequence>
 *         &lt;element name="dataBuffer" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="dataLength" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="encoding" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mimetype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlReferenceDataDto", propOrder = {
    "dataBuffer",
    "dataLength",
    "encoding",
    "mimetype"
})
public class XmlReferenceDataDto
    extends XmlPropertyDto
{

    protected byte[] dataBuffer;
    protected int dataLength;
    protected String encoding;
    protected String mimetype;

    /**
     * Recupera il valore della proprietà dataBuffer.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDataBuffer() {
        return dataBuffer;
    }

    /**
     * Imposta il valore della proprietà dataBuffer.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDataBuffer(byte[] value) {
        this.dataBuffer = value;
    }

    /**
     * Recupera il valore della proprietà dataLength.
     * 
     */
    public int getDataLength() {
        return dataLength;
    }

    /**
     * Imposta il valore della proprietà dataLength.
     * 
     */
    public void setDataLength(int value) {
        this.dataLength = value;
    }

    /**
     * Recupera il valore della proprietà encoding.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Imposta il valore della proprietà encoding.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncoding(String value) {
        this.encoding = value;
    }

    /**
     * Recupera il valore della proprietà mimetype.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMimetype() {
        return mimetype;
    }

    /**
     * Imposta il valore della proprietà mimetype.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMimetype(String value) {
        this.mimetype = value;
    }

}
