Change log:

"0.1.4" - Debug statements return strings which you can pass into a logger.

"0.1.5" - Performance improvement - printing a stack trace of length 0 makes no call to Thread.currentThread().getStackTrace.

"0.1.6" - Added traceCode, assertCode as an alternative to traceExpression, assertExpression.

"0.1.7" - Added traceContents for List, Map, etc.

"0.1.8" - Backwards compatibility to scalaVersions "2.10.4" (exception during macro expansion occurs in traceExpression and traceCode in 2.10.4).

"0.1.9" - Reformatted to allow separate imports for prints, traces, and asserts.

"0.2.0" - Added master shutoff switch. (foo).assertEqual(bar) can take two different types.

"0.2.1" - Fixed exception during macro expansion in traceExpression and traceCode in 2.10.4
        - Added jar file names to stacktrace in assertions. Ex. [scalatest_2.11-2.2.6.jar]
        - Enhanced container printing. Example: Debug.traceContents(List(0, 1, 2, 3, 4, 5), start = 2, numElements = 3) -> "List[Int] 2 3 4"

"0.2.2" - Added jar file name to traces as well.

"0.2.3" - Added logger integration, "find" method

"0.2.4" - Added package names from LogBack. See (/home/johnreed/sbtProjects/debug/experimental/experimental/src/main/java/info/collaboration_station/debug/internal/PackagingDataCalculator.java)

"0.2.5" - Windows file names (\\)

"0.2.6" - lost version #

"0.2.7" - Log.find(foo, 2) was supposed to print 2 elements of foo

"0.2.8" - Log.pos

"0.2.9" - Added jar file name to trace/assert statements

"1.2.9" - Breaking change - changed "assertNonFatal" into "check"

"1.2.10" - Log.find takes in Java arrays in addition to Scala data structures

Not yet released

"2.2.10" - Java compatibility (with Java test cases), shortened import name, Debug.assert uses bright color in "assert failed" messages

"2.2.15" - Added format functionality

"2.2.16" - shortened ".formatted" to ".wrap"

"2.2.17" - Set Java version to java-1.7.0-openjdk-amd64

Final publish info:

[info] Main Scala API documentation successful.
[info] Packaging /home/johnreed/sbtProjects/debug/experimental/experimental/target/scala-2.11/scala-trace-debug_2.11-2.2.17-javadoc.jar ...
[info] Done packaging.
[info]  published scala-trace-debug_2.11 to https://api.bintray.com/maven/johnreed2/maven/maven/scala/trace/scala-trace-debug_2.11/2.2.17/scala-trace-debug_2.11-2.2.17.pom
[info]  published scala-trace-debug_2.11 to https://api.bintray.com/maven/johnreed2/maven/maven/scala/trace/scala-trace-debug_2.11/2.2.17/scala-trace-debug_2.11-2.2.17.jar
[info]  published scala-trace-debug_2.11 to https://api.bintray.com/maven/johnreed2/maven/maven/scala/trace/scala-trace-debug_2.11/2.2.17/scala-trace-debug_2.11-2.2.17-sources.jar
[info]  published scala-trace-debug_2.11 to https://api.bintray.com/maven/johnreed2/maven/maven/scala/trace/scala-trace-debug_2.11/2.2.17/scala-trace-debug_2.11-2.2.17-javadoc.jar
[info] johnreed2/scala-trace-debug@2.2.17 was released
[success] Total time: 19 s, completed Jun 12, 2016 12:54:28 AM
[info] Setting version to 2.11.7
[info] Reapplying settings...
[info] Set current project to scala-trace-debug (in build file:/home/johnreed/sbtProjects/debug/experimental/experimental/)

Other stuff (to be done)

test case for code style, test for overflow - too long messages.

cleaner API, cleaner test cases/examples, tryout with Maven