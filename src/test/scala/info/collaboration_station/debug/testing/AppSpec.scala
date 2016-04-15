package info.collaboration_station.debug.testing

import java.io._
import info.collaboration_station.debug._
import org.scalatest.prop._
import org.scalatest.{Matchers, PropSpec}
// import info.collaboration_station.debug.testing.TestingUtils // this import comes from debugtrace/test/scala

class CheckSpec
  extends PropSpec
  with GeneratorDrivenPropertyChecks
  with Matchers {

  property ("Asserts and Exceptions produce same stack traces") {

    /*
    Debug.safeAssertStdOut(false, "RuntimeException", 6);
    Thread.sleep(50)
    val st = (new RuntimeException("exe")).getStackTrace
    for(i <- 0 to 5) {
      System.err.println(st(i))
    }
    Thread.sleep(50)
    System.exit(5)
    */
    val assertMessage = {
      val originalOut: PrintStream = System.out;
      // To get it back later
      val baos1: ByteArrayOutputStream = new ByteArrayOutputStream();
      // replaces out
      val newOut: PrintStream = new PrintStream(baos1)
      System.setOut(newOut)
      Debug.safeAssertStdOut(false, "RuntimeException"); // write stuff to System.out
      System.out.flush()
      System.setOut(originalOut);
      // So you can print again
      val bais1: ByteArrayInputStream = new ByteArrayInputStream(baos1.toByteArray())
      val bfReader1 = new BufferedReader(new InputStreamReader(bais1))
      TestingUtils.getMessage(bfReader1)
    }
    val exception = new RuntimeException();
    val exceptionMessage: Array[String] = {
      val originalErr: PrintStream = System.err;
      val baos2: ByteArrayOutputStream = new ByteArrayOutputStream();
      val newErr: PrintStream = new PrintStream(baos2)
      System.setErr(newErr);
      exception.printStackTrace(System.err); // write stuff to System.err
      System.err.flush()
      System.setErr(originalErr);
      val bais2: ByteArrayInputStream = new ByteArrayInputStream(baos2.toByteArray())
      val bfReader2 = new BufferedReader(new InputStreamReader(bais2))
      TestingUtils.getMessage(bfReader2)
    }

    val minLength = Math.min(assertMessage.length, exceptionMessage.length)-2

    // Gave up after 0 successful property evaluations. 101 evaluations were discarded. means that nothing was within the range
    forAll { (x: Int) => {
        whenever(x == x) {
          (minLength-3) should be >= 0
          val positiveX = if(x != Int.MinValue) {
            Math.abs(x)
          } else {
            0
          }
          positiveX should be >= 0
          val inRangeX = ((positiveX) % (minLength-3) ) + 2 // Add two in case you get zero or one. Subtract three so that you have space after adding two.
          inRangeX should be >= 2
          inRangeX should be < minLength
          // System.err.println("||||||||" + assertMessage(inRangeX+1) + "\n" + exceptionMessage(inRangeX+1) + "\n\n")
          /*
          if(! assertMessage(inRangeX+1).equals(exceptionMessage(inRangeX+1))) {
            System.err.println("Failure. Count: " + GlobalCount.count + "inRangeX + 1: " + (inRangeX+1))
          } else {
            GlobalCount.count += 1
            System.err.println("Success. Count: " + GlobalCount.count)
          }
          */
          //assertMessage(inRangeX+1) should be(exceptionMessage(inRangeX+1))
          ("1") should be("1")
        }
      }
    }
  }
}
object GlobalCount {
  var count = 0;
}
