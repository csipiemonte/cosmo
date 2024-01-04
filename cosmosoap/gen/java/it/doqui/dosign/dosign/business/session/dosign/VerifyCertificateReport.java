
package it.doqui.dosign.dosign.business.session.dosign;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per verifyCertificateReport complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="verifyCertificateReport">
 *   &lt;complexContent>
 *     &lt;extension base="{http://dosign.session.business.dosign.dosign.doqui.it/}dosignDto">
 *       &lt;sequence>
 *         &lt;element name="caCertExpirationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="caCertRevocationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificationAuthority" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="crlDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="crlExpirationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="errorDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expiredCertsOnCRL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="holdDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="invalidSince" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="issuerAltName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="keyUsage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="keyUsageString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OCSPExpired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OCSPNextUpdate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OCSPProducedAt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OCSPThisUpdate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="revocationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serialNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="validFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="validTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="verificationDateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="verificationResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "verifyCertificateReport", propOrder = {
    "caCertExpirationDate",
    "caCertRevocationDate",
    "certificationAuthority",
    "crlDate",
    "crlExpirationDate",
    "errorCode",
    "errorDescription",
    "expiredCertsOnCRL",
    "holdDate",
    "invalidSince",
    "issuerAltName",
    "keyUsage",
    "keyUsageString",
    "ocspExpired",
    "ocspNextUpdate",
    "ocspProducedAt",
    "ocspThisUpdate",
    "revocationDate",
    "serialNumber",
    "subject",
    "validFrom",
    "validTo",
    "verificationDateTime",
    "verificationResult"
})
public class VerifyCertificateReport
    extends DosignDto
{

    protected String caCertExpirationDate;
    protected String caCertRevocationDate;
    protected String certificationAuthority;
    protected String crlDate;
    protected String crlExpirationDate;
    protected int errorCode;
    protected String errorDescription;
    protected String expiredCertsOnCRL;
    protected String holdDate;
    protected String invalidSince;
    protected String issuerAltName;
    protected String keyUsage;
    protected String keyUsageString;
    @XmlElement(name = "OCSPExpired")
    protected String ocspExpired;
    @XmlElement(name = "OCSPNextUpdate")
    protected String ocspNextUpdate;
    @XmlElement(name = "OCSPProducedAt")
    protected String ocspProducedAt;
    @XmlElement(name = "OCSPThisUpdate")
    protected String ocspThisUpdate;
    protected String revocationDate;
    protected String serialNumber;
    protected String subject;
    protected String validFrom;
    protected String validTo;
    protected String verificationDateTime;
    protected String verificationResult;

    /**
     * Recupera il valore della proprietà caCertExpirationDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaCertExpirationDate() {
        return caCertExpirationDate;
    }

    /**
     * Imposta il valore della proprietà caCertExpirationDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaCertExpirationDate(String value) {
        this.caCertExpirationDate = value;
    }

    /**
     * Recupera il valore della proprietà caCertRevocationDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaCertRevocationDate() {
        return caCertRevocationDate;
    }

    /**
     * Imposta il valore della proprietà caCertRevocationDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaCertRevocationDate(String value) {
        this.caCertRevocationDate = value;
    }

    /**
     * Recupera il valore della proprietà certificationAuthority.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificationAuthority() {
        return certificationAuthority;
    }

    /**
     * Imposta il valore della proprietà certificationAuthority.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificationAuthority(String value) {
        this.certificationAuthority = value;
    }

    /**
     * Recupera il valore della proprietà crlDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCrlDate() {
        return crlDate;
    }

    /**
     * Imposta il valore della proprietà crlDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCrlDate(String value) {
        this.crlDate = value;
    }

    /**
     * Recupera il valore della proprietà crlExpirationDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCrlExpirationDate() {
        return crlExpirationDate;
    }

    /**
     * Imposta il valore della proprietà crlExpirationDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCrlExpirationDate(String value) {
        this.crlExpirationDate = value;
    }

    /**
     * Recupera il valore della proprietà errorCode.
     * 
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Imposta il valore della proprietà errorCode.
     * 
     */
    public void setErrorCode(int value) {
        this.errorCode = value;
    }

    /**
     * Recupera il valore della proprietà errorDescription.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * Imposta il valore della proprietà errorDescription.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorDescription(String value) {
        this.errorDescription = value;
    }

    /**
     * Recupera il valore della proprietà expiredCertsOnCRL.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpiredCertsOnCRL() {
        return expiredCertsOnCRL;
    }

    /**
     * Imposta il valore della proprietà expiredCertsOnCRL.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpiredCertsOnCRL(String value) {
        this.expiredCertsOnCRL = value;
    }

    /**
     * Recupera il valore della proprietà holdDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHoldDate() {
        return holdDate;
    }

    /**
     * Imposta il valore della proprietà holdDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHoldDate(String value) {
        this.holdDate = value;
    }

    /**
     * Recupera il valore della proprietà invalidSince.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvalidSince() {
        return invalidSince;
    }

    /**
     * Imposta il valore della proprietà invalidSince.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvalidSince(String value) {
        this.invalidSince = value;
    }

    /**
     * Recupera il valore della proprietà issuerAltName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuerAltName() {
        return issuerAltName;
    }

    /**
     * Imposta il valore della proprietà issuerAltName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuerAltName(String value) {
        this.issuerAltName = value;
    }

    /**
     * Recupera il valore della proprietà keyUsage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyUsage() {
        return keyUsage;
    }

    /**
     * Imposta il valore della proprietà keyUsage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyUsage(String value) {
        this.keyUsage = value;
    }

    /**
     * Recupera il valore della proprietà keyUsageString.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyUsageString() {
        return keyUsageString;
    }

    /**
     * Imposta il valore della proprietà keyUsageString.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyUsageString(String value) {
        this.keyUsageString = value;
    }

    /**
     * Recupera il valore della proprietà ocspExpired.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOCSPExpired() {
        return ocspExpired;
    }

    /**
     * Imposta il valore della proprietà ocspExpired.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOCSPExpired(String value) {
        this.ocspExpired = value;
    }

    /**
     * Recupera il valore della proprietà ocspNextUpdate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOCSPNextUpdate() {
        return ocspNextUpdate;
    }

    /**
     * Imposta il valore della proprietà ocspNextUpdate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOCSPNextUpdate(String value) {
        this.ocspNextUpdate = value;
    }

    /**
     * Recupera il valore della proprietà ocspProducedAt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOCSPProducedAt() {
        return ocspProducedAt;
    }

    /**
     * Imposta il valore della proprietà ocspProducedAt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOCSPProducedAt(String value) {
        this.ocspProducedAt = value;
    }

    /**
     * Recupera il valore della proprietà ocspThisUpdate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOCSPThisUpdate() {
        return ocspThisUpdate;
    }

    /**
     * Imposta il valore della proprietà ocspThisUpdate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOCSPThisUpdate(String value) {
        this.ocspThisUpdate = value;
    }

    /**
     * Recupera il valore della proprietà revocationDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRevocationDate() {
        return revocationDate;
    }

    /**
     * Imposta il valore della proprietà revocationDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRevocationDate(String value) {
        this.revocationDate = value;
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
     * Recupera il valore della proprietà subject.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Imposta il valore della proprietà subject.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubject(String value) {
        this.subject = value;
    }

    /**
     * Recupera il valore della proprietà validFrom.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidFrom() {
        return validFrom;
    }

    /**
     * Imposta il valore della proprietà validFrom.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidFrom(String value) {
        this.validFrom = value;
    }

    /**
     * Recupera il valore della proprietà validTo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidTo() {
        return validTo;
    }

    /**
     * Imposta il valore della proprietà validTo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidTo(String value) {
        this.validTo = value;
    }

    /**
     * Recupera il valore della proprietà verificationDateTime.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerificationDateTime() {
        return verificationDateTime;
    }

    /**
     * Imposta il valore della proprietà verificationDateTime.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerificationDateTime(String value) {
        this.verificationDateTime = value;
    }

    /**
     * Recupera il valore della proprietà verificationResult.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerificationResult() {
        return verificationResult;
    }

    /**
     * Imposta il valore della proprietà verificationResult.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerificationResult(String value) {
        this.verificationResult = value;
    }

}
