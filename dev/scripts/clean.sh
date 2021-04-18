#!/bin/sh

cd $(dirname $0)/..
if [ -d "../doc" ]
then
    pwd
    rm -rf ../doc
fi

if [ -d "../build" ]
then
    pwd
    rm -rf ../build
fi
