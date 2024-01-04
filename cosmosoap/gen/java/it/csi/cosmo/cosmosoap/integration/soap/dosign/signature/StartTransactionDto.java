
package it.csi.cosmo.cosmosoap.integration.soap.dosign.signature;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per startTransactionDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="startTransactionDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/}baseRemoteSignatureDto">
 *       &lt;sequence>
 *         &lt;element name="maxTranSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="otp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "startTransactionDto", propOrder = {
    "maxTranSize",
    "otp"
})
public class StartTransactionDto
    extends BaseRemoteSignatureDto
{

    protected int maxTranSize;
    protected String otp;

    /**
     * Recupera il valore della proprietà maxTranSize.
     * 
     */
    public int getMaxTranSize() {
        return maxTranSize;
    }

    /**
     * Imposta il valore della proprietà maxTranSize.
     * 
     */
    public void setMaxTranSize(int value) {
        this.maxTranSize = value;
    }

    /**
     * Recupera il valore della proprietà otp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtp() {
        return otp;
    }

    /**
     * Imposta il valore della proprietà otp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtp(String value) {
        this.otp = value;
    }

}
