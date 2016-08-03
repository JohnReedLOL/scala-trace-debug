# scala-trace-debug [![Build Status](https://travis-ci.org/JohnReedLOL/scala-trace-debug.svg?branch=3.0)](https://travis-ci.org/JohnReedLOL/scala-trace-debug)
Helps you debug faster with clickable print statements and assertions.

____________________________________________________________________________________________________________________

Table of Contents

* <a href="#Locate-statements">Locate statements</a>
* <a href="#Getting-started">Getting started</a>
* <a href="#Scala-Examples">Scala Examples</a>
* <a href="#Java-Examples">Java Examples</a>
* <a href="#Requirements">Requirements</a>
* <a href="#Instructions">Instructions</a>
* <a href="#Performance">Performance</a>
* <a href="#Developers-Guide">Developer's Guide</a>

____________________________________________________________________________________________________________________


<a name="Locate-statements"></a>

### Locate statements:

![Append Position](http://i.imgur.com/W2EQdWG.png)

#####^ Just add "Pos" ^

"Pos does not rely on runtime reflection or stack inspection, and is done at compile-time using macros. This means that it is both orders of magnitude faster than e.g. getting file-name and line-numbers using stack inspection, and also works on Scala.js where reflection and stack inspection can't be used."

____________________________________________________________________________________________________________________

<a name="Getting-started"></a>

### Getting started:

- 1. Copy this into your SBT "build.sbt" file:

```scala
resolvers += "johnreed2 bintray" at "http://dl.bintray.com/content/johnreed2/maven"

libraryDependencies += "com.github.johnreedlol" %% "scala-trace-debug" % "3.0.4"
```

Note: If you get: "NoClassDefFoundError: scala/reflect/runtime/package ... Caused by: java.lang.ClassNotFoundException"

Add: `libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value`


- 2. [See this pom.xml](examplePom.xml):

```scala
<repository>
    <id>johnreed2 bintray</id>
    <url>http://dl.bintray.com/content/johnreed2/maven</url>
</repository>
...
<dependency>
  <groupId>com.github.johnreedlol</groupId>
  <artifactId>scala-trace-debug_2.11</artifactId>
  <version>3.0.3</version>
</dependency>
```

- 3. See the <a href="#Developers-Guide">Developer's Guide</a> to compile and jar.

Note that Java users need to add [this](http://mvnrepository.com/artifact/org.scala-lang/scala-library) dependency to the maven build.

____________________________________________________________________________________________________________________


### Scala Example:

![Scala Example](http://i.imgur.com/OsCzN7s.png)

##### Code Example:

```scala

def sleep() = Thread.sleep(60) // to prevent output mangling
import scala.trace.Pos
Pos.err("Standard error") ; sleep()
Pos.out("Hello World")
println("an error message" + Pos()) /*position*/ ; sleep()
```

##### Debug Statements (Java Compatible):

```scala
import scala.trace.Debug

// debug traces are clickable

Debug.err("trace to standard error")
Debug.err("trace to standard error", numLines = 2)
Debug.arrayErr(Array(1,2,3))
Debug.arrayErr(Array(1,2,3), numElements = 2,  numLines = 2) ; sleep()

Debug.out("trace to standard out")
Debug.out("trace to standard out", numLines = 2)
Debug.arrayOut(Array(1,2,3))
Debug.arrayOut(Array(1,2,3), start = 1, numLines = 2) ; sleep()
```

##### Assertions (Java Compatible):

```scala
// assertions are totally fatal (full stop)
Debug.assert(1 == 1, "one must equal one")
Debug.assertOut(1 == 1, "one must equal one") ; sleep()

// turn them off like this
Debug.fatalAssertOff()
Debug.assert(1 == 2, "one must equal two")   // nothing happens

// checks are non-fatal (no thread death)
Debug.check("one" == 2, "one must equal one") ; sleep()
// output is colored bright red for visibility
Debug.checkOut("one" == 2, "one must equal one", numLines = 1) ; sleep()
```

##### Macros:

```scala
import scala.trace.Macro

// Macro methods use advanced Scala features to print code and types

Macro.contentsOut(List(1, 2, 3))
Macro.contentsOut(List(1, 2, 3), numLines = 2) ; sleep()

Macro.contentsErr(List(1, 2, 3))
Macro.contentsErr(List(1, 2, 3), numLines = 2) ; sleep()

Macro.checkCode("one" == 2)
Macro.assertCode("one" == "one") ; sleep()

val (one, two, three) = (1, 2, 3)

// desugaring includes all the parenthesis

Macro.desugarOut(one + two / three)
Macro.codeOut(one + two / three) ; sleep()

Macro.desugarErr(one + two / three)
Macro.codeErr(one + two / three) ; sleep()
```

##### Implicit Conversions:

```scala

import scala.trace.implicitlyTraceable

// you can easily remove calls to ".out" and ".err" from the source by pressing Ctr-R (find-replace)

"foo bar baz".out
"foo bar baz".err ; sleep()

println("")

import scala.trace.implicitlyAssertable

"foo bar".assertEq("foo bar", "foo bar must equal foo bar")
2.check(_ + 3 == 5, "two plus three is five")
```

##### Output Formatting:

```scala
import scala.trace.Format

// Formatting makes the string more readable
println(Format.text("This is a really really really really really really" +
  "really really really really really really really really really really" +
  "really really really really really really really really" +
  "long string that needs to be formatted because it is longer than 100 chars default \n"))

import scala.trace.implicitlyFormatable

// Instead of using Format you can use the .wrap method
println(("This is a really,really,really,really,really,really,really,really,really,really,really," +
  ",really,really,really,really,really," +
  ",really,really,really,really,really,," +
  "long,string,that,needs,to,be,formatted,because,it,is,longer,than," + Format.getLineLength
  + ",chars").wrap(delimiter = ","))
```

##### Output:

```scala
an error message - my.pkg.Main.main(Main.scala:16)

"trace to standard error" in thread main:
  at my.pkg.Main$.main(Main.scala:22)

"trace to standard error" in thread main:
  at my.pkg.Main$.main(Main.scala:23)
  at my.pkg.Main.main(Main.scala)

" 1, 2, 3
" in thread main:
  at my.pkg.Main$.main(Main.scala:24)

" 1, 2
" in thread main:
  at my.pkg.Main$.main(Main.scala:25)
  at my.pkg.Main.main(Main.scala)
// ...

```

##### ^ [Run it yourself](src/test/scala/my/pkg/Main.scala) with "sbt test:run" ^

____________________________________________________________________________________________________________________

<a name="Java-Examples"></a>

### Java Examples:


![Java Screenshot](http://i.imgur.com/DQlI9Rx.png)

^ Note that all my stack traces are off by one. This only happens when the methods are called from Java.
To get around this, specify "2" for last parameter (2 lines of stack trace). ^

If you just want to copy-paste, Java example is [here](src/test/java/main/JavaMain.java).

____________________________________________________________________________________________________________________


### Master Shutoff Switch (Java Capable):

If you set the environment variable `ENABLE_TRACE_DEBUG` to `false`, it will disable all printing and assertions.
A system property may also be used. "The system property takes precedence over the environment variable". 

The preprocessor will also replace all calls to `Log.find` and `Pos` with an empty String at compile time.


### Runtime Switches (Java Capable):

```scala
Debug.traceErrOn/Off()
Debug.traceOutOn/Off()
Debug.fatalAssertOn/Off()
Debug.nonFatalAssertOn/Off() // assertNonFatal = check
Debug.setElementsPerRow() // For container printing
```

____________________________________________________________________________________________________________________

<a name="Requirements"></a>

### Requirements:

- Scala 2.10.4 or higher (or Java 7+)
- Any IDE or text editor that supports stack trace highlighting

____________________________________________________________________________________________________________________

<a name="Instructions"></a>

### Instructions (for IntelliJ IDE):

1. Add the library dependency

2. import [scala.trace._](src/main/scala/scala/trace/package.scala)

3. Make sure that you have IntelliJ run configuration set up to run from the IntelliJ console

![Example](http://i.imgur.com/UPZAJHo.png)

- Place some calls to scala trace debug and click the green 'Debug' (Shift+F9) button and follow the stack traces in the console. 
 
- Use the IntelliJ console arrows to navigate up and down the stack traces.

![IntelliJ console](http://s29.postimg.org/ud0knou1j/debug_Screenshot_Crop.png)

____________________________________________________________________________________________________________________

<a name="Performance"></a>

### Performance:

No overhead for no stack trace.

```scala
"foo".err(0) // no call to Thread.currentThread.getStackTrace()
```

____________________________________________________________________________________________________________________

### Code layout:

Currently all the actual printing is done in [`Printer.scala`](src/main/scala/scala/trace/internal/Printer.scala), all the implicit conversions are in [`package.scala`](src/main/scala/scala/trace/package.scala), and all the calls to the "Debug" object are in [`Debug.scala`](src/main/scala/scala/trace/Debug.scala)

____________________________________________________________________________________________________________________

### Developer's Guide

<a name="Developers-Guide"></a>

1. git clone https://github.com/JohnReedLOL/scala-trace-debug 3.0
2. cd ./3.0/
3. sbt test
4. sbt test:run [pick option 1 - it should fail with exit code 7 for fatal assertion]
5. sbt test:run [pick option 2]
6. sbt package

____________________________________________________________________________________________________________________

#### Links (Old):

See [ScalaDoc](http://ec2-52-87-157-20.compute-1.amazonaws.com/) in source code for in detail documentation.

See also: http://stackoverflow.com/questions/36194905/how-can-we-trace-expressions-print-statements-with-line-numbers-in-scala/36194986#36194986

[http://stackoverflow.com/questions/4272797/debugging-functional-code-in-scala/36287172#36287172](http://stackoverflow.com/questions/4272797/debugging-functional-code-in-scala/36287172#36287172)

Old version of this library: [https://www.reddit.com/r/scala/comments/4aeqvh/debug_trace_library_needs_users_review/](https://www.reddit.com/r/scala/comments/4aeqvh/debug_trace_library_needs_users_review/)

Less old version of this library: [https://www.reddit.com/r/scala/comments/4fap0r/making_debugging_easier/](https://www.reddit.com/r/scala/comments/4fap0r/making_debugging_easier/)
