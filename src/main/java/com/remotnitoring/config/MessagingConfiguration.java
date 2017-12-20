package com.remotnitoring.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

import com.remotnitoring.messaging.Channels;
import com.remotnitoring.messaging.example.ConsumerChannel;
import com.remotnitoring.messaging.example.ProducerChannel;

/**
 * Configures Spring Cloud Stream support.
 *
 * See http://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/
 * for more information.
 */
@EnableBinding(value = { ProducerChannel.class, ConsumerChannel.class, Channels.class})
public class MessagingConfiguration {

}
