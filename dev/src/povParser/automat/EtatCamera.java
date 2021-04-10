package povParser.automat;

enum CameraContent
{
    OPENING_BRACKET,
    ENDING_BRACKET,
    LOCATION, //position
    DIRECTION,
    
}

public class EtatCamera implements EtatToken
{
    @Override
    public void action(Automat context)
    {

    }
}
