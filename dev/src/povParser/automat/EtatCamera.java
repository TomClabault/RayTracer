package povParser.automat;

enum CameraContent
{
    OPENING_BRACKET,
    ENDING_BRACKET,
    LOCATION, //position
    DIRECTION,
    LOOK_AT,
    
}

public class EtatCamera implements EtatToken
{
    @Override
    public void action(Automat context)
    {

    }
}
