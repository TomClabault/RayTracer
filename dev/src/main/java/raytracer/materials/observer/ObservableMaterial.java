package raytracer.materials.observer;

public interface ObservableMaterial 
{
	public void addListener(MaterialObserver listener);
	
	public void removeListener(MaterialObserver listener);
}
