Debugging example for https://github.com/JohnReedLOL/scala-trace-debug :
 
  Once upon a time, I had a very frustrating bug. It was caused by an
idiosyncrasy in the semantics of a copy constructor that caused it to do
a shallow copy instead of a deep copy and the code was multithreaded and
required a deep copy.
 
  How would I deal with such a subtle bug? Well since this tool was explicitly designed for this sort of a thing, here's one way.
 
1. Do git bisect to find the commit that produced the bug.
 
2. Read the lines of code that were changed.
 
3. Put break points in the area of what was changed. If your code has multiple threads that interact with one another, consider putting calls to scala trace debug in different threads. All printing in scala trace debug is done through an internal object called ImplicitTraceObject with a lock called PrintLock, so it should be thread safe.
 
4. Hot swap in some calls to scala-trace-debug during the debugging process to gather more information.
 
5. Follow [this IntelliJ doc](https://www.jetbrains.com/help/idea/2016.1/reloading-classes.html?origin=old_help) to enable hot reloading of source code while debugging. Scala IDE has similar ["hot code replace"](http://scala-ide.org/docs/current-user-doc/features/scaladebugger/index.html) functionality.
 

____________________________________________________________

Lemma: How to "hot reload" calls to scala-trace-debug in IntelliJ:
 
1 While debugging, add a call to trace, assert, or traceExpression and save. This call will be hot swapped in.
 
 
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
 
- Make calls to nonFatalAssert to verify any assumptions you may have about the source code or insert calls to the regular fatal assert to prove that something you believe to be true is in fact true. Note that often times these assertions make good unit tests.
 
- If you see a line of code that looks like "object method object method object method parameter" and you get confused by the whitespace, use traceExpression to de-sugar the expression.
 
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
