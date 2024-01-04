
package it.doqui.acta.acaris.archive;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.AcarisContentStreamType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contentStream" type="{common.acaris.acta.doqui.it}acarisContentStreamType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "contentStream"
})
@XmlRootElement(name = "getContentStreamResponse")
public class GetContentStreamResponse {

    @XmlElement(required = true)
    protected List<AcarisContentStreamType> contentStream;

    /**
     * Gets the value of the contentStream property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contentStream property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContentStream().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AcarisContentStreamType }
     * 
     * 
     */
    public List<AcarisContentStreamType> getContentStream() {
        if (contentStream == null) {
            contentStream = new ArrayList<AcarisContentStreamType>();
        }
        return this.contentStream;
    }

}
