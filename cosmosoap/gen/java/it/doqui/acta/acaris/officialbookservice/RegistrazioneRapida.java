
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per RegistrazioneRapida complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RegistrazioneRapida">
 *   &lt;complexContent>
 *     &lt;extension base="{officialbookservice.acaris.acta.doqui.it}RegistrazioneRequest">
 *       &lt;sequence>
 *         &lt;element name="oggetto" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="cognomeSoggetto" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="nomeSoggetto" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="denominazionePG" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="inserimentoMittenteInAnagrafica" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="classificazioneEstesaPerArchiviazione" type="{common.acaris.acta.doqui.it}string" minOccurs="0"/>
 *         &lt;element name="collocazioneCartacea" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="folderId" type="{common.acaris.acta.doqui.it}ObjectIdType" minOccurs="0"/>
 *         &lt;element name="folderType" type="{officialbookservice.acaris.acta.doqui.it}enumTipoContenitore" minOccurs="0"/>
 *         &lt;element name="tipoSoggetto" type="{officialbookservice.acaris.acta.doqui.it}enumTipoPFPG"/>
 *         &lt;element name="protocollante" type="{officialbookservice.acaris.acta.doqui.it}IdentificazioneProtocollante" minOccurs="0"/>
 *         &lt;element name="annotazione" type="{officialbookservice.acaris.acta.doqui.it}InfoCreazioneAnnotazioneOB" minOccurs="0"/>
 *         &lt;element name="mittenteEsterno" type="{officialbookservice.acaris.acta.doqui.it}MittenteEsterno"/>
 *         &lt;element name="destinatarioInterno" type="{officialbookservice.acaris.acta.doqui.it}DestinatarioInterno"/>
 *         &lt;element name="stampaRicevuta" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="forzareSePresenzaInviti" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="creazioneAsincronaDocumento" type="{common.acaris.acta.doqui.it}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistrazioneRapida", propOrder = {
    "oggetto",
    "cognomeSoggetto",
    "nomeSoggetto",
    "denominazionePG",
    "inserimentoMittenteInAnagrafica",
    "classificazioneEstesaPerArchiviazione",
    "collocazioneCartacea",
    "folderId",
    "folderType",
    "tipoSoggetto",
    "protocollante",
    "annotazione",
    "mittenteEsterno",
    "destinatarioInterno",
    "stampaRicevuta",
    "forzareSePresenzaInviti",
    "creazioneAsincronaDocumento"
})
public class RegistrazioneRapida
    extends RegistrazioneRequest
{

    @XmlElement(required = true)
    protected String oggetto;
    protected String cognomeSoggetto;
    protected String nomeSoggetto;
    protected String denominazionePG;
    protected boolean inserimentoMittenteInAnagrafica;
    protected String classificazioneEstesaPerArchiviazione;
    @XmlElement(required = true)
    protected String collocazioneCartacea;
    protected ObjectIdType folderId;
    protected EnumTipoContenitore folderType;
    @XmlElement(required = true)
    protected EnumTipoPFPG tipoSoggetto;
    protected IdentificazioneProtocollante protocollante;
    protected InfoCreazioneAnnotazioneOB annotazione;
    @XmlElement(required = true)
    protected MittenteEsterno mittenteEsterno;
    @XmlElement(required = true)
    protected DestinatarioInterno destinatarioInterno;
    protected boolean stampaRicevuta;
    protected boolean forzareSePresenzaInviti;
    protected boolean creazioneAsincronaDocumento;

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
     * Recupera il valore della proprietà cognomeSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognomeSoggetto() {
        return cognomeSoggetto;
    }

    /**
     * Imposta il valore della proprietà cognomeSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognomeSoggetto(String value) {
        this.cognomeSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà nomeSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeSoggetto() {
        return nomeSoggetto;
    }

    /**
     * Imposta il valore della proprietà nomeSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeSoggetto(String value) {
        this.nomeSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà denominazionePG.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazionePG() {
        return denominazionePG;
    }

    /**
     * Imposta il valore della proprietà denominazionePG.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazionePG(String value) {
        this.denominazionePG = value;
    }

    /**
     * Recupera il valore della proprietà inserimentoMittenteInAnagrafica.
     * 
     */
    public boolean isInserimentoMittenteInAnagrafica() {
        return inserimentoMittenteInAnagrafica;
    }

    /**
     * Imposta il valore della proprietà inserimentoMittenteInAnagrafica.
     * 
     */
    public void setInserimentoMittenteInAnagrafica(boolean value) {
        this.inserimentoMittenteInAnagrafica = value;
    }

    /**
     * Recupera il valore della proprietà classificazioneEstesaPerArchiviazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassificazioneEstesaPerArchiviazione() {
        return classificazioneEstesaPerArchiviazione;
    }

    /**
     * Imposta il valore della proprietà classificazioneEstesaPerArchiviazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassificazioneEstesaPerArchiviazione(String value) {
        this.classificazioneEstesaPerArchiviazione = value;
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
     * Recupera il valore della proprietà folderId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getFolderId() {
        return folderId;
    }

    /**
     * Imposta il valore della proprietà folderId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setFolderId(ObjectIdType value) {
        this.folderId = value;
    }

    /**
     * Recupera il valore della proprietà folderType.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipoContenitore }
     *     
     */
    public EnumTipoContenitore getFolderType() {
        return folderType;
    }

    /**
     * Imposta il valore della proprietà folderType.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipoContenitore }
     *     
     */
    public void setFolderType(EnumTipoContenitore value) {
        this.folderType = value;
    }

    /**
     * Recupera il valore della proprietà tipoSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipoPFPG }
     *     
     */
    public EnumTipoPFPG getTipoSoggetto() {
        return tipoSoggetto;
    }

    /**
     * Imposta il valore della proprietà tipoSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipoPFPG }
     *     
     */
    public void setTipoSoggetto(EnumTipoPFPG value) {
        this.tipoSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà protocollante.
     * 
     * @return
     *     possible object is
     *     {@link IdentificazioneProtocollante }
     *     
     */
    public IdentificazioneProtocollante getProtocollante() {
        return protocollante;
    }

    /**
     * Imposta il valore della proprietà protocollante.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificazioneProtocollante }
     *     
     */
    public void setProtocollante(IdentificazioneProtocollante value) {
        this.protocollante = value;
    }

    /**
     * Recupera il valore della proprietà annotazione.
     * 
     * @return
     *     possible object is
     *     {@link InfoCreazioneAnnotazioneOB }
     *     
     */
    public InfoCreazioneAnnotazioneOB getAnnotazione() {
        return annotazione;
    }

    /**
     * Imposta il valore della proprietà annotazione.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoCreazioneAnnotazioneOB }
     *     
     */
    public void setAnnotazione(InfoCreazioneAnnotazioneOB value) {
        this.annotazione = value;
    }

    /**
     * Recupera il valore della proprietà mittenteEsterno.
     * 
     * @return
     *     possible object is
     *     {@link MittenteEsterno }
     *     
     */
    public MittenteEsterno getMittenteEsterno() {
        return mittenteEsterno;
    }

    /**
     * Imposta il valore della proprietà mittenteEsterno.
     * 
     * @param value
     *     allowed object is
     *     {@link MittenteEsterno }
     *     
     */
    public void setMittenteEsterno(MittenteEsterno value) {
        this.mittenteEsterno = value;
    }

    /**
     * Recupera il valore della proprietà destinatarioInterno.
     * 
     * @return
     *     possible object is
     *     {@link DestinatarioInterno }
     *     
     */
    public DestinatarioInterno getDestinatarioInterno() {
        return destinatarioInterno;
    }

    /**
     * Imposta il valore della proprietà destinatarioInterno.
     * 
     * @param value
     *     allowed object is
     *     {@link DestinatarioInterno }
     *     
     */
    public void setDestinatarioInterno(DestinatarioInterno value) {
        this.destinatarioInterno = value;
    }

    /**
     * Recupera il valore della proprietà stampaRicevuta.
     * 
     */
    public boolean isStampaRicevuta() {
        return stampaRicevuta;
    }

    /**
     * Imposta il valore della proprietà stampaRicevuta.
     * 
     */
    public void setStampaRicevuta(boolean value) {
        this.stampaRicevuta = value;
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
     * Recupera il valore della proprietà creazioneAsincronaDocumento.
     * 
     */
    public boolean isCreazioneAsincronaDocumento() {
        return creazioneAsincronaDocumento;
    }

    /**
     * Imposta il valore della proprietà creazioneAsincronaDocumento.
     * 
     */
    public void setCreazioneAsincronaDocumento(boolean value) {
        this.creazioneAsincronaDocumento = value;
    }

}
