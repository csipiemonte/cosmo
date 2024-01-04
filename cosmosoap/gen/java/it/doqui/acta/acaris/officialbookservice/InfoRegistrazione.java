
package it.doqui.acta.acaris.officialbookservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per InfoRegistrazione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="InfoRegistrazione">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataProtocollo" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="codice" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="annullata" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="numeroRegistrazionePrecedente" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="annoRegistrazionePrecedente" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="registrazionePrecedenteId" type="{common.acaris.acta.doqui.it}ObjectIdType" minOccurs="0"/>
 *         &lt;element name="registrazioneRiservata" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="mezzoTrasmissivoId" type="{common.acaris.acta.doqui.it}IDDBType" minOccurs="0"/>
 *         &lt;element name="oggetto" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="collocazioneCartacea" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="forzareSeRegistrazioneSimile" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="documentoRiservato" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="protocollante" type="{officialbookservice.acaris.acta.doqui.it}IdentificazioneProtocollanteEstesa"/>
 *         &lt;element name="tipoRegistrazione" type="{officialbookservice.acaris.acta.doqui.it}enumTipoAPI"/>
 *         &lt;element name="infoAggiuntiveRegistrazioneInArrivo" type="{officialbookservice.acaris.acta.doqui.it}RegistrazioneArrivo" minOccurs="0"/>
 *         &lt;element name="annotazione" type="{officialbookservice.acaris.acta.doqui.it}InfoCreazioneAnnotazioneOB" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="infoAnnullamento" type="{officialbookservice.acaris.acta.doqui.it}InfoAnnullamentoRegistrazione" minOccurs="0"/>
 *         &lt;element name="mittenteInterno" type="{officialbookservice.acaris.acta.doqui.it}MittenteInterno" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="destinatarioInterno" type="{officialbookservice.acaris.acta.doqui.it}DestinatarioInterno" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="destinatarioEsterno" type="{officialbookservice.acaris.acta.doqui.it}DestinatarioEsterno" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="forzareSePresenzaInviti" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="forzareSePresenzaDaInoltrare" type="{common.acaris.acta.doqui.it}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InfoRegistrazione", propOrder = {
    "dataProtocollo",
    "codice",
    "annullata",
    "numeroRegistrazionePrecedente",
    "annoRegistrazionePrecedente",
    "registrazionePrecedenteId",
    "registrazioneRiservata",
    "mezzoTrasmissivoId",
    "oggetto",
    "collocazioneCartacea",
    "forzareSeRegistrazioneSimile",
    "documentoRiservato",
    "protocollante",
    "tipoRegistrazione",
    "infoAggiuntiveRegistrazioneInArrivo",
    "annotazione",
    "infoAnnullamento",
    "mittenteInterno",
    "destinatarioInterno",
    "destinatarioEsterno",
    "forzareSePresenzaInviti",
    "forzareSePresenzaDaInoltrare"
})
public class InfoRegistrazione {

    @XmlElement(required = true)
    protected XMLGregorianCalendar dataProtocollo;
    @XmlElement(required = true)
    protected String codice;
    protected boolean annullata;
    protected String numeroRegistrazionePrecedente;
    protected String annoRegistrazionePrecedente;
    protected ObjectIdType registrazionePrecedenteId;
    protected boolean registrazioneRiservata;
    protected Long mezzoTrasmissivoId;
    @XmlElement(required = true)
    protected String oggetto;
    protected String collocazioneCartacea;
    protected boolean forzareSeRegistrazioneSimile;
    protected boolean documentoRiservato;
    @XmlElement(required = true)
    protected IdentificazioneProtocollanteEstesa protocollante;
    @XmlElement(required = true)
    protected EnumTipoAPI tipoRegistrazione;
    protected RegistrazioneArrivo infoAggiuntiveRegistrazioneInArrivo;
    protected List<InfoCreazioneAnnotazioneOB> annotazione;
    protected InfoAnnullamentoRegistrazione infoAnnullamento;
    protected List<MittenteInterno> mittenteInterno;
    protected List<DestinatarioInterno> destinatarioInterno;
    protected List<DestinatarioEsterno> destinatarioEsterno;
    protected boolean forzareSePresenzaInviti;
    protected boolean forzareSePresenzaDaInoltrare;

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
     * Recupera il valore della proprietà annullata.
     * 
     */
    public boolean isAnnullata() {
        return annullata;
    }

