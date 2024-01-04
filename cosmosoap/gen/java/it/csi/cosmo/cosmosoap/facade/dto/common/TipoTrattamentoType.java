
package it.csi.cosmo.cosmosoap.facade.dto.common;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per TipoTrattamentoType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TipoTrattamentoType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="INESISTENTE"/>
 *     &lt;enumeration value="TrattamentoACTA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TipoTrattamentoType")
@XmlEnum
public enum TipoTrattamentoType {

    INESISTENTE("INESISTENTE"),
    @XmlEnumValue("TrattamentoACTA")
    TRATTAMENTO_ACTA("TrattamentoACTA");
    private final String value;

    TipoTrattamentoType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TipoTrattamentoType fromValue(String v) {
        for (TipoTrattamentoType c: TipoTrattamentoType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
