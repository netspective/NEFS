#!/bin/bash

echo Stopping Resin
$NEFS_RESIN stop

echo Performing CVS Update
cd Frameworks
cvs -q update -P -d

echo Building libraries
cd tools
sh build.sh

cd ../webapps

echo Cleaning old webapps class and execution.properties files
find . -name "*.class" -exec rm {} \; -print
find . -name "execution.properties" -exec rm {} \; -print

echo Starting up Resin
cd 
$NEFS_RESIN start

echo Waiting for Resin to startup...
sleep 10s

echo Warming up all the apps...
wget --non-verbose --input-file=$NEFS_HOME/tools/nefs-webapps-warm-up-urls.list --output-document=- > /dev/null

echo WARing up all the apps
cd Frameworks/webapps
sh build.sh nefs.war-all-apps

echo Building ChangeLog
cd Frameworks/support/docs
sh build.sh change-log

cd $HOME
