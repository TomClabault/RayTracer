package povParser.automat;

import java.util.ArrayList;

public class EtatSphere extends EtatSpherePlane
{
    @Override
    protected void createInstance(ArrayList<String> list)
    {
        System.out.println(list);
    }
}