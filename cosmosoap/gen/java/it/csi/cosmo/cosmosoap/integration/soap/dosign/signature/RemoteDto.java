
package it.csi.cosmo.cosmosoap.integration.soap.dosign.signature;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per remoteDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="remoteDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/}dosignDto">
 *       &lt;sequence>
 *         &lt;element name="customerCa" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="sendingType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "remoteDto", propOrder = {
    "customerCa",
    "sendingType"
})
@XmlSeeAlso({
    BaseRemoteSignatureDto.class
})
public class RemoteDto
    extends DosignDto
{

    protected int customerCa;
    protected String sendingType;

    /**
     * Recupera il valore della proprietà customerCa.
     * 
     */
    public int getCustomerCa() {
        return customerCa;
    }

    /**
     * Imposta il valore della proprietà customerCa.
     * 
     */
    public void setCustomerCa(int value) {
        this.customerCa = value;
    }

    /**
     * Recupera il valore della proprietà sendingType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendingType() {
        return sendingType;
    }

    /**
     * Imposta il valore della proprietà sendingType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendingType(String value) {
        this.sendingType = value;
    }

}
