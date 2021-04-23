package povParser.state;

import povParser.Automat;

//interface et sous-classes inspirées de: https://www.codingame.com/playgrounds/10542/design-pattern-state/introduction

/**
 * interface clé du pattern state qui est l'état courant du jeton, il représente la figure que l'on est en train de parser
 */
public interface EtatToken
{
    /**
     * Cette méthode effectue le parsing de n'importe quel objet
     * @param context contexte courant de l'automate
     */
    public Object action(Automat context);
}
