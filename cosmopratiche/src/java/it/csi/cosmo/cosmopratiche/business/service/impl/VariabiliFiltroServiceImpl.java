/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service.impl;

import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.TIPO_FILTRO_NON_TROVATO;
import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.TIPO_FORMATO_NON_TROVATO;
import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.TIPO_PRATICA_NON_TROVATA;
import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.VARIABILEFILTRO_NON_TROVATA;
import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.VARIABILEFILTRO_NON_VALIDA;
import static it.csi.cosmo.cosmopratiche.util.Util.parseNumber;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDFiltroCampo;
import it.csi.cosmo.common.entities.CosmoDFormatoCampo;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoTVariabileProcesso;
import it.csi.cosmo.common.entities.CosmoTVariabileProcesso_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmopratiche.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmopratiche.business.service.VariabiliFiltroService;
import it.csi.cosmo.cosmopratiche.config.ErrorMessages;
import it.csi.cosmo.cosmopratiche.config.ParametriApplicativo;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaVariabiliProcessoDTO;
import it.csi.cosmo.cosmopratiche.dto.rest.FormatoVariabileDiFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.PageInfo;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.VariabileDiFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.VariabiliDiFiltroResponse;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoTVariabileProcessoMapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDFiltroCampoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDFormatoCampoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTVariabileProcessoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.specifications.CosmoTVariabileProcessoSpecifications;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;;

@Service
@Transactional
public class VariabiliFiltroServiceImpl implements VariabiliFiltroService {


  @Autowired
  private CosmoTVariabileProcessoRepository cosmoTVariabileProcessoRepository;

  @Autowired
  private CosmoDTipoPraticaRepository cosmoDTipoPraticaRepository;

  @Autowired
  private CosmoDFiltroCampoRepository cosmoDFiltroCampoRepository;

  @Autowired
  private CosmoDFormatoCampoRepository cosmoDFormatoCampoRepository;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  CosmoTVariabileProcessoMapper mapper;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteVariabiliFiltroId(String id) {
    long parsedId = parseNumber(id, -1, (e -> e != -1), VARIABILEFILTRO_NON_VALIDA);

    CosmoTVariabileProcesso variabileProceso =
        cosmoTVariabileProcessoRepository.findByIdAndDtCancellazioneIsNull(parsedId);
    if (variabileProceso == null) {
      throw new NotFoundException(String.format(VARIABILEFILTRO_NON_TROVATA, id));
    }

    variabileProceso.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    variabileProceso.setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

    this.cosmoTVariabileProcessoRepository.save(variabileProceso);

  }

