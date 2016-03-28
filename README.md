# scala-trace-debug
Make bug tracing and prevention easier than ever with scala-trace-debug. 

This debugging utility provides user-friendly prints, traces, fatal assertions, non-fatal assertions, and macro printing.

____________________________________________________________________________________________________________________

**Example:**

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

[Methods available through implicit conversion](http://collaboration-station.info/debug/index.html#info.collaboration_station.debug.package$$ImplicitTrace):

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

[Methods available through Debug object](http://collaboration-station.info/debug/index.html#info.collaboration_station.debug.Debug$):

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

Switches available through Debug object:

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

  Once upon a time, I had a very frustrating bug. It was caused by an 
idiosyncrasy in the semantics of a copy constructor that caused it to do 
a shallow copy instead of a deep copy and the code was multithreaded and 
required a deep copy.

  This bug frustrated me because even though I looked at that very line of 
code from where the bug originated, I did not see the bug. It was so 
subtle.

  How would I deal with such a subtle bug? I am not 100% sure how you would 
do it, but since I wrote this tool, let me explain to you how I would do 
it.

1. Do git bisect to find the commit that produced the bug.

2. Read the lines of code that were changed.

3. Put break points in the area of what was changed.

4. Follow [this IntelliJ doc](https://www.jetbrains.com/help/idea/2016.1/reloading-classes.html?origin=old_help) to enable hot reloading of source code while debugging. Scala IDE has similar ["hot code replace"](http://scala-ide.org/docs/current-user-doc/features/scaladebugger/index.html) functionality. Basically, modify the file while in "debug" mode, click "Build > Compile File.scala", reload the class, drop the obsolete stack frame, and debug as usual.

Note: "Hot code replace adds the possibility to modify and re-compile (method body) code in a debug mode and to have these changes visible and taken into account by the debugged VM without restarting the application."

Hot code replace will cause your current stack frame to become obsolete and the JVM does not like obsolete frames. For hot code replace to work correctly, you must drop the obsolete frame by right clicking "Drop Frame" on the obsolete frame and then click "Step Over" (F8).

![Drop the obsolete stack frame](http://s29.postimg.org/qyox1zyif/obsolete_frame.png)

5. While stepping through the code, make or add calls to scala-trace-debug. 

6. Use stack traces to trace an execution path through the source code as you use the debugger to look at values in the source code.

7. Make calls to nonFatalAssert to verify any assumptions you may have about the source code or insert calls to the regular fatal assert to prove that something you believe to be true is in fact true. Note that often times these assertions make good unit tests.

8. If you see a line of code that looks like "object method object method object method parameter" and you get confused by the whitespace, use traceExpression to de-sugar the expression.

9. Don't forget to read the javadoc or self documentation in the code.

  Given all this information: the commit history, the documentation in the 
source code (or the self-documenting source code if no Javadoc is 
available), the value of variables, the flow of execution, and knowledge 
as to what assertions are true and what are not, we should be able to 
understand what is going on and figure out what to do to fix any violated 
assertions or failed unit tests. 

  p.s. Calls to scala-debug-trace are not meant to be left inside 
production code. `git reset --hard HEAD~1` should allow you to discards 
uncommitted changes.

  Side note: It is a personal pet peeve of mine to see threads with names 
like "1128471". If the code is creating a new thread, the name of the 
thread can double as a form of documentation. Example: "Database_Thread", 
"GUI_Thread", "Socket_Thread". To change the name of a thread pool, 
see [this link](http://stackoverflow.com/questions/6113746/naming-threads-and-thread-pools-of-executorservice)

____________________________________________________________________________________________________________________


**Bugs:**

To report or pinpoint bugs, email johnmichaelreedfas@gmail.com
