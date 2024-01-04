
package it.doqui.acta.acaris.backoffice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumServiceType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumServiceType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Archive"/>
 *     &lt;enumeration value="BackOffice"/>
 *     &lt;enumeration value="Management"/>
 *     &lt;enumeration value="OfficialBook"/>
 *     &lt;enumeration value="Sms"/>
 *     &lt;enumeration value="SubjectRegistry"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumServiceType")
@XmlEnum
public enum EnumServiceType {

    @XmlEnumValue("Archive")
    ARCHIVE("Archive"),
    @XmlEnumValue("BackOffice")
    BACK_OFFICE("BackOffice"),
    @XmlEnumValue("Management")
    MANAGEMENT("Management"),
    @XmlEnumValue("OfficialBook")
    OFFICIAL_BOOK("OfficialBook"),
    @XmlEnumValue("Sms")
    SMS("Sms"),
    @XmlEnumValue("SubjectRegistry")
    SUBJECT_REGISTRY("SubjectRegistry");
    private final String value;

    EnumServiceType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumServiceType fromValue(String v) {
        for (EnumServiceType c: EnumServiceType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
