#!/bin/bash

echo Stopping Resin
$NEFS_HOME/tools/resin-ctl.sh stop

echo Performing CVS Update
cd Frameworks
cvs -q update -P -d

echo Building libraries
cd tools
sh build.sh

cd ../webapps

echo Cleaning old webapps class and execution.properties files
find . -name "*.class" -exec rm -f {} \; -print
find . -name "execution.properties" -exec rm -f {} \; -print
find . -name "ant-build-project.log" -exec rm -f {} \; -print

echo Starting up Resin
cd 
$NEFS_HOME/tools/resin-ctl.sh start

echo Waiting for Resin to startup...
sleep 15s

echo Warming up all the apps...
wget --non-verbose --input-file=$NEFS_HOME/tools/nefs-webapps-warm-up-urls.list --output-document=- > /dev/null

echo Building Documentation
cd Frameworks/tools
sh build.sh docs

echo WARing up all the apps
cd Frameworks/webapps
sh build.sh nefs.war-all-apps

cd $HOME
