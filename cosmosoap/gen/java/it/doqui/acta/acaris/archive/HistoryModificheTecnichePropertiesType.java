
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per HistoryModificheTecnichePropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="HistoryModificheTecnichePropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}RelationshipPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="dataTrasformazione" type="{archive.acaris.acta.doqui.it}DataTrasformazioneType"/>
 *         &lt;element name="motivazioneTrasformazione" type="{archive.acaris.acta.doqui.it}enumMotivazioneTrasformazioneType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HistoryModificheTecnichePropertiesType", propOrder = {
    "dataTrasformazione",
    "motivazioneTrasformazione"
})
public class HistoryModificheTecnichePropertiesType
    extends RelationshipPropertiesType
{

    @XmlElement(required = true)
    protected XMLGregorianCalendar dataTrasformazione;
    @XmlElement(required = true)
    protected EnumMotivazioneTrasformazioneType motivazioneTrasformazione;

    /**
     * Recupera il valore della proprietà dataTrasformazione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataTrasformazione() {
        return dataTrasformazione;
    }

    /**
     * Imposta il valore della proprietà dataTrasformazione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataTrasformazione(XMLGregorianCalendar value) {
        this.dataTrasformazione = value;
    }

    /**
     * Recupera il valore della proprietà motivazioneTrasformazione.
     * 
     * @return
     *     possible object is
     *     {@link EnumMotivazioneTrasformazioneType }
     *     
     */
    public EnumMotivazioneTrasformazioneType getMotivazioneTrasformazione() {
        return motivazioneTrasformazione;
    }

    /**
     * Imposta il valore della proprietà motivazioneTrasformazione.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumMotivazioneTrasformazioneType }
     *     
     */
    public void setMotivazioneTrasformazione(EnumMotivazioneTrasformazioneType value) {
        this.motivazioneTrasformazione = value;
    }

}
