public class Shape {
    protected float height, width, length;


    public Shape(float height){
        this.height = height;
    }

    public Shape(float width, float length,float height){
        this.height = height;
        this.width = width;
        this.length = length;
    }

    public float getHeight(){
        return this.height;
    }

    public float getWidth(){
        return this.width;
    }

    public float getLength(){
        return this.length;
    }

    public float basisSurface() {
        return this.width * this.length;
    }








}
