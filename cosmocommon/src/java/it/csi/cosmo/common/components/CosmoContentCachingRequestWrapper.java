/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.components;

/**
 *
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

/**
 * {@link javax.servlet.http.HttpServletRequest} wrapper that caches all content read from the
 * {@linkplain #getInputStream() input stream} and {@linkplain #getReader() reader}, and allows this
 * content to be retrieved via a {@link #getContentAsByteArray() byte array}.
 *
 * <p>
 * Used e.g. by {@link org.springframework.web.filter.AbstractRequestLoggingFilter}. Note: As of
 * Spring Framework 5.0, this wrapper is built on the Servlet 3.1 API.
 *
 * @author Juergen Hoeller
 * @author Brian Clozel
 * @since 4.1.3
 * @see ContentCachingResponseWrapper
 */
public class CosmoContentCachingRequestWrapper extends HttpServletRequestWrapper {

  private final ByteArrayOutputStream cachedContent;

  private ServletInputStream inputStream;

  private BufferedReader reader;

  private byte[] inputStreamCache;

  /**
   * Create a new ContentCachingRequestWrapper for the given servlet request.
   * 
   * @param request the original servlet request
   */
  public CosmoContentCachingRequestWrapper(HttpServletRequest request) {
    super(request);
    int contentLength = request.getContentLength();
    this.cachedContent = new ByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);

    try {
      getInputStream();
    } catch (Exception e) {
      throw new RuntimeException("Error caching input stream content", e);
    }
  }

  public byte[] getCachedContent() {
    return inputStreamCache;
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    if (this.inputStream == null) {
      var is = getRequest().getInputStream();
      if (is != null) {
        inputStreamCache = is.readAllBytes();
        this.inputStream =
            new ContentCachingInputStream(getRequest().getInputStream(), inputStreamCache);
      }
    }
    return this.inputStream;
  }

  @Override
  public String getCharacterEncoding() {
    String enc = super.getCharacterEncoding();
    return (enc != null ? enc : WebUtils.DEFAULT_CHARACTER_ENCODING);
  }

  @Override
  public BufferedReader getReader() throws IOException {
    if (this.reader == null) {
      this.reader =
          new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
    }
    return this.reader;
  }

  @Override
  public String getParameter(String name) {
    if (this.cachedContent.size() == 0 && shouldDumpParameters()) {
      writeRequestParametersToCachedContent();
    }
    return super.getParameter(name);
  }

  @Override
  public Map<String, String[]> getParameterMap() {
    if (this.cachedContent.size() == 0 && shouldDumpParameters()) {
      writeRequestParametersToCachedContent();
    }
    return super.getParameterMap();
  }

  @Override
  public Enumeration<String> getParameterNames() {
    if (this.cachedContent.size() == 0 && shouldDumpParameters()) {
      writeRequestParametersToCachedContent();
    }
    return super.getParameterNames();
  }

  @Override
  public String[] getParameterValues(String name) {
    if (this.cachedContent.size() == 0 && shouldDumpParameters()) {
      writeRequestParametersToCachedContent();
    }
    return super.getParameterValues(name);
  }


  private boolean shouldDumpParameters() {
    /*
     * String contentType = getContentType(); return (contentType != null &&
     * contentType.contains(FORM_CONTENT_TYPE) && HttpMethod.POST.matches(getMethod()));
     *
     */
    return true;
  }

  private void writeRequestParametersToCachedContent() {
    try {
      if (this.cachedContent.size() == 0) {
        String requestEncoding = getCharacterEncoding();
        Map<String, String[]> form = super.getParameterMap();
        for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext();) {
          String name = nameIterator.next();
          List<String> values = Arrays.asList(form.get(name));
          for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext();) {
            String value = valueIterator.next();
            this.cachedContent.write(URLEncoder.encode(name, requestEncoding).getBytes());
            if (value != null) {
              this.cachedContent.write('=');
              this.cachedContent.write(URLEncoder.encode(value, requestEncoding).getBytes());
              if (valueIterator.hasNext()) {
                this.cachedContent.write('&');
              }
            }
          }
          if (nameIterator.hasNext()) {
            this.cachedContent.write('&');
          }
        }
      }
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to write request parameters to cached content", ex);
    }
  }

  /**
   * Return the cached request content as a byte array.
   * <p>
   * The returned array will never be larger than the content cache limit.
   * 
   * @see #ContentCachingRequestWrapper(HttpServletRequest, int)
   */
  public byte[] getContentAsByteArray() {
    return this.cachedContent.toByteArray();
  }

  private class ContentCachingInputStream extends ServletInputStream {

    private final ServletInputStream is;

    private final InputStream cachedContentStream;

    private boolean overflow = false;

    public ContentCachingInputStream(ServletInputStream is, byte[] cachedContent) {
      this.is = is;
      this.cachedContentStream = new ByteArrayInputStream(cachedContent);
    }

    @Override
    public int read() throws IOException {
      int ch = this.cachedContentStream.read();
      if (ch != -1 && !this.overflow) {
        cachedContent.write(ch);
      }
      return ch;
    }

    @Override
    public int read(byte[] b) throws IOException {
      int count = this.cachedContentStream.read(b);
      writeToCache(b, 0, count);
      return count;
    }

    private void writeToCache(final byte[] b, final int off, int count) {
      if (!this.overflow && count > 0) {
        cachedContent.write(b, off, count);
      }
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
      int count = this.cachedContentStream.read(b, off, len);
      writeToCache(b, off, count);
      return count;
    }

  }

}
