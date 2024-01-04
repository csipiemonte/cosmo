
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="repositoryInfo" type="{archive.acaris.acta.doqui.it}acarisRepositoryInfoType"/>
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
    "repositoryInfo"
})
@XmlRootElement(name = "getRepositoryInfoResponse")
public class GetRepositoryInfoResponse {

    @XmlElement(required = true)
    protected AcarisRepositoryInfoType repositoryInfo;

    /**
     * Recupera il valore della proprietà repositoryInfo.
     * 
     * @return
     *     possible object is
     *     {@link AcarisRepositoryInfoType }
     *     
     */
    public AcarisRepositoryInfoType getRepositoryInfo() {
        return repositoryInfo;
    }

    /**
     * Imposta il valore della proprietà repositoryInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link AcarisRepositoryInfoType }
     *     
     */
    public void setRepositoryInfo(AcarisRepositoryInfoType value) {
        this.repositoryInfo = value;
    }

}
