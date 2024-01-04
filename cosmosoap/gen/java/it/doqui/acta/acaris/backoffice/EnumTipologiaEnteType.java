
package it.doqui.acta.acaris.backoffice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumTipologiaEnteType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTipologiaEnteType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="ASL"/>
 *     &lt;enumeration value="AutoritaAmministrativaIndipendente"/>
 *     &lt;enumeration value="CameraDiCommercio"/>
 *     &lt;enumeration value="Comune"/>
 *     &lt;enumeration value="ComunitaMontana"/>
 *     &lt;enumeration value="EnteStrutturaAssociativa"/>
 *     &lt;enumeration value="EnteAutonomoLiricoIstituzioniConcertisticheAssimilate"/>
 *     &lt;enumeration value="EnteRegolazioneAttivitaEconomica"/>
 *     &lt;enumeration value="EnteAziendaOspedaliera"/>
 *     &lt;enumeration value="EnteIstituzioneRicercaNonStrumentale"/>
 *     &lt;enumeration value="EnteNazionalePrevidenzaAssistenzaSociale"/>
 *     &lt;enumeration value="EnteParco"/>
 *     &lt;enumeration value="EnteDirittoAlloStudio"/>
 *     &lt;enumeration value="EnteTurismo"/>
 *     &lt;enumeration value="EntePortuale"/>
 *     &lt;enumeration value="EnteProduttoreServiziCulturali"/>
 *     &lt;enumeration value="EnteProduttoreServiziEconomici"/>
 *     &lt;enumeration value="EnteRegionaleSviluppo"/>
 *     &lt;enumeration value="EnteRegionaleRicercaAmbiente"/>
 *     &lt;enumeration value="IstitutoStazioneSperimentaleRicerca"/>
 *     &lt;enumeration value="MinisteroPresidenzaConsiglio"/>
 *     &lt;enumeration value="OrganoCostituzionaleRilievoCostituzionale"/>
 *     &lt;enumeration value="Provincia"/>
 *     &lt;enumeration value="Regione"/>
 *     &lt;enumeration value="UniversitaIstitutiIstruzione"/>
 *     &lt;enumeration value="AltroEnte"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTipologiaEnteType")
@XmlEnum
public enum EnumTipologiaEnteType {

    ASL("ASL"),
    @XmlEnumValue("AutoritaAmministrativaIndipendente")
    AUTORITA_AMMINISTRATIVA_INDIPENDENTE("AutoritaAmministrativaIndipendente"),
    @XmlEnumValue("CameraDiCommercio")
    CAMERA_DI_COMMERCIO("CameraDiCommercio"),
    @XmlEnumValue("Comune")
    COMUNE("Comune"),
    @XmlEnumValue("ComunitaMontana")
    COMUNITA_MONTANA("ComunitaMontana"),
    @XmlEnumValue("EnteStrutturaAssociativa")
    ENTE_STRUTTURA_ASSOCIATIVA("EnteStrutturaAssociativa"),
    @XmlEnumValue("EnteAutonomoLiricoIstituzioniConcertisticheAssimilate")
    ENTE_AUTONOMO_LIRICO_ISTITUZIONI_CONCERTISTICHE_ASSIMILATE("EnteAutonomoLiricoIstituzioniConcertisticheAssimilate"),
    @XmlEnumValue("EnteRegolazioneAttivitaEconomica")
    ENTE_REGOLAZIONE_ATTIVITA_ECONOMICA("EnteRegolazioneAttivitaEconomica"),
    @XmlEnumValue("EnteAziendaOspedaliera")
    ENTE_AZIENDA_OSPEDALIERA("EnteAziendaOspedaliera"),
    @XmlEnumValue("EnteIstituzioneRicercaNonStrumentale")
    ENTE_ISTITUZIONE_RICERCA_NON_STRUMENTALE("EnteIstituzioneRicercaNonStrumentale"),
    @XmlEnumValue("EnteNazionalePrevidenzaAssistenzaSociale")
    ENTE_NAZIONALE_PREVIDENZA_ASSISTENZA_SOCIALE("EnteNazionalePrevidenzaAssistenzaSociale"),
    @XmlEnumValue("EnteParco")
    ENTE_PARCO("EnteParco"),
    @XmlEnumValue("EnteDirittoAlloStudio")
    ENTE_DIRITTO_ALLO_STUDIO("EnteDirittoAlloStudio"),
    @XmlEnumValue("EnteTurismo")
    ENTE_TURISMO("EnteTurismo"),
    @XmlEnumValue("EntePortuale")
    ENTE_PORTUALE("EntePortuale"),
    @XmlEnumValue("EnteProduttoreServiziCulturali")
    ENTE_PRODUTTORE_SERVIZI_CULTURALI("EnteProduttoreServiziCulturali"),
    @XmlEnumValue("EnteProduttoreServiziEconomici")
    ENTE_PRODUTTORE_SERVIZI_ECONOMICI("EnteProduttoreServiziEconomici"),
    @XmlEnumValue("EnteRegionaleSviluppo")
    ENTE_REGIONALE_SVILUPPO("EnteRegionaleSviluppo"),
    @XmlEnumValue("EnteRegionaleRicercaAmbiente")
    ENTE_REGIONALE_RICERCA_AMBIENTE("EnteRegionaleRicercaAmbiente"),
    @XmlEnumValue("IstitutoStazioneSperimentaleRicerca")
    ISTITUTO_STAZIONE_SPERIMENTALE_RICERCA("IstitutoStazioneSperimentaleRicerca"),
    @XmlEnumValue("MinisteroPresidenzaConsiglio")
    MINISTERO_PRESIDENZA_CONSIGLIO("MinisteroPresidenzaConsiglio"),
    @XmlEnumValue("OrganoCostituzionaleRilievoCostituzionale")
    ORGANO_COSTITUZIONALE_RILIEVO_COSTITUZIONALE("OrganoCostituzionaleRilievoCostituzionale"),
    @XmlEnumValue("Provincia")
    PROVINCIA("Provincia"),
    @XmlEnumValue("Regione")
    REGIONE("Regione"),
    @XmlEnumValue("UniversitaIstitutiIstruzione")
    UNIVERSITA_ISTITUTI_ISTRUZIONE("UniversitaIstitutiIstruzione"),
    @XmlEnumValue("AltroEnte")
    ALTRO_ENTE("AltroEnte");
    private final String value;

    EnumTipologiaEnteType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTipologiaEnteType fromValue(String v) {
        for (EnumTipologiaEnteType c: EnumTipologiaEnteType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
