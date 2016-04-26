package main;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import scala.trace.Debug;

public class TestJunit {
    @Test
    public void testAdd() {

        Debug.trace("Fooo1");

        Debug.traceStdOut("Fooo2");

        System.err.println("\"message\" in thread main:");

        // Debug.assertStdOut(false, "message", 3);

        System.err.println("^ The above stack trace leads to an assertion failure. ^");

        final char escape = (char)27;
        String clear = escape + "[0m"; // clear all formatting

        String s2 = escape + "[33mHelloWorld" + clear;

        String s = escape + "[36mHelloWorld" + clear;

        System.out.println(s);
        System.out.println(s2);

        String str= "Junit is working fine";
        Debug.trace("foo1");
        Debug.trace("foo3", 3);
        Debug.traceStdOut("foo1StdOut");
        Debug.traceStdOut("foo3StdOut", 3);
        Debug.disableEverything();
        Debug.trace("foo1");
        Debug.trace("foo3", 3);
        Debug.traceStdOut("foo1StdOut");
        Debug.traceStdOut("foo3StdOut", 3);
        assertEquals("Junit is working fine",str);
    }
}
