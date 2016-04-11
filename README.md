# scala-trace-debug
Make multithreaded bug tracing and prevention easier than ever with scala trace debug. 

Provides user-friendly prints, traces, assertions, container printing, and source code printing.

____________________________________________________________________________________________________________________

**Example:**

![Demo](http://s9.postimg.org/ssuso8f4f/Example_Screenshot_Highlight.png)

____________________________________________________________________________________________________________________

**Requirements:**

- Scala 2.10.4 or higher
- Some sort of IDE that supports stack trace highlighting

____________________________________________________________________________________________________________________

**Getting started:**

Just add these two lines to your "build.sbt" file:

```scala
resolvers += "johnreed2 bintray" at "http://dl.bintray.com/content/johnreed2/maven"

libraryDependencies += "scala-trace-debug" %% "scala-trace-debug" % "0.1.8"
```

____________________________________________________________________________________________________________________

**Logger Incorporation:**

All calls to `Debug.trace`, `Debug.assert`, etc. return a String that can be passed into a logger. 

You can disable printing to standard out and standard error via `Debug.disableEverything_!` This will still return a String that you can pass into a logger. 

____________________________________________________________________________________________________________________

**Container Printing:**

![ContainerExample](http://i.imgur.com/IMk1CnM.png)

^ A String containing the stack trace is obtained on line 33 and passed into println on line 34, giving you `List[Int] 1 2 3`. The number of elements printed is adjustable. ^

^ Works for any Scala container. To pass in Java containers, you can [import scala.collection.JavaConversions._](http://stackoverflow.com/questions/9638492/conversion-of-scala-map-containing-boolean-to-java-map-containing-java-lang-bool) ^

____________________________________________________________________________________________________________________

**Cheat Sheet / Examples:**

[Methods available through implicit conversion](http://ec2-52-87-157-20.compute-1.amazonaws.com/#info.collaboration_station.debug.package$$ImplicitTrace)

[Methods available through the Debug object](http://ec2-52-87-157-20.compute-1.amazonaws.com/#info.collaboration_station.debug.Debug$)

Example functions: http://pastebin.com/2e1JN1De

^ For more examples, see "src/test/scala/main/Main.scala"

Note: Fatal assertions kill the application with exit code 7. Non-fatal assertions never terminate any part of the application, not even the currently running thread. To terminate only the currectly running thread, use an exception.

____________________________________________________________________________________________________________________

**Chaining:**

All the add-on methods available through implicit conversion return the object they were called upon so that you can use them inside an expression or chain them together.

```scala

import info.collaboration_station.debug._ // wildcard import for implicit conversion
...
val foo = true
if( foo.trace ) { /* Do something with foo */ }

val foobar = "foo".trace.concat("bar").println() // Chaining. First print "foo", then print "foobar"

```

____________________________________________________________________________________________________________________


**Instructions (for IntelliJ IDE):**

1. Add the library dependency (in sbt) or grab the jar file from the "target/scala-2.11" folder.

2. import info.collaboration_station.debug._ (implicit conversion) or info.collaboration_station.debug.Debug (static methods)

3. Go to: Run > Edit Configurations > Add New Configuration (green plus sign).

4. Pick either "Application" (with a Main class) or "SBT Task" ("run", "test", or "test:run").

5. Position the stack traces and asserts in the line of likely sources of bugs.

6. Click the green 'Debug' (Shift+F9) button and follow the stack traces in the console. 
 
7. Use the IntelliJ console arrows to navigate up and down the stack traces.

![IntelliJ console](http://s29.postimg.org/ud0knou1j/debug_Screenshot_Crop.png)

____________________________________________________________________________________________________________________

**New features:**

Now featuring desugared macro expressions and code tracing:

**_Desugared macro expression tracing:_**

![Example](http://i.imgur.com/D1jLiaa.png)

**_Code tracing and assertions:_**

![Example2](http://i.imgur.com/pdey7Jk.png)

____________________________________________________________________________________________________________________

**Benefits:**

- Easy to locate print statements. Gives you an idea of what each thread is doing.
- Convenient object oriented style syntax facilitates chaining. Static methods usable with logger.
- Easy to locate and remove trace statements (just Ctr-R find and replace)
- Customizable features including stack trace length and enabling/disabling of assertions and traces.

____________________________________________________________________________________________________________________

**Use in practice:**

For use in practice, see [this link](http://pastebin.com/GSjxYQ70)

____________________________________________________________________________________________________________________

**Performance:**

No overhead for no stack trace. 

```scala
"foo".trace(0) // no call to Thread.currentThread.getStackTrace()
```
____________________________________________________________________________________________________________________

**More info:**

See [ScalaDoc](http://ec2-52-87-157-20.compute-1.amazonaws.com/) in source code for in detail documentation.

See also: http://stackoverflow.com/questions/36194905/how-can-we-trace-expressions-print-statements-with-line-numbers-in-scala/36194986#36194986

[http://stackoverflow.com/questions/4272797/debugging-functional-code-in-scala/36287172#36287172](https://www.reddit.com/r/scala/comments/4aeqvh/debug_trace_library_needs_users_review/)

[https://www.reddit.com/r/scala/comments/4aeqvh/debug_trace_library_needs_users_review/](https://www.reddit.com/r/scala/comments/4aeqvh/debug_trace_library_needs_users_review/)

____________________________________________________________________________________________________________________

**Code layout:**

Currently all the actual printing is done in *info.collaboration_station.debug.ImplicitTraceObject*, all the "add-on" methods are in *info.collaboration_station.debug.ImplicitTrace*, and all the calls to the "Debug" object are in 
*info.collaboration_station.debug.Debug*
