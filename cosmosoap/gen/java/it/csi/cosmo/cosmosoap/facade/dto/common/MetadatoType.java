
package it.csi.cosmo.cosmosoap.facade.dto.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per MetadatoType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MetadatoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Nome" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String200Type"/>
 *         &lt;choice>
 *           &lt;element name="Valore" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String1000Type"/>
 *           &lt;element name="Valori" type="{http://www.csi.it/stardas/services/StardasCommonTypes}ValoriType"/>
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
@XmlType(name = "MetadatoType", propOrder = {
    "nome",
    "valore",
    "valori"
})
public class MetadatoType {

    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "Valore")
    protected String valore;
    @XmlElement(name = "Valori")
    protected ValoriType valori;

    /**
     * Recupera il valore della proprietà nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprietà nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprietà valore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValore() {
        return valore;
    }

    /**
     * Imposta il valore della proprietà valore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValore(String value) {
        this.valore = value;
    }

    /**
     * Recupera il valore della proprietà valori.
     * 
     * @return
     *     possible object is
     *     {@link ValoriType }
     *     
     */
    public ValoriType getValori() {
        return valori;
    }

    /**
     * Imposta il valore della proprietà valori.
     * 
     * @param value
     *     allowed object is
     *     {@link ValoriType }
     *     
     */
    public void setValori(ValoriType value) {
        this.valori = value;
    }

}
