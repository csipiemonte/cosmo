/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.util.logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import it.csi.cosmo.common.components.AbstractCosmoRequestLoggingInterceptor;
import it.csi.cosmo.common.components.RequestFilterRequestAdapter;
import it.csi.cosmo.common.components.RequestFilterResponseAdapter;
import it.csi.cosmo.common.monitoring.HibernatePerRequestStatisticsCollector;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmocmmn.config.Constants;
import it.csi.cosmo.cosmocmmn.util.listener.SpringApplicationContextHelper;

public class RequestLoggingInterceptor extends HandlerInterceptorAdapter {

  AbstractCosmoRequestLoggingInterceptor instanceInterceptor =
      new AbstractCosmoRequestLoggingInterceptor(LogCategory.REST_LOG_CATEGORY.getCategory(),
          false) {

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
      };

  @Override
  public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse responseServlet,
      Object handler) throws Exception {

    // get hibernate request stats
    HibernatePerRequestStatisticsCollector ic =
        (HibernatePerRequestStatisticsCollector) SpringApplicationContextHelper
            .getBean("requestStatsCollector");

    ic.clearCounter();

    instanceInterceptor.before(getAdapterBeforeRequest(requestServlet, handler));

    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {

    instanceInterceptor.after(getAdapterAfterRequest(request, response, handler, modelAndView));
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception exception) throws Exception {
    // NOP
  }

  private RequestFilterRequestAdapter getAdapterBeforeRequest(HttpServletRequest requestServlet,
      Object handler) {

    return new RequestFilterRequestAdapter() {

      @Override
      public HttpServletRequest getRequest() {
        return RequestUtils.getCurrentRequest().orElse(null);
      }

      @Override
      public String getTargetClassName() {
        if (handler instanceof HandlerMethod) {
          var handlerMethod = (HandlerMethod) handler;
          if (handlerMethod.getBean() != null) {
            return handlerMethod.getClass().getSimpleName();
          }
        }
        return "<no class>";
      }

      @Override
      public String getTargetMethodName() {
        if (handler instanceof HandlerMethod) {
          var handlerMethod = (HandlerMethod) handler;
          if (handlerMethod.getMethod() != null) {
            return handlerMethod.getMethod().getName();
          }
        }
        return "<no class>";
      }
    };
  }

  private RequestFilterResponseAdapter getAdapterAfterRequest(HttpServletRequest request,
      HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    var b4 = getAdapterBeforeRequest(request, handler);

    return new RequestFilterResponseAdapter() {

      @Override
      public HttpServletRequest getRequest() {
        return b4.getRequest();
      }

      @Override
      public String getTargetClassName() {
        return b4.getTargetClassName();
      }

      @Override
      public String getTargetMethodName() {
        return b4.getTargetMethodName();
      }

      @Override
      public Object getResponseEntity() {
        return null;
      }

      @Override
      public Integer getResponseStatus() {
        return null;
      }

      @Override
      public Map<String, List<Object>> getResponseHeaders() {
        return null;
      }

      @Override
      public void addResponseHeaders(String key, String value) {
        response.addHeader(key, value);
      }

      @Override
      public void removeResponseHeader(String key) {
        response.setHeader(key, null);
      }
    };
  }
}
