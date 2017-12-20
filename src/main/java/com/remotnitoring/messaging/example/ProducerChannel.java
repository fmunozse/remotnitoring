package com.remotnitoring.messaging.example;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ProducerChannel {

    String MESSAGE_CHANNEL = "messageChannel";
    

    @Output (ProducerChannel.MESSAGE_CHANNEL)
    MessageChannel messageChannel();

    
}
