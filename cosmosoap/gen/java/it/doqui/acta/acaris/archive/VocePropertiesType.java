
package it.doqui.acta.acaris.archive;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import it.doqui.acta.acaris.common.IdAnnotazioniType;
import it.doqui.acta.acaris.common.IdProvvedimentoAutorizzatType;


/**
 * <p>Classe Java per VocePropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="VocePropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}FolderPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="codice" type="{archive.acaris.acta.doqui.it}CodiceVoceType"/>
 *         &lt;element name="descrizione" type="{archive.acaris.acta.doqui.it}DescrizioneType"/>
 *         &lt;element name="stato" type="{archive.acaris.acta.doqui.it}enumVoceStatoType"/>
 *         &lt;element name="indiceDiClassificazioneEsteso" type="{archive.acaris.acta.doqui.it}IndiceClassificazioneEstesaXMLType"/>
 *         &lt;element name="descrBreve" type="{archive.acaris.acta.doqui.it}DescrBreveType"/>
 *         &lt;element name="dataCreazione" type="{archive.acaris.acta.doqui.it}DataCreazioneType"/>
 *         &lt;element name="dataInizio" type="{archive.acaris.acta.doqui.it}DataInizioType"/>
 *         &lt;element name="dataFine" type="{archive.acaris.acta.doqui.it}DataFineType"/>
 *         &lt;element name="presenzaFascStand" type="{archive.acaris.acta.doqui.it}PresenzaFascStandType"/>
 *         &lt;element name="presenzaFascRealeAnnualeNV" type="{archive.acaris.acta.doqui.it}PresenzaFascRealeAnnualeNVType"/>
 *         &lt;element name="presenzaFascRealeContinuoNV" type="{archive.acaris.acta.doqui.it}PresenzaFascRealeContinuoNVType"/>
 *         &lt;element name="presenzaFascRealeLegislaturaNV" type="{archive.acaris.acta.doqui.it}PresenzaFascRealeLegislaturaNVType"/>
 *         &lt;element name="presenzaFascRealeEreditatoNV" type="{archive.acaris.acta.doqui.it}PresenzaFascRealeEreditatoNVType"/>
 *         &lt;element name="presenzaFascRealeLiberoNV" type="{archive.acaris.acta.doqui.it}PresenzaFascRealeLiberoNVType"/>
 *         &lt;element name="presenzaFascTemp" type="{archive.acaris.acta.doqui.it}PresenzaFascTempType"/>
 *         &lt;element name="presenzaSerieDoc" type="{archive.acaris.acta.doqui.it}PresenzaSerieDocType"/>
 *         &lt;element name="presenzaSerieFasc" type="{archive.acaris.acta.doqui.it}PresenzaSerieFascType"/>
 *         &lt;element name="presenzaSerieDoss" type="{archive.acaris.acta.doqui.it}PresenzaSerieDossType"/>
 *         &lt;element name="insertSottoVociGestCont" type="{archive.acaris.acta.doqui.it}InsertSottoVociGestContType"/>
 *         &lt;element name="creataGestCont" type="{archive.acaris.acta.doqui.it}CreataGestContType"/>
 *         &lt;element name="paroleChiave" type="{archive.acaris.acta.doqui.it}ParoleChiaveType"/>
 *         &lt;element name="dataUltimaModifica" type="{archive.acaris.acta.doqui.it}DataUltimaModificaType"/>
 *         &lt;element name="conservazioneCorrente" type="{archive.acaris.acta.doqui.it}ConservazioneCorrenteType"/>
 *         &lt;element name="conservazioneGenerale" type="{archive.acaris.acta.doqui.it}ConservazioneGeneraleType"/>
 *         &lt;element name="dataBloccoPassaggioInDeposito" type="{archive.acaris.acta.doqui.it}DataBloccoPassaggioInDepositoType"/>
 *         &lt;element name="dataSbloccoPassaggioInDeposito" type="{archive.acaris.acta.doqui.it}DataSbloccoPassaggioInDepositoType"/>
 *         &lt;element name="dataCancellazione" type="{archive.acaris.acta.doqui.it}DataCancellazioneType"/>
 *         &lt;element name="dataEsportazione" type="{archive.acaris.acta.doqui.it}DataEsportazioneType"/>
 *         &lt;element name="idFascicoloStandardList" type="{archive.acaris.acta.doqui.it}IdFascicoloStandardType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idProvvedimentoAutorizzatList" type="{common.acaris.acta.doqui.it}IdProvvedimentoAutorizzatType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idAnnotazioniList" type="{common.acaris.acta.doqui.it}IdAnnotazioniType" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "VocePropertiesType", propOrder = {
    "codice",
    "descrizione",
    "stato",
    "indiceDiClassificazioneEsteso",
    "descrBreve",
    "dataCreazione",
    "dataInizio",
    "dataFine",
    "presenzaFascStand",
    "presenzaFascRealeAnnualeNV",
    "presenzaFascRealeContinuoNV",
    "presenzaFascRealeLegislaturaNV",
    "presenzaFascRealeEreditatoNV",
    "presenzaFascRealeLiberoNV",
    "presenzaFascTemp",
    "presenzaSerieDoc",
    "presenzaSerieFasc",
    "presenzaSerieDoss",
    "insertSottoVociGestCont",
    "creataGestCont",
    "paroleChiave",
    "dataUltimaModifica",
    "conservazioneCorrente",
    "conservazioneGenerale",
    "dataBloccoPassaggioInDeposito",
    "dataSbloccoPassaggioInDeposito",
    "dataCancellazione",
    "dataEsportazione",
    "idFascicoloStandardList",
    "idProvvedimentoAutorizzatList",
    "idAnnotazioniList",
    "idTipoClasse"
})
public class VocePropertiesType
    extends FolderPropertiesType
{

    protected int codice;
    @XmlElement(required = true)
    protected String descrizione;
    @XmlElement(required = true)
    protected EnumVoceStatoType stato;
    @XmlElement(required = true)
    protected String indiceDiClassificazioneEsteso;
    @XmlElement(required = true)
    protected String descrBreve;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataCreazione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataInizio;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataFine;
    protected boolean presenzaFascStand;
    protected boolean presenzaFascRealeAnnualeNV;
    protected boolean presenzaFascRealeContinuoNV;
    protected boolean presenzaFascRealeLegislaturaNV;
    protected boolean presenzaFascRealeEreditatoNV;
    protected boolean presenzaFascRealeLiberoNV;
    protected boolean presenzaFascTemp;
    protected boolean presenzaSerieDoc;
    protected boolean presenzaSerieFasc;
    protected boolean presenzaSerieDoss;
    protected boolean insertSottoVociGestCont;
    protected boolean creataGestCont;
    @XmlElement(required = true)
    protected String paroleChiave;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataUltimaModifica;
    protected int conservazioneCorrente;
    protected int conservazioneGenerale;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataBloccoPassaggioInDeposito;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataSbloccoPassaggioInDeposito;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataCancellazione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataEsportazione;
    protected List<IdFascicoloStandardType> idFascicoloStandardList;
    protected List<IdProvvedimentoAutorizzatType> idProvvedimentoAutorizzatList;
    protected List<IdAnnotazioniType> idAnnotazioniList;
    protected int idTipoClasse;

    /**
     * Recupera il valore della proprietà codice.
     * 
     */
    public int getCodice() {
        return codice;
    }

    /**
     * Imposta il valore della proprietà codice.
     * 
     */
    public void setCodice(int value) {
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
     *     {@link EnumVoceStatoType }
     *     
     */
    public EnumVoceStatoType getStato() {
        return stato;
    }

    /**
     * Imposta il valore della proprietà stato.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumVoceStatoType }
     *     
     */
    public void setStato(EnumVoceStatoType value) {
        this.stato = value;
    }

    /**
     * Recupera il valore della proprietà indiceDiClassificazioneEsteso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndiceDiClassificazioneEsteso() {
        return indiceDiClassificazioneEsteso;
    }

    /**
     * Imposta il valore della proprietà indiceDiClassificazioneEsteso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndiceDiClassificazioneEsteso(String value) {
        this.indiceDiClassificazioneEsteso = value;
    }

    /**
     * Recupera il valore della proprietà descrBreve.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrBreve() {
        return descrBreve;
    }

    /**
     * Imposta il valore della proprietà descrBreve.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrBreve(String value) {
        this.descrBreve = value;
    }

    /**
     * Recupera il valore della proprietà dataCreazione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataCreazione() {
        return dataCreazione;
    }

    /**
     * Imposta il valore della proprietà dataCreazione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataCreazione(XMLGregorianCalendar value) {
        this.dataCreazione = value;
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
     * Recupera il valore della proprietà presenzaFascStand.
     * 
     */
    public boolean isPresenzaFascStand() {
        return presenzaFascStand;
    }

    /**
     * Imposta il valore della proprietà presenzaFascStand.
     * 
     */
    public void setPresenzaFascStand(boolean value) {
        this.presenzaFascStand = value;
    }

    /**
     * Recupera il valore della proprietà presenzaFascRealeAnnualeNV.
     * 
     */
    public boolean isPresenzaFascRealeAnnualeNV() {
        return presenzaFascRealeAnnualeNV;
    }

    /**
     * Imposta il valore della proprietà presenzaFascRealeAnnualeNV.
     * 
     */
    public void setPresenzaFascRealeAnnualeNV(boolean value) {
        this.presenzaFascRealeAnnualeNV = value;
    }

    /**
     * Recupera il valore della proprietà presenzaFascRealeContinuoNV.
     * 
     */
    public boolean isPresenzaFascRealeContinuoNV() {
        return presenzaFascRealeContinuoNV;
    }

    /**
     * Imposta il valore della proprietà presenzaFascRealeContinuoNV.
     * 
     */
    public void setPresenzaFascRealeContinuoNV(boolean value) {
        this.presenzaFascRealeContinuoNV = value;
    }

    /**
     * Recupera il valore della proprietà presenzaFascRealeLegislaturaNV.
     * 
     */
    public boolean isPresenzaFascRealeLegislaturaNV() {
        return presenzaFascRealeLegislaturaNV;
    }

    /**
     * Imposta il valore della proprietà presenzaFascRealeLegislaturaNV.
     * 
     */
    public void setPresenzaFascRealeLegislaturaNV(boolean value) {
        this.presenzaFascRealeLegislaturaNV = value;
    }

    /**
     * Recupera il valore della proprietà presenzaFascRealeEreditatoNV.
     * 
     */
    public boolean isPresenzaFascRealeEreditatoNV() {
        return presenzaFascRealeEreditatoNV;
    }

    /**
     * Imposta il valore della proprietà presenzaFascRealeEreditatoNV.
     * 
     */
    public void setPresenzaFascRealeEreditatoNV(boolean value) {
        this.presenzaFascRealeEreditatoNV = value;
    }

    /**
     * Recupera il valore della proprietà presenzaFascRealeLiberoNV.
     * 
     */
    public boolean isPresenzaFascRealeLiberoNV() {
        return presenzaFascRealeLiberoNV;
    }

    /**
     * Imposta il valore della proprietà presenzaFascRealeLiberoNV.
     * 
     */
    public void setPresenzaFascRealeLiberoNV(boolean value) {
        this.presenzaFascRealeLiberoNV = value;
    }

    /**
     * Recupera il valore della proprietà presenzaFascTemp.
     * 
     */
    public boolean isPresenzaFascTemp() {
        return presenzaFascTemp;
    }

    /**
     * Imposta il valore della proprietà presenzaFascTemp.
     * 
     */
    public void setPresenzaFascTemp(boolean value) {
        this.presenzaFascTemp = value;
    }

    /**
     * Recupera il valore della proprietà presenzaSerieDoc.
     * 
     */
    public boolean isPresenzaSerieDoc() {
        return presenzaSerieDoc;
    }

    /**
     * Imposta il valore della proprietà presenzaSerieDoc.
     * 
     */
    public void setPresenzaSerieDoc(boolean value) {
        this.presenzaSerieDoc = value;
    }

    /**
     * Recupera il valore della proprietà presenzaSerieFasc.
     * 
     */
    public boolean isPresenzaSerieFasc() {
        return presenzaSerieFasc;
    }

    /**
     * Imposta il valore della proprietà presenzaSerieFasc.
     * 
     */
    public void setPresenzaSerieFasc(boolean value) {
        this.presenzaSerieFasc = value;
    }

    /**
     * Recupera il valore della proprietà presenzaSerieDoss.
     * 
     */
    public boolean isPresenzaSerieDoss() {
        return presenzaSerieDoss;
    }

    /**
     * Imposta il valore della proprietà presenzaSerieDoss.
     * 
     */
    public void setPresenzaSerieDoss(boolean value) {
        this.presenzaSerieDoss = value;
    }

    /**
     * Recupera il valore della proprietà insertSottoVociGestCont.
     * 
     */
    public boolean isInsertSottoVociGestCont() {
        return insertSottoVociGestCont;
    }

    /**
     * Imposta il valore della proprietà insertSottoVociGestCont.
     * 
     */
    public void setInsertSottoVociGestCont(boolean value) {
        this.insertSottoVociGestCont = value;
    }

    /**
     * Recupera il valore della proprietà creataGestCont.
     * 
     */
    public boolean isCreataGestCont() {
        return creataGestCont;
    }

    /**
     * Imposta il valore della proprietà creataGestCont.
     * 
     */
    public void setCreataGestCont(boolean value) {
        this.creataGestCont = value;
    }

    /**
     * Recupera il valore della proprietà paroleChiave.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParoleChiave() {
        return paroleChiave;
    }

    /**
     * Imposta il valore della proprietà paroleChiave.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParoleChiave(String value) {
        this.paroleChiave = value;
    }

    /**
     * Recupera il valore della proprietà dataUltimaModifica.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    /**
     * Imposta il valore della proprietà dataUltimaModifica.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataUltimaModifica(XMLGregorianCalendar value) {
        this.dataUltimaModifica = value;
    }

    /**
     * Recupera il valore della proprietà conservazioneCorrente.
     * 
     */
    public int getConservazioneCorrente() {
        return conservazioneCorrente;
    }

    /**
     * Imposta il valore della proprietà conservazioneCorrente.
     * 
     */
    public void setConservazioneCorrente(int value) {
        this.conservazioneCorrente = value;
    }

    /**
     * Recupera il valore della proprietà conservazioneGenerale.
     * 
     */
    public int getConservazioneGenerale() {
        return conservazioneGenerale;
    }

    /**
     * Imposta il valore della proprietà conservazioneGenerale.
     * 
     */
    public void setConservazioneGenerale(int value) {
        this.conservazioneGenerale = value;
    }

    /**
     * Recupera il valore della proprietà dataBloccoPassaggioInDeposito.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataBloccoPassaggioInDeposito() {
        return dataBloccoPassaggioInDeposito;
    }

    /**
     * Imposta il valore della proprietà dataBloccoPassaggioInDeposito.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataBloccoPassaggioInDeposito(XMLGregorianCalendar value) {
        this.dataBloccoPassaggioInDeposito = value;
    }

    /**
     * Recupera il valore della proprietà dataSbloccoPassaggioInDeposito.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataSbloccoPassaggioInDeposito() {
        return dataSbloccoPassaggioInDeposito;
    }

    /**
     * Imposta il valore della proprietà dataSbloccoPassaggioInDeposito.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataSbloccoPassaggioInDeposito(XMLGregorianCalendar value) {
        this.dataSbloccoPassaggioInDeposito = value;
    }

    /**
     * Recupera il valore della proprietà dataCancellazione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataCancellazione() {
        return dataCancellazione;
    }

    /**
     * Imposta il valore della proprietà dataCancellazione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataCancellazione(XMLGregorianCalendar value) {
        this.dataCancellazione = value;
    }

    /**
     * Recupera il valore della proprietà dataEsportazione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataEsportazione() {
        return dataEsportazione;
    }

    /**
     * Imposta il valore della proprietà dataEsportazione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataEsportazione(XMLGregorianCalendar value) {
        this.dataEsportazione = value;
    }

    /**
     * Gets the value of the idFascicoloStandardList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idFascicoloStandardList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdFascicoloStandardList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IdFascicoloStandardType }
     * 
     * 
     */
    public List<IdFascicoloStandardType> getIdFascicoloStandardList() {
        if (idFascicoloStandardList == null) {
            idFascicoloStandardList = new ArrayList<IdFascicoloStandardType>();
        }
        return this.idFascicoloStandardList;
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
