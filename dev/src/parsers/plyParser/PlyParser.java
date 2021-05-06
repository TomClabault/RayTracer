package parsers.plyParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

import exceptions.PlyParsingException;
import geometry.ArbitraryTriangleShape;
import geometry.shapes.Triangle;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import materials.Material;
import materials.MatteMaterial;
import maths.Point;
import maths.Vector;

public class PlyParser 
{
	private long nbVertices;
	private long nbTriangles;
	
	private ArrayList<Point> vertices;

	private Vector translateVector;
	private double shapeScale;
	private ArbitraryTriangleShape parsedShape;
	private Material material;
	
	public PlyParser()
	{
		this(new MatteMaterial(Color.rgb(0, 0, 0)), 1, new Vector(0, 0, 0));
	}
	
	/**
	 * @param shapeMaterial Materiau qui sera utilise pour 'texturer' les objets parse par cette instance de PlyParser
	 */
	public PlyParser(Material shapeMaterial, double shapeScale, Vector translateVector)
	{
		this.nbVertices = 0;
		this.nbTriangles = 0;
		
		this.vertices = new ArrayList<>();

		this.translateVector = translateVector;
		this.shapeScale = shapeScale;
		this.material = shapeMaterial;
		this.parsedShape = new ArbitraryTriangleShape(shapeMaterial);
	}
	
	public ArbitraryTriangleShape parsePly(File plyFile)
	{
		try 
		{
			InputStreamReader inputStream = new InputStreamReader(new FileInputStream(plyFile));
			StreamTokenizer tokenizer = new StreamTokenizer(new BufferedReader(inputStream));
			tokenizer.wordChars('_', '_');
			
			
			
			parseHeader(tokenizer);
			parseVertices(tokenizer);
			parseTriangles(tokenizer);
			
			return this.parsedShape;//A ce stade, la shape contient tous les triangles du fichier ply
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			
			Platform.exit();
			System.exit(0);
		}
		
		return null;
	}
	
