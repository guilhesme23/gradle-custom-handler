package tech.heiliger.internal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.wso2.carbon.identity.event.handler.AbstractEventHandler;
import tech.heiliger.handler.AnalyticsHandler;

public class Activator implements BundleActivator {
    private ServiceRegistration svc;
    private static final Logger log = LogManager.getLogger(Activator.class.getName());
    /**
     * Called when this bundle is started so the Framework can perform the
     * bundle-specific activities necessary to start this bundle. This method
     * can be used to register services or to allocate any resources that this
     * bundle needs.
     *
     * <p>
     * This method must complete and return to its caller in a timely manner.
     *
     * @param context The execution context of the bundle being started.
     * @throws Exception If this method throws an exception, this bundle is
     *                   marked as stopped and the Framework will remove this bundle's
     *                   listeners, unregister all services registered by this bundle, and
     *                   release all services used by this bundle.
     */
    @Override
    public void start(BundleContext context) throws Exception {
        log.info("Registering the Handler Service");
        try {
            svc = context.registerService(AbstractEventHandler.class.getName(), new AnalyticsHandler(), null);
            if (svc != null) {
                log.info("Done");
            } else {
                log.error("Error during service registration");
            }
        } catch (Exception e) {
            log.error("Error registering service "+e.getMessage());
        }
    }

    /**
     * Called when this bundle is stopped so the Framework can perform the
     * bundle-specific activities necessary to stop the bundle. In general, this
     * method should undo the work that the {@code BundleActivator.start} method
     * started. There should be no active threads that were started by this
     * bundle when this bundle returns. A stopped bundle must not call any
     * Framework objects.
     *
     * <p>
     * This method must complete and return to its caller in a timely manner.
     *
     * @param context The execution context of the bundle being stopped.
     * @throws Exception If this method throws an exception, the bundle is still
     *                   marked as stopped, and the Framework will remove the bundle's
     *                   listeners, unregister all services registered by the bundle, and
     *                   release all services used by the bundle.
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        if (svc != null) {
            svc.unregister();
        }
    }
}
