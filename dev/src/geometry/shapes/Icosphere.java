package geometry.shapes;

import materials.Material;
import maths.Point;
import maths.Vector;
import geometry.Shape;
import geometry.ArbitraryTriangleShape;

import java.util.ArrayList;



/*
	Les positions des points de l'icosphere de subdivision 1 viennent de: http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
*/
public class Icosphere extends ArbitraryTriangleShape implements Shape
{
	private Point centre;
    private double size;
    private int subdivision;

    public Icosphere(Point depart, double size,int subdivision, Material material)
    {
    	super(material);
    	
    	double t = ((1 + Math.sqrt(5))/2);
    	
        if (subdivision > 0)
            this.subdivision = subdivision;
        else
            throw new IllegalArgumentException("Subdivision has to be bigger than 0");

        this.centre = depart;
        this.size = size;


        Point A, B, C, D, E, F, G, H, I, J, K, L;
        
        A = Vector.normalizeP(new Point(-1 , t*1 ,0 ));
        B = Vector.normalizeP(new Point(1 ,t*1 ,0 ));
        C = Vector.normalizeP(new Point(-1 ,-t*1 ,0 ));
        D = Vector.normalizeP(new Point(1 ,-t*1 ,0 ));

        E = Vector.normalizeP(new Point(0 ,-1 ,t*1 ));
        F = Vector.normalizeP(new Point(0 ,1 ,t*1 ));
        G = Vector.normalizeP(new Point(0 ,-1 ,-t*1 ));
        H = Vector.normalizeP(new Point(0 ,1 ,-t*1 ));

        I = Vector.normalizeP(new Point(t*1 ,0 ,-1 ));
        J = Vector.normalizeP(new Point(t*1 ,0 ,1 ));
        K = Vector.normalizeP(new Point(-t*1 ,0 ,-1 ));
        L = Vector.normalizeP(new Point(-t*1 ,0 ,1 ));




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
        super.triangleList = new ArrayList<Triangle>();
        super.triangleList.add(tr1);
        super.triangleList.add(tr2);
        super.triangleList.add(tr3);
        super.triangleList.add(tr4);
        super.triangleList.add(tr5);
        super.triangleList.add(tr6);
        super.triangleList.add(tr7);
        super.triangleList.add(tr8);
        super.triangleList.add(tr9);
        super.triangleList.add(tr10);
        super.triangleList.add(tr11);
        super.triangleList.add(tr12);
        super.triangleList.add(tr13);
        super.triangleList.add(tr14);
        super.triangleList.add(tr15);
        super.triangleList.add(tr16);
        super.triangleList.add(tr17);
        super.triangleList.add(tr18);
        super.triangleList.add(tr19);
        super.triangleList.add(tr20);

        


        // on va boucler selon combien de subdivision souhaite
        for (int itr = 0 ; itr < this.subdivision - 1; itr++ )
        {
            int listsize = triangleList.size();
            for (int i = 0; i < listsize; i++)
            {
            // on recupere les points du triangle a chaque bouclage
            Point Atr = triangleList.get(i).getA();
            Point Btr = triangleList.get(i).getB();
            Point Ctr = triangleList.get(i).getC();

            // on trouve le milieu de chaque segment du triangle
            Point ABmid = Vector.normalizeP(Point.midPoint(Atr, Btr));
            Point BCmid = Vector.normalizeP(Point.midPoint(Btr, Ctr));
            Point CAmid = Vector.normalizeP(Point.midPoint(Ctr, Atr));
           
            // on fait des triangles dans un triangle
            Triangle trimid = new Triangle(ABmid, BCmid, CAmid, material);
            Triangle trileft = new Triangle(Atr, ABmid, CAmid, material);
            Triangle triright = new Triangle(CAmid, BCmid, Ctr, material);
            Triangle tribot = new Triangle(ABmid, Btr, BCmid, material);

            // on ajout ces triangles dans la liste des triangles
            super.triangleList.add(trimid);
            super.triangleList.add(trileft);
            super.triangleList.add(triright);
            super.triangleList.add(tribot);

            }
            //supprimer les triangles de la subdiv avant
            if(itr >= 1) 
            {
	            double subdivavant = 20 * Math.pow(4, itr - 1);
	            for(int j = 0; j < subdivavant ; j++)
	            {
	                super.triangleList.remove(0);
	            }
            }
                                  
            
        }
        //On va multiplier les triangles avec la taille souhaitee puis additioner avec le point de depart
        for(Triangle triangle:triangleList) 
        {
        	Point Atr = triangle.getA();
        	Point Btr = triangle.getB();
        	Point Ctr = triangle.getC();
        	
        	Atr = Point.scalarMul(Atr, size);
        	Btr = Point.scalarMul(Btr, size);
        	Ctr = Point.scalarMul(Ctr, size);
        	
        	Atr = Point.add(Atr, depart);
        	Btr = Point.add(Btr, depart);
        	Ctr = Point.add(Ctr, depart);
        	
        	triangle.setA(Atr);
        	triangle.setB(Btr);
        	triangle.setC(Ctr);
        	
        }
    }
    
    @Override
	public String toString()
	{
		return String.format("[Icosphere Shape] Centre: %s | Size: %.3f | Subdivisions: %d | Material: %s", this.centre, this.size, this.subdivision, this.material);
	}
}
