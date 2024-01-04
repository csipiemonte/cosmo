
package it.doqui.dosign.dosign.business.session.dosign.remotev2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Mechanism.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="Mechanism">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="RSAMD5"/>
 *     &lt;enumeration value="RSASHA1"/>
 *     &lt;enumeration value="RSASHA256"/>
 *     &lt;enumeration value="RSASHA512"/>
 *     &lt;enumeration value="RSARAW"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Mechanism")
@XmlEnum
public enum Mechanism {

    @XmlEnumValue("RSAMD5")
    RSAMD_5("RSAMD5"),
    @XmlEnumValue("RSASHA1")
    RSASHA_1("RSASHA1"),
    @XmlEnumValue("RSASHA256")
    RSASHA_256("RSASHA256"),
    @XmlEnumValue("RSASHA512")
    RSASHA_512("RSASHA512"),
    RSARAW("RSARAW");
    private final String value;

    Mechanism(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Mechanism fromValue(String v) {
        for (Mechanism c: Mechanism.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
