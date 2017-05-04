package ru.spbau.yaveyn.java.junithw;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import sun.plugin.dom.exception.InvalidStateException;

/**
 * Created by bronti on 04.05.17.
 */
public class ThreadExpectedExceptionTest {

    @Rule
    public ThreadExpectedException tee = new ThreadExpectedException();



    @Test
    public void testSuccess() {
        Thread t = new Thread () {
            @Override
            public void run(){
                throw new InvalidStateException("");
            }
        };
        tee.expect(InvalidStateException.class);
        tee.registerThread(t);
        t.start();
        try {
            Thread.sleep(100);
        }
        catch (Exception e) {
            // do nothing
        }
    }

    @Test
    public void testNotExpected() {
        Thread t = new Thread () {
            @Override
            public void run(){
                throw new InvalidStateException("");
            }
        };
        tee.registerThread(t);
        t.start();
        try {
            Thread.sleep(100);
        }
        catch (Exception e) {
            // do nothing
        }
    }

    @Test
    @Ignore
    public void testFail() {
        Thread t = new Thread () {
            @Override
            public void run(){
                throw new InvalidStateException("");
            }
        };
        tee.expect(InvalidArgumentException.class);
        tee.registerThread(t);
        t.start();
        try {
            Thread.sleep(100);
        }
        catch (Exception e) {
            // do nothing
        }
    }
}
