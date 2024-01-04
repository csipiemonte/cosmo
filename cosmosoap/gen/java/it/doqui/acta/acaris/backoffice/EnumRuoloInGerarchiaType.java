
package it.doqui.acta.acaris.backoffice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumRuoloInGerarchiaType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumRuoloInGerarchiaType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="parent"/>
 *     &lt;enumeration value="child"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumRuoloInGerarchiaType")
@XmlEnum
public enum EnumRuoloInGerarchiaType {

    @XmlEnumValue("parent")
    PARENT("parent"),
    @XmlEnumValue("child")
    CHILD("child");
    private final String value;

    EnumRuoloInGerarchiaType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumRuoloInGerarchiaType fromValue(String v) {
        for (EnumRuoloInGerarchiaType c: EnumRuoloInGerarchiaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
