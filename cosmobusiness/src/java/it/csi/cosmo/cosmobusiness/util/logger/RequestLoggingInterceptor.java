/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.util.logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.annotations.interception.HeaderDecoratorPrecedence;
import it.csi.cosmo.common.components.CosmoRequestLoggingInterceptor;
import it.csi.cosmo.common.monitoring.HibernatePerRequestStatisticsCollector;
import it.csi.cosmo.cosmobusiness.config.Constants;
import it.csi.cosmo.cosmobusiness.util.listener.SpringApplicationContextHelper;

@Provider
@HeaderDecoratorPrecedence
@Priority(1000)
public class RequestLoggingInterceptor extends CosmoRequestLoggingInterceptor {

  public RequestLoggingInterceptor() {
    super(LogCategory.REST_LOG_CATEGORY.getCategory(), false);
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {

    // get hibernate request stats
    HibernatePerRequestStatisticsCollector ic =
        (HibernatePerRequestStatisticsCollector) SpringApplicationContextHelper
            .getBean("requestStatsCollector");

    ic.clearCounter();
    super.filter(requestContext);
  }

  @Override
  protected Map<String, Object> gatherStatistics() {
    // gathers more stats when stat reporting is enabled
    Map<String, Object> output = new HashMap<>();
    output.put("toComponent", Constants.COMPONENT_NAME);

    // get hibernate stats
    // StatisticsCollector sc =
    // (StatisticsCollector) SpringApplicationContextHelper.getBean("StatisticsCollector");
    // output.put("hibernateStats", sc.gatherRelevantStatistics());

    // get hibernate request stats
    HibernatePerRequestStatisticsCollector ic =
        (HibernatePerRequestStatisticsCollector) SpringApplicationContextHelper
            .getBean("requestStatsCollector");

    output.put("hibernateRequestStats", ic.gatherRelevantStatistics());

    return output;
  }
}
