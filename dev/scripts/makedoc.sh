#!/bin/doc

cd $(dirname $0)/..
env | grep PATH_TO_FX
return_status=$?
if [ $return_status -ne 0 ]
then
	>&2 echo "il faut d'abord installer la lib javafx et la mettre dans une variable PATH_TO_FX (voir README)"
	exit 1
fi
[ -d doc ] || mkdir doc
find ./src -type f -name *.java | xargs javadoc -d doc --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml,javafx.swing
