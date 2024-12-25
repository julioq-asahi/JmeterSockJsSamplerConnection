package orgMiJmeterSockjsSampler;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class SockJsWebsocketStompSessionHandler extends StompSessionHandlerAdapter {

    private String subscribeHeaders;
    private ResponseMessage responseMessage;

    public SockJsWebsocketStompSessionHandler(String subscribeHeaders, long connectionTime, long responseBufferTime, ResponseMessage responseMessage) {
        this.subscribeHeaders = subscribeHeaders;
        this.responseMessage = responseMessage;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        String connectionMessage = "Session id: " + session.getSessionId()
                                   + "\n - WebSocket connection has been opened"
                                   + "\n - Connection established";
        this.responseMessage.addMessage(connectionMessage);
    }

    public void subscribeTo(StompSession session) {
        StompHeaders headers = new StompHeaders();
        String[] splitHeaders = subscribeHeaders.split("\n");
        for (String header : splitHeaders) {
            String[] headerParameter = header.split(":");
            headers.add(headerParameter[0], headerParameter[1]);
        }

        session.subscribe(headers, new SockJsWebsocketSubscriptionHandler(this.responseMessage));
    }
    
}