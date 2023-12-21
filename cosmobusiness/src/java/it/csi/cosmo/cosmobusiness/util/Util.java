/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.csi.cosmo.cosmobusiness.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;

@Component
public class Util {

  public RuntimeException manageException(HttpClientErrorException e, String detailM) {

    String msg;
    Integer status;
    try {
      msg = (String) new ObjectMapper().readValue(e.getResponseBodyAsString(), Map.class)
          .get(detailM);
      status = e.getRawStatusCode();
    } catch (Exception e1) {
      throw new RuntimeException(e1);
    }

    Map<Integer, Supplier<RuntimeException>> err = new LinkedHashMap<>();
    err.put(400, () -> new BadRequestException(msg));
    err.put(404, () -> new NotFoundException(msg));
    err.put(401, () -> new NotAuthorizedException(msg));
    return err.getOrDefault(status, () -> new InternalServerErrorException(msg)).get();
  }

  public Map<String, Object> toJson(String s) {
    try {
      return new ObjectMapper().readValue(s, Map.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String fromJson(Object s) {
    try {
      if (s == null)
        return null;
      ObjectMapper om = new ObjectMapper();
      om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      return om.writeValueAsString(s);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String getConfigurazioneParams(ParametriApplicativo param) {
    ConfigurazioneService configurazioneService = ConfigurazioneService.getInstance();
    return configurazioneService.getConfig(param).asString();
  }

}
