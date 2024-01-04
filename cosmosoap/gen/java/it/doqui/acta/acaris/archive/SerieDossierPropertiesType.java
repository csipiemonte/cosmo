
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per SerieDossierPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="SerieDossierPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}SeriePropertiesType">
 *       &lt;sequence>
 *         &lt;element name="stato" type="{archive.acaris.acta.doqui.it}enumSerieDossierStatoType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SerieDossierPropertiesType", propOrder = {
    "stato"
})
public class SerieDossierPropertiesType
    extends SeriePropertiesType
{

    @XmlElement(required = true)
    protected EnumSerieDossierStatoType stato;

    /**
     * Recupera il valore della proprietà stato.
     * 
     * @return
     *     possible object is
     *     {@link EnumSerieDossierStatoType }
     *     
     */
    public EnumSerieDossierStatoType getStato() {
        return stato;
    }

    /**
     * Imposta il valore della proprietà stato.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumSerieDossierStatoType }
     *     
     */
    public void setStato(EnumSerieDossierStatoType value) {
        this.stato = value;
    }

}
