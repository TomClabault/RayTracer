package povParser;

public enum Figure 
{
    //OBJECT_NAME in our code("object name in pov syntax")
    SPHERE("sphere"),
    RECTANGLE("box"),
    TRIANGLE("triangle");

    private String figure;

    Figure(String figure)
    {
        this.figure = figure;
    }

    public String getFigureName()
    {
        return this.figure;
    }

}