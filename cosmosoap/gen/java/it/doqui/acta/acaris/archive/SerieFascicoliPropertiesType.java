
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per SerieFascicoliPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="SerieFascicoliPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}SeriePropertiesType">
 *       &lt;sequence>
 *         &lt;element name="obbligoFascStand" type="{archive.acaris.acta.doqui.it}ObbligoFascStandType"/>
 *         &lt;element name="tipologiaNumerazione" type="{archive.acaris.acta.doqui.it}enumTipologiaNumerazioneType"/>
 *         &lt;element name="idFascicoloStandard" type="{archive.acaris.acta.doqui.it}IdFascicoloStandardType"/>
 *         &lt;element name="stato" type="{archive.acaris.acta.doqui.it}enumSerieFascicoliStatoType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SerieFascicoliPropertiesType", propOrder = {
    "obbligoFascStand",
    "tipologiaNumerazione",
    "idFascicoloStandard",
    "stato"
})
public class SerieFascicoliPropertiesType
    extends SeriePropertiesType
{

    protected boolean obbligoFascStand;
    @XmlElement(required = true)
    protected EnumTipologiaNumerazioneType tipologiaNumerazione;
    @XmlElement(required = true)
    protected IdFascicoloStandardType idFascicoloStandard;
    @XmlElement(required = true)
    protected EnumSerieFascicoliStatoType stato;

    /**
     * Recupera il valore della proprietà obbligoFascStand.
     * 
     */
    public boolean isObbligoFascStand() {
        return obbligoFascStand;
    }

    /**
     * Imposta il valore della proprietà obbligoFascStand.
     * 
     */
    public void setObbligoFascStand(boolean value) {
        this.obbligoFascStand = value;
    }

    /**
     * Recupera il valore della proprietà tipologiaNumerazione.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipologiaNumerazioneType }
     *     
     */
    public EnumTipologiaNumerazioneType getTipologiaNumerazione() {
        return tipologiaNumerazione;
    }

    /**
     * Imposta il valore della proprietà tipologiaNumerazione.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipologiaNumerazioneType }
     *     
     */
    public void setTipologiaNumerazione(EnumTipologiaNumerazioneType value) {
        this.tipologiaNumerazione = value;
    }

    /**
     * Recupera il valore della proprietà idFascicoloStandard.
     * 
     * @return
     *     possible object is
     *     {@link IdFascicoloStandardType }
     *     
     */
    public IdFascicoloStandardType getIdFascicoloStandard() {
        return idFascicoloStandard;
    }

    /**
     * Imposta il valore della proprietà idFascicoloStandard.
     * 
     * @param value
     *     allowed object is
     *     {@link IdFascicoloStandardType }
     *     
     */
    public void setIdFascicoloStandard(IdFascicoloStandardType value) {
        this.idFascicoloStandard = value;
    }

    /**
     * Recupera il valore della proprietà stato.
     * 
     * @return
     *     possible object is
     *     {@link EnumSerieFascicoliStatoType }
     *     
     */
    public EnumSerieFascicoliStatoType getStato() {
        return stato;
    }

    /**
     * Imposta il valore della proprietà stato.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumSerieFascicoliStatoType }
     *     
     */
    public void setStato(EnumSerieFascicoliStatoType value) {
        this.stato = value;
    }

}
