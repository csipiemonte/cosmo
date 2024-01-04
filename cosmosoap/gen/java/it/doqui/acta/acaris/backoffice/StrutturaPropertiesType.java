
package it.doqui.acta.acaris.backoffice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per StrutturaPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="StrutturaPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{backoffice.acaris.acta.doqui.it}OrganizationUnitPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="tipo" type="{backoffice.acaris.acta.doqui.it}enumTipoStrutturaType"/>
 *         &lt;element name="livello" type="{backoffice.acaris.acta.doqui.it}LivelloType"/>
 *         &lt;element name="descrizioneLivello" type="{backoffice.acaris.acta.doqui.it}DescrizioneLivelloType"/>
 *         &lt;element name="idAOO" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StrutturaPropertiesType", propOrder = {
    "tipo",
    "livello",
    "descrizioneLivello",
    "idAOO"
})
public class StrutturaPropertiesType
    extends OrganizationUnitPropertiesType
{

    @XmlElement(required = true)
    protected EnumTipoStrutturaType tipo;
    protected int livello;
    @XmlElement(required = true)
    protected String descrizioneLivello;
    @XmlElement(required = true)
    protected ObjectIdType idAOO;

    /**
     * Recupera il valore della proprietà tipo.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipoStrutturaType }
     *     
     */
    public EnumTipoStrutturaType getTipo() {
        return tipo;
    }

    /**
     * Imposta il valore della proprietà tipo.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipoStrutturaType }
     *     
     */
    public void setTipo(EnumTipoStrutturaType value) {
        this.tipo = value;
    }

    /**
     * Recupera il valore della proprietà livello.
     * 
     */
    public int getLivello() {
        return livello;
    }

    /**
     * Imposta il valore della proprietà livello.
     * 
     */
    public void setLivello(int value) {
        this.livello = value;
    }

    /**
     * Recupera il valore della proprietà descrizioneLivello.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneLivello() {
        return descrizioneLivello;
    }

    /**
     * Imposta il valore della proprietà descrizioneLivello.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizioneLivello(String value) {
        this.descrizioneLivello = value;
    }

    /**
     * Recupera il valore della proprietà idAOO.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdAOO() {
        return idAOO;
    }

    /**
     * Imposta il valore della proprietà idAOO.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdAOO(ObjectIdType value) {
        this.idAOO = value;
    }

}
