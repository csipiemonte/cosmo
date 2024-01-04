
package it.doqui.acta.acaris.common.prt;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumFonte.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumFonte">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="OA"/>
 *     &lt;enumeration value="SA"/>
 *     &lt;enumeration value="INPA"/>
 *     &lt;enumeration value="HR"/>
 *     &lt;enumeration value="IPA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumFonte", namespace = "prt.common.acaris.acta.doqui.it")
@XmlEnum
public enum EnumFonte {

    OA,
    SA,
    INPA,
    HR,
    IPA;

    public String value() {
        return name();
    }

    public static EnumFonte fromValue(String v) {
        return valueOf(v);
    }

}
