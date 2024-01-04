
package it.doqui.acta.acaris.documentservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per MappaIdentificazioneDocumento complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MappaIdentificazioneDocumento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rappresentazioneLimitata" type="{common.acaris.acta.doqui.it}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MappaIdentificazioneDocumento", propOrder = {
    "rappresentazioneLimitata"
})
@XmlSeeAlso({
    ContenutoFisicoIdMap.class,
    DocumentoArchivisticoIdMap.class,
    DocumentoFisicoIdMap.class
})
public abstract class MappaIdentificazioneDocumento {

    protected boolean rappresentazioneLimitata;

    /**
     * Recupera il valore della proprietà rappresentazioneLimitata.
     * 
     */
    public boolean isRappresentazioneLimitata() {
        return rappresentazioneLimitata;
    }

    /**
     * Imposta il valore della proprietà rappresentazioneLimitata.
     * 
     */
    public void setRappresentazioneLimitata(boolean value) {
        this.rappresentazioneLimitata = value;
    }

}
