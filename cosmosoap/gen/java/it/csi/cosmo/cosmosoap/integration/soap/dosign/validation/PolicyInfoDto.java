
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per policyInfoDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="policyInfoDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}signatureValidationDto">
 *       &lt;sequence>
 *         &lt;element name="cpsUri" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="explicitText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="noticeNumbers" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "policyInfoDto", propOrder = {
    "cpsUri",
    "explicitText",
    "id",
    "noticeNumbers"
})
public class PolicyInfoDto
    extends SignatureValidationDto
{

    protected String cpsUri;
    protected String explicitText;
    protected String id;
    @XmlElement(nillable = true)
    protected List<Integer> noticeNumbers;

    /**
     * Recupera il valore della proprietà cpsUri.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpsUri() {
        return cpsUri;
    }

    /**
     * Imposta il valore della proprietà cpsUri.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpsUri(String value) {
        this.cpsUri = value;
    }

    /**
     * Recupera il valore della proprietà explicitText.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExplicitText() {
        return explicitText;
    }

    /**
     * Imposta il valore della proprietà explicitText.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExplicitText(String value) {
        this.explicitText = value;
    }

    /**
     * Recupera il valore della proprietà id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Imposta il valore della proprietà id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the noticeNumbers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the noticeNumbers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNoticeNumbers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getNoticeNumbers() {
        if (noticeNumbers == null) {
            noticeNumbers = new ArrayList<Integer>();
        }
        return this.noticeNumbers;
    }

}
