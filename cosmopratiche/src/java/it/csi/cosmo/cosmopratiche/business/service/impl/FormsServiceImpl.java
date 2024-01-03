/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service.impl;

import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.NON_VALIDO;
import static it.csi.cosmo.cosmopratiche.util.Util.parseNumber;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoRFormLogicoIstanzaFunzionalita;
import it.csi.cosmo.common.entities.CosmoRIstanzaFormLogicoParametroValore;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTFormLogico;
import it.csi.cosmo.common.entities.CosmoTFormLogico_;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmopratiche.business.service.FormsService;
import it.csi.cosmo.cosmopratiche.dto.rest.FunzionalitaFormLogico;
import it.csi.cosmo.cosmopratiche.dto.rest.ParametroFormLogico;
import it.csi.cosmo.cosmopratiche.dto.rest.SimpleForm;
import it.csi.cosmo.cosmopratiche.dto.rest.StrutturaFormLogico;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTFormLogicoRepository;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;


@Service
@Transactional
public class FormsServiceImpl implements FormsService {

  @Autowired
  private CosmoTFormLogicoRepository cosmoTFormLogicoRepository;

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Override
  public StrutturaFormLogico recuperaStrutturaDaNome(String codice) {
    Long idEnte = SecurityUtils.getUtenteCorrente().getEnte().getId();
    List<CosmoTFormLogico> list =
        cosmoTFormLogicoRepository.findNotDeletedByField(CosmoTFormLogico_.codice, codice);
    CosmoTFormLogico cosmoTFormLogico = list.stream()
        .filter(form -> form.getCosmoTEnte() != null && form.getCosmoTEnte().getId().equals(idEnte))
        .findFirst()
        .orElse(list.stream().filter(f -> f.getCosmoTEnte() == null).findFirst().orElse(null));
    StrutturaFormLogico strutturaFormLogico = null;
    if (cosmoTFormLogico != null) {
      strutturaFormLogico = new StrutturaFormLogico();
      strutturaFormLogico.setId(cosmoTFormLogico.getId());
      strutturaFormLogico.setCodice(cosmoTFormLogico.getCodice());
      strutturaFormLogico.setDescrizione(cosmoTFormLogico.getDescrizione());
      strutturaFormLogico.setWizard(cosmoTFormLogico.getWizard());
      strutturaFormLogico
      .setFunzionalita(cosmoTFormLogico.getCosmoRFormLogicoIstanzaFunzionalitas().stream()
          .filter(r -> r.valido() && r.getCosmoTIstanzaFunzionalitaFormLogico() != null
          && r.getCosmoTIstanzaFunzionalitaFormLogico().nonCancellato())
          .map(this::toFunzionalita).collect(Collectors.toList()));

    }

    return strutturaFormLogico;
  }

  @Override
  public StrutturaFormLogico recuperaStrutturaDaIdAttivita(String id) {
    long parsedId = parseNumber(id, -1, (e -> e > 0), NON_VALIDO);

    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(parsedId);

    if (attivita == null) {
      throw new NotFoundException();
    }

    StrutturaFormLogico strutturaFormLogico = null;
    CosmoTFormLogico cosmoTFormLogico = attivita.getFormLogico();

    if (cosmoTFormLogico != null && cosmoTFormLogico.getDtCancellazione() == null) {
      strutturaFormLogico = new StrutturaFormLogico();
      strutturaFormLogico.setId(cosmoTFormLogico.getId());
      strutturaFormLogico.setCodice(cosmoTFormLogico.getCodice());
      strutturaFormLogico.setDescrizione(cosmoTFormLogico.getDescrizione());
      strutturaFormLogico.setWizard(cosmoTFormLogico.getWizard());
      strutturaFormLogico
      .setFunzionalita(cosmoTFormLogico.getCosmoRFormLogicoIstanzaFunzionalitas().stream()
          .filter(r -> r.valido() && r.getCosmoTIstanzaFunzionalitaFormLogico() != null
          && r.getCosmoTIstanzaFunzionalitaFormLogico().nonCancellato())
          .map(this::toFunzionalita).collect(Collectors.toList()));
    }

    return strutturaFormLogico;

  }

