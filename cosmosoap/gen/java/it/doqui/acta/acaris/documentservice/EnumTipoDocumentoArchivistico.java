
package it.doqui.acta.acaris.documentservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipoDocumentoArchivistico.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipoDocumentoArchivistico">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="DocumentoSemplice"/>
 *     &lt;enumeration value="DocumentoDB"/>
 *     &lt;enumeration value="DocumentoRegistro"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipoDocumentoArchivistico")
@XmlEnum
public enum EnumTipoDocumentoArchivistico {

    @XmlEnumValue("DocumentoSemplice")
    DOCUMENTO_SEMPLICE("DocumentoSemplice"),
    @XmlEnumValue("DocumentoDB")
    DOCUMENTO_DB("DocumentoDB"),
    @XmlEnumValue("DocumentoRegistro")
    DOCUMENTO_REGISTRO("DocumentoRegistro");
    private final String value;

    EnumTipoDocumentoArchivistico(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipoDocumentoArchivistico fromValue(String v) {
        for (EnumTipoDocumentoArchivistico c: EnumTipoDocumentoArchivistico.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
