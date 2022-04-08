module raytracer {
	exports raytracer.gui;

	opens raytracer.gui to javafx.fxml;
	opens raytracer.gui.materialChooser to org.jfxtras.styles.jmetro;
	
	requires javafx.base;
	requires javafx.fxml;
	requires transitive javafx.controls;
	requires transitive javafx.graphics;
//	requires org.jfxtras.styles.jmetro;
	requires java.desktop;
	requires javafx.swing;
}