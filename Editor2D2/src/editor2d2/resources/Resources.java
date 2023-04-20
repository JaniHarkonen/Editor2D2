package editor2d2.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * This class will be used to load and reference the assets native to
 * Editor2D as well as other external assets. This class is a singleton.
 * 
 * @author User
 *
 */
public final class Resources {
	
	/**
	 * Whether the class has been instantiated.
	 */
	private static boolean isInstantiated;
	
	/**
	 * Mapping of names of graphics to the BufferedImages representing them.
	 */
	private final Map<String, BufferedImage> graphics;
	
	
		// Do not instantiate
	private Resources() {
		isInstantiated = true;
		this.graphics = new HashMap<String, BufferedImage>();
		
			// Loads the resources
		String path = (Resources.class.getResource("")).getPath();
		
				// Placeholder graphics
			loadGraphic("test-grass", path + "testing\\gr_grass.png");
			loadGraphic("test-dirt"	, path + "testing\\gr_dirt.png");
			
				// Asset icons
			loadGraphic("icon-asset-folder", path + "assets\\icon_folder.png");
			loadGraphic("icon-asset-object", path + "assets\\icon_object.png");
			loadGraphic("icon-asset-object-blank", path + "assets\\icon_object_blank.png");
			
				// Tool icons
			loadGraphic("icon-tool-select", path + "assets\\icon_tool_select.png");
			loadGraphic("icon-tool-move", path + "assets\\icon_tool_move.png");
			loadGraphic("icon-tool-place", path + "assets\\icon_tool_place.png");
			loadGraphic("icon-tool-fill", path + "assets\\icon_tool_fill.png");
			loadGraphic("icon-tool-rotate", path + "assets\\icon_tool_rotate.png");
			//loadGraphic("icon-tool-scale", path + "assets\\icon_tool_scale.png");
			
				// Scene controls pane icons
			loadGraphic("icon-resize-scene", path + "assets\\icon_resize_scene.png");
			loadGraphic("icon-toggle-cursor-grid", path + "assets\\icon_toggle_cursor_grid.png");
			loadGraphic("icon-toggle-layer-grid", path + "assets\\icon_toggle_layer_grid.png");
			loadGraphic("icon-return-to-origin", path + "assets\\icon_return_origin.png");
			
				// Layer pane controls
			loadGraphic("icon-toggle-layer-visibility-on", path + "assets\\icon_toggle_layer_visible_on.png");
			loadGraphic("icon-toggle-layer-visibility-off", path + "assets\\icon_toggle_layer_visible_off.png");
			
				// Null icons
			loadGraphic("icon-null-texture", path + "assets\\icon_null_texture.png");
			loadGraphic("icon-null-object", path + "assets\\icon_null_object.png");
	}
	
	
	/**
	 * Instantiates the Resources and returns a reference to the instance
	 * given that the class has not been instantiated yet.
	 * 
	 * @return A reference to the singleton instance.
	 */
	public static Resources instantiate() {
		if( isInstantiated == true )
		return null;
		
		return new Resources();
	}
	
	
	/**
	 * Loads an external graphic from a given file and returns
	 * a reference to the Buffered Image representing it.
	 * 
	 * @param path Path of the image file to load from.
	 * 
	 * @return BufferedImage of the loaded image or NULL if the
	 * loading failed.
	 */
	public BufferedImage loadExternalGraphic(String path) {
		if( path == null )
		return null;
		
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(new File(path));
		}
		catch( IOException e ) { }
		
		return img;
	}
	
	/**
	 * Loads an external graphic from a given file and returns
	 * a reference to the Buffered Image representing it.
	 * 
	 * @param file File object of the image file to load from.
	 * 
	 * @return BufferedImage of the loaded image or NULL if the
	 * loading failed.
	 */
	public BufferedImage loadExternalGraphic(File file) {
		if( file == null )
		return null;
		
		return loadExternalGraphic(file.getPath());
	}
	
	/**
	 * Returns a reference to the graphic of a given name.
	 * 
	 * @param name Name of the graphic whose reference to return.
	 * 
	 * @return The reference to the graphic.
	 */
	public BufferedImage getGraphic(String name) {
		return this.graphics.get(name);
	}
	
	
	/**
	 * Loads a graphic from a given file path and puts it in the
	 * graphics map under a given name.
	 * 
	 * @param name Name of the graphics resource.
	 * 
	 * @param fname Path to the file containing the iamge of the
	 * graphic.
	 */
	private void loadGraphic(String name, String fname) {
		BufferedImage img = loadExternalGraphic(fname);
		
		if( img != null )
		this.graphics.put(name, img);
	}
}
