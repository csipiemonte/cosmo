
package it.doqui.dosign.dosign.business.session.dosign;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per signature complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="signature">
 *   &lt;complexContent>
 *     &lt;extension base="{http://dosign.session.business.dosign.dosign.doqui.it/}dosignDto">
 *       &lt;sequence>
 *         &lt;element name="annoFirma" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="ca" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cert" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataOra" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="dataOraVerifica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dipartimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dnQualifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fineValidita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firmatario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="giornoFirma" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="givenname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inizioValidita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="meseFirma" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="minutiFirma" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="nominativoFirmatario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroControfirme" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="oraFirma" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="organizzazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paese" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="secondiFirma" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="serialNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signature" type="{http://dosign.session.business.dosign.dosign.doqui.it/}signature" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="surname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timestamped" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="tipoCertificato" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="tipoFirma" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signature", propOrder = {
    "annoFirma",
    "ca",
    "cert",
    "codiceFiscale",
    "dataOra",
    "dataOraVerifica",
    "dipartimento",
    "dnQualifier",
    "errorCode",
    "fineValidita",
    "firmatario",
    "giornoFirma",
    "givenname",
    "inizioValidita",
    "meseFirma",
    "minutiFirma",
    "nominativoFirmatario",
    "numeroControfirme",
    "oraFirma",
    "organizzazione",
    "paese",
    "secondiFirma",
    "serialNumber",
    "signature",
    "surname",
    "timestamped",
    "tipoCertificato",
    "tipoFirma"
})
@XmlSeeAlso({
    SignatureWT.class
})
public class Signature
    extends DosignDto
{

    protected long annoFirma;
    protected String ca;
    protected byte[] cert;
    protected String codiceFiscale;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataOra;
    protected String dataOraVerifica;
    protected String dipartimento;
    protected String dnQualifier;
    protected int errorCode;
    protected String fineValidita;
    protected String firmatario;
    protected long giornoFirma;
    protected String givenname;
    protected String inizioValidita;
    protected long meseFirma;
    protected long minutiFirma;
    protected String nominativoFirmatario;
    protected long numeroControfirme;
    protected long oraFirma;
    protected String organizzazione;
    protected String paese;
    protected long secondiFirma;
    protected String serialNumber;
    @XmlElement(nillable = true)
    protected List<Signature> signature;
    protected String surname;
    protected boolean timestamped;
    protected int tipoCertificato;
    protected long tipoFirma;

    /**
     * Recupera il valore della proprietà annoFirma.
     * 
     */
    public long getAnnoFirma() {
        return annoFirma;
    }

    /**
     * Imposta il valore della proprietà annoFirma.
     * 
     */
    public void setAnnoFirma(long value) {
        this.annoFirma = value;
    }

    /**
     * Recupera il valore della proprietà ca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCa() {
        return ca;
    }

    /**
     * Imposta il valore della proprietà ca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCa(String value) {
        this.ca = value;
    }

    /**
     * Recupera il valore della proprietà cert.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCert() {
        return cert;
    }

    /**
     * Imposta il valore della proprietà cert.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCert(byte[] value) {
        this.cert = value;
    }

    /**
     * Recupera il valore della proprietà codiceFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Imposta il valore della proprietà codiceFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
    }

    /**
     * Recupera il valore della proprietà dataOra.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataOra() {
        return dataOra;
    }

    /**
     * Imposta il valore della proprietà dataOra.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataOra(XMLGregorianCalendar value) {
        this.dataOra = value;
    }

    /**
     * Recupera il valore della proprietà dataOraVerifica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataOraVerifica() {
        return dataOraVerifica;
    }

    /**
     * Imposta il valore della proprietà dataOraVerifica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataOraVerifica(String value) {
        this.dataOraVerifica = value;
    }

    /**
     * Recupera il valore della proprietà dipartimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDipartimento() {
        return dipartimento;
    }

    /**
     * Imposta il valore della proprietà dipartimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDipartimento(String value) {
        this.dipartimento = value;
    }

    /**
     * Recupera il valore della proprietà dnQualifier.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDnQualifier() {
        return dnQualifier;
    }

    /**
     * Imposta il valore della proprietà dnQualifier.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDnQualifier(String value) {
        this.dnQualifier = value;
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
     * Recupera il valore della proprietà fineValidita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFineValidita() {
        return fineValidita;
    }

    /**
     * Imposta il valore della proprietà fineValidita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFineValidita(String value) {
        this.fineValidita = value;
    }

    /**
     * Recupera il valore della proprietà firmatario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirmatario() {
        return firmatario;
    }

    /**
     * Imposta il valore della proprietà firmatario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirmatario(String value) {
        this.firmatario = value;
    }

    /**
     * Recupera il valore della proprietà giornoFirma.
     * 
     */
    public long getGiornoFirma() {
        return giornoFirma;
    }

    /**
     * Imposta il valore della proprietà giornoFirma.
     * 
     */
    public void setGiornoFirma(long value) {
        this.giornoFirma = value;
    }

    /**
     * Recupera il valore della proprietà givenname.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGivenname() {
        return givenname;
    }

    /**
     * Imposta il valore della proprietà givenname.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGivenname(String value) {
        this.givenname = value;
    }

    /**
     * Recupera il valore della proprietà inizioValidita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInizioValidita() {
        return inizioValidita;
    }

    /**
     * Imposta il valore della proprietà inizioValidita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInizioValidita(String value) {
        this.inizioValidita = value;
    }

    /**
     * Recupera il valore della proprietà meseFirma.
     * 
     */
    public long getMeseFirma() {
        return meseFirma;
    }

    /**
     * Imposta il valore della proprietà meseFirma.
     * 
     */
    public void setMeseFirma(long value) {
        this.meseFirma = value;
    }

    /**
     * Recupera il valore della proprietà minutiFirma.
     * 
     */
    public long getMinutiFirma() {
        return minutiFirma;
    }

    /**
     * Imposta il valore della proprietà minutiFirma.
     * 
     */
    public void setMinutiFirma(long value) {
        this.minutiFirma = value;
    }

    /**
     * Recupera il valore della proprietà nominativoFirmatario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNominativoFirmatario() {
        return nominativoFirmatario;
    }

    /**
     * Imposta il valore della proprietà nominativoFirmatario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNominativoFirmatario(String value) {
        this.nominativoFirmatario = value;
    }

    /**
     * Recupera il valore della proprietà numeroControfirme.
     * 
     */
    public long getNumeroControfirme() {
        return numeroControfirme;
    }

    /**
     * Imposta il valore della proprietà numeroControfirme.
     * 
     */
    public void setNumeroControfirme(long value) {
        this.numeroControfirme = value;
    }

    /**
     * Recupera il valore della proprietà oraFirma.
     * 
     */
    public long getOraFirma() {
        return oraFirma;
    }

    /**
     * Imposta il valore della proprietà oraFirma.
     * 
     */
    public void setOraFirma(long value) {
        this.oraFirma = value;
    }

    /**
     * Recupera il valore della proprietà organizzazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganizzazione() {
        return organizzazione;
    }

    /**
     * Imposta il valore della proprietà organizzazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganizzazione(String value) {
        this.organizzazione = value;
    }

    /**
     * Recupera il valore della proprietà paese.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaese() {
        return paese;
    }

    /**
     * Imposta il valore della proprietà paese.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaese(String value) {
        this.paese = value;
    }

    /**
     * Recupera il valore della proprietà secondiFirma.
     * 
     */
    public long getSecondiFirma() {
        return secondiFirma;
    }

    /**
     * Imposta il valore della proprietà secondiFirma.
     * 
     */
    public void setSecondiFirma(long value) {
        this.secondiFirma = value;
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
     * Recupera il valore della proprietà surname.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Imposta il valore della proprietà surname.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSurname(String value) {
        this.surname = value;
    }

    /**
     * Recupera il valore della proprietà timestamped.
     * 
     */
    public boolean isTimestamped() {
        return timestamped;
    }

    /**
     * Imposta il valore della proprietà timestamped.
     * 
     */
    public void setTimestamped(boolean value) {
        this.timestamped = value;
    }

    /**
     * Recupera il valore della proprietà tipoCertificato.
     * 
     */
    public int getTipoCertificato() {
        return tipoCertificato;
    }

    /**
     * Imposta il valore della proprietà tipoCertificato.
     * 
     */
    public void setTipoCertificato(int value) {
        this.tipoCertificato = value;
    }

    /**
     * Recupera il valore della proprietà tipoFirma.
     * 
     */
    public long getTipoFirma() {
        return tipoFirma;
    }

    /**
     * Imposta il valore della proprietà tipoFirma.
     * 
     */
    public void setTipoFirma(long value) {
        this.tipoFirma = value;
    }

}
