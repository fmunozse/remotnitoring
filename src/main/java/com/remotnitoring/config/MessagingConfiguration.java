package com.remotnitoring.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

import com.remotnitoring.messaging.ConsumerChannel;
import com.remotnitoring.messaging.ProducerChannel;

/**
 * Configures Spring Cloud Stream support.
 *
 * See http://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/
 * for more information.
 */
@EnableBinding(value = { ProducerChannel.class, ConsumerChannel.class})
public class MessagingConfiguration {

}
