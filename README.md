# scala-trace-debug
Make bug tracing and prevention easier than ever with scala-trace-debug. 

This debugging utility provides user-friendly prints, traces, fatal assertions, non-fatal assertions, and macro printing.

____________________________________________________________________________________________________________________

**Example:**

```scala
import info.collaboration_station.debug._

object Main {
  def main(args: Array[String]) {
    Debug.enableEverything_!()
    
    "Hello World 1".trace // 1 line of stack trace
    "Hello World 3".trace(3) // 3 lines of stack trace
    
    Debug.fatalAssertOff_!() // disables fatal assert
    "foo".assertEquals("bar", "message3") // fatal assert cancelled
  }
}
```

*Output:*

```scala
	"Hello World 1" in thread main:
		at path.to.main(Main.scala:22)

	"Hello World 3" in thread main:
		path.to.main(Main.scala:22)
		path.to.file1(Main.scala:33)
		path.to.file2(Main.scala:44)
```

![Demo](http://s9.postimg.org/ssuso8f4f/Example_Screenshot_Highlight.png)

____________________________________________________________________________________________________________________

**Getting started:**

Just add these two lines to your "build.sbt" file:

```scala
resolvers += "johnreed2 bintray" at "http://dl.bintray.com/content/johnreed2/maven"

libraryDependencies += "scala-trace-debug" %% "scala-trace-debug" % "0.1.2"
```

____________________________________________________________________________________________________________________
**Cheat Sheet:**

Methods availiable through implicit conversion:

```scala

import info.collaboration_station.debug._

// tracing methods:
"foo".trace
"foo".trace(5) // 5 lines of stack trace
"foo".traceStdOut
"foo".traceStdOut(5)

// fatal assertions:
"foo".assert( _ equals "foo", "Assert failed")
"foo".assertEquals("foo", "Assert failed")
"foo".assertStdOut( _ equals "foo", "Assert failed")
"foo".assertEqualsStdOut("foo", "Assert failed")

// non-fatal assertions:
"foo".assertNonFatal( _ equals "foo", "Assert failed")
"foo".assertNonFatalEquals("foo", "Assert failed")
"foo".assertNonFatalStdOut( _ equals "foo", "Assert failed")
"foo".assertNonFatalEqualsStdOut("foo", "Assert failed")

// print methods:
"foo".print
"foo".println
"foo".printStdErr
"foo".printlnStdErr

```

Methods availiable through Debug object:

```scala

import info.collaboration_station.debug.Debug

// tracing methods:
Debug.trace("foo")
Debug.trace("foo", 5)
Debug.traceStack("foo") // entire stack trace
Debug.traceStdOut("foo")
Debug.traceStdOut("foo", 5)
Debug.traceStackStdOut("foo")

// fatal assertions:
Debug.assert("foo" equals "foo", "Assert failed")
Debug.assertStdOut("foo" equals "foo", "Assert failed")

// non-fatal assertions:
Debug.assertNonFatal("foo" equals "foo", "Assert failed")
Debug.assertNonFatalStdOut("foo" equals "foo", "Assert failed")

// macro expression prints to standard error
Debug.traceExpression{val one = 1; one + 2 == 3} 
Debug.traceExpression({val one = 1; one + 2 == 3}, 4) // 4 lines of stack trace
Debug.traceStack{val one = 1; one + 2 == 3} // entire stack trace

// macro expression prints to standard out
Debug.traceStdOutExpression{val one = 1; one + 2 == 3} 
Debug.traceStdOutExpression({val one = 1; one + 2 == 3}, 4) // 4 lines of stack trace
Debug.traceStdOutStack{val one = 1; one + 2 == 3} // entire stack trace

// macro expression asserts
Debug.assertExpression{val one = 1; one + 2 == 3}
Debug.assertExpression({val one = 1; one + 2 == 3}, 4)
Debug.assertNonFatalExpression{val one = 1; one + 2 == 3}
Debug.assertNonFatalExpression({val one = 1; one + 2 == 3}, 4) 

```

Switches availiable through Debug object:

```scala

import info.collaboration_station.debug.Debug

// Enable/disable tracing methods:
Debug.traceOutOn_!
Debug.traceOutOff_!
Debug.traceErrOn_!
Debug.traceErrOff_!

// Enable/disable assert and assertStdOut methods:
Debug.fatalAssertOn_!
Debug.fatalAssertOff_!
Debug.nonFatalAssertOn_!
Debug.nonFatalAssertOff_!

// Enable/disable everything:
Debug.enableEverything_!
Debug.disableEverything_!

```

Fatal assertions kill the application with exit code 7. Non-fatal assertions never terminate any part of the application, not even the currently running thread. To terminate only the currectly running thread, please use an exception.

____________________________________________________________________________________________________________________

**Requirements:**

- Scala 2.11
- SBT
- Some sort of IDE that supports stack trace highlighing

____________________________________________________________________________________________________________________

**Instructions (for IntelliJ IDE):**

1. Add the library dependency (sbt).

2. import info.collaboration_station.debug._ (implicit conversion) or info.collaboration_station.debug.Debug

2. Go to: Run > Edit Configurations > Add New Configuration (green plus sign).

3. Pick either "Application" (with a Main class) or "SBT Task" (run or test, usually).

4. Position the stack traces and asserts in the line of likely sources of bugs.

5. Click the green 'Debug' (Shift+F9) button and follow the stack traces in the console. 
 
IntelliJ console has shortcut up and down arrows to navigate up and down the stack trace.

![IntelliJ console](http://s29.postimg.org/ud0knou1j/debug_Screenshot_Crop.png)

____________________________________________________________________________________________________________________

**Benefits:**

- Easy to locate print statements
- Convenient object oriented style syntax
- Easy to locate and remove trace statements (just Ctr-R find and replace)
- Customizable features including stack trace length and enabling/disabling of assertions and traces.
- Ability to print whole expressions (see "New features")

____________________________________________________________________________________________________________________


**More info:**

See ScalaDoc in source code for in detail documentation.

See also: http://stackoverflow.com/questions/36194905/how-can-we-trace-expressions-print-statements-with-line-numbers-in-scala/36194986#36194986

____________________________________________________________________________________________________________________

**New features:**

Now featuring macro expressions:

```scala

    Debug.traceExpression{ // print out the (syntactically de-sugared) expression
                          val myVal = 4; 
                          1 + 2 + myVal }
                          
    Debug.assertExpression{ // print out the expression if the expression return false
                           val someVal = 2; 
                           1 + someVal == 4 }

```

Example:

![Example](http://i.imgur.com/D1jLiaa.png)

____________________________________________________________________________________________________________________

**Use in practice:**

Once upon a time, I had a very frustrating bug. It was caused by an idiosyncrasy in the semantics of a copy constructor that caused it to do a shallow copy instead of a deep copy and the code was multithreaded and required a deep copy.

This bug frustrated me because even though I looked at that very line of code from where the bug originated, I did not see the bug. It was so subtle.

How would I deal with such a subtle bug? I am not 100% sure how you would do it, but since I wrote this tool, let me explain to you how I would do it.

First thing, you find the exact commit where this bug began to happen. Someone reported it and you have a way to check for it and you do "git bisect" and do a bisection search until you find the commit from whence that bug originated.

Now that you know this bug originated from commit 9da581d910c9c4ac935570123456789abcef, you can see the changes that were made in that commit. Let's say that is not enough information for you to figure out the bug.

Then you can start tracing the execution in the vicinity of where changes were made. Let's say there was a "hot swap" or "hot replace" feature in the debugger that allowed you to "modify and re-compile code (inside methods/blocks) in debug mode and to have these changes visible and taken into account by the debugged VM". Let's use that feature.

If this "hot swap" feature works, we should be able to insert calls to scala-trace-debug inside our code and scroll through the stack traces. We sprinkle some breakpoints in the vicinity of the bug and maybe add a few "trace" or even "assertNonFatal" calls while we do our debugging.

By doing this, we should have an idea of the flow of execution of a given thread and we should be able to verify if this and this assumption is true or false. In addition to being able to trace variables and print de-sugared expressions, we can supplement the information provided by scala-trace-debug with the information provided by the debugger.

By combining the information about the flow of execution with the values displayed, we should be able to infer what is going on. We can make assumptions about what is true in the source code and turn those assumptions into assertions and even into unit tests.

Given all this information: the commit history, the documentation in the source code (or the self-documenting source code if no Javadoc is available), the value of variables, the flow of execution, and knowledge as to what assertions are true and what are not, we should be able to understand what is going on and figure out what to do to fix any violated assertions or failed unit tests.

Assuming that we can do that, the bug should be fixable.

____________________________________________________________________________________________________________________


**Bugs:**

To report or pinpoint bugs, email johnmichaelreedfas@gmail.com
