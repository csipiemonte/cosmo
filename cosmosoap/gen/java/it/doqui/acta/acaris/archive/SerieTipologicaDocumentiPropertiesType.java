
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per SerieTipologicaDocumentiPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="SerieTipologicaDocumentiPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}SeriePropertiesType">
 *       &lt;sequence>
 *         &lt;element name="modalitaCalcoloProgDoc" type="{archive.acaris.acta.doqui.it}enumModalitaCalcoloProgDocType"/>
 *         &lt;element name="parteFissa" type="{archive.acaris.acta.doqui.it}ParteFissaType"/>
 *         &lt;element name="registri" type="{archive.acaris.acta.doqui.it}RegistriType"/>
 *         &lt;element name="docAltraClassificazione" type="{archive.acaris.acta.doqui.it}DocAltraClassificazioneType"/>
 *         &lt;element name="stato" type="{archive.acaris.acta.doqui.it}enumSerieTipologicaDocumentiStatoType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SerieTipologicaDocumentiPropertiesType", propOrder = {
    "modalitaCalcoloProgDoc",
    "parteFissa",
    "registri",
    "docAltraClassificazione",
    "stato"
})
public class SerieTipologicaDocumentiPropertiesType
    extends SeriePropertiesType
{

    @XmlElement(required = true)
    protected EnumModalitaCalcoloProgDocType modalitaCalcoloProgDoc;
    @XmlElement(required = true)
    protected String parteFissa;
    protected boolean registri;
    protected boolean docAltraClassificazione;
    @XmlElement(required = true)
    protected EnumSerieTipologicaDocumentiStatoType stato;

    /**
     * Recupera il valore della proprietà modalitaCalcoloProgDoc.
     * 
     * @return
     *     possible object is
     *     {@link EnumModalitaCalcoloProgDocType }
     *     
     */
    public EnumModalitaCalcoloProgDocType getModalitaCalcoloProgDoc() {
        return modalitaCalcoloProgDoc;
    }

    /**
     * Imposta il valore della proprietà modalitaCalcoloProgDoc.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumModalitaCalcoloProgDocType }
     *     
     */
    public void setModalitaCalcoloProgDoc(EnumModalitaCalcoloProgDocType value) {
        this.modalitaCalcoloProgDoc = value;
    }

    /**
     * Recupera il valore della proprietà parteFissa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParteFissa() {
        return parteFissa;
    }

    /**
     * Imposta il valore della proprietà parteFissa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParteFissa(String value) {
        this.parteFissa = value;
    }

    /**
     * Recupera il valore della proprietà registri.
     * 
     */
    public boolean isRegistri() {
        return registri;
    }

    /**
     * Imposta il valore della proprietà registri.
     * 
     */
    public void setRegistri(boolean value) {
        this.registri = value;
    }

    /**
     * Recupera il valore della proprietà docAltraClassificazione.
     * 
     */
    public boolean isDocAltraClassificazione() {
        return docAltraClassificazione;
    }

    /**
     * Imposta il valore della proprietà docAltraClassificazione.
     * 
     */
    public void setDocAltraClassificazione(boolean value) {
        this.docAltraClassificazione = value;
    }

    /**
     * Recupera il valore della proprietà stato.
     * 
     * @return
     *     possible object is
     *     {@link EnumSerieTipologicaDocumentiStatoType }
     *     
     */
    public EnumSerieTipologicaDocumentiStatoType getStato() {
        return stato;
    }

    /**
     * Imposta il valore della proprietà stato.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumSerieTipologicaDocumentiStatoType }
     *     
     */
    public void setStato(EnumSerieTipologicaDocumentiStatoType value) {
        this.stato = value;
    }

}
