
package it.doqui.acta.acaris.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import it.doqui.acta.acaris.common.IdProvvedimentoAutorizzatType;


/**
 * <p>Classe Java per ProvvedimentoAutorizzatType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ProvvedimentoAutorizzatType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idProvvedimentoAutorizzat" type="{common.acaris.acta.doqui.it}IdProvvedimentoAutorizzatType"/>
 *         &lt;element name="numero" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="dataAppr" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="organo" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="tipoDoc" type="{management.acaris.acta.doqui.it}enumTipoDocType"/>
 *         &lt;element name="descr" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="tipoProvv" type="{management.acaris.acta.doqui.it}enumTipoProvvType"/>
 *         &lt;element name="note" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="indiceClassDocumento" type="{common.acaris.acta.doqui.it}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProvvedimentoAutorizzatType", propOrder = {
    "idProvvedimentoAutorizzat",
    "numero",
    "dataAppr",
    "organo",
    "tipoDoc",
    "descr",
    "tipoProvv",
    "note",
    "indiceClassDocumento"
})
public class ProvvedimentoAutorizzatType {

    @XmlElement(required = true)
    protected IdProvvedimentoAutorizzatType idProvvedimentoAutorizzat;
    @XmlElement(required = true)
    protected String numero;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataAppr;
    @XmlElement(required = true)
    protected String organo;
    @XmlElement(required = true)
    protected EnumTipoDocType tipoDoc;
    @XmlElement(required = true)
    protected String descr;
    @XmlElement(required = true)
    protected EnumTipoProvvType tipoProvv;
    @XmlElement(required = true)
    protected String note;
    @XmlElement(required = true)
    protected String indiceClassDocumento;

    /**
     * Recupera il valore della proprietà idProvvedimentoAutorizzat.
     * 
     * @return
     *     possible object is
     *     {@link IdProvvedimentoAutorizzatType }
     *     
     */
    public IdProvvedimentoAutorizzatType getIdProvvedimentoAutorizzat() {
        return idProvvedimentoAutorizzat;
    }

    /**
     * Imposta il valore della proprietà idProvvedimentoAutorizzat.
     * 
     * @param value
     *     allowed object is
     *     {@link IdProvvedimentoAutorizzatType }
     *     
     */
    public void setIdProvvedimentoAutorizzat(IdProvvedimentoAutorizzatType value) {
        this.idProvvedimentoAutorizzat = value;
    }

    /**
     * Recupera il valore della proprietà numero.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Imposta il valore della proprietà numero.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumero(String value) {
        this.numero = value;
    }

    /**
     * Recupera il valore della proprietà dataAppr.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataAppr() {
        return dataAppr;
    }

    /**
     * Imposta il valore della proprietà dataAppr.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataAppr(XMLGregorianCalendar value) {
        this.dataAppr = value;
    }

    /**
     * Recupera il valore della proprietà organo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgano() {
        return organo;
    }

    /**
     * Imposta il valore della proprietà organo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgano(String value) {
        this.organo = value;
    }

    /**
     * Recupera il valore della proprietà tipoDoc.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipoDocType }
     *     
     */
    public EnumTipoDocType getTipoDoc() {
        return tipoDoc;
    }

    /**
     * Imposta il valore della proprietà tipoDoc.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipoDocType }
     *     
     */
    public void setTipoDoc(EnumTipoDocType value) {
        this.tipoDoc = value;
    }

    /**
     * Recupera il valore della proprietà descr.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescr() {
        return descr;
    }

    /**
     * Imposta il valore della proprietà descr.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescr(String value) {
        this.descr = value;
    }

    /**
     * Recupera il valore della proprietà tipoProvv.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipoProvvType }
     *     
     */
    public EnumTipoProvvType getTipoProvv() {
        return tipoProvv;
    }

    /**
     * Imposta il valore della proprietà tipoProvv.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipoProvvType }
     *     
     */
    public void setTipoProvv(EnumTipoProvvType value) {
        this.tipoProvv = value;
    }

    /**
     * Recupera il valore della proprietà note.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNote() {
        return note;
    }

    /**
     * Imposta il valore della proprietà note.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

    /**
     * Recupera il valore della proprietà indiceClassDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndiceClassDocumento() {
        return indiceClassDocumento;
    }

    /**
     * Imposta il valore della proprietà indiceClassDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndiceClassDocumento(String value) {
        this.indiceClassDocumento = value;
    }

}
