#!/bin/sh

cd $(dirname $0)/..
env | grep PATH_TO_FX > /dev/null
return_status=$?
if [ $return_status -ne 0 ]
then
	>&2 echo "il faut d'abord installer la lib javafx et la mettre dans une variable PATH_TO_FX (voir README)"
	exit 1
fi

ant compile

cp -r resources/* build
