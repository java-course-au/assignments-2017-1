package ru.spbau.mit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LoggerExample {
    private static final Logger LOG = LogManager.getLogger(LoggerExample.class);

    private LoggerExample() {
    }

    public static void main(String[] args) {
        LOG.trace("Trace: ignored");
        LOG.debug("Debug: ignored");
        LOG.info("Info: file");
        LOG.warn("Warn: file");
        LOG.error("Error: console and file");
        LOG.fatal("Fatal: file");
    }
}
