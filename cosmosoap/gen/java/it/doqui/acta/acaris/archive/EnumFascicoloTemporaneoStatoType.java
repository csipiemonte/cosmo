
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumFascicoloTemporaneoStatoType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumFascicoloTemporaneoStatoType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Attivo"/>
 *     &lt;enumeration value="Disattivo"/>
 *     &lt;enumeration value="Congelato"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumFascicoloTemporaneoStatoType")
@XmlEnum
public enum EnumFascicoloTemporaneoStatoType {

    @XmlEnumValue("Attivo")
    ATTIVO("Attivo"),
    @XmlEnumValue("Disattivo")
    DISATTIVO("Disattivo"),
    @XmlEnumValue("Congelato")
    CONGELATO("Congelato");
    private final String value;

    EnumFascicoloTemporaneoStatoType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumFascicoloTemporaneoStatoType fromValue(String v) {
        for (EnumFascicoloTemporaneoStatoType c: EnumFascicoloTemporaneoStatoType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
