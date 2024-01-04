
package it.doqui.acta.acaris.common.prt;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumCodiceTipoSoggetto.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumCodiceTipoSoggetto">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="AOO"/>
 *     &lt;enumeration value="STR"/>
 *     &lt;enumeration value="NOD"/>
 *     &lt;enumeration value="UT"/>
 *     &lt;enumeration value="PF"/>
 *     &lt;enumeration value="PG"/>
 *     &lt;enumeration value="UL"/>
 *     &lt;enumeration value="AOO-INPA"/>
 *     &lt;enumeration value="DIP"/>
 *     &lt;enumeration value="BA"/>
 *     &lt;enumeration value="FIL"/>
 *     &lt;enumeration value="ORG"/>
 *     &lt;enumeration value="ALT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumCodiceTipoSoggetto", namespace = "prt.common.acaris.acta.doqui.it")
@XmlEnum
public enum EnumCodiceTipoSoggetto {

    AOO("AOO"),
    STR("STR"),
    NOD("NOD"),
    UT("UT"),
    PF("PF"),
    PG("PG"),
    UL("UL"),
    @XmlEnumValue("AOO-INPA")
    AOO_INPA("AOO-INPA"),
    DIP("DIP"),
    BA("BA"),
    FIL("FIL"),
    ORG("ORG"),
    ALT("ALT");
    private final String value;

    EnumCodiceTipoSoggetto(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumCodiceTipoSoggetto fromValue(String v) {
        for (EnumCodiceTipoSoggetto c: EnumCodiceTipoSoggetto.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
