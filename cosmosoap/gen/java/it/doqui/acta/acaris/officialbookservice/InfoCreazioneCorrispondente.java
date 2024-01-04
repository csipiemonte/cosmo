
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per InfoCreazioneCorrispondente complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="InfoCreazioneCorrispondente">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="denominazione" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="nome" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="cognome" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="ordinale" type="{common.acaris.acta.doqui.it}integer" minOccurs="0"/>
 *         &lt;element name="carica" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="persona" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="testoFoglioTrasmissione" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="infoSoggettoAssociato" type="{officialbookservice.acaris.acta.doqui.it}InfoSoggettoAssociato" minOccurs="0"/>
 *         &lt;element name="tipologiaCorrispondente" type="{officialbookservice.acaris.acta.doqui.it}enumTipologiaCorrispondente" minOccurs="0"/>
 *         &lt;element name="codiceFiscale" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="PIVA" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="codiceIPAPA" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="codiceIPAAOO" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="codiceIPAUO" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="indirizzoTelematicoPA" type="{officialbookservice.acaris.acta.doqui.it}IndirizzoTelematicoType" minOccurs="0"/>
 *         &lt;element name="indirizzoTelematicoAOO" type="{officialbookservice.acaris.acta.doqui.it}IndirizzoTelematicoType" minOccurs="0"/>
 *         &lt;element name="indirizzoTelematicoUO" type="{officialbookservice.acaris.acta.doqui.it}IndirizzoTelematicoType" minOccurs="0"/>
 *         &lt;element name="denominazioneAmministrazione" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="denominazioneUfficio" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="indirizzoTelematico" type="{officialbookservice.acaris.acta.doqui.it}IndirizzoTelematicoType" minOccurs="0"/>
 *         &lt;element name="richiestaConferma" type="{common.acaris.acta.doqui.it}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InfoCreazioneCorrispondente", propOrder = {
    "denominazione",
    "nome",
    "cognome",
    "ordinale",
    "carica",
    "persona",
    "testoFoglioTrasmissione",
    "infoSoggettoAssociato",
    "tipologiaCorrispondente",
    "codiceFiscale",
    "piva",
    "codiceIPAPA",
    "codiceIPAAOO",
    "codiceIPAUO",
    "indirizzoTelematicoPA",
    "indirizzoTelematicoAOO",
    "indirizzoTelematicoUO",
    "denominazioneAmministrazione",
    "denominazioneUfficio",
    "indirizzoTelematico",
    "richiestaConferma"
})
public class InfoCreazioneCorrispondente {

    protected String denominazione;
    protected String nome;
    protected String cognome;
    protected Integer ordinale;
    protected String carica;
    protected String persona;
    protected String testoFoglioTrasmissione;
    protected InfoSoggettoAssociato infoSoggettoAssociato;
    protected EnumTipologiaCorrispondente tipologiaCorrispondente;
    protected String codiceFiscale;
    @XmlElement(name = "PIVA")
    protected String piva;
    protected String codiceIPAPA;
    protected String codiceIPAAOO;
    protected String codiceIPAUO;
    protected IndirizzoTelematicoType indirizzoTelematicoPA;
    protected IndirizzoTelematicoType indirizzoTelematicoAOO;
    protected IndirizzoTelematicoType indirizzoTelematicoUO;
    protected String denominazioneAmministrazione;
    protected String denominazioneUfficio;
    protected IndirizzoTelematicoType indirizzoTelematico;
    protected Boolean richiestaConferma;

    /**
     * Recupera il valore della proprietà denominazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazione() {
        return denominazione;
    }

    /**
     * Imposta il valore della proprietà denominazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazione(String value) {
        this.denominazione = value;
    }

    /**
     * Recupera il valore della proprietà nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprietà nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprietà cognome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il valore della proprietà cognome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognome(String value) {
        this.cognome = value;
    }

    /**
     * Recupera il valore della proprietà ordinale.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOrdinale() {
        return ordinale;
    }

    /**
     * Imposta il valore della proprietà ordinale.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOrdinale(Integer value) {
        this.ordinale = value;
    }

    /**
     * Recupera il valore della proprietà carica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarica() {
        return carica;
    }

    /**
     * Imposta il valore della proprietà carica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarica(String value) {
        this.carica = value;
    }

    /**
     * Recupera il valore della proprietà persona.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersona() {
        return persona;
    }

    /**
     * Imposta il valore della proprietà persona.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersona(String value) {
        this.persona = value;
    }

    /**
     * Recupera il valore della proprietà testoFoglioTrasmissione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTestoFoglioTrasmissione() {
        return testoFoglioTrasmissione;
    }

    /**
     * Imposta il valore della proprietà testoFoglioTrasmissione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTestoFoglioTrasmissione(String value) {
        this.testoFoglioTrasmissione = value;
    }

    /**
     * Recupera il valore della proprietà infoSoggettoAssociato.
     * 
     * @return
     *     possible object is
     *     {@link InfoSoggettoAssociato }
     *     
     */
    public InfoSoggettoAssociato getInfoSoggettoAssociato() {
        return infoSoggettoAssociato;
    }

    /**
     * Imposta il valore della proprietà infoSoggettoAssociato.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoSoggettoAssociato }
     *     
     */
    public void setInfoSoggettoAssociato(InfoSoggettoAssociato value) {
        this.infoSoggettoAssociato = value;
    }

