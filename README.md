# scala-trace-debug
Make multithreaded print debugging easier than ever with scala trace debug. 

Provides human-friendly prints, traces, assertions, container printing, source code printing, and log output.

____________________________________________________________________________________________________________________

### Is this tool for me?

1. Do I use an IDE or a text editor with stack trace parsing?

2. Do I look around for where I put my print or log statements?
&nbsp;

![Append Position](http://i.imgur.com/4hvGQ0t.png)

^ Just add "Pos" ^
____________________________________________________________________________________________________________________

### Getting started:

- [Maven dependency](https://bintray.com/johnreed2/maven/scala-trace-debug/view):
- 
```scala
<dependency>
  <groupId>scala.trace</groupId>
  <artifactId>scala-trace-debug_2.11</artifactId>
  <version>2.2.14</version>
  <type>pom</type>
</dependency>
```

- SBT "build.sbt" file:

```scala
resolvers += "johnreed2 bintray" at "http://dl.bintray.com/content/johnreed2/maven"

libraryDependencies += "scala.trace" %% "scala-trace-debug" % "2.2.14"
```

- Jar file located in the [target](target) folder. 

Java users need to add [this](http://mvnrepository.com/artifact/org.scala-lang/scala-library) dependency to the maven build. Copy-pasting the jar works too.

All the functions that a Java user can call are [in here](http://johnreedlol.bitbucket.org/api/index.html#scala.trace.Debug$): http://johnreedlol.bitbucket.org/api/index.html#scala.trace.Debug$

____________________________________________________________________________________________________________________


### Java Examples:


![Java Screenshot](http://i.imgur.com/R4Kbpa9.png)

^ Note that all my stack traces are off by one. This only happens when the methods are called from Java.
To get around this, specify "2" for last parameter (2 lines of stack trace). ^

If you just want to copy-paste, Java example is [here](src/test/java/main/JavaMain.java).

____________________________________________________________________________________________________________________


### Master Shutoff Switch (Java Capable):

If you set the environment variable `ENABLE_TRACE_DEBUG` to `false`, it will disable all printing and assertions.
A system property may also be used. "The system property takes precedence over the environment variable". The preprocessor will also replace all calls to `Log.find` and `Pos` with an empty String at compile time.


### Runtime Switches (Java Capable):

```scala
Debug.traceErrOn/Off()
Debug.traceOutOn/Off()
Debug.fatalAssertOn/Off()
Debug.nonFatalAssertOn/Off() // assertNonFatal = check
Debug.setElementsPerRow() // For container printing
```

____________________________________________________________________________________________________________________

### Scala Examples:

#### Without logger:

![Demo](http://i.imgur.com/EFkBppw.png)

^ Note that this is an old screenshot. The import name was changed to `scala.trace`. ^

#### With logger:

![Logger](http://i.imgur.com/MNNkYXe.png)

^ The left side in parenthesis is the name of a variable; the right side (after "->") is the contents. ^

If you just want to copy-paste, Scala example is [here](src/test/scala/main/Main.scala).

____________________________________________________________________________________________________________________

### Requirements:

- Scala 2.10.4 or higher (or Java 8+)
- Any IDE or text editor that supports stack trace highlighting

____________________________________________________________________________________________________________________

### Instructions (for IntelliJ IDE):

1. Add the library dependency or grab the jar file from the [target/scala-2.11](target/scala-2.11) folder.

2. import [scala.trace._](src/main/scala/scala/trace/package.scala)

3. Place some calls to scala trace debug and click the green 'Debug' (Shift+F9) button and follow the stack traces in the console. 
 
4. Use the IntelliJ console arrows to navigate up and down the stack traces.

![IntelliJ console](http://s29.postimg.org/ud0knou1j/debug_Screenshot_Crop.png)

____________________________________________________________________________________________________________________

### Logger Incorporation:

[Log.find](http://johnreedlol.bitbucket.org/api/index.html#scala.trace.Log$) is designed to be used with a logger. Does not incur the overhead of a full stack trace.

[Debug methods](http://johnreedlol.bitbucket.org/api/index.html#scala.trace.Debug$) can be called from Java code and without a logger. All calls to `Debug.trace`, `Debug.assert`, etc. return a String that can be passed into a logger. 

[SDebug](http://johnreedlol.bitbucket.org/api/index.html#scala.trace.SDebug$) stands for "Scala Debug". It provides special debug methods that are only available in Scala (macros, source code printing, etc).

You can disable printing to standard out and standard error via `Debug.disableEverything_!`. `Debug` methods will still return a String that you can pass into a logger. 

____________________________________________________________________________________________________________________


### Container Printing:

![ContainerExample](http://i.imgur.com/P8mlz0C.png)

^ Note the jar file name, `scalatest_2.11`, in the stack trace. ^

^ Container printing works for any Scala container. To pass in Java containers, [import scala.collection.JavaConversions._](http://stackoverflow.com/questions/9638492/conversion-of-scala-map-containing-boolean-to-java-map-containing-java-lang-bool) ^

____________________________________________________________________________________________________________________

### Cheat Sheet / Examples:

[Methods available through implicit conversion](http://ec2-52-87-157-20.compute-1.amazonaws.com/#info.collaboration_station.debug.package$$ImplicitTrace)

Example functions (old): http://pastebin.com/2e1JN1De

^ For more examples, see [Main.scala](src/test/scala/main/Main.scala), which you can run with `sbt test:run`

____________________________________________________________________________________________________________________

### Method Chaining:

Add-on methods available through implicit conversion return the object they were called upon so that you can use them inside an expression or chain them together.

```scala

import scala.trace.implicitlyTraceable
...
val foo = true
if( foo.trace ) { ... }

import scala.trace.implicitlyPrintable
...
val foobar = "foo".trace().concat("bar").println() // Chaining.

```

____________________________________________________________________________________________________________________

### More features:

#### _Desugared macro expression tracing:_

![Example](http://i.imgur.com/LvB8lOd.png)

######^ Useful if you have a line like "object method object param" and you can't find where the dot and parenthesis go ^

^ Note that this is an old screenshot. These macro methods now use `SDebug` (Scala Debug) instead of `Debug`. ^

#### _Code tracing and assertions:_

![Example2](http://i.imgur.com/pdey7Jk.png)

######^ Useful if you do not want to repeat the name of a variable in a print statement. ^

^ Note that this is an old screenshot. These macro methods now use `SDebug` (Scala Debug) instead of `Debug`. ^

____________________________________________________________________________________________________________________

### Use in practice:

For use in practice, see [this link](USE_WITH_IDE.md)

- To only add prints, `import scala.trace.implicitlyPrintable`
- To only add traces, `import scala.trace.implicitlyTraceable`
- If only add asserts, `import scala.trace.implicitlyAssertable`
- To add prints, traces, and asserts, `import scala.trace._`

____________________________________________________________________________________________________________________

### Performance:

No overhead for no stack trace.

```scala
"foo".trace(0) // no call to Thread.currentThread.getStackTrace()
```

Note that calls to `Log.find` are faster than calls to `Debug.trace`, but `Log.find` is limited to one line.

____________________________________________________________________________________________________________________

#### Code layout:

Currently all the actual printing is done in [`Printer.scala`](src/main/scala/scala/trace/internal/Printer.scala), all the implicit conversions are in [`package.scala`](src/main/scala/scala/trace/package.scala), and all the calls to the "Debug" object are in [`Debug.scala`](src/main/scala/scala/trace/Debug.scala)

____________________________________________________________________________________________________________________

#### Links (Old):

See [ScalaDoc](http://ec2-52-87-157-20.compute-1.amazonaws.com/) in source code for in detail documentation.

See also: http://stackoverflow.com/questions/36194905/how-can-we-trace-expressions-print-statements-with-line-numbers-in-scala/36194986#36194986

[http://stackoverflow.com/questions/4272797/debugging-functional-code-in-scala/36287172#36287172](http://stackoverflow.com/questions/4272797/debugging-functional-code-in-scala/36287172#36287172)

Old version of this library: [https://www.reddit.com/r/scala/comments/4aeqvh/debug_trace_library_needs_users_review/](https://www.reddit.com/r/scala/comments/4aeqvh/debug_trace_library_needs_users_review/)

Less old version of this library: [https://www.reddit.com/r/scala/comments/4fap0r/making_debugging_easier/](https://www.reddit.com/r/scala/comments/4fap0r/making_debugging_easier/)
