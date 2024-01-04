
package it.csi.cosmo.cosmosoap.facade.dto.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per RiferimentoECMType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RiferimentoECMType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EcmUuid" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String400Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RiferimentoECMType", propOrder = {
    "ecmUuid"
})
public class RiferimentoECMType {

    @XmlElement(name = "EcmUuid", required = true)
    protected String ecmUuid;

    /**
     * Recupera il valore della proprietà ecmUuid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEcmUuid() {
        return ecmUuid;
    }

    /**
     * Imposta il valore della proprietà ecmUuid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEcmUuid(String value) {
        this.ecmUuid = value;
    }

}
