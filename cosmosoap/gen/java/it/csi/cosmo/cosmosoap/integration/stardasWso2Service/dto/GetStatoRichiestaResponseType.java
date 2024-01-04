
package it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.csi.cosmo.cosmosoap.facade.dto.common.ResponseType;
import it.csi.cosmo.cosmosoap.facade.dto.common.StatoRichiestaType;


/**
 * <p>Classe Java per GetStatoRichiestaResponseType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="GetStatoRichiestaResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.csi.it/stardas/services/StardasCommonTypes}ResponseType">
 *       &lt;sequence>
 *         &lt;element name="StatoRichiesta" type="{http://www.csi.it/stardas/services/StardasCommonTypes}StatoRichiestaType" minOccurs="0"/>
 *         &lt;element name="CodiceEsitoRichiesta" type="{http://www.csi.it/stardas/services/StardasCommonTypes}CodiceEsitoType" minOccurs="0"/>
 *         &lt;element name="DettaglioEsitoRichiesta" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String400Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetStatoRichiestaResponseType", propOrder = {
    "statoRichiesta",
    "codiceEsitoRichiesta",
    "dettaglioEsitoRichiesta"
})
public class GetStatoRichiestaResponseType
    extends ResponseType
{

    @XmlElement(name = "StatoRichiesta")
    protected StatoRichiestaType statoRichiesta;
    @XmlElement(name = "CodiceEsitoRichiesta")
    protected String codiceEsitoRichiesta;
    @XmlElement(name = "DettaglioEsitoRichiesta")
    protected String dettaglioEsitoRichiesta;

    /**
     * Recupera il valore della proprietà statoRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link StatoRichiestaType }
     *     
     */
    public StatoRichiestaType getStatoRichiesta() {
        return statoRichiesta;
    }

    /**
     * Imposta il valore della proprietà statoRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link StatoRichiestaType }
     *     
     */
    public void setStatoRichiesta(StatoRichiestaType value) {
        this.statoRichiesta = value;
    }

    /**
     * Recupera il valore della proprietà codiceEsitoRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceEsitoRichiesta() {
        return codiceEsitoRichiesta;
    }

    /**
     * Imposta il valore della proprietà codiceEsitoRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceEsitoRichiesta(String value) {
        this.codiceEsitoRichiesta = value;
    }

    /**
     * Recupera il valore della proprietà dettaglioEsitoRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDettaglioEsitoRichiesta() {
        return dettaglioEsitoRichiesta;
    }

    /**
     * Imposta il valore della proprietà dettaglioEsitoRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDettaglioEsitoRichiesta(String value) {
        this.dettaglioEsitoRichiesta = value;
    }

}
