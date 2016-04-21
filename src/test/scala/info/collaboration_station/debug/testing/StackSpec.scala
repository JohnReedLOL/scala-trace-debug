package info.collaboration_station.debug.testing

import java.io._

import info.collaboration_station.debug._
import org.scalatest._
import org.slf4j._

// import info.collaboration_station.debug.testing.TestingUtils // this import comes from debugtrace/test/scala

class StackSpec extends FlatSpec {

  "Container printing" should "work for arrays, lists, and maps" in {
    val logger = LoggerFactory.getLogger("Logger");
    Debug.traceContents(List(1,2))
    Debug.traceContentsStdOut(Array("Hello","World"))
    Debug.traceErrOff_!()
    logger.warn( Debug.traceContents(Map("1" -> 1, "2" -> 2)) )
    Debug.enableEverything_!()
  }

  "Exceptions and assert" should "have matching stack traces" in {
    val assertMessage: Array[String] = {
      val originalOut: PrintStream = System.out;
      // To get it back later
      val baos1: ByteArrayOutputStream = new ByteArrayOutputStream();
      // replaces out
      val newOut: PrintStream = new PrintStream(baos1)
      System.setOut(newOut)
      Debug.assertNonFatalStdOut(false, "RuntimeException"); // write stuff to System.out
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

    val minLength = Math.min(assertMessage.length, exceptionMessage.length) - 2
    // same line for same stack trace
    val exceptionTrimmed = exceptionMessage.slice(0, exceptionMessage.length);
    val assertTrimmed = assertMessage.slice(1, assertMessage.length);
    for(i <- 0 until 6) { // minLength) {
      println("i: " + i)
      println("exception: " + exceptionTrimmed(i))
      println("assertion: " + assertTrimmed(i))
      assert(exceptionTrimmed(i+2).split(" ")(0) === assertTrimmed(i+2).split(" ")(0)) // first word is "at"
      assert(exceptionTrimmed(i+2).split(" ")(1) === assertTrimmed(i+2).split(" ")(1)) // second word is trace
      /*
      System.err.println(exceptionTrimmed(i))
      System.err.println(assertTrimmed(i))
      assert(exceptionTrimmed(i).split(" ")(0) === assertTrimmed(i).split(" ")(0)) // first word is "at"
      assert(exceptionTrimmed(i).split(" ")(1) === assertTrimmed(i).split(" ")(1)) // second word is trace
      */
    }
  }

  "Enabling trace to std err" should "allow tracing to std err" in {
    // "Hello  World before on".trace;
    Debug.traceErrOn_!
    // "Hello  World after on".trace;
    val traceMessage = {
      val originalErr: PrintStream = System.err;
      // To get it back later
      val baosErr: ByteArrayOutputStream = new ByteArrayOutputStream();
      // replaces standard error with new PrintStream
      val newErr: PrintStream = new PrintStream(baosErr)
      System.setErr(newErr)
      "Hello  World".trace; // write stuff to System.err
      System.err.flush()
      System.setErr(originalErr);
      // So you can print again
      val baisErr: ByteArrayInputStream = new ByteArrayInputStream(baosErr.toByteArray())
      val bfReaderErr = new BufferedReader(new InputStreamReader(baisErr))
      // (bfReaderErr == null).trace // must be false
      val message = TestingUtils.getMessage(bfReaderErr)
      // (message == null).trace // must be false
      val didTraceMessage_? : Boolean = (message.length > 0)
      assert(didTraceMessage_?)
    }
  }

  "Disabling trace to std err" should "disable tracing to std err" in {
    Debug.traceErrOff_!
    val traceMessage = {
      val originalErr: PrintStream = System.err;
      // To get it back later
      val baosErr: ByteArrayOutputStream = new ByteArrayOutputStream();
      // replaces standard error with new PrintStream
      val newErr: PrintStream = new PrintStream(baosErr)
      System.setErr(newErr)
      "Hello  World".trace; // write stuff to System.err
      System.err.flush()
      System.setErr(originalErr);
      // So you can print again
      val baisErr: ByteArrayInputStream = new ByteArrayInputStream(baosErr.toByteArray())
      val bfReaderErr = new BufferedReader(new InputStreamReader(baisErr))
      // (bfReaderErr == null).trace // must be false
      val message = TestingUtils.getMessage(bfReaderErr)
      // (message == null).trace // must be false
      assert(message.length == 0) // no message was obtained
    }
  }


  "Enabling trace to std out" should "allow tracing to std out" in {
    val traceMessage = {
      val originalOut: PrintStream = System.out;
      // To get it back later
      val baosOut: ByteArrayOutputStream = new ByteArrayOutputStream();
      // replaces standard error with new PrintStream
      val newOut: PrintStream = new PrintStream(baosOut)
      System.setOut(newOut)
      Debug.traceOutOn_!
      "Hello  World".traceStdOut; // write stuff to System.out
      System.out.flush()
      System.setOut(originalOut);
      // So you can print again
      val baisOut: ByteArrayInputStream = new ByteArrayInputStream(baosOut.toByteArray())
      val bfReaderOut = new BufferedReader(new InputStreamReader(baisOut))
      // (bfReaderOut == null).trace // must be false
      val message = TestingUtils.getMessage(bfReaderOut)
      // (message == null).trace // must be false
      val didTraceMessage_? : Boolean = (message.length > 0)
      assert(didTraceMessage_?)
    }
  }

  "Disabling trace to std out" should "disable tracing to std out" in {
    Debug.traceOutOff_!
    val traceMessage = {
      val originalOut: PrintStream = System.out;
      // To get it back later
      val baosOut: ByteArrayOutputStream = new ByteArrayOutputStream();
      // replaces standard error with new PrintStream
      val newOut: PrintStream = new PrintStream(baosOut)
      System.setOut(newOut)
      "Hello  World".traceStdOut; // write stuff to System.out
      System.out.flush()
      System.setOut(originalOut);
      // So you can print again
      val baisOut: ByteArrayInputStream = new ByteArrayInputStream(baosOut.toByteArray())
      val bfReaderOut = new BufferedReader(new InputStreamReader(baisOut))
      // (bfReaderOut == null).trace // must be false
      val message = TestingUtils.getMessage(bfReaderOut)
      // (message == null).trace // must be false
      assert(message.length == 0) // no message was obtained
    }
  }

  it should "not disable assert to standard out" in {
    Debug.nonFatalAssertOn_!() // enable assertion to standard out
    Debug.traceOutOff_!() // disable trace to standard out
    val traceMessage = {
      val originalOut: PrintStream = System.out;
      // To get it back later
      val baosOut: ByteArrayOutputStream = new ByteArrayOutputStream();
      // replaces standard error with new PrintStream
      val newOut: PrintStream = new PrintStream(baosOut)
      System.setOut(newOut)
      val assertString = "foo".assertNonFatalStdOut(_ equals "bar", "Error message"); // write stuff to System.out
      System.out.flush()
      System.setOut(originalOut);
      // So you can print again
      val baisOut: ByteArrayInputStream = new ByteArrayInputStream(baosOut.toByteArray())
      val bfReaderOut = new BufferedReader(new InputStreamReader(baisOut))
      // (bfReaderOut == null).trace // must be false
      val message = TestingUtils.getMessage(bfReaderOut)
      // (message == null).trace // must be false
      val didTraceMessage_? : Boolean = (message.length > 0)
      assert(didTraceMessage_?)
    }
  }
  "Disabling non-fatal assert" should "stop non-fatal assert from working" in {
    Debug.nonFatalAssertOff_!() // disable assertion to standard out
    val traceMessage = {
      val originalOut: PrintStream = System.out;
      // To get it back later
      val baosOut: ByteArrayOutputStream = new ByteArrayOutputStream();
      // replaces standard error with new PrintStream
      val newOut: PrintStream = new PrintStream(baosOut)
      System.setOut(newOut)
      "foo".assertNonFatalStdOut(_ equals "bar", "Error message"); // write stuff to System.out
      System.out.flush()
      System.setOut(originalOut);
      // So you can print again
      val baisOut: ByteArrayInputStream = new ByteArrayInputStream(baosOut.toByteArray())
      val bfReaderOut = new BufferedReader(new InputStreamReader(baisOut))
      // (bfReaderOut == null).trace // must be false
      val message = TestingUtils.getMessage(bfReaderOut)
      // (message == null).trace // must be false
      assert(message.length == 0)
    }
  }
  /*
  "output" should "look good" in {
    Debug.enableEverything_!()
    "Hello World 1".trace // 1 line of stack trace
    // "Hello World 2".traceStdOut
    "Hello World 3".trace(3) // 3 lines of stack trace
    Debug.trace("Hello World 4")
    Debug.trace("Hello World 5", 2) // 2 lines of stack trace
    "foo".assertNonFatalEquals("bar", "assertFailure1", maxLines = 2)
    "foo".assertEquals("foo", "assertFailure2")
    2.assert( _ + 1 == 3, "2 + 1 = 3")
    Debug.fatalAssertOff_!() // disables fatal assert
    "foo".assertEquals("bar", "message3") // assert cancelled
    assert(true)
  }
  */
  /*
    "An empty Set" should "should have size 0" in {
      assert(Set.empty.size == 0)
    }

    it should "produce NoSuchElementException when head is invoked" in {
      intercept[NoSuchElementException] {
        Set.empty.head
      }
    }

    "A Stack" should "pop values in last-in-first-out order" in {
      val stack = new Stack[Int]
      stack.push(1)
      stack.push(2)
      assert(stack.pop() === 2)
      assert(stack.pop() === 1)
    }

    ignore should "throw NoSuchElementException if an empty stack is popped" in {
      val emptyStack = new Stack[String]
      intercept[NoSuchElementException] {
        emptyStack.pop()
      }
    }
  */
}

// http://www.scalatest.org/user_guide/tagging_your_tests

/*
import org.scalatest.FunSuite

class SetSuite extends FunSuite {

  test("An empty Set should have size 0") {
    assert(Set.empty.size == 0)
  }

  test("Invoking head on an empty Set should produce NoSuchElementException") {
    intercept[NoSuchElementException] {
      Set.empty.head
    }
  }
}
*/