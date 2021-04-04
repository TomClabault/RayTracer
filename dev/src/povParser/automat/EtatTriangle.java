package povParser.automat;

import java.io.StreamTokenizer;
import java.util.ArrayList;

enum Trianglecontent
{
    OPENING_BRACKET,
    ENDING_BRACKET,
    OPENING_CHEVRON,
    ENDING_CHEVRON,
    OUTSIDE,
    FINISH,
    PIGMENT, //color of the figure
    AMBIENT,
    DIFFUSE,
    SPECULAR,
}

public class EtatTriangle implements EtatToken
{
    @Override
    public void action(Automat context)
    {
        int nextToken = context.callNextToken(); //accolade ouvrante après le mot triangle
        ArrayList<String> list = new ArrayList<>();

        StreamTokenizer st = context.getStreamTokenizer();
        context.callNextToken();
        int bracketNb = 0;
        int coordNb = 0;
        boolean color = false;
        boolean triangleCoord = false;

        Trianglecontent state = Trianglecontent.OPENING_BRACKET;

        while(state != Trianglecontent.OUTSIDE)
        {

            switch(state)
            {
                case OPENING_BRACKET:
                {
                    state = Trianglecontent.OPENING_CHEVRON;
                    triangleCoord = true;
                    bracketNb++;
                    break;
                }

                case OPENING_CHEVRON:
                {
                    context.callNextToken(); //skip '<'
                    if(triangleCoord)
                    {
                        for(int point = 0; point < 3; point++)
                        {
                            if(point > 0)
                                context.callNextToken();
                            for (int coord = 0; coord < 3; coord++)
                            {
                                list.add(String.valueOf(context.getNumberValue()));

                                context.callNextToken();


                                if(point < 2 || coord < 2)
                                    context.callNextToken();


                            }
                            if (point < 2)
                            {
                                context.callNextToken();
                            }
                        }
                        triangleCoord = false;
                    }

                    if(!color)
                        coordNb++;
                    else // color == true
                    {
                        for (int i = 0; i < 3; i++) {
                            list.add(String.valueOf(st.nval));
                            System.out.println(context.getStreamTokenizer());
                            context.callNextToken(); // la virgule
                            context.callNextToken();
                        }
                    }
                    state = Trianglecontent.ENDING_CHEVRON;
                    break;
                }
                case ENDING_CHEVRON:
                {
                    context.callNextToken(); //passe le chevron fermant
                    if(context.isCurrentTokenAWord())
                    {
                        if(context.currentWord("finish"))
                        {
                            state = Trianglecontent.FINISH;
                        }
                        else if(context.currentWord("pigment"))
                        {
                            state = Trianglecontent.PIGMENT;
                        }
                    }
                    else
                    {
                        state = Trianglecontent.ENDING_BRACKET;
                    }
                    break;
                }
                case ENDING_BRACKET:
                {
                    bracketNb--;
                    if(bracketNb == 0)
                    {
                        state = Trianglecontent.OUTSIDE;
                        break;
                    }
                    context.callNextToken();
                    if(context.isCurrentTokenAWord())
                    {
                        if(context.currentWord("pigment"))
                        {
                            state = Trianglecontent.PIGMENT;
                        }
                        else if(context.currentWord("finish"))
                        {
                            state = Trianglecontent.FINISH;
                        }
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
                            list.add("color"); //équivaut à figure.setFinish();
                            nextToken = context.callNextToken();
                            if((char)nextToken == '<')
                            {
                                state = Trianglecontent.OPENING_CHEVRON;
                                break;
                            }
                            else
                                list.add(String.valueOf(context.getNumberValue()));
                            context.callNextToken(); // skip color value
                            context.callNextToken(); // skip '}'
                            if(context.isCurrentTokenAWord())
                            {
                                if(context.currentWord("finish"))
                                {
                                    state = Trianglecontent.FINISH;
                                }
                            }
                            else
                                state = Trianglecontent.ENDING_BRACKET;

                        }
                    }
                    break;
                }

                case FINISH:
                {
                    context.callNextToken(); //skip finish
                    context.callNextToken(); //skip '{'
                    if(context.isCurrentTokenAWord())
                    {
                        if(context.currentWord("ambient"))
                        {
                            state = Trianglecontent.AMBIENT;
                        }
                        else if(context.currentWord("diffuse"))
                        {
                            state = Trianglecontent.DIFFUSE;
                        }
                        else if(context.currentWord("specular"))
                        {
                            state = Trianglecontent.SPECULAR;
                        }
                    }
                    break;
                }

                case AMBIENT:
                {
                    list.add("ambient");
                    context.callNextToken(); //skip ambient

                    if(context.isCurrentTokenAWord())
                    {
                        if(context.currentWord("rgb"))
                        {
                            context.callNextToken();
                            state = Trianglecontent.OPENING_CHEVRON;
                        }
                    }
                    else
                    {
                        list.add(String.valueOf(context.getNumberValue()));
                    }

                    context.callNextToken();
                    if(context.isCurrentTokenAWord())
                    {
                        if (context.currentWord("diffuse"))
                        {
                        state = Trianglecontent.DIFFUSE;
                        }
                        else if(context.currentWord("specular"))
                        {
                            state = Trianglecontent.SPECULAR;
                        }
                    }
                    else
                        state = Trianglecontent.ENDING_BRACKET;
                    break;
                }

                case DIFFUSE:
                {
                    list.add("diffuse");
                    context.callNextToken(); //skip diffuse
                    list.add(String.valueOf(context.getNumberValue()));
                    context.callNextToken();
                    if(context.isCurrentTokenAWord())
                    {
                        if(context.currentWord("ambient"))
                        {
                            state = Trianglecontent.AMBIENT;
                        }
                        else if(context.currentWord("specular"))
                        {
                            state = Trianglecontent.SPECULAR;
                        }
                    }
                    else
                    {
                        state = Trianglecontent.ENDING_BRACKET;
                    }
                    break;
                }

                case SPECULAR:
                {
                    context.callNextToken(); //skip specular
                    list.add(String.valueOf(context.getNumberValue()));
                    context.callNextToken();
                    if(context.isCurrentTokenAWord())
                    {
                        if(context.currentWord("ambient"))
                        {
                            state = Trianglecontent.AMBIENT;
                        }
                        else if(context.currentWord("diffuse"))
                        {
                            state = Trianglecontent.DIFFUSE;
                        }
                    }
                    else
                    {
                        state = Trianglecontent.ENDING_BRACKET;
                    }
                    break;
                }

                case OUTSIDE:
                {
                    state = Trianglecontent.OUTSIDE;
                    break;
                }
            }
        }
        System.out.println(list);
    }
}
