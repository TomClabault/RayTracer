package parsers.povParser.state;

import geometry.Shape;
import parsers.povParser.PovAutomat;

/**
 * Classe décrivant tout simplement un état vide
 */
public class StateOutside implements StateToken
{
    /**
     * Méthode permettant de passer le dernier jeton
     * @param context contexte courant de l'automate
     * @return null car aucune figure n'est parsée dans cet état
     */
    @Override
    public Shape parse(PovAutomat context)
    {
        context.callNextToken();
        return null;
    }
}
