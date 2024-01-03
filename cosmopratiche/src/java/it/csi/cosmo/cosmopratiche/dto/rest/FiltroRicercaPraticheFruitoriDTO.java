/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.dto.rest;

/**
 *
 */

public class FiltroRicercaPraticheFruitoriDTO {
  
  private String codiceFiscaleUtente;
  private String codiceIpaEnte;
  private String codiceTipoPratica;
  private String codiceTag;
  private String apiManagerIdFruitore;
  private String sort;
  private String fields;
  private Integer page;
  private Integer size;
  private Integer limit;
  private Integer offset;
  
  public String getCodiceFiscaleUtente() {
    return codiceFiscaleUtente;
  }
  public void setCodiceFiscaleUtente(String codiceFiscaleUtente) {
    this.codiceFiscaleUtente = codiceFiscaleUtente;
  }
  public String getCodiceIpaEnte() {
    return codiceIpaEnte;
  }
  public void setCodiceIpaEnte(String codiceIpaEnte) {
    this.codiceIpaEnte = codiceIpaEnte;
  }
  public String getCodiceTipoPratica() {
    return codiceTipoPratica;
  }
  public void setCodiceTipoPratica(String codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }
  public String getCodiceTag() {
    return codiceTag;
  }
  public void setCodiceTag(String codiceTag) {
    this.codiceTag = codiceTag;
  }
  public String getApiManagerIdFruitore() {
    return apiManagerIdFruitore;
  }
  public void setApiManagerIdFruitore(String apiManagerIdFruitore) {
    this.apiManagerIdFruitore = apiManagerIdFruitore;
  }
  public String getSort() {
    return sort;
  }
  public void setSort(String sort) {
    this.sort = sort;
  }
  public String getFields() {
    return fields;
  }
  public void setFields(String fields) {
    this.fields = fields;
  }
  public Integer getPage() {
    return page;
  }
  public void setPage(Integer page) {
    this.page = page;
  }
  public Integer getSize() {
    return size;
  }
  public void setSize(Integer size) {
    this.size = size;
  }
  public Integer getLimit() {
    return limit;
  }
  public void setLimit(Integer limit) {
    this.limit = limit;
  }
  public Integer getOffset() {
    return offset;
  }
  public void setOffset(Integer offset) {
    this.offset = offset;
  }
}
