
package it.csi.cosmo.cosmosoap.facade.dto.common;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per MetadatiType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MetadatiType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Metadato" type="{http://www.csi.it/stardas/services/StardasCommonTypes}MetadatoType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetadatiType", propOrder = {
    "metadato"
})
public class MetadatiType {

    @XmlElement(name = "Metadato", required = true)
    protected List<MetadatoType> metadato;

    /**
     * Gets the value of the metadato property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metadato property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetadato().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MetadatoType }
     * 
     * 
     */
    public List<MetadatoType> getMetadato() {
        if (metadato == null) {
            metadato = new ArrayList<MetadatoType>();
        }
        return this.metadato;
    }

}
