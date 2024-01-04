
package it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.csi.cosmo.cosmosoap.facade.dto.common.ConfigurazioneChiamanteType;
import it.csi.cosmo.cosmosoap.facade.dto.common.DatiSmistaDocumentoType;


/**
 * <p>Classe Java per SmistaDocumentoRequestType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="SmistaDocumentoRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ConfigurazioneChiamante" type="{http://www.csi.it/stardas/services/StardasCommonTypes}ConfigurazioneChiamanteType"/>
 *         &lt;element name="DatiSmistaDocumento" type="{http://www.csi.it/stardas/services/StardasCommonTypes}DatiSmistaDocumentoType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SmistaDocumentoRequestType", propOrder = {
    "configurazioneChiamante",
    "datiSmistaDocumento"
})
public class SmistaDocumentoRequestType {

    @XmlElement(name = "ConfigurazioneChiamante", required = true)
    protected ConfigurazioneChiamanteType configurazioneChiamante;
    @XmlElement(name = "DatiSmistaDocumento", required = true)
    protected DatiSmistaDocumentoType datiSmistaDocumento;

    /**
     * Recupera il valore della proprietà configurazioneChiamante.
     * 
     * @return
     *     possible object is
     *     {@link ConfigurazioneChiamanteType }
     *     
     */
    public ConfigurazioneChiamanteType getConfigurazioneChiamante() {
        return configurazioneChiamante;
    }

    /**
     * Imposta il valore della proprietà configurazioneChiamante.
     * 
     * @param value
     *     allowed object is
     *     {@link ConfigurazioneChiamanteType }
     *     
     */
    public void setConfigurazioneChiamante(ConfigurazioneChiamanteType value) {
        this.configurazioneChiamante = value;
    }

    /**
     * Recupera il valore della proprietà datiSmistaDocumento.
     * 
     * @return
     *     possible object is
     *     {@link DatiSmistaDocumentoType }
     *     
     */
    public DatiSmistaDocumentoType getDatiSmistaDocumento() {
        return datiSmistaDocumento;
    }

    /**
     * Imposta il valore della proprietà datiSmistaDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiSmistaDocumentoType }
     *     
     */
    public void setDatiSmistaDocumento(DatiSmistaDocumentoType value) {
        this.datiSmistaDocumento = value;
    }

}
