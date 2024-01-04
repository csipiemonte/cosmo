
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ProtocollazioneDaSmistamento complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ProtocollazioneDaSmistamento">
 *   &lt;complexContent>
 *     &lt;extension base="{officialbookservice.acaris.acta.doqui.it}RegistrazioneRequest">
 *       &lt;sequence>
 *         &lt;element name="smistamentoId" type="{common.acaris.acta.doqui.it}IDDBType"/>
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
@XmlType(name = "ProtocollazioneDaSmistamento", propOrder = {
    "smistamentoId",
    "registrazioneAPI"
})
public class ProtocollazioneDaSmistamento
    extends RegistrazioneRequest
{

    protected long smistamentoId;
    @XmlElement(required = true)
    protected RegistrazioneAPI registrazioneAPI;

    /**
     * Recupera il valore della proprietà smistamentoId.
     * 
     */
    public long getSmistamentoId() {
        return smistamentoId;
    }

    /**
     * Imposta il valore della proprietà smistamentoId.
     * 
     */
    public void setSmistamentoId(long value) {
        this.smistamentoId = value;
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
