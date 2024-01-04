
package it.eng.auriga.repository2.webservices.addunitadoc.dto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="NomeUD" type="{}NomeUDType" minOccurs="0"/>
 *         &lt;element name="RegistrazioneData" type="{}RegistrazioneNumerazioneType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RegistrazioneDaDare" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="CategoriaReg">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;enumeration value="PG"/>
 *                         &lt;enumeration value="PP"/>
 *                         &lt;enumeration value="R"/>
 *                         &lt;enumeration value="I"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="SiglaReg" type="{}SiglaRegNumType" minOccurs="0"/>
 *                   &lt;element name="AnnoReg" type="{}AnnoType" minOccurs="0"/>
 *                 &lt;/all>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="OggettoUD" type="{}OggettoUDType" minOccurs="0"/>
 *         &lt;element name="TipoDoc" type="{}OggDiTabDiSistemaType" minOccurs="0"/>
 *         &lt;element name="OriginaleCartaceo" type="{}FlagSiNoType" minOccurs="0"/>
 *         &lt;element name="TipoCartaceo" type="{}TipoCartaceoType" minOccurs="0"/>
 *         &lt;element name="VersioneElettronica" type="{}VersioneElettronicaType" minOccurs="0"/>
 *         &lt;element name="TipoProvenienza" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="E|U|I"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NroAllegati" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="DatiEntrata" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="DataOraArrivo" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *                     &lt;element name="DataDocRicevuto" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                     &lt;element name="EstremiRegistrazioneDocRicevuto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                     &lt;element name="RiferimentiDocRicevuto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                     &lt;element name="MezzoTrasmissione" type="{}OggDiTabDiSistemaType" minOccurs="0"/>
 *                     &lt;element name="DataRaccomandata" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                     &lt;element name="NroRaccomandata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                     &lt;element name="MittenteEsterno" type="{}SoggettoEsternoType" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element name="FirmatarioEsterno" type="{}SoggettoEsternoType" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element name="IndirizzoProv" type="{}IndirizzoType" minOccurs="0"/>
 *                     &lt;element name="IndirizzoEmailProv" type="{}EmailType" minOccurs="0"/>
 *                     &lt;element name="UtenteRicezione" type="{}UserType" minOccurs="0"/>
 *                     &lt;element name="UffRicezione" type="{}UOType" minOccurs="0"/>
 *                     &lt;element name="UtenteCtrlAmmissib" type="{}UserType" minOccurs="0"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;sequence minOccurs="0">
 *             &lt;element name="DatiProduzione" minOccurs="0">
 *               &lt;complexType>
 *                 &lt;complexContent>
 *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                     &lt;sequence>
 *                       &lt;element name="DataStesura" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                       &lt;element name="LuogoStesura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                       &lt;element name="Estensore" type="{}UserType" maxOccurs="unbounded" minOccurs="0"/>
 *                       &lt;element name="UffProduttore" type="{}UOType" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;/sequence>
 *                   &lt;/restriction>
 *                 &lt;/complexContent>
 *               &lt;/complexType>
 *             &lt;/element>
 *             &lt;element name="DatiUscita" minOccurs="0">
 *               &lt;complexType>
 *                 &lt;complexContent>
 *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                     &lt;sequence>
 *                       &lt;element name="DataOraSped" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *                       &lt;element name="MezzoTrasmissione" type="{}OggDiTabDiSistemaType" minOccurs="0"/>
 *                       &lt;element name="DataRaccomandata" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                       &lt;element name="NroRaccomandata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                       &lt;element name="DestinatarioEsterno" type="{}DestinatarioEsternoType" maxOccurs="unbounded" minOccurs="0"/>
 *                       &lt;element name="UtenteSpedizione" type="{}UserType" minOccurs="0"/>
 *                       &lt;element name="UffSpedizione" type="{}UOType" minOccurs="0"/>
 *                     &lt;/sequence>
 *                   &lt;/restriction>
 *                 &lt;/complexContent>
 *               &lt;/complexType>
 *             &lt;/element>
 *           &lt;/sequence>
 *         &lt;/choice>
 *         &lt;element name="AltroSoggEsterno" type="{}SoggettoEstEstesoType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="AssegnazioneInterna" type="{}AssegnazioneInternaType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CopiaDaTenereA" type="{}SoggettoInternoType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="AltroSoggettoInterno" type="{}SoggettoInternoEstesoType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CollocazioneClassificazioneUD" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="LibreriaUD" type="{}LibreriaType" minOccurs="0"/>
 *                   &lt;element name="ClassifFascicolo" type="{}ClassifFascicoloType" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="Workspace" type="{}OggDiTabDiSistemaType" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AllegatoUD" type="{}AllegatoUDType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="InRispostaAUD" type="{}EstremiRegNumType" minOccurs="0"/>
 *         &lt;element name="LivelloRiservatezzaInterno" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="FlagVietatoAccessoEsterno" type="{}FlagSiNoType" minOccurs="0"/>
 *         &lt;element name="VietatoAccessoEsternoFinoAl" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="PermessoVsSoggInterno" type="{}ACLRecordType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="FlagEreditaPermessi" type="{}FlagSiNoType" minOccurs="0"/>
 *         &lt;element name="EreditaPermessiDa" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="FolderPath" type="{}FolderCustomType"/>
 *                   &lt;element name="NomeWorkspace" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="LivelloEvidenza" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="Conservazione" type="{}InfoConservazioneType" minOccurs="0"/>
 *         &lt;element name="Stato" type="{}OggDiTabDiSistemaType" minOccurs="0"/>
 *         &lt;element name="StatoDettaglio" type="{}OggDiTabDiSistemaType" minOccurs="0"/>
 *         &lt;element name="NoteUD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AttributoAddUD" type="{}AttributoAddizionaleType" maxOccurs="unbounded" minOccurs="0"/>
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
    "nomeUD",
    "registrazioneData",
    "registrazioneDaDare",
    "oggettoUD",
    "tipoDoc",
    "originaleCartaceo",
    "tipoCartaceo",
    "versioneElettronica",
    "tipoProvenienza",
    "nroAllegati",
    "datiEntrata",
    "datiProduzione",
    "datiUscita",
    "altroSoggEsterno",
    "assegnazioneInterna",
    "copiaDaTenereA",
    "altroSoggettoInterno",
    "collocazioneClassificazioneUD",
    "allegatoUD",
    "inRispostaAUD",
    "livelloRiservatezzaInterno",
    "flagVietatoAccessoEsterno",
    "vietatoAccessoEsternoFinoAl",
    "permessoVsSoggInterno",
    "flagEreditaPermessi",
    "ereditaPermessiDa",
    "livelloEvidenza",
    "conservazione",
    "stato",
    "statoDettaglio",
    "noteUD",
    "attributoAddUD"
})
@XmlRootElement(name = "NewUD")
public class NewUD {

