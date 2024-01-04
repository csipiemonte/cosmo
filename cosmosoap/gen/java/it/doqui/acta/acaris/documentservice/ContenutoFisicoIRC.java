
package it.doqui.acta.acaris.documentservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.AcarisContentStreamType;
import it.doqui.acta.acaris.common.ChangeTokenType;
import it.doqui.acta.acaris.common.EnumStreamId;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.common.PropertiesType;


/**
 * <p>Classe Java per ContenutoFisicoIRC complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ContenutoFisicoIRC">
 *   &lt;complexContent>
 *     &lt;extension base="{documentservice.acaris.acta.doqui.it}InfoRichiestaCreazione">
 *       &lt;sequence>
 *         &lt;element name="documentoFisico" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="dataUltimoAggiornamentoDocumento" type="{common.acaris.acta.doqui.it}ChangeTokenType"/>
 *         &lt;element name="propertiesContenutoFisico" type="{common.acaris.acta.doqui.it}PropertiesType"/>
 *         &lt;element name="tipo" type="{common.acaris.acta.doqui.it}enumStreamId"/>
 *         &lt;element name="stream" type="{common.acaris.acta.doqui.it}acarisContentStreamType"/>
 *         &lt;element name="azioniVerificaFirma" type="{documentservice.acaris.acta.doqui.it}StepErrorAction" maxOccurs="7" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContenutoFisicoIRC", propOrder = {
    "documentoFisico",
    "dataUltimoAggiornamentoDocumento",
    "propertiesContenutoFisico",
    "tipo",
    "stream",
    "azioniVerificaFirma"
})
public class ContenutoFisicoIRC
    extends InfoRichiestaCreazione
{

    @XmlElement(required = true)
    protected ObjectIdType documentoFisico;
    @XmlElement(required = true)
    protected ChangeTokenType dataUltimoAggiornamentoDocumento;
    @XmlElement(required = true)
    protected PropertiesType propertiesContenutoFisico;
    @XmlElement(required = true)
    protected EnumStreamId tipo;
    @XmlElement(required = true)
    protected AcarisContentStreamType stream;
    protected List<StepErrorAction> azioniVerificaFirma;

    /**
     * Recupera il valore della proprietà documentoFisico.
     * 
     * @return
     *     possible object is
     *     {@link ObjectIdType }
     *     
     */
    public ObjectIdType getDocumentoFisico() {
        return documentoFisico;
    }

    /**
     * Imposta il valore della proprietà documentoFisico.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectIdType }
     *     
     */
    public void setDocumentoFisico(ObjectIdType value) {
        this.documentoFisico = value;
    }

    /**
     * Recupera il valore della proprietà dataUltimoAggiornamentoDocumento.
     * 
     * @return
     *     possible object is
     *     {@link ChangeTokenType }
     *     
     */
    public ChangeTokenType getDataUltimoAggiornamentoDocumento() {
        return dataUltimoAggiornamentoDocumento;
    }

    /**
     * Imposta il valore della proprietà dataUltimoAggiornamentoDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link ChangeTokenType }
     *     
     */
    public void setDataUltimoAggiornamentoDocumento(ChangeTokenType value) {
        this.dataUltimoAggiornamentoDocumento = value;
    }

    /**
     * Recupera il valore della proprietà propertiesContenutoFisico.
     * 
     * @return
     *     possible object is
     *     {@link PropertiesType }
     *     
     */
    public PropertiesType getPropertiesContenutoFisico() {
        return propertiesContenutoFisico;
    }

    /**
     * Imposta il valore della proprietà propertiesContenutoFisico.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertiesType }
     *     
     */
    public void setPropertiesContenutoFisico(PropertiesType value) {
        this.propertiesContenutoFisico = value;
    }

    /**
     * Recupera il valore della proprietà tipo.
     * 
     * @return
     *     possible object is
     *     {@link EnumStreamId }
     *     
     */
    public EnumStreamId getTipo() {
        return tipo;
    }

    /**
     * Imposta il valore della proprietà tipo.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumStreamId }
     *     
     */
    public void setTipo(EnumStreamId value) {
        this.tipo = value;
    }

    /**
     * Recupera il valore della proprietà stream.
     * 
     * @return
     *     possible object is
     *     {@link AcarisContentStreamType }
     *     
     */
    public AcarisContentStreamType getStream() {
        return stream;
    }

    /**
     * Imposta il valore della proprietà stream.
     * 
     * @param value
     *     allowed object is
     *     {@link AcarisContentStreamType }
     *     
     */
    public void setStream(AcarisContentStreamType value) {
        this.stream = value;
    }

    /**
     * Gets the value of the azioniVerificaFirma property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the azioniVerificaFirma property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAzioniVerificaFirma().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StepErrorAction }
     * 
     * 
     */
    public List<StepErrorAction> getAzioniVerificaFirma() {
        if (azioniVerificaFirma == null) {
            azioniVerificaFirma = new ArrayList<StepErrorAction>();
        }
        return this.azioniVerificaFirma;
    }

}
