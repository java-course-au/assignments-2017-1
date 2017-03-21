package ru.spbau.mit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

final class Main {
    private static final Logger LOG = LogManager.getRootLogger();
    private Main() {}
    public static void main(String[] args) {
        LOG.info("info");
        LOG.debug("debug");
        LOG.trace("trace");
        LOG.error("error");
        LOG.fatal("fatal");
    }
}
