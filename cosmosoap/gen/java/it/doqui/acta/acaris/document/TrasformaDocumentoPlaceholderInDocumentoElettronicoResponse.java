
package it.doqui.acta.acaris.document;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.documentservice.IdentificazioneTrasformazione;


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
 *         &lt;element name="trasformazione" type="{documentservice.acaris.acta.doqui.it}IdentificazioneTrasformazione" maxOccurs="unbounded" minOccurs="0"/>
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
    "trasformazione"
})
@XmlRootElement(name = "trasformaDocumentoPlaceholderInDocumentoElettronicoResponse")
public class TrasformaDocumentoPlaceholderInDocumentoElettronicoResponse {

    protected List<IdentificazioneTrasformazione> trasformazione;

    /**
     * Gets the value of the trasformazione property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trasformazione property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrasformazione().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IdentificazioneTrasformazione }
     * 
     * 
     */
    public List<IdentificazioneTrasformazione> getTrasformazione() {
        if (trasformazione == null) {
            trasformazione = new ArrayList<IdentificazioneTrasformazione>();
        }
        return this.trasformazione;
    }

}
