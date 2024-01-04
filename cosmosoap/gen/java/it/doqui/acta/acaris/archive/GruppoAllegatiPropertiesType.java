
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per GruppoAllegatiPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="GruppoAllegatiPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}FolderPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="numeroAllegati" type="{archive.acaris.acta.doqui.it}NumeroAllegatiType"/>
 *         &lt;element name="dataInizio" type="{archive.acaris.acta.doqui.it}DataInizioType"/>
 *         &lt;element name="dataFine" type="{archive.acaris.acta.doqui.it}DataFineType"/>
 *         &lt;element name="classificazionePrincipale" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GruppoAllegatiPropertiesType", propOrder = {
    "numeroAllegati",
    "dataInizio",
    "dataFine",
    "classificazionePrincipale"
})
public class GruppoAllegatiPropertiesType
    extends FolderPropertiesType
{

    protected int numeroAllegati;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataInizio;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataFine;
    @XmlElement(required = true)
    protected ObjectIdType classificazionePrincipale;

    /**
     * Recupera il valore della proprietà numeroAllegati.
     * 
     */
    public int getNumeroAllegati() {
        return numeroAllegati;
    }

    /**
     * Imposta il valore della proprietà numeroAllegati.
     * 
     */
    public void setNumeroAllegati(int value) {
        this.numeroAllegati = value;
    }

    /**
     * Recupera il valore della proprietà dataInizio.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInizio() {
        return dataInizio;
    }

    /**
     * Imposta il valore della proprietà dataInizio.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInizio(XMLGregorianCalendar value) {
        this.dataInizio = value;
    }

    /**
     * Recupera il valore della proprietà dataFine.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataFine() {
        return dataFine;
    }

    /**
     * Imposta il valore della proprietà dataFine.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFine(XMLGregorianCalendar value) {
        this.dataFine = value;
    }

    /**
     * Recupera il valore della proprietà classificazionePrincipale.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getClassificazionePrincipale() {
        return classificazionePrincipale;
    }

    /**
     * Imposta il valore della proprietà classificazionePrincipale.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setClassificazionePrincipale(ObjectIdType value) {
        this.classificazionePrincipale = value;
    }

}
