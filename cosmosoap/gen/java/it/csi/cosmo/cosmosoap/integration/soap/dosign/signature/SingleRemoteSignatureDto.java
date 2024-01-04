
package it.csi.cosmo.cosmosoap.integration.soap.dosign.signature;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per singleRemoteSignatureDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="singleRemoteSignatureDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/}remoteSignatureDto">
 *       &lt;sequence>
 *         &lt;element name="authData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerTsa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="envelope" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="lastContent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="mode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="otp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timestamped" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "singleRemoteSignatureDto", propOrder = {
    "authData",
    "customerTsa",
    "data",
    "envelope",
    "lastContent",
    "mode",
    "otp",
    "timestamped"
})
@XmlSeeAlso({
    SingleRemotePdfSignatureDto.class
})
public class SingleRemoteSignatureDto
    extends RemoteSignatureDto
{

    protected String authData;
    protected String customerTsa;
    protected byte[] data;
    protected byte[] envelope;
    protected boolean lastContent;
    protected int mode;
    protected String otp;
    protected boolean timestamped;

    /**
     * Recupera il valore della proprietà authData.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthData() {
        return authData;
    }

    /**
     * Imposta il valore della proprietà authData.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthData(String value) {
        this.authData = value;
    }

    /**
     * Recupera il valore della proprietà customerTsa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerTsa() {
        return customerTsa;
    }

    /**
     * Imposta il valore della proprietà customerTsa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerTsa(String value) {
        this.customerTsa = value;
    }

    /**
     * Recupera il valore della proprietà data.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Imposta il valore della proprietà data.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setData(byte[] value) {
        this.data = value;
    }

    /**
     * Recupera il valore della proprietà envelope.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getEnvelope() {
        return envelope;
    }

    /**
     * Imposta il valore della proprietà envelope.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setEnvelope(byte[] value) {
        this.envelope = value;
    }

    /**
     * Recupera il valore della proprietà lastContent.
     * 
     */
    public boolean isLastContent() {
        return lastContent;
    }

    /**
     * Imposta il valore della proprietà lastContent.
     * 
     */
    public void setLastContent(boolean value) {
        this.lastContent = value;
    }

    /**
     * Recupera il valore della proprietà mode.
     * 
     */
    public int getMode() {
        return mode;
    }

    /**
     * Imposta il valore della proprietà mode.
     * 
     */
    public void setMode(int value) {
        this.mode = value;
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

    /**
     * Recupera il valore della proprietà timestamped.
     * 
     */
    public boolean isTimestamped() {
        return timestamped;
    }

    /**
     * Imposta il valore della proprietà timestamped.
     * 
     */
    public void setTimestamped(boolean value) {
        this.timestamped = value;
    }

}
