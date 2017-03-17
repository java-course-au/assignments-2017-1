package ru.spbau.mit;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class LoggingExample {
    private static final Logger LOG = LogManager.getLogger(LoggingExample.class);

    public static void main(String[] args) {
        LOG.info("Info message");
        LOG.error("Error message");
    }
}