    @XmlElement(name = "NomeUD")
    protected String nomeUD;
    @XmlElement(name = "RegistrazioneData")
    protected List<RegistrazioneNumerazioneType> registrazioneData;
    @XmlElement(name = "RegistrazioneDaDare")
    protected List<NewUD.RegistrazioneDaDare> registrazioneDaDare;
    @XmlElement(name = "OggettoUD")
    protected String oggettoUD;
    @XmlElement(name = "TipoDoc")
    protected OggDiTabDiSistemaType tipoDoc;
    @XmlElement(name = "OriginaleCartaceo")
    protected String originaleCartaceo;
    @XmlElement(name = "TipoCartaceo")
    protected String tipoCartaceo;
    @XmlElement(name = "VersioneElettronica")
    protected VersioneElettronicaType versioneElettronica;
    @XmlElement(name = "TipoProvenienza", defaultValue = "I")
    protected String tipoProvenienza;
    @XmlElement(name = "NroAllegati")
    protected BigInteger nroAllegati;
    @XmlElement(name = "DatiEntrata")
    protected NewUD.DatiEntrata datiEntrata;
    @XmlElement(name = "DatiProduzione")
    protected NewUD.DatiProduzione datiProduzione;
    @XmlElement(name = "DatiUscita")
    protected NewUD.DatiUscita datiUscita;
    @XmlElement(name = "AltroSoggEsterno")
    protected List<SoggettoEstEstesoType> altroSoggEsterno;
    @XmlElement(name = "AssegnazioneInterna")
    protected List<AssegnazioneInternaType> assegnazioneInterna;
    @XmlElement(name = "CopiaDaTenereA")
    protected List<SoggettoInternoType> copiaDaTenereA;
    @XmlElement(name = "AltroSoggettoInterno")
    protected List<SoggettoInternoEstesoType> altroSoggettoInterno;
    @XmlElement(name = "CollocazioneClassificazioneUD")
    protected NewUD.CollocazioneClassificazioneUD collocazioneClassificazioneUD;
    @XmlElement(name = "AllegatoUD")
    protected List<AllegatoUDType> allegatoUD;
    @XmlElement(name = "InRispostaAUD")
    protected EstremiRegNumType inRispostaAUD;
    @XmlElement(name = "LivelloRiservatezzaInterno")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger livelloRiservatezzaInterno;
    @XmlElement(name = "FlagVietatoAccessoEsterno")
    protected String flagVietatoAccessoEsterno;
    @XmlElement(name = "VietatoAccessoEsternoFinoAl")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar vietatoAccessoEsternoFinoAl;
    @XmlElement(name = "PermessoVsSoggInterno")
    protected List<ACLRecordType> permessoVsSoggInterno;
    @XmlElement(name = "FlagEreditaPermessi", defaultValue = "1")
    protected String flagEreditaPermessi;
    @XmlElement(name = "EreditaPermessiDa")
    protected NewUD.EreditaPermessiDa ereditaPermessiDa;
    @XmlElement(name = "LivelloEvidenza")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger livelloEvidenza;
    @XmlElement(name = "Conservazione")
    protected InfoConservazioneType conservazione;
    @XmlElement(name = "Stato")
    protected OggDiTabDiSistemaType stato;
    @XmlElement(name = "StatoDettaglio")
    protected OggDiTabDiSistemaType statoDettaglio;
    @XmlElement(name = "NoteUD")
    protected String noteUD;
    @XmlElement(name = "AttributoAddUD")
    protected List<AttributoAddizionaleType> attributoAddUD;

    /**
     * Recupera il valore della proprietà nomeUD.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeUD() {
        return nomeUD;
    }

    /**
     * Imposta il valore della proprietà nomeUD.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeUD(String value) {
        this.nomeUD = value;
    }

    /**
     * Gets the value of the registrazioneData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the registrazioneData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegistrazioneData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegistrazioneNumerazioneType }
     * 
     * 
     */
    public List<RegistrazioneNumerazioneType> getRegistrazioneData() {
        if (registrazioneData == null) {
            registrazioneData = new ArrayList<RegistrazioneNumerazioneType>();
        }
        return this.registrazioneData;
    }

