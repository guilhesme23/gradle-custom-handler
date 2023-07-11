package tech.heiliger.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wso2.carbon.identity.base.IdentityRuntimeException;
import org.wso2.carbon.identity.core.bean.context.MessageContext;
import org.wso2.carbon.identity.core.handler.InitConfig;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.identity.event.handler.AbstractEventHandler;

public class AnalyticsHandler extends AbstractEventHandler {
    private static final Logger log = LogManager.getLogger(AnalyticsHandler.class.getName());
    @Override
    public void handleEvent(Event event) throws IdentityEventException {
        log.info("This is the event handler");
        for (var prop : event.getEventProperties().entrySet()) {
            log.info("PROP["+prop.getKey()+"] => "+prop.getValue());
        }
        log.info("==> Done <==");
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
