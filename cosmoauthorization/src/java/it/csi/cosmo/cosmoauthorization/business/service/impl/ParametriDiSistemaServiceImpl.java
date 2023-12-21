/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoCConfigurazione;
import it.csi.cosmo.common.entities.CosmoCConfigurazione_;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.business.service.ParametriDiSistemaService;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaParametroDiSistemaRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaParametroDiSistemaRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaParametriDiSistemaDTO;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametroDiSistema;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametroDiSistemaResponse;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoCConfigurazioneMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoCConfigurazioneRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.specifications.CosmoCConfigurazioneParametroDiSistemaSpecifications;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;
import it.csi.cosmo.cosmoauthorization.dto.rest.PageInfo;
import org.springframework.data.domain.Pageable;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.config.ParametriApplicativo;
import org.springframework.data.domain.Page;
import org.apache.commons.lang3.StringUtils;

@Service
@Transactional
public class ParametriDiSistemaServiceImpl implements ParametriDiSistemaService {

	private static final String CLASS_NAME = ParametriDiSistemaServiceImpl.class.getSimpleName();

	private static final CosmoLogger logger = LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

	@Autowired
	CosmoCConfigurazioneRepository cosmoCConfigurazioneRepository;

	@Autowired
	CosmoCConfigurazioneMapper cosmoCConfigurazioneMapper;

	@Autowired
	ConfigurazioneService configurazioneService;

