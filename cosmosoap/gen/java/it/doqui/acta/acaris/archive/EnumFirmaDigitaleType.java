
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumFirmaDigitaleType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumFirmaDigitaleType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="NA"/>
 *     &lt;enumeration value="MarcaDetached"/>
 *     &lt;enumeration value="Enveloped"/>
 *     &lt;enumeration value="EnvelopedMultipart"/>
 *     &lt;enumeration value="FirmaDetached"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumFirmaDigitaleType")
@XmlEnum
public enum EnumFirmaDigitaleType {

    NA("NA"),
    @XmlEnumValue("MarcaDetached")
    MARCA_DETACHED("MarcaDetached"),
    @XmlEnumValue("Enveloped")
    ENVELOPED("Enveloped"),
    @XmlEnumValue("EnvelopedMultipart")
    ENVELOPED_MULTIPART("EnvelopedMultipart"),
    @XmlEnumValue("FirmaDetached")
    FIRMA_DETACHED("FirmaDetached");
    private final String value;

    EnumFirmaDigitaleType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumFirmaDigitaleType fromValue(String v) {
        for (EnumFirmaDigitaleType c: EnumFirmaDigitaleType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
