
package it.doqui.dosign.dosign.business.session.dosign;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per verifyCertificate complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="verifyCertificate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://dosign.session.business.dosign.dosign.doqui.it/}certBuffer" minOccurs="0"/>
 *         &lt;element name="arg1" type="{http://dosign.session.business.dosign.dosign.doqui.it/}verifyParameter" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "verifyCertificate", propOrder = {
    "arg0",
    "arg1"
})
public class VerifyCertificate {

    protected CertBuffer arg0;
    protected VerifyParameter arg1;

    /**
     * Recupera il valore della proprietà arg0.
     * 
     * @return
     *     possible object is
     *     {@link CertBuffer }
     *     
     */
    public CertBuffer getArg0() {
        return arg0;
    }

    /**
     * Imposta il valore della proprietà arg0.
     * 
     * @param value
     *     allowed object is
     *     {@link CertBuffer }
     *     
     */
    public void setArg0(CertBuffer value) {
        this.arg0 = value;
    }

    /**
     * Recupera il valore della proprietà arg1.
     * 
     * @return
     *     possible object is
     *     {@link VerifyParameter }
     *     
     */
    public VerifyParameter getArg1() {
        return arg1;
    }

    /**
     * Imposta il valore della proprietà arg1.
     * 
     * @param value
     *     allowed object is
     *     {@link VerifyParameter }
     *     
     */
    public void setArg1(VerifyParameter value) {
        this.arg1 = value;
    }

}
