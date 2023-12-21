/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.search;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetLimitPageable implements Pageable {

  private int offset;
  private int page;
  private int size;
  private Sort sort = null;

  protected OffsetLimitPageable(int offset, int page, int size) {
    if (offset < 0) {
      throw new IllegalArgumentException("Offset must not be less than zero!");
    }

    if (page < 0) {
      throw new IllegalArgumentException("Page index must not be less than zero!");
    }

    if (size < 1) {
      throw new IllegalArgumentException("Page size must not be less than one!");
    }

    this.offset = offset;
    this.page = page;
    this.size = size;
  }

  public static OffsetLimitPageable of(int offset, int page, int size) {
    return new OffsetLimitPageable(offset, page, size);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.domain.Pageable#getPageNumber()
   */
  @Override
  public int getPageNumber() {
    return page;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.domain.Pageable#getPageSize()
   */
  @Override
  public int getPageSize() {
    return size;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.domain.Pageable#getOffset()
   */
  @Override
  public int getOffset() {
    return offset + page * size;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.domain.Pageable#getSort()
   */
  @Override
  public Sort getSort() {
    return sort;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.domain.Pageable#next()
   */
  @Override
  public Pageable next() {
    return of(offset, page + 1, size);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.domain.Pageable#previousOrFirst()
   */
  @Override
  public Pageable previousOrFirst() {
    return hasPrevious() ? of(offset, page - 1, size) : first();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.domain.Pageable#first()
   */
  @Override
  public Pageable first() {
    return of(offset, 0, size);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.domain.Pageable#hasPrevious()
   */
  @Override
  public boolean hasPrevious() {
    return page > 0;
  }
}
