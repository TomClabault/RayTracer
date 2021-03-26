package geometry.shapes;

import materials.Material;
import maths.Vector3D;
import geometry.ShapeTriangle;

import java.util.ArrayList;
import java.lang.Math;




public class Icosphere extends ShapeTriangle
{
    /*
    !!!!!!!! Merci de lire cette notice avant de lire les codes !!!!!!!!!!!!!!
    !!! DISCLAIMER / ATTENTION / AVERTISSEMENT !!!
    Les codes ci-dessous ne sont pas totalement le fruit de mon travail, une petite partie est issue de ces sites:
    La liste des points vient de: http://blog.coredumping.com/subdivision-of-icosahedrons/
    La construction initiale d'icoshpere vient de: http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html








     */


    //protected Vector3D BottomPoint,Centre,TopPoint,Atop,Btop,Ctop,Dtop,Etop,Arot,Brot,Crot,Drot,Erot;

    //Disclaimer: Les codes ci-dessous ne sont pas totalement le fruit de mon travail, une petite partie est issue de ces sites:
    //La liste des points vient de: http://blog.coredumping.com/subdivision-of-icosahedrons/
    //La construction initiale d'icoshpere vient de: http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html

    protected Vector3D depart,A,B,C,D,E,F,G,H,I,J,K,L;
    protected double t = ((1 + Math.sqrt(5))/2);
    protected double size;
    protected int subdivision;

    public Icosphere(Point depart, double size,int subdivision, Material material)
    {
        this.depart = depart;
        this.size = size;
        this.subdivision = subdivision;

        super.material = material;

        double xdepart = this.depart.getX();
        double ydepart = this.depart.getY();
        double zdepart = this.depart.getZ();

        //On va construire des Points
        //ArrayList<Vector3D> listePoints = new ArrayList<Vector3D>();
        this.A = new Vector3D(-size + xdepart, t*size + ydepart,0 + zdepart);
        this.B = new Vector3D(size + xdepart,t*size + ydepart,0 + zdepart);
        this.C = new Vector3D(-size + xdepart,-t*size + ydepart,0 + zdepart);
        this.D = new Vector3D(size + xdepart,-t*size + ydepart,0 + zdepart);

        this.E = new Vector3D(0 + xdepart,-size + ydepart,t*size + zdepart);
        this.F = new Vector3D(0 + xdepart,size + ydepart,t*size + zdepart);
        this.G = new Vector3D(0 + xdepart,-size + ydepart,-t*size + zdepart);
        this.H = new Vector3D(0 + xdepart,size + ydepart,-t*size + zdepart);

        this.I = new Vector3D(t*size + xdepart,0 + ydepart,-size + zdepart);
        this.J = new Vector3D(t*size + xdepart,0 + ydepart,size + zdepart);
        this.K = new Vector3D(-t*size + xdepart,0 + ydepart,-size + zdepart);
        this.L = new Vector3D(-t*size + xdepart,0 + ydepart,size + zdepart);


        //On va construire des triangles avec les points
        Triangle tr1 = new Triangle(J,F,E, material);
        Triangle tr2 = new Triangle(F,L,E, material);
        Triangle tr3 = new Triangle(J,E,D, material);
        Triangle tr4 = new Triangle(F,J,B, material);
        Triangle tr5 = new Triangle(B,J,I, material);
        Triangle tr6 = new Triangle(J,D,I, material);
        Triangle tr7 = new Triangle(B,I,H, material);
        Triangle tr8 = new Triangle(B,H,A, material);
        Triangle tr9 = new Triangle(A,F,B, material);
        Triangle tr10 = new Triangle(A,L,F, material);
        Triangle tr11 = new Triangle(L,C,E, material);
        Triangle tr12 = new Triangle(K,L,C, material);
        Triangle tr13 = new Triangle(A,K,L, material);
        Triangle tr14 = new Triangle(A,H,K, material);
        Triangle tr15 = new Triangle(H,G,K, material);
        Triangle tr16 = new Triangle(K,G,C, material);
        Triangle tr17 = new Triangle(C,D,E, material);
        Triangle tr18 = new Triangle(H,I,G, material);
        Triangle tr19 = new Triangle(I,D,G, material);
        Triangle tr20 = new Triangle(G,D,C, material);

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
        super.listeTriangle.add(tr9);
        super.listeTriangle.add(tr10);
        super.listeTriangle.add(tr11);
        super.listeTriangle.add(tr12);
        super.listeTriangle.add(tr13);
        super.listeTriangle.add(tr14);
        super.listeTriangle.add(tr15);
        super.listeTriangle.add(tr16);
        super.listeTriangle.add(tr17);
        super.listeTriangle.add(tr18);
        super.listeTriangle.add(tr19);
        super.listeTriangle.add(tr20);





        for (int itr = 0 ; itr < this.subdivision; itr++ )
        {
            int listsize = listeTriangle.size();
            for (int i = 0; i < listsize; i++)
            {
            Point A = listeTriangle.get(i).getA();
            Point B = listeTriangle.get(i).getB();
            Point C = listeTriangle.get(i).getC();

            Point AB = Point.add(A,B);
            Point BC = Point.add(B,C);
            Point CA = Point.add(C,A);

            Point ABmid = Point.scalarMul(0.5, AB);
            Point BCmid = Point.scalarMul(0.5, BC);
            Point CAmid = Point.scalarMul(0.5, CA);


            Triangle tri = new Triangle(ABmid, BCmid, CAmid, material);

            super.listeTriangle.add(tri);


            }
        }





    }

