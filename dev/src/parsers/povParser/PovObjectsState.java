package parsers.povParser;

/**
 * énumération de tous les états de figure ainsi qu'un état extérieur
 */
public enum PovObjectsState
{
    OUTSIDE,
    SPHERE,
    TRIANGLE,
    PLANE,
    BOX, //correspond à un rectangle
    LIGHT_SOURCE,
    CAMERA,
}