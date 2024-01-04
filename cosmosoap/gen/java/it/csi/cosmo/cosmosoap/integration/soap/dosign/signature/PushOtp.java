
package it.csi.cosmo.cosmosoap.integration.soap.dosign.signature;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per pushOtp complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="pushOtp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/}baseRemoteSignatureDto" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pushOtp", propOrder = {
    "arg0"
})
public class PushOtp {

    protected BaseRemoteSignatureDto arg0;

    /**
     * Recupera il valore della proprietà arg0.
     * 
     * @return
     *     possible object is
     *     {@link BaseRemoteSignatureDto }
     *     
     */
    public BaseRemoteSignatureDto getArg0() {
        return arg0;
    }

    /**
     * Imposta il valore della proprietà arg0.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseRemoteSignatureDto }
     *     
     */
    public void setArg0(BaseRemoteSignatureDto value) {
        this.arg0 = value;
    }

}
