package ru.spbau.mit;

import org.apache.logging.log4j.*;

public class LogTest {
    private static final Logger LOGGER = LogManager.getLogger(LogTest.class.getName());

    public static void main(String[] args) {
        LOGGER.fatal("it's fatal");
        LOGGER.error("it's error");
        LOGGER.info("it's info");
        LOGGER.warn("it's warn");
        LOGGER.trace("it's trace");
        LOGGER.debug("it's debug");
    }
}
