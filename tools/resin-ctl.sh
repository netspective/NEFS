#!/bin/sh

JAVA_HOME=/usr/java/j2sdk1.4.2_03
RESIN_VERSION=2.1.12
RESIN_HOME=/shared/java/app-servers/resin-$RESIN_VERSION
RESIN_HTTPD_SCRIPT=$RESIN_HOME/bin/httpd.sh
WEBAPPS_HOME=/home/$USER/projects/Frameworks/webapps
RESIN_CONF_FILE=$WEBAPPS_HOME/nefs-resin-sde-ant.conf
RESIN_PID_FILE=$WEBAPPS_HOME/nefs-resin-sde-ant.PID
RESIN_LOG_HOME=/var/log/resin/$USER
RESIN="$RESIN_HTTPD_SCRIPT -java_home $JAVA_HOME -resin_home $RESIN_HOME -conf $RESIN_CONF_FILE -pid $RESIN_PID_FILE -stdout $RESIN_LOG_HOME/stdout.log -stderr $RESIN_LOG_HOME/stderr.log -J-Xms128m -J-Xmx256m"

echo $RESIN

$RESIN $@

