<h1>Standard tools</h1>
Although NEFS will work with almost any IDE and app server, when performing any work on the Frameworks from a development perspective, the assumption is that we're using the following tools:
<ul>
	<li><a href="http://java.sun.com">JDK 1.3 or 1.4</a> for Java Software Development Kit
	<li><a href="http://www.cvshome.org">CVS</a> for revision control (visit <a href="http://www.wincvs.org">www.wincvs.org</a> for a Windows client)
	<li><a href="http://www.caucho.com">Resin 2.1.10+</a> for web application server. Release 3.0 of Resin should work but has <b>not</b> been tested.
	<li><a href="http://www.jetbrains.com">JetBrains IDEA 3.0.3 or above</a> for Java IDE (optional) or you can use the supplied Ant build scripts </ul>

<h1>CVSROOT information</h1>
CVSROOT is <code>user@cvs.netspective.com:/home/netspective/cvs/Frameworks</code>. If you already have access to Sparx CVS, then your user.name and password is the same. If you don't yet have access to Sparx CVS then you'll need to get a login/account.

<h1>Default paths</h1>
Default location on development boxes are
<ul>
  <li><code>C:/Projects/Frameworks</code> for framework modules (called <b><code>NEFS_HOME</code></b>).
  <li><code>NEFS_HOME/webapps</code> for test applications</li>
  <li><code>NEFS_HOME/tools/idea/classes</code> for build directory through IDEA (not required if you're using Ant) </ul> <p>
<b>Note</b>: All defaults assume development work is being done on a Win32 platform
      but everything will work on OS X, Linux, and UNIX as well. If you are using
      OS X, Linux, or UNIX the default shell scripts assume that you are using
      <code>$HOME/projects/Frameworks</code> as NEFS_HOME.

<h1>Steps for CVS checkout of NEFS modules</h1>
<ol>
  <li><code>cvs checkout Axiom</code> (into <code>NEFS_HOME/Axiom</code>)
  <li><code>cvs checkout Commons</code> (into <code>NEFS_HOME/Commons</code>)
  <li><code>cvs checkout Junxion</code> (into <code>NEFS_HOME/Junxion</code>)
  <li><code>cvs checkout Sparx</code> (into <code>NEFS_HOME/Sparx</code>)</code>
  <li><code>cvs checkout support</code> (into <code>NEFS_HOME/support</code> -- contains the DocBook source for User's Manual)
  <li><code>cvs checkout tools</code> (into <code>NEFS_HOME/tools</code> -- <code>NEFS_HOME/tools/idea</code> contains IntelliJ IDEA project)
  <li><code>cvs checkout webapps</code> (into <code>NEFS_HOME/webapps</code> -- contains all the test and sample applications) </ol>

<h1>Compiling (required each time CVS sandbox is updated)</h1> <b>If you're using JDK 1.4</b> <ol>
    <li>Locate the <code>xalan.jar</code> file located in <code>NEFS_HOME/Commons/lib/redist</code>.
    <li>Locate your <code>JAVA_HOME</code> folder.
    <li>Create a folder under <code>JAVA_HOME/jre/lib</code> named <code>endorsed</code> if there isn't one already.
	<li>Copy the <code>xalan.jar</code> file into the previously mentioned folder. </ol>

<b>If you're compiling with Intellij IDEA</b>
<ol>
    <li>Open IDEA using <code>NEFS_HOME/tools/idea/Netspective&nbsp;Frameworks.ipr</code> project file.
    <li>In IDEA, open File | Project Properties and make sure that the output directory points to <code>NEFS_HOME/tools/idea/classes</code>.
    <li>Compile full project using Build | Rebuild Project (this will put classes in <code>NEFS_HOME/tools/idea/classes</code>)
</ol>

<b>If you're compiling with Ant</b>
<ol>
    <li>Open command prompt or terminal window</code>
    <li>cd <code>NEFS_HOME/tools</code>
    <li>On Win32 type '<code>build</code>' and on UNIX type '<code>sh build.sh</code>' </ol>

<h1>Starting the Resin app server</h1>
If you are not using the standard directory locations, open the Resin <code>NEFS_HOME/webapps/nefs-resin-*.conf</code> configuration files and follow the directions in the top of each file to ensure that the proper paths are supplied. <p> <b>If you compiled using Intellij IDEA</b> <ul>
    <li>
       Resin is started on Windows from the command line using
       <code>RESIN_HOME/bin/httpd -conf NEFS_HOME/webapps/nefs-resin-sde-<b>ide</b>.conf</code>
    </li>
    <li>
       Resin is started on UNIX/OS X from the command line using
       <code>RESIN_HOME/bin/httpd.sh -conf NEFS_HOME/webapps/nefs-resin-sde-<b>ide</b>.conf</code>
    </li>
</ul>

<b>If you compiled with Ant</b>
<ul>
    <li>
       Resin is started on Windows from the command line using
       <code>RESIN_HOME/bin/httpd -conf NEFS_HOME/webapps/nefs-resin-sde-<b>ant</b>.conf</code>
    </li>
    <li>
       Resin is started on UNIX/OS X from the command line using
       <code>RESIN_HOME/bin/httpd.sh -conf NEFS_HOME/webapps/nefs-resin-sde-<b>ant</b>.conf</code>
    </li>
</ul>

Point to <a href="http://localhost:8099">http://localhost:8099</a> to view the NEFS Sampler home page. <p>
