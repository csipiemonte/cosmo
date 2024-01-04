
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per DocumentoRegistroPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DocumentoRegistroPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}DocumentoPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="codiceRegistro" type="{archive.acaris.acta.doqui.it}CodiceRegistroType"/>
 *         &lt;element name="annoRegistro" type="{archive.acaris.acta.doqui.it}AnnoRegistroType"/>
 *         &lt;element name="numeroPrimaRegistrazione" type="{archive.acaris.acta.doqui.it}NumeroPrimaRegistrazioneType"/>
 *         &lt;element name="dataPrimaRegistrazione" type="{archive.acaris.acta.doqui.it}DataPrimaRegistrazioneType"/>
 *         &lt;element name="numeroUltimaRegistrazione" type="{archive.acaris.acta.doqui.it}NumeroUltimaRegistrazioneType"/>
 *         &lt;element name="dataUltimaRegistrazione" type="{archive.acaris.acta.doqui.it}DataUltimaRegistrazioneType"/>
 *         &lt;element name="dataApertura" type="{archive.acaris.acta.doqui.it}DataAperturaType"/>
 *         &lt;element name="dataChiusura" type="{archive.acaris.acta.doqui.it}DataChiusuraType"/>
 *         &lt;element name="ubicazione" type="{archive.acaris.acta.doqui.it}UbicazioneType"/>
 *         &lt;element name="versioneSW" type="{archive.acaris.acta.doqui.it}VersioneSWType"/>
 *         &lt;element name="tipoDocFisico" type="{archive.acaris.acta.doqui.it}enumTipoDocumentoType"/>
 *         &lt;element name="composizione" type="{archive.acaris.acta.doqui.it}enumDocPrimarioType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentoRegistroPropertiesType", propOrder = {
    "codiceRegistro",
    "annoRegistro",
    "numeroPrimaRegistrazione",
    "dataPrimaRegistrazione",
    "numeroUltimaRegistrazione",
    "dataUltimaRegistrazione",
    "dataApertura",
    "dataChiusura",
    "ubicazione",
    "versioneSW",
    "tipoDocFisico",
    "composizione"
})
public class DocumentoRegistroPropertiesType
    extends DocumentoPropertiesType
{

    @XmlElement(required = true)
    protected String codiceRegistro;
    protected int annoRegistro;
    @XmlElement(required = true)
    protected String numeroPrimaRegistrazione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataPrimaRegistrazione;
    @XmlElement(required = true)
    protected String numeroUltimaRegistrazione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataUltimaRegistrazione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataApertura;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataChiusura;
    @XmlElement(required = true)
    protected String ubicazione;
    @XmlElement(required = true)
    protected String versioneSW;
    @XmlElement(required = true)
    protected EnumTipoDocumentoType tipoDocFisico;
    @XmlElement(required = true)
    protected EnumDocPrimarioType composizione;

    /**
     * Recupera il valore della proprietà codiceRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceRegistro() {
        return codiceRegistro;
    }

    /**
     * Imposta il valore della proprietà codiceRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceRegistro(String value) {
        this.codiceRegistro = value;
    }

    /**
     * Recupera il valore della proprietà annoRegistro.
     * 
     */
    public int getAnnoRegistro() {
        return annoRegistro;
    }

    /**
     * Imposta il valore della proprietà annoRegistro.
     * 
     */
    public void setAnnoRegistro(int value) {
        this.annoRegistro = value;
    }

    /**
     * Recupera il valore della proprietà numeroPrimaRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroPrimaRegistrazione() {
        return numeroPrimaRegistrazione;
    }

    /**
     * Imposta il valore della proprietà numeroPrimaRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroPrimaRegistrazione(String value) {
        this.numeroPrimaRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà dataPrimaRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataPrimaRegistrazione() {
        return dataPrimaRegistrazione;
    }

    /**
     * Imposta il valore della proprietà dataPrimaRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataPrimaRegistrazione(XMLGregorianCalendar value) {
        this.dataPrimaRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà numeroUltimaRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroUltimaRegistrazione() {
        return numeroUltimaRegistrazione;
    }

    /**
     * Imposta il valore della proprietà numeroUltimaRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroUltimaRegistrazione(String value) {
        this.numeroUltimaRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà dataUltimaRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataUltimaRegistrazione() {
        return dataUltimaRegistrazione;
    }

    /**
     * Imposta il valore della proprietà dataUltimaRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataUltimaRegistrazione(XMLGregorianCalendar value) {
        this.dataUltimaRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà dataApertura.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataApertura() {
        return dataApertura;
    }

    /**
     * Imposta il valore della proprietà dataApertura.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataApertura(XMLGregorianCalendar value) {
        this.dataApertura = value;
    }

    /**
     * Recupera il valore della proprietà dataChiusura.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataChiusura() {
        return dataChiusura;
    }

    /**
     * Imposta il valore della proprietà dataChiusura.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataChiusura(XMLGregorianCalendar value) {
        this.dataChiusura = value;
    }

    /**
     * Recupera il valore della proprietà ubicazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUbicazione() {
        return ubicazione;
    }

    /**
     * Imposta il valore della proprietà ubicazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUbicazione(String value) {
        this.ubicazione = value;
    }

    /**
     * Recupera il valore della proprietà versioneSW.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersioneSW() {
        return versioneSW;
    }

    /**
     * Imposta il valore della proprietà versioneSW.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersioneSW(String value) {
        this.versioneSW = value;
    }

    /**
     * Recupera il valore della proprietà tipoDocFisico.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipoDocumentoType }
     *     
     */
    public EnumTipoDocumentoType getTipoDocFisico() {
        return tipoDocFisico;
    }

    /**
     * Imposta il valore della proprietà tipoDocFisico.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipoDocumentoType }
     *     
     */
    public void setTipoDocFisico(EnumTipoDocumentoType value) {
        this.tipoDocFisico = value;
    }

    /**
     * Recupera il valore della proprietà composizione.
     * 
     * @return
     *     possible object is
     *     {@link EnumDocPrimarioType }
     *     
     */
    public EnumDocPrimarioType getComposizione() {
        return composizione;
    }

    /**
     * Imposta il valore della proprietà composizione.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumDocPrimarioType }
     *     
     */
    public void setComposizione(EnumDocPrimarioType value) {
        this.composizione = value;
    }

}
