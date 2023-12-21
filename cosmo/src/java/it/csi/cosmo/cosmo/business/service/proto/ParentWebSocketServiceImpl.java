/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service.proto;

import java.io.IOException;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmo.dto.ws.WebSocketApplicationEvent;
import it.csi.cosmo.cosmo.dto.ws.WebSocketEvent;
import it.csi.cosmo.cosmo.dto.ws.WebSocketEventType;
import it.csi.cosmo.cosmo.dto.ws.WebSocketJointSession;
import it.csi.cosmo.cosmo.dto.ws.WebSocketMessageType;
import it.csi.cosmo.cosmo.dto.ws.proto.WebSocketRequest;
import it.csi.cosmo.cosmo.dto.ws.proto.WebSocketResponse;
import it.csi.cosmo.cosmo.filter.AuthenticationFilter;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;

public abstract class ParentWebSocketServiceImpl<I, O> implements ParentWebSocketService<O> {

  protected CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.WEBSOCKET_LOG_CATEGORY, this.getClass().getSimpleName());

  private static final int EXECUTOR_INTERVAL = 15 * 1000;

  private final Map<String, WebSocketJointSession> sessions =
      Collections.synchronizedMap(new HashMap<String, WebSocketJointSession>());

  public abstract O onMessage(WebSocketJointSession jointSession, I input);

  public abstract void onEvent(WebSocketJointSession jointSession, WebSocketEvent<Object> event);

  public abstract Class<I> getInputClass();

  public abstract Class<O> getOutputClass();

  public void broadcast(O payload) {
    this.broadcast(payload, null);
  }

  @Override
  public void broadcast(O payload, Predicate<UserInfoDTO> userFilter) {
    String method = "broadcast";

    logger.info(method, "received broadcast request");

    if (this.sessions == null || this.sessions.isEmpty()) {
      logger.info(method, "broadcasting skipped (no target candidates)");
      return;
    }

    this.sessions.values().stream()
    .filter(session -> userFilter == null || (userFilter.test(session.getPrincipal())))
    .forEach(targetSession -> {
      logger.info(method, "broadcasting to session " + targetSession.getId());
      try {
        this.send(targetSession, payload, true);
      } catch (IOException e) {
        logger.error(method, "error broadcasting to session " + targetSession.getId(), e);
      }
    });

    logger.info(method, "broadcasting completed");
  }

  @Override
  public void broadcastEvent(String event, Object payload, Predicate<UserInfoDTO> userFilter) {
    String method = "broadcast";

    logger.info(method, "received broadcast event request");

    if (this.sessions == null || this.sessions.isEmpty()) {
      logger.info(method, "broadcasting skipped for event (no target candidates)");
      return;
    }

    WebSocketApplicationEvent mess = new WebSocketApplicationEvent(event, payload);

    this.sessions.values().stream()
    .filter(session -> userFilter == null || (userFilter.test(session.getPrincipal())))
    .forEach(targetSession -> {
      logger.info(method, "broadcasting to session " + targetSession.getId());
      try {
        this.sendApplicationEvent(targetSession, mess, true);
      } catch (IOException e) {
        logger.error(method, "error broadcasting to session " + targetSession.getId(), e);
      }
    });

    logger.info(method, "broadcasting completed");
  }

  private ObjectMapper getMapper() {
    return ObjectUtils.getDataMapper();
  }

  private Object executorLock = new Object();

  private ScheduledExecutorService scheduledExecutor = null;

  @Override
  public Object handleRawMessage(Session session, String message) {
    String method = "handleMessage";

    WebSocketJointSession jointSession = sessions.getOrDefault(session.getId(), null);
    jointSession.setLastIncomingMessage(OffsetDateTime.now());

    logger.debug(method, "received message: [" + message + "]");

    WebSocketMessageType type;
    try {
      type = getMessageType(message);
    } catch (Exception e) {
      return convertOutput(fail("Unknown or invalid message type"));
    }

    if (type == null) {
      return convertOutput(fail("No message type specified"));
    } else if (type == WebSocketMessageType.RESPONSE) {
      return convertOutput(fail("Invalid message type specified: " + type.name()));
    }

    if (type == WebSocketMessageType.REQUEST) {
      return processRawRequest(jointSession, message);
    } else {
      processRawEvent(jointSession, message);
      return null;
    }
  }

  private String processRawRequest(WebSocketJointSession jointSession, String message) {
    String method = "processRawRequest";
    WebSocketRequest<I> input;
    try {
      input = convertInput(message);
    } catch (IOException e) {
      return convertOutput(fail("Error reading input", e));
    }

    O output;
    try {
      output = onMessage(jointSession, input.getRequest());
    } catch (Exception e) {
      return convertOutput(fail("Error processing your request", e));
    }

    WebSocketResponse response = buildResponse(output);

    String outputRaw = convertOutput(response);

    logger.debug(method, "returning response message output: " + outputRaw);

    return outputRaw;
  }

  private String processRawEvent(WebSocketJointSession jointSession, String message) {

    WebSocketEvent<Object> input;
    try {
      input = convertInputEvent(message);
    } catch (IOException e) {
      return convertOutput(fail("Error reading input event", e));
    }

    try {
      onEvent(jointSession, input);
    } catch (Exception e) {
      return convertOutput(fail("Error processing your event", e));
    }
    return null;
  }

  private WebSocketMessageType getMessageType(String raw) throws IOException {
    ObjectMapper mapper = getMapper();
    TypeReference<HashMap<String, String>> typeRef =
        new TypeReference<HashMap<String, String>>() {};
        HashMap<String, String> map = mapper.readValue(raw, typeRef);

        return WebSocketMessageType.valueOf(map.getOrDefault("messageType", null));
  }

  private WebSocketResponse fail(String message) {
    logger.error("fail", message);

    WebSocketResponse response = new WebSocketResponse();
    response.setResponse(null);
    response.setCode("ERROR");
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.setTitle(message);

    return response;
  }

  private WebSocketResponse fail(String message, Throwable t) {
    logger.error("fail", message, t);

    WebSocketResponse response = new WebSocketResponse();
    response.setResponse(null);
    response.setCode("ERROR");
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.setTitle(message + " - " + t.getMessage());

    return response;
  }

  private WebSocketResponse buildResponse(Object responseData) {
    WebSocketResponse response = new WebSocketResponse();
    response.setResponse(responseData);
    response.setCode("OK");
    response.setStatus(HttpStatus.OK.value());
    response.setTitle(null);

    return response;
  }

  private WebSocketRequest<I> convertInput(String raw) throws IOException {
    ObjectMapper mapper = getMapper();
    JavaType type =
        mapper.getTypeFactory().constructParametricType(WebSocketRequest.class, getInputClass());
    return mapper.readValue(raw, type);
  }

  @SuppressWarnings("unchecked")
  private WebSocketEvent<Object> convertInputEvent(String raw) throws IOException {
    ObjectMapper mapper = getMapper();
    return mapper.readValue(raw, WebSocketEvent.class);
  }

  private String convertOutput(Object response) {
    try {
      return getMapper().writeValueAsString(response);
    } catch (IOException e) {
      return "{\"code\":\"WRITE_ERROR\",\"status\":500,\"title\":\"Internal error writing response\"}";
    }
  }

  @Override
  public void handleRawConnectionOpened(Session session, HttpSession httpSession,
      EndpointConfig config) throws IOException {

    String method = "handleConnectionOpened";

    logger.info(method, "WebSocket opened: " + session.getId());

    UserInfoDTO userInfo = httpSession != null
        ? (UserInfoDTO) httpSession.getAttribute(AuthenticationFilter.USERINFO_SESSIONATTR)
            : null;

        WebSocketJointSession jointSession = new WebSocketJointSession(session, userInfo);
        sessions.put(session.getId(), jointSession);

        sendEvent(jointSession,
            new WebSocketEvent<>(WebSocketEventType.CONNECTION_OPENED,
                "Connection opened with websocket server. Welcome "
                    + (userInfo != null ? userInfo.getNome() : "guest user")));

        try {
          this.onConnectionOpened(jointSession);
        } catch (Exception e) {
          logger.error(method, "Error handling connection opened event", e);
        }

        scheduleExecutorIfNeeded();
  }

  private void scheduleExecutorIfNeeded() {
    String method = "scheduleExecutorIfNeeded";
    synchronized (executorLock) {

      if (this.scheduledExecutor == null && !this.sessions.isEmpty()) {
        logger.debug(method,
            "scheduled executor is not running but there are active sessions - launching executor");

        this.scheduledExecutor = Executors.newScheduledThreadPool(4);
        this.scheduledExecutor.scheduleAtFixedRate(this::executorTickWrapped, 500,
            EXECUTOR_INTERVAL, TimeUnit.MILLISECONDS);
      }
    }
  }

  private void clearExecutorIfNotNeeded() {
    String method = "clearExecutorIfNotNeeded";
    synchronized (executorLock) {

      if (this.scheduledExecutor != null && this.sessions.isEmpty()) {
        logger.debug(method, "no sessions left - clearing scheduled executor");
        this.scheduledExecutor.shutdown();
        this.scheduledExecutor = null;
      }
    }
  }

  private void executorTickWrapped() {
    try {
      executorTick();
    } catch (Throwable t) { // NOSONAR
      logger.error("executorTickWrapped", "error in scheduled executor tick", t);
    }
  }

  private void executorTick() {
    String method = "executorTick";
    List<WebSocketJointSession> sessionsSnapshot =
        this.sessions.values().stream().collect(Collectors.toList());
    sessionsSnapshot.forEach(session -> {
      logger.debug(method, "sending keep-alive event to session " + session.getId());
      try {
        sendEvent(session, new WebSocketEvent<Serializable>(WebSocketEventType.KEEP_ALIVE,
            "keep-alive from server"), true);
      } catch (Exception e) {
        logger.error(method, "error sending single keep-alive event to session " + session.getId(),
            e);
      }
    });
  }

  @Override
  public void handleRawConnectionClosed(Session session, CloseReason reason) {
    String method = "handleConnectionClosed";

    logger.info(method,
        "Closing a WebSocket due to " + reason.getCloseCode() + " - " + reason.getReasonPhrase());

    WebSocketJointSession jointSession = sessions.getOrDefault(session.getId(), null);

    try {
      this.onConnectionClosed(jointSession, reason);
    } catch (Exception e) {
      logger.error(method, "Error handling connection closed event", e);
    }

    this.sessions.remove(session.getId());

    clearExecutorIfNotNeeded();
  }

  @Override
  public void onConnectionOpened(WebSocketJointSession session) throws IOException {
    logger.debug("onConnectionOpened", "Class " + this.getClass().getSimpleName()
        + " did not provide an handler for connection opened event");
  }

  @Override
  public void onConnectionClosed(WebSocketJointSession session, CloseReason reason) {
    logger.debug("onConnectionClosed", "Class " + this.getClass().getSimpleName()
        + " did not provide an handler for connection closed event");
  }

  protected void sendEvent(WebSocketJointSession session, WebSocketEvent<?> event)
      throws IOException {
    sendEvent(session, event, true);
  }

  protected void sendEvent(WebSocketJointSession session, WebSocketEvent<?> event, boolean async)
      throws IOException {
    logger.debug("sendEvent", "sending event of type " + event.getEvent());

    if (async) {
      session.getWsSession().getAsyncRemote().sendText(convertOutput(event));
    } else {
      session.getWsSession().getBasicRemote().sendText(convertOutput(event));
    }
  }

  protected void sendApplicationEvent(WebSocketJointSession session,
      WebSocketApplicationEvent event, boolean async) throws IOException {
    logger.debug("sendEvent", "sending event of type " + event.getEvent());

    if (async) {
      session.getWsSession().getAsyncRemote().sendText(convertOutput(event));
    } else {
      session.getWsSession().getBasicRemote().sendText(convertOutput(event));
    }
  }

  protected void send(WebSocketJointSession session, O payload, boolean async) throws IOException {
    logger.debug("send",
        "sending payload of type " + (payload != null ? payload.getClass().getName() : "null"));

    String response = convertOutput(buildResponse(payload));

    if (logger.isDebugEnabled()) {
      logger.debug("send", "sending payload [" + response + "]");
    }

    if (async) {
      session.getWsSession().getAsyncRemote().sendText(response);
    } else {
      session.getWsSession().getBasicRemote().sendText(response);
    }
  }
}
