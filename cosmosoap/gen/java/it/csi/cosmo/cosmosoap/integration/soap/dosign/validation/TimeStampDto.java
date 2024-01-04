
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per timeStampDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="timeStampDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}errorInformationDto">
 *       &lt;sequence>
 *         &lt;element name="compliance" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="complianceCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="complianceInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="encodedTst" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="genTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="hashAlgorithm" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="hashValue" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="policyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serialNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signer" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}signerDto" minOccurs="0"/>
 *         &lt;element name="tsTokenLength" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "timeStampDto", propOrder = {
    "compliance",
    "complianceCode",
    "complianceInfo",
    "encodedTst",
    "genTime",
    "hashAlgorithm",
    "hashValue",
    "policyId",
    "serialNumber",
    "signer",
    "tsTokenLength"
})
public class TimeStampDto
    extends ErrorInformationDto
{

    protected int compliance;
    protected int complianceCode;
    protected String complianceInfo;
    protected byte[] encodedTst;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar genTime;
    protected int hashAlgorithm;
    protected byte[] hashValue;
    protected String policyId;
    protected String serialNumber;
    protected SignerDto signer;
    protected int tsTokenLength;

    /**
     * Recupera il valore della proprietà compliance.
     * 
     */
    public int getCompliance() {
        return compliance;
    }

    /**
     * Imposta il valore della proprietà compliance.
     * 
     */
    public void setCompliance(int value) {
        this.compliance = value;
    }

    /**
     * Recupera il valore della proprietà complianceCode.
     * 
     */
    public int getComplianceCode() {
        return complianceCode;
    }

    /**
     * Imposta il valore della proprietà complianceCode.
     * 
     */
    public void setComplianceCode(int value) {
        this.complianceCode = value;
    }

    /**
     * Recupera il valore della proprietà complianceInfo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComplianceInfo() {
        return complianceInfo;
    }

    /**
     * Imposta il valore della proprietà complianceInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComplianceInfo(String value) {
        this.complianceInfo = value;
    }

    /**
     * Recupera il valore della proprietà encodedTst.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getEncodedTst() {
        return encodedTst;
    }

    /**
     * Imposta il valore della proprietà encodedTst.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setEncodedTst(byte[] value) {
        this.encodedTst = value;
    }

    /**
     * Recupera il valore della proprietà genTime.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getGenTime() {
        return genTime;
    }

    /**
     * Imposta il valore della proprietà genTime.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setGenTime(XMLGregorianCalendar value) {
        this.genTime = value;
    }

    /**
     * Recupera il valore della proprietà hashAlgorithm.
     * 
     */
    public int getHashAlgorithm() {
        return hashAlgorithm;
    }

    /**
     * Imposta il valore della proprietà hashAlgorithm.
     * 
     */
    public void setHashAlgorithm(int value) {
        this.hashAlgorithm = value;
    }

    /**
     * Recupera il valore della proprietà hashValue.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getHashValue() {
        return hashValue;
    }

    /**
     * Imposta il valore della proprietà hashValue.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setHashValue(byte[] value) {
        this.hashValue = value;
    }

    /**
     * Recupera il valore della proprietà policyId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolicyId() {
        return policyId;
    }

    /**
     * Imposta il valore della proprietà policyId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicyId(String value) {
        this.policyId = value;
    }

    /**
     * Recupera il valore della proprietà serialNumber.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Imposta il valore della proprietà serialNumber.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerialNumber(String value) {
        this.serialNumber = value;
    }

    /**
     * Recupera il valore della proprietà signer.
     * 
     * @return
     *     possible object is
     *     {@link SignerDto }
     *     
     */
    public SignerDto getSigner() {
        return signer;
    }

    /**
     * Imposta il valore della proprietà signer.
     * 
     * @param value
     *     allowed object is
     *     {@link SignerDto }
     *     
     */
    public void setSigner(SignerDto value) {
        this.signer = value;
    }

    /**
     * Recupera il valore della proprietà tsTokenLength.
     * 
     */
    public int getTsTokenLength() {
        return tsTokenLength;
    }

    /**
     * Imposta il valore della proprietà tsTokenLength.
     * 
     */
    public void setTsTokenLength(int value) {
        this.tsTokenLength = value;
    }

}
