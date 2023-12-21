/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.config;

import javax.jms.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.jndi.JndiObjectFactoryBean;


/**
 *
 */

@Configuration
public class MessagingConfig {

  private static final String SEND_QUEUE = "cosmocmmn-queue";


  @Bean
  public JndiObjectFactoryBean jndiFactory() {
    JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
   jndi.setJndiName("java:/ConnectionFactory");
    jndi.setLookupOnStartup(true);
    jndi.setProxyInterface(ConnectionFactory.class);
    return jndi;
  }

  @Bean
  public ConnectionFactory connectionFactory() {
    return new SingleConnectionFactory((ConnectionFactory) jndiFactory().getObject());
  }


  public ConnectionFactory cachingConnectionFactory() {
    CachingConnectionFactory cf = new CachingConnectionFactory();
    cf.setTargetConnectionFactory(connectionFactory());
    cf.setSessionCacheSize(10);
    return cf;
  }


  @Bean
  public JmsTemplate jmsTemplate() {
    JmsTemplate template = new JmsTemplate();
    template.setConnectionFactory(connectionFactory());
    template.setDefaultDestinationName(SEND_QUEUE);
    return template;
  }

  @Bean
  MessageConverter converter() {
    return new SimpleMessageConverter();
  }

}
