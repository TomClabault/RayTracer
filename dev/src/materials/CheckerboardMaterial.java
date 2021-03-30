package materials;

/*
 * 
 */
public class CheckerboardMaterial extends Material
{
	public CheckerboardMaterial(Material baseAspect)
	{
		super(baseAspect.getColor(), baseAspect.getDiffuseCoeff(), baseAspect.getReflectiveCoeff(), baseAspect.getSpecularCoeff(), baseAspect.getShininess(), Material.CHECKERBOARD);
	}
}
