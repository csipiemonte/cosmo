
package it.eng.auriga.repository2.webservices.updunitadoc.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per NuovoAllegatoUDType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="NuovoAllegatoUDType">
 *   &lt;complexContent>
 *     &lt;extension base="{}AllegatoUDType">
 *       &lt;sequence>
 *         &lt;element name="VersioneElettronica" type="{}VersioneElettronicaType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NuovoAllegatoUDType", propOrder = {
    "versioneElettronica"
})
public class NuovoAllegatoUDType
    extends AllegatoUDType
{

    @XmlElement(name = "VersioneElettronica")
    protected VersioneElettronicaType versioneElettronica;

    /**
     * Recupera il valore della proprietà versioneElettronica.
     * 
     * @return
     *     possible object is
     *     {@link VersioneElettronicaType }
     *     
     */
    public VersioneElettronicaType getVersioneElettronica() {
        return versioneElettronica;
    }

    /**
     * Imposta il valore della proprietà versioneElettronica.
     * 
     * @param value
     *     allowed object is
     *     {@link VersioneElettronicaType }
     *     
     */
    public void setVersioneElettronica(VersioneElettronicaType value) {
        this.versioneElettronica = value;
    }

}
