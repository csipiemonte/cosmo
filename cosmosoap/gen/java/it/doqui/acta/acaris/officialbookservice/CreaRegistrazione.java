
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.common.PrincipalIdType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="repositoryId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="principalId" type="{common.acaris.acta.doqui.it}PrincipalIdType"/>
 *         &lt;element name="tipologiaCreazione" type="{officialbookservice.acaris.acta.doqui.it}enumTipoRegistrazioneDaCreare"/>
 *         &lt;element name="infoRichiestaCreazione" type="{officialbookservice.acaris.acta.doqui.it}RegistrazioneRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "repositoryId",
    "principalId",
    "tipologiaCreazione",
    "infoRichiestaCreazione"
})
@XmlRootElement(name = "creaRegistrazione")
public class CreaRegistrazione {

    @XmlElement(required = true)
    protected ObjectIdType repositoryId;
    @XmlElement(required = true)
    protected PrincipalIdType principalId;
    @XmlElement(required = true)
    protected EnumTipoRegistrazioneDaCreare tipologiaCreazione;
    @XmlElement(required = true)
    protected RegistrazioneRequest infoRichiestaCreazione;

    /**
     * Recupera il valore della proprietà repositoryId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getRepositoryId() {
        return repositoryId;
    }

    /**
     * Imposta il valore della proprietà repositoryId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setRepositoryId(ObjectIdType value) {
        this.repositoryId = value;
    }

    /**
     * Recupera il valore della proprietà principalId.
     * 
     * @return
     *     possible object is
     *     {@link PrincipalIdType }
     *     
     */
    public PrincipalIdType getPrincipalId() {
        return principalId;
    }

    /**
     * Imposta il valore della proprietà principalId.
     * 
     * @param value
     *     allowed object is
     *     {@link PrincipalIdType }
     *     
     */
    public void setPrincipalId(PrincipalIdType value) {
        this.principalId = value;
    }

    /**
     * Recupera il valore della proprietà tipologiaCreazione.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipoRegistrazioneDaCreare }
     *     
     */
    public EnumTipoRegistrazioneDaCreare getTipologiaCreazione() {
        return tipologiaCreazione;
    }

    /**
     * Imposta il valore della proprietà tipologiaCreazione.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipoRegistrazioneDaCreare }
     *     
     */
    public void setTipologiaCreazione(EnumTipoRegistrazioneDaCreare value) {
        this.tipologiaCreazione = value;
    }

    /**
     * Recupera il valore della proprietà infoRichiestaCreazione.
     * 
     * @return
     *     possible object is
     *     {@link RegistrazioneRequest }
     *     
     */
    public RegistrazioneRequest getInfoRichiestaCreazione() {
        return infoRichiestaCreazione;
    }

    /**
     * Imposta il valore della proprietà infoRichiestaCreazione.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistrazioneRequest }
     *     
     */
    public void setInfoRichiestaCreazione(RegistrazioneRequest value) {
        this.infoRichiestaCreazione = value;
    }

}
