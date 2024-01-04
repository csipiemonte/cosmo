
package it.doqui.dosign.dosign.business.session.dosign.remotev2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per remoteOtpDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="remoteOtpDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://remotev2.dosign.session.business.dosign.dosign.doqui.it/}remoteAuthDto">
 *       &lt;sequence>
 *         &lt;element name="uses" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "remoteOtpDto", propOrder = {
    "uses"
})
public class RemoteOtpDto
    extends RemoteAuthDto
{

    protected int uses;

    /**
     * Recupera il valore della proprietà uses.
     * 
     */
    public int getUses() {
        return uses;
    }

    /**
     * Imposta il valore della proprietà uses.
     * 
     */
    public void setUses(int value) {
        this.uses = value;
    }

}
