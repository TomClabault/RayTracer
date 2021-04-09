package povParser.automat;

import javafx.scene.paint.Color;
import materials.Material;

import javax.imageio.spi.ImageOutputStreamSpi;
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

    int nbBracket = 1;

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


    public Attribute checkEndingBracket(Automat context)
    {
        /*this.nbBracket--;
        Attribute state = parsePropertryAndGetState(context);
        int token = context.callNextToken();

            System.out.println(context.getStreamTokenizer());
            if ((char) token == '}')
            {
                System.out.println("NB DE BRACKETS : " + this.nbBracket);
                this.nbBracket --;
                if (this.nbBracket == 0)
                {
                    System.out.println("check ending bracket function : OUTSIDE");
                    return Attribute.OUTSIDE;
                }
                System.out.println("check ending bracket function : " + state);
                state = checkEndingBracket(context);
        }
        return state;*/
        Attribute state = null;
        System.out.println("nb bracket : " + this.nbBracket);
        this.nbBracket--;//'}'
        if(this.nbBracket == 0)
        {
            System.out.println("STATE in check function: " + state);
            return Attribute.OUTSIDE;
        }
        context.callNextToken();
        state = parsePropertryAndGetState(context);
        System.out.println("STATE IN CHEcK: " + state);
        if(state == null)
        {
            state = checkEndingBracket(context);
        }
        return state;
    }

    public Material parseAttributes(Automat context) throws RuntimeException
    {
        Material material = new Material(null, 0, 0, 0, 0, false, 0);
        Attribute state = parsePropertryAndGetState(context);
        System.out.println("ICIIIIIIII : " + state);
        int token = 0;
        boolean color = false;
        double phong_value = 0;

        while(state != Attribute.OUTSIDE)
        {
            switch (state)
            {
                case AMBIENT:
                {
                    System.out.println("ambient");
                    System.out.println(context.getStreamTokenizer());
                    context.callNextToken(); // skip ambient
                    //appeler le setter material correspondant à l'ambient
                    token = context.callNextToken(); // skip ambient coeff
                    state  = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    System.out.println("state : " + state);
                    System.out.println(context.getStreamTokenizer());
                    break;
                }

                case SPECULAR:
                {
                    System.out.println("specular");
                    System.out.println(context.getStreamTokenizer());

                    context.callNextToken(); // skip specular
                    material.setSpecularCoeff(context.getNumberValue());
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    System.out.println("state : " + state);
                    System.out.println(context.getStreamTokenizer());
                    break;
                }

                case DIFFUSE:
                {
                    System.out.println("specular");
                    System.out.println(context.getStreamTokenizer());

                    context.callNextToken(); //skip diffuse
                    material.setDiffuseCoeff(context.getNumberValue());
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    System.out.println("state : " + state);
                    System.out.println(context.getStreamTokenizer());
                    break;
                }

                case FINISH:
                {
                    System.out.println("finish");
                    System.out.println(context.getStreamTokenizer());

                    context.callNextToken(); // skip finish
                    context.callNextToken(); // skip '{'
                    nbBracket++;
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        throw new RuntimeException("fichier pov non valide\n");
                    }
                    System.out.println("state : " + state);
                    System.out.println(context.getStreamTokenizer());
                    break;
                }
                case PIGMENT:
                {
                    System.out.println("pigment");
                    System.out.println(context.getStreamTokenizer());

                    context.callNextToken(); //skip pigment
                    context.callNextToken(); //skip '{'
                    this.nbBracket++;
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
                            System.out.println("STREAM !! : " + context.getStreamTokenizer());

                            context.callNextToken(); // skip color value
                            //nbBracket--;
                            System.out.println(context.getStreamTokenizer());
                            state = parsePropertryAndGetState(context);
                            System.out.println("STATE : " + state);
                            if(state == null)
                            {
                                state = this.checkEndingBracket(context);
                                System.out.println("STATE : " + state);
                            }

                        }
                    }
                    System.out.println("state : " + state);
                    System.out.println(context.getStreamTokenizer());
                    break;
                }
                case PHONG:
                {
                    System.out.println("phong");
                    System.out.println(context.getStreamTokenizer());
                    context.callNextToken(); //skip phong
                    phong_value = context.getNumberValue();
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    System.out.println("state : " + state);
                    System.out.println(context.getStreamTokenizer());
                    break;
                }

                case PHONG_SIZE:
                {
                    System.out.println("phong_size");
                    System.out.println(context.getStreamTokenizer());
                    context.callNextToken(); //skip phong_size
                    material.setShininess((int)context.getNumberValue());
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    System.out.println("state : " + state);
                    System.out.println(context.getStreamTokenizer());
                    break;
                }

                case REFLECTION:
                {
                    System.out.println("reflection");
                    System.out.println(context.getStreamTokenizer());
                    context.callNextToken(); //skip reflexion
                    material.setReflectiveCoeff(context.getNumberValue());
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);

                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    System.out.println("state : " + state);
                    System.out.println(context.getStreamTokenizer());
                    break;
                }

                case OPENING_CHEVRON:
                {
                    System.out.println("IN OPENING CHEVRON : " + context.getStreamTokenizer());
                    context.callNextToken(); //skip '<'
                    int[] colorTab = new int[3];
                    for(int i = 0; i < 3; i++)
                    {
                        System.out.println(context.getNumberValue());
                        colorTab[i] = (int)(context.getNumberValue() * 255);
                        context.callNextToken();
                        if(i < 2)
                            context.callNextToken();
                    }
                    System.out.println("AFTER FOR LOOP " + context.getStreamTokenizer());
                    material.setColor(Color.rgb(colorTab[0], colorTab[1], colorTab[2]));
                    context.callNextToken(); //skip '>'
                    state = this.parsePropertryAndGetState(context);
                    System.out.println("STATE AFTER >: " + state);
                    if (state == null)
                    {
                        state = this.checkEndingBracket(context);
                        System.out.println("STATE AFTER NULL: " + state);
                    }
                    break;
                }
                case ENDING_CHEVRON:
                {
                    context.callNextToken(); // skip '>'
                    /*state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        //state = this.checkEndingBracket(context);
                    }*/
                    break;
                }
            }
        }
        System.out.println(material);
        return material;
    }
}