	@Override
	@Transactional(readOnly = true)
	public ParametroDiSistemaResponse getParamtriDiSistema(String filter) {

		ParametroDiSistemaResponse output = new ParametroDiSistemaResponse();

		GenericRicercaParametricaDTO<FiltroRicercaParametriDiSistemaDTO> ricercaParametrica = SearchUtils
				.getRicercaParametrica(filter, FiltroRicercaParametriDiSistemaDTO.class);

		Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
				configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

		Page<CosmoCConfigurazione> pageParameteriDiSistema = cosmoCConfigurazioneRepository
				.findAllActive(CosmoCConfigurazioneParametroDiSistemaSpecifications
						.findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

		List<CosmoCConfigurazione> cosmoCConfigurazioniSuDB = pageParameteriDiSistema.getContent();

		List<ParametroDiSistema> parametriDiSistema = new LinkedList<>();

		cosmoCConfigurazioniSuDB.forEach(cosmoCConfigurazioneSuDB -> parametriDiSistema
				.add(cosmoCConfigurazioneMapper.toDTOLight(cosmoCConfigurazioneSuDB)));
		output.setParametriDiSistema(parametriDiSistema);

		if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
			it.csi.cosmo.common.util.SearchUtils.filterFields(parametriDiSistema,
					Arrays.asList(ricercaParametrica.getFields().split(",")));
		}

		PageInfo pageInfo = new PageInfo();
		pageInfo.setPage(pageParameteriDiSistema.getNumber());
		pageInfo.setPageSize(pageParameteriDiSistema.getSize());
		pageInfo.setTotalElements(Math.toIntExact(pageParameteriDiSistema.getTotalElements()));
		pageInfo.setTotalPages(pageParameteriDiSistema.getTotalPages());

		output.setPageInfo(pageInfo);

		return output;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ParametroDiSistema deleteParametroDiSistemaByChiave(String chiave) {
		String methodName = "deleteParametroDiSistema";
		ValidationUtils.require(chiave, "chiave parametro di sistema");

		CosmoCConfigurazione paramtroDaEliminare = cosmoCConfigurazioneRepository.findOneActive(chiave).orElseThrow(
				() -> new NotFoundException(String.format(ErrorMessages.C_CONFIGURAZIONE_NON_TROVATA, chiave)));

		paramtroDaEliminare = cosmoCConfigurazioneRepository.deactivate(paramtroDaEliminare);
		logger.info(methodName, "parametro di configurazione con chiave {} eliminato", paramtroDaEliminare.getChiave());
		return cosmoCConfigurazioneMapper.toDTOLight(paramtroDaEliminare);
	}

	@Override
	@Transactional(readOnly = true)
	public ParametroDiSistema getParamtroDiSistemaByChiave(String chiave) {
		String methodName = "getParamtriDiSistemaByChiave";

		ValidationUtils.require(chiave, "chiave parametro di sistema da cercare");

		CosmoCConfigurazione cosmoCConfigurazione = cosmoCConfigurazioneRepository.findOneActive(chiave).orElseThrow(
				() -> new NotFoundException(String.format(ErrorMessages.C_CONFIGURAZIONE_NON_TROVATA, chiave)));

		logger.error(methodName, String.format(ErrorMessages.C_CONFIGURAZIONE_NON_TROVATA, chiave));
		return cosmoCConfigurazioneMapper.toDTOLight(cosmoCConfigurazione);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ParametroDiSistema postParametroDiSistema(CreaParametroDiSistemaRequest request) {
		Timestamp now = Timestamp.from(Instant.now());
		String methodName = "postParametroDiSistema";
		ValidationUtils.require(request, "request");
		ValidationUtils.validaAnnotations(request);

		// controlla che non ci siano conflitti
		checkConflict(CosmoCConfigurazione_.chiave, clean(request.getChiave()), null);

		CosmoCConfigurazione cc = new CosmoCConfigurazione();
		cc.setChiave(request.getChiave());
		cc.setValore(request.getValore());
		cc.setDescrizione(request.getDescrizione());
		cc.setDtInizioVal(now);

		var parametroSalvato = cosmoCConfigurazioneRepository.save(cc);
		logger.info(methodName, "Parametro di Sistema con chiave {} creato", parametroSalvato.getChiave());

		return cosmoCConfigurazioneMapper.toDTOLight(parametroSalvato);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ParametroDiSistema putParametroDiSistemaByChiave(String chiave, AggiornaParametroDiSistemaRequest request) {

		String methodName = "putParametroDiSistemaByChiave";
		ValidationUtils.require(chiave, "chiave");
		ValidationUtils.require(request, "request");
		ValidationUtils.validaAnnotations(request);

		CosmoCConfigurazione parametroDaAggiornare = cosmoCConfigurazioneRepository.findOneActive(chiave).orElseThrow(
				() -> new NotFoundException(String.format(ErrorMessages.C_CONFIGURAZIONE_NON_TROVATA, chiave)));

		parametroDaAggiornare.setValore(clean(request.getValore()));
		parametroDaAggiornare.setDescrizione(clean(request.getDescrizione()));

		parametroDaAggiornare = cosmoCConfigurazioneRepository.save(parametroDaAggiornare);

		logger.info(methodName, "parametro di sistema con chiave {} aggiornato", parametroDaAggiornare.getChiave());
		return cosmoCConfigurazioneMapper.toDTOLight(parametroDaAggiornare);
	}

	private void checkConflict(SingularAttribute<CosmoCConfigurazione, String> field, String value,
			String excludeChiave) {
		findByFieldEqualsIgnoreCase(field, value, excludeChiave).ifPresent(other -> {
			throw new ConflictException("Campo \"" + field.getName() + "\" gia' assegnato ad un parametro di sistema");
		});
	}

	private Optional<CosmoCConfigurazione> findByFieldEqualsIgnoreCase(
			SingularAttribute<CosmoCConfigurazione, String> field, String value, String excludeChiave) {
		return cosmoCConfigurazioneRepository.findAllActive((root, query, cb) -> {
			var condition = cb.equal(cb.upper(root.get(field)), value != null ? value.toUpperCase() : null);
			if (excludeChiave != null) {
				condition = cb.and(condition, cb.notEqual(root.get(CosmoCConfigurazione_.chiave), excludeChiave));
			}
			return condition;
		}).stream().findAny();
	}

	private String clean(String raw) {
		if (StringUtils.isBlank(raw)) {
			return null;
		}
		return raw.strip();
	}

}
