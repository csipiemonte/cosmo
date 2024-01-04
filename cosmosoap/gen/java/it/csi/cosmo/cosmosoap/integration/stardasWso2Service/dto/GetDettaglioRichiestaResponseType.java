
package it.csi.cosmo.cosmosoap.integration.stardasWso2Service.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import it.csi.cosmo.cosmosoap.facade.dto.common.DettaglioCompletoRichiestaType;
import it.csi.cosmo.cosmosoap.facade.dto.common.ResponseType;
import it.csi.cosmo.cosmosoap.facade.dto.common.ResultType;


/**
 * <p>Classe Java per GetDettaglioRichiestaResponseType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="GetDettaglioRichiestaResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.csi.it/stardas/services/StardasCommonTypes}ResponseType">
 *       &lt;sequence>
 *         &lt;element name="Result" type="{http://www.csi.it/stardas/services/StardasCommonTypes}ResultType"/>
 *         &lt;element name="DettaglioRichiesta" type="{http://www.csi.it/stardas/services/StardasCommonTypes}DettaglioCompletoRichiestaType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDettaglioRichiestaResponseType", propOrder = {
    "rest"
})
public class GetDettaglioRichiestaResponseType
    extends ResponseType
{

    @XmlElementRefs({
        @XmlElementRef(name = "DettaglioRichiesta", namespace = "http://www.csi.it/stardas/wso2/StardasService", type = JAXBElement.class),
        @XmlElementRef(name = "Result", namespace = "http://www.csi.it/stardas/wso2/StardasService", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> rest;

    /**
     * Recupera il resto del modello di contenuto. 
     * 
     * <p>
     * Questa proprietà "catch-all" viene recuperata per il seguente motivo: 
     * Il nome di campo "Result" è usato da due diverse parti di uno schema. Vedere: 
     * riga 109 di file:/C:/Users/grtommason/Work/viste/CSI_GIT/GitHub/cosmo/cosmosoap/src/resources/wsdl/StardasWso2ServiceTypes.xsd
     * riga 8 di file:/C:/Users/grtommason/Work/viste/CSI_GIT/GitHub/cosmo/cosmosoap/src/resources/wsdl/StardasCommonTypes.xsd
     * <p>
     * Per eliminare questa proprietà, applicare una personalizzazione della proprietà a una 
     * delle seguenti due dichiarazioni per modificarne il nome: 
     * Gets the value of the rest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ResultType }{@code >}
     * {@link JAXBElement }{@code <}{@link DettaglioCompletoRichiestaType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getRest() {
        if (rest == null) {
            rest = new ArrayList<JAXBElement<?>>();
        }
        return this.rest;
    }

}
