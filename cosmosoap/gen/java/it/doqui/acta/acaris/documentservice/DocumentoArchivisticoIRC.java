
package it.doqui.acta.acaris.documentservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.archive.GruppoAllegatiPropertiesType;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.common.PropertiesType;


/**
 * <p>Classe Java per DocumentoArchivisticoIRC complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DocumentoArchivisticoIRC">
 *   &lt;complexContent>
 *     &lt;extension base="{documentservice.acaris.acta.doqui.it}InfoRichiestaCreazione">
 *       &lt;sequence>
 *         &lt;element name="parentFolderId" type="{common.acaris.acta.doqui.it}ObjectIdType" minOccurs="0"/>
 *         &lt;element name="tipoDocumento" type="{documentservice.acaris.acta.doqui.it}enumTipoDocumentoArchivistico"/>
 *         &lt;element name="allegato" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="gruppoAllegati" type="{archive.acaris.acta.doqui.it}GruppoAllegatiPropertiesType" minOccurs="0"/>
 *         &lt;element name="classificazionePrincipale" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="propertiesDocumento" type="{common.acaris.acta.doqui.it}PropertiesType"/>
 *         &lt;element name="propertiesClassificazione" type="{common.acaris.acta.doqui.it}PropertiesType"/>
 *         &lt;element name="documentiFisici" type="{documentservice.acaris.acta.doqui.it}DocumentoFisicoIRC" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentoArchivisticoIRC", propOrder = {
    "parentFolderId",
    "tipoDocumento",
    "allegato",
    "gruppoAllegati",
    "classificazionePrincipale",
    "propertiesDocumento",
    "propertiesClassificazione",
    "documentiFisici"
})
public class DocumentoArchivisticoIRC
    extends InfoRichiestaCreazione
{

    protected ObjectIdType parentFolderId;
    @XmlElement(required = true)
    protected EnumTipoDocumentoArchivistico tipoDocumento;
    protected boolean allegato;
    protected GruppoAllegatiPropertiesType gruppoAllegati;
    @XmlElement(required = true)
    protected ObjectIdType classificazionePrincipale;
    @XmlElement(required = true)
    protected PropertiesType propertiesDocumento;
    @XmlElement(required = true)
    protected PropertiesType propertiesClassificazione;
    protected List<DocumentoFisicoIRC> documentiFisici;

    /**
     * Recupera il valore della proprietà parentFolderId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getParentFolderId() {
        return parentFolderId;
    }

    /**
     * Imposta il valore della proprietà parentFolderId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setParentFolderId(ObjectIdType value) {
        this.parentFolderId = value;
    }

    /**
     * Recupera il valore della proprietà tipoDocumento.
     * 
     * @return
     *     possible object is
     *     {@link EnumTipoDocumentoArchivistico }
     *     
     */
    public EnumTipoDocumentoArchivistico getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Imposta il valore della proprietà tipoDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTipoDocumentoArchivistico }
     *     
     */
    public void setTipoDocumento(EnumTipoDocumentoArchivistico value) {
        this.tipoDocumento = value;
    }

    /**
     * Recupera il valore della proprietà allegato.
     * 
     */
    public boolean isAllegato() {
        return allegato;
    }

    /**
     * Imposta il valore della proprietà allegato.
     * 
     */
    public void setAllegato(boolean value) {
        this.allegato = value;
    }

    /**
     * Recupera il valore della proprietà gruppoAllegati.
     * 
     * @return
     *     possible object is
     *     {@link GruppoAllegatiPropertiesType }
     *     
     */
    public GruppoAllegatiPropertiesType getGruppoAllegati() {
        return gruppoAllegati;
    }

    /**
     * Imposta il valore della proprietà gruppoAllegati.
     * 
     * @param value
     *     allowed object is
     *     {@link GruppoAllegatiPropertiesType }
     *     
     */
    public void setGruppoAllegati(GruppoAllegatiPropertiesType value) {
        this.gruppoAllegati = value;
    }

    /**
     * Recupera il valore della proprietà classificazionePrincipale.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getClassificazionePrincipale() {
        return classificazionePrincipale;
    }

    /**
     * Imposta il valore della proprietà classificazionePrincipale.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setClassificazionePrincipale(ObjectIdType value) {
        this.classificazionePrincipale = value;
    }

    /**
     * Recupera il valore della proprietà propertiesDocumento.
     * 
     * @return
     *     possible object is
     *     {@link PropertiesType }
     *     
     */
    public PropertiesType getPropertiesDocumento() {
        return propertiesDocumento;
    }

    /**
     * Imposta il valore della proprietà propertiesDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertiesType }
     *     
     */
    public void setPropertiesDocumento(PropertiesType value) {
        this.propertiesDocumento = value;
    }

    /**
     * Recupera il valore della proprietà propertiesClassificazione.
     * 
     * @return
     *     possible object is
     *     {@link PropertiesType }
     *     
     */
    public PropertiesType getPropertiesClassificazione() {
        return propertiesClassificazione;
    }

    /**
     * Imposta il valore della proprietà propertiesClassificazione.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertiesType }
     *     
     */
    public void setPropertiesClassificazione(PropertiesType value) {
        this.propertiesClassificazione = value;
    }

    /**
     * Gets the value of the documentiFisici property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentiFisici property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentiFisici().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentoFisicoIRC }
     * 
     * 
     */
    public List<DocumentoFisicoIRC> getDocumentiFisici() {
        if (documentiFisici == null) {
            documentiFisici = new ArrayList<DocumentoFisicoIRC>();
        }
        return this.documentiFisici;
    }

}
