package tech.heiliger.handler;

import com.hazelcast.com.fasterxml.jackson.core.JsonProcessingException;
import com.hazelcast.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.osgi.service.event.EventProperties;
import org.wso2.carbon.identity.base.IdentityRuntimeException;
import org.wso2.carbon.identity.core.bean.context.MessageContext;
import org.wso2.carbon.identity.core.handler.InitConfig;
import org.wso2.carbon.identity.event.IdentityEventConstants;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.identity.event.handler.AbstractEventHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsHandler extends AbstractEventHandler {
    private static final Logger log = LogManager.getLogger(AnalyticsHandler.class.getName());
    @Override
    public void handleEvent(Event event) throws IdentityEventException {
        log.info("This is the event handler");
        for (var prop : event.getEventProperties().entrySet()) {
            log.info("PROP["+prop.getKey()+"] => "+prop.getValue());
        }
        try {
            if (event.getEventName().equals(IdentityEventConstants.Event.POST_AUTHENTICATION)) {
                publishData(event.getEventProperties());
            }
        } catch (Exception e) {
            log.error("Error during request", e);
        }
        log.info("==> Done <==");
    }

    private void publishData(Map<String, Object> props) throws IOException, InterruptedException {
        var mapper = new ObjectMapper();
        var values = new HashMap<String, String>();
        values.put("username", (String) props.get(IdentityEventConstants.EventProperty.USER_NAME));
        log.info(values.toString());
        var body = mapper.writeValueAsString(values);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/analytics"))
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        log.info("Request result => "+res.body());
    }

    @Override
    public String getName() {
        return "AnalyticsHandler";
    }

    @Override
    public int getPriority(MessageContext messageContext) {
        return 50;
    }

    @Override
    public void init(InitConfig configuration) throws IdentityRuntimeException {
        super.init(configuration);
    }
}
