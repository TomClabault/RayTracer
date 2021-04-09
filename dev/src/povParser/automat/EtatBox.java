package povParser.automat;

import materials.Material;

import java.io.StreamTokenizer;
import java.util.ArrayList;

enum Boxcontent
{
    OPENING_BRACKET,
    ENDING_BRACKET,
    OPENING_CHEVRON,
    ENDING_CHEVRON,
    OUTSIDE,
    ATTRIBUTE,
}

public class EtatBox extends EtatUtil implements EtatToken
{
    @Override
    public void action(Automat context)
    {
        int nextToken = context.callNextToken();
        ArrayList<String> list = new ArrayList<>();

        StreamTokenizer st = context.getStreamTokenizer();
        context.callNextToken();
        int bracketNb = 0;
        int coordNb = 0;
        Material material = null;

        Boxcontent state = Boxcontent.OPENING_BRACKET;

        while(state != Boxcontent.OUTSIDE)
        {
            System.out.println("STATE IN BOX: " + state);
            switch (state)
            {
                case OPENING_BRACKET:
                {
                    context.callNextToken();

                    state = Boxcontent.OPENING_CHEVRON;
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
                    state = Boxcontent.ENDING_CHEVRON;
                    coordNb++;

                    break;
                }
                case ENDING_CHEVRON:
                {
                    context.callNextToken(); //passe le chevron fermant
                    if(coordNb == 2)
                    {
                        state = Boxcontent.ATTRIBUTE;
                    }
                    else
                    {
                        state = Boxcontent.OPENING_CHEVRON;
                    }
                    break;
                }
                case ENDING_BRACKET:
                {
                    context.callNextToken();
                    if(context.isCurrentTokenAWord())
                    {
                        state = Boxcontent.ATTRIBUTE;
                    }
                    bracketNb--;
                    if(bracketNb == 0)
                    {
                        state = Boxcontent.OUTSIDE;
                    }
                    break;
                }

                case ATTRIBUTE:
                {
                    material = super.parseAttributes(context);
                    state = Boxcontent.OUTSIDE;
                    break;
                }
            }
        }
        System.out.println(material);
        System.out.println(list);
    }
}