  @Override
  @Transactional(readOnly = true)
  public VariabileDiFiltro getVariabiliFiltroId(String id) {

    long parsedId = parseNumber(id, -1, (e -> e > 0), VARIABILEFILTRO_NON_VALIDA);

    CosmoTVariabileProcesso variabileProcesso =
        cosmoTVariabileProcessoRepository.findByIdAndDtCancellazioneIsNull(parsedId);

    if (variabileProcesso == null) {
      throw new NotFoundException(String.format(VARIABILEFILTRO_NON_TROVATA, id));
    }

    return mapper.toVariabileDiFiltro(variabileProcesso);

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public VariabileDiFiltro postVariabiliFiltro(VariabileDiFiltro body) {

    ValidationUtils.require(body, "request body");
    ValidationUtils.validaAnnotations(body);

    // verifico il codice tipo pratica e ottengo la reference
    CosmoDTipoPratica tipoPratica = cosmoDTipoPraticaRepository
        .findOneActive(body.getTipoPratica().getCodice()).orElseThrow(() -> new BadRequestException(
            String.format(TIPO_PRATICA_NON_TROVATA, body.getTipoPratica().getCodice())));

    // verifico il codice tipo filtro e ottengo la reference
    CosmoDFiltroCampo filtroCampo = this.cosmoDFiltroCampoRepository
        .findOneActive(body.getTipoFiltro().getCodice()).orElseThrow(() -> new BadRequestException(
            String.format(TIPO_FILTRO_NON_TROVATO, body.getTipoFiltro().getCodice())));

    // verifico il codice formato filtro e ottengo la reference
    CosmoDFormatoCampo formatoCampo = this.cosmoDFormatoCampoRepository
        .findOneActive(body.getFormato().getCodice()).orElseThrow(() -> new BadRequestException(
            String.format(TIPO_FORMATO_NON_TROVATO, body.getFormato().getCodice())));

    if (this.cosmoTVariabileProcessoRepository
        .findByNomeVariabileAndTipoPraticaCodiceAndDtCancellazioneIsNull(body.getLabel(),
            body.getTipoPratica().getCodice())
        .isPresent())
      throw new BadRequestException(
          String.format(ErrorMessages.LABEL_VARIABILEFILTRO_DUPLICATA, body.getLabel(),
              body.getTipoPratica().getCodice()));

    if (cosmoTVariabileProcessoRepository.findOneNotDeleted((root, cq, cb) -> cb.and(
        cb.equal(root.get(CosmoTVariabileProcesso_.tipoPratica).get(CosmoDTipoPratica_.codice),
            body.getTipoPratica().getCodice()),
        cb.equal(root.get(CosmoTVariabileProcesso_.nomeVariabileFlowable), body.getNome())))
        .isPresent()) {
      throw new BadRequestException(String.format(ErrorMessages.NOME_VARIABILEFILTRO_DUPLICATA,
          body.getNome(), body.getTipoPratica().getCodice()));
    }

    CosmoTVariabileProcesso variabileProcesso = new CosmoTVariabileProcesso();
    variabileProcesso.setTipoPratica(tipoPratica);
    variabileProcesso.setFiltroCampo(filtroCampo);
    variabileProcesso.setFormatoCampo(formatoCampo);
    variabileProcesso.setNomeVariabile(body.getLabel());
    variabileProcesso.setNomeVariabileFlowable(body.getNome());
    variabileProcesso.setVisualizzareInTabella(body.isAggiungereARisultatoRicerca());

    variabileProcesso = this.cosmoTVariabileProcessoRepository.save(variabileProcesso);

    return mapper.toVariabileDiFiltro(variabileProcesso);

  }



  @Override
  @Transactional(rollbackFor = Exception.class)
  public VariabileDiFiltro putVariabiliFiltroId(String id, VariabileDiFiltro body) {

    long parsedId = parseNumber(id, -1, (e -> e != -1), VARIABILEFILTRO_NON_VALIDA);
    ValidationUtils.require(body, "request body");
    ValidationUtils.validaAnnotations(body);

    CosmoTVariabileProcesso variabileProcesso =
        this.cosmoTVariabileProcessoRepository.findByIdAndDtCancellazioneIsNull(parsedId);
    if (variabileProcesso == null) {
      throw new NotFoundException(String.format(VARIABILEFILTRO_NON_TROVATA, id));
    }

    // verifico il codice tipo pratica e ottengo la reference
    CosmoDTipoPratica tipoPratica = cosmoDTipoPraticaRepository
        .findOneActive(body.getTipoPratica().getCodice()).orElseThrow(() -> new BadRequestException(
            String.format(TIPO_PRATICA_NON_TROVATA, body.getTipoPratica().getCodice())));

    // verifico il codice tipo filtro e ottengo la reference
    CosmoDFiltroCampo filtroCampo = this.cosmoDFiltroCampoRepository
        .findOneActive(body.getTipoFiltro().getCodice()).orElseThrow(() -> new BadRequestException(
            String.format(TIPO_FILTRO_NON_TROVATO, body.getTipoFiltro().getCodice())));

    // verifico il codice formato filtro e ottengo la reference
    CosmoDFormatoCampo formatoCampo = this.cosmoDFormatoCampoRepository
        .findOneActive(body.getFormato().getCodice()).orElseThrow(() -> new BadRequestException(
            String.format(TIPO_FORMATO_NON_TROVATO, body.getFormato().getCodice())));

    Optional<CosmoTVariabileProcesso> duplicateVariabileProcesso =
        this.cosmoTVariabileProcessoRepository
        .findByNomeVariabileAndTipoPraticaCodiceAndDtCancellazioneIsNull(body.getLabel(),
            body.getTipoPratica().getCodice());
    if (duplicateVariabileProcesso.isPresent()
        && duplicateVariabileProcesso.get().getId().longValue() != parsedId)
      throw new BadRequestException(String.format(ErrorMessages.LABEL_VARIABILEFILTRO_DUPLICATA,
          body.getLabel(), body.getTipoPratica().getCodice()));

    var duplicateNome =
        cosmoTVariabileProcessoRepository.findOneNotDeleted((root, cq, cb) -> cb.and(
        cb.equal(root.get(CosmoTVariabileProcesso_.tipoPratica).get(CosmoDTipoPratica_.codice),
            body.getTipoPratica().getCodice()),
            cb.equal(root.get(CosmoTVariabileProcesso_.nomeVariabileFlowable), body.getNome())));

    if (duplicateNome.isPresent() && duplicateNome.get().getId().longValue() != parsedId) {
      throw new BadRequestException(String.format(ErrorMessages.NOME_VARIABILEFILTRO_DUPLICATA,
          body.getNome(), body.getTipoPratica().getCodice()));
    }

    variabileProcesso.setTipoPratica(tipoPratica);
    variabileProcesso.setFiltroCampo(filtroCampo);
    variabileProcesso.setFormatoCampo(formatoCampo);
    variabileProcesso.setNomeVariabile(body.getLabel());
    variabileProcesso.setNomeVariabileFlowable(body.getNome());
    variabileProcesso.setVisualizzareInTabella(body.isAggiungereARisultatoRicerca());


    CosmoTVariabileProcesso savedVariabileProcesso =
        this.cosmoTVariabileProcessoRepository.save(variabileProcesso);


    return mapper.toVariabileDiFiltro(savedVariabileProcesso);

  }



  @Override
  @Transactional(readOnly = true)
  public VariabiliDiFiltroResponse getVariabiliFiltro(String filter) {

    VariabiliDiFiltroResponse output = new VariabiliDiFiltroResponse();

    GenericRicercaParametricaDTO<FiltroRicercaVariabiliProcessoDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaVariabiliProcessoDTO.class);


    var size = ParametriApplicativo.MAX_PAGE_SIZE;

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(size).asInteger());


