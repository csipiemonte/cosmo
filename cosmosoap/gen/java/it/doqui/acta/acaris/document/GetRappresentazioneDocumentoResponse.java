
package it.doqui.acta.acaris.document;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.documentservice.MappaIdentificazioneDocumento;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="info" type="{documentservice.acaris.acta.doqui.it}MappaIdentificazioneDocumento"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "info"
})
@XmlRootElement(name = "getRappresentazioneDocumentoResponse")
public class GetRappresentazioneDocumentoResponse {

    @XmlElement(required = true)
    protected MappaIdentificazioneDocumento info;

    /**
     * Recupera il valore della proprietà info.
     * 
     * @return
     *     possible object is
     *     {@link MappaIdentificazioneDocumento }
     *     
     */
    public MappaIdentificazioneDocumento getInfo() {
        return info;
    }

    /**
     * Imposta il valore della proprietà info.
     * 
     * @param value
     *     allowed object is
     *     {@link MappaIdentificazioneDocumento }
     *     
     */
    public void setInfo(MappaIdentificazioneDocumento value) {
        this.info = value;
    }

}
