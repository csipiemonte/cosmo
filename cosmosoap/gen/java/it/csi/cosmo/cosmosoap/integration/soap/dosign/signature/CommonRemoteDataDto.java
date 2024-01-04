
package it.csi.cosmo.cosmosoap.integration.soap.dosign.signature;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per commonRemoteDataDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="commonRemoteDataDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/}contentDto">
 *       &lt;sequence>
 *         &lt;element name="authData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "commonRemoteDataDto", propOrder = {
    "authData"
})
public class CommonRemoteDataDto
    extends ContentDto
{

    protected String authData;

    /**
     * Recupera il valore della proprietà authData.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthData() {
        return authData;
    }

    /**
     * Imposta il valore della proprietà authData.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthData(String value) {
        this.authData = value;
    }

}
