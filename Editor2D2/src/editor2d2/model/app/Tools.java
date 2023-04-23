package editor2d2.model.app;

import editor2d2.model.app.tool.TFill;
import editor2d2.model.app.tool.TMove;
import editor2d2.model.app.tool.TPlace;
import editor2d2.model.app.tool.TRotate;
import editor2d2.model.app.tool.TScale;
import editor2d2.model.app.tool.TSelect;
import editor2d2.model.app.tool.Tool;

/**
 * This class should list all the Tool instances 
 * that are to be used in the application. The 
 * application components can reference this class 
 * to get the list of all the available Tools. 
 * The Tools will be available in the editor's 
 * toolbar.
 * <br/><br/>
 * 
 * If the developer wishes to add more Tools to 
 * the toolbar, they must be instantiated in 
 * the availableTools-field.
 * 
 * This class is a singleton and should not be 
 * instantiated.
 * <br/><br/>
 * 
 * See Tool for more information on tools.
 * <br/><br/>
 * 
 * See Toolbar for more information on the editor 
 * toolbar.
 * 
 * @author User
 *
 */
public final class Tools {

	/**
	 * Array of the instances of all the available 
	 * Tools.
	 */
	private static final Tool[] availableTools = {
			new TSelect(),
			new TMove(),
			new TPlace(),
			new TFill(),
			new TRotate(),
			new TScale(),
	};
	
	
		// Do not instantiate
	private Tools() { }
	
	
	/**
	 * Returns an array of instances of all the 
	 * Tools available in the application.
	 * 
	 * @return Array of the instances of all the 
	 * available Tools.
	 */
	public static Tool[] getAvailableTools() {
		return availableTools;
	}
}
