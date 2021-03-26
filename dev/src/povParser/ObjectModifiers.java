package povParser;

public enum ObjectModifiers
{
    COLOR("pigment"), //toujours suivi de rgb
    MATERIAL("finish");

    private String modifier;

    ObjectModifiers(String modifier)
    {
        this.modifier = modifier;
    }

    public String getModifierName()
    {
        return this.modifier;
    }
}