# scala-trace-debug
Make bug tracing and prevention easier than ever with scala trace debug. 

This debugging utility provides user-friendly prints, traces, fatal assertions, non-fatal assertions, container printing, and code printing.

____________________________________________________________________________________________________________________

**Example:**

![Demo](http://s9.postimg.org/ssuso8f4f/Example_Screenshot_Highlight.png)

____________________________________________________________________________________________________________________

**Getting started:**

Just add these two lines to your "build.sbt" file:

```scala
resolvers += "johnreed2 bintray" at "http://dl.bintray.com/content/johnreed2/maven"

libraryDependencies += "scala-trace-debug" %% "scala-trace-debug" % "0.1.6"
```

____________________________________________________________________________________________________________________

**Logger incorporation:**

Due to demand for the ability to integrate this tool with a logger, all calls to Debug.trace, Debug.assert, etc. now return a String that can be passed into a logger. 

To use this feature, try "libraryDependencies += "scala-trace-debug" %% "scala-trace-debug" % "0.1.4" (version 0.1.4 or above)

You can disable printing to standard out and standard error via Debug.disableEverything_! This will still return a String that you can pass into a logger. Note that Debug.nonFatalAssertOff_! only prevents non-fatal assertions from printing - they still return a String containing what they would have printed if they were on (just like Debug.trace does when you do Debug.traceErrOff_!).

In addition, all the add-on methods available through implicit conversion still return the object they were called upon so that you can use them inside an expression.

Example:

```scala

import info.collaboration_station.debug._ // wildcard import for implicit conversion
...
val foo = true
if( foo.trace ) { /* Do something with foo */ }

```

____________________________________________________________________________________________________________________

**Performance:**

No overhead for no stack trace. 

```scala
"foo".trace(0) // no call to Thread.currentThread.getStackTrace()
```

____________________________________________________________________________________________________________________

**Container Printing:**

![ContainerExample](http://i.imgur.com/IMk1CnM.png)

____________________________________________________________________________________________________________________
**Cheat Sheet:**

[Methods available through implicit conversion](http://collaboration-station.info/debug/index.html#info.collaboration_station.debug.package$$ImplicitTrace):

[Methods available through Debug object](http://collaboration-station.info/debug/index.html#info.collaboration_station.debug.Debug$):

Example functions: http://pastebin.com/2e1JN1De

Note: Fatal assertions kill the application with exit code 7. Non-fatal assertions never terminate any part of the application, not even the currently running thread. To terminate only the currectly running thread, please use an exception.

____________________________________________________________________________________________________________________

**Requirements:**

- Scala 2.11
- SBT
- Some sort of IDE that supports stack trace highlighting

____________________________________________________________________________________________________________________

**Instructions (for IntelliJ IDE):**

1. Add the library dependency (in sbt) or grab the jar file from the "scala-trace-debug/target/scala-2.11" folder.

2. import info.collaboration_station.debug._ (implicit conversion) or info.collaboration_station.debug.Debug

3. Go to: Run > Edit Configurations > Add New Configuration (green plus sign).

4. Pick either "Application" (with a Main class) or "SBT Task" ("run", "test", or "test:run").

5. Position the stack traces and asserts in the line of likely sources of bugs.

6. Click the green 'Debug' (Shift+F9) button and follow the stack traces in the console. 
 
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

[http://stackoverflow.com/questions/4272797/debugging-functional-code-in-scala/36287172#36287172](https://www.reddit.com/r/scala/comments/4aeqvh/debug_trace_library_needs_users_review/)

[https://www.reddit.com/r/scala/comments/4aeqvh/debug_trace_library_needs_users_review/](https://www.reddit.com/r/scala/comments/4aeqvh/debug_trace_library_needs_users_review/)


____________________________________________________________________________________________________________________

**New features:**

Now featuring desugared macro expressions and code tracing:

Desugared macro expression:

![Example](http://i.imgur.com/D1jLiaa.png)

Code tracing:

![Example2](http://i.imgur.com/pdey7Jk.png)

____________________________________________________________________________________________________________________

**Use in practice:**

For a debugging story, see [this link](http://pastebin.com/GSjxYQ70)

____________________________________________________________________________________________________________________


**Bugs:**

To report or pinpoint bugs, email johnmichaelreedfas@gmail.com

____________________________________________________________________________________________________________________

**Features:**

If you want to implement a new feature, just ask. Currently all the actual printing is done in `info.collaboration_station.debug.ImplicitTraceObject`, all the "add-on" methods are in 
`info.collaboration_station.debug.ImplicitTrace`, and all the calls to the "Debug" object are in 
`info.collaboration_station.debug.Debug`

____________________________________________________________________________________________________________________


**Design decisions:**

I made a few design decisions that you might not agree with. "trace" is the default and "traceStdOut" requires six extra characters to type. This is because if you trace to standard out and you get an exception in standard error, the text will be garbled in the console. 

The function names are long. I could have named them something shorter, but the presumption is that if you are using this tool, you have an IDE which can auto-complete long function names.

traceExpression is listed before traceCode. This is because traceCode has a few bugs. For example, trailing functions like (4).toString sometimes get cut off. With traceCode, you want to stick to Object.something(). 

Fatal assertions are the default and if you want non-fatal you have to type eight extra characters. That was probably a mistake.

traceContainer requires an implicit conversion (import collection.JavaConversions._ ) to work with java containers.