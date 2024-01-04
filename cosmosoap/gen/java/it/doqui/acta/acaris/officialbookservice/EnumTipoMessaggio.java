
package it.doqui.acta.acaris.officialbookservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipoMessaggio.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipoMessaggio">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="Segnatura"/>
 *     &lt;enumeration value="Conferma"/>
 *     &lt;enumeration value="Aggiornamento"/>
 *     &lt;enumeration value="Annullamento"/>
 *     &lt;enumeration value="NotificaEccezione"/>
 *     &lt;enumeration value="RicevutaPEC"/>
 *     &lt;enumeration value="MailPECNonStrutturata"/>
 *     &lt;enumeration value="MailNonPEC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipoMessaggio")
@XmlEnum
public enum EnumTipoMessaggio {

    @XmlEnumValue("Segnatura")
    SEGNATURA("Segnatura"),
    @XmlEnumValue("Conferma")
    CONFERMA("Conferma"),
    @XmlEnumValue("Aggiornamento")
    AGGIORNAMENTO("Aggiornamento"),
    @XmlEnumValue("Annullamento")
    ANNULLAMENTO("Annullamento"),
    @XmlEnumValue("NotificaEccezione")
    NOTIFICA_ECCEZIONE("NotificaEccezione"),
    @XmlEnumValue("RicevutaPEC")
    RICEVUTA_PEC("RicevutaPEC"),
    @XmlEnumValue("MailPECNonStrutturata")
    MAIL_PEC_NON_STRUTTURATA("MailPECNonStrutturata"),
    @XmlEnumValue("MailNonPEC")
    MAIL_NON_PEC("MailNonPEC");
    private final String value;

    EnumTipoMessaggio(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipoMessaggio fromValue(String v) {
        for (EnumTipoMessaggio c: EnumTipoMessaggio.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
