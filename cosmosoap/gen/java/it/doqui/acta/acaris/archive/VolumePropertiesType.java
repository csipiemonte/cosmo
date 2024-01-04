
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per VolumePropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="VolumePropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}AggregazionePropertiesType">
 *       &lt;sequence>
 *         &lt;element name="intervalloNumericoDa" type="{archive.acaris.acta.doqui.it}IntervalloNumericoDaType"/>
 *         &lt;element name="intervalloNumericoA" type="{archive.acaris.acta.doqui.it}IntervalloNumericoAType"/>
 *         &lt;element name="stato" type="{archive.acaris.acta.doqui.it}enumVolumeStatoType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VolumePropertiesType", propOrder = {
    "intervalloNumericoDa",
    "intervalloNumericoA",
    "stato"
})
@XmlSeeAlso({
    VolumeSerieTipologicaDocumentiPropertiesType.class,
    VolumeSottofascicoliPropertiesType.class,
    VolumeSerieFascicoliPropertiesType.class,
    VolumeFascicoliPropertiesType.class
})
public abstract class VolumePropertiesType
    extends AggregazionePropertiesType
{

    @XmlElement(required = true)
    protected String intervalloNumericoDa;
    @XmlElement(required = true)
    protected String intervalloNumericoA;
    @XmlElement(required = true)
    protected EnumVolumeStatoType stato;

    /**
     * Recupera il valore della proprietà intervalloNumericoDa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIntervalloNumericoDa() {
        return intervalloNumericoDa;
    }

    /**
     * Imposta il valore della proprietà intervalloNumericoDa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIntervalloNumericoDa(String value) {
        this.intervalloNumericoDa = value;
    }

    /**
     * Recupera il valore della proprietà intervalloNumericoA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIntervalloNumericoA() {
        return intervalloNumericoA;
    }

    /**
     * Imposta il valore della proprietà intervalloNumericoA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIntervalloNumericoA(String value) {
        this.intervalloNumericoA = value;
    }

    /**
     * Recupera il valore della proprietà stato.
     * 
     * @return
     *     possible object is
     *     {@link EnumVolumeStatoType }
     *     
     */
    public EnumVolumeStatoType getStato() {
        return stato;
    }

    /**
     * Imposta il valore della proprietà stato.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumVolumeStatoType }
     *     
     */
    public void setStato(EnumVolumeStatoType value) {
        this.stato = value;
    }

}
