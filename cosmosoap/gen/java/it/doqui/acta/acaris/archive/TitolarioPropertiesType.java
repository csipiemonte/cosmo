
package it.doqui.acta.acaris.archive;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import it.doqui.acta.acaris.common.IdProvvedimentoAutorizzatType;


/**
 * <p>Classe Java per TitolarioPropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="TitolarioPropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}FolderPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="codice" type="{archive.acaris.acta.doqui.it}CodiceType"/>
 *         &lt;element name="descrizione" type="{archive.acaris.acta.doqui.it}DescrizioneType"/>
 *         &lt;element name="stato" type="{archive.acaris.acta.doqui.it}enumTitolarioStatoType"/>
 *         &lt;element name="idRaggruppamentoAOO" type="{archive.acaris.acta.doqui.it}IdRaggruppamentoAOOType"/>
 *         &lt;element name="dataCreaz" type="{archive.acaris.acta.doqui.it}DataCreazType"/>
 *         &lt;element name="dataInizio" type="{archive.acaris.acta.doqui.it}DataInizioType"/>
 *         &lt;element name="dataFine" type="{archive.acaris.acta.doqui.it}DataFineType"/>
 *         &lt;element name="numeroMaxLivelli" type="{archive.acaris.acta.doqui.it}NumeroMaxlivelliType"/>
 *         &lt;element name="numeroLivSottofascicoliAmmessi" type="{archive.acaris.acta.doqui.it}NumeroLivSottofascicoliAmmessiType"/>
 *         &lt;element name="creaAutomaPrimaSerie" type="{archive.acaris.acta.doqui.it}CreaAutomaPrimaSerieType"/>
 *         &lt;element name="serieMultipleVoce" type="{archive.acaris.acta.doqui.it}SerieMultipleVoceType"/>
 *         &lt;element name="insertContenutiInVociConSottovoci" type="{archive.acaris.acta.doqui.it}InsertContenutIinVociConSottovociType"/>
 *         &lt;element name="insertDocInFascConSottofasc" type="{archive.acaris.acta.doqui.it}InsertDocInFascConSottofascType"/>
 *         &lt;element name="insertFuoriVolumi" type="{archive.acaris.acta.doqui.it}InsertFuoriVolumiType"/>
 *         &lt;element name="scartoSoloConVolumi" type="{archive.acaris.acta.doqui.it}ScartoSoloConVolumiType"/>
 *         &lt;element name="idProvvedimentoAutorizzatList" type="{common.acaris.acta.doqui.it}IdProvvedimentoAutorizzatType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TitolarioPropertiesType", propOrder = {
    "codice",
    "descrizione",
    "stato",
    "idRaggruppamentoAOO",
    "dataCreaz",
    "dataInizio",
    "dataFine",
    "numeroMaxLivelli",
    "numeroLivSottofascicoliAmmessi",
    "creaAutomaPrimaSerie",
    "serieMultipleVoce",
    "insertContenutiInVociConSottovoci",
    "insertDocInFascConSottofasc",
    "insertFuoriVolumi",
    "scartoSoloConVolumi",
    "idProvvedimentoAutorizzatList"
})
public class TitolarioPropertiesType
    extends FolderPropertiesType
{

    @XmlElement(required = true)
    protected String codice;
    @XmlElement(required = true)
    protected String descrizione;
    @XmlElement(required = true)
    protected EnumTitolarioStatoType stato;
    @XmlElement(required = true)
    protected String idRaggruppamentoAOO;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataCreaz;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataInizio;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataFine;
    protected int numeroMaxLivelli;
    protected int numeroLivSottofascicoliAmmessi;
    protected boolean creaAutomaPrimaSerie;
    protected boolean serieMultipleVoce;
    protected boolean insertContenutiInVociConSottovoci;
    protected boolean insertDocInFascConSottofasc;
    protected boolean insertFuoriVolumi;
    protected boolean scartoSoloConVolumi;
    protected List<IdProvvedimentoAutorizzatType> idProvvedimentoAutorizzatList;

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
     * Recupera il valore della proprietà descrizione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta il valore della proprietà descrizione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizione(String value) {
        this.descrizione = value;
    }

    /**
     * Recupera il valore della proprietà stato.
     * 
     * @return
     *     possible object is
     *     {@link EnumTitolarioStatoType }
     *     
     */
    public EnumTitolarioStatoType getStato() {
        return stato;
    }

    /**
     * Imposta il valore della proprietà stato.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTitolarioStatoType }
     *     
     */
    public void setStato(EnumTitolarioStatoType value) {
        this.stato = value;
    }

    /**
     * Recupera il valore della proprietà idRaggruppamentoAOO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdRaggruppamentoAOO() {
        return idRaggruppamentoAOO;
    }

    /**
     * Imposta il valore della proprietà idRaggruppamentoAOO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdRaggruppamentoAOO(String value) {
        this.idRaggruppamentoAOO = value;
    }

    /**
     * Recupera il valore della proprietà dataCreaz.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataCreaz() {
        return dataCreaz;
    }

    /**
     * Imposta il valore della proprietà dataCreaz.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataCreaz(XMLGregorianCalendar value) {
        this.dataCreaz = value;
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
     * Recupera il valore della proprietà numeroMaxLivelli.
     * 
     */
    public int getNumeroMaxLivelli() {
        return numeroMaxLivelli;
    }

    /**
     * Imposta il valore della proprietà numeroMaxLivelli.
     * 
     */
    public void setNumeroMaxLivelli(int value) {
        this.numeroMaxLivelli = value;
    }

    /**
     * Recupera il valore della proprietà numeroLivSottofascicoliAmmessi.
     * 
     */
    public int getNumeroLivSottofascicoliAmmessi() {
        return numeroLivSottofascicoliAmmessi;
    }

    /**
     * Imposta il valore della proprietà numeroLivSottofascicoliAmmessi.
     * 
     */
    public void setNumeroLivSottofascicoliAmmessi(int value) {
        this.numeroLivSottofascicoliAmmessi = value;
    }

    /**
     * Recupera il valore della proprietà creaAutomaPrimaSerie.
     * 
     */
    public boolean isCreaAutomaPrimaSerie() {
        return creaAutomaPrimaSerie;
    }

    /**
     * Imposta il valore della proprietà creaAutomaPrimaSerie.
     * 
     */
    public void setCreaAutomaPrimaSerie(boolean value) {
        this.creaAutomaPrimaSerie = value;
    }

    /**
     * Recupera il valore della proprietà serieMultipleVoce.
     * 
     */
    public boolean isSerieMultipleVoce() {
        return serieMultipleVoce;
    }

    /**
     * Imposta il valore della proprietà serieMultipleVoce.
     * 
     */
    public void setSerieMultipleVoce(boolean value) {
        this.serieMultipleVoce = value;
    }

    /**
     * Recupera il valore della proprietà insertContenutiInVociConSottovoci.
     * 
     */
    public boolean isInsertContenutiInVociConSottovoci() {
        return insertContenutiInVociConSottovoci;
    }

    /**
     * Imposta il valore della proprietà insertContenutiInVociConSottovoci.
     * 
     */
    public void setInsertContenutiInVociConSottovoci(boolean value) {
        this.insertContenutiInVociConSottovoci = value;
    }

    /**
     * Recupera il valore della proprietà insertDocInFascConSottofasc.
     * 
     */
    public boolean isInsertDocInFascConSottofasc() {
        return insertDocInFascConSottofasc;
    }

    /**
     * Imposta il valore della proprietà insertDocInFascConSottofasc.
     * 
     */
    public void setInsertDocInFascConSottofasc(boolean value) {
        this.insertDocInFascConSottofasc = value;
    }

    /**
     * Recupera il valore della proprietà insertFuoriVolumi.
     * 
     */
    public boolean isInsertFuoriVolumi() {
        return insertFuoriVolumi;
    }

    /**
     * Imposta il valore della proprietà insertFuoriVolumi.
     * 
     */
    public void setInsertFuoriVolumi(boolean value) {
        this.insertFuoriVolumi = value;
    }

    /**
     * Recupera il valore della proprietà scartoSoloConVolumi.
     * 
     */
    public boolean isScartoSoloConVolumi() {
        return scartoSoloConVolumi;
    }

    /**
     * Imposta il valore della proprietà scartoSoloConVolumi.
     * 
     */
    public void setScartoSoloConVolumi(boolean value) {
        this.scartoSoloConVolumi = value;
    }

    /**
     * Gets the value of the idProvvedimentoAutorizzatList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idProvvedimentoAutorizzatList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdProvvedimentoAutorizzatList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IdProvvedimentoAutorizzatType }
     * 
     * 
     */
    public List<IdProvvedimentoAutorizzatType> getIdProvvedimentoAutorizzatList() {
        if (idProvvedimentoAutorizzatList == null) {
            idProvvedimentoAutorizzatList = new ArrayList<IdProvvedimentoAutorizzatType>();
        }
        return this.idProvvedimentoAutorizzatList;
    }

}
