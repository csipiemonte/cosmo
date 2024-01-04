
package it.doqui.dosign.dosign.business.session.dosign.remotev2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per TsFormat.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TsFormat">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TSR"/>
 *     &lt;enumeration value="TSD"/>
 *     &lt;enumeration value="PDF"/>
 *     &lt;enumeration value="TST"/>
 *     &lt;enumeration value="M7M"/>
 *     &lt;enumeration value="P7M"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TsFormat")
@XmlEnum
public enum TsFormat {

    TSR("TSR"),
    TSD("TSD"),
    PDF("PDF"),
    TST("TST"),
    @XmlEnumValue("M7M")
    M_7_M("M7M"),
    @XmlEnumValue("P7M")
    P_7_M("P7M");
    private final String value;

    TsFormat(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TsFormat fromValue(String v) {
        for (TsFormat c: TsFormat.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
