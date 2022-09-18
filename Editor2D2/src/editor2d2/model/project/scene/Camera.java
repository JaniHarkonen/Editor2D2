package editor2d2.model.project.scene;

import java.awt.Graphics2D;

import editor2d2.DebugUtils;
import editor2d2.common.grid.Grid;
import editor2d2.model.project.Scene;
import editor2d2.model.project.layers.Layer;
import editor2d2.model.project.scene.placeables.Drawable;
import editor2d2.model.project.scene.placeables.Placeable;
import editor2d2.model.project.scene.placeables.RenderContext;

public class Camera {

		// X-coordinate of the camera in the scene
	private double x;

		// Y-coordinate of the camera in the scene
	private double y;
	
		// Z-coordinate of the camera in the scene
	private double z;
	
		// Width of the view port in pixels
	private int portWidth;
	
		// Height of the view port in pixels
	private int portHeight;
	
		// Reference to the Scene the camera is embedded in
	private Scene scene;
	
	
	public Camera() {
		this.x = 0;
		this.y = 0;
		this.z = 1;
		this.portWidth = 0;
		this.portHeight = 0;
		this.scene = null;
	}
	
	
		// Renders the area in the map visible to the camera
	public void render(Graphics2D gg) {
		RenderContext rctxt = new RenderContext(gg, this);
		
		for( Layer<? extends Placeable> lay : this.scene.getLayers() )
		{
				// Skip invisible layers and layers with 0 opacity
			if( !lay.checkVisible() || lay.getOpacity() < 1 )
			continue;
			
			Grid grid = lay.getObjectGrid();
			
				// Grid dimensions
			int cw = grid.getCellWidth(),
				ch = grid.getCellHeight(),
				gw = grid.getRowLength(),
				gh = grid.getColumnLength();
				
			CameraBounds bounds = getBounds();
			
				// Area visible to the Camera
			int start_x = Math.max(0 , (int) bounds.left / cw),
				start_y = Math.max(0 , (int) bounds.top / ch),
				end_x	= Math.min(gw, (int) bounds.right / cw),
				end_y 	= Math.min(gh, (int) bounds.bottom / ch);
			
				// Draw the visible area of the Scene
			for( int x = start_x; x < end_x; x++ )
			{
				for( int y = start_y; y < end_y; y++ )
				{
					Drawable place = (Drawable) grid.get(x, y);
					
					if( place == null )
					continue;
					
					place.draw(rctxt);
				}
			}
		}
	}
	
	/**
	 * Takes an X-coordinate and calculates its on-screen X-coordinate.
	 * @param x X-coordinate whose on-screen coordinate to calculate.
	 * @return On-screen X-coordinate.
	 */
	public double getOnScreenX(double x) {
		return (x - this.x) * this.z + this.portWidth / 2;
	}
	
	/**
	 * Takes an Y-coordinate and calculates its on-screen Y-coordinate.
	 * @param y Y-coordinate whose on-screen coordinate to calculate.
	 * @return On-screen Y-coordinate.
	 */
	public double getOnScreenY(double y) {
		return (y - this.y) * this.z + this.portHeight / 2;
	}
	
	/**
	 * Takes an on-screen X-coordinate and calculates the X-coordinate
	 * of the point that corresponds to it in the game world.
	 * @param x On-screen X-coordinate whose coordinate to calculate. 
	 * @return In-world X-coordinate.
	 */
	public double getInSceneX(double x) {
		return this.x + (x - this.portWidth / 2) / this.z;
	}
	
	/**
	 * Takes an on-screen Y-coordinate and calculates the Y-coordinate
	 * of the point that corresponds to it in the game world.
	 * @param y On-screen Y-coordinate whose coordinate to calculate. 
	 * @return In-world Y-coordinate.
	 */
	public double getInSceneY(double y) {
		return this.y + (y - this.portHeight / 2) / this.z;
	}
	
	
		// Returns the bounds of the view of the Camera
	public CameraBounds getBounds() {
		double 	wh = this.portWidth / 2 * this.z,
				hh = this.portHeight / 2 * this.z;
		
		return new CameraBounds(this.x - wh, this.y - hh, this.x + wh, this.y + hh);
	}
	
		// Returns the X-coordinate of the camera
	public double getX() {
		return this.x;
	}
	
		// Returns the Y-coordinate of the camera
	public double getY() {
		return this.y;
	}
	
		// Returns the Z-coordinate (height) of the camera
	public double getZ() {
		return this.z;
	}
	
		// Returns the view port width of the camera
	public int getPortWidth() {
		return this.portWidth;
	}
	
		// Returns the view port height of the camera
	public int getPortHeight() {
		return this.portHeight;
	}
	
		// Returns a reference to the Scene that the camera is embedded in
	public Scene getScene() {
		return this.scene;
	}
	
		// Sets the position of the camera
	public void setPosition(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
		// Shifts the position of the camera
	public void shift(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
		// Sets the view port dimensions of the camera
	public void setPortDimensions(int portWidth, int portHeight) {
		this.portWidth = portWidth;
		this.portHeight = portHeight;
	}
	
		// Sets the Scene that this camera is embedded in
	public void setScene(Scene scene) {
		this.scene = scene;
	}
}
