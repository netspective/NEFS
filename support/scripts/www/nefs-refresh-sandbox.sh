#!/bin/bash

export NEFS_WEBAPPS_HOME=$NEFS_HOME/webapps
export NEFS_BUILD_HOME=$NEFS_HOME/support/build
export WWW_SCRIPTS_HOME=$NEFS_HOME/support/scripts/www

echo Stopping Resin
$WWW_SCRIPTS_HOME/resin-ctl.sh stop

echo Performing CVS Update
cd Frameworks
cvs -q update -P -d

echo Building libraries
cd $NEFS_BUILD_HOME
sh build.sh

cd $NEFS_WEBAPPS_HOME

echo Cleaning old webapps class and execution.properties files
find . -name "*.class" -exec rm -f {} \; -print
find . -name "execution.properties" -exec rm -f {} \; -print
find . -name "ant-build-project.log" -exec rm -f {} \; -print

echo Starting up Resin
cd 
$WWW_SCRIPTS_HOME/resin-ctl.sh start

echo Waiting for Resin to startup...
sleep 15s

echo Warming up all the apps...
wget --non-verbose --input-file=$WWW_SCRIPTS_HOME/nefs-webapps-warm-up-urls.list --output-document=- > /dev/null

echo Building Documentation
cd $NEFS_BUILD_HOME
sh build.sh docs

echo WARing up all the apps
cd $NEFS_WEBAPPS_HOME
sh build.sh nefs.war-all-apps

cd $HOME
