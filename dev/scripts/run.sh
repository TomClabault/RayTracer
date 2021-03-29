#!/bin/sh


cd $(dirname $0)/..
cd scripts
sh compile.sh
cd ..
ant run