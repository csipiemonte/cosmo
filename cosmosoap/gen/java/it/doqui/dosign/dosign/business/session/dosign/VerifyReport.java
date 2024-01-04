
package it.doqui.dosign.dosign.business.session.dosign;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per verifyReport complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="verifyReport">
 *   &lt;complexContent>
 *     &lt;extension base="{http://dosign.session.business.dosign.dosign.doqui.it/}dosignDto">
 *       &lt;sequence>
 *         &lt;element name="child" type="{http://dosign.session.business.dosign.dosign.doqui.it/}verifyReport" minOccurs="0"/>
 *         &lt;element name="conformitaParametriInput" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="formatoFirma" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numCertificatiFirma" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numCertificatiMarca" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="signature" type="{http://dosign.session.business.dosign.dosign.doqui.it/}signature" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="tipoFirma" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "verifyReport", propOrder = {
    "child",
    "conformitaParametriInput",
    "data",
    "date",
    "errorCode",
    "formatoFirma",
    "numCertificatiFirma",
    "numCertificatiMarca",
    "signature",
    "tipoFirma"
})
public class VerifyReport
    extends DosignDto
{

    protected VerifyReport child;
    protected int conformitaParametriInput;
    protected byte[] data;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    protected int errorCode;
    protected int formatoFirma;
    protected int numCertificatiFirma;
    protected int numCertificatiMarca;
    @XmlElement(nillable = true)
    protected List<Signature> signature;
    protected int tipoFirma;

    /**
     * Recupera il valore della proprietà child.
     * 
     * @return
     *     possible object is
     *     {@link VerifyReport }
     *     
     */
    public VerifyReport getChild() {
        return child;
    }

    /**
     * Imposta il valore della proprietà child.
     * 
     * @param value
     *     allowed object is
     *     {@link VerifyReport }
     *     
     */
    public void setChild(VerifyReport value) {
        this.child = value;
    }

    /**
     * Recupera il valore della proprietà conformitaParametriInput.
     * 
     */
    public int getConformitaParametriInput() {
        return conformitaParametriInput;
    }

    /**
     * Imposta il valore della proprietà conformitaParametriInput.
     * 
     */
    public void setConformitaParametriInput(int value) {
        this.conformitaParametriInput = value;
    }

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
     * Recupera il valore della proprietà formatoFirma.
     * 
     */
    public int getFormatoFirma() {
        return formatoFirma;
    }

    /**
     * Imposta il valore della proprietà formatoFirma.
     * 
     */
    public void setFormatoFirma(int value) {
        this.formatoFirma = value;
    }

    /**
     * Recupera il valore della proprietà numCertificatiFirma.
     * 
     */
    public int getNumCertificatiFirma() {
        return numCertificatiFirma;
    }

    /**
     * Imposta il valore della proprietà numCertificatiFirma.
     * 
     */
    public void setNumCertificatiFirma(int value) {
        this.numCertificatiFirma = value;
    }

    /**
     * Recupera il valore della proprietà numCertificatiMarca.
     * 
     */
    public int getNumCertificatiMarca() {
        return numCertificatiMarca;
    }

    /**
     * Imposta il valore della proprietà numCertificatiMarca.
     * 
     */
    public void setNumCertificatiMarca(int value) {
        this.numCertificatiMarca = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signature property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignature().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Signature }
     * 
     * 
     */
    public List<Signature> getSignature() {
        if (signature == null) {
            signature = new ArrayList<Signature>();
        }
        return this.signature;
    }

    /**
     * Recupera il valore della proprietà tipoFirma.
     * 
     */
    public int getTipoFirma() {
        return tipoFirma;
    }

    /**
     * Imposta il valore della proprietà tipoFirma.
     * 
     */
    public void setTipoFirma(int value) {
        this.tipoFirma = value;
    }

}
