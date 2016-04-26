package main;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import scala.trace.Debug;

public class TestJunit {
    @Test
    public void testAdd() {
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
