package org.hogel;

import java.io.FileNotFoundException;
import java.net.URL;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class LoggerConfigLoader {
    public static enum Mode {
        CONSOLE("logback-console"),
        FILE("logback-file");

        private final String[] resourceNames;
        private Mode(String resourceName) {
            resourceNames = new String[] {
                '/' + resourceName + "-test.xml",
                '/' + resourceName + ".xml",
            };
        }

        public URL getXmlResource() throws FileNotFoundException {
            for (String resourceName : resourceNames) {
                URL resource = LoggerConfigLoader.class.getResource(resourceName);
                if (resource != null) {
                    return resource;
                }
            }
            throw new FileNotFoundException("resource not found!");
        }
    }

    public static void setMode(Mode mode) throws FileNotFoundException, JoranException {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        context.reset();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);

        URL configUrl = mode.getXmlResource();
        configurator.doConfigure(configUrl);
    }
}
