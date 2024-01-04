
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per verifyReportDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="verifyReportDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}errorInformationDto">
 *       &lt;sequence>
 *         &lt;element name="verifyInfo" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}verifyInfoDto" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "verifyReportDto", propOrder = {
    "verifyInfo"
})
public class VerifyReportDto
    extends ErrorInformationDto
{

    @XmlElement(nillable = true)
    protected List<VerifyInfoDto> verifyInfo;

    /**
     * Gets the value of the verifyInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the verifyInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVerifyInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VerifyInfoDto }
     * 
     * 
     */
    public List<VerifyInfoDto> getVerifyInfo() {
        if (verifyInfo == null) {
            verifyInfo = new ArrayList<VerifyInfoDto>();
        }
        return this.verifyInfo;
    }

}
