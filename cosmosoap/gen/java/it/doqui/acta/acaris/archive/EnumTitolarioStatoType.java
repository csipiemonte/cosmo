
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTitolarioStatoType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTitolarioStatoType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Bozza"/>
 *     &lt;enumeration value="Preattivo"/>
 *     &lt;enumeration value="Attivo"/>
 *     &lt;enumeration value="Disattivo"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTitolarioStatoType")
@XmlEnum
public enum EnumTitolarioStatoType {

    @XmlEnumValue("Bozza")
    BOZZA("Bozza"),
    @XmlEnumValue("Preattivo")
    PREATTIVO("Preattivo"),
    @XmlEnumValue("Attivo")
    ATTIVO("Attivo"),
    @XmlEnumValue("Disattivo")
    DISATTIVO("Disattivo");
    private final String value;

    EnumTitolarioStatoType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTitolarioStatoType fromValue(String v) {
        for (EnumTitolarioStatoType c: EnumTitolarioStatoType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
