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

        Trianglecontent state = Trianglecontent.OPENING_BRACKET;

        while(state != Trianglecontent.OUTSIDE)
        {
            switch(state)
            {
                case OPENING_BRACKET:
                {
                    context.callNextToken();

                    state = Trianglecontent.OPENING_CHEVRON;
                    bracketNb++;
                    break;
                }

                case OPENING_CHEVRON:
                {
                    for(int i = 0; i < 3; i++)
                    {
                        context.callNextToken(); // la virgule
                        context.callNextToken();
                        list.add(String.valueOf(st.nval));
                    }
                    state = Trianglecontent.ENDING_CHEVRON;
                    coordNb++;

                    break;
                }
                case ENDING_CHEVRON:
                {
                    context.callNextToken(); //passe le chevron fermant
                    if(coordNb == 3)
                    {
                        nextToken = context.callNextToken();

                        if(context.isCurrentTokenAWord())
                        {
                            if(context.currentWord("pigment"))
                            {
                                System.out.println("pigment");
                                state = Trianglecontent.PIGMENT;
                            }
                            else if(context.currentWord("finish"))
                            {
                                System.out.println("finish");
                                state = Trianglecontent.FINISH;
                            }
                        }
                        else
                        {
                            state = Trianglecontent.ENDING_BRACKET;
                        }
                    }
                    else
                    {
                        state = Trianglecontent.OPENING_CHEVRON;
                    }
                    break;
                }
                case ENDING_BRACKET:
                {
                    context.callNextToken();
                    bracketNb--;
                    if(bracketNb == 0)
                    {
                        state = Trianglecontent.OUTSIDE;
                    }
                    break;
                }

                case PIGMENT:
                {
                    context.callNextToken(); //skip '{'
                    context.callNextToken(); //skip
                    System.exit
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
