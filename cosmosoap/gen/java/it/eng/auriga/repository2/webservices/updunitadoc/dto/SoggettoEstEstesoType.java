
package it.eng.auriga.repository2.webservices.updunitadoc.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Soggetto esterno che ha una relazione con un
 * 				documento, fascicolo, proced. ecc. diversa da quella di
 * 				mittente/destinatario
 * 
 * <p>Classe Java per SoggettoEstEstesoType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="SoggettoEstEstesoType">
 *   &lt;complexContent>
 *     &lt;extension base="{}SoggettoEsternoType">
 *       &lt;sequence>
 *         &lt;element name="DettNaturaRelazioneConUD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoggettoEstEstesoType", propOrder = {
    "dettNaturaRelazioneConUD"
})
public class SoggettoEstEstesoType
    extends SoggettoEsternoType
{

    @XmlElement(name = "DettNaturaRelazioneConUD")
    protected String dettNaturaRelazioneConUD;

    /**
     * Recupera il valore della proprietà dettNaturaRelazioneConUD.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDettNaturaRelazioneConUD() {
        return dettNaturaRelazioneConUD;
    }

    /**
     * Imposta il valore della proprietà dettNaturaRelazioneConUD.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDettNaturaRelazioneConUD(String value) {
        this.dettNaturaRelazioneConUD = value;
    }

}
