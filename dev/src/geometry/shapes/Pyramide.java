package geometry.shapes;

import materials.Material;
import maths.Vector3D;
import geometry.ShapeUtil;

import java.util.ArrayList;


public class Pyramide extends ShapeUtil
{
    /*Imagine une pyramide ABCDE


                        E
                        /\
                       /  \
                      /    \
                     /      \
                    /        \
                   /          \
                  /            \
                 /              \
                /                \
             D / ________________ \ C
              / /                \ \
             /______________________\
            A                        B

    Cette pyramide sera construite avec 6 Triangles.

     */

	private Vector3D A, B, C, D, E;

    public Pyramide(Vector3D A, Vector3D B, Vector3D C, Vector3D D, Vector3D E, Material material) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.E = E;

        super.material = material;

        buildPyramide();
    }

    // Constructeur pour creer une pyramide equilaterale
    public Pyramide(Vector3D depart, double height, double width, Material material) {
        this.A = depart;

        this.C = new Vector3D(this.A.getX() + width, this.A.getY(), this.A.getZ() + width);
        this.D = new Vector3D(this.A.getX(), this.A.getY(), this.A.getZ() + width);
        this.B = new Vector3D(this.A.getX() + width, this.A.getY(), this.A.getZ());
        this.E = new Vector3D(this.A.getX() + width / 2, this.A.getY() + height, this.A.getZ() + width / 2);

        // b --> c , c --> d , d --> b


        super.material = material;

        buildPyramide();


    }


    protected void buildPyramide() {
        /*on va construire les 6 triangles*/
        Triangle tr1 = new Triangle(E, D, A, material);
        Triangle tr2 = new Triangle(E, A, B, material);
        Triangle tr3 = new Triangle(E, B, C, material);
        Triangle tr4 = new Triangle(E, C, D, material);
        Triangle tr5 = new Triangle(D, A, B, material);
        Triangle tr6 = new Triangle(D, B, C, material);

        /*on va ajouter les triangles dans la listeTriangle*/
        super.listeTriangle = new ArrayList<Triangle>();
        super.listeTriangle.add(tr1);
        super.listeTriangle.add(tr2);
        super.listeTriangle.add(tr3);
        super.listeTriangle.add(tr4);
        super.listeTriangle.add(tr5);
        super.listeTriangle.add(tr6);

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