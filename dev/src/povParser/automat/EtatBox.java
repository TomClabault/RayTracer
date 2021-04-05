package povParser.automat;

import java.io.StreamTokenizer;
import java.util.ArrayList;

enum Boxcontent
{
    OPENING_BRACKET,
    ENDING_BRACKET,
    OPENING_CHEVRON,
    ENDING_CHEVRON,
    OUTSIDE,
}

public class EtatBox implements EtatToken
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

        Boxcontent state = Boxcontent.OPENING_BRACKET;

        while(state != Boxcontent.OUTSIDE)
        {
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
                        state = Boxcontent.ENDING_BRACKET;
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
                    bracketNb--;
                    if(bracketNb == 0)
                    {
                        state = Boxcontent.OUTSIDE;
                    }
                    break;
                }
                case OUTSIDE:
                {
                    state = Boxcontent.OUTSIDE;
                    break;
                }
            }
        }
        System.out.println(list);
    }
}
