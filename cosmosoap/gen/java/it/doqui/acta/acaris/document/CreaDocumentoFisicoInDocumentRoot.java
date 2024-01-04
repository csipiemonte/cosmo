
package it.doqui.acta.acaris.document;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.archive.EnumDocumentObjectType;
import it.doqui.acta.acaris.archive.EnumFolderObjectType;
import it.doqui.acta.acaris.common.AcarisContentStreamType;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.common.PrincipalIdType;
import it.doqui.acta.acaris.common.PropertiesType;


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
 *         &lt;element name="typeId" type="{archive.acaris.acta.doqui.it}enumDocumentObjectType"/>
 *         &lt;element name="associativeObjectTypeId" type="{archive.acaris.acta.doqui.it}enumFolderObjectType"/>
 *         &lt;element name="principalId" type="{common.acaris.acta.doqui.it}PrincipalIdType"/>
 *         &lt;element name="properties" type="{common.acaris.acta.doqui.it}PropertiesType"/>
 *         &lt;element name="associativeObjectProperties" type="{common.acaris.acta.doqui.it}PropertiesType"/>
 *         &lt;element name="folderId" type="{common.acaris.acta.doqui.it}ObjectIdType" minOccurs="0"/>
 *         &lt;element name="contentStream" type="{common.acaris.acta.doqui.it}acarisContentStreamType" minOccurs="0"/>
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
    "typeId",
    "associativeObjectTypeId",
    "principalId",
    "properties",
    "associativeObjectProperties",
    "folderId",
    "contentStream"
})
@XmlRootElement(name = "creaDocumentoFisicoInDocumentRoot")
public class CreaDocumentoFisicoInDocumentRoot {

    @XmlElement(required = true)
    protected ObjectIdType repositoryId;
    @XmlElement(required = true)
    protected EnumDocumentObjectType typeId;
    @XmlElement(required = true)
    protected EnumFolderObjectType associativeObjectTypeId;
    @XmlElement(required = true)
    protected PrincipalIdType principalId;
    @XmlElement(required = true)
    protected PropertiesType properties;
    @XmlElement(required = true)
    protected PropertiesType associativeObjectProperties;
    protected ObjectIdType folderId;
    protected AcarisContentStreamType contentStream;

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
     * Recupera il valore della proprietà typeId.
     * 
     * @return
     *     possible object is
     *     {@link EnumDocumentObjectType }
     *     
     */
    public EnumDocumentObjectType getTypeId() {
        return typeId;
    }

    /**
     * Imposta il valore della proprietà typeId.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumDocumentObjectType }
     *     
     */
    public void setTypeId(EnumDocumentObjectType value) {
        this.typeId = value;
    }

    /**
     * Recupera il valore della proprietà associativeObjectTypeId.
     * 
     * @return
     *     possible object is
     *     {@link EnumFolderObjectType }
     *     
     */
    public EnumFolderObjectType getAssociativeObjectTypeId() {
        return associativeObjectTypeId;
    }

    /**
     * Imposta il valore della proprietà associativeObjectTypeId.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumFolderObjectType }
     *     
     */
    public void setAssociativeObjectTypeId(EnumFolderObjectType value) {
        this.associativeObjectTypeId = value;
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
     * Recupera il valore della proprietà properties.
     * 
     * @return
     *     possible object is
     *     {@link PropertiesType }
     *     
     */
    public PropertiesType getProperties() {
        return properties;
    }

    /**
     * Imposta il valore della proprietà properties.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertiesType }
     *     
     */
    public void setProperties(PropertiesType value) {
        this.properties = value;
    }

    /**
     * Recupera il valore della proprietà associativeObjectProperties.
     * 
     * @return
     *     possible object is
     *     {@link PropertiesType }
     *     
     */
    public PropertiesType getAssociativeObjectProperties() {
        return associativeObjectProperties;
    }

    /**
     * Imposta il valore della proprietà associativeObjectProperties.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertiesType }
     *     
     */
    public void setAssociativeObjectProperties(PropertiesType value) {
        this.associativeObjectProperties = value;
    }

    /**
     * Recupera il valore della proprietà folderId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getFolderId() {
        return folderId;
    }

    /**
     * Imposta il valore della proprietà folderId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setFolderId(ObjectIdType value) {
        this.folderId = value;
    }

    /**
     * Recupera il valore della proprietà contentStream.
     * 
     * @return
     *     possible object is
     *     {@link AcarisContentStreamType }
     *     
     */
    public AcarisContentStreamType getContentStream() {
        return contentStream;
    }

    /**
     * Imposta il valore della proprietà contentStream.
     * 
     * @param value
     *     allowed object is
     *     {@link AcarisContentStreamType }
     *     
     */
    public void setContentStream(AcarisContentStreamType value) {
        this.contentStream = value;
    }

}
