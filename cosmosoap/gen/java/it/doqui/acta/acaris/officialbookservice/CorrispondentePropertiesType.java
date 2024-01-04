
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import it.doqui.acta.acaris.common.DecodificaType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per CorrispondentePropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="CorrispondentePropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{officialbookservice.acaris.acta.doqui.it}OfficialBookPropertiesType">
 *       &lt;sequence>
 *         &lt;element name="denominazione" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="nome" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="cognome" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="tipoMD" type="{officialbookservice.acaris.acta.doqui.it}enumTipoCorrispondente"/>
 *         &lt;element name="interno" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="ordinale" type="{common.acaris.acta.doqui.it}integer"/>
 *         &lt;element name="carica" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="persona" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="motivoCancellazione" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="dataInizioValidita" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataFineValidita" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="testoFoglioTrasmissione" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="ruolo" type="{common.acaris.acta.doqui.it}DecodificaType"/>
 *         &lt;element name="idSoggetto" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="idRegistrazione" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CorrispondentePropertiesType", propOrder = {
    "denominazione",
    "nome",
    "cognome",
    "tipoMD",
    "interno",
    "ordinale",
    "carica",
    "persona",
    "motivoCancellazione",
    "dataInizioValidita",
    "dataFineValidita",
    "testoFoglioTrasmissione",
    "ruolo",
    "idSoggetto",
    "idRegistrazione"
})
public class CorrispondentePropertiesType
    extends OfficialBookPropertiesType
{

    @XmlElement(required = true)
    protected String denominazione;
    @XmlElement(required = true)
    protected String nome;
    @XmlElement(required = true)
    protected String cognome;
    @XmlElement(required = true)
    protected EnumTipoCorrispondente tipoMD;
    protected boolean interno;
    protected int ordinale;
    @XmlElement(required = true)
    protected String carica;
    @XmlElement(required = true)
    protected String persona;
    @XmlElement(required = true)
    protected String motivoCancellazione;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataInizioValidita;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataFineValidita;
    @XmlElement(required = true)
    protected String testoFoglioTrasmissione;
    @XmlElement(required = true)
    protected DecodificaType ruolo;
    @XmlElement(required = true)
    protected ObjectIdType idSoggetto;
    @XmlElement(required = true)
    protected ObjectIdType idRegistrazione;

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
     * Recupera il valore della proprietà tipoMD.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipoCorrispondente }
     *     
     */
    public EnumTipoCorrispondente getTipoMD() {
        return tipoMD;
    }

    /**
     * Imposta il valore della proprietà tipoMD.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipoCorrispondente }
     *     
     */
    public void setTipoMD(EnumTipoCorrispondente value) {
        this.tipoMD = value;
    }

    /**
     * Recupera il valore della proprietà interno.
     * 
     */
    public boolean isInterno() {
        return interno;
    }

    /**
     * Imposta il valore della proprietà interno.
     * 
     */
    public void setInterno(boolean value) {
        this.interno = value;
    }

    /**
     * Recupera il valore della proprietà ordinale.
     * 
     */
    public int getOrdinale() {
        return ordinale;
    }

    /**
     * Imposta il valore della proprietà ordinale.
     * 
     */
    public void setOrdinale(int value) {
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
     * Recupera il valore della proprietà motivoCancellazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotivoCancellazione() {
        return motivoCancellazione;
    }

    /**
     * Imposta il valore della proprietà motivoCancellazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotivoCancellazione(String value) {
        this.motivoCancellazione = value;
    }

    /**
     * Recupera il valore della proprietà dataInizioValidita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInizioValidita() {
        return dataInizioValidita;
    }

    /**
     * Imposta il valore della proprietà dataInizioValidita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInizioValidita(XMLGregorianCalendar value) {
        this.dataInizioValidita = value;
    }

    /**
     * Recupera il valore della proprietà dataFineValidita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataFineValidita() {
        return dataFineValidita;
    }

    /**
     * Imposta il valore della proprietà dataFineValidita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFineValidita(XMLGregorianCalendar value) {
        this.dataFineValidita = value;
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
     * Recupera il valore della proprietà ruolo.
     * 
     * @return
     *     possible object is
     *     {@link DecodificaType }
     *     
     */
    public DecodificaType getRuolo() {
        return ruolo;
    }

    /**
     * Imposta il valore della proprietà ruolo.
     * 
     * @param value
     *     allowed object is
     *     {@link DecodificaType }
     *     
     */
    public void setRuolo(DecodificaType value) {
        this.ruolo = value;
    }

    /**
     * Recupera il valore della proprietà idSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdSoggetto() {
        return idSoggetto;
    }

    /**
     * Imposta il valore della proprietà idSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdSoggetto(ObjectIdType value) {
        this.idSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà idRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getIdRegistrazione() {
        return idRegistrazione;
    }

    /**
     * Imposta il valore della proprietà idRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setIdRegistrazione(ObjectIdType value) {
        this.idRegistrazione = value;
    }

}
