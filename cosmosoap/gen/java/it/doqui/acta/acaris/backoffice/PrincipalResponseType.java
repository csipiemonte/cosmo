
package it.doqui.acta.acaris.backoffice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.IdAOOType;
import it.doqui.acta.acaris.common.IdNodoType;
import it.doqui.acta.acaris.common.IdStrutturaType;
import it.doqui.acta.acaris.common.PrincipalIdType;


/**
 * <p>Classe Java per PrincipalResponseType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="PrincipalResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idPrincipal" type="{common.acaris.acta.doqui.it}PrincipalIdType"/>
 *         &lt;element name="idAOO" type="{common.acaris.acta.doqui.it}IdAOOType"/>
 *         &lt;element name="idStruttura" type="{common.acaris.acta.doqui.it}IdStrutturaType"/>
 *         &lt;element name="idNodo" type="{common.acaris.acta.doqui.it}IdNodoType"/>
 *         &lt;element name="profili" type="{backoffice.acaris.acta.doqui.it}ProfiloPropertiesType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PrincipalResponseType", propOrder = {
    "idPrincipal",
    "idAOO",
    "idStruttura",
    "idNodo",
    "profili"
})
public class PrincipalResponseType {

    @XmlElement(required = true)
    protected PrincipalIdType idPrincipal;
    @XmlElement(required = true)
    protected IdAOOType idAOO;
    @XmlElement(required = true)
    protected IdStrutturaType idStruttura;
    @XmlElement(required = true)
    protected IdNodoType idNodo;
    @XmlElement(required = true)
    protected List<ProfiloPropertiesType> profili;

    /**
     * Recupera il valore della proprietà idPrincipal.
     * 
     * @return
     *     possible object is
     *     {@link PrincipalIdType }
     *     
     */
    public PrincipalIdType getIdPrincipal() {
        return idPrincipal;
    }

    /**
     * Imposta il valore della proprietà idPrincipal.
     * 
     * @param value
     *     allowed object is
     *     {@link PrincipalIdType }
     *     
     */
    public void setIdPrincipal(PrincipalIdType value) {
        this.idPrincipal = value;
    }

    /**
     * Recupera il valore della proprietà idAOO.
     * 
     * @return
     *     possible object is
     *     {@link IdAOOType }
     *     
     */
    public IdAOOType getIdAOO() {
        return idAOO;
    }

    /**
     * Imposta il valore della proprietà idAOO.
     * 
     * @param value
     *     allowed object is
     *     {@link IdAOOType }
     *     
     */
    public void setIdAOO(IdAOOType value) {
        this.idAOO = value;
    }

    /**
     * Recupera il valore della proprietà idStruttura.
     * 
     * @return
     *     possible object is
     *     {@link IdStrutturaType }
     *     
     */
    public IdStrutturaType getIdStruttura() {
        return idStruttura;
    }

    /**
     * Imposta il valore della proprietà idStruttura.
     * 
     * @param value
     *     allowed object is
     *     {@link IdStrutturaType }
     *     
     */
    public void setIdStruttura(IdStrutturaType value) {
        this.idStruttura = value;
    }

    /**
     * Recupera il valore della proprietà idNodo.
     * 
     * @return
     *     possible object is
     *     {@link IdNodoType }
     *     
     */
    public IdNodoType getIdNodo() {
        return idNodo;
    }

    /**
     * Imposta il valore della proprietà idNodo.
     * 
     * @param value
     *     allowed object is
     *     {@link IdNodoType }
     *     
     */
    public void setIdNodo(IdNodoType value) {
        this.idNodo = value;
    }

    /**
     * Gets the value of the profili property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the profili property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProfili().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProfiloPropertiesType }
     * 
     * 
     */
    public List<ProfiloPropertiesType> getProfili() {
        if (profili == null) {
            profili = new ArrayList<ProfiloPropertiesType>();
        }
        return this.profili;
    }

}
