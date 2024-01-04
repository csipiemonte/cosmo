
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.EnumMimeTypeType;


/**
 * <p>Classe Java per DocumentPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DocumentPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}ArchivePropertiesType">
 *       &lt;sequence>
 *         &lt;element name="objectTypeId" type="{archive.acaris.acta.doqui.it}enumArchiveObjectType"/>
 *         &lt;element name="contentStreamLength" type="{common.acaris.acta.doqui.it}contentStreamLengthType"/>
 *         &lt;element name="contentStreamMimeType" type="{common.acaris.acta.doqui.it}enumMimeTypeType"/>
 *         &lt;element name="contentStreamFilename" type="{common.acaris.acta.doqui.it}contentStreamFilenameType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentPropertiesType", propOrder = {
    "objectTypeId",
    "contentStreamLength",
    "contentStreamMimeType",
    "contentStreamFilename"
})
@XmlSeeAlso({
    ContenutoFisicoPropertiesType.class,
    ClipsMetallicaPropertiesType.class,
    DocumentoPropertiesType.class
})
public abstract class DocumentPropertiesType
    extends ArchivePropertiesType
{

    @XmlElement(required = true)
    protected EnumArchiveObjectType objectTypeId;
    protected int contentStreamLength;
    @XmlElement(required = true)
    protected EnumMimeTypeType contentStreamMimeType;
    @XmlElement(required = true)
    protected String contentStreamFilename;

    /**
     * Recupera il valore della proprietà objectTypeId.
     * 
     * @return
     *     possible object is
     *     {@link EnumArchiveObjectType }
     *     
     */
    public EnumArchiveObjectType getObjectTypeId() {
        return objectTypeId;
    }

    /**
     * Imposta il valore della proprietà objectTypeId.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumArchiveObjectType }
     *     
     */
    public void setObjectTypeId(EnumArchiveObjectType value) {
        this.objectTypeId = value;
    }

    /**
     * Recupera il valore della proprietà contentStreamLength.
     * 
     */
    public int getContentStreamLength() {
        return contentStreamLength;
    }

    /**
     * Imposta il valore della proprietà contentStreamLength.
     * 
     */
    public void setContentStreamLength(int value) {
        this.contentStreamLength = value;
    }

    /**
     * Recupera il valore della proprietà contentStreamMimeType.
     * 
     * @return
     *     possible object is
     *     {@link EnumMimeTypeType }
     *     
     */
    public EnumMimeTypeType getContentStreamMimeType() {
        return contentStreamMimeType;
    }

    /**
     * Imposta il valore della proprietà contentStreamMimeType.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumMimeTypeType }
     *     
     */
    public void setContentStreamMimeType(EnumMimeTypeType value) {
        this.contentStreamMimeType = value;
    }

    /**
     * Recupera il valore della proprietà contentStreamFilename.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContentStreamFilename() {
        return contentStreamFilename;
    }

    /**
     * Imposta il valore della proprietà contentStreamFilename.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentStreamFilename(String value) {
        this.contentStreamFilename = value;
    }

}
