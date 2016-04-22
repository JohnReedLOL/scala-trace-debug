Debugging example for https://github.com/JohnReedLOL/scala-trace-debug

____________________________________________________________
 
  Once upon a time, I had a very frustrating bug. It was caused by an
idiosyncrasy in the semantics of a copy constructor that caused it to do
a shallow copy instead of a deep copy in multithreaded code.
 
  How would I deal with such a subtle bug? Well since this tool was explicitly designed for this sort of a thing, here's one way.
 
1. Do git bisect to find the commit that produced the bug (or if a unit test caught a bug you can just put breakpoints in the unit test).
 
2. Read the lines of code that were changed.
 
3. Put break points in the area of what was changed. If your code has multiple threads that interact with one another, consider putting calls to scala trace debug in different threads. All printing in scala trace debug is done through an internal object called ImplicitTraceObject with a lock called PrintLock, so it should be thread safe.
 
4. "Hot swap" in some calls to scala-trace-debug during the debugging process to gather more information.
 
5. Follow [this IntelliJ doc](https://www.jetbrains.com/help/idea/2016.1/reloading-classes.html?origin=old_help) to enable hot swapping of source code while debugging. Scala IDE has similar ["hot code replace"](http://scala-ide.org/docs/current-user-doc/features/scaladebugger/index.html) functionality.
 

____________________________________________________________

Lemma: How to "hot reload" calls to scala-trace-debug in IntelliJ:
 
1 While debugging, add a call to trace, assert, or traceExpression and then save. This call will be hot swapped in.
 
 
2 Compile the class that you are in.
 
![Compile](http://i.imgur.com/pihleox.png)
 
 
3 Reload said class.
 
![Reload](http://i.imgur.com/25yb2cw.png)
 
 
4 Drop the current stack frame which has become obsolete.
 
![Drop](http://i.imgur.com/6QRxWRt.png)
 
 
5 Click "Step Over" to get a new stack frame.
 
![New](http://i.imgur.com/0VkAV0k.png)
 
 
6 The hot swapped code should run.
 
![Run](http://i.imgur.com/Soy49Lm.png)
 
 
____________________________________________________________
 
Back to "Use in practice":
 
- Use stack traces to trace an execution path through the source code as you use the debugger to look at values in the source code.
 
- Make calls to nonFatalAssert to verify that any assumptions you may have about the source code are correct.
 
- If you see a line of code that looks like "object method object method parameter" and you don't know where to put the parenthesis, use traceExpression to de-sugar the expression.
 
Calls to scala-debug-trace are not meant to be left inside
production code. Use `git reset --hard` or `git reset --hard HEAD~0` to obliterate all uncommitted changes.
