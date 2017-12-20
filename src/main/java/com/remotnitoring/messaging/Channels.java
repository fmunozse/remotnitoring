package com.remotnitoring.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Channels {
    
    String OUTPUT_ECHO_CHANNEL = "outputEchoChannel";
    
    String INPUT_ECHO_CHANNEL = "inputEchoChannel";

    @Input (Channels.INPUT_ECHO_CHANNEL) 
    SubscribableChannel inputEchoChannel();

    @Output (Channels.OUTPUT_ECHO_CHANNEL)
    MessageChannel outputEchoChannel();

    
}
