package ru.spbau.mit;

import org.apache.logging.log4j.*;

public final class LogTest {
    private static final Logger LOGGER = LogManager.getLogger(LogTest.class.getName());

    private LogTest() {

    }
    public static void main(String[] args) {
        LOGGER.fatal("it's fatal (Console)");
        LOGGER.error("it's error (Console)");
        LOGGER.info("it's info (file)");
        LOGGER.warn("it's warn (file)");
        LOGGER.trace("it's trace (ignore)");
        LOGGER.debug("it's debug (ignore)");
    }
}
