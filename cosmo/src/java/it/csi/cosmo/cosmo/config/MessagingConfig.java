/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.config;

import javax.jms.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.jndi.JndiObjectFactoryBean;
import it.csi.cosmo.cosmo.business.service.MessagingService;

/**
 *
 */

@Configuration
public class MessagingConfig {

  private static final String SEND_QUEUE = "cosmo-notifications-topic";
  private static final String RECEIVE_QUEUE = "cosmo-notifications-topic";

  // removed the autowired messagingService

  @Bean
  public JndiObjectFactoryBean jndiFactory() {
    JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
    jndi.setJndiName("java:/ConnectionFactory");
    jndi.setLookupOnStartup(false);
    jndi.setProxyInterface(ConnectionFactory.class);
    return jndi;
  }

  @Bean
  public ConnectionFactory connectionFactory() {
    return new SingleConnectionFactory((ConnectionFactory) jndiFactory().getObject());
  }

  /*
   * Optionally you can use cached connection factory if performance is a big concern.
   */
  public ConnectionFactory cachingConnectionFactory() {
    CachingConnectionFactory cf = new CachingConnectionFactory();
    cf.setTargetConnectionFactory(connectionFactory());
    cf.setSessionCacheSize(10);
    return cf;
  }

  /*
   * Message listener container, used for invoking messagingService.onMessage on message reception.
   */
  @Bean
  public MessageListenerContainer getContainer(MessagingService messagingService) {
    DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
    container.setConnectionFactory(connectionFactory());
    container.setDestinationName(RECEIVE_QUEUE);
    container.setMessageListener(messagingService);
    container.setPubSubDomain(true);
    return container;
  }

  /*
   * Used for Sending Messages.
   */
  @Bean
  public JmsTemplate jmsTemplate() {
    JmsTemplate template = new JmsTemplate();
    template.setConnectionFactory(connectionFactory());
    template.setDefaultDestinationName(SEND_QUEUE);
    template.setPubSubDomain(true);
    return template;
  }

  @Bean
  MessageConverter converter() {
    return new SimpleMessageConverter();
  }

}
