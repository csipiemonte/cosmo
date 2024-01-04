
package it.eng.auriga.repository2.webservices.updunitadoc.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per OldAllegatoUDToAggType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="OldAllegatoUDToAggType">
 *   &lt;complexContent>
 *     &lt;extension base="{}AllegatoUDType">
 *       &lt;sequence>
 *         &lt;element name="AggVersioneElettronica" type="{}AggVersioneElettronicaType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OldAllegatoUDToAggType", propOrder = {
    "aggVersioneElettronica"
})
public class OldAllegatoUDToAggType
    extends AllegatoUDType
{

    @XmlElement(name = "AggVersioneElettronica")
    protected AggVersioneElettronicaType aggVersioneElettronica;

    /**
     * Recupera il valore della proprietà aggVersioneElettronica.
     * 
     * @return
     *     possible object is
     *     {@link AggVersioneElettronicaType }
     *     
     */
    public AggVersioneElettronicaType getAggVersioneElettronica() {
        return aggVersioneElettronica;
    }

    /**
     * Imposta il valore della proprietà aggVersioneElettronica.
     * 
     * @param value
     *     allowed object is
     *     {@link AggVersioneElettronicaType }
     *     
     */
    public void setAggVersioneElettronica(AggVersioneElettronicaType value) {
        this.aggVersioneElettronica = value;
    }

}
