package parsers.povParser.state;

import geometry.Shape;
import parsers.povParser.PovAutomat;

/**
 * Classe decrivant tout simplement un etat vide
 */
public class StateOutside implements StateToken
{
    /**
     * Methode permettant de passer le dernier jeton
     * @param context contexte courant de l'automate
     * @return null car aucune figure n'est parsee dans cet etat
     */
    @Override
    public Shape parse(PovAutomat context)
    {
        context.callNextToken();
        return null;
    }
}
