
package it.doqui.dosign.dosign.business.session.dosign.remotev2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per remoteGraphicDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="remoteGraphicDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://remotev2.dosign.session.business.dosign.dosign.doqui.it/}dosignDto">
 *       &lt;sequence>
 *         &lt;element name="page" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="leftx" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="lefty" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="rightx" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="righty" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="image" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="testo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="imageOnly" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="showDateTime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="labelReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="labelDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="formatDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="labelSignedBy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="font" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fontSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fontStyle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="apparance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "remoteGraphicDto", propOrder = {
    "page",
    "leftx",
    "lefty",
    "rightx",
    "righty",
    "image",
    "testo",
    "imageOnly",
    "showDateTime",
    "labelReason",
    "reason",
    "labelDate",
    "formatDate",
    "labelSignedBy",
    "font",
    "fontSize",
    "fontStyle",
    "apparance"
})
public class RemoteGraphicDto
    extends DosignDto
{

    protected int page;
    protected int leftx;
    protected int lefty;
    protected int rightx;
    protected int righty;
    protected byte[] image;
    protected String testo;
    protected boolean imageOnly;
    protected boolean showDateTime;
    protected String labelReason;
    protected String reason;
    protected String labelDate;
    protected String formatDate;
    protected String labelSignedBy;
    protected String font;
    protected String fontSize;
    protected String fontStyle;
    protected String apparance;

    /**
     * Recupera il valore della proprietà page.
     * 
     */
    public int getPage() {
        return page;
    }

    /**
     * Imposta il valore della proprietà page.
     * 
     */
    public void setPage(int value) {
        this.page = value;
    }

    /**
     * Recupera il valore della proprietà leftx.
     * 
     */
    public int getLeftx() {
        return leftx;
    }

    /**
     * Imposta il valore della proprietà leftx.
     * 
     */
    public void setLeftx(int value) {
        this.leftx = value;
    }

    /**
     * Recupera il valore della proprietà lefty.
     * 
     */
    public int getLefty() {
        return lefty;
    }

    /**
     * Imposta il valore della proprietà lefty.
     * 
     */
    public void setLefty(int value) {
        this.lefty = value;
    }

    /**
     * Recupera il valore della proprietà rightx.
     * 
     */
    public int getRightx() {
        return rightx;
    }

    /**
     * Imposta il valore della proprietà rightx.
     * 
     */
    public void setRightx(int value) {
        this.rightx = value;
    }

    /**
     * Recupera il valore della proprietà righty.
     * 
     */
    public int getRighty() {
        return righty;
    }

    /**
     * Imposta il valore della proprietà righty.
     * 
     */
    public void setRighty(int value) {
        this.righty = value;
    }

    /**
     * Recupera il valore della proprietà image.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * Imposta il valore della proprietà image.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setImage(byte[] value) {
        this.image = value;
    }

    /**
     * Recupera il valore della proprietà testo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTesto() {
        return testo;
    }

    /**
     * Imposta il valore della proprietà testo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTesto(String value) {
        this.testo = value;
    }

    /**
     * Recupera il valore della proprietà imageOnly.
     * 
     */
    public boolean isImageOnly() {
        return imageOnly;
    }

    /**
     * Imposta il valore della proprietà imageOnly.
     * 
     */
    public void setImageOnly(boolean value) {
        this.imageOnly = value;
    }

    /**
     * Recupera il valore della proprietà showDateTime.
     * 
     */
    public boolean isShowDateTime() {
        return showDateTime;
    }

    /**
     * Imposta il valore della proprietà showDateTime.
     * 
     */
    public void setShowDateTime(boolean value) {
        this.showDateTime = value;
    }

    /**
     * Recupera il valore della proprietà labelReason.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabelReason() {
        return labelReason;
    }

    /**
     * Imposta il valore della proprietà labelReason.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabelReason(String value) {
        this.labelReason = value;
    }

    /**
     * Recupera il valore della proprietà reason.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Imposta il valore della proprietà reason.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
    }

    /**
     * Recupera il valore della proprietà labelDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabelDate() {
        return labelDate;
    }

    /**
     * Imposta il valore della proprietà labelDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabelDate(String value) {
        this.labelDate = value;
    }

    /**
     * Recupera il valore della proprietà formatDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormatDate() {
        return formatDate;
    }

    /**
     * Imposta il valore della proprietà formatDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormatDate(String value) {
        this.formatDate = value;
    }

    /**
     * Recupera il valore della proprietà labelSignedBy.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabelSignedBy() {
        return labelSignedBy;
    }

    /**
     * Imposta il valore della proprietà labelSignedBy.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabelSignedBy(String value) {
        this.labelSignedBy = value;
    }

    /**
     * Recupera il valore della proprietà font.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFont() {
        return font;
    }

    /**
     * Imposta il valore della proprietà font.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFont(String value) {
        this.font = value;
    }

    /**
     * Recupera il valore della proprietà fontSize.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFontSize() {
        return fontSize;
    }

    /**
     * Imposta il valore della proprietà fontSize.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFontSize(String value) {
        this.fontSize = value;
    }

    /**
     * Recupera il valore della proprietà fontStyle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFontStyle() {
        return fontStyle;
    }

    /**
     * Imposta il valore della proprietà fontStyle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFontStyle(String value) {
        this.fontStyle = value;
    }

    /**
     * Recupera il valore della proprietà apparance.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApparance() {
        return apparance;
    }

    /**
     * Imposta il valore della proprietà apparance.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApparance(String value) {
        this.apparance = value;
    }

}
