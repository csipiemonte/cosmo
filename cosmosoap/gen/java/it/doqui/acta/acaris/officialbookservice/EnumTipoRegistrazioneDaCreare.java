
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipoRegistrazioneDaCreare.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipoRegistrazioneDaCreare">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="RegistrazionRapida"/>
 *     &lt;enumeration value="Protocollazione"/>
 *     &lt;enumeration value="ProtocollazioneDocumentoEsistente"/>
 *     &lt;enumeration value="ProtocollazioneDaSmistamento"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipoRegistrazioneDaCreare")
@XmlEnum
public enum EnumTipoRegistrazioneDaCreare {

    @XmlEnumValue("RegistrazionRapida")
    REGISTRAZION_RAPIDA("RegistrazionRapida"),
    @XmlEnumValue("Protocollazione")
    PROTOCOLLAZIONE("Protocollazione"),
    @XmlEnumValue("ProtocollazioneDocumentoEsistente")
    PROTOCOLLAZIONE_DOCUMENTO_ESISTENTE("ProtocollazioneDocumentoEsistente"),
    @XmlEnumValue("ProtocollazioneDaSmistamento")
    PROTOCOLLAZIONE_DA_SMISTAMENTO("ProtocollazioneDaSmistamento");
    private final String value;

    EnumTipoRegistrazioneDaCreare(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipoRegistrazioneDaCreare fromValue(String v) {
        for (EnumTipoRegistrazioneDaCreare c: EnumTipoRegistrazioneDaCreare.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