    protected void buildIcosphere()
    {
        /*
        //On va construire des Points
        //ArrayList<Vector3D> listePoints = new ArrayList<Vector3D>();
        Vector3D A = new Vector3D(-size, t*size,0);
        Vector3D B = new Vector3D(size,t*size,0);
        Vector3D C = new Vector3D(-size,-t*size,0);
        Vector3D D = new Vector3D(size,-t*size,0);

        Vector3D E = new Vector3D(0,-size,t*size);
        Vector3D F = new Vector3D(0,size,t*size);
        Vector3D G = new Vector3D(0,-size,-t*size);
        Vector3D H = new Vector3D(0,size,-t*size);

        Vector3D I = new Vector3D(t*size,0,-size);
        Vector3D J = new Vector3D(t*size,0,size);
        Vector3D K = new Vector3D(-t*size,0,-size);
        Vector3D L = new Vector3D(-t*size,0,size);
        */



        //On va construire des triangles avec les points
        Triangle tr1 = new Triangle(J,F,E, material);
        Triangle tr2 = new Triangle(F,L,E, material);
        Triangle tr3 = new Triangle(J,E,D, material);
        Triangle tr4 = new Triangle(F,J,B, material);
        Triangle tr5 = new Triangle(B,J,I, material);
        Triangle tr6 = new Triangle(J,D,I, material);
        Triangle tr7 = new Triangle(B,I,H, material);
        Triangle tr8 = new Triangle(B,H,A, material);
        Triangle tr9 = new Triangle(A,F,B, material);
        Triangle tr10 = new Triangle(A,L,F, material);
        Triangle tr11 = new Triangle(L,C,E, material);
        Triangle tr12 = new Triangle(K,L,C, material);
        Triangle tr13 = new Triangle(A,K,L, material);
        Triangle tr14 = new Triangle(A,H,K, material);
        Triangle tr15 = new Triangle(H,G,K, material);
        Triangle tr16 = new Triangle(K,G,C, material);
        Triangle tr17 = new Triangle(C,D,E, material);
        Triangle tr18 = new Triangle(H,I,G, material);
        Triangle tr19 = new Triangle(I,D,G, material);
        Triangle tr20 = new Triangle(G,D,C, material);

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
        super.listeTriangle.add(tr9);
        super.listeTriangle.add(tr10);
        super.listeTriangle.add(tr11);
        super.listeTriangle.add(tr12);
        super.listeTriangle.add(tr13);
        super.listeTriangle.add(tr14);
        super.listeTriangle.add(tr15);
        super.listeTriangle.add(tr16);
        super.listeTriangle.add(tr17);
        super.listeTriangle.add(tr18);
        super.listeTriangle.add(tr19);
        super.listeTriangle.add(tr20);






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
