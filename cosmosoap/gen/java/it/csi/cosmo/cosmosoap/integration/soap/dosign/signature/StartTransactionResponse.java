
package it.csi.cosmo.cosmosoap.integration.soap.dosign.signature;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per startTransactionResponse complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="startTransactionResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/}commonRemoteDataDto" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "startTransactionResponse", propOrder = {
    "_return"
})
public class StartTransactionResponse {

    @XmlElement(name = "return")
    protected CommonRemoteDataDto _return;

    /**
     * Recupera il valore della proprietà return.
     * 
     * @return
     *     possible object is
     *     {@link CommonRemoteDataDto }
     *     
     */
    public CommonRemoteDataDto getReturn() {
        return _return;
    }

    /**
     * Imposta il valore della proprietà return.
     * 
     * @param value
     *     allowed object is
     *     {@link CommonRemoteDataDto }
     *     
     */
    public void setReturn(CommonRemoteDataDto value) {
        this._return = value;
    }

}
