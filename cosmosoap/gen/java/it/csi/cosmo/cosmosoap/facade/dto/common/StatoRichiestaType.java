
package it.csi.cosmo.cosmosoap.facade.dto.common;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per StatoRichiestaType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="StatoRichiestaType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="IN_CORSO_DI_ACQUISIZIONE"/>
 *     &lt;enumeration value="DA_TRATTARE"/>
 *     &lt;enumeration value="TRATTAMENTO_IN_ESECUZIONE"/>
 *     &lt;enumeration value="ESEGUITA"/>
 *     &lt;enumeration value="ERRORE_IN_FASE_DI_TRATTAMENTO"/>
 *     &lt;enumeration value="NON_TRATTATA"/>
 *     &lt;enumeration value="ERRORE_IN_FASE_DI_ACQUISIZIONE"/>
 *     &lt;enumeration value="PRESA_IN_CARICO_ASSISTENZA"/>
 *     &lt;enumeration value="ATTESA_TRATTAMENTO_ALLEGATI"/>
 *     &lt;enumeration value="ATTESA_ARCHIVIAZIONE_PRINCIPALE"/>
 *     &lt;enumeration value="ATTESA_ESITO_SEGNATURA"/>
 *     &lt;enumeration value="TENTATIVI_RETRY_CALLBACK_SOSPESI"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StatoRichiestaType")
@XmlEnum
public enum StatoRichiestaType {

    IN_CORSO_DI_ACQUISIZIONE,
    DA_TRATTARE,
    TRATTAMENTO_IN_ESECUZIONE,
    ESEGUITA,
    ERRORE_IN_FASE_DI_TRATTAMENTO,
    NON_TRATTATA,
    ERRORE_IN_FASE_DI_ACQUISIZIONE,
    PRESA_IN_CARICO_ASSISTENZA,
    ATTESA_TRATTAMENTO_ALLEGATI,
    ATTESA_ARCHIVIAZIONE_PRINCIPALE,
    ATTESA_ESITO_SEGNATURA,
    TENTATIVI_RETRY_CALLBACK_SOSPESI;

    public String value() {
        return name();
    }

    public static StatoRichiestaType fromValue(String v) {
        return valueOf(v);
    }

}
