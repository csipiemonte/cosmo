
package it.doqui.dosign.dosign.business.session.dosign;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per asyncVerifyDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="asyncVerifyDto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="notifyUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="param" type="{http://dosign.session.business.dosign.dosign.doqui.it/}verifyParameter" minOccurs="0"/>
 *         &lt;element name="signed" type="{http://dosign.session.business.dosign.dosign.doqui.it/}signedBuffer" minOccurs="0"/>
 *         &lt;element name="tokeUuid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "asyncVerifyDto", propOrder = {
    "notifyUrl",
    "param",
    "signed",
    "tokeUuid"
})
public class AsyncVerifyDto {

    protected String notifyUrl;
    protected VerifyParameter param;
    protected SignedBuffer signed;
    protected String tokeUuid;

    /**
     * Recupera il valore della proprietà notifyUrl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * Imposta il valore della proprietà notifyUrl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotifyUrl(String value) {
        this.notifyUrl = value;
    }

    /**
     * Recupera il valore della proprietà param.
     * 
     * @return
     *     possible object is
     *     {@link VerifyParameter }
     *     
     */
    public VerifyParameter getParam() {
        return param;
    }

    /**
     * Imposta il valore della proprietà param.
     * 
     * @param value
     *     allowed object is
     *     {@link VerifyParameter }
     *     
     */
    public void setParam(VerifyParameter value) {
        this.param = value;
    }

    /**
     * Recupera il valore della proprietà signed.
     * 
     * @return
     *     possible object is
     *     {@link SignedBuffer }
     *     
     */
    public SignedBuffer getSigned() {
        return signed;
    }

    /**
     * Imposta il valore della proprietà signed.
     * 
     * @param value
     *     allowed object is
     *     {@link SignedBuffer }
     *     
     */
    public void setSigned(SignedBuffer value) {
        this.signed = value;
    }

    /**
     * Recupera il valore della proprietà tokeUuid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTokeUuid() {
        return tokeUuid;
    }

    /**
     * Imposta il valore della proprietà tokeUuid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTokeUuid(String value) {
        this.tokeUuid = value;
    }

}
