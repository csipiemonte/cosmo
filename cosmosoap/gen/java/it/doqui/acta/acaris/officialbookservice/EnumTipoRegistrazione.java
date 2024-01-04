
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipoRegistrazione.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipoRegistrazione">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Arrivo"/>
 *     &lt;enumeration value="Partenza"/>
 *     &lt;enumeration value="Interna"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipoRegistrazione")
@XmlEnum
public enum EnumTipoRegistrazione {

    @XmlEnumValue("Arrivo")
    ARRIVO("Arrivo"),
    @XmlEnumValue("Partenza")
    PARTENZA("Partenza"),
    @XmlEnumValue("Interna")
    INTERNA("Interna");
    private final String value;

    EnumTipoRegistrazione(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipoRegistrazione fromValue(String v) {
        for (EnumTipoRegistrazione c: EnumTipoRegistrazione.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
