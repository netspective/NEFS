******************************************************************************
** This document describes general knowledge required for framework         **
** programmers.                                                             **
******************************************************************************

$ID:$

Standard IDE and App Server
------------------------------------------------------------------------------
* CVS for revision control (http://www.cvshome.org)
* Resin for web application server (http://www.caucho.com)
* JetBrains IDEA for Java IDE (http://www.jetbrains.com)

Note: you are free to use any other app server or IDE you would like, but you
      will need to setup your own environments and configuration files
      manually because the Netspective developers use the standards shown
      above.

Steps for CVS checkout
------------------------------------------------------------------------------
CVSROOT is user.name@cvs.netspective.com:/home/netspective/cvs/Frameworks.

Default location on development boxes
------------------------------------------------------------------------------
* C:/Projects/Frameworks (called NEFS_HOME)
* NEFS_HOME/tools/idea/classes for build directory through IDEA
* NEFS_HOME/webapps for test applications

Note: There is a default resin configuration file called
      NEFS_HOME/webapps/resin-2.x.conf available for your use. This
      configuration file points to port 8099 as the webserver and
      NEFS_HOME/webapps as the default war-dir so that it "auto-registers"
      all the web apps automatically.

Steps for obtaining the code
------------------------------------------------------------------------------
1. cvs checkout Axiom (into NEFS_HOME/Axiom)
2. cvs checkout Commons (into NEFS_HOME/Commons) 
3. cvs checkout Junxion (into NEFS_HOME/Junxion) 
4. cvs checkout Sparx (into NEFS_HOME/Sparx) 
5. cvs checkout tools (into NEFS_HOME/tools -- contains IDEA project)
6. cvs checkout webapps (into NEFS_HOME/webapps -- contains all the test and
   sample applications)

First-time only steps
------------------------------------------------------------------------------
1. If you are not using the standard directory locations, open
   NEFS_HOME/Sparx/src/java/com/netspective/sparx/conf/console.xml and change
   <register-freemarker-configuration name='console'> 'base-dir' attribute to
   your particular location.
2. If you are not using the standard directory locations, open the Resin
   NEFS_HOME/webapps/resin-2.x.conf configuration file and follow the
   directions in the top of the file.

Steps for all builds (each time code is updated)
------------------------------------------------------------------------------
1. Open IDEA using NEFS_HOME/tools/idea/Netspective Frameworks.ipr project
   file.
2. Compile full project (will put classes in NEFS_HOME/tools/idea/classes) 
3. Start Resin using NEFS_HOME/webapps/resin-2.x.conf configuration file and
   point to http://localhost:8099/ to view the NEFS Web Apps home page.
   Resin is started on Windows from the command line using
   RESIN_HOME/bin/httpd -conf NEFS_HOME/webapps/resin-2.x.conf and on UNIX
   using RESIN_HOME/bin/httpd.sh -conf NEFS_HOME/webapps/resin-2.x.conf.
