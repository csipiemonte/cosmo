
package it.doqui.dosign.dosign.business.session.dosign;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per asyncSigilloInDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="asyncSigilloInDto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="data" type="{http://dosign.session.business.dosign.dosign.doqui.it/}sigilloSignatureDto" minOccurs="0"/>
 *         &lt;element name="notifyUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tokenUid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "asyncSigilloInDto", propOrder = {
    "data",
    "notifyUrl",
    "tokenUid"
})
public class AsyncSigilloInDto {

    protected SigilloSignatureDto data;
    protected String notifyUrl;
    protected String tokenUid;

    /**
     * Recupera il valore della proprietà data.
     * 
     * @return
     *     possible object is
     *     {@link SigilloSignatureDto }
     *     
     */
    public SigilloSignatureDto getData() {
        return data;
    }

    /**
     * Imposta il valore della proprietà data.
     * 
     * @param value
     *     allowed object is
     *     {@link SigilloSignatureDto }
     *     
     */
    public void setData(SigilloSignatureDto value) {
        this.data = value;
    }

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
     * Recupera il valore della proprietà tokenUid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTokenUid() {
        return tokenUid;
    }

    /**
     * Imposta il valore della proprietà tokenUid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTokenUid(String value) {
        this.tokenUid = value;
    }

}
