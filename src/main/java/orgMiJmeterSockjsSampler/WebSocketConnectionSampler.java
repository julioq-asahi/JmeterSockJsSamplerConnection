package orgMiJmeterSockjsSampler;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.springframework.messaging.simp.stomp.StompSession;

import java.net.URI;

public class WebSocketConnectionSampler extends AbstractJavaSamplerClient {

    private WebSocketConnectionManager connectionManager;

    @Override
    public Arguments getDefaultParameters() {
        Arguments args = new Arguments();
        args.addArgument("host", "ws://localhost:8080");
        args.addArgument("path", "/test-websocket");
        args.addArgument("subscribeHeaders", "id:sub-0\ndestination:/topic/messages");
        args.addArgument("connectionTime", "30000");
        return args;
    }

    @Override
    public void setupTest(JavaSamplerContext context) {
        connectionManager = new WebSocketConnectionManager();
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();
        result.sampleStart();

        ResponseMessage responseMessage = new ResponseMessage();
        String subscribeHeaders = context.getParameter("subscribeHeaders");
        long connectionTime = context.getLongParameter("connectionTime");

        try {
            URI stompUrlEndpoint = new URI(context.getParameter("host") + context.getParameter("path"));
            
            SockJsWebsocketStompSessionHandler sessionHandler = new SockJsWebsocketStompSessionHandler(
                subscribeHeaders, connectionTime, 0, responseMessage);

            StompSession session = connectionManager.connect(stompUrlEndpoint, sessionHandler);

            sessionHandler.subscribeTo(session);

            WebSocketSessionManager.getInstance().setSession(session);
            result.setSuccessful(true);
            result.setResponseMessage("Connected and subscribed successfully.\n" + responseMessage.getMessage());
        } catch (Exception e) {
            result.setSuccessful(false);
            result.setResponseMessage("Connection or subscription failed.\n" + e.getMessage());
            e.printStackTrace(); 
        } finally {
            result.sampleEnd();
        }

        return result;
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        StompSession session = WebSocketSessionManager.getInstance().getSession();
        if (session != null && session.isConnected()) {
            connectionManager.disconnect(session);
        }
    }
}