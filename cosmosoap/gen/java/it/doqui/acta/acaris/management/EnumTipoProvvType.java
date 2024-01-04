
package it.doqui.acta.acaris.management;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipoProvvType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipoProvvType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="ProvvedimentoOriginatore"/>
 *     &lt;enumeration value="ProvvedimentoDiModifica"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipoProvvType")
@XmlEnum
public enum EnumTipoProvvType {

    @XmlEnumValue("ProvvedimentoOriginatore")
    PROVVEDIMENTO_ORIGINATORE("ProvvedimentoOriginatore"),
    @XmlEnumValue("ProvvedimentoDiModifica")
    PROVVEDIMENTO_DI_MODIFICA("ProvvedimentoDiModifica");
    private final String value;

    EnumTipoProvvType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipoProvvType fromValue(String v) {
        for (EnumTipoProvvType c: EnumTipoProvvType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
