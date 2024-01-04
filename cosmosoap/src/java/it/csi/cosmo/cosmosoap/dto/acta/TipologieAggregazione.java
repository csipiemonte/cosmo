/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.dto.acta;

/**
 * Enumerazione contenente tutti gli Scopes gestiti dall'applicativo
 */
public enum TipologieAggregazione {
  SERIE_TIPOLOGICA_DOCUMENTI_PROPERTIES_TYPE(1L, "SerieTipologicaDocumentiPropertiesType"),
  SERIE_FASCICOLI_PROPERTIES_TYPE(2L, "SerieFascicoliPropertiesType"),
  SERIE_DOSSIER_PROPERTIES_TYPE(3L, "SerieDossierPropertiesType"),
  SOTTOFASCICOLO_PROPERTIES_TYPE(4L, "SottofascicoloPropertiesType"),
  FASCICOLO_REALE_ANNUALE_PROPERTIES_TYPE(5L, "FascicoloRealeAnnualePropertiesType"),
  FASCICOLO_REALE_CONTINUO_PROPERTIES_TYPE(6L, "FascicoloRealeContinuoPropertiesType"),
  FASCICOLO_REALE_LIBERO_PROPERTIES_TYPE(7L, "FascicoloRealeLiberoPropertiesType"),
  FASCICOLO_REALE_EREDITATO_PROPERTIES_TYPE(8L, "FascicoloRealeEreditatoPropertiesType"),
  FASCICOLO_REALE_LEGISLATURA_PROPERTIES_TYPE(9L, "FascicoloRealeLegislaturaPropertiesType"),
  DOSSIER_PROPERTIES_TYPE(10L, "DossierPropertiesType"),
  VOLUME_SOTTOFASCICOLI_PROPERTIES_TYPE(11L, "VolumeSottofascicoliPropertiesType"),
  VOLUME_FASCICOLI_PROPERTIES_TYPE(12L, "VolumeFascicoliPropertiesType"),
  VOLUME_SERIE_FASCICOLI_PROPERTIES_TYPE(13L, "VolumeSerieFascicoliPropertiesType"),
  VOLUME_SERIE_TIPOLOGICA_DOCUMENTI_PROPERTIES_TYPE(14L, "VolumeSerieTipologicaDocumentiPropertiesType");

  private Long codice;
  private String descrizione;

  TipologieAggregazione(Long codice, String descrizione) {
    this.codice = codice;
    this.descrizione = descrizione;
  }

  public Long getCodice() {
    return codice;
  }

  public String getDescrizione() {
    return descrizione;
  }

}
