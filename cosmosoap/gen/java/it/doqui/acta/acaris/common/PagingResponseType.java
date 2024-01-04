
package it.doqui.acta.acaris.common;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per PagingResponseType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="PagingResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objects" type="{common.acaris.acta.doqui.it}ObjectResponseType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="hasMoreItems" type="{common.acaris.acta.doqui.it}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PagingResponseType", propOrder = {
    "objects",
    "hasMoreItems"
})
public class PagingResponseType {

    protected List<ObjectResponseType> objects;
    protected boolean hasMoreItems;

    /**
     * Gets the value of the objects property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objects property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjects().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectResponseType }
     * 
     * 
     */
    public List<ObjectResponseType> getObjects() {
        if (objects == null) {
            objects = new ArrayList<ObjectResponseType>();
        }
        return this.objects;
    }

    /**
     * Recupera il valore della proprietà hasMoreItems.
     * 
     */
    public boolean isHasMoreItems() {
        return hasMoreItems;
    }

    /**
     * Imposta il valore della proprietà hasMoreItems.
     * 
     */
    public void setHasMoreItems(boolean value) {
        this.hasMoreItems = value;
    }

}