    /**
     * Gets the value of the registrazioneDaDare property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the registrazioneDaDare property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegistrazioneDaDare().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NewUD.RegistrazioneDaDare }
     * 
     * 
     */
    public List<NewUD.RegistrazioneDaDare> getRegistrazioneDaDare() {
        if (registrazioneDaDare == null) {
            registrazioneDaDare = new ArrayList<NewUD.RegistrazioneDaDare>();
        }
        return this.registrazioneDaDare;
    }

    /**
     * Recupera il valore della proprietà oggettoUD.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOggettoUD() {
        return oggettoUD;
    }

    /**
     * Imposta il valore della proprietà oggettoUD.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOggettoUD(String value) {
        this.oggettoUD = value;
    }

    /**
     * Recupera il valore della proprietà tipoDoc.
     * 
     * @return
     *     possible object is
     *     {@link OggDiTabDiSistemaType }
     *     
     */
    public OggDiTabDiSistemaType getTipoDoc() {
        return tipoDoc;
    }

    /**
     * Imposta il valore della proprietà tipoDoc.
     * 
     * @param value
     *     allowed object is
     *     {@link OggDiTabDiSistemaType }
     *     
     */
    public void setTipoDoc(OggDiTabDiSistemaType value) {
        this.tipoDoc = value;
    }

    /**
     * Recupera il valore della proprietà originaleCartaceo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginaleCartaceo() {
        return originaleCartaceo;
    }

    /**
     * Imposta il valore della proprietà originaleCartaceo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginaleCartaceo(String value) {
        this.originaleCartaceo = value;
    }

    /**
     * Recupera il valore della proprietà tipoCartaceo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCartaceo() {
        return tipoCartaceo;
    }

    /**
     * Imposta il valore della proprietà tipoCartaceo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCartaceo(String value) {
        this.tipoCartaceo = value;
    }

    /**
     * Recupera il valore della proprietà versioneElettronica.
     * 
     * @return
     *     possible object is
     *     {@link VersioneElettronicaType }
     *     
     */
    public VersioneElettronicaType getVersioneElettronica() {
        return versioneElettronica;
    }

    /**
     * Imposta il valore della proprietà versioneElettronica.
     * 
     * @param value
     *     allowed object is
     *     {@link VersioneElettronicaType }
     *     
     */
    public void setVersioneElettronica(VersioneElettronicaType value) {
        this.versioneElettronica = value;
    }

    /**
     * Recupera il valore della proprietà tipoProvenienza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoProvenienza() {
        return tipoProvenienza;
    }

    /**
     * Imposta il valore della proprietà tipoProvenienza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoProvenienza(String value) {
        this.tipoProvenienza = value;
    }

    /**
     * Recupera il valore della proprietà nroAllegati.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNroAllegati() {
        return nroAllegati;
    }

    /**
     * Imposta il valore della proprietà nroAllegati.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNroAllegati(BigInteger value) {
        this.nroAllegati = value;
    }

    /**
     * Recupera il valore della proprietà datiEntrata.
     * 
     * @return
     *     possible object is
     *     {@link NewUD.DatiEntrata }
     *     
     */
    public NewUD.DatiEntrata getDatiEntrata() {
        return datiEntrata;
    }

    /**
     * Imposta il valore della proprietà datiEntrata.
     * 
     * @param value
     *     allowed object is
     *     {@link NewUD.DatiEntrata }
     *     
     */
    public void setDatiEntrata(NewUD.DatiEntrata value) {
        this.datiEntrata = value;
    }

    /**
     * Recupera il valore della proprietà datiProduzione.
     * 
     * @return
     *     possible object is
     *     {@link NewUD.DatiProduzione }
     *     
     */
    public NewUD.DatiProduzione getDatiProduzione() {
        return datiProduzione;
    }

    /**
     * Imposta il valore della proprietà datiProduzione.
     * 
     * @param value
     *     allowed object is
     *     {@link NewUD.DatiProduzione }
     *     
     */
    public void setDatiProduzione(NewUD.DatiProduzione value) {
        this.datiProduzione = value;
    }

    /**
     * Recupera il valore della proprietà datiUscita.
     * 
     * @return
     *     possible object is
     *     {@link NewUD.DatiUscita }
     *     
     */
    public NewUD.DatiUscita getDatiUscita() {
        return datiUscita;
    }

    /**
     * Imposta il valore della proprietà datiUscita.
     * 
     * @param value
     *     allowed object is
     *     {@link NewUD.DatiUscita }
     *     
     */
    public void setDatiUscita(NewUD.DatiUscita value) {
        this.datiUscita = value;
    }

    /**
     * Gets the value of the altroSoggEsterno property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the altroSoggEsterno property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAltroSoggEsterno().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SoggettoEstEstesoType }
     * 
     * 
     */
    public List<SoggettoEstEstesoType> getAltroSoggEsterno() {
        if (altroSoggEsterno == null) {
            altroSoggEsterno = new ArrayList<SoggettoEstEstesoType>();
        }
        return this.altroSoggEsterno;
    }

