package ru.spbau.mit;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class main {
    private static final Logger LOG = LogManager.getLogger(main.class.getName());
    public static void main(final String... args) {
        LOG.error("Error");
        LOG.warn("Warn");
        LOG.fatal("Fatal");
        LOG.info("Info");

    }
}