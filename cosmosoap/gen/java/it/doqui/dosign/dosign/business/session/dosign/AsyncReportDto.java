
package it.doqui.dosign.dosign.business.session.dosign;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per asyncReportDto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="asyncReportDto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="report" type="{http://dosign.session.business.dosign.dosign.doqui.it/}verifyReport" minOccurs="0"/>
 *         &lt;element name="status" type="{http://dosign.session.business.dosign.dosign.doqui.it/}status" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "asyncReportDto", propOrder = {
    "report",
    "status"
})
public class AsyncReportDto {

    protected VerifyReport report;
    protected Status status;

    /**
     * Recupera il valore della proprietà report.
     * 
     * @return
     *     possible object is
     *     {@link VerifyReport }
     *     
     */
    public VerifyReport getReport() {
        return report;
    }

    /**
     * Imposta il valore della proprietà report.
     * 
     * @param value
     *     allowed object is
     *     {@link VerifyReport }
     *     
     */
    public void setReport(VerifyReport value) {
        this.report = value;
    }

    /**
     * Recupera il valore della proprietà status.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Imposta il valore della proprietà status.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }

}
