
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipologiaCorrispondente.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipologiaCorrispondente">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="PA"/>
 *     &lt;enumeration value="AE"/>
 *     &lt;enumeration value="PG"/>
 *     &lt;enumeration value="PF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipologiaCorrispondente")
@XmlEnum
public enum EnumTipologiaCorrispondente {

    PA,
    AE,
    PG,
    PF;

    public String value() {
        return name();
    }

    public static EnumTipologiaCorrispondente fromValue(String v) {
        return valueOf(v);
    }

}
