package raytracer.benchmarks;

import javafx.scene.paint.Color;
import raytracer.accelerationStructures.BoundingVolume;
import raytracer.geometry.Shape;
import raytracer.geometry.shapes.Plane;
import raytracer.geometry.shapes.Sphere;
import raytracer.geometry.shapes.Triangle;
import raytracer.geometry.shapes.strategies.TriangleMollerTrumboreStrategy;
import raytracer.geometry.shapes.strategies.TriangleNaiveStrategy;
import raytracer.materials.MatteMaterial;
import raytracer.maths.Point;
import raytracer.maths.Ray;
import raytracer.maths.Vector;

public class Main 
{
	private static final int NB_INTERSECTIONS = 10000000;

	public static void main(String[] args) 
	{
		String[] titles = new String[] {"Ray-triangle [naive] intersection", "Ray-triangle [Moller-Trumbore] intersection", "Ray-sphere intersection", "Ray-plane intersection", "Ray-bounding volume intersection"};
		String[] primitives = new String[] {"triangleNaive", "triangleMoller", "sphere", "plane", "boundingVolume"};
		
		int longestTitleLength = 0;
		for(int i = 0; i < titles.length; i++)
			if(titles[i].length() > longestTitleLength)
				longestTitleLength = titles[i].length();
		
		Ray[] rays = new Ray[NB_INTERSECTIONS];
		for(int i = 0; i < NB_INTERSECTIONS; i++)
			rays[i] = new Ray(new Point(Math.random(), Math.random(), Math.random()), new Vector(Math.random(), Math.random(), Math.random()));
		
		System.out.println(String.format("---------- Starting benchmark ----------"));
		
		for(int i = 0; i < titles.length; i++)
		{
			System.out.printf("%s%s : ", titles[i], printSpaces(titles, i, longestTitleLength));
			System.out.printf("%.3fM/s\n", intersectionTest(rays, primitives[i]));
		}
		
		System.out.println("------------ Benchmark end -------------");
	}
	
	
	
	
	
	private static String printSpaces(String[] titles, int index, int longestTitle)
	{
		if(longestTitle - titles[index].length() == 0)
			return "";
		else
			return new String(new char[longestTitle - titles[index].length()]).replace('\0', ' ');
	}
	
	private static double intersectionTest(Ray[] rays, String primitive)
	{
		long primitiveType = 0;
		
		Shape[] shape = null;
		
		if(primitive.equals("triangleNaive"))
		{
			shape = new Triangle[NB_INTERSECTIONS];
			primitiveType = 1;
		}
		else if(primitive.equals("triangleMoller"))
		{
			shape = new Triangle[NB_INTERSECTIONS];
			primitiveType = 2;
		}
		else if(primitive.equals("sphere"))
		{
			shape = new Sphere[NB_INTERSECTIONS];
			primitiveType = 3;
		}
		else if(primitive.equals("plane"))
		{
			shape = new Plane[NB_INTERSECTIONS];
			primitiveType = 4;
		}
		else if(primitive.equals("boundingVolume"))
		{
			return boundingVolumeInterTest();
		}
		
		if(primitiveType == 0)
			return 0;
		
		for(int i = 0; i < NB_INTERSECTIONS; i++)
		{
			shape[i] = primitiveType == 1 ? new Triangle(new Point(Math.random(), Math.random(), Math.random()), new Point(Math.random(), Math.random(), Math.random()), new Point(Math.random(), Math.random(), Math.random()), new MatteMaterial(Color.RED), new TriangleNaiveStrategy())
					:  primitiveType == 2 ? new Triangle(new Point(Math.random(), Math.random(), Math.random()), new Point(Math.random(), Math.random(), Math.random()), new Point(Math.random(), Math.random(), Math.random()), new MatteMaterial(Color.RED), new TriangleMollerTrumboreStrategy())
					:  primitiveType == 3 ? new Sphere(new Point(Math.random(), Math.random(), Math.random()), Math.random(), new MatteMaterial(Color.RED))
					:  primitiveType == 4 ? new Plane(new Vector(Math.random(), Math.random(), Math.random()), Math.random(), new MatteMaterial(Color.RED))
					:  null;
		}
		
		/* Warmup */
		for(int j = 0; j < 10; j++)
			for(int i = 0; i < NB_INTERSECTIONS; i++)
				shape[i].intersect(rays[i], null, null);
		/* Warmup */
		
		long start = System.nanoTime();
		for(int j = 0; j < 10; j++)
			for(int i = 0; i < NB_INTERSECTIONS; i++)
				shape[i].intersect(rays[i], null, null);
		long end = System.nanoTime();
		
		return ((double)(NB_INTERSECTIONS*10) / ((double)(end - start)/1000000000.0))/1000000;
	}
	
	private static double boundingVolumeInterTest()
	{
		int localIntersectionCount = NB_INTERSECTIONS / 100;
		
		Ray[] rays = new Ray[localIntersectionCount];
		Triangle[] triangles = new Triangle[localIntersectionCount];
		
		BoundingVolume[] volumes = new BoundingVolume[localIntersectionCount];
		
		for(int i = 0; i < localIntersectionCount; i++)
		{
			rays[i] = new Ray(new Point(Math.random(), Math.random(), Math.random()), new Vector(Math.random(), Math.random(), Math.random()));
			triangles[i] = new Triangle(new Point(Math.random(), Math.random(), Math.random()), new Point(Math.random(), Math.random(), Math.random()), new Point(Math.random(), Math.random(), Math.random()), new MatteMaterial(Color.RED));
			
			volumes[i] = triangles[i].getBoundingVolume();
		}
		
		/*Warmup*/
		for(int j = 0; j < 1000; j++)
			for(int i = 0; i < localIntersectionCount; i++)
				volumes[i].intersect(rays[i]);
		/*Warmup*/
		
		long start = System.nanoTime();
		for(int j = 0; j < 1000; j++)
			for(int i = 0; i < localIntersectionCount; i++)
				volumes[i].intersect(rays[i]);
		long end = System.nanoTime();
		
		return ((double)(localIntersectionCount*1000) / ((double)(end - start)/1000000000.0))/1000000;
	}
}
