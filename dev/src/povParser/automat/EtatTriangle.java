package povParser.automat;

import materials.Material;

import java.io.StreamTokenizer;
import java.util.ArrayList;

enum Trianglecontent
{
    OPENING_BRACKET,
    ENDING_BRACKET,
    OPENING_CHEVRON,
    ENDING_CHEVRON,
    OUTSIDE,
    ATTRIBUTE;
}

public class EtatTriangle extends EtatUtil implements EtatToken
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
                        state = Trianglecontent.ATTRIBUTE;
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
                        state = Trianglecontent.ATTRIBUTE;
                    }
                    break;
                }


                case ATTRIBUTE:
                {
                    Material material = super.parseAttributes(context);
                    state = Trianglecontent.OUTSIDE;
                    break;
                }
            }
        }
        System.out.println(list);
    }
}
