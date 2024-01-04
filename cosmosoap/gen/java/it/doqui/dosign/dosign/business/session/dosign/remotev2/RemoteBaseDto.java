
package it.doqui.dosign.dosign.business.session.dosign.remotev2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per remoteBaseDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="remoteBaseDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://remotev2.dosign.session.business.dosign.dosign.doqui.it/}dosignDto">
 *       &lt;sequence>
 *         &lt;element name="env" type="{http://remotev2.dosign.session.business.dosign.dosign.doqui.it/}Env" minOccurs="0"/>
 *         &lt;element name="provider" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="collocazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "remoteBaseDto", propOrder = {
    "env",
    "provider",
    "codiceFiscale",
    "collocazione"
})
@XmlSeeAlso({
    RemoteTimestampDto.class,
    RemoteAuthDto.class
})
public class RemoteBaseDto
    extends DosignDto
{

    @XmlElement(defaultValue = "BOX")
    protected Env env;
    @XmlElement(required = true)
    protected String provider;
    protected String codiceFiscale;
    protected String collocazione;

    /**
     * Recupera il valore della proprietà env.
     * 
     * @return
     *     possible object is
     *     {@link Env }
     *     
     */
    public Env getEnv() {
        return env;
    }

    /**
     * Imposta il valore della proprietà env.
     * 
     * @param value
     *     allowed object is
     *     {@link Env }
     *     
     */
    public void setEnv(Env value) {
        this.env = value;
    }

    /**
     * Recupera il valore della proprietà provider.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Imposta il valore della proprietà provider.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvider(String value) {
        this.provider = value;
    }

    /**
     * Recupera il valore della proprietà codiceFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Imposta il valore della proprietà codiceFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
    }

    /**
     * Recupera il valore della proprietà collocazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollocazione() {
        return collocazione;
    }

    /**
     * Imposta il valore della proprietà collocazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollocazione(String value) {
        this.collocazione = value;
    }

}
