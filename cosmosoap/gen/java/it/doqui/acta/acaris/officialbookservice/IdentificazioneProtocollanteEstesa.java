
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per IdentificazioneProtocollanteEstesa complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="IdentificazioneProtocollanteEstesa">
 *   &lt;complexContent>
 *     &lt;extension base="{officialbookservice.acaris.acta.doqui.it}IdentificazioneProtocollante">
 *       &lt;sequence>
 *         &lt;element name="idAoo" type="{common.acaris.acta.doqui.it}ObjectIdType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdentificazioneProtocollanteEstesa", propOrder = {
    "idAoo"
})
public class IdentificazioneProtocollanteEstesa
    extends IdentificazioneProtocollante
{

    protected ObjectIdType idAoo;

    /**
     * Recupera il valore della proprietà idAoo.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdAoo() {
        return idAoo;
    }

    /**
     * Imposta il valore della proprietà idAoo.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdAoo(ObjectIdType value) {
        this.idAoo = value;
    }

}
