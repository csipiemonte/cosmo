
package it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.csi.cosmo.cosmosoap.facade.dto.common.ConfigurazioneChiamanteType;


/**
 * <p>Classe Java per GetDettaglioRichiestaRequestType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="GetDettaglioRichiestaRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ConfigurazioneChiamante" type="{http://www.csi.it/stardas/services/StardasCommonTypes}ConfigurazioneChiamanteType"/>
 *         &lt;element name="IdDocFruitore" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String200Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDettaglioRichiestaRequestType", propOrder = {
    "configurazioneChiamante",
    "idDocFruitore"
})
public class GetDettaglioRichiestaRequestType {

    @XmlElement(name = "ConfigurazioneChiamante", required = true)
    protected ConfigurazioneChiamanteType configurazioneChiamante;
    @XmlElement(name = "IdDocFruitore", required = true)
    protected String idDocFruitore;

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
     * Recupera il valore della proprietà idDocFruitore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocFruitore() {
        return idDocFruitore;
    }

    /**
     * Imposta il valore della proprietà idDocFruitore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocFruitore(String value) {
        this.idDocFruitore = value;
    }

}
