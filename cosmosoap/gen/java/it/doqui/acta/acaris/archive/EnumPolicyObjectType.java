
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumPolicyObjectType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumPolicyObjectType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="ActaACEPropertiesType"/>
 *     &lt;enumeration value="PrincipalId"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumPolicyObjectType")
@XmlEnum
public enum EnumPolicyObjectType {

    @XmlEnumValue("ActaACEPropertiesType")
    ACTA_ACE_PROPERTIES_TYPE("ActaACEPropertiesType"),
    @XmlEnumValue("PrincipalId")
    PRINCIPAL_ID("PrincipalId");
    private final String value;

    EnumPolicyObjectType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumPolicyObjectType fromValue(String v) {
        for (EnumPolicyObjectType c: EnumPolicyObjectType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
