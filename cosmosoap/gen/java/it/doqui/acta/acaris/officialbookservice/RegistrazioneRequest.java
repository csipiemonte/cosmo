
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per RegistrazioneRequest complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RegistrazioneRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="registroId" type="{common.acaris.acta.doqui.it}ObjectIdType" minOccurs="0"/>
 *         &lt;element name="aooProtocollanteId" type="{common.acaris.acta.doqui.it}ObjectIdType" minOccurs="0"/>
 *         &lt;element name="senzaCreazioneSoggettiEsterni" type="{common.acaris.acta.doqui.it}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistrazioneRequest", propOrder = {
    "registroId",
    "aooProtocollanteId",
    "senzaCreazioneSoggettiEsterni"
})
@XmlSeeAlso({
    Protocollazione.class,
    ProtocollazioneDocumentoEsistente.class,
    ProtocollazioneDaSmistamento.class,
    RegistrazioneRapida.class
})
public abstract class RegistrazioneRequest {

    protected ObjectIdType registroId;
    protected ObjectIdType aooProtocollanteId;
    protected boolean senzaCreazioneSoggettiEsterni;

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
     * Recupera il valore della proprietà aooProtocollanteId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getAooProtocollanteId() {
        return aooProtocollanteId;
    }

    /**
     * Imposta il valore della proprietà aooProtocollanteId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setAooProtocollanteId(ObjectIdType value) {
        this.aooProtocollanteId = value;
    }

    /**
     * Recupera il valore della proprietà senzaCreazioneSoggettiEsterni.
     * 
     */
    public boolean isSenzaCreazioneSoggettiEsterni() {
        return senzaCreazioneSoggettiEsterni;
    }

    /**
     * Imposta il valore della proprietà senzaCreazioneSoggettiEsterni.
     * 
     */
    public void setSenzaCreazioneSoggettiEsterni(boolean value) {
        this.senzaCreazioneSoggettiEsterni = value;
    }

}
