
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumSerieTipologicaDocumentiStatoType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumSerieTipologicaDocumentiStatoType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Attiva"/>
 *     &lt;enumeration value="Disattiva"/>
 *     &lt;enumeration value="Congelata"/>
 *     &lt;enumeration value="ChiusaInCorrente"/>
 *     &lt;enumeration value="ChiusaInDeposito"/>
 *     &lt;enumeration value="Cancellata"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumSerieTipologicaDocumentiStatoType")
@XmlEnum
public enum EnumSerieTipologicaDocumentiStatoType {

    @XmlEnumValue("Attiva")
    ATTIVA("Attiva"),
    @XmlEnumValue("Disattiva")
    DISATTIVA("Disattiva"),
    @XmlEnumValue("Congelata")
    CONGELATA("Congelata"),
    @XmlEnumValue("ChiusaInCorrente")
    CHIUSA_IN_CORRENTE("ChiusaInCorrente"),
    @XmlEnumValue("ChiusaInDeposito")
    CHIUSA_IN_DEPOSITO("ChiusaInDeposito"),
    @XmlEnumValue("Cancellata")
    CANCELLATA("Cancellata");
    private final String value;

    EnumSerieTipologicaDocumentiStatoType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumSerieTipologicaDocumentiStatoType fromValue(String v) {
        for (EnumSerieTipologicaDocumentiStatoType c: EnumSerieTipologicaDocumentiStatoType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
