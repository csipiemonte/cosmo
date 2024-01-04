
package it.doqui.dosign.dosign.business.session.dosign.remotev2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per remotePdfSignatureDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="remotePdfSignatureDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://remotev2.dosign.session.business.dosign.dosign.doqui.it/}remoteSignatureDto">
 *       &lt;sequence>
 *         &lt;element name="graphicDto" type="{http://remotev2.dosign.session.business.dosign.dosign.doqui.it/}remoteGraphicDto" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "remotePdfSignatureDto", propOrder = {
    "graphicDto"
})
public class RemotePdfSignatureDto
    extends RemoteSignatureDto
{

    protected RemoteGraphicDto graphicDto;

    /**
     * Recupera il valore della proprietà graphicDto.
     * 
     * @return
     *     possible object is
     *     {@link RemoteGraphicDto }
     *     
     */
    public RemoteGraphicDto getGraphicDto() {
        return graphicDto;
    }

    /**
     * Imposta il valore della proprietà graphicDto.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoteGraphicDto }
     *     
     */
    public void setGraphicDto(RemoteGraphicDto value) {
        this.graphicDto = value;
    }

}
