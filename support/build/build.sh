#!/bin/sh
#@echo off

#$Id: build.sh,v 1.1 2004-02-16 17:41:56 shahid.shah Exp $

#**************************************************************************
#** This script should be be run from the PROJECT_HOME\conf directory.   **
#** It is basically a "launcher" for Ant and the actual work is done in  **
#** the build.xml file.                                                  **
#**************************************************************************

#--------------------------------------------------------------------------
#-- Locate the Java home
#--------------------------------------------------------------------------

if [ -n "$JAVA_HOME" ] ; then
  if [ -f "$JAVA_HOME/lib/tools.jar" ] ; then
    CLASSPATH=$CLASSPATH:$JAVA_HOME/lib/tools.jar
  fi
 
  if [ -f "$JAVA_HOME/lib/classes.zip" ] ; then
    CLASSPATH=$CLASSPATH:$JAVA_HOME/lib/classes.zip
  fi
else
  echo "Warning: JAVA_HOME environment variable not set."
  echo "  If build fails because sun.* classes could not be found"
  echo "  you will need to set the JAVA_HOME environment variable"
  echo "  to the installation directory of java."
fi

#--------------------------------------------------------------------------
#-- Locate the Java compiler
#--------------------------------------------------------------------------

# IBM's JDK on AIX uses strange locations for the executables:
# JAVA_HOME/jre/sh for java and rmid
# JAVA_HOME/sh for javac and rmic
if [ -z "$JAVAC" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/sh/javac" ] ; then 
      JAVAC=${JAVA_HOME}/sh/javac;
    else
      JAVAC=${JAVA_HOME}/bin/javac;
    fi
  else
    JAVAC=javac
  fi
fi
if [ -z "$JAVACMD" ] ; then 
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then 
      JAVACMD=$JAVA_HOME/jre/sh/java
    else
      JAVACMD=$JAVA_HOME/bin/java
    fi
  else
    JAVACMD=java
  fi
fi
 
if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly."
  echo "  We cannot execute $JAVACMD"
  exit
fi

#--------------------------------------------------------------------------
#-- Setup the Netspective Enterprise Frameworks Suite (NEFS) locations
#--------------------------------------------------------------------------

if [ -n "$NEFS_HOME" ] ; then
  NEFS_HOME=..
fi

if [ -n "$NEFS_COMMONS_HOME" ] ; then
  NEFS_COMMONS_HOME=$NEFS_HOME/Commons
fi

NEFS_COMMONS_REDIST_LIB=$NEFS_COMMONS_HOME/lib/redist

$JAVACMD -classpath $NEFS_COMMONS_REDIST_LIB/ant.jar:$NEFS_COMMONS_REDIST_LIB/ant-optional.jar:$NEFS_COMMONS_REDIST_LIB/junit.jar:$NEFS_COMMONS_REDIST_LIB/clover.jar:$NEFS_COMMONS_REDIST_LIB/xerces.jar:$CLASSPATH org.apache.tools.ant.Main $1 $2 $3 $4 $5 $6 $7 $8 $9

