------------------------------------------------------

L'exécution du projet nécessite l'installation de la librairie javafx. Nous avons
réalisé notre projet avec javafx version 16. Pour éviter tout éventuels problèmes
de compatibilité, l'installation de la version 16 de javafx est recommandée.

Téléchargement possible à l'adresse: https://gluonhq.com/products/javafx/

------------------------------------------------------

Clean.sh:
Supprime les dossiers générés par les autres scripts tels que "doc" et "build".

Exécution:
sh clean.sh

-----
Compile.sh:
Compile les sources du projet. Nécessite d'avoir téléchargé la library javafx
et d'avoir mis en place la variable d'environnement PATH_TO_FX="xxx/xxx/javafx-sdk/lib"

Exécution:
sh compile.sh

-----
Makedoc.sh:
Génère la documentation du projet et la place dans un dossier "doc" qui est
créé si préalablement inexistant.

Exécution:
sh makedoc.sh

-----
Run.sh:
Compile le projet et l'exécute.

Exécution:
sh run.sh
