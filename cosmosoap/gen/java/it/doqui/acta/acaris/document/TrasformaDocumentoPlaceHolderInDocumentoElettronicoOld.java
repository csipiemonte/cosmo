
package it.doqui.acta.acaris.document;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.AcarisContentStreamType;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.common.PrincipalIdType;
import it.doqui.acta.acaris.common.PropertiesType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="repositoryId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="principalId" type="{common.acaris.acta.doqui.it}PrincipalIdType"/>
 *         &lt;element name="placeHolderId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="placeHolderId" type="{common.acaris.acta.doqui.it}ObjectIdType"/>
 *         &lt;element name="tipoDocFisicoId" type="{common.acaris.acta.doqui.it}integer"/>
 *         &lt;element name="composizioneId" type="{common.acaris.acta.doqui.it}integer"/>
 *         &lt;element name="multiplo" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="rimandareOperazioneSbustamento" type="{common.acaris.acta.doqui.it}boolean"/>
 *         &lt;element name="documentoFisico" type="{common.acaris.acta.doqui.it}PropertiesType" maxOccurs="unbounded"/>
 *         &lt;element name="contenutoFisico" type="{common.acaris.acta.doqui.it}PropertiesType" maxOccurs="unbounded"/>
 *         &lt;element name="contentStream" type="{common.acaris.acta.doqui.it}acarisContentStreamType" maxOccurs="unbounded"/>
 *         &lt;element name="annotazione" type="{common.acaris.acta.doqui.it}PropertiesType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "content"
})
@XmlRootElement(name = "trasformaDocumentoPlaceHolderInDocumentoElettronicoOld")
public class TrasformaDocumentoPlaceHolderInDocumentoElettronicoOld {

    @XmlElementRefs({
        @XmlElementRef(name = "principalId", type = JAXBElement.class),
        @XmlElementRef(name = "rimandareOperazioneSbustamento", type = JAXBElement.class),
        @XmlElementRef(name = "tipoDocFisicoId", type = JAXBElement.class),
        @XmlElementRef(name = "repositoryId", type = JAXBElement.class),
        @XmlElementRef(name = "placeHolderId", type = JAXBElement.class),
        @XmlElementRef(name = "contenutoFisico", type = JAXBElement.class),
        @XmlElementRef(name = "annotazione", type = JAXBElement.class),
        @XmlElementRef(name = "composizioneId", type = JAXBElement.class),
        @XmlElementRef(name = "contentStream", type = JAXBElement.class),
        @XmlElementRef(name = "documentoFisico", type = JAXBElement.class),
        @XmlElementRef(name = "multiplo", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> content;

    /**
     * Recupera il resto del modello di contenuto. 
     * 
     * <p>
     * Questa proprietà "catch-all" viene recuperata per il seguente motivo: 
     * Il nome di campo "PlaceHolderId" è usato da due diverse parti di uno schema. Vedere: 
     * riga 66 di file:/C:/Users/grtommason/Work/viste/CSI_GIT/GitHub/cosmo/cosmosoap/src/resources/wsdl/ACARIS-DocumentMessaging.xsd
     * riga 65 di file:/C:/Users/grtommason/Work/viste/CSI_GIT/GitHub/cosmo/cosmosoap/src/resources/wsdl/ACARIS-DocumentMessaging.xsd
     * <p>
     * Per eliminare questa proprietà, applicare una personalizzazione della proprietà a una 
     * delle seguenti due dichiarazioni per modificarne il nome: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link PrincipalIdType }{@code >}
     * {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * {@link JAXBElement }{@code <}{@link ObjectIdType }{@code >}
     * {@link JAXBElement }{@code <}{@link ObjectIdType }{@code >}
     * {@link JAXBElement }{@code <}{@link PropertiesType }{@code >}
     * {@link JAXBElement }{@code <}{@link PropertiesType }{@code >}
     * {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * {@link JAXBElement }{@code <}{@link AcarisContentStreamType }{@code >}
     * {@link JAXBElement }{@code <}{@link PropertiesType }{@code >}
     * {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getContent() {
        if (content == null) {
            content = new ArrayList<JAXBElement<?>>();
        }
        return this.content;
    }

}
