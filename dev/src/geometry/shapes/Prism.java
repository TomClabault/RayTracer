package geometry.shapes;

import materials.Material;
import maths.Vector3D;
import geometry.ShapeUtil;

import java.util.ArrayList;

public class Prism extends ShapeUtil
{


    /*

    Imagine un prism

                         F
                        /\
                       / |\
                      /  | \
                     /   |  \
                    /    |   \
                  D/_____|____\C
                   |     |    |
                   |    E|    |
                   |    / \   |
                   |   /   \  |
                   |  /     \ |
                   | /       \|
                   |/_________\
                   A           B



                Prism couchee

                   E                   F
                   _____________________
                  /\                   /\
                 /  \                 /  \
                /    \               /    \
             A /______\_____________/D     \
                       \____________________\
                       B                    C




    */

	private Vector3D A,B,C,D,E,F;


    public Prism(Vector3D A, Vector3D B, Vector3D C, Vector3D D, Vector3D E, Vector3D F, Material material)
    {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.E = E;
        this.F = F;

        super. material = material;

        buildPrism();

    }

    public Prism(Vector3D depart, double height, double width, Material material)
    {
        this.A = depart;

        this.B = new Vector3D(this.A.getX() + width, this.A.getY(), this.A.getZ());
        this.C = new Vector3D(this.A.getX() + width, this.A.getY(), this.A.getZ() + width);
        this.D = new Vector3D(this.A.getX(), this.A.getY(), this.A.getZ() + width);
        this.E = new Vector3D(this.A.getX() + width/2, this.A.getY() + height, this.A.getZ());
        this.F = new Vector3D(this.A.getX() + width/2, this.A.getY() + height, this.A.getZ() + width);

<<<<<<< HEAD
        
        this.material = material;
=======
        super.material = material;
>>>>>>> origin/master

        buildPrism();
    }




    protected void buildPrism()
    {

        /*on va construire les 8 triangles*/
        Triangle tr1 = new Triangle(D,A,B, material);
        Triangle tr2 = new Triangle(D,B,C, material);
        Triangle tr3 = new Triangle(C,B,E, material);
        Triangle tr4 = new Triangle(C,E,F, material);
        Triangle tr5 = new Triangle(D,A,E, material);
        Triangle tr6 = new Triangle(D,E,F, material);
        Triangle tr7 = new Triangle(F,D,C, material);
        Triangle tr8 = new Triangle(E,B,A, material);

        /*on ajoute dans le array des triangles*/
        super.listeTriangle = new ArrayList<Triangle>();
        super.listeTriangle.add(tr1);
        super.listeTriangle.add(tr2);
        super.listeTriangle.add(tr3);
        super.listeTriangle.add(tr4);
        super.listeTriangle.add(tr5);
        super.listeTriangle.add(tr6);
        super.listeTriangle.add(tr7);
        super.listeTriangle.add(tr8);

    }

    /*
	 * @link{geometry.shapes.Shape#getUVCoords}
	 */
	@Override
	public Vector3D getUVCoords(Vector3D point)
	{
		return null;
	}
}