    /**
     * Gets the value of the assegnazioneInterna property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assegnazioneInterna property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssegnazioneInterna().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssegnazioneInternaType }
     * 
     * 
     */
    public List<AssegnazioneInternaType> getAssegnazioneInterna() {
        if (assegnazioneInterna == null) {
            assegnazioneInterna = new ArrayList<AssegnazioneInternaType>();
        }
        return this.assegnazioneInterna;
    }

    /**
     * Gets the value of the copiaDaTenereA property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the copiaDaTenereA property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCopiaDaTenereA().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SoggettoInternoType }
     * 
     * 
     */
    public List<SoggettoInternoType> getCopiaDaTenereA() {
        if (copiaDaTenereA == null) {
            copiaDaTenereA = new ArrayList<SoggettoInternoType>();
        }
        return this.copiaDaTenereA;
    }

    /**
     * Gets the value of the altroSoggettoInterno property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the altroSoggettoInterno property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAltroSoggettoInterno().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SoggettoInternoEstesoType }
     * 
     * 
     */
    public List<SoggettoInternoEstesoType> getAltroSoggettoInterno() {
        if (altroSoggettoInterno == null) {
            altroSoggettoInterno = new ArrayList<SoggettoInternoEstesoType>();
        }
        return this.altroSoggettoInterno;
    }

    /**
     * Recupera il valore della proprietà collocazioneClassificazioneUD.
     * 
     * @return
     *     possible object is
     *     {@link NewUD.CollocazioneClassificazioneUD }
     *     
     */
    public NewUD.CollocazioneClassificazioneUD getCollocazioneClassificazioneUD() {
        return collocazioneClassificazioneUD;
    }

    /**
     * Imposta il valore della proprietà collocazioneClassificazioneUD.
     * 
     * @param value
     *     allowed object is
     *     {@link NewUD.CollocazioneClassificazioneUD }
     *     
     */
    public void setCollocazioneClassificazioneUD(NewUD.CollocazioneClassificazioneUD value) {
        this.collocazioneClassificazioneUD = value;
    }

    /**
     * Gets the value of the allegatoUD property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the allegatoUD property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAllegatoUD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AllegatoUDType }
     * 
     * 
     */
    public List<AllegatoUDType> getAllegatoUD() {
        if (allegatoUD == null) {
            allegatoUD = new ArrayList<AllegatoUDType>();
        }
        return this.allegatoUD;
    }

    /**
     * Recupera il valore della proprietà inRispostaAUD.
     * 
     * @return
     *     possible object is
     *     {@link EstremiRegNumType }
     *     
     */
    public EstremiRegNumType getInRispostaAUD() {
        return inRispostaAUD;
    }

    /**
     * Imposta il valore della proprietà inRispostaAUD.
     * 
     * @param value
     *     allowed object is
     *     {@link EstremiRegNumType }
     *     
     */
    public void setInRispostaAUD(EstremiRegNumType value) {
        this.inRispostaAUD = value;
    }

    /**
     * Recupera il valore della proprietà livelloRiservatezzaInterno.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLivelloRiservatezzaInterno() {
        return livelloRiservatezzaInterno;
    }

    /**
     * Imposta il valore della proprietà livelloRiservatezzaInterno.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLivelloRiservatezzaInterno(BigInteger value) {
        this.livelloRiservatezzaInterno = value;
    }

    /**
     * Recupera il valore della proprietà flagVietatoAccessoEsterno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagVietatoAccessoEsterno() {
        return flagVietatoAccessoEsterno;
    }

    /**
     * Imposta il valore della proprietà flagVietatoAccessoEsterno.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagVietatoAccessoEsterno(String value) {
        this.flagVietatoAccessoEsterno = value;
    }

    /**
     * Recupera il valore della proprietà vietatoAccessoEsternoFinoAl.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVietatoAccessoEsternoFinoAl() {
        return vietatoAccessoEsternoFinoAl;
    }

    /**
     * Imposta il valore della proprietà vietatoAccessoEsternoFinoAl.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVietatoAccessoEsternoFinoAl(XMLGregorianCalendar value) {
        this.vietatoAccessoEsternoFinoAl = value;
    }

    /**
     * Gets the value of the permessoVsSoggInterno property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the permessoVsSoggInterno property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPermessoVsSoggInterno().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ACLRecordType }
     * 
     * 
     */
    public List<ACLRecordType> getPermessoVsSoggInterno() {
        if (permessoVsSoggInterno == null) {
            permessoVsSoggInterno = new ArrayList<ACLRecordType>();
        }
        return this.permessoVsSoggInterno;
    }

    /**
     * Recupera il valore della proprietà flagEreditaPermessi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagEreditaPermessi() {
        return flagEreditaPermessi;
    }

    /**
     * Imposta il valore della proprietà flagEreditaPermessi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagEreditaPermessi(String value) {
        this.flagEreditaPermessi = value;
    }

    /**
     * Recupera il valore della proprietà ereditaPermessiDa.
     * 
     * @return
     *     possible object is
     *     {@link NewUD.EreditaPermessiDa }
     *     
     */
    public NewUD.EreditaPermessiDa getEreditaPermessiDa() {
        return ereditaPermessiDa;
    }

    /**
     * Imposta il valore della proprietà ereditaPermessiDa.
     * 
     * @param value
     *     allowed object is
     *     {@link NewUD.EreditaPermessiDa }
     *     
     */
    public void setEreditaPermessiDa(NewUD.EreditaPermessiDa value) {
        this.ereditaPermessiDa = value;
    }

