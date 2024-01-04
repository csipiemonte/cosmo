
package it.doqui.acta.acaris.document;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.common.PrincipalIdType;
import it.doqui.acta.acaris.documentservice.IdentificatoreDocumento;


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
 *         &lt;element name="puntoDiPartenza" type="{documentservice.acaris.acta.doqui.it}IdentificatoreDocumento"/>
 *         &lt;element name="recuperaAscendenza" type="{common.acaris.acta.doqui.it}boolean"/>
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
    "puntoDiPartenza",
    "recuperaAscendenza"
})
@XmlRootElement(name = "getRappresentazioneDocumento")
public class GetRappresentazioneDocumento {

    @XmlElement(required = true)
    protected ObjectIdType repositoryId;
    @XmlElement(required = true)
    protected PrincipalIdType principalId;
    @XmlElement(required = true)
    protected IdentificatoreDocumento puntoDiPartenza;
    protected boolean recuperaAscendenza;

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
     * Recupera il valore della proprietà puntoDiPartenza.
     * 
     * @return
     *     possible object is
     *     {@link IdentificatoreDocumento }
     *     
     */
    public IdentificatoreDocumento getPuntoDiPartenza() {
        return puntoDiPartenza;
    }

    /**
     * Imposta il valore della proprietà puntoDiPartenza.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificatoreDocumento }
     *     
     */
    public void setPuntoDiPartenza(IdentificatoreDocumento value) {
        this.puntoDiPartenza = value;
    }

    /**
     * Recupera il valore della proprietà recuperaAscendenza.
     * 
     */
    public boolean isRecuperaAscendenza() {
        return recuperaAscendenza;
    }

    /**
     * Imposta il valore della proprietà recuperaAscendenza.
     * 
     */
    public void setRecuperaAscendenza(boolean value) {
        this.recuperaAscendenza = value;
    }

}
