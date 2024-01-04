
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumRuoloCorrispondente.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumRuoloCorrispondente">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="PerCompetenza"/>
 *     &lt;enumeration value="PerQuantoDiCompetenza"/>
 *     &lt;enumeration value="PerConoscenza"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumRuoloCorrispondente")
@XmlEnum
public enum EnumRuoloCorrispondente {

    @XmlEnumValue("PerCompetenza")
    PER_COMPETENZA("PerCompetenza"),
    @XmlEnumValue("PerQuantoDiCompetenza")
    PER_QUANTO_DI_COMPETENZA("PerQuantoDiCompetenza"),
    @XmlEnumValue("PerConoscenza")
    PER_CONOSCENZA("PerConoscenza");
    private final String value;

    EnumRuoloCorrispondente(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumRuoloCorrispondente fromValue(String v) {
        for (EnumRuoloCorrispondente c: EnumRuoloCorrispondente.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
