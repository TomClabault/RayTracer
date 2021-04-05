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
                    coord = true;
                    break;
                }
                case ENDING_BRACKET:
                {

                    nextToken = context.callNextToken(); //skip '}'
                    state = super.parsePropertryAndGetState(context);

                    if(state == null)
                    {
                        bracketNb--;
                        if(bracketNb == 0)
                        {
                            state = SpherePlanecontent.OUTSIDE;
                        }
                    }
                    break;
                }
                case OPENING_CHEVRON:
                {
                    if(coord)
                    {
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
                case ENDING_CHEVRON:
                {
                    nextToken = context.callNextToken(); //skip le chevron fermant
                    state  = super.parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        if((char)nextToken == ',')
                        {
                            context.callNextToken();
                            list.add(String.valueOf(context.getNumberValue()));
                            context.callNextToken();
                            state = super.parsePropertryAndGetState(context);
                            if(state == null)
                                state = SpherePlanecontent.ENDING_BRACKET;
                        }
                        else if((char) nextToken == '}')
                        {
                            state = SpherePlanecontent.ENDING_BRACKET;
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
                                state = SpherePlanecontent.OPENING_CHEVRON;
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
                                    state = SpherePlanecontent.FINISH;
                                }
                            }
                            else
                                state = SpherePlanecontent.ENDING_BRACKET;

                        }
                    }
                    break;
                }
                case FINISH:
                {
                    context.callNextToken(); //skip finish
                    context.callNextToken(); //skip '{'
                    state = super.parsePropertryAndGetState(context);
                    if (state == null)
                        state = SpherePlanecontent.ENDING_BRACKET;
                    break;
                }
                case SPECULAR:
                {
                    list.add("specular");
                    context.callNextToken(); //skip specular
                    list.add(String.valueOf(context.getNumberValue()));
                    context.callNextToken();
                    state = super.parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = SpherePlanecontent.ENDING_BRACKET;
                    }
                    break;
                }
                case DIFFUSE:
                {
                    list.add("diffuse");
                    context.callNextToken(); //skip diffuse
                    list.add(String.valueOf(context.getNumberValue()));
                    context.callNextToken();
                    state = super.parsePropertryAndGetState(context);
                    if(state == null)
                    {
                        state = SpherePlanecontent.ENDING_BRACKET;
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
                            state = SpherePlanecontent.OPENING_CHEVRON;
                        }
                    }
                    else
                    {
                        list.add(String.valueOf(context.getNumberValue()));
                    }

                    context.callNextToken();
                    state = super.parsePropertryAndGetState(context);
                    if(state == null)
                        state = SpherePlanecontent.ENDING_BRACKET;
                    break;
                }
            }
        }
        createInstance(list);
    }
}
