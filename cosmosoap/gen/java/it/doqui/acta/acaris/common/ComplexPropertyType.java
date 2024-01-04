
package it.doqui.acta.acaris.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ComplexPropertyType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ComplexPropertyType">
 *   &lt;complexContent>
 *     &lt;extension base="{common.acaris.acta.doqui.it}PropertyType">
 *       &lt;sequence>
 *         &lt;element name="nested" type="{common.acaris.acta.doqui.it}ComplexPropertyType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ComplexPropertyType", propOrder = {
    "nested"
})
public class ComplexPropertyType
    extends PropertyType
{

    @XmlElement(required = true)
    protected ComplexPropertyType nested;

    /**
     * Recupera il valore della proprietà nested.
     * 
     * @return
     *     possible object is
     *     {@link ComplexPropertyType }
     *     
     */
    public ComplexPropertyType getNested() {
        return nested;
    }

    /**
     * Imposta il valore della proprietà nested.
     * 
     * @param value
     *     allowed object is
     *     {@link ComplexPropertyType }
     *     
     */
    public void setNested(ComplexPropertyType value) {
        this.nested = value;
    }

}
