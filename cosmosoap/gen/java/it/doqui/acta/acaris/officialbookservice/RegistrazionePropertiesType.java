
package it.doqui.acta.acaris.officialbookservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import it.doqui.acta.acaris.common.DecodificaType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per RegistrazionePropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RegistrazionePropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{officialbookservice.acaris.acta.doqui.it}OfficialBookPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="uuid" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="codice" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="stato" type="{common.acaris.acta.doqui.it}DecodificaType"/>
 *         &lt;element name="condizione" type="{common.acaris.acta.doqui.it}DecodificaType"/>
 *         &lt;element name="storico" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="tipoRegistrazione" type="{common.acaris.acta.doqui.it}DecodificaType"/>
 *         &lt;element name="riservato" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="oggetto" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="classificazioneProposta" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="mezzoTrasmissivo" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="dataDocumento" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataRicezione" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataTimbroPostale" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="codiceProtocolloMittente" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="annoProtocolloMittente" type="{common.acaris.acta.doqui.it}integer"/>
 *         &lt;element name="dataProtocolloMittente" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="numeroAllegati" type="{common.acaris.acta.doqui.it}integer"/>
 *         &lt;element name="dataProtocollo" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataInoltro" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="codiceEmergenza" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="dataEmergenza" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataRecupero" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataAnnullamento" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="motivoCreazione" type="{common.acaris.acta.doqui.it}DecodificaType"/>
 *         &lt;element name="idCaricamento" type="{common.acaris.acta.doqui.it}IDDBType"/>
 *         &lt;element name="idAOOProtocollante" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idAOOResponsabile" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idStrutturaProtocollante" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idStrutturaResponsabile" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idNodoProtocollante" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idNodoResponsabile" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idUtenteAnnullamento" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idUtenteCreazione" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idUtenteProtocollista" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idClassificazione" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idDocumento" type="{common.acaris.acta.doqui.it}ObjectIdType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idRegistro" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idRegistrazionePrecedente" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idRegistrazioneSuccessiva" type="{common.acaris.acta.doqui.it}ObjectIdType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idCorrispondente" type="{common.acaris.acta.doqui.it}ObjectIdType" maxOccurs="unbounded"/>
 *         &lt;element name="idAnnotazioneOB" type="{common.acaris.acta.doqui.it}ObjectIdType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idMessaggio" type="{common.acaris.acta.doqui.it}ObjectIdType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idSmistamento" type="{common.acaris.acta.doqui.it}ObjectIdType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistrazionePropertiesType", propOrder = {
    "uuid",
    "codice",
    "stato",
    "condizione",
    "storico",
    "tipoRegistrazione",
    "riservato",
    "oggetto",
    "classificazioneProposta",
    "mezzoTrasmissivo",
    "dataDocumento",
    "dataRicezione",
    "dataTimbroPostale",
    "codiceProtocolloMittente",
    "annoProtocolloMittente",
    "dataProtocolloMittente",
    "numeroAllegati",
    "dataProtocollo",
    "dataInoltro",
    "codiceEmergenza",
    "dataEmergenza",
    "dataRecupero",
    "dataAnnullamento",
    "motivoCreazione",
    "idCaricamento",
    "idAOOProtocollante",
    "idAOOResponsabile",
    "idStrutturaProtocollante",
    "idStrutturaResponsabile",
    "idNodoProtocollante",
    "idNodoResponsabile",
    "idUtenteAnnullamento",
    "idUtenteCreazione",
    "idUtenteProtocollista",
    "idClassificazione",
    "idDocumento",
    "idRegistro",
    "idRegistrazionePrecedente",
    "idRegistrazioneSuccessiva",
    "idCorrispondente",
    "idAnnotazioneOB",
    "idMessaggio",
    "idSmistamento"
})
public class RegistrazionePropertiesType
    extends OfficialBookPropertiesType
{

    @XmlElement(required = true)
    protected String uuid;
    @XmlElement(required = true)
    protected String codice;
    @XmlElement(required = true)
    protected DecodificaType stato;
    @XmlElement(required = true)
    protected DecodificaType condizione;
    protected boolean storico;
    @XmlElement(required = true)
    protected DecodificaType tipoRegistrazione;
    protected boolean riservato;
    @XmlElement(required = true)
    protected String oggetto;
    @XmlElement(required = true)
    protected String classificazioneProposta;
    @XmlElement(required = true)
    protected String mezzoTrasmissivo;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataDocumento;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataRicezione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataTimbroPostale;
    @XmlElement(required = true)
    protected String codiceProtocolloMittente;
    protected int annoProtocolloMittente;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataProtocolloMittente;
    protected int numeroAllegati;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataProtocollo;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataInoltro;
    @XmlElement(required = true)
    protected String codiceEmergenza;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataEmergenza;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataRecupero;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataAnnullamento;
    @XmlElement(required = true)
    protected DecodificaType motivoCreazione;
    protected long idCaricamento;
    @XmlElement(required = true)
    protected ObjectIdType idAOOProtocollante;
    @XmlElement(required = true)
    protected ObjectIdType idAOOResponsabile;
    @XmlElement(required = true)
    protected ObjectIdType idStrutturaProtocollante;
    @XmlElement(required = true)
    protected ObjectIdType idStrutturaResponsabile;
    @XmlElement(required = true)
    protected ObjectIdType idNodoProtocollante;
    @XmlElement(required = true)
    protected ObjectIdType idNodoResponsabile;
    @XmlElement(required = true)
    protected ObjectIdType idUtenteAnnullamento;
    @XmlElement(required = true)
    protected ObjectIdType idUtenteCreazione;
    @XmlElement(required = true)
    protected ObjectIdType idUtenteProtocollista;
    @XmlElement(required = true)
    protected ObjectIdType idClassificazione;
    protected List<ObjectIdType> idDocumento;
    @XmlElement(required = true)
    protected ObjectIdType idRegistro;
    @XmlElement(required = true)
    protected ObjectIdType idRegistrazionePrecedente;
    protected List<ObjectIdType> idRegistrazioneSuccessiva;
    @XmlElement(required = true)
    protected List<ObjectIdType> idCorrispondente;
    protected List<ObjectIdType> idAnnotazioneOB;
    protected List<ObjectIdType> idMessaggio;
    protected List<ObjectIdType> idSmistamento;

    /**
     * Recupera il valore della proprietà uuid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Imposta il valore della proprietà uuid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUuid(String value) {
        this.uuid = value;
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
     * Recupera il valore della proprietà stato.
     * 
     * @return
     *     possible object is
     *     {@link DecodificaType }
     *     
     */
    public DecodificaType getStato() {
        return stato;
    }

    /**
     * Imposta il valore della proprietà stato.
     * 
     * @param value
     *     allowed object is
     *     {@link DecodificaType }
     *     
     */
    public void setStato(DecodificaType value) {
        this.stato = value;
    }

    /**
     * Recupera il valore della proprietà condizione.
     * 
     * @return
     *     possible object is
     *     {@link DecodificaType }
     *     
     */
    public DecodificaType getCondizione() {
        return condizione;
    }

    /**
     * Imposta il valore della proprietà condizione.
     * 
     * @param value
     *     allowed object is
     *     {@link DecodificaType }
     *     
     */
    public void setCondizione(DecodificaType value) {
        this.condizione = value;
    }

    /**
     * Recupera il valore della proprietà storico.
     * 
     */
    public boolean isStorico() {
        return storico;
    }

    /**
     * Imposta il valore della proprietà storico.
     * 
     */
    public void setStorico(boolean value) {
        this.storico = value;
    }

    /**
     * Recupera il valore della proprietà tipoRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link DecodificaType }
     *     
     */
    public DecodificaType getTipoRegistrazione() {
        return tipoRegistrazione;
    }

    /**
     * Imposta il valore della proprietà tipoRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link DecodificaType }
     *     
     */
    public void setTipoRegistrazione(DecodificaType value) {
        this.tipoRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà riservato.
     * 
     */
    public boolean isRiservato() {
        return riservato;
    }

    /**
     * Imposta il valore della proprietà riservato.
     * 
     */
    public void setRiservato(boolean value) {
        this.riservato = value;
    }

    /**
     * Recupera il valore della proprietà oggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOggetto() {
        return oggetto;
    }

    /**
     * Imposta il valore della proprietà oggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOggetto(String value) {
        this.oggetto = value;
    }

    /**
     * Recupera il valore della proprietà classificazioneProposta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassificazioneProposta() {
        return classificazioneProposta;
    }

    /**
     * Imposta il valore della proprietà classificazioneProposta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassificazioneProposta(String value) {
        this.classificazioneProposta = value;
    }

    /**
     * Recupera il valore della proprietà mezzoTrasmissivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMezzoTrasmissivo() {
        return mezzoTrasmissivo;
    }

    /**
     * Imposta il valore della proprietà mezzoTrasmissivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMezzoTrasmissivo(String value) {
        this.mezzoTrasmissivo = value;
    }

    /**
     * Recupera il valore della proprietà dataDocumento.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataDocumento() {
        return dataDocumento;
    }

    /**
     * Imposta il valore della proprietà dataDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataDocumento(XMLGregorianCalendar value) {
        this.dataDocumento = value;
    }

    /**
     * Recupera il valore della proprietà dataRicezione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataRicezione() {
        return dataRicezione;
    }

    /**
     * Imposta il valore della proprietà dataRicezione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataRicezione(XMLGregorianCalendar value) {
        this.dataRicezione = value;
    }

    /**
     * Recupera il valore della proprietà dataTimbroPostale.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataTimbroPostale() {
        return dataTimbroPostale;
    }

    /**
     * Imposta il valore della proprietà dataTimbroPostale.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataTimbroPostale(XMLGregorianCalendar value) {
        this.dataTimbroPostale = value;
    }

    /**
     * Recupera il valore della proprietà codiceProtocolloMittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceProtocolloMittente() {
        return codiceProtocolloMittente;
    }

    /**
     * Imposta il valore della proprietà codiceProtocolloMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceProtocolloMittente(String value) {
        this.codiceProtocolloMittente = value;
    }

    /**
     * Recupera il valore della proprietà annoProtocolloMittente.
     * 
     */
    public int getAnnoProtocolloMittente() {
        return annoProtocolloMittente;
    }

    /**
     * Imposta il valore della proprietà annoProtocolloMittente.
     * 
     */
    public void setAnnoProtocolloMittente(int value) {
        this.annoProtocolloMittente = value;
    }

    /**
     * Recupera il valore della proprietà dataProtocolloMittente.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataProtocolloMittente() {
        return dataProtocolloMittente;
    }

    /**
     * Imposta il valore della proprietà dataProtocolloMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataProtocolloMittente(XMLGregorianCalendar value) {
        this.dataProtocolloMittente = value;
    }

    /**
     * Recupera il valore della proprietà numeroAllegati.
     * 
     */
    public int getNumeroAllegati() {
        return numeroAllegati;
    }

    /**
     * Imposta il valore della proprietà numeroAllegati.
     * 
     */
    public void setNumeroAllegati(int value) {
        this.numeroAllegati = value;
    }

    /**
     * Recupera il valore della proprietà dataProtocollo.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataProtocollo() {
        return dataProtocollo;
    }

    /**
     * Imposta il valore della proprietà dataProtocollo.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataProtocollo(XMLGregorianCalendar value) {
        this.dataProtocollo = value;
    }

    /**
     * Recupera il valore della proprietà dataInoltro.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInoltro() {
        return dataInoltro;
    }

    /**
     * Imposta il valore della proprietà dataInoltro.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInoltro(XMLGregorianCalendar value) {
        this.dataInoltro = value;
    }

    /**
     * Recupera il valore della proprietà codiceEmergenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceEmergenza() {
        return codiceEmergenza;
    }

    /**
     * Imposta il valore della proprietà codiceEmergenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceEmergenza(String value) {
        this.codiceEmergenza = value;
    }

    /**
     * Recupera il valore della proprietà dataEmergenza.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataEmergenza() {
        return dataEmergenza;
    }

    /**
     * Imposta il valore della proprietà dataEmergenza.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataEmergenza(XMLGregorianCalendar value) {
        this.dataEmergenza = value;
    }

    /**
     * Recupera il valore della proprietà dataRecupero.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataRecupero() {
        return dataRecupero;
    }

    /**
     * Imposta il valore della proprietà dataRecupero.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataRecupero(XMLGregorianCalendar value) {
        this.dataRecupero = value;
    }

    /**
     * Recupera il valore della proprietà dataAnnullamento.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataAnnullamento() {
        return dataAnnullamento;
    }

    /**
     * Imposta il valore della proprietà dataAnnullamento.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataAnnullamento(XMLGregorianCalendar value) {
        this.dataAnnullamento = value;
    }

    /**
     * Recupera il valore della proprietà motivoCreazione.
     * 
     * @return
     *     possible object is
     *     {@link DecodificaType }
     *     
     */
    public DecodificaType getMotivoCreazione() {
        return motivoCreazione;
    }

    /**
     * Imposta il valore della proprietà motivoCreazione.
     * 
     * @param value
     *     allowed object is
     *     {@link DecodificaType }
     *     
     */
    public void setMotivoCreazione(DecodificaType value) {
        this.motivoCreazione = value;
    }

    /**
     * Recupera il valore della proprietà idCaricamento.
     * 
     */
    public long getIdCaricamento() {
        return idCaricamento;
    }

    /**
     * Imposta il valore della proprietà idCaricamento.
     * 
     */
    public void setIdCaricamento(long value) {
        this.idCaricamento = value;
    }

    /**
     * Recupera il valore della proprietà idAOOProtocollante.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdAOOProtocollante() {
        return idAOOProtocollante;
    }

    /**
     * Imposta il valore della proprietà idAOOProtocollante.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdAOOProtocollante(ObjectIdType value) {
        this.idAOOProtocollante = value;
    }

    /**
     * Recupera il valore della proprietà idAOOResponsabile.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdAOOResponsabile() {
        return idAOOResponsabile;
    }

    /**
     * Imposta il valore della proprietà idAOOResponsabile.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdAOOResponsabile(ObjectIdType value) {
        this.idAOOResponsabile = value;
    }

    /**
     * Recupera il valore della proprietà idStrutturaProtocollante.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdStrutturaProtocollante() {
        return idStrutturaProtocollante;
    }

    /**
     * Imposta il valore della proprietà idStrutturaProtocollante.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdStrutturaProtocollante(ObjectIdType value) {
        this.idStrutturaProtocollante = value;
    }

    /**
     * Recupera il valore della proprietà idStrutturaResponsabile.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdStrutturaResponsabile() {
        return idStrutturaResponsabile;
    }

    /**
     * Imposta il valore della proprietà idStrutturaResponsabile.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdStrutturaResponsabile(ObjectIdType value) {
        this.idStrutturaResponsabile = value;
    }

    /**
     * Recupera il valore della proprietà idNodoProtocollante.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdNodoProtocollante() {
        return idNodoProtocollante;
    }

    /**
     * Imposta il valore della proprietà idNodoProtocollante.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdNodoProtocollante(ObjectIdType value) {
        this.idNodoProtocollante = value;
    }

    /**
     * Recupera il valore della proprietà idNodoResponsabile.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdNodoResponsabile() {
        return idNodoResponsabile;
    }

    /**
     * Imposta il valore della proprietà idNodoResponsabile.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdNodoResponsabile(ObjectIdType value) {
        this.idNodoResponsabile = value;
    }

    /**
     * Recupera il valore della proprietà idUtenteAnnullamento.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdUtenteAnnullamento() {
        return idUtenteAnnullamento;
    }

    /**
     * Imposta il valore della proprietà idUtenteAnnullamento.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdUtenteAnnullamento(ObjectIdType value) {
        this.idUtenteAnnullamento = value;
    }

    /**
     * Recupera il valore della proprietà idUtenteCreazione.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdUtenteCreazione() {
        return idUtenteCreazione;
    }

    /**
     * Imposta il valore della proprietà idUtenteCreazione.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdUtenteCreazione(ObjectIdType value) {
        this.idUtenteCreazione = value;
    }

    /**
     * Recupera il valore della proprietà idUtenteProtocollista.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdUtenteProtocollista() {
        return idUtenteProtocollista;
    }

    /**
     * Imposta il valore della proprietà idUtenteProtocollista.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdUtenteProtocollista(ObjectIdType value) {
        this.idUtenteProtocollista = value;
    }

    /**
     * Recupera il valore della proprietà idClassificazione.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdClassificazione() {
        return idClassificazione;
    }

    /**
     * Imposta il valore della proprietà idClassificazione.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdClassificazione(ObjectIdType value) {
        this.idClassificazione = value;
    }

    /**
     * Gets the value of the idDocumento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idDocumento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdDocumento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectIdType }
     * 
     * 
     */
    public List<ObjectIdType> getIdDocumento() {
        if (idDocumento == null) {
            idDocumento = new ArrayList<ObjectIdType>();
        }
        return this.idDocumento;
    }

    /**
     * Recupera il valore della proprietà idRegistro.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdRegistro() {
        return idRegistro;
    }

    /**
     * Imposta il valore della proprietà idRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdRegistro(ObjectIdType value) {
        this.idRegistro = value;
    }

    /**
     * Recupera il valore della proprietà idRegistrazionePrecedente.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdRegistrazionePrecedente() {
        return idRegistrazionePrecedente;
    }

    /**
     * Imposta il valore della proprietà idRegistrazionePrecedente.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdRegistrazionePrecedente(ObjectIdType value) {
        this.idRegistrazionePrecedente = value;
    }

    /**
     * Gets the value of the idRegistrazioneSuccessiva property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idRegistrazioneSuccessiva property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdRegistrazioneSuccessiva().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectIdType }
     * 
     * 
     */
    public List<ObjectIdType> getIdRegistrazioneSuccessiva() {
        if (idRegistrazioneSuccessiva == null) {
            idRegistrazioneSuccessiva = new ArrayList<ObjectIdType>();
        }
        return this.idRegistrazioneSuccessiva;
    }

    /**
     * Gets the value of the idCorrispondente property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idCorrispondente property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdCorrispondente().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectIdType }
     * 
     * 
     */
    public List<ObjectIdType> getIdCorrispondente() {
        if (idCorrispondente == null) {
            idCorrispondente = new ArrayList<ObjectIdType>();
        }
        return this.idCorrispondente;
    }

    /**
     * Gets the value of the idAnnotazioneOB property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idAnnotazioneOB property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdAnnotazioneOB().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectIdType }
     * 
     * 
     */
    public List<ObjectIdType> getIdAnnotazioneOB() {
        if (idAnnotazioneOB == null) {
            idAnnotazioneOB = new ArrayList<ObjectIdType>();
        }
        return this.idAnnotazioneOB;
    }

    /**
     * Gets the value of the idMessaggio property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idMessaggio property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdMessaggio().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectIdType }
     * 
     * 
     */
    public List<ObjectIdType> getIdMessaggio() {
        if (idMessaggio == null) {
            idMessaggio = new ArrayList<ObjectIdType>();
        }
        return this.idMessaggio;
    }

    /**
     * Gets the value of the idSmistamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idSmistamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdSmistamento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectIdType }
     * 
     * 
     */
    public List<ObjectIdType> getIdSmistamento() {
        if (idSmistamento == null) {
            idSmistamento = new ArrayList<ObjectIdType>();
        }
        return this.idSmistamento;
    }

}
