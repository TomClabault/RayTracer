package parsers.povParser.state;

import parsers.povParser.PovAutomat;

//interface et sous-classes inspirees de: https://www.codingame.com/playgrounds/10542/design-pattern-state/introduction

/**
 * interface cle du pattern state qui est l'etat courant du jeton, il represente la figure que l'on est en train de parser
 */
public interface StateToken
{
    /**
     * Cette methode effectue le parsing de n'importe quel objet
     * @param context contexte courant de l'automate
     */
    public Object parse(PovAutomat context);
}
