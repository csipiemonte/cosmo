
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per xmlReferenceDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="xmlReferenceDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}xmlPropertyDto">
 *       &lt;sequence>
 *         &lt;element name="dataObjectFormat" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}xmlDataObjectFormatDto" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="insertData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referenceData" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}xmlReferenceDataDto" minOccurs="0"/>
 *         &lt;element name="transformation" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}xmlTransformationDto" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uri" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlReferenceDto", propOrder = {
    "dataObjectFormat",
    "id",
    "insertData",
    "referenceData",
    "transformation",
    "type",
    "uri"
})
public class XmlReferenceDto
    extends XmlPropertyDto
{

    protected XmlDataObjectFormatDto dataObjectFormat;
    protected String id;
    protected String insertData;
    protected XmlReferenceDataDto referenceData;
    @XmlElement(nillable = true)
    protected List<XmlTransformationDto> transformation;
    protected String type;
    protected String uri;

    /**
     * Recupera il valore della proprietà dataObjectFormat.
     * 
     * @return
     *     possible object is
     *     {@link XmlDataObjectFormatDto }
     *     
     */
    public XmlDataObjectFormatDto getDataObjectFormat() {
        return dataObjectFormat;
    }

    /**
     * Imposta il valore della proprietà dataObjectFormat.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlDataObjectFormatDto }
     *     
     */
    public void setDataObjectFormat(XmlDataObjectFormatDto value) {
        this.dataObjectFormat = value;
    }

    /**
     * Recupera il valore della proprietà id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Imposta il valore della proprietà id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Recupera il valore della proprietà insertData.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsertData() {
        return insertData;
    }

    /**
     * Imposta il valore della proprietà insertData.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsertData(String value) {
        this.insertData = value;
    }

    /**
     * Recupera il valore della proprietà referenceData.
     * 
     * @return
     *     possible object is
     *     {@link XmlReferenceDataDto }
     *     
     */
    public XmlReferenceDataDto getReferenceData() {
        return referenceData;
    }

    /**
     * Imposta il valore della proprietà referenceData.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlReferenceDataDto }
     *     
     */
    public void setReferenceData(XmlReferenceDataDto value) {
        this.referenceData = value;
    }

    /**
     * Gets the value of the transformation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transformation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransformation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlTransformationDto }
     * 
     * 
     */
    public List<XmlTransformationDto> getTransformation() {
        if (transformation == null) {
            transformation = new ArrayList<XmlTransformationDto>();
        }
        return this.transformation;
    }

    /**
     * Recupera il valore della proprietà type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Imposta il valore della proprietà type.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Recupera il valore della proprietà uri.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUri() {
        return uri;
    }

    /**
     * Imposta il valore della proprietà uri.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUri(String value) {
        this.uri = value;
    }

}
