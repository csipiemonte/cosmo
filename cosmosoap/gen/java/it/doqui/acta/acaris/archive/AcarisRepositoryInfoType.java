
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per acarisRepositoryInfoType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="acarisRepositoryInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="repositoryId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="repositoryName" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="repositoryURI" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="repositoryDescription" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="rootFolderId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="documentRootFolderId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="numeroMassimoRisultati" type="{common.acaris.acta.doqui.it}integer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "acarisRepositoryInfoType", propOrder = {
    "repositoryId",
    "repositoryName",
    "repositoryURI",
    "repositoryDescription",
    "rootFolderId",
    "documentRootFolderId",
    "numeroMassimoRisultati"
})
public class AcarisRepositoryInfoType {

    @XmlElement(required = true)
    protected ObjectIdType repositoryId;
    @XmlElement(required = true)
    protected String repositoryName;
    @XmlElement(required = true)
    protected String repositoryURI;
    @XmlElement(required = true)
    protected String repositoryDescription;
    @XmlElement(required = true)
    protected ObjectIdType rootFolderId;
    @XmlElement(required = true)
    protected ObjectIdType documentRootFolderId;
    protected int numeroMassimoRisultati;

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
     * Recupera il valore della proprietà repositoryName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepositoryName() {
        return repositoryName;
    }

    /**
     * Imposta il valore della proprietà repositoryName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepositoryName(String value) {
        this.repositoryName = value;
    }

    /**
     * Recupera il valore della proprietà repositoryURI.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepositoryURI() {
        return repositoryURI;
    }

    /**
     * Imposta il valore della proprietà repositoryURI.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepositoryURI(String value) {
        this.repositoryURI = value;
    }

    /**
     * Recupera il valore della proprietà repositoryDescription.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepositoryDescription() {
        return repositoryDescription;
    }

    /**
     * Imposta il valore della proprietà repositoryDescription.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepositoryDescription(String value) {
        this.repositoryDescription = value;
    }

    /**
     * Recupera il valore della proprietà rootFolderId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getRootFolderId() {
        return rootFolderId;
    }

    /**
     * Imposta il valore della proprietà rootFolderId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setRootFolderId(ObjectIdType value) {
        this.rootFolderId = value;
    }

    /**
     * Recupera il valore della proprietà documentRootFolderId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getDocumentRootFolderId() {
        return documentRootFolderId;
    }

    /**
     * Imposta il valore della proprietà documentRootFolderId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setDocumentRootFolderId(ObjectIdType value) {
        this.documentRootFolderId = value;
    }

    /**
     * Recupera il valore della proprietà numeroMassimoRisultati.
     * 
     */
    public int getNumeroMassimoRisultati() {
        return numeroMassimoRisultati;
    }

    /**
     * Imposta il valore della proprietà numeroMassimoRisultati.
     * 
     */
    public void setNumeroMassimoRisultati(int value) {
        this.numeroMassimoRisultati = value;
    }

}
