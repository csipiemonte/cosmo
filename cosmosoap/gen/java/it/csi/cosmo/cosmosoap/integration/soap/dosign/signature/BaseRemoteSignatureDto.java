
package it.csi.cosmo.cosmosoap.integration.soap.dosign.signature;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per baseRemoteSignatureDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="baseRemoteSignatureDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/}remoteDto">
 *       &lt;sequence>
 *         &lt;element name="alias" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerInformation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "baseRemoteSignatureDto", propOrder = {
    "alias",
    "customerInformation",
    "pin"
})
@XmlSeeAlso({
    StartTransactionDto.class,
    RemoteSignatureDto.class,
    TransactionDataDto.class
})
public class BaseRemoteSignatureDto
    extends RemoteDto
{

    protected String alias;
    protected String customerInformation;
    protected String pin;

    /**
     * Recupera il valore della proprietà alias.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Imposta il valore della proprietà alias.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlias(String value) {
        this.alias = value;
    }

    /**
     * Recupera il valore della proprietà customerInformation.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerInformation() {
        return customerInformation;
    }

    /**
     * Imposta il valore della proprietà customerInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerInformation(String value) {
        this.customerInformation = value;
    }

    /**
     * Recupera il valore della proprietà pin.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPin() {
        return pin;
    }

    /**
     * Imposta il valore della proprietà pin.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPin(String value) {
        this.pin = value;
    }

}