    /**
     * Recupera il valore della proprietà livelloEvidenza.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLivelloEvidenza() {
        return livelloEvidenza;
    }

    /**
     * Imposta il valore della proprietà livelloEvidenza.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLivelloEvidenza(BigInteger value) {
        this.livelloEvidenza = value;
    }

    /**
     * Recupera il valore della proprietà conservazione.
     * 
     * @return
     *     possible object is
     *     {@link InfoConservazioneType }
     *     
     */
    public InfoConservazioneType getConservazione() {
        return conservazione;
    }

    /**
     * Imposta il valore della proprietà conservazione.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoConservazioneType }
     *     
     */
    public void setConservazione(InfoConservazioneType value) {
        this.conservazione = value;
    }

    /**
     * Recupera il valore della proprietà stato.
     * 
     * @return
     *     possible object is
     *     {@link OggDiTabDiSistemaType }
     *     
     */
    public OggDiTabDiSistemaType getStato() {
        return stato;
    }

    /**
     * Imposta il valore della proprietà stato.
     * 
     * @param value
     *     allowed object is
     *     {@link OggDiTabDiSistemaType }
     *     
     */
    public void setStato(OggDiTabDiSistemaType value) {
        this.stato = value;
    }

    /**
     * Recupera il valore della proprietà statoDettaglio.
     * 
     * @return
     *     possible object is
     *     {@link OggDiTabDiSistemaType }
     *     
     */
    public OggDiTabDiSistemaType getStatoDettaglio() {
        return statoDettaglio;
    }

    /**
     * Imposta il valore della proprietà statoDettaglio.
     * 
     * @param value
     *     allowed object is
     *     {@link OggDiTabDiSistemaType }
     *     
     */
    public void setStatoDettaglio(OggDiTabDiSistemaType value) {
        this.statoDettaglio = value;
    }

