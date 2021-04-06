package povParser.automat;

import materials.Material;

import java.io.StreamTokenizer;
import java.util.ArrayList;

enum SpherePlanecontent
{
    OPENING_BRACKET,
    ENDING_BRACKET,
    OPENING_CHEVRON,
    ENDING_CHEVRON,
    OUTSIDE,
    ATTRIBUTE
}

public abstract class EtatSpherePlane extends EtatUtil implements EtatToken
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
        boolean color = false;
        boolean coord = false;
        int nextToken = 0;
        Material material = null;

        SpherePlanecontent state = SpherePlanecontent.OPENING_BRACKET;

        while(state != SpherePlanecontent.OUTSIDE)
        {
            switch(state) {
                case OPENING_BRACKET: {
                    context.callNextToken();

                    state = SpherePlanecontent.OPENING_CHEVRON;
                    bracketNb++;
                    coord = true;
                    break;
                }
                case ENDING_BRACKET: {

                    nextToken = context.callNextToken(); //skip '}'
                    if (context.isCurrentTokenAWord()) {
                        state = SpherePlanecontent.ATTRIBUTE;
                    } else {
                        bracketNb--;
                        if (bracketNb == 0) {
                            state = SpherePlanecontent.OUTSIDE;
                        }
                    }
                    break;
                }
                case OPENING_CHEVRON: {
                    if (coord) {
                        for (int i = 0; i < 3; i++) {
                            context.callNextToken(); // la virgule
                            context.callNextToken();
                            list.add(String.valueOf(st.nval));
                        }
                    }
                    //coord = false;
                    state = SpherePlanecontent.ENDING_CHEVRON;
                    break;
                }
                case ENDING_CHEVRON: {
                    nextToken = context.callNextToken(); //skip le chevron fermant
                    if (context.isCurrentTokenAWord())
                    {
                        state = SpherePlanecontent.ATTRIBUTE;
                    }
                    else
                        {
                        if ((char) nextToken == ',') {
                            context.callNextToken();
                            list.add(String.valueOf(context.getNumberValue()));
                            context.callNextToken();
                            if(context.isCurrentTokenAWord())
                            {
                                state = SpherePlanecontent.ATTRIBUTE;
                            }
                            else
                                state = SpherePlanecontent.ENDING_BRACKET;
                        } else if ((char) nextToken == '}') {
                            state = SpherePlanecontent.ENDING_BRACKET;
                        }
                    }
                    break;
                }
                case ATTRIBUTE:
                {
                    material = super.parseAttributes(context);
                    state = SpherePlanecontent.OUTSIDE;
                    break;
                }
            }
        }
        createInstance(list);
    }
}
