
package it.csi.cosmo.cosmosoap.facade.dto.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto.DownloadDettagliInvioSegnaturaResponseType;
import it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto.GetDettaglioRichiestaResponseType;
import it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto.GetStatoRichiestaResponseType;
import it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto.SmistaDocumentoResponseType;


/**
 * <p>Classe Java per ResponseType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Result" type="{http://www.csi.it/stardas/services/StardasCommonTypes}ResultType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseType", propOrder = {
    "result"
})
@XmlSeeAlso({
    GetStatoRichiestaResponseType.class,
    DownloadDettagliInvioSegnaturaResponseType.class,
    SmistaDocumentoResponseType.class,
    GetDettaglioRichiestaResponseType.class
})
public class ResponseType {

    @XmlElement(name = "Result", required = true)
    protected ResultType result;

    /**
     * Recupera il valore della proprietà result.
     * 
     * @return
     *     possible object is
     *     {@link ResultType }
     *     
     */
    public ResultType getResult() {
        return result;
    }

    /**
     * Imposta il valore della proprietà result.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultType }
     *     
     */
    public void setResult(ResultType value) {
        this.result = value;
    }

}
