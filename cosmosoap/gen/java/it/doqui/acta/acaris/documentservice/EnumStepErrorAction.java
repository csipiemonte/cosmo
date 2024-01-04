
package it.doqui.acta.acaris.documentservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumStepErrorAction.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumStepErrorAction">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="insert"/>
 *     &lt;enumeration value="exception"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumStepErrorAction")
@XmlEnum
public enum EnumStepErrorAction {

    @XmlEnumValue("insert")
    INSERT("insert"),
    @XmlEnumValue("exception")
    EXCEPTION("exception");
    private final String value;

    EnumStepErrorAction(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumStepErrorAction fromValue(String v) {
        for (EnumStepErrorAction c: EnumStepErrorAction.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