  private FunzionalitaFormLogico toFunzionalita(CosmoRFormLogicoIstanzaFunzionalita in) {
    var funzionalita = new FunzionalitaFormLogico();
    if (in != null && in.getCosmoTIstanzaFunzionalitaFormLogico() != null
        && in.getCosmoTIstanzaFunzionalitaFormLogico().getCosmoDFunzionalitaFormLogico() != null) {
      funzionalita.setCodice(in.getCosmoTIstanzaFunzionalitaFormLogico().getCosmoDFunzionalitaFormLogico().getCodice());
      funzionalita.setDescrizione(in.getCosmoTIstanzaFunzionalitaFormLogico().getDescrizione());
      funzionalita.setId(in.getCosmoTIstanzaFunzionalitaFormLogico().getId());
      funzionalita.setMultiIstanza(in.getCosmoTIstanzaFunzionalitaFormLogico()
          .getCosmoDFunzionalitaFormLogico().isMultiIstanza());
      funzionalita.setParametri(
          in.getCosmoTIstanzaFunzionalitaFormLogico().getCosmoRIstanzaFormLogicoParametroValores()
          .stream()
          .filter(r -> r.valido() && r.getCosmoDChiaveParametroFunzionalitaFormLogico() != null
          && r.getCosmoDChiaveParametroFunzionalitaFormLogico().valido())
          .map(this::toParametro).collect(Collectors.toList()));
    }
    return funzionalita;
  }

  private ParametroFormLogico toParametro(CosmoRIstanzaFormLogicoParametroValore in) {
    var parametro = new ParametroFormLogico();
    if (in != null && in.getCosmoDChiaveParametroFunzionalitaFormLogico() != null) {
      parametro.setChiave(in.getCosmoDChiaveParametroFunzionalitaFormLogico().getCodice());
      parametro.setValore(in.getValoreParametro());
    }
    return parametro;

  }

  @Override
  public SimpleForm getPraticheTaskIdTaskForm(String idTask) {
    ValidationUtils.require(idTask, "idTask");

    // trovare l'attivita tramite task id
    Optional<CosmoTAttivita> attivita = cosmoTAttivitaRepository
        .findOneNotDeletedByField(CosmoTAttivita_.linkAttivita, "tasks/" + idTask);

    if (!attivita.isPresent()) {
      throw new NotFoundException("Attivita' non trovata");
    }

    CosmoTAttivita att = attivita.get();

    var task = this.cosmoCmmnFeignClient.getTaskId(idTask);
    if (task == null) {
      throw new NotFoundException("Task non trovato");
    }

    if (StringUtils.isEmpty(task.getFormKey())) {
      return null;
    }

    String tenantId = att.getCosmoTPratica().getEnte().getCodiceIpa();
    return getFormDefinitionFromFormKey(task.getFormKey(), tenantId);
  }

  @Override
  public SimpleForm getFormDefinitionFormKey(String formKey) {
    ValidationUtils.require(formKey, "formKey");

    String tenantId = SecurityUtils.getUtenteCorrente().getEnte().getTenantId();

    return getFormDefinitionFromFormKey(formKey, tenantId);
  }

  private SimpleForm getFormDefinitionFromFormKey(String formKey, String tenantId) {
    ValidationUtils.require(formKey, "formKey");
    ValidationUtils.require(tenantId, "tenantId");

    var formDefinitions = this.cosmoCmmnFeignClient.queryFormDefinitions(tenantId, formKey, true);

    if (formDefinitions.getTotal() < 1) {
      throw new NotFoundException("Form non trovato");
    } else if (formDefinitions.getTotal() > 1) {
      throw new InternalServerException("Definizioni multiple per il form " + formKey);
    }

    var simpleForm =
        this.cosmoCmmnFeignClient.getFormDefinitionModel(formDefinitions.getData()[0].getId());

    if (simpleForm == null) {
      throw new NotFoundException("Form definition model non trovato");
    }

    return simpleForm;
  }

}
