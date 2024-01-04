
package it.doqui.acta.acaris.common.prt;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumPFPGUL.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumPFPGUL">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="PF"/>
 *     &lt;enumeration value="PG"/>
 *     &lt;enumeration value="UL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumPFPGUL", namespace = "prt.common.acaris.acta.doqui.it")
@XmlEnum
public enum EnumPFPGUL {

    PF,
    PG,
    UL;

    public String value() {
        return name();
    }

    public static EnumPFPGUL fromValue(String v) {
        return valueOf(v);
    }

}
