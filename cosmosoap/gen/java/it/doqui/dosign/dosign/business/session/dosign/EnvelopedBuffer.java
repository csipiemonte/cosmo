
package it.doqui.dosign.dosign.business.session.dosign;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per envelopedBuffer complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="envelopedBuffer">
 *   &lt;complexContent>
 *     &lt;extension base="{http://dosign.session.business.dosign.dosign.doqui.it/}envelopeInformation">
 *       &lt;sequence>
 *         &lt;element name="buffer" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "envelopedBuffer", propOrder = {
    "buffer"
})
public class EnvelopedBuffer
    extends EnvelopeInformation
{

    protected byte[] buffer;

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

}
