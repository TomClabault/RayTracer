package povParser.automat;

import java.io.StreamTokenizer;
import java.util.ArrayList;

enum SpherePlanecontent
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

public abstract class EtatSpherePlane implements EtatToken
{
    protected abstract void createInstance(ArrayList<String> list);
    @Override
    public void action(Automat context)
    {
        ArrayList<String> list = new ArrayList<>();

        StreamTokenizer st = context.getStreamTokenizer();
        context.callNextToken();
        int bracketNb = 0;
        boolean sphereCoord = false;

        SpherePlanecontent state = SpherePlanecontent.OPENING_BRACKET;

        while(state != SpherePlanecontent.OUTSIDE)
        {
            switch(state)
            {
                case OPENING_BRACKET:
                {
                    context.callNextToken();

                    state = SpherePlanecontent.OPENING_CHEVRON;
                    bracketNb++;
                    break;
                }
                case ENDING_BRACKET:
                {
                    bracketNb--;
                    if(bracketNb == 0)
                    {
                        state = SpherePlanecontent.OUTSIDE;
                    }
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
                    state = SpherePlanecontent.ENDING_CHEVRON;
                    sphereCoord = true;
                    break;
                }
                case ENDING_CHEVRON:
                {
                    context.callNextToken(); //skip le chevron fermant
                    if(context.isCurrentTokenAWord())
                    {
                        if(context.currentWord("finish"))
                        {
                            state = SpherePlanecontent.FINISH;
                        }
                        else if(context.currentWord("pigment"))
                        {
                            state = SpherePlanecontent.PIGMENT;
                        }
                    }
                    else if (sphereCoord)
                    {
                        context.callNextToken(); //skip la virgule
                        list.add(String.valueOf(st.nval));
                        sphereCoord = false;
                        state = SpherePlanecontent.ENDING_BRACKET;
                    }
                    break;
                }
                case PIGMENT:
                {

                    break;
                }
            }
        }
        createInstance(list);
    }
}