    /**
     * Recupera il valore della proprietà noteUD.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoteUD() {
        return noteUD;
    }

    /**
     * Imposta il valore della proprietà noteUD.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoteUD(String value) {
        this.noteUD = value;
    }

    /**
     * Gets the value of the attributoAddUD property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributoAddUD property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributoAddUD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributoAddizionaleType }
     * 
     * 
     */
    public List<AttributoAddizionaleType> getAttributoAddUD() {
        if (attributoAddUD == null) {
            attributoAddUD = new ArrayList<AttributoAddizionaleType>();
        }
        return this.attributoAddUD;
    }


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
     *         &lt;element name="LibreriaUD" type="{}LibreriaType" minOccurs="0"/>
     *         &lt;element name="ClassifFascicolo" type="{}ClassifFascicoloType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="Workspace" type="{}OggDiTabDiSistemaType" maxOccurs="unbounded" minOccurs="0"/>
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
        "libreriaUD",
        "classifFascicolo",
        "workspace"
    })
    public static class CollocazioneClassificazioneUD {

        @XmlElement(name = "LibreriaUD")
        protected String libreriaUD;
        @XmlElement(name = "ClassifFascicolo")
        protected List<ClassifFascicoloType> classifFascicolo;
        @XmlElement(name = "Workspace")
        protected List<OggDiTabDiSistemaType> workspace;

        /**
         * Recupera il valore della proprietà libreriaUD.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLibreriaUD() {
            return libreriaUD;
        }

        /**
         * Imposta il valore della proprietà libreriaUD.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLibreriaUD(String value) {
            this.libreriaUD = value;
        }

        /**
         * Gets the value of the classifFascicolo property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the classifFascicolo property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getClassifFascicolo().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ClassifFascicoloType }
         * 
         * 
         */
        public List<ClassifFascicoloType> getClassifFascicolo() {
            if (classifFascicolo == null) {
                classifFascicolo = new ArrayList<ClassifFascicoloType>();
            }
            return this.classifFascicolo;
        }

        /**
         * Gets the value of the workspace property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the workspace property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getWorkspace().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link OggDiTabDiSistemaType }
         * 
         * 
         */
        public List<OggDiTabDiSistemaType> getWorkspace() {
            if (workspace == null) {
                workspace = new ArrayList<OggDiTabDiSistemaType>();
            }
            return this.workspace;
        }

    }


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
     *         &lt;element name="DataOraArrivo" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
     *         &lt;element name="DataDocRicevuto" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *         &lt;element name="EstremiRegistrazioneDocRicevuto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="RiferimentiDocRicevuto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MezzoTrasmissione" type="{}OggDiTabDiSistemaType" minOccurs="0"/>
     *         &lt;element name="DataRaccomandata" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *         &lt;element name="NroRaccomandata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MittenteEsterno" type="{}SoggettoEsternoType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="FirmatarioEsterno" type="{}SoggettoEsternoType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="IndirizzoProv" type="{}IndirizzoType" minOccurs="0"/>
     *         &lt;element name="IndirizzoEmailProv" type="{}EmailType" minOccurs="0"/>
     *         &lt;element name="UtenteRicezione" type="{}UserType" minOccurs="0"/>
     *         &lt;element name="UffRicezione" type="{}UOType" minOccurs="0"/>
     *         &lt;element name="UtenteCtrlAmmissib" type="{}UserType" minOccurs="0"/>
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
        "dataOraArrivo",
        "dataDocRicevuto",
        "estremiRegistrazioneDocRicevuto",
        "riferimentiDocRicevuto",
        "mezzoTrasmissione",
        "dataRaccomandata",
        "nroRaccomandata",
        "mittenteEsterno",
        "firmatarioEsterno",
        "indirizzoProv",
        "indirizzoEmailProv",
        "utenteRicezione",
        "uffRicezione",
        "utenteCtrlAmmissib"
    })
    public static class DatiEntrata {

        @XmlElement(name = "DataOraArrivo")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataOraArrivo;
        @XmlElement(name = "DataDocRicevuto")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dataDocRicevuto;
        @XmlElement(name = "EstremiRegistrazioneDocRicevuto")
        protected String estremiRegistrazioneDocRicevuto;
        @XmlElement(name = "RiferimentiDocRicevuto")
        protected String riferimentiDocRicevuto;
        @XmlElement(name = "MezzoTrasmissione")
        protected OggDiTabDiSistemaType mezzoTrasmissione;
        @XmlElement(name = "DataRaccomandata")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dataRaccomandata;
        @XmlElement(name = "NroRaccomandata")
        protected String nroRaccomandata;
        @XmlElement(name = "MittenteEsterno")
        protected List<SoggettoEsternoType> mittenteEsterno;
        @XmlElement(name = "FirmatarioEsterno")
        protected List<SoggettoEsternoType> firmatarioEsterno;
        @XmlElement(name = "IndirizzoProv")
        protected IndirizzoType indirizzoProv;
        @XmlElement(name = "IndirizzoEmailProv")
        protected EmailType indirizzoEmailProv;
        @XmlElement(name = "UtenteRicezione")
        protected UserType utenteRicezione;
        @XmlElement(name = "UffRicezione")
        protected UOType uffRicezione;
        @XmlElement(name = "UtenteCtrlAmmissib")
        protected UserType utenteCtrlAmmissib;

        /**
         * Recupera il valore della proprietà dataOraArrivo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataOraArrivo() {
            return dataOraArrivo;
        }

        /**
         * Imposta il valore della proprietà dataOraArrivo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataOraArrivo(XMLGregorianCalendar value) {
            this.dataOraArrivo = value;
        }

        /**
         * Recupera il valore della proprietà dataDocRicevuto.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataDocRicevuto() {
            return dataDocRicevuto;
        }

        /**
         * Imposta il valore della proprietà dataDocRicevuto.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataDocRicevuto(XMLGregorianCalendar value) {
            this.dataDocRicevuto = value;
        }

        /**
         * Recupera il valore della proprietà estremiRegistrazioneDocRicevuto.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEstremiRegistrazioneDocRicevuto() {
            return estremiRegistrazioneDocRicevuto;
        }

        /**
         * Imposta il valore della proprietà estremiRegistrazioneDocRicevuto.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEstremiRegistrazioneDocRicevuto(String value) {
            this.estremiRegistrazioneDocRicevuto = value;
        }

        /**
         * Recupera il valore della proprietà riferimentiDocRicevuto.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRiferimentiDocRicevuto() {
            return riferimentiDocRicevuto;
        }

        /**
         * Imposta il valore della proprietà riferimentiDocRicevuto.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRiferimentiDocRicevuto(String value) {
            this.riferimentiDocRicevuto = value;
        }

        /**
         * Recupera il valore della proprietà mezzoTrasmissione.
         * 
         * @return
         *     possible object is
         *     {@link OggDiTabDiSistemaType }
         *     
         */
        public OggDiTabDiSistemaType getMezzoTrasmissione() {
            return mezzoTrasmissione;
        }

        /**
         * Imposta il valore della proprietà mezzoTrasmissione.
         * 
         * @param value
         *     allowed object is
         *     {@link OggDiTabDiSistemaType }
         *     
         */
        public void setMezzoTrasmissione(OggDiTabDiSistemaType value) {
            this.mezzoTrasmissione = value;
        }

        /**
         * Recupera il valore della proprietà dataRaccomandata.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataRaccomandata() {
            return dataRaccomandata;
        }

        /**
         * Imposta il valore della proprietà dataRaccomandata.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataRaccomandata(XMLGregorianCalendar value) {
            this.dataRaccomandata = value;
        }

        /**
         * Recupera il valore della proprietà nroRaccomandata.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNroRaccomandata() {
            return nroRaccomandata;
        }

        /**
         * Imposta il valore della proprietà nroRaccomandata.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNroRaccomandata(String value) {
            this.nroRaccomandata = value;
        }

        /**
         * Gets the value of the mittenteEsterno property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the mittenteEsterno property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMittenteEsterno().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SoggettoEsternoType }
         * 
         * 
         */
        public List<SoggettoEsternoType> getMittenteEsterno() {
            if (mittenteEsterno == null) {
                mittenteEsterno = new ArrayList<SoggettoEsternoType>();
            }
            return this.mittenteEsterno;
        }

        /**
         * Gets the value of the firmatarioEsterno property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the firmatarioEsterno property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFirmatarioEsterno().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SoggettoEsternoType }
         * 
         * 
         */
        public List<SoggettoEsternoType> getFirmatarioEsterno() {
            if (firmatarioEsterno == null) {
                firmatarioEsterno = new ArrayList<SoggettoEsternoType>();
            }
            return this.firmatarioEsterno;
        }

        /**
         * Recupera il valore della proprietà indirizzoProv.
         * 
         * @return
         *     possible object is
         *     {@link IndirizzoType }
         *     
         */
        public IndirizzoType getIndirizzoProv() {
            return indirizzoProv;
        }

        /**
         * Imposta il valore della proprietà indirizzoProv.
         * 
         * @param value
         *     allowed object is
         *     {@link IndirizzoType }
         *     
         */
        public void setIndirizzoProv(IndirizzoType value) {
            this.indirizzoProv = value;
        }

        /**
         * Recupera il valore della proprietà indirizzoEmailProv.
         * 
         * @return
         *     possible object is
         *     {@link EmailType }
         *     
         */
        public EmailType getIndirizzoEmailProv() {
            return indirizzoEmailProv;
        }

        /**
         * Imposta il valore della proprietà indirizzoEmailProv.
         * 
         * @param value
         *     allowed object is
         *     {@link EmailType }
         *     
         */
        public void setIndirizzoEmailProv(EmailType value) {
            this.indirizzoEmailProv = value;
        }

        /**
         * Recupera il valore della proprietà utenteRicezione.
         * 
         * @return
         *     possible object is
         *     {@link UserType }
         *     
         */
        public UserType getUtenteRicezione() {
            return utenteRicezione;
        }

        /**
         * Imposta il valore della proprietà utenteRicezione.
         * 
         * @param value
         *     allowed object is
         *     {@link UserType }
         *     
         */
        public void setUtenteRicezione(UserType value) {
            this.utenteRicezione = value;
        }

        /**
         * Recupera il valore della proprietà uffRicezione.
         * 
         * @return
         *     possible object is
         *     {@link UOType }
         *     
         */
        public UOType getUffRicezione() {
            return uffRicezione;
        }

        /**
         * Imposta il valore della proprietà uffRicezione.
         * 
         * @param value
         *     allowed object is
         *     {@link UOType }
         *     
         */
        public void setUffRicezione(UOType value) {
            this.uffRicezione = value;
        }

        /**
         * Recupera il valore della proprietà utenteCtrlAmmissib.
         * 
         * @return
         *     possible object is
         *     {@link UserType }
         *     
         */
        public UserType getUtenteCtrlAmmissib() {
            return utenteCtrlAmmissib;
        }

        /**
         * Imposta il valore della proprietà utenteCtrlAmmissib.
         * 
         * @param value
         *     allowed object is
         *     {@link UserType }
         *     
         */
        public void setUtenteCtrlAmmissib(UserType value) {
            this.utenteCtrlAmmissib = value;
        }

    }


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
     *         &lt;element name="DataStesura" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *         &lt;element name="LuogoStesura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="Estensore" type="{}UserType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="UffProduttore" type="{}UOType" maxOccurs="unbounded" minOccurs="0"/>
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
        "dataStesura",
        "luogoStesura",
        "estensore",
        "uffProduttore"
    })
    public static class DatiProduzione {

        @XmlElement(name = "DataStesura")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dataStesura;
        @XmlElement(name = "LuogoStesura")
        protected String luogoStesura;
        @XmlElement(name = "Estensore")
        protected List<UserType> estensore;
        @XmlElement(name = "UffProduttore")
        protected List<UOType> uffProduttore;

        /**
         * Recupera il valore della proprietà dataStesura.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataStesura() {
            return dataStesura;
        }

        /**
         * Imposta il valore della proprietà dataStesura.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataStesura(XMLGregorianCalendar value) {
            this.dataStesura = value;
        }

        /**
         * Recupera il valore della proprietà luogoStesura.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLuogoStesura() {
            return luogoStesura;
        }

        /**
         * Imposta il valore della proprietà luogoStesura.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLuogoStesura(String value) {
            this.luogoStesura = value;
        }

        /**
         * Gets the value of the estensore property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the estensore property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEstensore().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link UserType }
         * 
         * 
         */
        public List<UserType> getEstensore() {
            if (estensore == null) {
                estensore = new ArrayList<UserType>();
            }
            return this.estensore;
        }

        /**
         * Gets the value of the uffProduttore property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the uffProduttore property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getUffProduttore().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link UOType }
         * 
         * 
         */
        public List<UOType> getUffProduttore() {
            if (uffProduttore == null) {
                uffProduttore = new ArrayList<UOType>();
            }
            return this.uffProduttore;
        }

    }


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
     *         &lt;element name="DataOraSped" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
     *         &lt;element name="MezzoTrasmissione" type="{}OggDiTabDiSistemaType" minOccurs="0"/>
     *         &lt;element name="DataRaccomandata" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *         &lt;element name="NroRaccomandata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="DestinatarioEsterno" type="{}DestinatarioEsternoType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="UtenteSpedizione" type="{}UserType" minOccurs="0"/>
     *         &lt;element name="UffSpedizione" type="{}UOType" minOccurs="0"/>
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
        "dataOraSped",
        "mezzoTrasmissione",
        "dataRaccomandata",
        "nroRaccomandata",
        "destinatarioEsterno",
        "utenteSpedizione",
        "uffSpedizione"
    })
    public static class DatiUscita {

        @XmlElement(name = "DataOraSped")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataOraSped;
        @XmlElement(name = "MezzoTrasmissione")
        protected OggDiTabDiSistemaType mezzoTrasmissione;
        @XmlElement(name = "DataRaccomandata")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dataRaccomandata;
        @XmlElement(name = "NroRaccomandata")
        protected String nroRaccomandata;
        @XmlElement(name = "DestinatarioEsterno")
        protected List<DestinatarioEsternoType> destinatarioEsterno;
        @XmlElement(name = "UtenteSpedizione")
        protected UserType utenteSpedizione;
        @XmlElement(name = "UffSpedizione")
        protected UOType uffSpedizione;

        /**
         * Recupera il valore della proprietà dataOraSped.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataOraSped() {
            return dataOraSped;
        }

        /**
         * Imposta il valore della proprietà dataOraSped.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataOraSped(XMLGregorianCalendar value) {
            this.dataOraSped = value;
        }

        /**
         * Recupera il valore della proprietà mezzoTrasmissione.
         * 
         * @return
         *     possible object is
         *     {@link OggDiTabDiSistemaType }
         *     
         */
        public OggDiTabDiSistemaType getMezzoTrasmissione() {
            return mezzoTrasmissione;
        }

        /**
         * Imposta il valore della proprietà mezzoTrasmissione.
         * 
         * @param value
         *     allowed object is
         *     {@link OggDiTabDiSistemaType }
         *     
         */
        public void setMezzoTrasmissione(OggDiTabDiSistemaType value) {
            this.mezzoTrasmissione = value;
        }

        /**
         * Recupera il valore della proprietà dataRaccomandata.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataRaccomandata() {
            return dataRaccomandata;
        }

        /**
         * Imposta il valore della proprietà dataRaccomandata.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataRaccomandata(XMLGregorianCalendar value) {
            this.dataRaccomandata = value;
        }

        /**
         * Recupera il valore della proprietà nroRaccomandata.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNroRaccomandata() {
            return nroRaccomandata;
        }

        /**
         * Imposta il valore della proprietà nroRaccomandata.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNroRaccomandata(String value) {
            this.nroRaccomandata = value;
        }

        /**
         * Gets the value of the destinatarioEsterno property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the destinatarioEsterno property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDestinatarioEsterno().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DestinatarioEsternoType }
         * 
         * 
         */
        public List<DestinatarioEsternoType> getDestinatarioEsterno() {
            if (destinatarioEsterno == null) {
                destinatarioEsterno = new ArrayList<DestinatarioEsternoType>();
            }
            return this.destinatarioEsterno;
        }

        /**
         * Recupera il valore della proprietà utenteSpedizione.
         * 
         * @return
         *     possible object is
         *     {@link UserType }
         *     
         */
        public UserType getUtenteSpedizione() {
            return utenteSpedizione;
        }

        /**
         * Imposta il valore della proprietà utenteSpedizione.
         * 
         * @param value
         *     allowed object is
         *     {@link UserType }
         *     
         */
        public void setUtenteSpedizione(UserType value) {
            this.utenteSpedizione = value;
        }

        /**
         * Recupera il valore della proprietà uffSpedizione.
         * 
         * @return
         *     possible object is
         *     {@link UOType }
         *     
         */
        public UOType getUffSpedizione() {
            return uffSpedizione;
        }

        /**
         * Imposta il valore della proprietà uffSpedizione.
         * 
         * @param value
         *     allowed object is
         *     {@link UOType }
         *     
         */
        public void setUffSpedizione(UOType value) {
            this.uffSpedizione = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="FolderPath" type="{}FolderCustomType"/>
     *         &lt;element name="NomeWorkspace" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "folderPath",
        "nomeWorkspace"
    })
    public static class EreditaPermessiDa {

        @XmlElement(name = "FolderPath")
        protected String folderPath;
        @XmlElement(name = "NomeWorkspace")
        protected Object nomeWorkspace;

        /**
         * Recupera il valore della proprietà folderPath.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFolderPath() {
            return folderPath;
        }

        /**
         * Imposta il valore della proprietà folderPath.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFolderPath(String value) {
            this.folderPath = value;
        }

        /**
         * Recupera il valore della proprietà nomeWorkspace.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getNomeWorkspace() {
            return nomeWorkspace;
        }

        /**
         * Imposta il valore della proprietà nomeWorkspace.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setNomeWorkspace(Object value) {
            this.nomeWorkspace = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;all>
     *         &lt;element name="CategoriaReg">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;enumeration value="PG"/>
     *               &lt;enumeration value="PP"/>
     *               &lt;enumeration value="R"/>
     *               &lt;enumeration value="I"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="SiglaReg" type="{}SiglaRegNumType" minOccurs="0"/>
     *         &lt;element name="AnnoReg" type="{}AnnoType" minOccurs="0"/>
     *       &lt;/all>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {

    })
    public static class RegistrazioneDaDare {

        @XmlElement(name = "CategoriaReg", required = true, defaultValue = "I")
        protected String categoriaReg;
        @XmlElement(name = "SiglaReg")
        protected String siglaReg;
        @XmlElement(name = "AnnoReg")
        protected Integer annoReg;

        /**
         * Recupera il valore della proprietà categoriaReg.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCategoriaReg() {
            return categoriaReg;
        }

        /**
         * Imposta il valore della proprietà categoriaReg.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCategoriaReg(String value) {
            this.categoriaReg = value;
        }

        /**
         * Recupera il valore della proprietà siglaReg.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSiglaReg() {
            return siglaReg;
        }

        /**
         * Imposta il valore della proprietà siglaReg.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSiglaReg(String value) {
            this.siglaReg = value;
        }

        /**
         * Recupera il valore della proprietà annoReg.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getAnnoReg() {
            return annoReg;
        }

        /**
         * Imposta il valore della proprietà annoReg.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setAnnoReg(Integer value) {
            this.annoReg = value;
        }

    }

}
