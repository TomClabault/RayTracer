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
            System.out.println(state);
            switch(state)
            {
                case OPENING_BRACKET:
                {
                    context.callNextToken();

                    state = Trianglecontent.OPENING_CHEVRON;
                    triangleCoord = true;
                    bracketNb++;
                    break;
                }

                case OPENING_CHEVRON:
                {
                    if(triangleCoord)
                    {
                        /*for (int i = 0; i < 3; i++)
                        {
                            System.out.println(context.getStreamTokenizer());
                            if(i > 0)
                                context.callNextToken();
                            for(int j = 0; j < 3; j++)
                            {

                                list.add(String.valueOf(context.getNumberValue()));
                                if(j < 2)
                                    context.callNextToken(); // skip ',' between values

                            }
                            context.callNextToken(); // skip '>'
                            if(i < 2)
                                context.callNextToken(); //skip ',' between <, , >
                        }*/
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                System.out.println(context.getStreamTokenizer());
                                context.callNextToken();

                            }
                        }
                    }
                    triangleCoord = false;
                    state = Trianglecontent.ENDING_CHEVRON;
                    if(!color)
                        coordNb++;

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
                                state = Trianglecontent.OPENING_CHEVRON;
                            else
                                list.add(String.valueOf(context.getNumberValue()));
                            nextToken = context.callNextToken();
                            if(context.isCurrentTokenAWord())
                            {
                                if(context.currentWord("finish"))
                                {
                                    state = Trianglecontent.FINISH;
                                }
                            }
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
                    context.callNextToken(); //skip ambient
                    if(context.isCurrentTokenAWord())
                    {
                        if(context.currentWord("rgb"))
                        {
                            state = Trianglecontent.OPENING_CHEVRON;
                        }
                    }
                    else
                        list.add(String.valueOf((char) context.getNumberValue()));
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
