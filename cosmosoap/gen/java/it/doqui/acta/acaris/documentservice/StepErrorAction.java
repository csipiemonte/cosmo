
package it.doqui.acta.acaris.documentservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per StepErrorAction complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="StepErrorAction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="step" type="{common.acaris.acta.doqui.it}integer"/>
 *         &lt;element name="action" type="{documentservice.acaris.acta.doqui.it}enumStepErrorAction"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StepErrorAction", propOrder = {
    "step",
    "action"
})
public class StepErrorAction {

    protected int step;
    @XmlElement(required = true)
    protected EnumStepErrorAction action;

    /**
     * Recupera il valore della proprietà step.
     * 
     */
    public int getStep() {
        return step;
    }

    /**
     * Imposta il valore della proprietà step.
     * 
     */
    public void setStep(int value) {
        this.step = value;
    }

    /**
     * Recupera il valore della proprietà action.
     * 
     * @return
     *     possible object is
     *     {@link EnumStepErrorAction }
     *     
     */
    public EnumStepErrorAction getAction() {
        return action;
    }

    /**
     * Imposta il valore della proprietà action.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumStepErrorAction }
     *     
     */
    public void setAction(EnumStepErrorAction value) {
        this.action = value;
    }

}
