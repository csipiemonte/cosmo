
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumDocPrimarioType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumDocPrimarioType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="SignatureDetached"/>
 *     &lt;enumeration value="AllegatoXML"/>
 *     &lt;enumeration value="DocumentoSingolo"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumDocPrimarioType")
@XmlEnum
public enum EnumDocPrimarioType {

    @XmlEnumValue("SignatureDetached")
    SIGNATURE_DETACHED("SignatureDetached"),
    @XmlEnumValue("AllegatoXML")
    ALLEGATO_XML("AllegatoXML"),
    @XmlEnumValue("DocumentoSingolo")
    DOCUMENTO_SINGOLO("DocumentoSingolo");
    private final String value;

    EnumDocPrimarioType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumDocPrimarioType fromValue(String v) {
        for (EnumDocPrimarioType c: EnumDocPrimarioType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