    /**
     * Imposta il valore della proprietà annullata.
     * 
     */
    public void setAnnullata(boolean value) {
        this.annullata = value;
    }

    /**
     * Recupera il valore della proprietà numeroRegistrazionePrecedente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroRegistrazionePrecedente() {
        return numeroRegistrazionePrecedente;
    }

    /**
     * Imposta il valore della proprietà numeroRegistrazionePrecedente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroRegistrazionePrecedente(String value) {
        this.numeroRegistrazionePrecedente = value;
    }

    /**
     * Recupera il valore della proprietà annoRegistrazionePrecedente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnnoRegistrazionePrecedente() {
        return annoRegistrazionePrecedente;
    }

    /**
     * Imposta il valore della proprietà annoRegistrazionePrecedente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnnoRegistrazionePrecedente(String value) {
        this.annoRegistrazionePrecedente = value;
    }

    /**
     * Recupera il valore della proprietà registrazionePrecedenteId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getRegistrazionePrecedenteId() {
        return registrazionePrecedenteId;
    }

    /**
     * Imposta il valore della proprietà registrazionePrecedenteId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setRegistrazionePrecedenteId(ObjectIdType value) {
        this.registrazionePrecedenteId = value;
    }

    /**
     * Recupera il valore della proprietà registrazioneRiservata.
     * 
     */
    public boolean isRegistrazioneRiservata() {
        return registrazioneRiservata;
    }

    /**
     * Imposta il valore della proprietà registrazioneRiservata.
     * 
     */
    public void setRegistrazioneRiservata(boolean value) {
        this.registrazioneRiservata = value;
    }

    /**
     * Recupera il valore della proprietà mezzoTrasmissivoId.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMezzoTrasmissivoId() {
        return mezzoTrasmissivoId;
    }

    /**
     * Imposta il valore della proprietà mezzoTrasmissivoId.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMezzoTrasmissivoId(Long value) {
        this.mezzoTrasmissivoId = value;
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
     * Recupera il valore della proprietà forzareSeRegistrazioneSimile.
     * 
     */
    public boolean isForzareSeRegistrazioneSimile() {
        return forzareSeRegistrazioneSimile;
    }

    /**
     * Imposta il valore della proprietà forzareSeRegistrazioneSimile.
     * 
     */
    public void setForzareSeRegistrazioneSimile(boolean value) {
        this.forzareSeRegistrazioneSimile = value;
    }

    /**
     * Recupera il valore della proprietà documentoRiservato.
     * 
     */
    public boolean isDocumentoRiservato() {
        return documentoRiservato;
    }

    /**
     * Imposta il valore della proprietà documentoRiservato.
     * 
     */
    public void setDocumentoRiservato(boolean value) {
        this.documentoRiservato = value;
    }

    /**
     * Recupera il valore della proprietà protocollante.
     * 
     * @return
     *     possible object is
     *     {@link IdentificazioneProtocollanteEstesa }
     *     
     */
    public IdentificazioneProtocollanteEstesa getProtocollante() {
        return protocollante;
    }

    /**
     * Imposta il valore della proprietà protocollante.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificazioneProtocollanteEstesa }
     *     
     */
    public void setProtocollante(IdentificazioneProtocollanteEstesa value) {
        this.protocollante = value;
    }

