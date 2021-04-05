package povParser.automat;

import materials.Material;

import java.io.StreamTokenizer;

enum Attribute
{
    PIGMENT,
    FINISH,
    SPECULAR, // only a coeff (not a vector)
    AMBIENT, // only a coeff (not a vector)
    DIFFUSE,
    REFLECTION,
    PHONG_SIZE, // = SHININESS in our raytracer
    PHONG, // diffuse -> diffuse * phong / specular -> specular * phong / ambient -> ambient * phong
    OUTSIDE,
}

public abstract class EtatUtil
{
    public Attribute parsePropertryAndGetState(Automat context)
    {
        if(context.isCurrentTokenAWord())
        {
            if(context.currentWord("finish"))
            {
                return Attribute.FINISH;
            }
            else if(context.currentWord("pigment"))
            {
                return Attribute.PIGMENT;
            }
            else if(context.currentWord("ambient"))
            {
                return Attribute.AMBIENT;
            }
            else if(context.currentWord("specular"))
            {
                return Attribute.SPECULAR;
            }
            else if(context.currentWord("diffuse"))
            {
                return Attribute.DIFFUSE;
            }
            else if(context.currentWord("reflection"))
            {
                return Attribute.REFLECTION;
            }
            else if(context.currentWord("phong_size"))
            {
                return Attribute.PHONG_SIZE;
            }
            else if(context.currentWord("phong"))
            {
                return Attribute.PHONG;
            }
        }
        return null;
    }

    public Material parseAttributes(Automat context)
    {
        Material material = new Material(null, 0, 0, 0, 0, false, 0);
        Attribute state = parsePropertryAndGetState(context);
        int token = 0;
        while(state != Attribute.OUTSIDE)
        {
            switch (state)
            {
                case AMBIENT:
                {
                    context.callNextToken(); // skip ambient
                    //appeler le setter material correspondant à l'ambient
                    context.callNextToken(); // skip ambient coeff
                    state  = parsePropertryAndGetState(context);
                    if(state == null)
                        state = Attribute.OUTSIDE;
                    break;
                }
                case SPECULAR:
                {
                    context.callNextToken(); // skip specular
                    break;
                }
            }
        }

    }
}