	private void parseHeader(StreamTokenizer tokenizer)
	{
		boolean headerEnd = false;
		
		try
		{
			while(!headerEnd)
			{
				tokenizer.nextToken();
				
				if(tokenizer.ttype == StreamTokenizer.TT_EOF)
					throw new IllegalArgumentException("Le fichier ply choisi n'est pas valide. end_header introuvable.");
				
				if(tokenizer.ttype == StreamTokenizer.TT_WORD)
				{
					if(tokenizer.sval.equals("element"))
					{
						tokenizer.nextToken();
						if(tokenizer.sval.equals("vertex"))
						{
							tokenizer.nextToken();
							this.nbVertices = (long)tokenizer.nval;
						}
						else if(tokenizer.sval.equals("face"))
						{
							tokenizer.nextToken();
							this.nbTriangles = (long)tokenizer.nval;
						}
						else
							throw new RuntimeException(String.format("Element de syntaxe non supporte: 'element %s' ligne %d", tokenizer.sval, tokenizer.lineno()));
					}
					else if(tokenizer.sval.equals("end_header"))
					{
						tokenizer.nextToken();//Skip du end_header
						headerEnd = true;
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Lorsqu'un token de la forme XXXe-05 est trouvee, cette methode permet de parser l'exposant 'e-05' et de calculer la valeur du nombre a partir de
	 * la mantisse 'XXX' passee en argument
	 * 
	 * @param tokenizer Le stream tokenizer courant. Le token courant doit etre l'exposant e-05
	 * @param mantiss Dans un nombre de la forme XXXe-05, 'mantiss' represente le nombre XXX
	 * 
	 * @return La mantisse multipliee par l'exposant. En d'autre terme, la valeur du nombre represente par une chaîne de caractere de la forme 'XXXe-05'
	 */
	private double attemptToParseScientificNotation(StreamTokenizer tokenizer, double mantiss) throws PlyParsingException, IOException
	{
		String exposantString = tokenizer.sval;
		
		double power = 0;
		
		if(exposantString.indexOf('-') != -1)
			power = Double.parseDouble(exposantString.substring(exposantString.indexOf('-')));
		else
			throw new PlyParsingException(String.format("Exposant incorrect : %s | Seuls les exposants negatifs sont supportes", tokenizer));
		
		try { tokenizer.nextToken();} //On skip l'exposant scientifique qu'on vient de parser
		catch (IOException e) { throw e; }
		
		return mantiss * Math.pow(10, power);
	}
	
	private double parseDoubleNumber(StreamTokenizer tokenizer) throws PlyParsingException, IOException
	{
		double mantiss = tokenizer.nval;//Represente la mantisse dans le cas d'un nombre de la forme XXXe-08 et le nombre lui meme s'il n'y a pas d'exposant
		tokenizer.nextToken();
		
		try 
		{
			if(tokenizer.ttype == StreamTokenizer.TT_NUMBER)//Le nombre n'est pas ecrit en notation scientifique
				return mantiss;
			else if(tokenizer.ttype == StreamTokenizer.TT_WORD)//Il y a une notation scientifique apres la mantisse
				return attemptToParseScientificNotation(tokenizer, mantiss);
			else
				throw new PlyParsingException("Fin de fichier innatendue.");
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}
	
	private void parseVertices(StreamTokenizer tokenizer)
	{
		try
		{
			for(int vertex = 0; vertex < this.nbVertices; vertex++)
			{
				double[] coords = new double[3];

				for(int coord = 0; coord < 3; coord++)
				{
					if(tokenizer.ttype == StreamTokenizer.TT_WORD)
						throw new PlyParsingException(String.format("Erreur durant le parsing des vertices. Token innatendu: %s", tokenizer));
					else if(tokenizer.ttype == StreamTokenizer.TT_EOF)
						throw new PlyParsingException("Erreur durant le parsing des vertices. Fin de fichier atteinte. Nombre de vertex incorrect.");
					else if(tokenizer.ttype == StreamTokenizer.TT_NUMBER)
						coords[coord] = parseDoubleNumber(tokenizer);
					///L'appel a next token est effectue dans la ligne ci-dessus
					///pas besoin d'un nouvel appel pour passer au token suivant
				}
				
				Point verticesCoords = new Point(coords[0] * this.shapeScale, coords[1] * this.shapeScale, coords[2] * this.shapeScale);
				this.vertices.add(Point.translateMul(verticesCoords, translateVector, 1));
			}
		}
		catch(PlyParsingException e)
		{
			System.out.println(e.getMessage());
			
			Platform.exit();
			System.exit(0);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void parseTriangles(StreamTokenizer tokenizer)
	{
		try
		{
			for(int triangle = 0; triangle < this.nbTriangles; triangle++)
			{
				int[] pointsIndex = new int[3];

				tokenizer.nextToken();//Skip du nombre de point en debut de ligne, on sait qu'on parse des triangles, le parser
				//ne supporte pas autre chose
				for(int coord = 0; coord < 3; coord++)
				{
					if(tokenizer.ttype == StreamTokenizer.TT_WORD)
						throw new PlyParsingException(String.format("Erreur durant le parsing des triangles. Token innatendu: %s", tokenizer));
					else if(tokenizer.ttype == StreamTokenizer.TT_EOF)
						throw new PlyParsingException("Erreur durant le parsing des triangles. Fin de fichier atteinte. Nombre de triangles incorrect.");
					else if(tokenizer.ttype == StreamTokenizer.TT_NUMBER)
						pointsIndex[coord] = (int)tokenizer.nval;
					
					tokenizer.nextToken();
				}
				
				this.parsedShape.addTriangle(new Triangle(
						this.vertices.get(pointsIndex[0]), 
						this.vertices.get(pointsIndex[1]), 
						this.vertices.get(pointsIndex[2]), 
						this.material));
			}
		}
		catch(PlyParsingException e)
		{
			System.out.println(e.getMessage());
			
			Platform.exit();
			System.exit(0);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