    /**
     * Recupera il valore della proprietà tipologiaCorrispondente.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipologiaCorrispondente }
     *     
     */
    public EnumTipologiaCorrispondente getTipologiaCorrispondente() {
        return tipologiaCorrispondente;
    }

    /**
     * Imposta il valore della proprietà tipologiaCorrispondente.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipologiaCorrispondente }
     *     
     */
    public void setTipologiaCorrispondente(EnumTipologiaCorrispondente value) {
        this.tipologiaCorrispondente = value;
    }

    /**
     * Recupera il valore della proprietà codiceFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Imposta il valore della proprietà codiceFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
    }

    /**
     * Recupera il valore della proprietà piva.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPIVA() {
        return piva;
    }

    /**
     * Imposta il valore della proprietà piva.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPIVA(String value) {
        this.piva = value;
    }

    /**
     * Recupera il valore della proprietà codiceIPAPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceIPAPA() {
        return codiceIPAPA;
    }

    /**
     * Imposta il valore della proprietà codiceIPAPA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceIPAPA(String value) {
        this.codiceIPAPA = value;
    }

    /**
     * Recupera il valore della proprietà codiceIPAAOO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceIPAAOO() {
        return codiceIPAAOO;
    }

    /**
     * Imposta il valore della proprietà codiceIPAAOO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceIPAAOO(String value) {
        this.codiceIPAAOO = value;
    }

    /**
     * Recupera il valore della proprietà codiceIPAUO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceIPAUO() {
        return codiceIPAUO;
    }

    /**
     * Imposta il valore della proprietà codiceIPAUO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceIPAUO(String value) {
        this.codiceIPAUO = value;
    }

    /**
     * Recupera il valore della proprietà indirizzoTelematicoPA.
     * 
     * @return
     *     possible object is
     *     {@link IndirizzoTelematicoType }
     *     
     */
    public IndirizzoTelematicoType getIndirizzoTelematicoPA() {
        return indirizzoTelematicoPA;
    }

    /**
     * Imposta il valore della proprietà indirizzoTelematicoPA.
     * 
     * @param value
     *     allowed object is
     *     {@link IndirizzoTelematicoType }
     *     
     */
    public void setIndirizzoTelematicoPA(IndirizzoTelematicoType value) {
        this.indirizzoTelematicoPA = value;
    }

    /**
     * Recupera il valore della proprietà indirizzoTelematicoAOO.
     * 
     * @return
     *     possible object is
     *     {@link IndirizzoTelematicoType }
     *     
     */
    public IndirizzoTelematicoType getIndirizzoTelematicoAOO() {
        return indirizzoTelematicoAOO;
    }

    /**
     * Imposta il valore della proprietà indirizzoTelematicoAOO.
     * 
     * @param value
     *     allowed object is
     *     {@link IndirizzoTelematicoType }
     *     
     */
    public void setIndirizzoTelematicoAOO(IndirizzoTelematicoType value) {
        this.indirizzoTelematicoAOO = value;
    }

    /**
     * Recupera il valore della proprietà indirizzoTelematicoUO.
     * 
     * @return
     *     possible object is
     *     {@link IndirizzoTelematicoType }
     *     
     */
    public IndirizzoTelematicoType getIndirizzoTelematicoUO() {
        return indirizzoTelematicoUO;
    }

    /**
     * Imposta il valore della proprietà indirizzoTelematicoUO.
     * 
     * @param value
     *     allowed object is
     *     {@link IndirizzoTelematicoType }
     *     
     */
    public void setIndirizzoTelematicoUO(IndirizzoTelematicoType value) {
        this.indirizzoTelematicoUO = value;
    }

    /**
     * Recupera il valore della proprietà denominazioneAmministrazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazioneAmministrazione() {
        return denominazioneAmministrazione;
    }

    /**
     * Imposta il valore della proprietà denominazioneAmministrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazioneAmministrazione(String value) {
        this.denominazioneAmministrazione = value;
    }

    /**
     * Recupera il valore della proprietà denominazioneUfficio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazioneUfficio() {
        return denominazioneUfficio;
    }

    /**
     * Imposta il valore della proprietà denominazioneUfficio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazioneUfficio(String value) {
        this.denominazioneUfficio = value;
    }

    /**
     * Recupera il valore della proprietà indirizzoTelematico.
     * 
     * @return
     *     possible object is
     *     {@link IndirizzoTelematicoType }
     *     
     */
    public IndirizzoTelematicoType getIndirizzoTelematico() {
        return indirizzoTelematico;
    }

    /**
     * Imposta il valore della proprietà indirizzoTelematico.
     * 
     * @param value
     *     allowed object is
     *     {@link IndirizzoTelematicoType }
     *     
     */
    public void setIndirizzoTelematico(IndirizzoTelematicoType value) {
        this.indirizzoTelematico = value;
    }

    /**
     * Recupera il valore della proprietà richiestaConferma.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRichiestaConferma() {
        return richiestaConferma;
    }

    /**
     * Imposta il valore della proprietà richiestaConferma.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRichiestaConferma(Boolean value) {
        this.richiestaConferma = value;
    }

}
