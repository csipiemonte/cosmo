
package it.doqui.acta.acaris.archive;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import it.doqui.acta.acaris.common.CodiceFiscaleType;
import it.doqui.acta.acaris.common.IdAnnotazioniType;
import it.doqui.acta.acaris.common.IdMovimentazioneType;


/**
 * <p>Classe Java per ClassificazionePropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ClassificazionePropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}FolderPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="numero" type="{archive.acaris.acta.doqui.it}NumeroType"/>
 *         &lt;element name="codice" type="{archive.acaris.acta.doqui.it}CodiceType"/>
 *         &lt;element name="numeroInput" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="stato" type="{archive.acaris.acta.doqui.it}enumClassificazioneStatoType"/>
 *         &lt;element name="dataInizio" type="{archive.acaris.acta.doqui.it}DataInizioType"/>
 *         &lt;element name="dataFine" type="{archive.acaris.acta.doqui.it}DataFineType"/>
 *         &lt;element name="utenteCreazione" type="{common.acaris.acta.doqui.it}CodiceFiscaleType"/>
 *         &lt;element name="collocazioneCartacea" type="{archive.acaris.acta.doqui.it}CollocazioneCartaceaType"/>
 *         &lt;element name="copiaCartacea" type="{archive.acaris.acta.doqui.it}CopiaCartaceaType"/>
 *         &lt;element name="cartaceo" type="{archive.acaris.acta.doqui.it}CartaceoType"/>
 *         &lt;element name="legislatura" type="{archive.acaris.acta.doqui.it}LegislaturaType"/>
 *         &lt;element name="idAnnotazioniList" type="{common.acaris.acta.doqui.it}IdAnnotazioniType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idMovimentazioneList" type="{common.acaris.acta.doqui.it}IdMovimentazioneType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idStatoDocumento" type="{archive.acaris.acta.doqui.it}IdStatoDocumentoType"/>
 *         &lt;element name="idTipoClasse" type="{archive.acaris.acta.doqui.it}IdTipoClasseType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClassificazionePropertiesType", propOrder = {
    "numero",
    "codice",
    "numeroInput",
    "stato",
    "dataInizio",
    "dataFine",
    "utenteCreazione",
    "collocazioneCartacea",
    "copiaCartacea",
    "cartaceo",
    "legislatura",
    "idAnnotazioniList",
    "idMovimentazioneList",
    "idStatoDocumento",
    "idTipoClasse"
})
public class ClassificazionePropertiesType
    extends FolderPropertiesType
{

    protected int numero;
    @XmlElement(required = true)
    protected String codice;
    @XmlElement(required = true)
    protected String numeroInput;
    @XmlElement(required = true)
    protected EnumClassificazioneStatoType stato;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataInizio;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataFine;
    @XmlElement(required = true)
    protected CodiceFiscaleType utenteCreazione;
    @XmlElement(required = true)
    protected String collocazioneCartacea;
    protected boolean copiaCartacea;
    protected boolean cartaceo;
    @XmlElement(required = true)
    protected String legislatura;
    protected List<IdAnnotazioniType> idAnnotazioniList;
    protected List<IdMovimentazioneType> idMovimentazioneList;
    protected int idStatoDocumento;
    protected int idTipoClasse;

    /**
     * Recupera il valore della proprietà numero.
     * 
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Imposta il valore della proprietà numero.
     * 
     */
    public void setNumero(int value) {
        this.numero = value;
    }

    /**
     * Recupera il valore della proprietà codice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodice() {
        return codice;
    }

    /**
     * Imposta il valore della proprietà codice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodice(String value) {
        this.codice = value;
    }

    /**
     * Recupera il valore della proprietà numeroInput.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroInput() {
        return numeroInput;
    }

    /**
     * Imposta il valore della proprietà numeroInput.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroInput(String value) {
        this.numeroInput = value;
    }

    /**
     * Recupera il valore della proprietà stato.
     * 
     * @return
     *     possible object is
     *     {@link EnumClassificazioneStatoType }
     *     
     */
    public EnumClassificazioneStatoType getStato() {
        return stato;
    }

    /**
     * Imposta il valore della proprietà stato.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumClassificazioneStatoType }
     *     
     */
    public void setStato(EnumClassificazioneStatoType value) {
        this.stato = value;
    }

    /**
     * Recupera il valore della proprietà dataInizio.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInizio() {
        return dataInizio;
    }

    /**
     * Imposta il valore della proprietà dataInizio.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInizio(XMLGregorianCalendar value) {
        this.dataInizio = value;
    }

    /**
     * Recupera il valore della proprietà dataFine.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataFine() {
        return dataFine;
    }

    /**
     * Imposta il valore della proprietà dataFine.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFine(XMLGregorianCalendar value) {
        this.dataFine = value;
    }

    /**
     * Recupera il valore della proprietà utenteCreazione.
     * 
     * @return
     *     possible object is
     *     {@link CodiceFiscaleType }
     *     
     */
    public CodiceFiscaleType getUtenteCreazione() {
        return utenteCreazione;
    }

    /**
     * Imposta il valore della proprietà utenteCreazione.
     * 
     * @param value
     *     allowed object is
     *     {@link CodiceFiscaleType }
     *     
     */
    public void setUtenteCreazione(CodiceFiscaleType value) {
        this.utenteCreazione = value;
    }

    /**
     * Recupera il valore della proprietà collocazioneCartacea.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollocazioneCartacea() {
        return collocazioneCartacea;
    }

    /**
     * Imposta il valore della proprietà collocazioneCartacea.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollocazioneCartacea(String value) {
        this.collocazioneCartacea = value;
    }

    /**
     * Recupera il valore della proprietà copiaCartacea.
     * 
     */
    public boolean isCopiaCartacea() {
        return copiaCartacea;
    }

    /**
     * Imposta il valore della proprietà copiaCartacea.
     * 
     */
    public void setCopiaCartacea(boolean value) {
        this.copiaCartacea = value;
    }

    /**
     * Recupera il valore della proprietà cartaceo.
     * 
     */
    public boolean isCartaceo() {
        return cartaceo;
    }

    /**
     * Imposta il valore della proprietà cartaceo.
     * 
     */
    public void setCartaceo(boolean value) {
        this.cartaceo = value;
    }

    /**
     * Recupera il valore della proprietà legislatura.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegislatura() {
        return legislatura;
    }

    /**
     * Imposta il valore della proprietà legislatura.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegislatura(String value) {
        this.legislatura = value;
    }

    /**
     * Gets the value of the idAnnotazioniList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idAnnotazioniList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdAnnotazioniList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IdAnnotazioniType }
     * 
     * 
     */
    public List<IdAnnotazioniType> getIdAnnotazioniList() {
        if (idAnnotazioniList == null) {
            idAnnotazioniList = new ArrayList<IdAnnotazioniType>();
        }
        return this.idAnnotazioniList;
    }

    /**
     * Gets the value of the idMovimentazioneList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idMovimentazioneList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdMovimentazioneList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IdMovimentazioneType }
     * 
     * 
     */
    public List<IdMovimentazioneType> getIdMovimentazioneList() {
        if (idMovimentazioneList == null) {
            idMovimentazioneList = new ArrayList<IdMovimentazioneType>();
        }
        return this.idMovimentazioneList;
    }

    /**
     * Recupera il valore della proprietà idStatoDocumento.
     * 
     */
    public int getIdStatoDocumento() {
        return idStatoDocumento;
    }

    /**
     * Imposta il valore della proprietà idStatoDocumento.
     * 
     */
    public void setIdStatoDocumento(int value) {
        this.idStatoDocumento = value;
    }

    /**
     * Recupera il valore della proprietà idTipoClasse.
     * 
     */
    public int getIdTipoClasse() {
        return idTipoClasse;
    }

    /**
     * Imposta il valore della proprietà idTipoClasse.
     * 
     */
    public void setIdTipoClasse(int value) {
        this.idTipoClasse = value;
    }

}