    Page<CosmoTVariabileProcesso> pageVariabiliProcesso = this.cosmoTVariabileProcessoRepository
        .findAll(
            CosmoTVariabileProcessoSpecifications.findByFilters(ricercaParametrica.getFilter(),
                ricercaParametrica.getSort()),
            paging);

    //@formatter:off
    output.setVariabiliDiFiltro(pageVariabiliProcesso.getContent().stream()
        .map(x -> mapRisultatoRicerca(x))
        .collect(Collectors.toCollection(LinkedList::new)));
    //@formatter:on

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(output.getVariabiliDiFiltro(),
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageVariabiliProcesso.getNumber());
    pageInfo.setPageSize(pageVariabiliProcesso.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageVariabiliProcesso.getTotalElements()));
    pageInfo.setTotalPages(pageVariabiliProcesso.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;

  }



  private VariabileDiFiltro mapRisultatoRicerca(CosmoTVariabileProcesso variabileProcesso) {
    if (variabileProcesso == null) {
      return null;
    }


    return this.mapper.toVariabileDiFiltro(variabileProcesso);
  }



  @Override
  public List<FormatoVariabileDiFiltro> getVariabiliFiltroFormati() {
    return this.cosmoDFormatoCampoRepository.findAllActive().stream().map(temp -> {
      FormatoVariabileDiFiltro formato = new FormatoVariabileDiFiltro();
      formato.setCodice(temp.getCodice());
      formato.setDescrizione(temp.getDescrizione());
      return formato;
    }).collect(Collectors.toList());
  }



  @Override
  public List<TipoFiltro> getVariabiliFiltroTipiFiltro() {
    return this.cosmoDFiltroCampoRepository.findAllActive().stream().map(temp -> {
      TipoFiltro tipoFiltro = new TipoFiltro();
      tipoFiltro.setCodice(temp.getCodice());
      tipoFiltro.setDescrizione(temp.getDescrizione());
      return tipoFiltro;
    }).collect(Collectors.toList());
  }



  @Override
  @Transactional(readOnly = true)
  public List<VariabileDiFiltro> getVariabiliFiltroTipoPratica(String codice) {
    var tipoPratica = cosmoDTipoPraticaRepository.findOneActive(codice);
    if (!tipoPratica.isPresent()) {
      throw new NotFoundException("Tipo pratica non trovato");
    }
    List<CosmoTVariabileProcesso> variabili = cosmoTVariabileProcessoRepository
        .findNotDeletedByField(CosmoTVariabileProcesso_.tipoPratica, tipoPratica.get());
    return variabili.stream().map(this::mapRisultatoRicerca).collect(Collectors.toList());
  }


}
