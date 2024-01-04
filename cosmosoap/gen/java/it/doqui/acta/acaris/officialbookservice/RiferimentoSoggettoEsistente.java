
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.common.prt.EnumPFPGUL;


/**
 * <p>Classe Java per RiferimentoSoggettoEsistente complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RiferimentoSoggettoEsistente">
 *   &lt;complexContent>
 *     &lt;extension base="{officialbookservice.acaris.acta.doqui.it}InfoSoggettoAssociato">
 *       &lt;sequence>
 *         &lt;element name="soggettoId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="nodoIdSeTipologiaUtente" type="{common.acaris.acta.doqui.it}ObjectIdType" minOccurs="0"/>
 *         &lt;element name="tipologia" type="{officialbookservice.acaris.acta.doqui.it}enumTipologiaSoggettoAssociato"/>
 *         &lt;element name="idPFPGUL" type="{prt.common.acaris.acta.doqui.it}enumPFPGUL"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RiferimentoSoggettoEsistente", propOrder = {
    "soggettoId",
    "nodoIdSeTipologiaUtente",
    "tipologia",
    "idPFPGUL"
})
public class RiferimentoSoggettoEsistente
    extends InfoSoggettoAssociato
{

    @XmlElement(required = true)
    protected ObjectIdType soggettoId;
    protected ObjectIdType nodoIdSeTipologiaUtente;
    @XmlElement(required = true)
    protected EnumTipologiaSoggettoAssociato tipologia;
    @XmlElement(required = true)
    protected EnumPFPGUL idPFPGUL;

    /**
     * Recupera il valore della proprietà soggettoId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getSoggettoId() {
        return soggettoId;
    }

    /**
     * Imposta il valore della proprietà soggettoId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setSoggettoId(ObjectIdType value) {
        this.soggettoId = value;
    }

    /**
     * Recupera il valore della proprietà nodoIdSeTipologiaUtente.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getNodoIdSeTipologiaUtente() {
        return nodoIdSeTipologiaUtente;
    }

    /**
     * Imposta il valore della proprietà nodoIdSeTipologiaUtente.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setNodoIdSeTipologiaUtente(ObjectIdType value) {
        this.nodoIdSeTipologiaUtente = value;
    }

    /**
     * Recupera il valore della proprietà tipologia.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipologiaSoggettoAssociato }
     *     
     */
    public EnumTipologiaSoggettoAssociato getTipologia() {
        return tipologia;
    }

    /**
     * Imposta il valore della proprietà tipologia.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipologiaSoggettoAssociato }
     *     
     */
    public void setTipologia(EnumTipologiaSoggettoAssociato value) {
        this.tipologia = value;
    }

    /**
     * Recupera il valore della proprietà idPFPGUL.
     * 
     * @return
     *     possible object is
     *     {@link EnumPFPGUL }
     *     
     */
    public EnumPFPGUL getIdPFPGUL() {
        return idPFPGUL;
    }

    /**
     * Imposta il valore della proprietà idPFPGUL.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumPFPGUL }
     *     
     */
    public void setIdPFPGUL(EnumPFPGUL value) {
        this.idPFPGUL = value;
    }

}
