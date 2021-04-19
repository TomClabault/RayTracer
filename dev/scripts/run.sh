#!/bin/sh


cd $(dirname $0)/..
cd scripts
sh compile.sh
return_status=$?
if [ $return_status -ne 0 ]
then
	exit 1
fi
cd ..
ant run
