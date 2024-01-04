
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumStatoRegistrazione.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumStatoRegistrazione">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Registrata"/>
 *     &lt;enumeration value="Modificata"/>
 *     &lt;enumeration value="Annullata"/>
 *     &lt;enumeration value="DaInoltrareAltraAOO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumStatoRegistrazione")
@XmlEnum
public enum EnumStatoRegistrazione {

    @XmlEnumValue("Registrata")
    REGISTRATA("Registrata"),
    @XmlEnumValue("Modificata")
    MODIFICATA("Modificata"),
    @XmlEnumValue("Annullata")
    ANNULLATA("Annullata"),
    @XmlEnumValue("DaInoltrareAltraAOO")
    DA_INOLTRARE_ALTRA_AOO("DaInoltrareAltraAOO");
    private final String value;

    EnumStatoRegistrazione(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumStatoRegistrazione fromValue(String v) {
        for (EnumStatoRegistrazione c: EnumStatoRegistrazione.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
