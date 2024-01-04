
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.CommonPropertiesType;


/**
 * <p>Classe Java per OfficialBookPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="OfficialBookPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{common.acaris.acta.doqui.it}CommonPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="objectTypeId" type="{officialbookservice.acaris.acta.doqui.it}enumOfficialBookObjectType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OfficialBookPropertiesType", propOrder = {
    "objectTypeId"
})
@XmlSeeAlso({
    CorrispondenteMessaggioPropertiesType.class,
    MessaggioPropertiesType.class,
    RegistroProtocolloPropertiesType.class,
    CorrispondentePropertiesType.class,
    AnnotazioneOBPropertiesType.class,
    RegistrazionePropertiesType.class,
    LogProtocolloPropertiesType.class
})
public abstract class OfficialBookPropertiesType
    extends CommonPropertiesType
{

    @XmlElement(required = true)
    protected EnumOfficialBookObjectType objectTypeId;

    /**
     * Recupera il valore della proprietà objectTypeId.
     * 
     * @return
     *     possible object is
     *     {@link EnumOfficialBookObjectType }
     *     
     */
    public EnumOfficialBookObjectType getObjectTypeId() {
        return objectTypeId;
    }

    /**
     * Imposta il valore della proprietà objectTypeId.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumOfficialBookObjectType }
     *     
     */
    public void setObjectTypeId(EnumOfficialBookObjectType value) {
        this.objectTypeId = value;
    }

}
