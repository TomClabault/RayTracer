#!/bin/sh

cd $(dirname $0)/..
[ -d lib ] || mkdir lib
wget https://gluonhq.com/download/javafx-16-sdk-linux/ -o /lib/javafx.zip #peut mettre un certain temps à s'exécuter
unzip lib/javafx.zip
rm -rf lib/javafx.zip
ant compile