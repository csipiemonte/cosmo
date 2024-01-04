/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.parser;

import java.util.HashMap;
import java.util.Map;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;
import it.doqui.acta.acaris.archive.EnumFascicoloRealeStatoType;
import it.doqui.acta.acaris.archive.EnumSerieFascicoliStatoType;
import it.doqui.acta.acaris.archive.EnumTipoDocumentoType;
import it.doqui.acta.acaris.archive.EnumTipologiaNumerazioneType;
import it.doqui.acta.acaris.archive.EnumVolumeStatoType;
import it.doqui.acta.acaris.archive.IdFascicoloStandardType;
import it.doqui.acta.acaris.archive.IdFormaDocumentariaType;
import it.doqui.acta.acaris.archive.IdStatoDiEfficaciaType;
import it.doqui.acta.acaris.common.ChangeTokenType;
import it.doqui.acta.acaris.common.CodiceFiscaleType;
import it.doqui.acta.acaris.common.IdAOOType;
import it.doqui.acta.acaris.common.IdAnnotazioniType;
import it.doqui.acta.acaris.common.IdNodoType;
import it.doqui.acta.acaris.common.IdStrutturaType;
import it.doqui.acta.acaris.common.ObjectIdType;


/**
 *
 */

public class ActaFieldConverterMap {

  protected static final CosmoLogger log = LoggerFactory
      .getLogger(LogCategory.BUSINESS_LOG_CATEGORY, ActaFieldConverterMap.class.getSimpleName());

  private static Map<Class<?>, ValueConverter> converterMap = null;

  public interface ValueConverter {

    public default Object convert(String input) {
      throw new RuntimeException("not implemented");
    }
  }

  public interface ArrayValueConverter extends ValueConverter {

    public Object convert(String[] input);
  }

  public static Map<Class<?>, ValueConverter> getConverterMap() {
    if (converterMap == null) {
      converterMap = new HashMap<>();
      initialize();
    }

    return converterMap;
  }

  private static void initialize() {

    log.trace("initialize", "building type converter map");

    converterMap.put(ObjectIdType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        ObjectIdType output = new ObjectIdType();
        output.setValue(input);
        return output;
      }
    });

    converterMap.put(EnumVolumeStatoType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        return EnumVolumeStatoType.fromValue(input);
      }
    });

    converterMap.put(IdNodoType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        IdNodoType output = new IdNodoType();
        output.setValue(Long.valueOf(input));
        return output;
      }
    });

    converterMap.put(EnumFascicoloRealeStatoType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        return EnumFascicoloRealeStatoType.fromValue(input);
      }
    });

    converterMap.put(EnumTipoDocumentoType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        return EnumTipoDocumentoType.fromValue(input);
      }
    });

    converterMap.put(EnumSerieFascicoliStatoType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        return EnumSerieFascicoliStatoType.fromValue(input);
      }
    });

    converterMap.put(EnumTipologiaNumerazioneType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        return EnumTipologiaNumerazioneType.fromValue(input);
      }
    });

    converterMap.put(IdAnnotazioniType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        IdAnnotazioniType output = new IdAnnotazioniType();
        output.setValue(Long.valueOf(input));
        return output;
      }
    });

    converterMap.put(IdStrutturaType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        IdStrutturaType output = new IdStrutturaType();
        output.setValue(Long.valueOf(input));
        return output;
      }
    });

    converterMap.put(CodiceFiscaleType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        CodiceFiscaleType output = new CodiceFiscaleType();
        output.setValue(input);
        return output;
      }
    });

    converterMap.put(ChangeTokenType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        ChangeTokenType output = new ChangeTokenType();
        output.setValue(input);
        return output;
      }
    });

    converterMap.put(IdAOOType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        IdAOOType output = new IdAOOType();
        output.setValue(Long.valueOf(input));
        return output;
      }
    });

    converterMap.put(IdFormaDocumentariaType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        IdFormaDocumentariaType output = new IdFormaDocumentariaType();
        output.setValue(Long.valueOf(input));
        return output;
      }
    });

    converterMap.put(IdStatoDiEfficaciaType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        IdStatoDiEfficaciaType output = new IdStatoDiEfficaciaType();
        output.setValue(Long.valueOf(input));
        return output;
      }
    });

    converterMap.put(IdFascicoloStandardType.class, new ValueConverter() {

      @Override
      public Object convert(String input) {
        IdFascicoloStandardType output = new IdFascicoloStandardType();
        output.setValue(Long.valueOf(input));
        return output;
      }
    });

  }
}
