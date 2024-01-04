
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.ChangeTokenType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 * <p>Classe Java per IdentificazioneRegistrazione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="IdentificazioneRegistrazione">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="registrazioneId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="folderId" type="{common.acaris.acta.doqui.it}ObjectIdType" minOccurs="0"/>
 *         &lt;element name="folderType" type="{officialbookservice.acaris.acta.doqui.it}enumTipoContenitore" minOccurs="0"/>
 *         &lt;element name="dataUltimoAggiornamento" type="{common.acaris.acta.doqui.it}ChangeTokenType"/>
 *         &lt;element name="numero" type="{common.acaris.acta.doqui.it}string"/>
 *         &lt;element name="classificazioneId" type="{common.acaris.acta.doqui.it}ObjectIdType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdentificazioneRegistrazione", propOrder = {
    "registrazioneId",
    "folderId",
    "folderType",
    "dataUltimoAggiornamento",
    "numero",
    "classificazioneId"
})
public class IdentificazioneRegistrazione {

    @XmlElement(required = true)
    protected ObjectIdType registrazioneId;
    protected ObjectIdType folderId;
    protected EnumTipoContenitore folderType;
    @XmlElement(required = true)
    protected ChangeTokenType dataUltimoAggiornamento;
    @XmlElement(required = true)
    protected String numero;
    protected ObjectIdType classificazioneId;

    /**
     * Recupera il valore della proprietà registrazioneId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getRegistrazioneId() {
        return registrazioneId;
    }

    /**
     * Imposta il valore della proprietà registrazioneId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setRegistrazioneId(ObjectIdType value) {
        this.registrazioneId = value;
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
     * Recupera il valore della proprietà dataUltimoAggiornamento.
     * 
     * @return
     *     possible object is
     *     {@link ChangeTokenType }
     *     
     */
    public ChangeTokenType getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }

    /**
     * Imposta il valore della proprietà dataUltimoAggiornamento.
     * 
     * @param value
     *     allowed object is
     *     {@link ChangeTokenType }
     *     
     */
    public void setDataUltimoAggiornamento(ChangeTokenType value) {
        this.dataUltimoAggiornamento = value;
    }

    /**
     * Recupera il valore della proprietà numero.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Imposta il valore della proprietà numero.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumero(String value) {
        this.numero = value;
    }

    /**
     * Recupera il valore della proprietà classificazioneId.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getClassificazioneId() {
        return classificazioneId;
    }

    /**
     * Imposta il valore della proprietà classificazioneId.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setClassificazioneId(ObjectIdType value) {
        this.classificazioneId = value;
    }

}
