
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per signerDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="signerDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}errorInformationDto">
 *       &lt;sequence>
 *         &lt;element name="archiveTimeStamps" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}timeStampDto" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="certificateId" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}certificateDto" minOccurs="0"/>
 *         &lt;element name="certificateSn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificateStatus" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="certificateStatusCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="certificateStatusDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificateStatusInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificateValue" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="certificateVerificationMode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="certificationLevel" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="complianceEnvironment" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="contact" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="corruptedData" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="crlIssueDn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="crlNextUpdateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="crlRevocationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="crlUpdateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="crlUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateOfBirth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="digestAlgorithm" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="digestAlgorithmName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="digestValue" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fiscalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="invalidSignCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="issuerDn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="keyType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="keyUsage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="md5Fingerprint" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="ocspProducedAt" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="ocspUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="policies" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}certificatePolicyDto" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="qcCompliance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="qcLimitValue" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="qcLimitValueCurrency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="qcRetentionPeriod" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="qcSscd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="references" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}xmlReferenceDto" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="refsOnlyTimeStamps" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}timeStampDto" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="revision" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="shaFingerprint" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="sigAndRefsTimeStamps" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}timeStampDto" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="signatureAlgorithm" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="signatureAlgorithmName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signatureCompliance" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="signatureComplianceCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="signatureComplianceInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signatureFormat" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="signatureProductionPlace" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}xmlSignatureProductionPlaceDto" minOccurs="0"/>
 *         &lt;element name="signatureTimeStamps" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}timeStampDto" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="signatureValue" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="signerCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="signers" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}signerDto" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="signingTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="subjectDn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timeStamp" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}timeStampDto" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="verificationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signerDto", propOrder = {
    "archiveTimeStamps",
    "certificateId",
    "certificateSn",
    "certificateStatus",
    "certificateStatusCode",
    "certificateStatusDescription",
    "certificateStatusInfo",
    "certificateValue",
    "certificateVerificationMode",
    "certificationLevel",
    "complianceEnvironment",
    "contact",
    "corruptedData",
    "crlIssueDn",
    "crlNextUpdateTime",
    "crlRevocationDate",
    "crlUpdateTime",
    "crlUrl",
    "dateOfBirth",
    "digestAlgorithm",
    "digestAlgorithmName",
    "digestValue",
    "endDate",
    "fiscalCode",
    "invalidSignCount",
    "issuerDn",
    "keyType",
    "keyUsage",
    "location",
    "md5Fingerprint",
    "ocspProducedAt",
    "ocspUrl",
    "policies",
    "qcCompliance",
    "qcLimitValue",
    "qcLimitValueCurrency",
    "qcRetentionPeriod",
    "qcSscd",
    "reason",
    "references",
    "refsOnlyTimeStamps",
    "revision",
    "shaFingerprint",
    "sigAndRefsTimeStamps",
    "signatureAlgorithm",
    "signatureAlgorithmName",
    "signatureCompliance",
    "signatureComplianceCode",
    "signatureComplianceInfo",
    "signatureFormat",
    "signatureProductionPlace",
    "signatureTimeStamps",
    "signatureValue",
    "signerCount",
    "signers",
    "signingTime",
    "startDate",
    "subjectDn",
    "timeStamp",
    "verificationDate"
})
public class SignerDto
    extends ErrorInformationDto
{

    @XmlElement(nillable = true)
    protected List<TimeStampDto> archiveTimeStamps;
    protected CertificateDto certificateId;
    protected String certificateSn;
    protected int certificateStatus;
    protected int certificateStatusCode;
    protected String certificateStatusDescription;
    protected String certificateStatusInfo;
    protected byte[] certificateValue;
    protected int certificateVerificationMode;
    protected int certificationLevel;
    protected int complianceEnvironment;
    protected String contact;
    protected boolean corruptedData;
    protected String crlIssueDn;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar crlNextUpdateTime;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar crlRevocationDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar crlUpdateTime;
    protected String crlUrl;
    protected String dateOfBirth;
    protected int digestAlgorithm;
    protected String digestAlgorithmName;
    protected byte[] digestValue;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar endDate;
    protected String fiscalCode;
    protected int invalidSignCount;
    protected String issuerDn;
    protected int keyType;
    protected int keyUsage;
    protected String location;
    protected byte[] md5Fingerprint;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar ocspProducedAt;
    protected String ocspUrl;
    @XmlElement(nillable = true)
    protected List<CertificatePolicyDto> policies;
    protected String qcCompliance;
    protected int qcLimitValue;
    protected String qcLimitValueCurrency;
    protected int qcRetentionPeriod;
    protected String qcSscd;
    protected String reason;
    @XmlElement(nillable = true)
    protected List<XmlReferenceDto> references;
    @XmlElement(nillable = true)
    protected List<TimeStampDto> refsOnlyTimeStamps;
    protected int revision;
    protected byte[] shaFingerprint;
    @XmlElement(nillable = true)
    protected List<TimeStampDto> sigAndRefsTimeStamps;
    protected int signatureAlgorithm;
    protected String signatureAlgorithmName;
    protected int signatureCompliance;
    protected int signatureComplianceCode;
    protected String signatureComplianceInfo;
    protected int signatureFormat;
    protected XmlSignatureProductionPlaceDto signatureProductionPlace;
    @XmlElement(nillable = true)
    protected List<TimeStampDto> signatureTimeStamps;
    protected byte[] signatureValue;
    protected int signerCount;
    @XmlElement(nillable = true)
    protected List<SignerDto> signers;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar signingTime;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar startDate;
    protected String subjectDn;
    @XmlElement(nillable = true)
    protected List<TimeStampDto> timeStamp;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar verificationDate;

    /**
     * Gets the value of the archiveTimeStamps property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the archiveTimeStamps property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArchiveTimeStamps().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TimeStampDto }
     * 
     * 
     */
    public List<TimeStampDto> getArchiveTimeStamps() {
        if (archiveTimeStamps == null) {
            archiveTimeStamps = new ArrayList<TimeStampDto>();
        }
        return this.archiveTimeStamps;
    }

    /**
     * Recupera il valore della proprietà certificateId.
     * 
     * @return
     *     possible object is
     *     {@link CertificateDto }
     *     
     */
    public CertificateDto getCertificateId() {
        return certificateId;
    }

    /**
     * Imposta il valore della proprietà certificateId.
     * 
     * @param value
     *     allowed object is
     *     {@link CertificateDto }
     *     
     */
    public void setCertificateId(CertificateDto value) {
        this.certificateId = value;
    }

    /**
     * Recupera il valore della proprietà certificateSn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateSn() {
        return certificateSn;
    }

    /**
     * Imposta il valore della proprietà certificateSn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateSn(String value) {
        this.certificateSn = value;
    }

    /**
     * Recupera il valore della proprietà certificateStatus.
     * 
     */
    public int getCertificateStatus() {
        return certificateStatus;
    }

    /**
     * Imposta il valore della proprietà certificateStatus.
     * 
     */
    public void setCertificateStatus(int value) {
        this.certificateStatus = value;
    }

    /**
     * Recupera il valore della proprietà certificateStatusCode.
     * 
     */
    public int getCertificateStatusCode() {
        return certificateStatusCode;
    }

    /**
     * Imposta il valore della proprietà certificateStatusCode.
     * 
     */
    public void setCertificateStatusCode(int value) {
        this.certificateStatusCode = value;
    }

    /**
     * Recupera il valore della proprietà certificateStatusDescription.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateStatusDescription() {
        return certificateStatusDescription;
    }

    /**
     * Imposta il valore della proprietà certificateStatusDescription.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateStatusDescription(String value) {
        this.certificateStatusDescription = value;
    }

    /**
     * Recupera il valore della proprietà certificateStatusInfo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateStatusInfo() {
        return certificateStatusInfo;
    }

    /**
     * Imposta il valore della proprietà certificateStatusInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateStatusInfo(String value) {
        this.certificateStatusInfo = value;
    }

    /**
     * Recupera il valore della proprietà certificateValue.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCertificateValue() {
        return certificateValue;
    }

    /**
     * Imposta il valore della proprietà certificateValue.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCertificateValue(byte[] value) {
        this.certificateValue = value;
    }

    /**
     * Recupera il valore della proprietà certificateVerificationMode.
     * 
     */
    public int getCertificateVerificationMode() {
        return certificateVerificationMode;
    }

    /**
     * Imposta il valore della proprietà certificateVerificationMode.
     * 
     */
    public void setCertificateVerificationMode(int value) {
        this.certificateVerificationMode = value;
    }

    /**
     * Recupera il valore della proprietà certificationLevel.
     * 
     */
    public int getCertificationLevel() {
        return certificationLevel;
    }

    /**
     * Imposta il valore della proprietà certificationLevel.
     * 
     */
    public void setCertificationLevel(int value) {
        this.certificationLevel = value;
    }

    /**
     * Recupera il valore della proprietà complianceEnvironment.
     * 
     */
    public int getComplianceEnvironment() {
        return complianceEnvironment;
    }

    /**
     * Imposta il valore della proprietà complianceEnvironment.
     * 
     */
    public void setComplianceEnvironment(int value) {
        this.complianceEnvironment = value;
    }

    /**
     * Recupera il valore della proprietà contact.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContact() {
        return contact;
    }

    /**
     * Imposta il valore della proprietà contact.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContact(String value) {
        this.contact = value;
    }

    /**
     * Recupera il valore della proprietà corruptedData.
     * 
     */
    public boolean isCorruptedData() {
        return corruptedData;
    }

    /**
     * Imposta il valore della proprietà corruptedData.
     * 
     */
    public void setCorruptedData(boolean value) {
        this.corruptedData = value;
    }

    /**
     * Recupera il valore della proprietà crlIssueDn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCrlIssueDn() {
        return crlIssueDn;
    }

    /**
     * Imposta il valore della proprietà crlIssueDn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCrlIssueDn(String value) {
        this.crlIssueDn = value;
    }

    /**
     * Recupera il valore della proprietà crlNextUpdateTime.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCrlNextUpdateTime() {
        return crlNextUpdateTime;
    }

    /**
     * Imposta il valore della proprietà crlNextUpdateTime.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCrlNextUpdateTime(XMLGregorianCalendar value) {
        this.crlNextUpdateTime = value;
    }

    /**
     * Recupera il valore della proprietà crlRevocationDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCrlRevocationDate() {
        return crlRevocationDate;
    }

    /**
     * Imposta il valore della proprietà crlRevocationDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCrlRevocationDate(XMLGregorianCalendar value) {
        this.crlRevocationDate = value;
    }

    /**
     * Recupera il valore della proprietà crlUpdateTime.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCrlUpdateTime() {
        return crlUpdateTime;
    }

    /**
     * Imposta il valore della proprietà crlUpdateTime.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCrlUpdateTime(XMLGregorianCalendar value) {
        this.crlUpdateTime = value;
    }

    /**
     * Recupera il valore della proprietà crlUrl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCrlUrl() {
        return crlUrl;
    }

    /**
     * Imposta il valore della proprietà crlUrl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCrlUrl(String value) {
        this.crlUrl = value;
    }

    /**
     * Recupera il valore della proprietà dateOfBirth.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Imposta il valore della proprietà dateOfBirth.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateOfBirth(String value) {
        this.dateOfBirth = value;
    }

    /**
     * Recupera il valore della proprietà digestAlgorithm.
     * 
     */
    public int getDigestAlgorithm() {
        return digestAlgorithm;
    }

    /**
     * Imposta il valore della proprietà digestAlgorithm.
     * 
     */
    public void setDigestAlgorithm(int value) {
        this.digestAlgorithm = value;
    }

    /**
     * Recupera il valore della proprietà digestAlgorithmName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDigestAlgorithmName() {
        return digestAlgorithmName;
    }

    /**
     * Imposta il valore della proprietà digestAlgorithmName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDigestAlgorithmName(String value) {
        this.digestAlgorithmName = value;
    }

    /**
     * Recupera il valore della proprietà digestValue.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDigestValue() {
        return digestValue;
    }

    /**
     * Imposta il valore della proprietà digestValue.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDigestValue(byte[] value) {
        this.digestValue = value;
    }

    /**
     * Recupera il valore della proprietà endDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Imposta il valore della proprietà endDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

    /**
     * Recupera il valore della proprietà fiscalCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFiscalCode() {
        return fiscalCode;
    }

    /**
     * Imposta il valore della proprietà fiscalCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFiscalCode(String value) {
        this.fiscalCode = value;
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
     * Recupera il valore della proprietà issuerDn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuerDn() {
        return issuerDn;
    }

    /**
     * Imposta il valore della proprietà issuerDn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuerDn(String value) {
        this.issuerDn = value;
    }

    /**
     * Recupera il valore della proprietà keyType.
     * 
     */
    public int getKeyType() {
        return keyType;
    }

    /**
     * Imposta il valore della proprietà keyType.
     * 
     */
    public void setKeyType(int value) {
        this.keyType = value;
    }

    /**
     * Recupera il valore della proprietà keyUsage.
     * 
     */
    public int getKeyUsage() {
        return keyUsage;
    }

    /**
     * Imposta il valore della proprietà keyUsage.
     * 
     */
    public void setKeyUsage(int value) {
        this.keyUsage = value;
    }

    /**
     * Recupera il valore della proprietà location.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * Imposta il valore della proprietà location.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Recupera il valore della proprietà md5Fingerprint.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getMd5Fingerprint() {
        return md5Fingerprint;
    }

    /**
     * Imposta il valore della proprietà md5Fingerprint.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setMd5Fingerprint(byte[] value) {
        this.md5Fingerprint = value;
    }

    /**
     * Recupera il valore della proprietà ocspProducedAt.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOcspProducedAt() {
        return ocspProducedAt;
    }

    /**
     * Imposta il valore della proprietà ocspProducedAt.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOcspProducedAt(XMLGregorianCalendar value) {
        this.ocspProducedAt = value;
    }

    /**
     * Recupera il valore della proprietà ocspUrl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOcspUrl() {
        return ocspUrl;
    }

    /**
     * Imposta il valore della proprietà ocspUrl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOcspUrl(String value) {
        this.ocspUrl = value;
    }

    /**
     * Gets the value of the policies property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the policies property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolicies().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CertificatePolicyDto }
     * 
     * 
     */
    public List<CertificatePolicyDto> getPolicies() {
        if (policies == null) {
            policies = new ArrayList<CertificatePolicyDto>();
        }
        return this.policies;
    }

    /**
     * Recupera il valore della proprietà qcCompliance.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQcCompliance() {
        return qcCompliance;
    }

    /**
     * Imposta il valore della proprietà qcCompliance.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQcCompliance(String value) {
        this.qcCompliance = value;
    }

    /**
     * Recupera il valore della proprietà qcLimitValue.
     * 
     */
    public int getQcLimitValue() {
        return qcLimitValue;
    }

    /**
     * Imposta il valore della proprietà qcLimitValue.
     * 
     */
    public void setQcLimitValue(int value) {
        this.qcLimitValue = value;
    }

    /**
     * Recupera il valore della proprietà qcLimitValueCurrency.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQcLimitValueCurrency() {
        return qcLimitValueCurrency;
    }

    /**
     * Imposta il valore della proprietà qcLimitValueCurrency.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQcLimitValueCurrency(String value) {
        this.qcLimitValueCurrency = value;
    }

    /**
     * Recupera il valore della proprietà qcRetentionPeriod.
     * 
     */
    public int getQcRetentionPeriod() {
        return qcRetentionPeriod;
    }

    /**
     * Imposta il valore della proprietà qcRetentionPeriod.
     * 
     */
    public void setQcRetentionPeriod(int value) {
        this.qcRetentionPeriod = value;
    }

    /**
     * Recupera il valore della proprietà qcSscd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQcSscd() {
        return qcSscd;
    }

    /**
     * Imposta il valore della proprietà qcSscd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQcSscd(String value) {
        this.qcSscd = value;
    }

    /**
     * Recupera il valore della proprietà reason.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Imposta il valore della proprietà reason.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
    }

    /**
     * Gets the value of the references property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the references property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReferences().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlReferenceDto }
     * 
     * 
     */
    public List<XmlReferenceDto> getReferences() {
        if (references == null) {
            references = new ArrayList<XmlReferenceDto>();
        }
        return this.references;
    }

    /**
     * Gets the value of the refsOnlyTimeStamps property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the refsOnlyTimeStamps property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRefsOnlyTimeStamps().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TimeStampDto }
     * 
     * 
     */
    public List<TimeStampDto> getRefsOnlyTimeStamps() {
        if (refsOnlyTimeStamps == null) {
            refsOnlyTimeStamps = new ArrayList<TimeStampDto>();
        }
        return this.refsOnlyTimeStamps;
    }

    /**
     * Recupera il valore della proprietà revision.
     * 
     */
    public int getRevision() {
        return revision;
    }

    /**
     * Imposta il valore della proprietà revision.
     * 
     */
    public void setRevision(int value) {
        this.revision = value;
    }

    /**
     * Recupera il valore della proprietà shaFingerprint.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getShaFingerprint() {
        return shaFingerprint;
    }

    /**
     * Imposta il valore della proprietà shaFingerprint.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setShaFingerprint(byte[] value) {
        this.shaFingerprint = value;
    }

    /**
     * Gets the value of the sigAndRefsTimeStamps property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sigAndRefsTimeStamps property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSigAndRefsTimeStamps().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TimeStampDto }
     * 
     * 
     */
    public List<TimeStampDto> getSigAndRefsTimeStamps() {
        if (sigAndRefsTimeStamps == null) {
            sigAndRefsTimeStamps = new ArrayList<TimeStampDto>();
        }
        return this.sigAndRefsTimeStamps;
    }

    /**
     * Recupera il valore della proprietà signatureAlgorithm.
     * 
     */
    public int getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    /**
     * Imposta il valore della proprietà signatureAlgorithm.
     * 
     */
    public void setSignatureAlgorithm(int value) {
        this.signatureAlgorithm = value;
    }

    /**
     * Recupera il valore della proprietà signatureAlgorithmName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignatureAlgorithmName() {
        return signatureAlgorithmName;
    }

    /**
     * Imposta il valore della proprietà signatureAlgorithmName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignatureAlgorithmName(String value) {
        this.signatureAlgorithmName = value;
    }

    /**
     * Recupera il valore della proprietà signatureCompliance.
     * 
     */
    public int getSignatureCompliance() {
        return signatureCompliance;
    }

    /**
     * Imposta il valore della proprietà signatureCompliance.
     * 
     */
    public void setSignatureCompliance(int value) {
        this.signatureCompliance = value;
    }

    /**
     * Recupera il valore della proprietà signatureComplianceCode.
     * 
     */
    public int getSignatureComplianceCode() {
        return signatureComplianceCode;
    }

    /**
     * Imposta il valore della proprietà signatureComplianceCode.
     * 
     */
    public void setSignatureComplianceCode(int value) {
        this.signatureComplianceCode = value;
    }

    /**
     * Recupera il valore della proprietà signatureComplianceInfo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignatureComplianceInfo() {
        return signatureComplianceInfo;
    }

    /**
     * Imposta il valore della proprietà signatureComplianceInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignatureComplianceInfo(String value) {
        this.signatureComplianceInfo = value;
    }

    /**
     * Recupera il valore della proprietà signatureFormat.
     * 
     */
    public int getSignatureFormat() {
        return signatureFormat;
    }

    /**
     * Imposta il valore della proprietà signatureFormat.
     * 
     */
    public void setSignatureFormat(int value) {
        this.signatureFormat = value;
    }

    /**
     * Recupera il valore della proprietà signatureProductionPlace.
     * 
     * @return
     *     possible object is
     *     {@link XmlSignatureProductionPlaceDto }
     *     
     */
    public XmlSignatureProductionPlaceDto getSignatureProductionPlace() {
        return signatureProductionPlace;
    }

    /**
     * Imposta il valore della proprietà signatureProductionPlace.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlSignatureProductionPlaceDto }
     *     
     */
    public void setSignatureProductionPlace(XmlSignatureProductionPlaceDto value) {
        this.signatureProductionPlace = value;
    }

    /**
     * Gets the value of the signatureTimeStamps property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signatureTimeStamps property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignatureTimeStamps().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TimeStampDto }
     * 
     * 
     */
    public List<TimeStampDto> getSignatureTimeStamps() {
        if (signatureTimeStamps == null) {
            signatureTimeStamps = new ArrayList<TimeStampDto>();
        }
        return this.signatureTimeStamps;
    }

    /**
     * Recupera il valore della proprietà signatureValue.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSignatureValue() {
        return signatureValue;
    }

    /**
     * Imposta il valore della proprietà signatureValue.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSignatureValue(byte[] value) {
        this.signatureValue = value;
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

    /**
     * Gets the value of the signers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSigners().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SignerDto }
     * 
     * 
     */
    public List<SignerDto> getSigners() {
        if (signers == null) {
            signers = new ArrayList<SignerDto>();
        }
        return this.signers;
    }

    /**
     * Recupera il valore della proprietà signingTime.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSigningTime() {
        return signingTime;
    }

    /**
     * Imposta il valore della proprietà signingTime.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSigningTime(XMLGregorianCalendar value) {
        this.signingTime = value;
    }

    /**
     * Recupera il valore della proprietà startDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Imposta il valore della proprietà startDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Recupera il valore della proprietà subjectDn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubjectDn() {
        return subjectDn;
    }

    /**
     * Imposta il valore della proprietà subjectDn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubjectDn(String value) {
        this.subjectDn = value;
    }

    /**
     * Gets the value of the timeStamp property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the timeStamp property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimeStamp().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TimeStampDto }
     * 
     * 
     */
    public List<TimeStampDto> getTimeStamp() {
        if (timeStamp == null) {
            timeStamp = new ArrayList<TimeStampDto>();
        }
        return this.timeStamp;
    }

    /**
     * Recupera il valore della proprietà verificationDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVerificationDate() {
        return verificationDate;
    }

    /**
     * Imposta il valore della proprietà verificationDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVerificationDate(XMLGregorianCalendar value) {
        this.verificationDate = value;
    }

}
