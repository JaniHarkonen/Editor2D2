package editor2d2.gui;

/**
 * This is a container class for all the handles used by the
 * SubscriptionService of the Window. Subscription handles
 * should be listed here as public static final fields and
 * later through this class to avoid confusion.<br /><br />
 *
 * THIS CLASS IS NOT TO BE INSTANTIATED.
 * 
 * @author User
 *
 */
public final class Handles {

	/**
	 * Handle of AssetPane.
	 */
	public static final String ASSET_PANE = "asset-pane";
	
	/**
	 * Handle of ModalWindow.
	 */
	public static final String MODAL = "modal";
	
	/**
	 * Handle of SceneView.
	 */
	public static final String SCENE_VIEW = "scene-view";
	
	/**
	 * Handle of SceneControlsPane. Called when cursor grid 
	 * settings are changed.
	 */
	public static final String CURSOR_GRID_SETTINGS_CHANGED = "cursor-grid-settings-changed";
	
	/**
	 * Handle of SceneControlsPane. Called when cursor grid 
	 * visibility is toggled on or off.
	 */
	public static final String CURSOR_GRID_TOGGLED = "cursor-grid-toggled";
	
	/**
	 * Handle of SceneControlsPane. Called when layer's own 
	 * grid visibility is toggled on or off.
	 */
	public static final String LAYER_GRID_TOGGLED = "layer-grid-toggled";
	
	/**
	 * Handle of SceneControlsPane. Called when the camera 
	 * is returned back to (0, 0).
	 */
	public static final String CAMERA_RETURNED_TO_ORIGIN = "camera-returned-to-origin";
	
	/**
	 * Handle of Root. Called when the horizontal split in 
	 * the application is adjusted.
	 */
	public static final String HORIZONTAL_SPLIT_ADJUSTED = "horizontal-split-adjusted";
	
	/**
	 * Handle of Root. Called when the vertical split in 
	 * the application is adjusted.
	 */
	public static final String VERTICAL_SPLIT_ADJUSTED = "vertical-split-adjusted";
	
		// Do not instantiate
	private Handles() { }
}
