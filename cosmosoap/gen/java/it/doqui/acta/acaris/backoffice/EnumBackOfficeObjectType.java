
package it.doqui.acta.acaris.backoffice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumBackOfficeObjectType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumBackOfficeObjectType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="EntePropertiesType"/>
 *     &lt;enumeration value="GruppoAOOPropertiesType"/>
 *     &lt;enumeration value="AOOPropertiesType"/>
 *     &lt;enumeration value="StrutturaPropertiesType"/>
 *     &lt;enumeration value="NodoPropertiesType"/>
 *     &lt;enumeration value="UtentePropertiesType"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumBackOfficeObjectType")
@XmlEnum
public enum EnumBackOfficeObjectType {

    @XmlEnumValue("EntePropertiesType")
    ENTE_PROPERTIES_TYPE("EntePropertiesType"),
    @XmlEnumValue("GruppoAOOPropertiesType")
    GRUPPO_AOO_PROPERTIES_TYPE("GruppoAOOPropertiesType"),
    @XmlEnumValue("AOOPropertiesType")
    AOO_PROPERTIES_TYPE("AOOPropertiesType"),
    @XmlEnumValue("StrutturaPropertiesType")
    STRUTTURA_PROPERTIES_TYPE("StrutturaPropertiesType"),
    @XmlEnumValue("NodoPropertiesType")
    NODO_PROPERTIES_TYPE("NodoPropertiesType"),
    @XmlEnumValue("UtentePropertiesType")
    UTENTE_PROPERTIES_TYPE("UtentePropertiesType");
    private final String value;

    EnumBackOfficeObjectType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumBackOfficeObjectType fromValue(String v) {
        for (EnumBackOfficeObjectType c: EnumBackOfficeObjectType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
