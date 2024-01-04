
package it.doqui.acta.acaris.officialbookservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per RegistrazioneArrivo complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RegistrazioneArrivo">
 *   &lt;complexContent>
 *     &lt;extension base="{officialbookservice.acaris.acta.doqui.it}RegistrazioneAPI">
 *       &lt;sequence>
 *         &lt;element name="infoDateArrivo" type="{officialbookservice.acaris.acta.doqui.it}InfoDateArrivo" minOccurs="0"/>
 *         &lt;element name="infoProtocolloMittente" type="{officialbookservice.acaris.acta.doqui.it}InfoProtocolloMittente" minOccurs="0"/>
 *         &lt;element name="mittenteEsterno" type="{officialbookservice.acaris.acta.doqui.it}MittenteEsterno" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistrazioneArrivo", propOrder = {
    "infoDateArrivo",
    "infoProtocolloMittente",
    "mittenteEsterno"
})
public class RegistrazioneArrivo
    extends RegistrazioneAPI
{

    protected InfoDateArrivo infoDateArrivo;
    protected InfoProtocolloMittente infoProtocolloMittente;
    protected List<MittenteEsterno> mittenteEsterno;

    /**
     * Recupera il valore della proprietà infoDateArrivo.
     * 
     * @return
     *     possible object is
     *     {@link InfoDateArrivo }
     *     
     */
    public InfoDateArrivo getInfoDateArrivo() {
        return infoDateArrivo;
    }

    /**
     * Imposta il valore della proprietà infoDateArrivo.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoDateArrivo }
     *     
     */
    public void setInfoDateArrivo(InfoDateArrivo value) {
        this.infoDateArrivo = value;
    }

    /**
     * Recupera il valore della proprietà infoProtocolloMittente.
     * 
     * @return
     *     possible object is
     *     {@link InfoProtocolloMittente }
     *     
     */
    public InfoProtocolloMittente getInfoProtocolloMittente() {
        return infoProtocolloMittente;
    }

    /**
     * Imposta il valore della proprietà infoProtocolloMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoProtocolloMittente }
     *     
     */
    public void setInfoProtocolloMittente(InfoProtocolloMittente value) {
        this.infoProtocolloMittente = value;
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
     * {@link MittenteEsterno }
     * 
     * 
     */
    public List<MittenteEsterno> getMittenteEsterno() {
        if (mittenteEsterno == null) {
            mittenteEsterno = new ArrayList<MittenteEsterno>();
        }
        return this.mittenteEsterno;
    }

}
