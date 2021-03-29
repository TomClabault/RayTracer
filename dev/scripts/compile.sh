#!/bin/sh

wget https://gluonhq.com/download/javafx-16-sdk-linux/ /lib/javafx.zip
unzip /lib/javafx.zip
rm -rf /lib/javafx.zip
../ant compile