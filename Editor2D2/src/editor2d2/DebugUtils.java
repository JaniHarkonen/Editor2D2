package editor2d2;

import java.awt.image.BufferedImage;

import editor2d2.model.app.Controller;
import editor2d2.model.project.Project;
import editor2d2.model.project.Scene;
import editor2d2.model.project.assets.Image;
import editor2d2.model.project.layers.TileLayer;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.placeables.Tile;
import editor2d2.model.subservice.Subscriber;
import editor2d2.model.subservice.Subscription;

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
				cam1.setPortDimensions(320, 160);
				cam1.shift(0, 0, 0);
			
			scene1.setCamera(cam1);
			
					// Layer
				TileLayer tl1 = new TileLayer(scene1, 32);
				
					// Tiles
				int d = 0;
				for( int x = 0; x < 6; x++ )
				{
					for( int y = 0; y < 6; y++ )
					{
						Tile t1 = new Tile();
						t1.place(tl1, x, y);
						d++;
					}
				}
				
				DebugUtils.log(""+d, new DebugUtils());
				
			scene1.addLayer(tl1);
			
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
		dummy.addAsset(img);
		
		img = new Image();
		img.setName("dirt");
		img.setImage(Application.resources.getGraphic("test-dirt"));
		dummy.addAsset(img);
		
		controller.openProject(dummy);
	}
	
	/**
	 * Prints a subscription information of a given handle including: <br/>
	 * - handle ID<br/>
	 * - vendor reference<br/>
	 * - list of subscribers
	 * 
	 * @param handle Handle whose information to print.
	 */
	public static void printSubscription(String handle) {
		Subscription subsc = Application.subscriptionService.getSubscription(handle);
		
		if( subsc == null )
		{
			System.out.println("Handle " + handle + " is null!");
			return;
		}
		
		System.out.println("Handle: " + handle);
		System.out.println("Vendor: " + subsc.getVendor());
		System.out.println("Subscribers: ");
		
		for( Subscriber sub : subsc.getSubscribers() )
		System.out.println("\t" + sub);
	}
	
	/**
	 * Prints a message in console using System.out.println containing
	 * a reference to the caller.
	 * 
	 * THIS METHOD SHOULD BE FAVORED OVER System.out.println to avoid having
	 * to scour through the code to determine where a log is being printed
	 * from.
	 * @param msg Message to log to the console.
	 * @param ref Method caller.
	 */
	public static void log(String msg, Object ref) {
		System.out.println(ref);
		System.out.println(msg);
	}
}
