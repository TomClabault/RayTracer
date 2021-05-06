package parsers.povParser;

/**
 * enumeration de tous les etats de figure ainsi qu'un etat exterieur
 */
public enum PovObjectsState
{
    OUTSIDE,
    SPHERE,
    TRIANGLE,
    PLANE,
    BOX, //correspond a un rectangle
    LIGHT_SOURCE,
    CAMERA,
}
