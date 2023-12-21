/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmo.testbed.model;

import static org.mockito.Mockito.mock;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmo.filter.AuthenticationFilter;
import it.csi.cosmo.cosmo.filter.ClientAuthenticationFilter;
import it.csi.cosmo.cosmo.security.SecurityInterceptor;
import it.csi.test.cosmo.cosmo.testbed.config.TestConstants;

public abstract class ParentIntegrationTest extends ParentUnitTest {

  private final Logger logger = Logger.getLogger(ParentIntegrationTest.class);

  private MockHttpServletRequest request;

  private MockHttpServletResponse response;

  private SecurityInterceptor securityInterceptor = new SecurityInterceptor();

  @Before
  public void beforeParentIntegrationTest() {
    simulaRequest();
  }

  protected void autentica(UserInfoDTO userInfo) {
    autentica(userInfo, null);
  }

  protected void autentica(UserInfoDTO userInfo, ClientInfoDTO clientInfo) {

    request.getSession(true);
    if (userInfo != null) {
      request.setAttribute(AuthenticationFilter.USERINFO_SESSIONATTR, userInfo);
    }
    if (clientInfo != null) {
      request.setAttribute(ClientAuthenticationFilter.CLIENTINFO_REQUESTATTR, clientInfo);
    }
  }

  protected void simulaRequest() {
    resetRequest();
    doFilterChain();
  }

  protected void doFilterChain() {
    // NOP
  }

  protected void resetRequest() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    // notify org.springframework.web.context.request.RequestContextListener
    ServletRequestAttributes sra = new ServletRequestAttributes(request);
    RequestContextHolder.setRequestAttributes(sra);

    getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
        getApplicationContext());

    request.setRequestURI(TestConstants.REQUEST_URL_VALIDA);
  }

  protected <T> T buildResource(Class<T> cl) {
    T output;
    try {
      output = cl.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      throw new InternalServerException("Errore in building resource", e);
    }
    getApplicationContext().getAutowireCapableBeanFactory().autowireBean(output);
    return output;
  }

  protected Response dispatch(Class<?> resourceInterface, Class<?> resourceImplementation,
      String methodName, Class<?>[] argTypes, Object[] args) {

    if (resourceImplementation != null) {
      logger.debug("dispatching to " + resourceImplementation.getSimpleName());
    } else {
      throw new InternalServerException("resourceImplementation is required");
    }

    try {
      if (resourceInterface == null || !resourceInterface.isInterface()) {
        throw new InvalidParameterException("invalid resource interface");
      }
      if (resourceImplementation.isInterface()) {
        throw new InvalidParameterException("invalid resource implementation");
      }

      Method methodImplementation = resourceImplementation.getMethod(methodName, argTypes);
      Object implementation = buildResource(resourceImplementation);

      return (Response) methodImplementation.invoke(implementation, args);

    } catch (Exception e) {
      throw new InternalServerException("manual dispatch failed: " + e.getMessage(), e);
    }
  }

  protected HttpHeaders getHttpHeaders() {
    return mock(HttpHeaders.class);
  }

  protected SecurityContext getSecurityContext() {
    return mock(SecurityContext.class);
  }

  protected MockHttpServletRequest getRequest() {
    return request;
  }

  protected MockHttpServletResponse getResponse() {
    return response;
  }

  protected SecurityInterceptor getSecurityInterceptor() {
    return securityInterceptor;
  }


}
