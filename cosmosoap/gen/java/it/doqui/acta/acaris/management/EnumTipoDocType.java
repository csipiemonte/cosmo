
package it.doqui.acta.acaris.management;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipoDocType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipoDocType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Determinazione"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipoDocType")
@XmlEnum
public enum EnumTipoDocType {

    @XmlEnumValue("Determinazione")
    DETERMINAZIONE("Determinazione");
    private final String value;

    EnumTipoDocType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipoDocType fromValue(String v) {
        for (EnumTipoDocType c: EnumTipoDocType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
