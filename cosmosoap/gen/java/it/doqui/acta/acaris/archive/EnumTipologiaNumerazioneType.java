
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipologiaNumerazioneType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipologiaNumerazioneType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Annuale"/>
 *     &lt;enumeration value="Legislatura"/>
 *     &lt;enumeration value="Libera"/>
 *     &lt;enumeration value="Continua"/>
 *     &lt;enumeration value="Ereditata"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipologiaNumerazioneType")
@XmlEnum
public enum EnumTipologiaNumerazioneType {

    @XmlEnumValue("Annuale")
    ANNUALE("Annuale"),
    @XmlEnumValue("Legislatura")
    LEGISLATURA("Legislatura"),
    @XmlEnumValue("Libera")
    LIBERA("Libera"),
    @XmlEnumValue("Continua")
    CONTINUA("Continua"),
    @XmlEnumValue("Ereditata")
    EREDITATA("Ereditata");
    private final String value;

    EnumTipologiaNumerazioneType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipologiaNumerazioneType fromValue(String v) {
        for (EnumTipologiaNumerazioneType c: EnumTipologiaNumerazioneType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
