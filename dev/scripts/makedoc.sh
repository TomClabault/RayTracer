#!/bin/doc

cd $(dirname $0)/..
[ -d doc ] || mkdir doc
javadoc -d "/doc" -sourcepath ./src/*/*.java