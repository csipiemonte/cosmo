
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DocumentoSemplicePropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DocumentoSemplicePropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}DocumentoPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="codBarre" type="{archive.acaris.acta.doqui.it}CodBarreType"/>
 *         &lt;element name="multiplo" type="{archive.acaris.acta.doqui.it}MultiploType"/>
 *         &lt;element name="tipoDocFisico" type="{archive.acaris.acta.doqui.it}enumTipoDocumentoType"/>
 *         &lt;element name="composizione" type="{archive.acaris.acta.doqui.it}enumDocPrimarioType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentoSemplicePropertiesType", propOrder = {
    "codBarre",
    "multiplo",
    "tipoDocFisico",
    "composizione"
})
public class DocumentoSemplicePropertiesType
    extends DocumentoPropertiesType
{

    @XmlElement(required = true)
    protected String codBarre;
    protected boolean multiplo;
    @XmlElement(required = true)
    protected EnumTipoDocumentoType tipoDocFisico;
    @XmlElement(required = true)
    protected EnumDocPrimarioType composizione;

    /**
     * Recupera il valore della proprietà codBarre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodBarre() {
        return codBarre;
    }

    /**
     * Imposta il valore della proprietà codBarre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodBarre(String value) {
        this.codBarre = value;
    }

    /**
     * Recupera il valore della proprietà multiplo.
     * 
     */
    public boolean isMultiplo() {
        return multiplo;
    }

    /**
     * Imposta il valore della proprietà multiplo.
     * 
     */
    public void setMultiplo(boolean value) {
        this.multiplo = value;
    }

    /**
     * Recupera il valore della proprietà tipoDocFisico.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipoDocumentoType }
     *     
     */
    public EnumTipoDocumentoType getTipoDocFisico() {
        return tipoDocFisico;
    }

    /**
     * Imposta il valore della proprietà tipoDocFisico.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipoDocumentoType }
     *     
     */
    public void setTipoDocFisico(EnumTipoDocumentoType value) {
        this.tipoDocFisico = value;
    }

    /**
     * Recupera il valore della proprietà composizione.
     * 
     * @return
     *     possible object is
     *     {@link EnumDocPrimarioType }
     *     
     */
    public EnumDocPrimarioType getComposizione() {
        return composizione;
    }

    /**
     * Imposta il valore della proprietà composizione.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumDocPrimarioType }
     *     
     */
    public void setComposizione(EnumDocPrimarioType value) {
        this.composizione = value;
    }

}
