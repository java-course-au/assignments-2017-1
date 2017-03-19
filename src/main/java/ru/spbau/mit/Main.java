package ru.spbau.mit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);

    private Main() {
    }

    public static void main(String[] args) {
        LOG.trace("Trace Message!"); // ignore
        LOG.debug("Debug Message!"); // ignore
        LOG.info("Info Message!");   // console
        LOG.warn("Warn Message!");   // console
        LOG.error("Error Message!"); // file
        LOG.fatal("Fatal Message!"); // console
    }
}
