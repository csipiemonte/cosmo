
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipoContenitore.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipoContenitore">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="fascicoloTemporaneoTitolario"/>
 *     &lt;enumeration value="fascicoloTemporaneoVoce"/>
 *     &lt;enumeration value="strutturaAggregativa"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipoContenitore")
@XmlEnum
public enum EnumTipoContenitore {

    @XmlEnumValue("fascicoloTemporaneoTitolario")
    FASCICOLO_TEMPORANEO_TITOLARIO("fascicoloTemporaneoTitolario"),
    @XmlEnumValue("fascicoloTemporaneoVoce")
    FASCICOLO_TEMPORANEO_VOCE("fascicoloTemporaneoVoce"),
    @XmlEnumValue("strutturaAggregativa")
    STRUTTURA_AGGREGATIVA("strutturaAggregativa");
    private final String value;

    EnumTipoContenitore(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipoContenitore fromValue(String v) {
        for (EnumTipoContenitore c: EnumTipoContenitore.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
