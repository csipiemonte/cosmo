/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.dto.search.OffsetLimitPageable;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;

/**
 * Classe per funzioni di utilities comuni alle ricerche
 */
public abstract class SearchUtils {

  public static final int DEFAULT_PAGE_SIZE = 20;

  public static final int MAX_PAGE_SIZE = 1000;

  private static final Pattern SORT_FORMAT_REGEX =
      Pattern.compile("^\\s*([\\+\\-]?)\\s*([^\\s]*)\\s*(asc|desc|ASC|DESC)?\\s*$");

  private SearchUtils() {
    // private
  }

  public static <T> GenericRicercaParametricaDTO<T> getRicercaParametrica(String filter,
      Class<T> filterClass) {

    if (StringUtils.isBlank(filter)) {
      return new GenericRicercaParametricaDTO<>();
    }

    ObjectMapper mapper = ObjectUtils.getDataMapper();

    try {
      JavaType type = mapper.getTypeFactory()
          .constructParametricType(GenericRicercaParametricaDTO.class, filterClass);
      return mapper.readValue(filter, type);
    } catch (IOException e) {
      throw new BadRequestException(e.getMessage(), e);
    }
  }

  public static Pageable getPageRequest(GenericRicercaParametricaDTO<?> input) {
    return getPageRequest(input, null, null);
  }

  public static Pageable getPageRequest(GenericRicercaParametricaDTO<?> input,
      Integer maxPageSize) {
    return getPageRequest(input, maxPageSize, null);
  }

  public static Pageable getPageRequest(GenericRicercaParametricaDTO<?> input, Integer maxPageSize, // NOSONAR
      Integer defaultPageSize) {

    Integer page = null;
    Integer size = null;

    if (maxPageSize == null) {
      maxPageSize = MAX_PAGE_SIZE;
    }
    if (defaultPageSize == null) {
      defaultPageSize = DEFAULT_PAGE_SIZE;
    }

    Integer inputPage = input != null ? input.getPage() : null;
    Integer inputSize = input != null ? input.getSize() : null;
    Integer inputOffset = input != null ? input.getOffset() : null;
    Integer inputLimit = input != null ? input.getLimit() : null;

    if (inputPage != null && inputOffset != null) {
      throw new BadRequestException("Only one of 'page' and 'offset' should be specified");
    }
    if (inputSize != null && inputLimit != null) {
      throw new BadRequestException("Only one of 'size' and 'limit' should be specified");
    }

    if (inputPage != null) {
      if (inputPage >= 0) {
        page = inputPage;
      } else {
        throw new BadRequestException("Negative page indexes are not allowed");
      }
    }

    if (inputSize != null) {
      if (inputSize <= 0) {
        throw new BadRequestException("Negative sized or empty pages are not allowed");
      } else if (inputSize <= maxPageSize) {
        size = inputSize;
      } else {
        throw new BadRequestException("Page sizes over " + maxPageSize + " are not allowed");
      }
    }

    if (inputLimit != null) {
      if (inputLimit <= 0) {
        throw new BadRequestException("Negative sized or empty pages are not allowed");
      } else if (inputLimit <= maxPageSize) {
        size = inputLimit;
      } else {
        throw new BadRequestException("Page limits over " + maxPageSize + " are not allowed");
      }
    }

    if (size == null) {
      size = defaultPageSize;
    }
    if (page == null) {
      page = 0;
    }

    if (inputOffset != null) {
      if (inputOffset < 0) {
        throw new BadRequestException("Negative offsets are not allowed");
      }
      return OffsetLimitPageable.of(inputOffset, 0, size);
    }

    return new PageRequest(page, size);
  }

  /*
   * //@formatter:off
   * accepted formats: 
   * field 
   * + field 
   * - field 
   * +field 
   * -field 
   * field ASC 
   * field DESC 
   * field asc 
   * field desc
   * //@formatter:on
   */
  public static Order parseOrderClause(String raw) { // NOSONAR
    if (StringUtils.isBlank(raw)) {
      return null;
    }

    Matcher matcher = SORT_FORMAT_REGEX.matcher(raw);
    if (matcher.matches() && matcher.groupCount() >= 2) {
      Direction direction = null;
      String property;

      String firstGroupMatch = matcher.group(1);
      String secondGroupMatch = matcher.group(2);
      String thirdGroupMatch = matcher.group(3);

      if (!StringUtils.isBlank(firstGroupMatch)) {
        // validi "+" e "-"
        firstGroupMatch = firstGroupMatch.trim();
        if ("+".equals(firstGroupMatch)) {
          direction = Direction.ASC;
        } else if ("-".equals(firstGroupMatch)) {
          direction = Direction.DESC;
        } else {
          throw new BadRequestException(
              String.format("Il token '%s' non e' una clausola di direzione ordinamento valida",
                  firstGroupMatch));
        }
      }

      if (StringUtils.isBlank(secondGroupMatch)) {
        throw new BadRequestException(
            "Il token '" + secondGroupMatch + "' non e' un campo di ordinamento valido");
      }
      property = secondGroupMatch.trim();

      if (!StringUtils.isBlank(thirdGroupMatch)) {
        if (direction != null) {
          throw new BadRequestException("Troppe clausole di direzione ordinamento utilizzate");
        }

        thirdGroupMatch = thirdGroupMatch.toLowerCase();
        if ("asc".equals(thirdGroupMatch)) {
          direction = Direction.ASC;
        } else if ("desc".equals(thirdGroupMatch)) {
          direction = Direction.DESC;
        } else {
          throw new BadRequestException("Il token '" + thirdGroupMatch
              + "' non e' una clausola di direzione ordinamento valida");
        }
      }

      return new Order(direction, property);

    } else {
      throw new BadRequestException(
          "Il pattern '" + raw + "' non e' una clausola di ordinamento valida");
    }
  }

  public static <T> void filterFields(Collection<T> entities, Collection<String> fields) {
    if (entities == null || entities.isEmpty() || fields == null || fields.isEmpty()) {
      return;
    }

    // get first object
    Object first = null;
    for (Object entry : entities) {
      if (entry != null) {
        first = entry;
        break;
      }
    }
    if (first == null) {
      return;
    }

    // compute fields to remove
    Field[] allEntityFields = FieldUtils.getAllFields(first.getClass());
    Set<String> fieldsToKeepUpperCase = fields.stream().map(s -> s.trim().toUpperCase())
        .filter(StringUtils::isNotBlank).collect(Collectors.toSet());

    if (fieldsToKeepUpperCase.isEmpty()) {
      return;
    }

    List<Field> fieldsToRemove =
        Arrays.stream(allEntityFields).filter(field -> !field.getType().isPrimitive())
            .filter(field -> !fieldsToKeepUpperCase.contains(field.getName().toUpperCase()))
            .collect(Collectors.toList());

    // iterate entities to remove fields
    if (fieldsToRemove.isEmpty()) {
      return;
    }

    entities.forEach(entity -> {
      fieldsToRemove.forEach(fieldToRemove -> {
        try {
          FieldUtils.writeField(fieldToRemove, entity, null, true);
        } catch (IllegalAccessException e) {
          throw new InternalServerException("Cannot polish fields on entities", e);
        }
      });
    });
  }
}
