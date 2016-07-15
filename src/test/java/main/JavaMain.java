package main;

import scala.trace.Debug;

public class JavaMain {
    public static void sleep() throws Exception {
        Thread.sleep(9);
    }

    public static void main(String args[]) throws Exception {
        Debug.err("Trace to standard error");
        sleep();
        Debug.out("Trace to standard out");
        sleep();
        Debug.out("0 lines of trace", 0);
        sleep();
        Debug.out("2 lines of trace", 2);
        sleep();
        Debug.checkOut(7 == 8, "assertion failures are bright red", 1);
        sleep();
        String[] array = {"1", "2", "3"};
        Debug.arrayErr(array, 0, 3, 1); // 3 elements from 0
        Debug.assertOut(7 == 8, "assert is fatal, check is not", 1);
    }
}
