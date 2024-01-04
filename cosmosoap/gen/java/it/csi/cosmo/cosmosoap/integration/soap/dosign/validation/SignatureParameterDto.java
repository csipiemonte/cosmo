
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per signatureParameterDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="signatureParameterDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}dosignDataDto">
 *       &lt;sequence>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="extractData" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="profileType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="verificationScope" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="verificationType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signatureParameterDto", propOrder = {
    "date",
    "extractData",
    "profileType",
    "verificationScope",
    "verificationType"
})
@XmlSeeAlso({
    VerifyDto.class
})
public class SignatureParameterDto
    extends DosignDataDto
{

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    protected boolean extractData;
    protected int profileType;
    protected int verificationScope;
    protected int verificationType;

    /**
     * Recupera il valore della proprietà date.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Imposta il valore della proprietà date.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Recupera il valore della proprietà extractData.
     * 
     */
    public boolean isExtractData() {
        return extractData;
    }

    /**
     * Imposta il valore della proprietà extractData.
     * 
     */
    public void setExtractData(boolean value) {
        this.extractData = value;
    }

    /**
     * Recupera il valore della proprietà profileType.
     * 
     */
    public int getProfileType() {
        return profileType;
    }

    /**
     * Imposta il valore della proprietà profileType.
     * 
     */
    public void setProfileType(int value) {
        this.profileType = value;
    }

    /**
     * Recupera il valore della proprietà verificationScope.
     * 
     */
    public int getVerificationScope() {
        return verificationScope;
    }

    /**
     * Imposta il valore della proprietà verificationScope.
     * 
     */
    public void setVerificationScope(int value) {
        this.verificationScope = value;
    }

    /**
     * Recupera il valore della proprietà verificationType.
     * 
     */
    public int getVerificationType() {
        return verificationType;
    }

    /**
     * Imposta il valore della proprietà verificationType.
     * 
     */
    public void setVerificationType(int value) {
        this.verificationType = value;
    }

}
