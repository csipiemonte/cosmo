
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumStatoRegistro.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumStatoRegistro">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Bozza"/>
 *     &lt;enumeration value="Aperto"/>
 *     &lt;enumeration value="Chiuso"/>
 *     &lt;enumeration value="Archiviato"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumStatoRegistro")
@XmlEnum
public enum EnumStatoRegistro {

    @XmlEnumValue("Bozza")
    BOZZA("Bozza"),
    @XmlEnumValue("Aperto")
    APERTO("Aperto"),
    @XmlEnumValue("Chiuso")
    CHIUSO("Chiuso"),
    @XmlEnumValue("Archiviato")
    ARCHIVIATO("Archiviato");
    private final String value;

    EnumStatoRegistro(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumStatoRegistro fromValue(String v) {
        for (EnumStatoRegistro c: EnumStatoRegistro.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
