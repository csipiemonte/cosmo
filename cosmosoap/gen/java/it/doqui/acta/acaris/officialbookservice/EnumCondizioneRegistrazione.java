
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumCondizioneRegistrazione.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumCondizioneRegistrazione">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Registrazione"/>
 *     &lt;enumeration value="Classificazione"/>
 *     &lt;enumeration value="Smistamento"/>
 *     &lt;enumeration value="ModificaDocumento"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumCondizioneRegistrazione")
@XmlEnum
public enum EnumCondizioneRegistrazione {

    @XmlEnumValue("Registrazione")
    REGISTRAZIONE("Registrazione"),
    @XmlEnumValue("Classificazione")
    CLASSIFICAZIONE("Classificazione"),
    @XmlEnumValue("Smistamento")
    SMISTAMENTO("Smistamento"),
    @XmlEnumValue("ModificaDocumento")
    MODIFICA_DOCUMENTO("ModificaDocumento");
    private final String value;

    EnumCondizioneRegistrazione(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumCondizioneRegistrazione fromValue(String v) {
        for (EnumCondizioneRegistrazione c: EnumCondizioneRegistrazione.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
