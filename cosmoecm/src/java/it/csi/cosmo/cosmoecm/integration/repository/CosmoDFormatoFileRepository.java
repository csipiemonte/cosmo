/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import it.csi.cosmo.common.entities.CosmoDFormatoFile;
import it.csi.cosmo.common.repository.CosmoDRepository;

/**
 */

public interface CosmoDFormatoFileRepository
extends CosmoDRepository<CosmoDFormatoFile, String> {

  CosmoDFormatoFile findByMimeType(String mime);

  //@formatter:off
  @Query(value = "select distinct " +
      " case when locate( \'(\', c.descrizione) > 0 then TRIM(SUBSTRING(c.descrizione, 0, locate( \'(\', c.descrizione) - 1)) "
      + " else c.descrizione end as descrizione, "
      + " case when LENGTH(c.descrizione)> 0 then (case when locate( \'(\', c.descrizione) > 0 then TRIM(SUBSTRING(c.descrizione, 0, locate( \'(\', c.descrizione) - 1)) else c.descrizione end) else c.codice end as codice, "
      + " case when LENGTH(c.descrizione)> 0 then 'raggruppato' else c.mimeType end as mimeType "
      + " FROM CosmoDFormatoFile c "
      + " WHERE c.dtFineVal is null "
      + " GROUP BY c.descrizione, c.codice "
      + " ORDER BY case when locate( \'(\', c.descrizione) > 0 then TRIM(SUBSTRING(c.descrizione, 0, locate( \'(\', c.descrizione) - 1)) "
      + " else c.descrizione end, "
      + " case when LENGTH(c.descrizione)> 0 then (case when locate( \'(\', c.descrizione) > 0 then TRIM(SUBSTRING(c.descrizione, 0, locate( \'(\', c.descrizione) - 1)) else c.descrizione end) else c.codice end, "
      + " case when LENGTH(c.descrizione)> 0 then 'raggruppato' else c.mimeType end ",
      countQuery="select distinct "
      + " count(distinct c.codice) as codice "
      + " FROM CosmoDFormatoFile c "
      + " WHERE c.dtFineVal is null and c.descrizione is null ")
  Page<Object[]> findAllActiveGroupedByDescription(Pageable page);

  @Query(value = "select "
      + " (select count(*) from ( select distinct "
      + "  case when position('(' in c.descrizione)>0 then trim(substring(c.descrizione , 0 , position('(' in c.descrizione)-1)) else c.descrizione end as descrizione "
      + "  FROM COSMO_D_FORMATO_FILE c "
      + "  WHERE c.dt_fine_val is null and c.descrizione is not null) as countGrouped ) + "
      + " (select count(*) "
      + " from COSMO_D_FORMATO_FILE cdff "
      + " where cdff.descrizione is null) as sum ", nativeQuery = true)
  Integer countByGroupedDescriptions();

  @Query(value = "select distinct " +
      " case when locate( \'(\', c.descrizione) > 0 then TRIM(SUBSTRING(c.descrizione, 0, locate( \'(\', c.descrizione) - 1)) "
      + " else c.descrizione end as descrizione, "
      + " case when LENGTH(c.descrizione)> 0 then (case when locate( \'(\', c.descrizione) > 0 then TRIM(SUBSTRING(c.descrizione, 0, locate( \'(\', c.descrizione) - 1)) else c.descrizione end) else c.codice end as codice, "
      + " case when LENGTH(c.descrizione)> 0 then 'raggruppato' else c.mimeType end as mimeType "
      + " FROM CosmoDFormatoFile c "
      + " WHERE c.dtFineVal is null AND c.codice like %:fullText% "
      + " GROUP BY c.descrizione, c.codice "
      + " ORDER BY case when locate( \'(\', c.descrizione) > 0 then TRIM(SUBSTRING(c.descrizione, 0, locate( \'(\', c.descrizione) - 1)) "
      + " else c.descrizione end, "
      + " case when LENGTH(c.descrizione)> 0 then (case when locate( \'(\', c.descrizione) > 0 then TRIM(SUBSTRING(c.descrizione, 0, locate( \'(\', c.descrizione) - 1)) else c.descrizione end) else c.codice end, "
      + " case when LENGTH(c.descrizione)> 0 then 'raggruppato' else c.mimeType end ",
      countQuery="select distinct "
      + " count(distinct c.codice) as codice "
      + " FROM CosmoDFormatoFile c "
      + " WHERE c.dtFineVal is null and c.codice like %:fullText% ")
  Page<Object[]> findAllActiveGroupedByDescription(@Param("fullText") String fullText,Pageable page);

  @Query(value = "select count(*) from ( select distinct "
      + " case when position('(' in c.descrizione)>0 then trim(substring(c.descrizione , 0 , position('(' in c.descrizione)-1)) else c.descrizione end as descrizione"
      + " FROM COSMO_D_FORMATO_FILE c "
      + " WHERE c.dt_fine_val is null and c.descrizione is not null) as countGrouped ", nativeQuery = true)
  Integer countByGroupedDescriptions(@Param("fullText") String fullText);
}
