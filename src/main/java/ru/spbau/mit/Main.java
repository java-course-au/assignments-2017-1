package ru.spbau.mit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by bronti on 20.03.17.
 */
public class Main {
    private static final Logger LOG = LogManager.getLogger();

    public static void main(String[] args) {
        LOG.debug("This should not show up anywhere.");
        LOG.info("This should show up in the file.");
        LOG.error("This should show up both in the file and in the console.");
    }
}
