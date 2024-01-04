
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.prt.EnumPFPGUL;


/**
 * <p>Classe Java per SoggettoEsterno complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="SoggettoEsterno">
 *   &lt;complexContent>
 *     &lt;extension base="{officialbookservice.acaris.acta.doqui.it}InfoSoggettoAssociato">
 *       &lt;sequence>
 *         &lt;element name="chiaveEsterna" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="idPFPGUL" type="{prt.common.acaris.acta.doqui.it}enumPFPGUL"/>
 *         &lt;element name="codiceTipoSoggetto" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="codiceFonte" type="{common.acaris.acta.doqui.it}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoggettoEsterno", propOrder = {
    "chiaveEsterna",
    "idPFPGUL",
    "codiceTipoSoggetto",
    "codiceFonte"
})
public class SoggettoEsterno
    extends InfoSoggettoAssociato
{

    @XmlElement(required = true)
    protected String chiaveEsterna;
    @XmlElement(required = true)
    protected EnumPFPGUL idPFPGUL;
    @XmlElement(required = true)
    protected String codiceTipoSoggetto;
    @XmlElement(required = true)
    protected String codiceFonte;

    /**
     * Recupera il valore della proprietà chiaveEsterna.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChiaveEsterna() {
        return chiaveEsterna;
    }

    /**
     * Imposta il valore della proprietà chiaveEsterna.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChiaveEsterna(String value) {
        this.chiaveEsterna = value;
    }

    /**
     * Recupera il valore della proprietà idPFPGUL.
     * 
     * @return
     *     possible object is
     *     {@link EnumPFPGUL }
     *     
     */
    public EnumPFPGUL getIdPFPGUL() {
        return idPFPGUL;
    }

    /**
     * Imposta il valore della proprietà idPFPGUL.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumPFPGUL }
     *     
     */
    public void setIdPFPGUL(EnumPFPGUL value) {
        this.idPFPGUL = value;
    }

    /**
     * Recupera il valore della proprietà codiceTipoSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceTipoSoggetto() {
        return codiceTipoSoggetto;
    }

    /**
     * Imposta il valore della proprietà codiceTipoSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceTipoSoggetto(String value) {
        this.codiceTipoSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà codiceFonte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFonte() {
        return codiceFonte;
    }

    /**
     * Imposta il valore della proprietà codiceFonte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFonte(String value) {
        this.codiceFonte = value;
    }

}
