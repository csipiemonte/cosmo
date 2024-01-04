
package it.doqui.acta.acaris.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per QueryConditionType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="QueryConditionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="propertyName" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="operator" type="{common.acaris.acta.doqui.it}enumQueryOperator"/>
 *         &lt;element name="value" type="{common.acaris.acta.doqui.it}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QueryConditionType", propOrder = {
    "propertyName",
    "operator",
    "value"
})
public class QueryConditionType {

    @XmlElement(required = true)
    protected String propertyName;
    @XmlElement(required = true)
    protected EnumQueryOperator operator;
    @XmlElement(required = true)
    protected String value;

    /**
     * Recupera il valore della proprietà propertyName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Imposta il valore della proprietà propertyName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyName(String value) {
        this.propertyName = value;
    }

    /**
     * Recupera il valore della proprietà operator.
     * 
     * @return
     *     possible object is
     *     {@link EnumQueryOperator }
     *     
     */
    public EnumQueryOperator getOperator() {
        return operator;
    }

    /**
     * Imposta il valore della proprietà operator.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumQueryOperator }
     *     
     */
    public void setOperator(EnumQueryOperator value) {
        this.operator = value;
    }

    /**
     * Recupera il valore della proprietà value.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Imposta il valore della proprietà value.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

}
