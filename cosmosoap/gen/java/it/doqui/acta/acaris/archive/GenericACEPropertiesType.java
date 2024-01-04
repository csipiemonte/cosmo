
package it.doqui.acta.acaris.archive;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.PrincipalIdType;


/**
 * <p>Classe Java per GenericACEPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="GenericACEPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}PolicyPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="principalList" type="{common.acaris.acta.doqui.it}PrincipalIdType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GenericACEPropertiesType", propOrder = {
    "principalList"
})
@XmlSeeAlso({
    ActaACEPropertiesType.class
})
public abstract class GenericACEPropertiesType
    extends PolicyPropertiesType
{

    @XmlElement(required = true)
    protected List<PrincipalIdType> principalList;

    /**
     * Gets the value of the principalList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the principalList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrincipalList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PrincipalIdType }
     * 
     * 
     */
    public List<PrincipalIdType> getPrincipalList() {
        if (principalList == null) {
            principalList = new ArrayList<PrincipalIdType>();
        }
        return this.principalList;
    }

}
