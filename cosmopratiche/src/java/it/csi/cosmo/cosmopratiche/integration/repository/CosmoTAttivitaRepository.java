/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.repository.CosmoTRepository;

public interface CosmoTAttivitaRepository extends CosmoTRepository<CosmoTAttivita, Long> {

  // Page<CosmoTAttivita>
  // findByCodiceFiscaleAndAssegnatarioAndUtenteCancellazioneNullAndDtCancellazioneNull(String cf,
  // Boolean assegnatario, Pageable p);
  List<CosmoTAttivita> findBycosmoTPraticaId(Long idPratica );
  CosmoTAttivita findBycosmoTPraticaIdAndLinkAttivita(Long idPratica,String linkAttivita );

  CosmoTAttivita findBylinkAttivitaAndDtCancellazioneIsNull(String linkAttivita);

  CosmoTAttivita deleteByCosmoTPraticaId(Long idPratica);


  Page<CosmoTAttivita> findBycosmoRAttivitaAssegnazionesIdUtenteAndCosmoRAttivitaAssegnazionesAssegnatario(
      Integer idUtente, Boolean assegnatario, Pageable p);

  Page<CosmoTAttivita> findBycosmoRAttivitaAssegnazionesIdUtenteAndCosmoRAttivitaAssegnazionesAssegnatarioAndDtCancellazioneNull(
      int intValue, boolean b, PageRequest pageRequest);


  Page<CosmoTAttivita> findBycosmoRAttivitaAssegnazionesIdUtenteAndCosmoRAttivitaAssegnazionesAssegnatarioIsTrueAndDtCancellazioneNullOrCosmoRAttivitaAssegnazionesIdGruppoInAndDtCancellazioneNull(
      int intValue, List<Integer> gruppiId, boolean b, PageRequest pageRequest);

  @Query("select d from CosmoTAttivita d JOIN d.cosmoRAttivitaAssegnaziones ass  where d.dtCancellazione is null  and ((ass.idUtente = :idUser and ass.assegnatario = true and ass.dtFineVal is null) or (ass.idGruppo in (:gruppiId))) and ass.dtFineVal is null")
  Page<CosmoTAttivita> findByAssegnazioneOrGruppo(@Param("idUser") int idUtente,
      @Param("gruppiId") List<Integer> gruppiId, Pageable p);

  @Query("select d from CosmoTAttivita d JOIN d.cosmoRAttivitaAssegnaziones ass  where d.dtCancellazione is null  and ass.idUtente = :idUser and ass.assegnatario = true and ass.dtFineVal is null and ass.dtFineVal is null")
  Page<CosmoTAttivita> findByAssegnazione(@Param("idUser") int idUtente, Pageable p);


}
