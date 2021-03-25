package povParser;

public enum Finish
{
    AMBIENT("ambient"),
    DIFFUSE("diffuse"),
    REFLECTIVE("reflection");

    private String finish;

    Finish(String finish)
    {
        this.finish = finish;
    }

    public String getFinishName()
    {
        return this.finish;
    }

}
