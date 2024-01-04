
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per StatoDiEfficaciaType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="StatoDiEfficaciaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idStatoDiEfficacia" type="{archive.acaris.acta.doqui.it}IdStatoDiEfficaciaType"/>
 *         &lt;element name="descrizione" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="dataFineValidita" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="valoreDefault" type="{common.acaris.acta.doqui.it}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatoDiEfficaciaType", propOrder = {
    "idStatoDiEfficacia",
    "descrizione",
    "dataFineValidita",
    "valoreDefault"
})
public class StatoDiEfficaciaType {

    @XmlElement(required = true)
    protected IdStatoDiEfficaciaType idStatoDiEfficacia;
    @XmlElement(required = true)
    protected String descrizione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataFineValidita;
    protected boolean valoreDefault;

    /**
     * Recupera il valore della proprietà idStatoDiEfficacia.
     * 
     * @return
     *     possible object is
     *     {@link IdStatoDiEfficaciaType }
     *     
     */
    public IdStatoDiEfficaciaType getIdStatoDiEfficacia() {
        return idStatoDiEfficacia;
    }

    /**
     * Imposta il valore della proprietà idStatoDiEfficacia.
     * 
     * @param value
     *     allowed object is
     *     {@link IdStatoDiEfficaciaType }
     *     
     */
    public void setIdStatoDiEfficacia(IdStatoDiEfficaciaType value) {
        this.idStatoDiEfficacia = value;
    }

    /**
     * Recupera il valore della proprietà descrizione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta il valore della proprietà descrizione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizione(String value) {
        this.descrizione = value;
    }

    /**
     * Recupera il valore della proprietà dataFineValidita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataFineValidita() {
        return dataFineValidita;
    }

    /**
     * Imposta il valore della proprietà dataFineValidita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFineValidita(XMLGregorianCalendar value) {
        this.dataFineValidita = value;
    }

    /**
     * Recupera il valore della proprietà valoreDefault.
     * 
     */
    public boolean isValoreDefault() {
        return valoreDefault;
    }

    /**
     * Imposta il valore della proprietà valoreDefault.
     * 
     */
    public void setValoreDefault(boolean value) {
        this.valoreDefault = value;
    }

}
