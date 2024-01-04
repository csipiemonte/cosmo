
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumOfficialBookObjectType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumOfficialBookObjectType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="RegistrazionePropertiesType"/>
 *     &lt;enumeration value="CorrispondentePropertiesType"/>
 *     &lt;enumeration value="AnnotazioneOBPropertiesType"/>
 *     &lt;enumeration value="RegistroProtocolloPropertiesType"/>
 *     &lt;enumeration value="LogProtocolloPropertiesType"/>
 *     &lt;enumeration value="MessaggioPropertiesType"/>
 *     &lt;enumeration value="CorrispondenteMessaggioPropertiesType"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumOfficialBookObjectType")
@XmlEnum
public enum EnumOfficialBookObjectType {

    @XmlEnumValue("RegistrazionePropertiesType")
    REGISTRAZIONE_PROPERTIES_TYPE("RegistrazionePropertiesType"),
    @XmlEnumValue("CorrispondentePropertiesType")
    CORRISPONDENTE_PROPERTIES_TYPE("CorrispondentePropertiesType"),
    @XmlEnumValue("AnnotazioneOBPropertiesType")
    ANNOTAZIONE_OB_PROPERTIES_TYPE("AnnotazioneOBPropertiesType"),
    @XmlEnumValue("RegistroProtocolloPropertiesType")
    REGISTRO_PROTOCOLLO_PROPERTIES_TYPE("RegistroProtocolloPropertiesType"),
    @XmlEnumValue("LogProtocolloPropertiesType")
    LOG_PROTOCOLLO_PROPERTIES_TYPE("LogProtocolloPropertiesType"),
    @XmlEnumValue("MessaggioPropertiesType")
    MESSAGGIO_PROPERTIES_TYPE("MessaggioPropertiesType"),
    @XmlEnumValue("CorrispondenteMessaggioPropertiesType")
    CORRISPONDENTE_MESSAGGIO_PROPERTIES_TYPE("CorrispondenteMessaggioPropertiesType");
    private final String value;

    EnumOfficialBookObjectType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumOfficialBookObjectType fromValue(String v) {
        for (EnumOfficialBookObjectType c: EnumOfficialBookObjectType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
