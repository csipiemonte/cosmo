
package it.doqui.acta.acaris.documentservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per DocumentoFisicoIdMap complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DocumentoFisicoIdMap">
 *   &lt;complexContent>
 *     &lt;extension base="{documentservice.acaris.acta.doqui.it}MappaIdentificazioneDocumento">
 *       &lt;sequence>
 *         &lt;element name="documentoFisicoId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="documentoArchivisticoId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="ContenutiFisiciIdMap" type="{documentservice.acaris.acta.doqui.it}ContenutoFisicoIdMap" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentoFisicoIdMap", propOrder = {
    "documentoFisicoId",
    "documentoArchivisticoId",
    "contenutiFisiciIdMap"
})
public class DocumentoFisicoIdMap
    extends MappaIdentificazioneDocumento
{

    @XmlElement(required = true)
    protected ObjectIdType documentoFisicoId;
    @XmlElement(required = true)
    protected ObjectIdType documentoArchivisticoId;
    @XmlElement(name = "ContenutiFisiciIdMap", required = true)
    protected List<ContenutoFisicoIdMap> contenutiFisiciIdMap;

    /**
     * Recupera il valore della proprietà documentoFisicoId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getDocumentoFisicoId() {
        return documentoFisicoId;
    }

    /**
     * Imposta il valore della proprietà documentoFisicoId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setDocumentoFisicoId(ObjectIdType value) {
        this.documentoFisicoId = value;
    }

    /**
     * Recupera il valore della proprietà documentoArchivisticoId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getDocumentoArchivisticoId() {
        return documentoArchivisticoId;
    }

    /**
     * Imposta il valore della proprietà documentoArchivisticoId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setDocumentoArchivisticoId(ObjectIdType value) {
        this.documentoArchivisticoId = value;
    }

    /**
     * Gets the value of the contenutiFisiciIdMap property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contenutiFisiciIdMap property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContenutiFisiciIdMap().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContenutoFisicoIdMap }
     * 
     * 
     */
    public List<ContenutoFisicoIdMap> getContenutiFisiciIdMap() {
        if (contenutiFisiciIdMap == null) {
            contenutiFisiciIdMap = new ArrayList<ContenutoFisicoIdMap>();
        }
        return this.contenutiFisiciIdMap;
    }

}
