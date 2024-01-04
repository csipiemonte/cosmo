
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.EnumMimeTypeType;
import it.doqui.acta.acaris.common.EnumStreamId;


/**
 * <p>Classe Java per acarisRenditionType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="acarisRenditionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="streamId" type="{common.acaris.acta.doqui.it}enumStreamId"/>
 *         &lt;element name="mimeType" type="{common.acaris.acta.doqui.it}enumMimeTypeType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "acarisRenditionType", propOrder = {
    "streamId",
    "mimeType"
})
public class AcarisRenditionType {

    @XmlElement(required = true)
    protected EnumStreamId streamId;
    protected EnumMimeTypeType mimeType;

    /**
     * Recupera il valore della proprietà streamId.
     * 
     * @return
     *     possible object is
     *     {@link EnumStreamId }
     *     
     */
    public EnumStreamId getStreamId() {
        return streamId;
    }

    /**
     * Imposta il valore della proprietà streamId.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumStreamId }
     *     
     */
    public void setStreamId(EnumStreamId value) {
        this.streamId = value;
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

}
