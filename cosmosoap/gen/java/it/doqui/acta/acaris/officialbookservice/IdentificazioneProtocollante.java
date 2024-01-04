
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per IdentificazioneProtocollante complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="IdentificazioneProtocollante">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="strutturaId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="nodoId" type="{common.acaris.acta.doqui.it}ObjectIdType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdentificazioneProtocollante", propOrder = {
    "strutturaId",
    "nodoId"
})
@XmlSeeAlso({
    IdentificazioneProtocollanteEstesa.class
})
public class IdentificazioneProtocollante {

    @XmlElement(required = true)
    protected ObjectIdType strutturaId;
    protected ObjectIdType nodoId;

    /**
     * Recupera il valore della proprietà strutturaId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getStrutturaId() {
        return strutturaId;
    }

    /**
     * Imposta il valore della proprietà strutturaId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setStrutturaId(ObjectIdType value) {
        this.strutturaId = value;
    }

    /**
     * Recupera il valore della proprietà nodoId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getNodoId() {
        return nodoId;
    }

    /**
     * Imposta il valore della proprietà nodoId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setNodoId(ObjectIdType value) {
        this.nodoId = value;
    }

}
