#!/bin/sh

cd $(dirname $0)/..
if [ -d doc ]
then
    rm -rf doc
fi

if [ -d build ]
then
    rm -rf build
fi
