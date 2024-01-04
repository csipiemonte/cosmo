
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per certificatePolicyDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="certificatePolicyDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}signatureValidationDto">
 *       &lt;sequence>
 *         &lt;element name="oid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="policyInfoCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="policyInfo" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}policyInfoDto" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "certificatePolicyDto", propOrder = {
    "oid",
    "policyInfoCount",
    "policyInfo"
})
public class CertificatePolicyDto
    extends SignatureValidationDto
{

    protected String oid;
    protected int policyInfoCount;
    @XmlElement(nillable = true)
    protected List<PolicyInfoDto> policyInfo;

    /**
     * Recupera il valore della proprietà oid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOid() {
        return oid;
    }

    /**
     * Imposta il valore della proprietà oid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOid(String value) {
        this.oid = value;
    }

    /**
     * Recupera il valore della proprietà policyInfoCount.
     * 
     */
    public int getPolicyInfoCount() {
        return policyInfoCount;
    }

    /**
     * Imposta il valore della proprietà policyInfoCount.
     * 
     */
    public void setPolicyInfoCount(int value) {
        this.policyInfoCount = value;
    }

    /**
     * Gets the value of the policyInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the policyInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolicyInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PolicyInfoDto }
     * 
     * 
     */
    public List<PolicyInfoDto> getPolicyInfo() {
        if (policyInfo == null) {
            policyInfo = new ArrayList<PolicyInfoDto>();
        }
        return this.policyInfo;
    }

}
