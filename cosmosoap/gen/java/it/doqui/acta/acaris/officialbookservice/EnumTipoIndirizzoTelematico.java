
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipoIndirizzoTelematico.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipoIndirizzoTelematico">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="smtp"/>
 *     &lt;enumeration value="url"/>
 *     &lt;enumeration value="other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipoIndirizzoTelematico")
@XmlEnum
public enum EnumTipoIndirizzoTelematico {

    @XmlEnumValue("smtp")
    SMTP("smtp"),
    @XmlEnumValue("url")
    URL("url"),
    @XmlEnumValue("other")
    OTHER("other");
    private final String value;

    EnumTipoIndirizzoTelematico(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipoIndirizzoTelematico fromValue(String v) {
        for (EnumTipoIndirizzoTelematico c: EnumTipoIndirizzoTelematico.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
