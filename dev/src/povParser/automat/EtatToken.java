package povParser.automat;

/**
 * interface clé du pattern state qui est l'état courant du jeton, il représente la figure que l'on est en train de parser
 */
public interface EtatToken
{
    /**
     * @param context contexte courant de l'automate
     * Cette méthode effectue le parsing d'un objet light_source (point light)
     */
    public Object action(Automat context);
}
