package geometry.materials;

import javafx.scene.paint.Color;

public class TransparentMaterial extends Material
{
    private double transmission;
    private double indiceRef;

	public TransparentMaterial(double transmission, double indiceRef)
	{
		super(Color.WHITE,0.5 , 0.75, 0.2, 0,  1);
        this.transmission = transmission;
        this.indiceRef = indiceRef;
	}

    public double getTransmission() {
        return this.transmission;
    }

    public double getIndiceRef() {
        return this.indiceRef;
    }
}
