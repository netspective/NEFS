@echo off

REM $Id: build.bat,v 1.1 2003-10-27 18:36:18 shahid.shah Exp $

REM **************************************************************************
REM ** This script should be be run from the project build.xml directory.   **
REM ** It is basically a "launcher" for Ant and the actual work is done in  **
REM ** the build.xml file.                                                  **
REM **************************************************************************

if "%NEFS_HOME%" == "" set NEFS_HOME=..\..
if "%NEFS_COMMONS_HOME%" == "" set NEFS_COMMONS_HOME=%NEFS_HOME%\Commons
if "%NEFS_COMMONS_REDIST_LIB%" == "" set NEFS_COMMONS_REDIST_LIB=%NEFS_COMMONS_HOME%\lib\redist

java -classpath %NEFS_COMMONS_REDIST_LIB%\ant.jar;%NEFS_COMMONS_REDIST_LIB%\ant-optional.jar;%NEFS_COMMONS_REDIST_LIB%\xerces.jar;%JAVACP% org.apache.tools.ant.Main %1 %2 %3 %4 %5 %6 %7 %8 %9

:end
