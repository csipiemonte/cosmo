
package it.csi.cosmo.cosmosoap.integration.soap.dosign.validation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per timeStampedDataInfoDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="timeStampedDataInfoDto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}errorInformationDto">
 *       &lt;sequence>
 *         &lt;element name="timeStamp" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}timeStampDto" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="verifyReport" type="{http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/}verifyReportDto" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "timeStampedDataInfoDto", propOrder = {
    "timeStamp",
    "verifyReport"
})
public class TimeStampedDataInfoDto
    extends ErrorInformationDto
{

    @XmlElement(nillable = true)
    protected List<TimeStampDto> timeStamp;
    protected VerifyReportDto verifyReport;

    /**
     * Gets the value of the timeStamp property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the timeStamp property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimeStamp().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TimeStampDto }
     * 
     * 
     */
    public List<TimeStampDto> getTimeStamp() {
        if (timeStamp == null) {
            timeStamp = new ArrayList<TimeStampDto>();
        }
        return this.timeStamp;
    }

    /**
     * Recupera il valore della proprietà verifyReport.
     * 
     * @return
     *     possible object is
     *     {@link VerifyReportDto }
     *     
     */
    public VerifyReportDto getVerifyReport() {
        return verifyReport;
    }

    /**
     * Imposta il valore della proprietà verifyReport.
     * 
     * @param value
     *     allowed object is
     *     {@link VerifyReportDto }
     *     
     */
    public void setVerifyReport(VerifyReportDto value) {
        this.verifyReport = value;
    }

}
