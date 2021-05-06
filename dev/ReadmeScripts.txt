------------------------------------------------------

L'execution du projet necessite l'installation de la librairie javafx. Nous avons
realise notre projet avec javafx version 16. Pour eviter tout eventuels problemes
de compatibilite, l'installation de la version 16 de javafx est recommandee.

Telechargement possible a l'adresse: https://gluonhq.com/products/javafx/

------------------------------------------------------

Clean.sh:
Supprime les dossiers generes par les autres scripts tels que "doc" et "build".

Execution:
sh clean.sh

-----
Compile.sh:
Compile les sources du projet. Necessite d'avoir telecharge la library javafx
et d'avoir mis en place la variable d'environnement PATH_TO_FX="xxx/xxx/javafx-sdk/lib"

Execution:
sh compile.sh

-----
Makedoc.sh:
Genere la documentation du projet et la place dans un dossier "doc" qui est
cree si prealablement inexistant.

Execution:
sh makedoc.sh

-----
Run.sh:
Compile le projet et l'execute.

Execution:
sh run.sh
