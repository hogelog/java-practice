package org.hogel;

import java.io.FileNotFoundException;

import org.hogel.LoggerConfigLoader.Mode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.joran.spi.JoranException;

public class LoggerConfigLoaderTest {
    private static final Logger LOG = LoggerFactory.getLogger("test");

    @Test
    public void console() throws FileNotFoundException, JoranException {
        LoggerConfigLoader.setMode(Mode.CONSOLE);
        LOG.debug("console debug!"); // -> stdout
    }

    @Test
    public void file() throws FileNotFoundException, JoranException {
        LoggerConfigLoader.setMode(Mode.FILE);
        LOG.debug("file debug!"); // -> target/file.log
    }

    @Test
    public void mixed() throws FileNotFoundException, JoranException {
        LoggerConfigLoader.setMode(Mode.CONSOLE);
        LOG.debug("console debug!"); // -> stdout
        LoggerConfigLoader.setMode(Mode.FILE);
        LOG.debug("file debug!"); // -> target/file.log
    }
}
