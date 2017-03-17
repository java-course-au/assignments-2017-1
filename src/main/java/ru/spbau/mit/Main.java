package ru.spbau.mit;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        LOGGER.trace("trace");
        LOGGER.info("info");
        LOGGER.debug("debug");
        LOGGER.warn("warn");
        LOGGER.error("error");
        LOGGER.fatal("fatal");

    }

}
