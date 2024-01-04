
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipoIE.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipoIE">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Interno"/>
 *     &lt;enumeration value="Esterno"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipoIE")
@XmlEnum
public enum EnumTipoIE {

    @XmlEnumValue("Interno")
    INTERNO("Interno"),
    @XmlEnumValue("Esterno")
    ESTERNO("Esterno");
    private final String value;

    EnumTipoIE(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipoIE fromValue(String v) {
        for (EnumTipoIE c: EnumTipoIE.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
