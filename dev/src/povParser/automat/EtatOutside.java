package povParser.automat;

import geometry.Shape;

public class EtatOutside implements EtatToken
{
    @Override
    public Shape action(Automat context)
    {
        context.callNextToken();
        return null;
    }
}
