
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per ProtocollazioneDocumentoEsistente complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ProtocollazioneDocumentoEsistente">
 *   &lt;complexContent>
 *     &lt;extension base="{officialbookservice.acaris.acta.doqui.it}RegistrazioneRequest">
 *       &lt;sequence>
 *         &lt;element name="classificazioneId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="registrazioneAPI" type="{officialbookservice.acaris.acta.doqui.it}RegistrazioneAPI"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProtocollazioneDocumentoEsistente", propOrder = {
    "classificazioneId",
    "registrazioneAPI"
})
public class ProtocollazioneDocumentoEsistente
    extends RegistrazioneRequest
{

    @XmlElement(required = true)
    protected ObjectIdType classificazioneId;
    @XmlElement(required = true)
    protected RegistrazioneAPI registrazioneAPI;

    /**
     * Recupera il valore della proprietà classificazioneId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getClassificazioneId() {
        return classificazioneId;
    }

    /**
     * Imposta il valore della proprietà classificazioneId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setClassificazioneId(ObjectIdType value) {
        this.classificazioneId = value;
    }

    /**
     * Recupera il valore della proprietà registrazioneAPI.
     * 
     * @return
     *     possible object is
     *     {@link RegistrazioneAPI }
     *     
     */
    public RegistrazioneAPI getRegistrazioneAPI() {
        return registrazioneAPI;
    }

    /**
     * Imposta il valore della proprietà registrazioneAPI.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistrazioneAPI }
     *     
     */
    public void setRegistrazioneAPI(RegistrazioneAPI value) {
        this.registrazioneAPI = value;
    }

}
