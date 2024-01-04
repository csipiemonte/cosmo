
package it.csi.cosmo.cosmosoap.facade.dto.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ConfigurazioneChiamanteType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ConfigurazioneChiamanteType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodiceFiscaleEnte" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/>
 *         &lt;element name="CodiceFruitore" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/>
 *         &lt;element name="CodiceApplicazione" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/>
 *         &lt;element name="CodiceTipoDocumento" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConfigurazioneChiamanteType", propOrder = {
    "codiceFiscaleEnte",
    "codiceFruitore",
    "codiceApplicazione",
    "codiceTipoDocumento"
})
public class ConfigurazioneChiamanteType {

    @XmlElement(name = "CodiceFiscaleEnte", required = true)
    protected String codiceFiscaleEnte;
    @XmlElement(name = "CodiceFruitore", required = true)
    protected String codiceFruitore;
    @XmlElement(name = "CodiceApplicazione", required = true)
    protected String codiceApplicazione;
    @XmlElement(name = "CodiceTipoDocumento", required = true)
    protected String codiceTipoDocumento;

    /**
     * Recupera il valore della proprietà codiceFiscaleEnte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscaleEnte() {
        return codiceFiscaleEnte;
    }

    /**
     * Imposta il valore della proprietà codiceFiscaleEnte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscaleEnte(String value) {
        this.codiceFiscaleEnte = value;
    }

    /**
     * Recupera il valore della proprietà codiceFruitore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFruitore() {
        return codiceFruitore;
    }

    /**
     * Imposta il valore della proprietà codiceFruitore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFruitore(String value) {
        this.codiceFruitore = value;
    }

    /**
     * Recupera il valore della proprietà codiceApplicazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceApplicazione() {
        return codiceApplicazione;
    }

    /**
     * Imposta il valore della proprietà codiceApplicazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceApplicazione(String value) {
        this.codiceApplicazione = value;
    }

    /**
     * Recupera il valore della proprietà codiceTipoDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceTipoDocumento() {
        return codiceTipoDocumento;
    }

    /**
     * Imposta il valore della proprietà codiceTipoDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceTipoDocumento(String value) {
        this.codiceTipoDocumento = value;
    }

}
