
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
 *         &lt;element name="classificazioneId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="registroId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="infoRegistrazione" type="{officialbookservice.acaris.acta.doqui.it}InfoRegistrazione"/>
 *         &lt;element name="senzaCreazioneSoggetti" type="{common.acaris.acta.doqui.it}boolean"/>
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
    "classificazioneId",
    "registroId",
    "infoRegistrazione",
    "senzaCreazioneSoggetti"
})
@XmlRootElement(name = "importaRegistrazione")
public class ImportaRegistrazione {

    @XmlElement(required = true)
    protected ObjectIdType repositoryId;
    @XmlElement(required = true)
    protected PrincipalIdType principalId;
    @XmlElement(required = true)
    protected ObjectIdType classificazioneId;
    @XmlElement(required = true)
    protected ObjectIdType registroId;
    @XmlElement(required = true)
    protected InfoRegistrazione infoRegistrazione;
    protected boolean senzaCreazioneSoggetti;

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
     * Recupera il valore della proprietà classificazioneId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getClassificazioneId() {
        return classificazioneId;
    }

    /**
     * Imposta il valore della proprietà classificazioneId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setClassificazioneId(ObjectIdType value) {
        this.classificazioneId = value;
    }

    /**
     * Recupera il valore della proprietà registroId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getRegistroId() {
        return registroId;
    }

    /**
     * Imposta il valore della proprietà registroId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setRegistroId(ObjectIdType value) {
        this.registroId = value;
    }

    /**
     * Recupera il valore della proprietà infoRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link InfoRegistrazione }
     *     
     */
    public InfoRegistrazione getInfoRegistrazione() {
        return infoRegistrazione;
    }

    /**
     * Imposta il valore della proprietà infoRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoRegistrazione }
     *     
     */
    public void setInfoRegistrazione(InfoRegistrazione value) {
        this.infoRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà senzaCreazioneSoggetti.
     * 
     */
    public boolean isSenzaCreazioneSoggetti() {
        return senzaCreazioneSoggetti;
    }

    /**
     * Imposta il valore della proprietà senzaCreazioneSoggetti.
     * 
     */
    public void setSenzaCreazioneSoggetti(boolean value) {
        this.senzaCreazioneSoggetti = value;
    }

}
