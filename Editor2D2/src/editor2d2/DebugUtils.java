package editor2d2;

import java.awt.image.BufferedImage;

import editor2d2.model.app.Controller;
import editor2d2.model.project.Project;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.Scene;
import editor2d2.modules.data.layer.DataLayer;
import editor2d2.modules.image.asset.Image;
import editor2d2.modules.image.layer.TileLayer;
import editor2d2.modules.image.placeable.Tile;
import editor2d2.modules.object.layer.InstanceLayer;

/**
 * This class is set to contain utility methods for running debugging tests
 * and generating various testing data.
 * 
 * The class can be commented out to see if any class is still implementing
 * its methods, so that all references can be easily scrubbed from the
 * final release.
 * @author User
 *
 */
public class DebugUtils {
	
	/**
	 * Placeholder image for grass ground.
	 */
	public static BufferedImage IMG_GRASS;
	
	/**
	 * Placeholder image for dirt ground
	 */
	public static BufferedImage IMG_DIRT;
	
		// Don't instantiate
	private DebugUtils() { }
	

	public static void controllerDebugSetup(Controller controller) {
		
			// Project
		Project dummy = new Project();
		dummy.setName("testproj :/");
		
				// Scene1
			Scene scene1 = new Scene("small scene");
			scene1.setDimensions(200, 200);
			
					// Camera
				Camera cam1 = new Camera();
				cam1.setPortDimensions(358, 210);
				cam1.shift(0, 0, 0);
			
			scene1.setCamera(cam1);
			
					// Layer
				TileLayer tl1 = new TileLayer(scene1, 32);
				tl1.setName("tiles");
				InstanceLayer ol1 = new InstanceLayer(scene1);
				ol1.setName("objs");
				DataLayer dl1 = new DataLayer(scene1, 32);
				dl1.setName("weapon.dat");
				
					// Tiles
				int d = 0;
				for( int x = 0; x < 6; x++ )
				{
					for( int y = 0; y < 6; y++ )
					{
						tl1.place(x, y, new Tile());
						d++;
					}
				}
				
			scene1.addLayer(tl1);
			scene1.addLayer(ol1);
			scene1.addLayer(dl1);
			
				// Scene2
			Scene scene2 = new Scene("BIG scene O_O");
			scene2.setDimensions(500, 500);
			
					// Camera
				Camera cam2 = new Camera();
				cam2.setPortDimensions(640, 480);
			
			scene2.setCamera(cam2);
			
					// Layer
				TileLayer tl2 = new TileLayer(scene2, 64);
			
			scene2.addLayer(tl2);
		
		dummy.addScene(scene1);
		dummy.addScene(scene2);
		
			// Project assets
		Image img = new Image();
		img.setName("grass");
		img.setImage(Application.resources.getGraphic("test-grass"));
		dummy.addAsset(img, dummy.getRootFolder());
		
		img = new Image();
		img.setName("dirt");
		img.setImage(Application.resources.getGraphic("test-dirt"));
		dummy.addAsset(img, dummy.getRootFolder());
		
		controller.openProject(dummy);
	}
	
	/**
	 * Prints a message in console using System.out.println containing
	 * a reference to the caller. <br /> <br />
	 * 
	 * <b>THIS METHOD SHOULD BE FAVORED OVER</b> System.out.println to avoid
	 * having to scour through the code to determine where a log is being
	 * printed from.
	 * @param msg Message to log to the console.
	 * @param ref Method caller.
	 */
	public static void log(Object msg, Object ref) {
		System.out.println(ref + " :");
		System.out.println(msg);
	}
	
	/**
	 * Prints a given set of coordinates in console using the DebugUtils.log-
	 * method. <br />
	 * Example log:<br />
	 * (108, 254) <br /> <br />
	 * 
	 * See DebugUtils.log for further explanation.
	 * @param x X-coordinate to be printed.
	 * @param y Y-coordinate to be printed. 
	 * @param ref Method caller.
	 */
	public static void logCoordinates(Number x, Number y, Object ref) {
		log("(" + x + ", " + y + ")", ref);
	}
	
	/**
	 * Prints the coordinates of a given area in console using the
	 * DebugUtils.log-method. <br />
	 * Example log:<br />
	 * (100, 100) <-> (200, 200) <br /> <br />
	 * 
	 * @param x1 Starting X-coorindate of the area.
	 * @param y1 Starting Y-coordinate of the area.
	 * @param x2 Ending X-coordinate of the area.
	 * @param y2 Ending Y-coordinate of the area.
	 * @param ref Method caller.
	 */
	public static void logArea(Number x1, Number y1, Number x2, Number y2, Object ref) {
		log("(" + x1 + ", " + y1 + ") <-> (" + x2 + ", " + y2 + ")", ref);
	}
	
	/**
	 * Prints a given set of coordinate and dimensions in console using the
	 * DebugUtils.log-method. <br />
	 * Example log:<br />
	 * P = (108, 254) <br />
	 * D = (55, 55)<br /> <br />
	 * 
	 * See DebugUtils.log for further explanation.
	 * @param x X-coordinate to be printed.
	 * @param y Y-coordinate to be printed.
	 * @param width Width to be printed.
	 * @param height Height to be printed. 
	 * @param ref Method caller.
	 */
	public static void logPositionDimensions(Number x, Number y, Number width, Number height, Object ref) {
		log("P = (" + x + ", " + y + ")\nD = (" + width + ", " + height + ")", ref);
	}
}
