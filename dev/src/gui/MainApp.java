package gui;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import accelerationStructures.BVHAccelerationStructure;
import gui.threads.RefreshSimpleRenderThread;
import gui.threads.RenderTask;
import gui.toolbox.SimpleRenderToolbox;
import gui.toolbox.ToolboxWindow;
import gui.windows.ChooseRenderSettingsWindow;
import gui.windows.RenderWindow;
import gui.windows.RenderWindowOld;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import rayTracer.RayTracer;
import rayTracer.RayTracerSettings;
import scene.RayTracingScene;

/**
 * La classe contenant le Main qui gere la totalite de l'application
*/
public class MainApp extends Application {
    /**
     * La hauteur de la resolution de la fenetre du rendu definie par {@link gui.windows.ChooseRenderSettingsWindow}
    */
    public static int HEIGHT;
    /**
     * La largeur de la resolution de la fenetre du rendu definie par {@link gui.windows.ChooseRenderSettingsWindow}
    */
    public static int WIDTH;

    /**
     * Vaut true si le mode automatique est active
     *
     * Le mode automatique maximize la fenetre de rendu et etire le rendu, le rendu devient pixelise si la resolution du rendu est inferieur a la taille de la fenetre
     */
    public static boolean FULLSCREEN_MODE;

    /**
     * True si le ray tracer ne doit rendre qu'une image et arreter les calculs. False si le RayTracer doit rendre les images en temps
     * reel (autorise ainsi les mouvements de camera)
     */
    public static boolean SIMPLE_RENDER;
    
    /**
     * La methode main de java
     * @param args
     */
    public static void main(String[] args) {

        Application.launch(args);

    }
    
    /**
     * Contient la methode a Override de {@link javafx.application.Application}
     * Elle est executee dans le main
     * @param stage Le stage de la fenetre de rendu
     */
    public void start(Stage stage) 
    {
	   	stage.setOnCloseRequest(this::gracefulExit);

	   	
	   	
    	//TODO (tom) n'activer le comptage du nombre d'intersections que sur demande (une feature 'debug' en gros) parce que ça tape dans les perfs
    	//mine de rien
    	RayTracingScene rayTracingScene = MainUtil.getSceneFromFile(stage);
	   	//rayTracingScene = MainUtil.addSkybox(rayTracingScene);
	   	//rayTracingScene.setAccelerationStructure(new BVHAccelerationStructure(rayTracingScene.getSceneObjects(), 16));
	   	
        RayTracerSettings rayTracerSettings = new RayTracerSettings(Runtime.getRuntime().availableProcessors(), 4, 9, 4, 64);
	   	ChooseRenderSettingsWindow renderSettingsWindow = new ChooseRenderSettingsWindow(rayTracerSettings);
        renderSettingsWindow.execute();
        
	   	RayTracer rayTracer = new RayTracer(MainApp.WIDTH, MainApp.HEIGHT);
	   	
	   	//startRender(stage, rayTracer, rayTracingScene, rayTracerSettings);
    }
    
    private void gracefulExit(WindowEvent event)
    {
    	Platform.exit();
    	System.exit(0);
    }
    
    /*
     * Lance le rendu en fonction du ray tracer, des réglages et de la scène passés en argument.
     * Lance le rendu d'une seule image ou d'une rendu en temps réel en fonction de ce qu'a préalablement choisi
     * l'utilisateur  
     */
    private void startRender(Stage stage, RayTracer rayTracer, RayTracingScene rayTracingScene, RayTracerSettings rayTracerSettings)
    {
    	if(!MainApp.SIMPLE_RENDER)//On lance le rendu en temps reel s'il est voulu
        {
        	//TODO (tom) clean render window old
        	RenderWindowOld renderWindow = new RenderWindowOld(stage, rayTracer, rayTracingScene, rayTracerSettings);
        	renderWindow.execute();

        	ToolboxWindow toolbox = new ToolboxWindow(renderWindow.getStatPane(), renderWindow.getProgressBar(), rayTracerSettings, renderWindow.getWritableImage());
        	toolbox.execute();
        }
        else
        {
        	RenderWindow renderWindow = new RenderWindow(stage);
        	
        	RefreshSimpleRenderThread refreshRenderThread = new RefreshSimpleRenderThread(rayTracer, renderWindow.getPixelWriter(), PixelFormat.getIntArgbInstance(), renderWindow.getStatsPane());
        	refreshRenderThread.start();
        	
        	SimpleRenderToolbox saveRenderWindow = new SimpleRenderToolbox(renderWindow.getWritableImage(), renderWindow.getStatsPane());
        	
        	ExecutorService executorService = Executors.newFixedThreadPool(1);
        	RenderTask renderTask = new RenderTask(renderWindow.getPixelWriter(), PixelFormat.getIntArgbInstance(), rayTracer, rayTracingScene, rayTracerSettings);
        	executorService.submit(renderTask);
        	
        	renderTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() 
        	{
        		@Override
        		public void handle(WorkerStateEvent arg0) 
        		{
        			RenderWindowOld.drawImage(rayTracer.getRenderedPixels(), renderWindow.getPixelWriter(), PixelFormat.getIntArgbInstance());
        		}
			});
        }
    }
}
