Please store any optional ant tasks in this directory and also:

!!! DELETE THE OPTIONAL ANT TASK from $ANT_HOME/lib !!!

ant-junit.jar requires classes found in junit.jar.  The below classloader 
problem requires us to "remove" ant-junit.jar from the ant distribution 
libraries and copy it here where our default classloader can happily load 
it and its required junit.jar.  So go to your ant install directory and 
rename ant-junit.jar to something like ant-junit.jarhide, then junit
tasks will function.

See http://ant.apache.org/faq.html#delegating-classloader for
more details -- basically, all the optional junit tasks have to
be deleted from your system so we can reference them here.

"In Ant 1.6 optional.jar has been split into multiple jars, each one 
containing classes with the same dependencies on external libraries. You 
can move the "offending" jar out of ANT_HOME/lib. For the <junit> task it 
would be ant-junit.jar and for <style>  it would be ant-trax.jar, 
ant-xalan1.jar or ant-xslp.jar - depending on the processor you use.

If you use the option to break up optional.jar for <junit> or
remove ant-junit.jar, you still have to use a <taskdef> with a 
nested <classpath> to define the junit task."
