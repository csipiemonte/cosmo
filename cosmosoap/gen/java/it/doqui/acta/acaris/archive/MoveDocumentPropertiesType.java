
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.IdSmistamentoType;
import it.doqui.acta.acaris.common.PropertiesType;


/**
 * <p>Classe Java per MoveDocumentPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MoveDocumentPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{common.acaris.acta.doqui.it}PropertiesType">
 *       &lt;sequence>
 *         &lt;element name="offlineMoveRequest" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="idSmistamentoType" type="{common.acaris.acta.doqui.it}IdSmistamentoType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MoveDocumentPropertiesType", propOrder = {
    "offlineMoveRequest",
    "idSmistamentoType"
})
public class MoveDocumentPropertiesType
    extends PropertiesType
{

    protected boolean offlineMoveRequest;
    protected IdSmistamentoType idSmistamentoType;

    /**
     * Recupera il valore della proprietà offlineMoveRequest.
     * 
     */
    public boolean isOfflineMoveRequest() {
        return offlineMoveRequest;
    }

    /**
     * Imposta il valore della proprietà offlineMoveRequest.
     * 
     */
    public void setOfflineMoveRequest(boolean value) {
        this.offlineMoveRequest = value;
    }

    /**
     * Recupera il valore della proprietà idSmistamentoType.
     * 
     * @return
     *     possible object is
     *     {@link IdSmistamentoType }
     *     
     */
    public IdSmistamentoType getIdSmistamentoType() {
        return idSmistamentoType;
    }

    /**
     * Imposta il valore della proprietà idSmistamentoType.
     * 
     * @param value
     *     allowed object is
     *     {@link IdSmistamentoType }
     *     
     */
    public void setIdSmistamentoType(IdSmistamentoType value) {
        this.idSmistamentoType = value;
    }

}
