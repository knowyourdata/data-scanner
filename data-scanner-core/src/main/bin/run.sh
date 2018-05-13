#!/bin/sh

# Verzeichnis des gerade ausgeführten Skripts ermitteln.
DIRECTORY=$(cd `dirname $0` && pwd)

# ins übergeordnete Verzeichnis des ausgeführten Skripts wechseln
cd $DIRECTORY/..

# BATCH_HOME setzen für config.sh
BATCH_HOME=`pwd`

cd bin

CP=$BATCH_HOME/lib/*
CP=$CP:$BATCH_HOME/config
CP=$CP:$BATCH_HOME/data

java -cp $CP -Xms256M -Xmx1024M $JAVA_OPTS at.fwd.data_scanner.ScannerCommand
