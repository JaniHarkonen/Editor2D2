package editor2d2.model.project.scene;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;

import editor2d2.common.Bounds;
import editor2d2.common.grid.Grid;
import editor2d2.model.project.scene.placeable.Drawable;
import editor2d2.model.project.scene.placeable.RenderContext;
import editor2d2.modules.object.layer.ObjectArray;

/**
 * Each Scene will have a Camera through which the Scene is 
 * viewed through. The Camera functions as the renderer for 
 * the Scene and everything visible in the Camera's view 
 * will be rendered in the editor. Each Scene MUST have a 
 * Camera instance.
 * 
 * @author User
 *
 */
public class Camera {

	/**
	 * World X-coordinate of the Camera in the Scene.
	 */
	private double x;

	/**
	 * World Y-coordinate of the Camera in the Scene.
	 */
	private double y;
	
	/**
	 * World Z-coordinate (height) of the Camera in the 
	 * Scene.
	 */
	private double z;
	 
	/**
	 * Width of the world area that will be rendered by 
	 * the Camera. 
	 */
	private int portWidth;
	
	/**
	 * Height of the world area that will be rendered by 
	 * the Camera.
	 */
	private int portHeight;
	
	/**
	 * Reference to the Scene that the Camera is embedded 
	 * in.
	 */
	private Scene scene;
	
	/**
	 * Constructs a Camera instance with the default 
	 * settings, including zeroed out dimensions and 
	 * coordinates. The Scene of the Camera will also be 
	 * NULL by default.
	 */
	public Camera() {
		this.x = 0;
		this.y = 0;
		this.z = 1;
		this.portWidth = 0;
		this.portHeight = 0;
		this.scene = null;
	}

	/**
	 * Renders the view port of the Camera into a given
	 * Graphics2D context. The Camera will iterate through 
	 * each visible cell of each Layer in the Scene that 
	 * the Camera is embedded in. Because each Layer 
	 * consists of a Grid, determining the cellular bounds 
	 * of the view port is easy and the rendering is quick 
	 * as well.
	 * 
	 * @param gg Reference to the Graphics2D context object 
	 * the Scene will be rendered in.
	 */
	public void render(Graphics2D gg) {
		long currentTime = System.nanoTime();
		RenderContext rctxt = new RenderContext(gg, this);
		Composite prevComp = rctxt.gg.getComposite();
		
		int renderCount = 0;
		
		for( Layer lay : this.scene.getLayers() )
		{
				// Skip invisible layers and layers with 0 opacity
			if( !lay.checkVisible() || lay.getOpacity() < 0.001 )
			continue;
			
				// Set the opacity for the Graphics2D-context
			rctxt.gg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) lay.getOpacity()));
			
			Grid grid = lay.getObjectGrid();
			Bounds bounds = getBounds();
			
				// Grid dimensions
			int cw = grid.getCellWidth(),
				ch = grid.getCellHeight(),
				gw = grid.getRowLength(),
				gh = grid.getColumnLength();
			
				// Area visible to the Camera
			int start_x = Math.max(0 , (int) bounds.left / cw - 1),
				start_y = Math.max(0 , (int) bounds.top / ch - 1),
				end_x	= Math.min(gw, (int) bounds.right / cw + 1),
				end_y 	= Math.min(gh, (int) bounds.bottom / ch + 1);
			
				// Draw the visible area of the Scene
			for( int x = start_x; x < end_x; x++ )
			{
				for( int y = start_y; y < end_y; y++ )
				{
					Drawable drawable = (Drawable) grid.get(x, y);
					
					if( drawable == null )
					continue;
					
					drawable.draw(rctxt);
					
						// DEBUG - Counts the number of rendered objects
					if( drawable instanceof ObjectArray )
					renderCount += ((ObjectArray) drawable).objects.size();
					else
					renderCount++;
				}
			}
		}
		
			// Reset Graphics2D composite
		rctxt.gg.setComposite(prevComp);
		currentTime = System.nanoTime() - currentTime;
		rctxt.gg.drawString("Placeables rendered: " + renderCount, 0, 100);
		rctxt.gg.drawString("Rendering time: " + (currentTime / 1000000) + " ms (" + currentTime + " ns)", 0, 116);
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
	
	/**
	 * Returns a Bounds object containing the bounds of the view port 
	 * of the Camera in the world.
	 * 
	 * @return Reference to the Bounds object representing the bounds 
	 * of the Camera.
	 */
	public Bounds getBounds() {
		double 	wh = this.portWidth / 2 / this.z,
				hh = this.portHeight / 2 / this.z;
		
		return new Bounds(this.x - wh, this.y - hh, this.x + wh, this.y + hh);
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns the in world X-coordinate of the Camera.
	 * 
	 * @return Returns the Camera X-coordinate.
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Returns the in world Y-coordinate of the Camera.
	 * 
	 * @return Returns the Camera Y-coordinate.
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Returns the in world Z-coordinate of the Camera.
	 * 
	 * @return Returns the Camera Z-coordinate.
	 */
	public double getZ() {
		return this.z;
	}
	
	/**
	 * Returns the width of the area that is visible to 
	 * the Camera.
	 * 
	 * @return Returns the in-world width of the view 
	 * port of the Camera.
	 */
	public int getPortWidth() {
		return this.portWidth;
	}
	
	/**
	 * Returns the height of the area that is visible to 
	 * the Camera.
	 * 
	 * @return Returns the in-world height of the view 
	 * port of the Camera.
	 */
	public int getPortHeight() {
		return this.portHeight;
	}
	
	/**
	 * Returns a reference to the Scene that the Camera is 
	 * embedded in.
	 * 
	 * @return Reference to the Scene that the Camera is 
	 * in.
	 */
	public Scene getScene() {
		return this.scene;
	}
	
	/**
	 * Sets the position of the Camera inside the Scene.
	 * 
	 * @param x The new X-coordinate of the Camera.
	 * @param y The new Y-coordinate of the Camera.
	 * @param z The new Z-coordinate of the Camera.
	 */
	public void setPosition(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Shifts the position of the Camera by incrementing 
	 * each coordinate by a given amount. This method 
	 * avoids having to call a getter and a setter in 
	 * order to shift the Camera.
	 * 
	 * @param x Amount by which to shift the Camera on 
	 * the X-axis.
	 * @param y Amount by which to shift the Camera on 
	 * the Y-axis.
	 * @param z Amount by which to shift the Camera on 
	 * the Z-axis.
	 */
	public void shift(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	/**
	 * Sets the width and the height of the view port 
	 * that is visible to the Camera.
	 * 
	 * @param portWidth The new width of the view port.
	 * @param portHeight The new height of the view 
	 * port.
	 */
	public void setPortDimensions(int portWidth, int portHeight) {
		this.portWidth = portWidth;
		this.portHeight = portHeight;
	}
	
	/**
	 * Sets the Scene that the Camera is embedded in.
	 * 
	 * @param scene Reference to the Scene that the 
	 * Camera is to be embedded in.
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}
}
