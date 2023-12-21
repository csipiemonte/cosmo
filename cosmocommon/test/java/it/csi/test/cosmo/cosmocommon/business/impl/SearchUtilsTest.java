/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmocommon.business.impl;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.test.cosmo.cosmocommon.testbed.model.ParentUnitTest;

public class SearchUtilsTest extends ParentUnitTest {

  private static final int MAX_PAGE_SIZE = 37;
  private static final int DEFAULT_PAGE_SIZE = 7;

  @Test
  public void testNull() {
    GenericRicercaParametricaDTO<?> input = null;

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);

    assertEquals(0, result.getPageNumber());
    assertEquals(DEFAULT_PAGE_SIZE, result.getPageSize());
    assertEquals(0, result.getOffset());
  }

  @Test
  public void testEmpty() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);

    assertEquals(0, result.getPageNumber());
    assertEquals(DEFAULT_PAGE_SIZE, result.getPageSize());
    assertEquals(0, result.getOffset());
  }

  @Test
  public void testPageOnly() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(2);
    input.setSize(null);

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);

    assertEquals(input.getPage().intValue(), result.getPageNumber());
    assertEquals(DEFAULT_PAGE_SIZE, result.getPageSize());
    assertEquals(input.getPage() * DEFAULT_PAGE_SIZE, result.getOffset());
  }

  @Test
  public void testSizeOnly() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(null);
    input.setSize(5);

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);

    assertEquals(0, result.getPageNumber());
    assertEquals(input.getSize().intValue(), result.getPageSize());
    assertEquals(0, result.getOffset());
  }

  @Test
  public void testPageSize() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(3);
    input.setSize(10);

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);

    assertEquals(3, result.getPageNumber());
    assertEquals(10, result.getPageSize());
    assertEquals(30, result.getOffset());
  }

  @Test
  public void testLimitOnly() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(5);

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);

    assertEquals(0, result.getPageNumber());
    assertEquals(input.getLimit().intValue(), result.getPageSize());
    assertEquals(0, result.getOffset());
  }

  @Test
  public void testOffsetOnly() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setOffset(7);

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);

    assertEquals(0, result.getPageNumber());
    assertEquals(DEFAULT_PAGE_SIZE, result.getPageSize());
    assertEquals(input.getOffset().intValue(), result.getOffset());
  }

  @Test
  public void testLimitOffset() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(5);
    input.setOffset(7);

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);

    assertEquals(0, result.getPageNumber());
    assertEquals(input.getLimit().intValue(), result.getPageSize());
    assertEquals(input.getOffset().intValue(), result.getOffset());
  }

  @Test
  public void testNullNoDefault() {
    GenericRicercaParametricaDTO<?> input = null;

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);

    assertEquals(0, result.getPageNumber());
    assertEquals(SearchUtils.DEFAULT_PAGE_SIZE, result.getPageSize());
    assertEquals(0, result.getOffset());
  }

  @Test
  public void testEmptyNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);

    assertEquals(0, result.getPageNumber());
    assertEquals(SearchUtils.DEFAULT_PAGE_SIZE, result.getPageSize());
    assertEquals(0, result.getOffset());
  }

  @Test
  public void testPageOnlyNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(2);
    input.setSize(null);

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);

    assertEquals(input.getPage().intValue(), result.getPageNumber());
    assertEquals(SearchUtils.DEFAULT_PAGE_SIZE, result.getPageSize());
    assertEquals(input.getPage() * SearchUtils.DEFAULT_PAGE_SIZE, result.getOffset());
  }

  @Test
  public void testSizeOnlyNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(null);
    input.setSize(5);

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);

    assertEquals(0, result.getPageNumber());
    assertEquals(input.getSize().intValue(), result.getPageSize());
    assertEquals(0, result.getOffset());
  }

  @Test
  public void testPageSizeNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(3);
    input.setSize(10);

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);

    assertEquals(3, result.getPageNumber());
    assertEquals(10, result.getPageSize());
    assertEquals(30, result.getOffset());
  }

  @Test
  public void testLimitOnlyNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(5);

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);

    assertEquals(0, result.getPageNumber());
    assertEquals(input.getLimit().intValue(), result.getPageSize());
    assertEquals(0, result.getOffset());
  }

  @Test
  public void testOffsetOnlyNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setOffset(7);

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);

    assertEquals(0, result.getPageNumber());
    assertEquals(SearchUtils.DEFAULT_PAGE_SIZE, result.getPageSize());
    assertEquals(input.getOffset().intValue(), result.getOffset());
  }

  @Test
  public void testLimitOffsetNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(5);
    input.setOffset(7);

    var result = SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);

    assertEquals(0, result.getPageNumber());
    assertEquals(input.getLimit().intValue(), result.getPageSize());
    assertEquals(input.getOffset().intValue(), result.getOffset());
  }

  @Test
  public void testNullNoParams() {
    GenericRicercaParametricaDTO<?> input = null;

    var result = SearchUtils.getPageRequest(input);

    assertEquals(0, result.getPageNumber());
    assertEquals(SearchUtils.DEFAULT_PAGE_SIZE, result.getPageSize());
    assertEquals(0, result.getOffset());
  }

  @Test
  public void testEmptyNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();

    var result = SearchUtils.getPageRequest(input);

    assertEquals(0, result.getPageNumber());
    assertEquals(SearchUtils.DEFAULT_PAGE_SIZE, result.getPageSize());
    assertEquals(0, result.getOffset());
  }

  @Test
  public void testPageOnlyNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(2);
    input.setSize(null);

    var result = SearchUtils.getPageRequest(input);

    assertEquals(input.getPage().intValue(), result.getPageNumber());
    assertEquals(SearchUtils.DEFAULT_PAGE_SIZE, result.getPageSize());
    assertEquals(input.getPage() * SearchUtils.DEFAULT_PAGE_SIZE, result.getOffset());
  }

  @Test
  public void testSizeOnlyNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(null);
    input.setSize(5);

    var result = SearchUtils.getPageRequest(input);

    assertEquals(0, result.getPageNumber());
    assertEquals(input.getSize().intValue(), result.getPageSize());
    assertEquals(0, result.getOffset());
  }

  @Test
  public void testPageSizeNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(3);
    input.setSize(10);

    var result = SearchUtils.getPageRequest(input);

    assertEquals(3, result.getPageNumber());
    assertEquals(10, result.getPageSize());
    assertEquals(30, result.getOffset());
  }

  @Test
  public void testLimitOnlyNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(5);

    var result = SearchUtils.getPageRequest(input);

    assertEquals(0, result.getPageNumber());
    assertEquals(input.getLimit().intValue(), result.getPageSize());
    assertEquals(0, result.getOffset());
  }

  @Test
  public void testOffsetOnlyNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setOffset(7);

    var result = SearchUtils.getPageRequest(input);

    assertEquals(0, result.getPageNumber());
    assertEquals(SearchUtils.DEFAULT_PAGE_SIZE, result.getPageSize());
    assertEquals(input.getOffset().intValue(), result.getOffset());
  }

  @Test
  public void testLimitOffsetNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(5);
    input.setOffset(7);

    SearchUtils.getPageRequest(input);
  }

  @Test(expected = BadRequestException.class)
  public void testSizeTooHigh() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(0);
    input.setSize(MAX_PAGE_SIZE + 1);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testSizeZero() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(0);
    input.setSize(0);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testSizeNegative() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(0);
    input.setSize(-1);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testPageNegative() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(-1);
    input.setSize(2);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testLimitZero() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(0);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testLimitNegative() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(-1);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testOffsetNegative() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setOffset(-4);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testLimitTooHigh() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(MAX_PAGE_SIZE + 1);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testPageAndOffset() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(1);
    input.setOffset(2);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testSizeAndLimit() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setSize(3);
    input.setLimit(4);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testSizeTooHighNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(0);
    input.setSize(MAX_PAGE_SIZE + 1);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testSizeZeroNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(0);
    input.setSize(0);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testSizeNegativeNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(0);
    input.setSize(-1);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testPageNegativeNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(-1);
    input.setSize(2);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testLimitZeroNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(0);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testLimitNegativeNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(-1);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testOffsetNegativeNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setOffset(-4);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testLimitTooHighNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(MAX_PAGE_SIZE + 1);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testPageAndOffsetNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(1);
    input.setOffset(2);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testSizeAndLimitNoDefault() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setSize(3);
    input.setLimit(4);

    SearchUtils.getPageRequest(input, MAX_PAGE_SIZE);
  }

  @Test(expected = BadRequestException.class)
  public void testSizeTooHighNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(0);
    input.setSize(SearchUtils.MAX_PAGE_SIZE + 1);

    SearchUtils.getPageRequest(input);
  }

  @Test(expected = BadRequestException.class)
  public void testSizeZeroNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(0);
    input.setSize(0);

    SearchUtils.getPageRequest(input);
  }

  @Test(expected = BadRequestException.class)
  public void testSizeNegativeNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(0);
    input.setSize(-1);

    SearchUtils.getPageRequest(input);
  }

  @Test(expected = BadRequestException.class)
  public void testPageNegativeNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(-1);
    input.setSize(2);

    SearchUtils.getPageRequest(input);
  }

  @Test(expected = BadRequestException.class)
  public void testLimitZeroNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(0);

    SearchUtils.getPageRequest(input);
  }

  @Test(expected = BadRequestException.class)
  public void testLimitNegativeNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(-1);

    SearchUtils.getPageRequest(input);
  }

  @Test(expected = BadRequestException.class)
  public void testOffsetNegativeNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setOffset(-4);

    SearchUtils.getPageRequest(input);
  }

  @Test(expected = BadRequestException.class)
  public void testLimitTooHighNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setLimit(SearchUtils.MAX_PAGE_SIZE + 1);

    SearchUtils.getPageRequest(input);
  }

  @Test(expected = BadRequestException.class)
  public void testPageAndOffsetNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setPage(1);
    input.setOffset(2);

    SearchUtils.getPageRequest(input);
  }

  @Test(expected = BadRequestException.class)
  public void testSizeAndLimitNoParams() {
    GenericRicercaParametricaDTO<?> input = new GenericRicercaParametricaDTO<>();
    input.setSize(3);
    input.setLimit(4);

    SearchUtils.getPageRequest(input);
  }

}
