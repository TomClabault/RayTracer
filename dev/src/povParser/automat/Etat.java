package povParser.automat;

public enum Etat
{
    EXTERIEUR,

        SPHERE,
        SPHERE_OPENING_BRACKET,
        SPHERE_ENDING_BRACKET,
        SPHERE_OPENING_CHEVRON,
        SPHERE_ENDING_CHEVRON,

            SPHERE_PIGMENT_OPENING_BRACKET,
            SPHERE_PIGMENT_ENDING_BRACKET,
            SPHERE_PIGMENT_COLOR,


                SPHERE_PIGMENT_RGB_OPENING_CHEVRON,
                SPHERE_PIGMENT_RGB_ENDING_CHEVRON,


            SPHERE_FINISH_AMBIENT,
            SPHERE_FINISH_DIFFUSE,
            SPHERE_FINISH_SPECULAR,
            SPHERE_FINISH_PHONG, // --> shininess
            /*
            multiplier ambient diffuse et specular par phong
            * */
            SPHERE_FINISH_PHONG_SIZE,

            SPHERE_FINISH_REFLECTION,

    TRIANGLE,
    POLYGON,
    PLANE,
    PRISM

}
