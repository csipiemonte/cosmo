
package it.doqui.dosign.dosign.business.session.dosign.remotev2;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per remoteTimestampDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="remoteTimestampDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://remotev2.dosign.session.business.dosign.dosign.doqui.it/}remoteBaseDto">
 *       &lt;sequence>
 *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="format" type="{http://remotev2.dosign.session.business.dosign.dosign.doqui.it/}TsFormat"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "remoteTimestampDto", propOrder = {
    "data",
    "format"
})
public class RemoteTimestampDto
    extends RemoteBaseDto
{

    @XmlElement(required = true)
    @XmlMimeType("application/octet-stream")
    protected DataHandler data;
    @XmlElement(required = true, defaultValue = "TSR")
    protected TsFormat format;

    /**
     * Recupera il valore della proprietà data.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getData() {
        return data;
    }

    /**
     * Imposta il valore della proprietà data.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setData(DataHandler value) {
        this.data = value;
    }

    /**
     * Recupera il valore della proprietà format.
     * 
     * @return
     *     possible object is
     *     {@link TsFormat }
     *     
     */
    public TsFormat getFormat() {
        return format;
    }

    /**
     * Imposta il valore della proprietà format.
     * 
     * @param value
     *     allowed object is
     *     {@link TsFormat }
     *     
     */
    public void setFormat(TsFormat value) {
        this.format = value;
    }

}
