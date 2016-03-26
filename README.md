# scala-trace-debug
Make bug tracing and prevention easier than ever with scala-trace-debug. 

This debugging utility provides user-friendly prints, traces, fatal assertions, non-fatal assertions, and macro printing.

____________________________________________________________________________________________________________________

Example:

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

Output:

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

Getting started:

Just add these two lines to your "build.sbt" file:

```scala
resolvers += "johnreed2 bintray" at "http://dl.bintray.com/content/johnreed2/maven"

libraryDependencies += "scala-trace-debug" % "scala-trace-debug_2.11" % "0.1.1"
```

____________________________________________________________________________________________________________________
Cheat Sheet:

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

Dependencies:

- libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)

- Works best when run with an IDE that supports click-able stack traces. 

____________________________________________________________________________________________________________________

Instructions (for IntelliJ IDE):

- Add the jar file to your project (lib directory). 

- import info.collaboration_station.debug._ (implicit conversion) or info.collaboration_station.debug.Debug

- Go to: Run > Edit Configurations > Add New Configuration (green plus sign)

- Pick either "Application" (with a Main class) or "SBT Task" (run or test, usually)

- Click the green 'Run' (Shift+F10) or 'Debug' (Shift+F9) button and watch the stack traces in the console. 

- For best results, click 'Debug' as the stack traces will be optimized away in 'Run' mode. 
 
IntelliJ console has shortcut up and down arrows to navigate up and down the stack trace.

![IntelliJ console](http://s29.postimg.org/ud0knou1j/debug_Screenshot_Crop.png)

____________________________________________________________________________________________________________________

Benefits:

- Easy to locate print statements
- Convenient object oriented style syntax
- Easy to locate and remove trace statements (just Ctr-R find and replace)
- Customizable features including stack trace length and enabling/disabling of assertions and traces.
- Ability to print whole expressions (see "New features")

____________________________________________________________________________________________________________________

Requirements:

- Scala
- SBT
- Some sort of IDE that supports stack trace highlighing

____________________________________________________________________________________________________________________

More info:

See ScalaDoc in source code for in detail documentation.

See also: http://stackoverflow.com/questions/36194905/how-can-we-trace-expressions-print-statements-with-line-numbers-in-scala/36194986#36194986

____________________________________________________________________________________________________________________

New features:

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

![Example](http://i.imgur.com/2066Z0L.png)

____________________________________________________________________________________________________________________

Bugs:

To report or pinpoint bugs, email johnmichaelreedfas@gmail.com
