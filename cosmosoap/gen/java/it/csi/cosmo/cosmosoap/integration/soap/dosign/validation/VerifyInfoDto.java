
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per verifyInfoDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="verifyInfoDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}errorInformationDto">
 *       &lt;sequence>
 *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="envelopeCompliance" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="envelopeComplianceCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="envelopeComplianceInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="invalidSignCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="signer" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}signerDto" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="signerCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "verifyInfoDto", propOrder = {
    "data",
    "envelopeCompliance",
    "envelopeComplianceCode",
    "envelopeComplianceInfo",
    "invalidSignCount",
    "signer",
    "signerCount"
})
public class VerifyInfoDto
    extends ErrorInformationDto
{

    protected byte[] data;
    protected int envelopeCompliance;
    protected int envelopeComplianceCode;
    protected String envelopeComplianceInfo;
    protected int invalidSignCount;
    @XmlElement(nillable = true)
    protected List<SignerDto> signer;
    protected int signerCount;

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
     * Recupera il valore della proprietà envelopeCompliance.
     * 
     */
    public int getEnvelopeCompliance() {
        return envelopeCompliance;
    }

    /**
     * Imposta il valore della proprietà envelopeCompliance.
     * 
     */
    public void setEnvelopeCompliance(int value) {
        this.envelopeCompliance = value;
    }

    /**
     * Recupera il valore della proprietà envelopeComplianceCode.
     * 
     */
    public int getEnvelopeComplianceCode() {
        return envelopeComplianceCode;
    }

    /**
     * Imposta il valore della proprietà envelopeComplianceCode.
     * 
     */
    public void setEnvelopeComplianceCode(int value) {
        this.envelopeComplianceCode = value;
    }

    /**
     * Recupera il valore della proprietà envelopeComplianceInfo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnvelopeComplianceInfo() {
        return envelopeComplianceInfo;
    }

    /**
     * Imposta il valore della proprietà envelopeComplianceInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvelopeComplianceInfo(String value) {
        this.envelopeComplianceInfo = value;
    }

    /**
     * Recupera il valore della proprietà invalidSignCount.
     * 
     */
    public int getInvalidSignCount() {
        return invalidSignCount;
    }

    /**
     * Imposta il valore della proprietà invalidSignCount.
     * 
     */
    public void setInvalidSignCount(int value) {
        this.invalidSignCount = value;
    }

    /**
     * Gets the value of the signer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSigner().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SignerDto }
     * 
     * 
     */
    public List<SignerDto> getSigner() {
        if (signer == null) {
            signer = new ArrayList<SignerDto>();
        }
        return this.signer;
    }

    /**
     * Recupera il valore della proprietà signerCount.
     * 
     */
    public int getSignerCount() {
        return signerCount;
    }

    /**
     * Imposta il valore della proprietà signerCount.
     * 
     */
    public void setSignerCount(int value) {
        this.signerCount = value;
    }

}
