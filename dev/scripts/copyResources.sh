#!/bin/Sh

cd $(dirname $0)/..

if [ -d build ]
then
cp -r resources/* build
elif [ -d bin ]
then
cp -r resources/* bin
fi