    /**
     * Recupera il valore della proprietà tipoRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipoAPI }
     *     
     */
    public EnumTipoAPI getTipoRegistrazione() {
        return tipoRegistrazione;
    }

    /**
     * Imposta il valore della proprietà tipoRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipoAPI }
     *     
     */
    public void setTipoRegistrazione(EnumTipoAPI value) {
        this.tipoRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà infoAggiuntiveRegistrazioneInArrivo.
     * 
     * @return
     *     possible object is
     *     {@link RegistrazioneArrivo }
     *     
     */
    public RegistrazioneArrivo getInfoAggiuntiveRegistrazioneInArrivo() {
        return infoAggiuntiveRegistrazioneInArrivo;
    }

    /**
     * Imposta il valore della proprietà infoAggiuntiveRegistrazioneInArrivo.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistrazioneArrivo }
     *     
     */
    public void setInfoAggiuntiveRegistrazioneInArrivo(RegistrazioneArrivo value) {
        this.infoAggiuntiveRegistrazioneInArrivo = value;
    }

    /**
     * Gets the value of the annotazione property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the annotazione property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnnotazione().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InfoCreazioneAnnotazioneOB }
     * 
     * 
     */
    public List<InfoCreazioneAnnotazioneOB> getAnnotazione() {
        if (annotazione == null) {
            annotazione = new ArrayList<InfoCreazioneAnnotazioneOB>();
        }
        return this.annotazione;
    }

    /**
     * Recupera il valore della proprietà infoAnnullamento.
     * 
     * @return
     *     possible object is
     *     {@link InfoAnnullamentoRegistrazione }
     *     
     */
    public InfoAnnullamentoRegistrazione getInfoAnnullamento() {
        return infoAnnullamento;
    }

    /**
     * Imposta il valore della proprietà infoAnnullamento.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoAnnullamentoRegistrazione }
     *     
     */
    public void setInfoAnnullamento(InfoAnnullamentoRegistrazione value) {
        this.infoAnnullamento = value;
    }

    /**
     * Gets the value of the mittenteInterno property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mittenteInterno property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMittenteInterno().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MittenteInterno }
     * 
     * 
     */
    public List<MittenteInterno> getMittenteInterno() {
        if (mittenteInterno == null) {
            mittenteInterno = new ArrayList<MittenteInterno>();
        }
        return this.mittenteInterno;
    }

    /**
     * Gets the value of the destinatarioInterno property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the destinatarioInterno property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDestinatarioInterno().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DestinatarioInterno }
     * 
     * 
     */
    public List<DestinatarioInterno> getDestinatarioInterno() {
        if (destinatarioInterno == null) {
            destinatarioInterno = new ArrayList<DestinatarioInterno>();
        }
        return this.destinatarioInterno;
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
     * {@link DestinatarioEsterno }
     * 
     * 
     */
    public List<DestinatarioEsterno> getDestinatarioEsterno() {
        if (destinatarioEsterno == null) {
            destinatarioEsterno = new ArrayList<DestinatarioEsterno>();
        }
        return this.destinatarioEsterno;
    }

    /**
     * Recupera il valore della proprietà forzareSePresenzaInviti.
     * 
     */
    public boolean isForzareSePresenzaInviti() {
        return forzareSePresenzaInviti;
    }

    /**
     * Imposta il valore della proprietà forzareSePresenzaInviti.
     * 
     */
    public void setForzareSePresenzaInviti(boolean value) {
        this.forzareSePresenzaInviti = value;
    }

    /**
     * Recupera il valore della proprietà forzareSePresenzaDaInoltrare.
     * 
     */
    public boolean isForzareSePresenzaDaInoltrare() {
        return forzareSePresenzaDaInoltrare;
    }

    /**
     * Imposta il valore della proprietà forzareSePresenzaDaInoltrare.
     * 
     */
    public void setForzareSePresenzaDaInoltrare(boolean value) {
        this.forzareSePresenzaDaInoltrare = value;
    }

}
