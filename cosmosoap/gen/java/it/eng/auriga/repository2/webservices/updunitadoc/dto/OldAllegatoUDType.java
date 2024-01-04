
package it.eng.auriga.repository2.webservices.updunitadoc.dto;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per OldAllegatoUDType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="OldAllegatoUDType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NroAllegato" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;choice>
 *           &lt;element name="FlagElimina" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *           &lt;element name="DatiAllegato" type="{}OldAllegatoUDToAggType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OldAllegatoUDType", propOrder = {
    "nroAllegato",
    "flagElimina",
    "datiAllegato"
})
public class OldAllegatoUDType {

    @XmlElement(name = "NroAllegato", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger nroAllegato;
    @XmlElement(name = "FlagElimina")
    protected Object flagElimina;
    @XmlElement(name = "DatiAllegato")
    protected OldAllegatoUDToAggType datiAllegato;

    /**
     * Recupera il valore della proprietà nroAllegato.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNroAllegato() {
        return nroAllegato;
    }

    /**
     * Imposta il valore della proprietà nroAllegato.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNroAllegato(BigInteger value) {
        this.nroAllegato = value;
    }

    /**
     * Recupera il valore della proprietà flagElimina.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFlagElimina() {
        return flagElimina;
    }

    /**
     * Imposta il valore della proprietà flagElimina.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFlagElimina(Object value) {
        this.flagElimina = value;
    }

    /**
     * Recupera il valore della proprietà datiAllegato.
     * 
     * @return
     *     possible object is
     *     {@link OldAllegatoUDToAggType }
     *     
     */
    public OldAllegatoUDToAggType getDatiAllegato() {
        return datiAllegato;
    }

    /**
     * Imposta il valore della proprietà datiAllegato.
     * 
     * @param value
     *     allowed object is
     *     {@link OldAllegatoUDToAggType }
     *     
     */
    public void setDatiAllegato(OldAllegatoUDToAggType value) {
        this.datiAllegato = value;
    }

}
