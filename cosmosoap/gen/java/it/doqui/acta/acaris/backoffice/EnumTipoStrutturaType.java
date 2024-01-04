
package it.doqui.acta.acaris.backoffice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipoStrutturaType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipoStrutturaType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Gerarchica"/>
 *     &lt;enumeration value="Libera"/>
 *     &lt;enumeration value="Virtuale"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipoStrutturaType")
@XmlEnum
public enum EnumTipoStrutturaType {

    @XmlEnumValue("Gerarchica")
    GERARCHICA("Gerarchica"),
    @XmlEnumValue("Libera")
    LIBERA("Libera"),
    @XmlEnumValue("Virtuale")
    VIRTUALE("Virtuale");
    private final String value;

    EnumTipoStrutturaType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipoStrutturaType fromValue(String v) {
        for (EnumTipoStrutturaType c: EnumTipoStrutturaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
