package editor2d2.gui;

/**
 * This is a container class for all the handles used by the
 * SubscriptionService of the Window. Subscription handles
 * should be listed here as public static final fields and
 * later through this class to avoid confusion.<br /><br />
 *
 * THIS CLASS IS NOT TO BE INSTANTIATED.
 * @author User
 *
 */
public final class Handles {

	public static final String ASSET_PANE = "asset-pane";
	public static final String MODAL = "modal";
	public static final String SCENE_VIEW = "scene-view";
	public static final String CURSOR_GRID_SETTINGS_CHANGED = "cursor-grid-settings-changed";
	public static final String CURSOR_GRID_TOGGLED = "cursor-grid-toggled";
	public static final String LAYER_GRID_TOGGLED = "layer-grid-toggled";
	
		// Do not instantiate
	private Handles() { }
}
