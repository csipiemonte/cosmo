
package it.doqui.dosign.dosign.business.session.dosign;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per sigilloSignatureDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="sigilloSignatureDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://dosign.session.business.dosign.dosign.doqui.it/}remoteSignatureDto">
 *       &lt;sequence>
 *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="delegatedDomain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delegatedPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delegatedUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="otpPwd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="typeHSM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="typeOtpAuth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sigilloSignatureDto", propOrder = {
    "data",
    "delegatedDomain",
    "delegatedPassword",
    "delegatedUser",
    "otpPwd",
    "type",
    "typeHSM",
    "typeOtpAuth",
    "user"
})
public class SigilloSignatureDto
    extends RemoteSignatureDto
{

    protected byte[] data;
    protected String delegatedDomain;
    protected String delegatedPassword;
    protected String delegatedUser;
    protected String otpPwd;
    protected String type;
    protected String typeHSM;
    protected String typeOtpAuth;
    protected String user;

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
     * Recupera il valore della proprietà delegatedDomain.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelegatedDomain() {
        return delegatedDomain;
    }

    /**
     * Imposta il valore della proprietà delegatedDomain.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelegatedDomain(String value) {
        this.delegatedDomain = value;
    }

    /**
     * Recupera il valore della proprietà delegatedPassword.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelegatedPassword() {
        return delegatedPassword;
    }

    /**
     * Imposta il valore della proprietà delegatedPassword.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelegatedPassword(String value) {
        this.delegatedPassword = value;
    }

    /**
     * Recupera il valore della proprietà delegatedUser.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelegatedUser() {
        return delegatedUser;
    }

    /**
     * Imposta il valore della proprietà delegatedUser.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelegatedUser(String value) {
        this.delegatedUser = value;
    }

    /**
     * Recupera il valore della proprietà otpPwd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtpPwd() {
        return otpPwd;
    }

    /**
     * Imposta il valore della proprietà otpPwd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtpPwd(String value) {
        this.otpPwd = value;
    }

    /**
     * Recupera il valore della proprietà type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Imposta il valore della proprietà type.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Recupera il valore della proprietà typeHSM.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeHSM() {
        return typeHSM;
    }

    /**
     * Imposta il valore della proprietà typeHSM.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeHSM(String value) {
        this.typeHSM = value;
    }

    /**
     * Recupera il valore della proprietà typeOtpAuth.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeOtpAuth() {
        return typeOtpAuth;
    }

    /**
     * Imposta il valore della proprietà typeOtpAuth.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeOtpAuth(String value) {
        this.typeOtpAuth = value;
    }

    /**
     * Recupera il valore della proprietà user.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Imposta il valore della proprietà user.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
    }

}
