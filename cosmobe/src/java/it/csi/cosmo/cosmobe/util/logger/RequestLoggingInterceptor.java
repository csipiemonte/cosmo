/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.util.logger;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Priority;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.annotations.interception.HeaderDecoratorPrecedence;
import it.csi.cosmo.common.components.CosmoRequestLoggingInterceptor;
import it.csi.cosmo.cosmobe.config.Constants;

@Provider
@HeaderDecoratorPrecedence
@Priority(1000)
public class RequestLoggingInterceptor extends CosmoRequestLoggingInterceptor {

  public RequestLoggingInterceptor() {
    super(LogCategory.REST_LOG_CATEGORY.getCategory(), true);
  }

  @Override
  protected Map<String, Object> gatherStatistics() {
    // gathers more stats when stat reporting is enabled
    Map<String, Object> output = new HashMap<>();
    output.put("toComponent", Constants.COMPONENT_NAME);

    return output;
  }
}
