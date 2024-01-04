
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipoPFPG.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipoPFPG">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="PersonaFisica"/>
 *     &lt;enumeration value="PersonaGiuridica"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipoPFPG")
@XmlEnum
public enum EnumTipoPFPG {

    @XmlEnumValue("PersonaFisica")
    PERSONA_FISICA("PersonaFisica"),
    @XmlEnumValue("PersonaGiuridica")
    PERSONA_GIURIDICA("PersonaGiuridica");
    private final String value;

    EnumTipoPFPG(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipoPFPG fromValue(String v) {
        for (EnumTipoPFPG c: EnumTipoPFPG.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
