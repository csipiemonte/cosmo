/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest.impl;

import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.FORMATO_FILTRO_NON_CORRETTO;
import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.OPERATORE_NON_SUPPORTATO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.BadRequestException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.entities.CosmoTPratica;

@Component
public class FilterParser {
  private Map<String, Criteria> mapCriterio = new HashMap<>();

  private Map<String, String> mapString = new HashMap<>();

  public FilterParser() {
    mapCriterio.put("eq", (cb, key, value) -> cb.equal(key, value));
    mapCriterio.put("ne", (cb, key, value) -> cb.notEqual(key, value));
    mapCriterio.put("gt", (cb, key, value) -> cb.greaterThan(key, value.toString()));
    mapCriterio.put("lt", (cb, key, value) -> cb.lessThan(key, value.toString()));
    mapCriterio.put("lte", (cb, key, value) -> cb.lessThanOrEqualTo(key, value.toString()));
    mapCriterio.put("gte", (cb, key, value) -> cb.greaterThanOrEqualTo(key, value.toString()));
    mapCriterio.put("c", (cb, key, value) -> cb.like(key, "%" + value + "%"));
    mapCriterio.put("ci",
        (cb, key, value) -> cb.like(cb.lower(key), "%" + value.toString().toLowerCase() + "%"));
    mapCriterio.put("s", (cb, key, value) -> cb.like(key, value + "%"));
    mapCriterio.put("e", (cb, key, value) -> cb.like(key, "%" + value));

    mapCriterio.put("si",
        (cb, key, value) -> cb.like(cb.lower(key), value.toString().toLowerCase() + "%"));
    mapCriterio.put("ei",
        (cb, key, value) -> cb.like(cb.lower(key), "%" + value.toString().toLowerCase()));

    mapCriterio.put("in", (cb, key, value) -> {
      List<Object> values = (List<Object>) value;
      In<Object> in = cb.in(key);
      values.forEach(in::value);
      return in;
    });
    mapCriterio.put("nin", (cb, key, value) -> {
      List<Object> values = (List<Object>) value;
      In<Object> in = cb.in(key);
      values.forEach(in::value);
      return cb.not(in);
    });

    mapString.put("stato.id", "cosmoDStatoPratica.id");
    mapString.put("tipo.id", "cosmoDTipoPratica.id");

  }

  private String toDbField(String key) {
    if (mapString.get(key) != null) {
      return mapString.get(key);
    }
    return key;
  }

  public Specification<CosmoTPratica> parse(String filter) {
    try {
      filter = filter != null ? filter : "{}";
      Map<String, Map<String, Object>> map = new ObjectMapper().readValue(filter, Map.class);
      return (root, query, cb) -> {
        List<Predicate> ls = map.entrySet().stream()
            .map(e -> createPredicate(root, cb, toDbField(e.getKey()), e.getValue()))
            .collect(Collectors.toList());
        ls.add(cb.isNull(root.get("dtCancellazione")));
        Predicate[] res = new Predicate[ls.size()];
        return cb.and(ls.toArray(res));
      };
    } catch (Exception e) {
      throw new BadRequestException(String.format(FORMATO_FILTRO_NON_CORRETTO));
    }
  }

  private Predicate createPredicate(Root<CosmoTPratica> root, CriteriaBuilder cb, String key,
      Map<String, Object> criterio) {

    List<Predicate> ls = criterio.entrySet().stream().map(e -> {
      String op = e.getKey();
      Object value = e.getValue();
      Criteria criteria = mapCriterio.get(op);
      if (criteria == null) {
        throw new BadRequestException(String.format(OPERATORE_NON_SUPPORTATO, op));
      }
      return criteria.apply(cb, getPath(root, key), value);

    }).collect(Collectors.toList());

    Predicate[] res = new Predicate[ls.size()];
    return cb.and(ls.toArray(res));
  }

  private Expression<String> getPath(Root<CosmoTPratica> root, String attributeName) {
    Path<CosmoTPratica> path = root;
    for (String part : attributeName.split("\\.")) {
      path = path.get(part);
    }
    return path.as(String.class);
  }

}
