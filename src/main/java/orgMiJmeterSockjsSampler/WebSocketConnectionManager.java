package orgMiJmeterSockjsSampler;

import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.messaging.simp.stomp.StompSession;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class WebSocketConnectionManager {

    private WebSocketStompClient stompClient;

    public WebSocketConnectionManager() {
        StandardWebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
        SSLContext sslContext = createBlindTrustSSLContext();
        simpleWebSocketClient.getUserProperties().put("org.apache.tomcat.websockets.sslContext", sslContext);

        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(simpleWebSocketClient));

        SockJsClient sockJsClient = new SockJsClient(transports);
        stompClient = new WebSocketStompClient(sockJsClient);
    }

    public StompSession connect(URI endpoint, SockJsWebsocketStompSessionHandler sessionHandler) throws Exception {
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        return stompClient.connect(endpoint.toString(), headers, sessionHandler).get();
    }

    public void disconnect(StompSession session) {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        stompClient.stop();
    }

    private SSLContext createBlindTrustSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new BlindTrustManager()}, null);
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SSL context with blind trust manager", e);
        }
    }
}