
package it.doqui.acta.acaris.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per PropertyType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="PropertyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="queryName" type="{common.acaris.acta.doqui.it}QueryNameType"/>
 *         &lt;element name="value" type="{common.acaris.acta.doqui.it}ValueType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PropertyType", propOrder = {
    "queryName",
    "value"
})
@XmlSeeAlso({
    ComplexPropertyType.class
})
public class PropertyType {

    @XmlElement(required = true)
    protected QueryNameType queryName;
    @XmlElement(required = true)
    protected ValueType value;

    /**
     * Recupera il valore della proprietà queryName.
     * 
     * @return
     *     possible object is
     *     {@link QueryNameType }
     *     
     */
    public QueryNameType getQueryName() {
        return queryName;
    }

    /**
     * Imposta il valore della proprietà queryName.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryNameType }
     *     
     */
    public void setQueryName(QueryNameType value) {
        this.queryName = value;
    }

    /**
     * Recupera il valore della proprietà value.
     * 
     * @return
     *     possible object is
     *     {@link ValueType }
     *     
     */
    public ValueType getValue() {
        return value;
    }

    /**
     * Imposta il valore della proprietà value.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueType }
     *     
     */
    public void setValue(ValueType value) {
        this.value = value;
    }

}
