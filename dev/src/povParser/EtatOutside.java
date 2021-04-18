package povParser;

import geometry.Shape;

/**
 * Classe décrivant tout simplement un état vide
 */
public class EtatOutside implements EtatToken
{
    /**
     * Méthode permettant de passer le dernier jeton
     * @param context contexte courant de l'automate
     * @return null car aucune figure n'est parsée dans cet état
     */
    @Override
    public Shape action(Automat context)
    {
        context.callNextToken();
        return null;
    }
}
