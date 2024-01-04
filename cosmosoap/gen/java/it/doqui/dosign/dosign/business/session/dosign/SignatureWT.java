
package it.doqui.dosign.dosign.business.session.dosign;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per signatureWT complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="signatureWT">
 *   &lt;complexContent>
 *     &lt;extension base="{http://dosign.session.business.dosign.dosign.doqui.it/}signature">
 *       &lt;sequence>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signatureWT", propOrder = {
    "type"
})
public class SignatureWT
    extends Signature
{

    protected long type;

    /**
     * Recupera il valore della proprietà type.
     * 
     */
    public long getType() {
        return type;
    }

    /**
     * Imposta il valore della proprietà type.
     * 
     */
    public void setType(long value) {
        this.type = value;
    }

}
