
package it.doqui.dosign.dosign.business.session.dosign.remotev2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per remoteDigestDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="remoteDigestDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://remotev2.dosign.session.business.dosign.dosign.doqui.it/}remoteAuthDto">
 *       &lt;sequence>
 *         &lt;element name="otp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sessionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mechanism" type="{http://remotev2.dosign.session.business.dosign.dosign.doqui.it/}Mechanism"/>
 *         &lt;element name="plaintext" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billingUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billingPassword" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "remoteDigestDto", propOrder = {
    "otp",
    "sessionId",
    "identifier",
    "mechanism",
    "plaintext",
    "billingUser",
    "billingPassword"
})
public class RemoteDigestDto
    extends RemoteAuthDto
{

    protected String otp;
    protected String sessionId;
    @XmlElement(required = true)
    protected String identifier;
    @XmlElement(required = true, defaultValue = "RSARAW")
    protected Mechanism mechanism;
    @XmlElement(required = true)
    protected String plaintext;
    @XmlElement(required = true)
    protected String billingUser;
    @XmlElement(required = true)
    protected String billingPassword;

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
     * Recupera il valore della proprietà sessionId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Imposta il valore della proprietà sessionId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionId(String value) {
        this.sessionId = value;
    }

    /**
     * Recupera il valore della proprietà identifier.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Imposta il valore della proprietà identifier.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Recupera il valore della proprietà mechanism.
     * 
     * @return
     *     possible object is
     *     {@link Mechanism }
     *     
     */
    public Mechanism getMechanism() {
        return mechanism;
    }

    /**
     * Imposta il valore della proprietà mechanism.
     * 
     * @param value
     *     allowed object is
     *     {@link Mechanism }
     *     
     */
    public void setMechanism(Mechanism value) {
        this.mechanism = value;
    }

    /**
     * Recupera il valore della proprietà plaintext.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaintext() {
        return plaintext;
    }

    /**
     * Imposta il valore della proprietà plaintext.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaintext(String value) {
        this.plaintext = value;
    }

    /**
     * Recupera il valore della proprietà billingUser.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingUser() {
        return billingUser;
    }

    /**
     * Imposta il valore della proprietà billingUser.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingUser(String value) {
        this.billingUser = value;
    }

    /**
     * Recupera il valore della proprietà billingPassword.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingPassword() {
        return billingPassword;
    }

    /**
     * Imposta il valore della proprietà billingPassword.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingPassword(String value) {
        this.billingPassword = value;
    }

}
