
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipoCorrispondente.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipoCorrispondente">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Mittente"/>
 *     &lt;enumeration value="Destinatario"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipoCorrispondente")
@XmlEnum
public enum EnumTipoCorrispondente {

    @XmlEnumValue("Mittente")
    MITTENTE("Mittente"),
    @XmlEnumValue("Destinatario")
    DESTINATARIO("Destinatario");
    private final String value;

    EnumTipoCorrispondente(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipoCorrispondente fromValue(String v) {
        for (EnumTipoCorrispondente c: EnumTipoCorrispondente.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
