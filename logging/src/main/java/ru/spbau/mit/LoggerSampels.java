package ru.spbau.mit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerSampels {

    private static final Logger LOGGER = LogManager.getLogger(LoggerSampels.class);

    public static void main(String[] args) {
        LOGGER.error("error");   // console
        LOGGER.fatal("fatal");   // file
        LOGGER.info("info");     // file
        LOGGER.warn("warn ");    // file
        LOGGER.debug("debug ");  // not visible
        LOGGER.trace("trace ");  // not visible
    }
}