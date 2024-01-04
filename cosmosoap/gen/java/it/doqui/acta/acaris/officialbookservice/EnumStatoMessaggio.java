
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumStatoMessaggio.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumStatoMessaggio">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Acquisito"/>
 *     &lt;enumeration value="NonAcquisito"/>
 *     &lt;enumeration value="Aggiornato"/>
 *     &lt;enumeration value="Confermato"/>
 *     &lt;enumeration value="Annullato"/>
 *     &lt;enumeration value="Inviato"/>
 *     &lt;enumeration value="Ripudiato"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumStatoMessaggio")
@XmlEnum
public enum EnumStatoMessaggio {

    @XmlEnumValue("Acquisito")
    ACQUISITO("Acquisito"),
    @XmlEnumValue("NonAcquisito")
    NON_ACQUISITO("NonAcquisito"),
    @XmlEnumValue("Aggiornato")
    AGGIORNATO("Aggiornato"),
    @XmlEnumValue("Confermato")
    CONFERMATO("Confermato"),
    @XmlEnumValue("Annullato")
    ANNULLATO("Annullato"),
    @XmlEnumValue("Inviato")
    INVIATO("Inviato"),
    @XmlEnumValue("Ripudiato")
    RIPUDIATO("Ripudiato");
    private final String value;

    EnumStatoMessaggio(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumStatoMessaggio fromValue(String v) {
        for (EnumStatoMessaggio c: EnumStatoMessaggio.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
