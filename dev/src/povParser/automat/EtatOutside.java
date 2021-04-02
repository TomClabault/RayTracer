package povParser.automat;

public class EtatOutside implements EtatToken
{
    @Override
    public void action(Automat context)
    {
        context.callNextToken();
    }
}
