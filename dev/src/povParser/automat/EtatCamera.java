package povParser.automat;

/**
 * différents attributs que peut prendre la caméra ainsi que les éléments de syntaxe qui représentent différents états
 */
enum CameraContent
{
    OPENING_BRACKET,
    ENDING_BRACKET,
    LOCATION, //position
    DIRECTION,
    LOOK_AT,

}

/**
 * Classe représentant l'état camera, c'est à dire que le jeton courant est le mot
 * camera dans le fichier pov. C'est ici que tout le parsing de la camera est effectué.
 */
public class EtatCamera implements EtatToken
{

    /**
     * @param context contexte courant de l'automate
     * Cette méthode effectue le parsing d'un objet camera
     */
    @Override
    public void action(Automat context)
    {

    }
}
