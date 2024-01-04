
package it.doqui.acta.acaris.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import it.doqui.acta.acaris.common.CodiceFiscaleType;
import it.doqui.acta.acaris.common.IdAOOType;
import it.doqui.acta.acaris.common.IdMovimentazioneType;
import it.doqui.acta.acaris.common.IdNodoType;
import it.doqui.acta.acaris.common.IdStrutturaType;


/**
 * <p>Classe Java per MovimentazioneType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MovimentazioneType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idMovimentazione" type="{common.acaris.acta.doqui.it}IdMovimentazioneType"/>
 *         &lt;element name="idAOORicevente" type="{common.acaris.acta.doqui.it}IdAOOType"/>
 *         &lt;element name="idStrutturaRicevente" type="{common.acaris.acta.doqui.it}IdStrutturaType"/>
 *         &lt;element name="idNodoRicevente" type="{common.acaris.acta.doqui.it}IdNodoType"/>
 *         &lt;element name="dataConsegna" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataPianificataRestituz" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="dataRestituzione" type="{common.acaris.acta.doqui.it}date"/>
 *         &lt;element name="codFiscaleUtenteRicevente" type="{common.acaris.acta.doqui.it}CodiceFiscaleType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MovimentazioneType", propOrder = {
    "idMovimentazione",
    "idAOORicevente",
    "idStrutturaRicevente",
    "idNodoRicevente",
    "dataConsegna",
    "dataPianificataRestituz",
    "dataRestituzione",
    "codFiscaleUtenteRicevente"
})
public class MovimentazioneType {

    @XmlElement(required = true)
    protected IdMovimentazioneType idMovimentazione;
    @XmlElement(required = true)
    protected IdAOOType idAOORicevente;
    @XmlElement(required = true)
    protected IdStrutturaType idStrutturaRicevente;
    @XmlElement(required = true)
    protected IdNodoType idNodoRicevente;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataConsegna;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataPianificataRestituz;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dataRestituzione;
    @XmlElement(required = true)
    protected CodiceFiscaleType codFiscaleUtenteRicevente;

    /**
     * Recupera il valore della proprietà idMovimentazione.
     * 
     * @return
     *     possible object is
     *     {@link IdMovimentazioneType }
     *     
     */
    public IdMovimentazioneType getIdMovimentazione() {
        return idMovimentazione;
    }

    /**
     * Imposta il valore della proprietà idMovimentazione.
     * 
     * @param value
     *     allowed object is
     *     {@link IdMovimentazioneType }
     *     
     */
    public void setIdMovimentazione(IdMovimentazioneType value) {
        this.idMovimentazione = value;
    }

    /**
     * Recupera il valore della proprietà idAOORicevente.
     * 
     * @return
     *     possible object is
     *     {@link IdAOOType }
     *     
     */
    public IdAOOType getIdAOORicevente() {
        return idAOORicevente;
    }

    /**
     * Imposta il valore della proprietà idAOORicevente.
     * 
     * @param value
     *     allowed object is
     *     {@link IdAOOType }
     *     
     */
    public void setIdAOORicevente(IdAOOType value) {
        this.idAOORicevente = value;
    }

    /**
     * Recupera il valore della proprietà idStrutturaRicevente.
     * 
     * @return
     *     possible object is
     *     {@link IdStrutturaType }
     *     
     */
    public IdStrutturaType getIdStrutturaRicevente() {
        return idStrutturaRicevente;
    }

    /**
     * Imposta il valore della proprietà idStrutturaRicevente.
     * 
     * @param value
     *     allowed object is
     *     {@link IdStrutturaType }
     *     
     */
    public void setIdStrutturaRicevente(IdStrutturaType value) {
        this.idStrutturaRicevente = value;
    }

    /**
     * Recupera il valore della proprietà idNodoRicevente.
     * 
     * @return
     *     possible object is
     *     {@link IdNodoType }
     *     
     */
    public IdNodoType getIdNodoRicevente() {
        return idNodoRicevente;
    }

    /**
     * Imposta il valore della proprietà idNodoRicevente.
     * 
     * @param value
     *     allowed object is
     *     {@link IdNodoType }
     *     
     */
    public void setIdNodoRicevente(IdNodoType value) {
        this.idNodoRicevente = value;
    }

    /**
     * Recupera il valore della proprietà dataConsegna.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataConsegna() {
        return dataConsegna;
    }

    /**
     * Imposta il valore della proprietà dataConsegna.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataConsegna(XMLGregorianCalendar value) {
        this.dataConsegna = value;
    }

    /**
     * Recupera il valore della proprietà dataPianificataRestituz.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataPianificataRestituz() {
        return dataPianificataRestituz;
    }

    /**
     * Imposta il valore della proprietà dataPianificataRestituz.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataPianificataRestituz(XMLGregorianCalendar value) {
        this.dataPianificataRestituz = value;
    }

    /**
     * Recupera il valore della proprietà dataRestituzione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataRestituzione() {
        return dataRestituzione;
    }

    /**
     * Imposta il valore della proprietà dataRestituzione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataRestituzione(XMLGregorianCalendar value) {
        this.dataRestituzione = value;
    }

    /**
     * Recupera il valore della proprietà codFiscaleUtenteRicevente.
     * 
     * @return
     *     possible object is
     *     {@link CodiceFiscaleType }
     *     
     */
    public CodiceFiscaleType getCodFiscaleUtenteRicevente() {
        return codFiscaleUtenteRicevente;
    }

    /**
     * Imposta il valore della proprietà codFiscaleUtenteRicevente.
     * 
     * @param value
     *     allowed object is
     *     {@link CodiceFiscaleType }
     *     
     */
    public void setCodFiscaleUtenteRicevente(CodiceFiscaleType value) {
        this.codFiscaleUtenteRicevente = value;
    }

}
