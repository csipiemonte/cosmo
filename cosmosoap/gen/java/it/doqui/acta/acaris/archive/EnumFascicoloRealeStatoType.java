
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumFascicoloRealeStatoType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumFascicoloRealeStatoType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Aperto"/>
 *     &lt;enumeration value="AttesaDiChiusura"/>
 *     &lt;enumeration value="ChiusoInCorrente"/>
 *     &lt;enumeration value="ChiusoInDeposito"/>
 *     &lt;enumeration value="Congelato"/>
 *     &lt;enumeration value="Spostato"/>
 *     &lt;enumeration value="Scartato"/>
 *     &lt;enumeration value="Cancellato"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumFascicoloRealeStatoType")
@XmlEnum
public enum EnumFascicoloRealeStatoType {

    @XmlEnumValue("Aperto")
    APERTO("Aperto"),
    @XmlEnumValue("AttesaDiChiusura")
    ATTESA_DI_CHIUSURA("AttesaDiChiusura"),
    @XmlEnumValue("ChiusoInCorrente")
    CHIUSO_IN_CORRENTE("ChiusoInCorrente"),
    @XmlEnumValue("ChiusoInDeposito")
    CHIUSO_IN_DEPOSITO("ChiusoInDeposito"),
    @XmlEnumValue("Congelato")
    CONGELATO("Congelato"),
    @XmlEnumValue("Spostato")
    SPOSTATO("Spostato"),
    @XmlEnumValue("Scartato")
    SCARTATO("Scartato"),
    @XmlEnumValue("Cancellato")
    CANCELLATO("Cancellato");
    private final String value;

    EnumFascicoloRealeStatoType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumFascicoloRealeStatoType fromValue(String v) {
        for (EnumFascicoloRealeStatoType c: EnumFascicoloRealeStatoType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
