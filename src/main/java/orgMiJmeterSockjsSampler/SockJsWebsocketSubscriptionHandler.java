package orgMiJmeterSockjsSampler;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class SockJsWebsocketSubscriptionHandler implements StompFrameHandler {

    private ResponseMessage responseMessage;
    
    public SockJsWebsocketSubscriptionHandler(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        // Define the type of payload expected
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        // Handle the received message
        String message = (String) payload;
        String logMessage = "Received message: " + message;
        responseMessage.addMessage(logMessage);
        // Further processing can be added here
    }
}