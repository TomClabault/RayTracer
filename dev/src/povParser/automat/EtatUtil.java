package povParser.automat;

import javafx.scene.paint.Color;
import materials.Material;

import java.io.StreamTokenizer;
import java.util.ArrayList;

enum Attribute
{
    PIGMENT,
    FINISH,
    SPECULAR, // only a coeff (not a vector)
    AMBIENT, // only a coeff (not a vector)
    DIFFUSE,
    REFLECTION, // only a coeff
    PHONG_SIZE, // = SHININESS in our raytracer
    PHONG, // is a coeff | diffuse -> diffuse * phong / specular -> specular * phong / ambient -> ambient * phong
    OPENING_CHEVRON,
    ENDING_CHEVRON,
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

    public Material parseAttributes(Automat context) throws RuntimeException
    {
        Material material = new Material(null, 0, 0, 0, 0, false, 0);
        Attribute state = parsePropertryAndGetState(context);
        int token = 0;
        boolean color = false;
        double phong_value = 0;
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
                    material.setSpecularCoeff(context.getNumberValue());
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                        state = Attribute.OUTSIDE;
                    break;
                }

                case DIFFUSE:
                {
                    context.callNextToken(); //skip diffuse
                    material.setDiffuseCoeff(context.getNumberValue());
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                        state = Attribute.OUTSIDE;
                    break;
                }

                case FINISH:
                {
                    context.callNextToken(); // skip finish
                    context.callNextToken(); // skip '{'
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        throw new RuntimeException("fichier pov non valide\n");
                    }
                    break;
                }
                case PIGMENT:
                {
                    context.callNextToken(); //skip pigment
                    context.callNextToken(); //skip '{'
                    if (context.currentWord("color"))
                    {
                        color = true;
                        context.callNextToken();
                        if(context.currentWord("rgb"))
                        {
                            token = context.callNextToken();
                            if((char)token == '<')
                            {
                                state = Attribute.OPENING_CHEVRON;
                                break;
                            }
                            else
                            {
                                int colorAttribute = (int)context.getNumberValue() * 255;
                                material.setColor(Color.rgb(colorAttribute, colorAttribute, colorAttribute));
                            }
                            context.callNextToken(); // skip color value
                            context.callNextToken(); // skip '}'
                            state = parsePropertryAndGetState(context);
                            if(state == null)
                                state = Attribute.OUTSIDE;

                        }
                    }
                    break;
                }
                case PHONG:
                {
                    context.callNextToken(); //skip phong
                    phong_value = context.getNumberValue();
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);
                    if (state == null)
                        state = Attribute.OUTSIDE;
                    break;
                }

                case PHONG_SIZE:
                {
                    context.callNextToken(); //skip phong_size
                    material.setShininess((int)context.getNumberValue());
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                        state = Attribute.OUTSIDE;
                    break;
                }

                case REFLECTION:
                {
                    context.callNextToken(); //skip reflexion
                    material.setReflectiveCoeff(context.getNumberValue());
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = Attribute.OUTSIDE;
                    }
                    break;
                }

                case OPENING_CHEVRON:
                {
                    int[] colorTab = new int[3];
                    for(int i = 0; i < 3; i++)
                    {
                        context.callNextToken();
                        context.callNextToken();
                        colorTab[i] = (int)context.getNumberValue() * 255;
                    }
                    material.setColor(Color.rgb(colorTab[0], colorTab[1], colorTab[2]));
                    state = Attribute.ENDING_CHEVRON;
                    break;
                }
                case ENDING_CHEVRON:
                {
                    context.callNextToken(); // skip '>'
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = Attribute.OUTSIDE;
                    }
                }
            }
        }
        return material;
    }
}
