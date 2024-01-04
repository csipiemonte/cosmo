
package it.doqui.dosign.dosign.business.session.dosign;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per verifyParameter complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="verifyParameter">
 *   &lt;complexContent>
 *     &lt;extension base="{http://dosign.session.business.dosign.dosign.doqui.it/}dosignDto">
 *       &lt;sequence>
 *         &lt;element name="profileType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="verificationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
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
@XmlType(name = "verifyParameter", propOrder = {
    "profileType",
    "verificationDate",
    "verificationScope",
    "verificationType"
})
public class VerifyParameter
    extends DosignDto
{

    protected int profileType;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar verificationDate;
    protected int verificationScope;
    protected int verificationType;

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
