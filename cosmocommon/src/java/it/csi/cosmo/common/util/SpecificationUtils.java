/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import it.csi.cosmo.common.dto.rest.FilterCriteria;
import it.csi.cosmo.common.dto.search.AbstractFilter;
import it.csi.cosmo.common.dto.search.AbstractNumericFilter;
import it.csi.cosmo.common.dto.search.BooleanFilter;
import it.csi.cosmo.common.dto.search.DateFilter;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public interface SpecificationUtils {

  private static <T> Predicate applyAbstractFilter(AbstractFilter<T> filter, Expression<?> field,
      Predicate predicate, CriteriaBuilder cb) {

    if (filter == null) {
      return predicate;
    }

    if (filter.getDefined() != null) {
      predicate = cb.and(predicate,
          filter.getDefined().booleanValue() ? cb.isNotNull(field) : cb.isNull(field));
    }

    if (filter.getEquals() != null) {
      predicate = cb.and(predicate, cb.equal(field, filter.getEquals()));
    }

    if (filter.getNotEquals() != null) {
      predicate = cb.and(predicate, cb.notEqual(field, filter.getNotEquals()));
    }

    if (filter.getIn() != null) {
      predicate = cb.and(predicate, field.in(filter.getIn()));
    }

    if (filter.getNotIn() != null) {
      predicate = cb.and(predicate, cb.not(field.in(filter.getNotIn())));
    }

    return predicate;
  }

  public static <T extends Comparable<T>> Predicate applyFilter(AbstractNumericFilter<T> filter,
      Expression<T> field, Predicate predicate, CriteriaBuilder cb) {

    if (filter == null) {
      return predicate;
    }

    predicate = applyAbstractFilter(filter, field, predicate, cb);

    if (filter.getGreaterThan() != null) {
      predicate = cb.and(predicate, cb.greaterThan(field, filter.getGreaterThan()));
    }

    if (filter.getGreaterThanOrEqualTo() != null) {
      predicate =
          cb.and(predicate, cb.greaterThanOrEqualTo(field, filter.getGreaterThanOrEqualTo()));
    }

    if (filter.getLessThan() != null) {
      predicate = cb.and(predicate, cb.lessThan(field, filter.getLessThan()));
    }

    if (filter.getLessThanOrEqualTo() != null) {
      predicate = cb.and(predicate, cb.lessThanOrEqualTo(field, filter.getLessThanOrEqualTo()));
    }

    return predicate;
  }

  public static Predicate applyFilter(StringFilter filter, Expression<String> field,
      Predicate predicate, CriteriaBuilder cb) {

    if (filter == null) {
      return predicate;
    }

    predicate = applyAbstractFilter(filter, field, predicate, cb);

    if (filter.getContains() != null) {
      predicate = cb.and(predicate, cb.like(field, "%" + filter.getContains() + "%"));
    }

    if (filter.getContainsIgnoreCase() != null) {
      predicate = cb.and(predicate,
          cb.like(cb.lower(field), "%" + filter.getContainsIgnoreCase().toLowerCase() + "%"));
    }

    if (filter.getNotContains() != null) {
      predicate = cb.and(predicate, cb.not(cb.like(field, "%" + filter.getNotContains() + "%")));
    }

    if (filter.getNotContainsIgnoreCase() != null) {
      predicate = cb.and(predicate, cb.not(
          cb.like(cb.lower(field), "%" + filter.getNotContainsIgnoreCase().toLowerCase() + "%")));
    }

    if (filter.getLike() != null) {
      predicate = cb.and(predicate, cb.like(field, filter.getLike()));
    }

    if (filter.getNotLike() != null) {
      predicate = cb.and(predicate, cb.notLike(field, filter.getNotLike()));
    }

    if (filter.getEqualsIgnoreCase() != null) {
      predicate =
          cb.and(predicate, cb.equal(cb.upper(field), filter.getEqualsIgnoreCase().toUpperCase()));
    }

    if (filter.getNotEqualsIgnoreCase() != null) {
      predicate = cb.and(predicate,
          cb.notEqual(cb.upper(field), filter.getNotEqualsIgnoreCase().toUpperCase()));
    }

    if (filter.getInIgnoreCase() != null) {
      predicate = cb.and(predicate, cb.upper(field).in(toUpper(filter.getInIgnoreCase())));
    }

    if (filter.getNotInIgnoreCase() != null) {
      predicate =
          cb.and(predicate, cb.not(cb.upper(field).in(toUpper(filter.getNotInIgnoreCase()))));
    }

    if (filter.getStartsWith() != null) {
      predicate = cb.and(predicate, cb.like(field, filter.getStartsWith() + "%"));
    }

    if (filter.getEndsWith() != null) {
      predicate = cb.and(predicate, cb.like(field, "%" + filter.getEndsWith()));
    }

    if (filter.getStartsWithIgnoreCase() != null) {
      predicate = cb.and(predicate,
          cb.like(cb.upper(field), filter.getStartsWithIgnoreCase().toUpperCase() + "%"));
    }

    if (filter.getEndsWithIgnoreCase() != null) {
      predicate = cb.and(predicate,
          cb.like(cb.upper(field), "%" + filter.getEndsWithIgnoreCase().toUpperCase()));
    }


    return predicate;
  }

  public static Predicate applyFilter(BooleanFilter filter, Expression<Boolean> field,
      Predicate predicate, CriteriaBuilder cb) {

    if (filter == null) {
      return predicate;
    }

    predicate = applyAbstractFilter(filter, field, predicate, cb);

    return predicate;
  }

  public static Predicate applyFilter(DateFilter filter, Expression<Timestamp> field, // NOSONAR
      Predicate predicate, CriteriaBuilder cb) {

    if (filter == null) {
      return predicate;
    }

    // applyAbstractFilter non chiamato perche' il tipo differisce (LocalDate != Timestamp)

    Function<LocalDate, Timestamp> toTimestamp = raw -> Timestamp.valueOf(raw.atStartOfDay());

    Function<LocalDate, Predicate> equalsOneDateBuilder = (LocalDate value) -> cb.and(
        cb.greaterThanOrEqualTo(field, toTimestamp.apply(value)),
        cb.lessThan(field, toTimestamp.apply(value.plusDays(1))));

    if (filter.getDefined() != null) {
      predicate = cb.and(predicate,
          filter.getDefined().booleanValue() ? cb.isNotNull(field) : cb.isNull(field));
    }

    if (filter.getEquals() != null) {
      //@formatter:off
      predicate = cb.and(
          predicate,
          equalsOneDateBuilder.apply(filter.getEquals())
          );
      //@formatter:on
    }

    if (filter.getNotEquals() != null) {
      //@formatter:off
      predicate = cb.and(
          predicate,
          cb.not(equalsOneDateBuilder.apply(filter.getNotEquals()))
          );
      //@formatter:on
    }

    if (filter.getIn() != null) {
      //@formatter:off
      if (filter.getIn().length < 1) {
        predicate = cb.and(predicate, cb.isTrue(cb.literal(false)));
      } else {
        predicate = cb.and(
            predicate,
            cb.or(
                Arrays.stream(filter.getIn())
                .map(equalsOneDateBuilder)
                .collect(Collectors.toList())
                .toArray(new Predicate[0])
                )
            );
      }
      //@formatter:on
    }

    if (filter.getNotIn() != null) {
      //@formatter:off
      if (filter.getNotIn().length < 1) {
        predicate = cb.and(predicate, cb.isTrue(cb.literal(true)));
      } else {
        predicate = cb.and(
            predicate,
            cb.not(
                cb.or(
                    Arrays.stream(filter.getNotIn())
                    .map(equalsOneDateBuilder)
                    .collect(Collectors.toList())
                    .toArray(new Predicate[0])
                    )
                )
            );
      }
      //@formatter:on
    }

    if (filter.getGreaterThan() != null) {
      //@formatter:off
      predicate = cb.and(predicate,
          cb.greaterThanOrEqualTo( // not a typo: X > data, in timestamp, si traduce con X >= (data + 1 gg).atStartOfDay()
              field,
              toTimestamp.apply(filter.getGreaterThan().plusDays(1))
              )
          );
      //@formatter:on
    }

    if (filter.getGreaterThanOrEqualTo() != null) {
      //@formatter:off
      predicate = cb.and(predicate,
          cb.greaterThanOrEqualTo(
              field,
              toTimestamp.apply(filter.getGreaterThanOrEqualTo())
              )
          );
      //@formatter:on
    }

    if (filter.getLessThan() != null) {
      //@formatter:off
      predicate = cb.and(predicate,
          cb.lessThan(
              field,
              toTimestamp.apply(filter.getLessThan())
              )
          );
      //@formatter:on
    }

    if (filter.getLessThanOrEqualTo() != null) {
      //@formatter:off
      predicate = cb.and(predicate,
          cb.lessThan( // not a typo: X <= data, in timestamp, si traduce con X < (data + 1 gg).atStartOfDay()
              field,
              toTimestamp.apply(filter.getLessThanOrEqualTo().plusDays(1))
              )
          );
      //@formatter:on
    }

    return predicate;
  }

  private static Collection<String> toUpper(String[] input) {
    Collection<String> output = new ArrayList<>(input.length);
    for (int i = 0; i < input.length; i++) {
      output.add(input[i] != null ? input[i].toUpperCase() : null);
    }
    return output;
  }

  public static Predicate applyStringFilter(Map<String, Object> filter, Path<String> field,
      Predicate predicate, CriteriaBuilder cb) {

    if (filter == null || filter.isEmpty()) {
      return predicate;
    }

    if (filter.containsKey(FilterCriteria.DEFINED.getCodice())) {
      if (Boolean.parseBoolean((String) filter.get(FilterCriteria.DEFINED.getCodice()))) {
        predicate = cb.and(predicate, cb.isNotNull(field));
      } else {
        predicate = cb.and(predicate, cb.isNull(field));
      }
    }

    if (filter.containsKey(FilterCriteria.EQUALS.getCodice())) {
      predicate = cb.and(predicate, cb.equal(field, filter.get(FilterCriteria.EQUALS.getCodice())));
    }

    if (filter.containsKey(FilterCriteria.NOT_EQUALS.getCodice())) {
      predicate =
          cb.and(predicate, cb.notEqual(field, filter.get(FilterCriteria.NOT_EQUALS.getCodice())));
    }

    if (filter.containsKey(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())) {
      predicate = cb.and(predicate,
          cb.like(cb.lower(field), "%"
              + ((String) filter.get(FilterCriteria.CONTAINS_IGNORE_CASE.getCodice())).toLowerCase()
              + "%"));

    }

    if (filter.containsKey(FilterCriteria.CONTAINS.getCodice())) {
      predicate = cb.and(predicate,
          cb.like(field, "%" + filter.get(FilterCriteria.CONTAINS.getCodice()) + "%"));

    }

    if (filter.containsKey(FilterCriteria.IN.getCodice())) {
      predicate = cb.and(predicate, field.in(filter.get(FilterCriteria.IN.getCodice())));
    }

    if (filter.containsKey(FilterCriteria.NOT_IN.getCodice())) {
      predicate =
          cb.and(predicate, cb.not(field.in(filter.get(FilterCriteria.NOT_IN.getCodice()))));
    }

    if (filter.containsKey(FilterCriteria.STARTS_WITH.getCodice())) {
      predicate = cb.and(predicate,
          cb.like(field, filter.get(FilterCriteria.STARTS_WITH.getCodice()) + "%"));
    }

    if (filter.containsKey(FilterCriteria.ENDS_WITH.getCodice())) {
      predicate =
          cb.and(predicate, cb.like(field, "%" + filter.get(FilterCriteria.ENDS_WITH.getCodice())));
    }

    if (filter.containsKey(FilterCriteria.STARTS_WITH_IGNORE_CASE.getCodice())) {
      predicate = cb.and(predicate,
          cb.like(cb.lower(field),
              ((String) filter.get(FilterCriteria.STARTS_WITH_IGNORE_CASE.getCodice()))
              .toLowerCase() + "%"));
    }

    if (filter.containsKey(FilterCriteria.ENDS_WITH_IGNORE_CASE.getCodice())) {
      predicate =
          cb.and(predicate, cb.like(cb.lower(field),
              "%" + ((String) filter.get(FilterCriteria.ENDS_WITH_IGNORE_CASE.getCodice()))
              .toLowerCase()));
    }


    return predicate;
  }

  public static Predicate applyNumericFilter(Map<String, String> filter,
      Path<? extends Number> field, Predicate predicate, CriteriaBuilder cb) {

    if (filter == null || filter.isEmpty()) {
      return predicate;
    }

    if (filter.containsKey(FilterCriteria.DEFINED.getCodice())) {
      if (Boolean.parseBoolean(filter.get(FilterCriteria.DEFINED.getCodice()))) {
        predicate = cb.and(predicate, cb.isNotNull(field));
      } else {
        predicate = cb.and(predicate, cb.isNull(field));
      }
    }

    if (filter.containsKey(FilterCriteria.EQUALS.getCodice())) {
      predicate = cb.and(predicate, cb.equal(field, filter.get(FilterCriteria.EQUALS.getCodice())));
    }

    if (filter.containsKey(FilterCriteria.IN.getCodice())) {
      predicate = cb.and(predicate, field.in(filter.get(FilterCriteria.IN.getCodice())));

    }
    if (filter.containsKey(FilterCriteria.NOT_IN.getCodice())) {
      predicate =
          cb.and(predicate, cb.not(field.in(filter.get(FilterCriteria.NOT_IN.getCodice()))));

    }
    if (filter.containsKey(FilterCriteria.NOT_EQUALS.getCodice())) {
      predicate = cb.and(predicate,
          cb.not(cb.equal(field, filter.get(FilterCriteria.NOT_EQUALS.getCodice()))));
    }

    return predicate;
  }

}
