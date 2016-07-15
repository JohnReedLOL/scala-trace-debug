package main;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import scala.trace.Debug;

public class TestJunit {

    @Test
    public void testOverflow() {
        String overflowStr = "";
        Debug.enableEverything();
        /*
        for(int i = 0; i < 2000000000; ++i) {
            overflowStr += "0"; // IntelliJ Console dies at i = 58000. Let's buffer at 10,000 for safety reasons.
            if(i % 1000 == 0) {
                System.err.println("i: " + i + "||" + overflowStr);
            }
        }
        Debug.trace("Done");
        */

        assertEquals("Junit is working fine","Junit is working fine");
    }

    @Test
    public void testAdd() {

        Debug.err("Fooo1");

        Debug.out("Fooo2");

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
        Debug.err("foo1");
        Debug.err("foo3", 3);
        Debug.out("foo1StdOut");
        Debug.out("foo3StdOut", 3);
        Debug.disableEverything();
        Debug.err("foo1");
        Debug.err("foo3", 3);
        Debug.out("foo1StdOut");
        Debug.out("foo3StdOut", 3);
        assertEquals("Junit is working fine",str);
    }
}
