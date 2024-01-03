/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTHelper;
import it.csi.cosmo.cosmonotifications.dto.rest.Helper;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
    componentModel = "spring")
public interface CosmoTHelperMapper {

  @Mapping(source = "cosmoDHelperPagina", target = "codicePagina")
  @Mapping(source = "cosmoDHelperTab", target = "codiceTab")
  @Mapping(source = "cosmoDCustomFormFormio", target = "codiceForm")
  @Mapping(source = "cosmoDHelperModale", target = "codiceModale")
  @Mapping(target = "codiceModale.codicePagina", ignore = true)
  @Mapping(target = "codiceModale.codiceTab", ignore = true)
  @Mapping(target = "codiceForm.codiceTipoPratica", ignore = true)
  Helper toDTO(CosmoTHelper input);

  List<Helper> toDTOs(List<CosmoTHelper> input);

  @Mapping(target = "dtCancellazione", ignore = true)
  @Mapping(target = "dtInserimento", ignore = true)
  @Mapping(target = "dtUltimaModifica", ignore = true)
  @Mapping(target = "utenteCancellazione", ignore = true)
  @Mapping(target = "utenteInserimento", ignore = true)
  @Mapping(target = "utenteUltimaModifica", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(source = "codicePagina.codice", target = "cosmoDHelperPagina.codice")
  @Mapping(source = "codicePagina.descrizione", target = "cosmoDHelperPagina.descrizione")
  @Mapping(source = "codiceTab.codice", target = "cosmoDHelperTab.codice")
  @Mapping(source = "codiceForm.codice", target = "cosmoDCustomFormFormio.codice")
  @Mapping(source = "codiceModale.codice", target = "cosmoDHelperModale.codice")
  @Mapping(target = "cosmoDHelperPagina.cosmoTHelpers", ignore = true)
  @Mapping(target = "cosmoDHelperPagina.cosmoDHelperTabs", ignore = true)
  @Mapping(target = "cosmoDHelperPagina.dtFineVal", ignore = true)
  @Mapping(target = "cosmoDHelperPagina.dtInizioVal", ignore = true)
  @Mapping(target = "cosmoDHelperPagina.cosmoDHelperModales", ignore = true)
  @Mapping(target = "cosmoDHelperTab.dtFineVal", ignore = true)
  @Mapping(target = "cosmoDHelperTab.cosmoTHelpers", ignore = true)
  @Mapping(target = "cosmoDHelperTab.dtInizioVal", ignore = true)
  @Mapping(target = "cosmoDHelperTab.cosmoDHelperPagina", ignore = true)
  @Mapping(target = "cosmoDHelperTab.cosmoDHelperModales", ignore = true)
  @Mapping(target = "cosmoDHelperModale.dtFineVal", ignore = true)
  @Mapping(target = "cosmoDHelperModale.dtInizioVal", ignore = true)
  @Mapping(target = "cosmoDHelperModale.cosmoDHelperPagina", ignore = true)
  @Mapping(target = "cosmoDHelperModale.cosmoDHelperTab", ignore = true)
  @Mapping(target = "cosmoDHelperModale.cosmoTHelpers", ignore = true)
  @Mapping(target = "cosmoDCustomFormFormio.dtFineVal", ignore = true)
  @Mapping(target = "cosmoDCustomFormFormio.dtInizioVal", ignore = true)
  @Mapping(target = "cosmoDCustomFormFormio.cosmoDTipoPratica", ignore = true)
  @Mapping(target = "cosmoDCustomFormFormio.cosmoTHelpers", ignore = true)
  CosmoTHelper toRecord(Helper input);

}
