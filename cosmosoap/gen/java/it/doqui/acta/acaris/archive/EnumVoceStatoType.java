
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumVoceStatoType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumVoceStatoType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Bozza"/>
 *     &lt;enumeration value="Preattiva"/>
 *     &lt;enumeration value="Attiva"/>
 *     &lt;enumeration value="Disattiva"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumVoceStatoType")
@XmlEnum
public enum EnumVoceStatoType {

    @XmlEnumValue("Bozza")
    BOZZA("Bozza"),
    @XmlEnumValue("Preattiva")
    PREATTIVA("Preattiva"),
    @XmlEnumValue("Attiva")
    ATTIVA("Attiva"),
    @XmlEnumValue("Disattiva")
    DISATTIVA("Disattiva");
    private final String value;

    EnumVoceStatoType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumVoceStatoType fromValue(String v) {
        for (EnumVoceStatoType c: EnumVoceStatoType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
