
package it.doqui.dosign.dosign.business.session.dosign.remotev2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Format.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="Format">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PADES"/>
 *     &lt;enumeration value="CADES"/>
 *     &lt;enumeration value="XADES_ENVELOPED"/>
 *     &lt;enumeration value="XADES_ENVELOPING"/>
 *     &lt;enumeration value="XADES_DETACHED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Format")
@XmlEnum
public enum Format {

    PADES,
    CADES,
    XADES_ENVELOPED,
    XADES_ENVELOPING,
    XADES_DETACHED;

    public String value() {
        return name();
    }

    public static Format fromValue(String v) {
        return valueOf(v);
    }

}
