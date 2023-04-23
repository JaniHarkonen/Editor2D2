package editor2d2.model;

/**
 * This is a container class for all the handles used by the
 * SubscriptionService of the Controller. Subscription handles
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
	 * Handle of Controller. Called when the currently 
	 * selected Placeable changes.
	 */
	public static final String SELECTED_PLACEABLE = "selected-placeable";
	
	/**
	 * Handle of Controller. Called when the currently active 
	 * Project changes.
	 */
	public static final String ACTIVE_PROJECT = "active-project";
	
	/**
	 * Handle of Controller. Called when the Folder that is 
	 * currently open and visible in the AssetPane changes.
	 */
	public static final String OPEN_FOLDER = "open-folder";
	
	/**
	 * Handle of Controller. Called when the currently active 
	 * Scene changes.
	 */
	public static final String ACTIVE_SCENE = "active-scene";
	
	
	public static final String LAYER_VISIBILITY = "layer-visibility";
	
	/**
	 * Handle of Controller. Called when the currently active 
	 * Layer is deleted.
	 */
	public static final String LAYER_DELETED = "layer-deleted";
	
	
	public static final String LAYER_REORDER = "layer-reorder";
	
	/**
	 * Handle of Controller. Called when the currently 
	 * selected Tool changes.
	 */
	public static final String SELECTED_TOOL = "selected-tool";
	
	
		// Do not instantiate
	private Handles() { }
	
	/**
	 * Utility method that determines if two handles are the 
	 * same without the need for the developer to call the 
	 * equals-method themselves.
	 * 
	 * @param handle1 First handle to be compared.
	 * @param handle2 Second handle to be compared.
	 * 
	 * @return Returns whether the first and the second 
	 * handle is the same one.
	 */
	public static boolean handleEquals(String handle1, String handle2) {
		return handle1.equals(handle2);
	}
}
