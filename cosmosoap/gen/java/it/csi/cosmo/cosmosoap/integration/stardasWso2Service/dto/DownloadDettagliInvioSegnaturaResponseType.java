
package it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.csi.cosmo.cosmosoap.facade.dto.common.EmbeddedBinaryType;
import it.csi.cosmo.cosmosoap.facade.dto.common.ResponseType;


/**
 * <p>Classe Java per DownloadDettagliInvioSegnaturaResponseType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DownloadDettagliInvioSegnaturaResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.csi.it/stardas/services/StardasCommonTypes}ResponseType">
 *       &lt;sequence>
 *         &lt;element name="ContenutoBinario" type="{http://www.csi.it/stardas/services/StardasCommonTypes}EmbeddedBinaryType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DownloadDettagliInvioSegnaturaResponseType", propOrder = {
    "contenutoBinario"
})
public class DownloadDettagliInvioSegnaturaResponseType
    extends ResponseType
{

    @XmlElement(name = "ContenutoBinario", required = true)
    protected EmbeddedBinaryType contenutoBinario;

    /**
     * Recupera il valore della proprietà contenutoBinario.
     * 
     * @return
     *     possible object is
     *     {@link EmbeddedBinaryType }
     *     
     */
    public EmbeddedBinaryType getContenutoBinario() {
        return contenutoBinario;
    }

    /**
     * Imposta il valore della proprietà contenutoBinario.
     * 
     * @param value
     *     allowed object is
     *     {@link EmbeddedBinaryType }
     *     
     */
    public void setContenutoBinario(EmbeddedBinaryType value) {
        this.contenutoBinario = value;
    }

}
