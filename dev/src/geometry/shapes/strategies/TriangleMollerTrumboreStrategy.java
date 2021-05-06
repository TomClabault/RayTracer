package geometry.shapes.strategies;

import geometry.shapes.Triangle;
import maths.Point;
import maths.Ray;
import maths.Vector;

/**
 * Implementation de: https://fr.wikipedia.org/wiki/Algorithme_d%27intersection_de_M%C3%B6ller%E2%80%93Trumbore
 * Legerement modifie pour satisfaire les besoins de notre ray tracer
 */
public class TriangleMollerTrumboreStrategy implements TriangleIntersectionStrategy
{
    private static double EPSILON = 0.0000001;

	public Double intersect(Triangle triangle, Ray ray, Point outInterPoint, Vector outNormalAtInter)
	{
        Point vertex0 = triangle.getA();
        Point vertex1 = triangle.getB();
        Point vertex2 = triangle.getC();
        
        Vector edge1 = new Vector(vertex0, vertex1);
        Vector edge2 = new Vector(vertex0, vertex2);
        Vector h;
        Vector s;
        Vector q;
        
        Point rayOrigin = ray.getOrigin();
        Vector rayDirection = ray.getDirection();
        
        double a, f, u, v;
        
        h = Vector.crossProduct(rayDirection, edge2);
        a = Vector.dotProduct(edge1, h);
        if (a > -EPSILON && a < EPSILON) {
            return null;// Le rayon est parallele au triangle.
        }
        f = 1.0 / a;
        
        s = new Vector(vertex0, rayOrigin);
        u = f * Vector.dotProduct(s, h);
        if (u < 0.0 || u > 1.0) {
            return null;
        }
        
        q = Vector.crossProduct(s, edge1);
        v = f * Vector.dotProduct(rayDirection, q);
        if (v < 0.0 || u + v > 1.0) {
            return null;
        }
        // On calcule t pour savoir ou le point d'intersection se situe sur la ligne.
        double t = f * Vector.dotProduct(edge2, q);
        if (t > EPSILON) // // Intersection avec le rayon
        {
        	if(outNormalAtInter != null)
        		outNormalAtInter.copyIn(triangle.getNormal(null));
            
        	if(outInterPoint != null)//Si on souhaite recuperer le point d'intersection
        		outInterPoint.copyIn(ray.determinePoint(t));
        	
            return t;
        }
        else // On a bien une intersection de droite, mais pas de rayon.
        {
            return null;
        }
    }
}
