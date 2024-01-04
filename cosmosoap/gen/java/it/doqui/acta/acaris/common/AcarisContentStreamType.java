
package it.doqui.acta.acaris.common;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per acarisContentStreamType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="acarisContentStreamType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="length" type="{common.acaris.acta.doqui.it}contentStreamLengthType" minOccurs="0"/>
 *         &lt;element name="mimeType" type="{common.acaris.acta.doqui.it}enumMimeTypeType" minOccurs="0"/>
 *         &lt;element name="filename" type="{common.acaris.acta.doqui.it}contentStreamFilenameType" minOccurs="0"/>
 *         &lt;element name="streamMTOM" type="{common.acaris.acta.doqui.it}base64Binary"/>
 *         &lt;element name="stream" type="{common.acaris.acta.doqui.it}base64Binary"/>
 *         &lt;element name="nodeUID" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "acarisContentStreamType", propOrder = {
    "length",
    "mimeType",
    "filename",
    "streamMTOM",
    "stream",
    "nodeUID"
})
public class AcarisContentStreamType {

    protected Integer length;
    protected EnumMimeTypeType mimeType;
    protected String filename;
    @XmlElement(required = true)
    @XmlMimeType("*/*")
    protected DataHandler streamMTOM;
    @XmlElement(required = true)
    protected byte[] stream;
    protected String nodeUID;

    /**
     * Recupera il valore della proprietà length.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLength() {
        return length;
    }

    /**
     * Imposta il valore della proprietà length.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLength(Integer value) {
        this.length = value;
    }

    /**
     * Recupera il valore della proprietà mimeType.
     * 
     * @return
     *     possible object is
     *     {@link EnumMimeTypeType }
     *     
     */
    public EnumMimeTypeType getMimeType() {
        return mimeType;
    }

    /**
     * Imposta il valore della proprietà mimeType.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumMimeTypeType }
     *     
     */
    public void setMimeType(EnumMimeTypeType value) {
        this.mimeType = value;
    }

    /**
     * Recupera il valore della proprietà filename.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Imposta il valore della proprietà filename.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilename(String value) {
        this.filename = value;
    }

    /**
     * Recupera il valore della proprietà streamMTOM.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getStreamMTOM() {
        return streamMTOM;
    }

    /**
     * Imposta il valore della proprietà streamMTOM.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setStreamMTOM(DataHandler value) {
        this.streamMTOM = value;
    }

    /**
     * Recupera il valore della proprietà stream.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getStream() {
        return stream;
    }

    /**
     * Imposta il valore della proprietà stream.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setStream(byte[] value) {
        this.stream = value;
    }

    /**
     * Recupera il valore della proprietà nodeUID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeUID() {
        return nodeUID;
    }

    /**
     * Imposta il valore della proprietà nodeUID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeUID(String value) {
        this.nodeUID = value;
    }

}
