
package it.eng.auriga.repository2.webservices.updunitadoc.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Contiene i dati per aggiornare/rimuovere la
 * 				versione elettronica di un documento
 * 
 * <p>Classe Java per AggVersioneElettronicaType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="AggVersioneElettronicaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EliminazioneVecchiaVersione" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="CtrlUltimaVers" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="DatiVersioneElettronica" type="{}DatiVersioneElettronicaType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AggVersioneElettronicaType", propOrder = {
    "eliminazioneVecchiaVersione",
    "ctrlUltimaVers",
    "datiVersioneElettronica"
})
public class AggVersioneElettronicaType {

    @XmlElement(name = "EliminazioneVecchiaVersione")
    protected Object eliminazioneVecchiaVersione;
    @XmlElement(name = "CtrlUltimaVers")
    protected Object ctrlUltimaVers;
    @XmlElement(name = "DatiVersioneElettronica")
    protected DatiVersioneElettronicaType datiVersioneElettronica;

    /**
     * Recupera il valore della proprietà eliminazioneVecchiaVersione.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getEliminazioneVecchiaVersione() {
        return eliminazioneVecchiaVersione;
    }

    /**
     * Imposta il valore della proprietà eliminazioneVecchiaVersione.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setEliminazioneVecchiaVersione(Object value) {
        this.eliminazioneVecchiaVersione = value;
    }

    /**
     * Recupera il valore della proprietà ctrlUltimaVers.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getCtrlUltimaVers() {
        return ctrlUltimaVers;
    }

    /**
     * Imposta il valore della proprietà ctrlUltimaVers.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setCtrlUltimaVers(Object value) {
        this.ctrlUltimaVers = value;
    }

    /**
     * Recupera il valore della proprietà datiVersioneElettronica.
     * 
     * @return
     *     possible object is
     *     {@link DatiVersioneElettronicaType }
     *     
     */
    public DatiVersioneElettronicaType getDatiVersioneElettronica() {
        return datiVersioneElettronica;
    }

    /**
     * Imposta il valore della proprietà datiVersioneElettronica.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiVersioneElettronicaType }
     *     
     */
    public void setDatiVersioneElettronica(DatiVersioneElettronicaType value) {
        this.datiVersioneElettronica = value;
    }

}
