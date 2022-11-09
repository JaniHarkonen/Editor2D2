package editor2d2.model;

public final class Handles {

	public static final String SELECTED_PLACEABLE = "selected-placeable";
	public static final String ACTIVE_PROJECT = "active-project";
	public static final String OPEN_FOLDER = "open-folder";
	public static final String ACTIVE_SCENE = "active-scene";
	public static final String LAYER_VISIBILITY = "layer-visibility";
	public static final String LAYER_DELETED = "layer-deleted";
	public static final String LAYER_REORDER = "layer-reorder";
	
	
		// Do not instantiate
	private Handles() { }
	
	
	public static boolean handleEquals(String handle1, String handle2) {
		return handle1.equals(handle2);
	}
}
