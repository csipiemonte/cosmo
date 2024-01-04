
package it.doqui.acta.acaris.common;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ObjectMetadataType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ObjectMetadataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="queryName" type="{common.acaris.acta.doqui.it}QueryNameType"/>
 *         &lt;element name="selectable" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="queryable" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="operators" type="{common.acaris.acta.doqui.it}string" maxOccurs="unbounded"/>
 *         &lt;element name="dataType" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="updatable" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="propertyFilterConfigurationInfo" type="{common.acaris.acta.doqui.it}PropertyFilterConfigurationInfoType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectMetadataType", propOrder = {
    "queryName",
    "selectable",
    "queryable",
    "operators",
    "dataType",
    "updatable",
    "propertyFilterConfigurationInfo"
})
public class ObjectMetadataType {

    @XmlElement(required = true)
    protected QueryNameType queryName;
    protected boolean selectable;
    protected boolean queryable;
    @XmlElement(required = true)
    protected List<String> operators;
    @XmlElement(required = true)
    protected String dataType;
    protected boolean updatable;
    protected List<PropertyFilterConfigurationInfoType> propertyFilterConfigurationInfo;

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
     * Recupera il valore della proprietà selectable.
     * 
     */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * Imposta il valore della proprietà selectable.
     * 
     */
    public void setSelectable(boolean value) {
        this.selectable = value;
    }

    /**
     * Recupera il valore della proprietà queryable.
     * 
     */
    public boolean isQueryable() {
        return queryable;
    }

    /**
     * Imposta il valore della proprietà queryable.
     * 
     */
    public void setQueryable(boolean value) {
        this.queryable = value;
    }

    /**
     * Gets the value of the operators property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operators property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperators().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOperators() {
        if (operators == null) {
            operators = new ArrayList<String>();
        }
        return this.operators;
    }

    /**
     * Recupera il valore della proprietà dataType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Imposta il valore della proprietà dataType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataType(String value) {
        this.dataType = value;
    }

    /**
     * Recupera il valore della proprietà updatable.
     * 
     */
    public boolean isUpdatable() {
        return updatable;
    }

    /**
     * Imposta il valore della proprietà updatable.
     * 
     */
    public void setUpdatable(boolean value) {
        this.updatable = value;
    }

    /**
     * Gets the value of the propertyFilterConfigurationInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propertyFilterConfigurationInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertyFilterConfigurationInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyFilterConfigurationInfoType }
     * 
     * 
     */
    public List<PropertyFilterConfigurationInfoType> getPropertyFilterConfigurationInfo() {
        if (propertyFilterConfigurationInfo == null) {
            propertyFilterConfigurationInfo = new ArrayList<PropertyFilterConfigurationInfoType>();
        }
        return this.propertyFilterConfigurationInfo;
    }

}
