/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.async.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 */

/**
 *
 *
 */
public class ContextAwareCallable<T> implements Callable<T> {

  private boolean enableContextAwareness = true;
  private Callable<T> task;
  private RequestAttributes context;
  private Map<String, Object> attributes;

  public ContextAwareCallable(Callable<T> task, RequestAttributes context) {
    this.task = task;
    this.context = context;
    importAttributes(context);
  }

  @Override
  public T call() throws Exception {
    if (!enableContextAwareness) {
      return task.call();
    }

    if (context != null) {
      if (attributes != null && context instanceof ServletRequestAttributes) {
        ServletRequestAttributes scra = (ServletRequestAttributes) context;
        ServletRequestAttributes sra = new ServletRequestAttributes(
            createAsyncRequestForContext(scra.getRequest(), attributes));
        RequestContextHolder.setRequestAttributes(sra);
        // if (scra.getRequest() != null) {
        // attributes.forEach((k, v) -> scra.getRequest().setAttribute(k, v));
        // }
      } else {
        RequestContextHolder.setRequestAttributes(context);
      }

    } else {
      RequestContextHolder.resetRequestAttributes();
    }

    try {
      return task.call();
    } finally {
      RequestContextHolder.resetRequestAttributes();
    }
  }

  @SuppressWarnings("unchecked")
  private void importAttributes(RequestAttributes context) {
    Map<String, Object> attributes = new HashMap<>();
    if (context instanceof ServletRequestAttributes) {
      ServletRequestAttributes scra = (ServletRequestAttributes) context;
      if (scra.getRequest() == null) {
        return;
      }

      scra.getRequest().getAttributeNames().asIterator().forEachRemaining(attributeName -> {
        Object attribute = scra.getRequest().getAttribute((String) attributeName);
        attributes.put((String) attributeName, attribute);
      });

    }
    this.attributes = attributes;
  }

  private HttpServletRequest createAsyncRequestForContext(HttpServletRequest source,
      Map<String, Object> attributes) {
    return new HttpServletRequest() {

      @Override
      public Object getAttribute(String arg0) {
        return attributes.get(arg0);
      }

      @Override
      public Enumeration getAttributeNames() {
        return new Vector(attributes.keySet()).elements();
      }

      @Override
      public String getCharacterEncoding() {
        return source.getCharacterEncoding();
      }

      @Override
      public int getContentLength() {
        return source.getContentLength();
      }

      @Override
      public String getContentType() {
        return source.getContentType();
      }

      @Override
      public ServletInputStream getInputStream() throws IOException {
        return source.getInputStream();
      }

      @Override
      public String getLocalAddr() {
        return source.getLocalAddr();
      }

      @Override
      public String getLocalName() {
        return source.getLocalName();
      }

      @Override
      public int getLocalPort() {
        return source.getLocalPort();
      }

      @Override
      public Locale getLocale() {
        return source.getLocale();
      }

      @Override
      public Enumeration getLocales() {
        return source.getLocales();
      }

      @Override
      public String getParameter(String arg0) {
        return source.getParameter(arg0);
      }

      @Override
      public Map getParameterMap() {
        return source.getParameterMap();
      }

      @Override
      public Enumeration getParameterNames() {
        return source.getParameterNames();
      }

      @Override
      public String[] getParameterValues(String arg0) {
        return source.getParameterValues(arg0);
      }

      @Override
      public String getProtocol() {
        return source.getProtocol();
      }

      @Override
      public BufferedReader getReader() throws IOException {
        return source.getReader();
      }

      @SuppressWarnings("deprecation")
      @Override
      public String getRealPath(String arg0) {
        return source.getRealPath(arg0);
      }

      @Override
      public String getRemoteAddr() {
        return source.getRemoteAddr();
      }

      @Override
      public String getRemoteHost() {
        return source.getRemoteHost();
      }

      @Override
      public int getRemotePort() {
        return source.getRemotePort();
      }

      @Override
      public RequestDispatcher getRequestDispatcher(String arg0) {
        return source.getRequestDispatcher(arg0);
      }

      @Override
      public String getScheme() {
        return source.getScheme();
      }

      @Override
      public String getServerName() {
        return source.getServerName();
      }

      @Override
      public int getServerPort() {
        return source.getServerPort();
      }

      @Override
      public boolean isSecure() {
        return source.isSecure();
      }

      @Override
      public void removeAttribute(String arg0) {
        source.removeAttribute(arg0);
        attributes.remove(arg0);
      }

      @Override
      public void setAttribute(String arg0, Object arg1) {
        source.setAttribute(arg0, arg1);
        attributes.put(arg0, arg1);
      }

      @Override
      public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
        source.setCharacterEncoding(arg0);
      }

      @Override
      public String getAuthType() {
        return source.getAuthType();
      }

      @Override
      public String getContextPath() {
        return source.getContextPath();
      }

      @Override
      public Cookie[] getCookies() {
        return source.getCookies();
      }

      @Override
      public long getDateHeader(String arg0) {
        return source.getDateHeader(arg0);
      }

      @Override
      public String getHeader(String arg0) {
        return source.getHeader(arg0);
      }

      @Override
      public Enumeration getHeaderNames() {
        return source.getHeaderNames();
      }

      @Override
      public Enumeration getHeaders(String arg0) {
        return source.getHeaders(arg0);
      }

      @Override
      public int getIntHeader(String arg0) {
        return source.getIntHeader(arg0);
      }

      @Override
      public String getMethod() {
        return source.getMethod();
      }

      @Override
      public String getPathInfo() {
        return source.getPathInfo();
      }

      @Override
      public String getPathTranslated() {
        return source.getPathTranslated();
      }

      @Override
      public String getQueryString() {
        return source.getQueryString();
      }

      @Override
      public String getRemoteUser() {
        return source.getRemoteUser();
      }

      @Override
      public String getRequestURI() {
        return source.getRequestURI();
      }

      @Override
      public StringBuffer getRequestURL() {
        return source.getRequestURL();
      }

      @Override
      public String getRequestedSessionId() {
        return source.getRequestedSessionId();
      }

      @Override
      public String getServletPath() {
        return source.getServletPath();
      }

      @Override
      public HttpSession getSession() {
        return source.getSession();
      }

      @Override
      public HttpSession getSession(boolean arg0) {
        return source.getSession(arg0);
      }

      @Override
      public Principal getUserPrincipal() {
        return source.getUserPrincipal();
      }

      @Override
      public boolean isRequestedSessionIdFromCookie() {
        return source.isRequestedSessionIdFromCookie();
      }

      @Override
      public boolean isRequestedSessionIdFromURL() {
        return source.isRequestedSessionIdFromURL();
      }

      @Override
      public boolean isRequestedSessionIdFromUrl() {
        return source.isRequestedSessionIdFromUrl();
      }

      @Override
      public boolean isRequestedSessionIdValid() {
        return source.isRequestedSessionIdValid();
      }

      @Override
      public boolean isUserInRole(String arg0) {
        return source.isUserInRole(arg0);
      }

    };
  }
}
