
package it.doqui.dosign.dosign.business.session.dosign.remotev2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Env.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="Env">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="BOX"/>
 *     &lt;enumeration value="CLOUD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Env")
@XmlEnum
public enum Env {

    BOX,
    CLOUD;

    public String value() {
        return name();
    }

    public static Env fromValue(String v) {
        return valueOf(v);
    }

}
