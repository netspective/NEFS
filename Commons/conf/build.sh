#!/bin/sh
#@echo off

#REM $Id: build.sh,v 1.1 2003-07-14 14:01:18 erich.oliphant Exp $

#REM **************************************************************************
#REM ** This script should be be run from the PROJECT_HOME\conf directory.   **
#REM ** It is basically a "launcher" for Ant and the actual work is done in  **
#REM ** the build.xml file.                                                  **
#REM **************************************************************************

#if "%JAVA_HOME%" == "" echo Error: JAVA_HOME environment variable is not set. && goto end
#if "%NS_COMMONS_HOME%" == "" set NS_COMMONS_HOME=..\
NS_COMMONS_HOME=../
#if "%NS_COMMONS_REDIST_LIB%" == "" set NS_COMMONS_REDIST_LIB=%NS_COMMONS_HOME%\lib\redist
NS_COMMONS_REDIST_LIB=$NS_COMMONS_HOME/lib/redist

#if "%JAVACMD%" == "" set JAVACMD=%JAVA_HOME%\bin\java
#if not exist "%JAVACMD%.exe" echo Error: "%JAVACMD%.exe" not found - check JAVA_HOME && goto end
JAVACMD=$JAVA_HOME/bin/java

#if exist "%JAVA_HOME%/lib/tools.jar" set JAVACP=%JAVA_HOME%\lib\tools.jar
#if exist "%JAVA_HOME%/lib/classes.zip" set JAVACP=%JAVACP%;%JAVA_HOME%\lib\classes.zip
JAVACP=$JAVA_HOME/lib/tools.jar
JAVACP=$JAVACP:$JAVA_HOME/lib/classes.zip
$JAVACMD -classpath $NS_COMMONS_REDIST_LIB/ant.jar:$NS_COMMONS_REDIST_LIB/ant-optional.jar:$NS_COMMONS_REDIST_LIB/junit.jar:$NS_COMMONS_REDIST_LIB/clover.jar:$NS_COMMONS_REDIST_LIB/xerces.jar:$JAVACP org.apache.tools.ant.Main $1 $2 $3 $4 $5 $6 $7 $8 $9

