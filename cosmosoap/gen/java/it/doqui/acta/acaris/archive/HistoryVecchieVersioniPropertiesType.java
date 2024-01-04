
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per HistoryVecchieVersioniPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="HistoryVecchieVersioniPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}RelationshipPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="numeroVersione" type="{archive.acaris.acta.doqui.it}NumeroVersioneType"/>
 *         &lt;element name="dataVersione" type="{archive.acaris.acta.doqui.it}DataVersioneType"/>
 *         &lt;element name="motivazioneVersione" type="{archive.acaris.acta.doqui.it}MotivazioneVersioneType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HistoryVecchieVersioniPropertiesType", propOrder = {
    "numeroVersione",
    "dataVersione",
    "motivazioneVersione"
})
public class HistoryVecchieVersioniPropertiesType
    extends RelationshipPropertiesType
{

    protected int numeroVersione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataVersione;
    @XmlElement(required = true)
    protected String motivazioneVersione;

    /**
     * Recupera il valore della proprietà numeroVersione.
     * 
     */
    public int getNumeroVersione() {
        return numeroVersione;
    }

    /**
     * Imposta il valore della proprietà numeroVersione.
     * 
     */
    public void setNumeroVersione(int value) {
        this.numeroVersione = value;
    }

    /**
     * Recupera il valore della proprietà dataVersione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataVersione() {
        return dataVersione;
    }

    /**
     * Imposta il valore della proprietà dataVersione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataVersione(XMLGregorianCalendar value) {
        this.dataVersione = value;
    }

    /**
     * Recupera il valore della proprietà motivazioneVersione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotivazioneVersione() {
        return motivazioneVersione;
    }

    /**
     * Imposta il valore della proprietà motivazioneVersione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotivazioneVersione(String value) {
        this.motivazioneVersione = value;
    }

}
