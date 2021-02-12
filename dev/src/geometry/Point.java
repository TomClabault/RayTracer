package geometry;

public class Point 
{
    private float x,y,z;

    public Point(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public float getZ(){
        return this.z;
    }

    public void setX(float newX){
        this.x = newX;
    }

    public void setY(float newY){
        this.y = newY;
    }

    public void setZ(float newZ){
        this.z = newZ;
    }


}
