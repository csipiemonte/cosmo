
package it.doqui.acta.acaris.archive;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import it.doqui.acta.acaris.common.CodiceFiscaleType;
import it.doqui.acta.acaris.common.IdAOOType;
import it.doqui.acta.acaris.common.IdAnnotazioniType;
import it.doqui.acta.acaris.common.IdMovimentazioneType;
import it.doqui.acta.acaris.common.IdNodoType;
import it.doqui.acta.acaris.common.IdStrutturaType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per AggregazionePropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="AggregazionePropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{archive.acaris.acta.doqui.it}FolderPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="codice" type="{archive.acaris.acta.doqui.it}CodiceType"/>
 *         &lt;element name="descrizione" type="{archive.acaris.acta.doqui.it}DescrizioneType"/>
 *         &lt;element name="indiceDiClassificazioneEsteso" type="{archive.acaris.acta.doqui.it}IndiceClassificazioneEstesaXMLType"/>
 *         &lt;element name="dataCreazione" type="{archive.acaris.acta.doqui.it}DataCreazioneType"/>
 *         &lt;element name="dataInizio" type="{archive.acaris.acta.doqui.it}DataInizioType"/>
 *         &lt;element name="dataFine" type="{archive.acaris.acta.doqui.it}DataFineType"/>
 *         &lt;element name="conservazioneCorrente" type="{archive.acaris.acta.doqui.it}ConservazioneCorrenteType"/>
 *         &lt;element name="conservazioneGenerale" type="{archive.acaris.acta.doqui.it}ConservazioneGeneraleType"/>
 *         &lt;element name="dataPassaggioInDeposito" type="{archive.acaris.acta.doqui.it}DataPassaggioInDepositoType"/>
 *         &lt;element name="dataBloccoPassaggioInDeposito" type="{archive.acaris.acta.doqui.it}DataBloccoPassaggioInDepositoType"/>
 *         &lt;element name="dataSbloccoPassaggioInDeposito" type="{archive.acaris.acta.doqui.it}DataSbloccoPassaggioInDepositoType"/>
 *         &lt;element name="dataScarto" type="{archive.acaris.acta.doqui.it}DataScartoType"/>
 *         &lt;element name="archivioCorrente" type="{archive.acaris.acta.doqui.it}ArchivioCorrenteType"/>
 *         &lt;element name="datiPersonali" type="{archive.acaris.acta.doqui.it}DatiPersonaliType"/>
 *         &lt;element name="datiSensibili" type="{archive.acaris.acta.doqui.it}DatiSensibiliType"/>
 *         &lt;element name="datiRiservati" type="{archive.acaris.acta.doqui.it}DatiRiservatiType"/>
 *         &lt;element name="collocazioneCartaceo" type="{archive.acaris.acta.doqui.it}CollocazioneCartaceoType"/>
 *         &lt;element name="paroleChiave" type="{archive.acaris.acta.doqui.it}ParoleChiaveType"/>
 *         &lt;element name="dataUltimaModifica" type="{archive.acaris.acta.doqui.it}DataUltimaModificaType"/>
 *         &lt;element name="dataCancellazione" type="{archive.acaris.acta.doqui.it}DataCancellazioneType"/>
 *         &lt;element name="dataEsportazione" type="{archive.acaris.acta.doqui.it}DataEsportazioneType"/>
 *         &lt;element name="estremiProvvedimentoDiScarto" type="{archive.acaris.acta.doqui.it}EstremiProvvedimentoDiScartoType"/>
 *         &lt;element name="utenteCreazione" type="{common.acaris.acta.doqui.it}CodiceFiscaleType"/>
 *         &lt;element name="idDeposito" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idMovimentazioneList" type="{common.acaris.acta.doqui.it}IdMovimentazioneType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idAnnotazioniList" type="{common.acaris.acta.doqui.it}IdAnnotazioniType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idTipoClasse" type="{archive.acaris.acta.doqui.it}IdTipoClasseType"/>
 *         &lt;element name="idAOORespMat" type="{common.acaris.acta.doqui.it}IdAOOType"/>
 *         &lt;element name="idAOORespCons" type="{common.acaris.acta.doqui.it}IdAOOType"/>
 *         &lt;element name="idStrutturaRespMat" type="{common.acaris.acta.doqui.it}IdStrutturaType"/>
 *         &lt;element name="idStrutturaRespCons" type="{common.acaris.acta.doqui.it}IdStrutturaType"/>
 *         &lt;element name="idNodoRespMat" type="{common.acaris.acta.doqui.it}IdNodoType"/>
 *         &lt;element name="idNodoRespCons" type="{common.acaris.acta.doqui.it}IdNodoType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AggregazionePropertiesType", propOrder = {
    "codice",
    "descrizione",
    "indiceDiClassificazioneEsteso",
    "dataCreazione",
    "dataInizio",
    "dataFine",
    "conservazioneCorrente",
    "conservazioneGenerale",
    "dataPassaggioInDeposito",
    "dataBloccoPassaggioInDeposito",
    "dataSbloccoPassaggioInDeposito",
    "dataScarto",
    "archivioCorrente",
    "datiPersonali",
    "datiSensibili",
    "datiRiservati",
    "collocazioneCartaceo",
    "paroleChiave",
    "dataUltimaModifica",
    "dataCancellazione",
    "dataEsportazione",
    "estremiProvvedimentoDiScarto",
    "utenteCreazione",
    "idDeposito",
    "idMovimentazioneList",
    "idAnnotazioniList",
    "idTipoClasse",
    "idAOORespMat",
    "idAOORespCons",
    "idStrutturaRespMat",
    "idStrutturaRespCons",
    "idNodoRespMat",
    "idNodoRespCons"
})
@XmlSeeAlso({
    DossierPropertiesType.class,
    SottofascicoloPropertiesType.class,
    FascicoloRealePropertiesType.class,
    VolumePropertiesType.class,
    SeriePropertiesType.class
})
public abstract class AggregazionePropertiesType
    extends FolderPropertiesType
{

    @XmlElement(required = true)
    protected String codice;
    @XmlElement(required = true)
    protected String descrizione;
    @XmlElement(required = true)
    protected String indiceDiClassificazioneEsteso;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataCreazione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataInizio;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataFine;
    protected int conservazioneCorrente;
    protected int conservazioneGenerale;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataPassaggioInDeposito;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataBloccoPassaggioInDeposito;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataSbloccoPassaggioInDeposito;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataScarto;
    protected boolean archivioCorrente;
    protected boolean datiPersonali;
    protected boolean datiSensibili;
    protected boolean datiRiservati;
    @XmlElement(required = true)
    protected String collocazioneCartaceo;
    @XmlElement(required = true)
    protected String paroleChiave;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataUltimaModifica;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataCancellazione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataEsportazione;
    @XmlElement(required = true)
    protected String estremiProvvedimentoDiScarto;
    @XmlElement(required = true)
    protected CodiceFiscaleType utenteCreazione;
    @XmlElement(required = true)
    protected ObjectIdType idDeposito;
    protected List<IdMovimentazioneType> idMovimentazioneList;
    protected List<IdAnnotazioniType> idAnnotazioniList;
    protected int idTipoClasse;
    @XmlElement(required = true)
    protected IdAOOType idAOORespMat;
    @XmlElement(required = true)
    protected IdAOOType idAOORespCons;
    @XmlElement(required = true)
    protected IdStrutturaType idStrutturaRespMat;
    @XmlElement(required = true)
    protected IdStrutturaType idStrutturaRespCons;
    @XmlElement(required = true)
    protected IdNodoType idNodoRespMat;
    @XmlElement(required = true)
    protected IdNodoType idNodoRespCons;

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
     * Recupera il valore della proprietà dataPassaggioInDeposito.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataPassaggioInDeposito() {
        return dataPassaggioInDeposito;
    }

    /**
     * Imposta il valore della proprietà dataPassaggioInDeposito.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataPassaggioInDeposito(XMLGregorianCalendar value) {
        this.dataPassaggioInDeposito = value;
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
     * Recupera il valore della proprietà dataScarto.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataScarto() {
        return dataScarto;
    }

    /**
     * Imposta il valore della proprietà dataScarto.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataScarto(XMLGregorianCalendar value) {
        this.dataScarto = value;
    }

    /**
     * Recupera il valore della proprietà archivioCorrente.
     * 
     */
    public boolean isArchivioCorrente() {
        return archivioCorrente;
    }

    /**
     * Imposta il valore della proprietà archivioCorrente.
     * 
     */
    public void setArchivioCorrente(boolean value) {
        this.archivioCorrente = value;
    }

    /**
     * Recupera il valore della proprietà datiPersonali.
     * 
     */
    public boolean isDatiPersonali() {
        return datiPersonali;
    }

    /**
     * Imposta il valore della proprietà datiPersonali.
     * 
     */
    public void setDatiPersonali(boolean value) {
        this.datiPersonali = value;
    }

    /**
     * Recupera il valore della proprietà datiSensibili.
     * 
     */
    public boolean isDatiSensibili() {
        return datiSensibili;
    }

    /**
     * Imposta il valore della proprietà datiSensibili.
     * 
     */
    public void setDatiSensibili(boolean value) {
        this.datiSensibili = value;
    }

    /**
     * Recupera il valore della proprietà datiRiservati.
     * 
     */
    public boolean isDatiRiservati() {
        return datiRiservati;
    }

    /**
     * Imposta il valore della proprietà datiRiservati.
     * 
     */
    public void setDatiRiservati(boolean value) {
        this.datiRiservati = value;
    }

    /**
     * Recupera il valore della proprietà collocazioneCartaceo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollocazioneCartaceo() {
        return collocazioneCartaceo;
    }

    /**
     * Imposta il valore della proprietà collocazioneCartaceo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollocazioneCartaceo(String value) {
        this.collocazioneCartaceo = value;
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
     * Recupera il valore della proprietà estremiProvvedimentoDiScarto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstremiProvvedimentoDiScarto() {
        return estremiProvvedimentoDiScarto;
    }

    /**
     * Imposta il valore della proprietà estremiProvvedimentoDiScarto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstremiProvvedimentoDiScarto(String value) {
        this.estremiProvvedimentoDiScarto = value;
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
     * Recupera il valore della proprietà idDeposito.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdDeposito() {
        return idDeposito;
    }

    /**
     * Imposta il valore della proprietà idDeposito.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdDeposito(ObjectIdType value) {
        this.idDeposito = value;
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

    /**
     * Recupera il valore della proprietà idAOORespMat.
     * 
     * @return
     *     possible object is
     *     {@link IdAOOType }
     *     
     */
    public IdAOOType getIdAOORespMat() {
        return idAOORespMat;
    }

    /**
     * Imposta il valore della proprietà idAOORespMat.
     * 
     * @param value
     *     allowed object is
     *     {@link IdAOOType }
     *     
     */
    public void setIdAOORespMat(IdAOOType value) {
        this.idAOORespMat = value;
    }

    /**
     * Recupera il valore della proprietà idAOORespCons.
     * 
     * @return
     *     possible object is
     *     {@link IdAOOType }
     *     
     */
    public IdAOOType getIdAOORespCons() {
        return idAOORespCons;
    }

    /**
     * Imposta il valore della proprietà idAOORespCons.
     * 
     * @param value
     *     allowed object is
     *     {@link IdAOOType }
     *     
     */
    public void setIdAOORespCons(IdAOOType value) {
        this.idAOORespCons = value;
    }

    /**
     * Recupera il valore della proprietà idStrutturaRespMat.
     * 
     * @return
     *     possible object is
     *     {@link IdStrutturaType }
     *     
     */
    public IdStrutturaType getIdStrutturaRespMat() {
        return idStrutturaRespMat;
    }

    /**
     * Imposta il valore della proprietà idStrutturaRespMat.
     * 
     * @param value
     *     allowed object is
     *     {@link IdStrutturaType }
     *     
     */
    public void setIdStrutturaRespMat(IdStrutturaType value) {
        this.idStrutturaRespMat = value;
    }

    /**
     * Recupera il valore della proprietà idStrutturaRespCons.
     * 
     * @return
     *     possible object is
     *     {@link IdStrutturaType }
     *     
     */
    public IdStrutturaType getIdStrutturaRespCons() {
        return idStrutturaRespCons;
    }

    /**
     * Imposta il valore della proprietà idStrutturaRespCons.
     * 
     * @param value
     *     allowed object is
     *     {@link IdStrutturaType }
     *     
     */
    public void setIdStrutturaRespCons(IdStrutturaType value) {
        this.idStrutturaRespCons = value;
    }

    /**
     * Recupera il valore della proprietà idNodoRespMat.
     * 
     * @return
     *     possible object is
     *     {@link IdNodoType }
     *     
     */
    public IdNodoType getIdNodoRespMat() {
        return idNodoRespMat;
    }

    /**
     * Imposta il valore della proprietà idNodoRespMat.
     * 
     * @param value
     *     allowed object is
     *     {@link IdNodoType }
     *     
     */
    public void setIdNodoRespMat(IdNodoType value) {
        this.idNodoRespMat = value;
    }

    /**
     * Recupera il valore della proprietà idNodoRespCons.
     * 
     * @return
     *     possible object is
     *     {@link IdNodoType }
     *     
     */
    public IdNodoType getIdNodoRespCons() {
        return idNodoRespCons;
    }

    /**
     * Imposta il valore della proprietà idNodoRespCons.
     * 
     * @param value
     *     allowed object is
     *     {@link IdNodoType }
     *     
     */
    public void setIdNodoRespCons(IdNodoType value) {
        this.idNodoRespCons = value;
    }

}
