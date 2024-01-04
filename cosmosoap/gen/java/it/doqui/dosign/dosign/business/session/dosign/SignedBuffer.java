
package it.doqui.dosign.dosign.business.session.dosign;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per signedBuffer complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="signedBuffer">
 *   &lt;complexContent>
 *     &lt;extension base="{http://dosign.session.business.dosign.dosign.doqui.it/}dosignDto">
 *       &lt;sequence>
 *         &lt;element name="buffer" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="detachedBuffer" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signedBuffer", propOrder = {
    "buffer",
    "detachedBuffer"
})
public class SignedBuffer
    extends DosignDto
{

    protected byte[] buffer;
    protected byte[] detachedBuffer;

    /**
     * Recupera il valore della proprietà buffer.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBuffer() {
        return buffer;
    }

    /**
     * Imposta il valore della proprietà buffer.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBuffer(byte[] value) {
        this.buffer = value;
    }

    /**
     * Recupera il valore della proprietà detachedBuffer.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDetachedBuffer() {
        return detachedBuffer;
    }

    /**
     * Imposta il valore della proprietà detachedBuffer.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDetachedBuffer(byte[] value) {
        this.detachedBuffer = value;
    }

}
