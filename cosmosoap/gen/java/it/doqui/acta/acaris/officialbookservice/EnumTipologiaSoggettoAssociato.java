
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipologiaSoggettoAssociato.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipologiaSoggettoAssociato">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="SoggettoActa"/>
 *     &lt;enumeration value="Struttura"/>
 *     &lt;enumeration value="Nodo"/>
 *     &lt;enumeration value="Utente"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipologiaSoggettoAssociato")
@XmlEnum
public enum EnumTipologiaSoggettoAssociato {

    @XmlEnumValue("SoggettoActa")
    SOGGETTO_ACTA("SoggettoActa"),
    @XmlEnumValue("Struttura")
    STRUTTURA("Struttura"),
    @XmlEnumValue("Nodo")
    NODO("Nodo"),
    @XmlEnumValue("Utente")
    UTENTE("Utente");
    private final String value;

    EnumTipologiaSoggettoAssociato(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipologiaSoggettoAssociato fromValue(String v) {
        for (EnumTipologiaSoggettoAssociato c: EnumTipologiaSoggettoAssociato.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
