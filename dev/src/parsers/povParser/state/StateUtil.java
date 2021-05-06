package parsers.povParser.state;

import javafx.scene.paint.Color;
import materials.Material;
import materials.textures.ProceduralTextureCheckerboard;
import parsers.povParser.PovAutomat;


/**
 * enumeration decrivant les differents etats d'un modificateur d'objet (couleur, texture ...)
 */
enum Attribute
{
    PIGMENT,
    INTERIOR,
    IOR,
    SIZE,
    ROUGHNESS,
    FINISH,
    SPECULAR, // only a coeff (not a vector)
    AMBIENT, // only a coeff (not a vector)
    DIFFUSE,
    REFLECTION, // only a coeff
    PHONG_SIZE, // = SHININESS in our raytracer
    PHONG, // is a coeff | diffuse -> diffuse * phong / specular -> specular * phong / ambient -> ambient * phong
    OPENING_CHEVRON,
    OUTSIDE,
}

/**
 * Ctte classe decrit l'etat "modificateur de figure"
 */
public abstract class StateUtil
{

    private int nbBracket = 1;

    /**
     * Cette methode sert a renvoyer le prochain etat du jeton
     * @param context contexte courant de l'automate
     * @return L'etat decrivant l'objet qui va etre parsee
     */
    public Attribute parsePropertryAndGetState(PovAutomat context)
    {
        if(context.isCurrentTokenAWord())
        {
            if(context.currentWord("finish"))
            {
                return Attribute.FINISH;
            }
            else if(context.currentWord("ior"))
            {
                return Attribute.IOR;
            }
            else if(context.currentWord("interior"))
            {
                return Attribute.INTERIOR;
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
            else if(context.currentWord("roughness"))
            {
                return Attribute.ROUGHNESS;
            }
            else if(context.currentWord("size"))
            {
                return Attribute.SIZE;
            }
        }
        return null;
    }


    /**
     * Cette methode sert a tester la presence de fin de bloc apres une accolade fermante, s'il y a un objet apres un certain
     * nombre d'accolades, on renvoit l'etat decrivant cette objet
     * @param context contexte courant de l'automate
     * @return l'etat decrivant l'objet ou letat exterieur si l'on se trouve en fin de bloc
     */
    public Attribute checkEndingBracket(PovAutomat context)
    {
        Attribute state = null;
        this.nbBracket--;//'}'
        if(this.nbBracket == 0)
        {
            return Attribute.OUTSIDE;
        }
        context.callNextToken();
        state = parsePropertryAndGetState(context);
        if(state == null)
        {
            if((char)context.getCurrentToken() == ',')
            {
                context.callNextToken();
                state = parsePropertryAndGetState(context);
            }
            else
                state = checkEndingBracket(context);
        }
        return state;
    }

    /**
     * Cette methode sert a parser les modificateurs d'objet afin de les ajouter a une figure
     * @param context contexte courant de l'automate
     * @return Objet de type Attribute qui decrit toutes les modifications apportees a une figure
     */
    public Material parseAttributes(PovAutomat context) throws RuntimeException
    {
        Material material = new Material(Color.rgb(0, 0, 0), 0, 0, 0, 0, 0, false, 0, 0);
        Attribute state = parsePropertryAndGetState(context);
        int token = 0;
        boolean color = false;
        Double phong_value = null;
        ProceduralTextureCheckerboard checkerboard = new ProceduralTextureCheckerboard(Color.rgb(0, 0, 0), Color.rgb(0, 0, 0));
        int[] checkerColor1 = new int[3];
        int[] checkerColor2 = new int[3];
        boolean hasChecker = false;
        boolean checker2 = false; //second checkerboard color

        while(state != Attribute.OUTSIDE)
        {
            switch (state)
            {
                case AMBIENT:
                {
                    context.callNextToken(); // skip ambient
                    material.setAmbientCoeff(context.getNumberValue());
                    context.callNextToken(); // skip ambient coeff
                    state  = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    break;
                }

                case ROUGHNESS:
                {
                    context.callNextToken(); //skip roughness
                    material.setRoughness(context.getNumberValue());
                    context.callNextToken(); //skip roughness coeff
                    state = this.parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    break;
                }

                case SPECULAR:
                {
                    context.callNextToken(); // skip specular
                    material.setSpecularCoeff(context.getNumberValue());
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    break;
                }

                case DIFFUSE:
                {
                    context.callNextToken(); //skip diffuse
                    material.setDiffuseCoeff(context.getNumberValue());
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    break;
                }

                case FINISH:
                {
                    context.callNextToken(); // skip finish
                    context.callNextToken(); // skip '{'
                    nbBracket++;
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
                                int colorAttribute = (int)(context.getNumberValue() * 255);
                                if(hasChecker && !checker2)
                                {
                                    checkerboard.setColor1(Color.rgb(colorAttribute, colorAttribute, colorAttribute));
                                    checker2 = true;
                                }
                                else if(hasChecker && checker2)
                                {
                                    checkerboard.setColor2(Color.rgb(colorAttribute, colorAttribute, colorAttribute));
                                }
                                else
                                    material.setColor(Color.rgb(colorAttribute, colorAttribute, colorAttribute));
                            }

                            context.callNextToken(); // skip color value
                            state = parsePropertryAndGetState(context);
                            if(state == null)
                            {
                                state = this.checkEndingBracket(context);
                            }

                        }
                        else if (context.currentWord("Clear"))
                        {
                            material.setTransparent(true);
                            material.setColor(Color.BLACK);
                            context.callNextToken(); //skip Clear
                            state = this.parsePropertryAndGetState(context);
                            if (state == null)
                            {
                                state = this.checkEndingBracket(context);
                            }
                        }
                    }
                    else if(context.currentWord("checker"))
                    {
                        context.callNextToken(); // skip checker
                        state = Attribute.PIGMENT;
                        hasChecker = true;
                    }
                    break;
                }

                case PHONG:
                {
                    context.callNextToken(); //skip phong
                    phong_value = context.getNumberValue();
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    break;
                }

                case PHONG_SIZE:
                {
                    context.callNextToken(); //skip phong_size
                    material.setShininess((int)context.getNumberValue());
                    context.callNextToken();
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    break;
                }

                case REFLECTION:
                {
                    context.callNextToken(); //skip reflexion
                    material.setReflectiveCoeff(context.getNumberValue());
                    context.callNextToken(); //skip reflection value
                    state = parsePropertryAndGetState(context);

                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    break;
                }

                case SIZE:
                {
                    context.callNextToken(); //skip "size"
                    checkerboard.setSize(context.getNumberValue());
                    context.callNextToken(); // skip size value
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    break;

                }

                case OPENING_CHEVRON:
                {
                    context.callNextToken(); //skip '<'
                    int[] colorTab = new int[3];
                    for(int i = 0; i < 3; i++)
                    {
                        colorTab[i] = (int)(context.getNumberValue() * 255);
                        context.callNextToken();
                        if(i < 2)
                            context.callNextToken();
                    }
                    if(hasChecker && !checker2)
                    {
                        checkerboard.setColor1(Color.rgb(colorTab[0], colorTab[1], colorTab[2]));
                        checker2 = true;
                    }
                    else if(hasChecker && checker2)
                    {
                        checkerboard.setColor2(Color.rgb(colorTab[0], colorTab[1], colorTab[2]));
                        checkerboard.setColor2(Color.rgb(colorTab[0], colorTab[1], colorTab[2]));
                    }
                    else
                        material.setColor(Color.rgb(colorTab[0], colorTab[1], colorTab[2]));
                    context.callNextToken(); //skip '>'
                    state = this.parsePropertryAndGetState(context);
                    if (state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                    break;
                }
                case INTERIOR:
                {
                    context.callNextToken(); //skip interior
                    context.callNextToken(); //skip '{'
                    state = parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        throw new RuntimeException("fichier pov non valide");
                    }
                    break;
                }
                case IOR:
                {
                    context.callNextToken(); //skip "ior"
                    material.setRefractionIndex(context.getNumberValue());
                    state = this.parsePropertryAndGetState(context);
                    if (state == null)
                    {
                        state = this.checkEndingBracket(context);
                    }
                }
            }
        }
        if(phong_value != null)
        {
            material.setAmbientCoeff(material.getAmbientCoeff() * phong_value);
            material.setSpecularCoeff(material.getSpecularCoeff() * phong_value);
            material.setDiffuseCoeff(material.getDiffuseCoeff() * phong_value);
        }
        else if(hasChecker)
        {
            material.setProceduralTexture(checkerboard);
        }
        return material;
    }
}

