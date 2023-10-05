package editor2d2.gui.body.scenectrl;

import editor2d2.Application;
import editor2d2.gui.components.requirements.Require;
import editor2d2.gui.components.requirements.RequireIntegerBetween;
import editor2d2.subservice.Vendor;

/**
 * This is a specific RequirementFilter used only by the 
 * SceneControlsPanel. It extends the RequireIntegerBetween-
 * RequirementFilter as the input has to be an integer, 
 * however, it only needs to have a minimum value. Each time 
 * the input is successfully validated, a given Vendor (the 
 * SceneControlsPanel) is registered with the specified 
 * handle. 
 * <br/><br/>
 * 
 * The SceneControlsPanel uses a RequireIntegerAndNotify to 
 * determine whether the inputs to its cursor grid cell 
 * dimensions is valid (integer greater than or equal to 0).
 * <br/><br/>
 * 
 * See the SceneControlsPanel for more information on 
 * Scene controls and how this RequirementFilters is used.
 * 
 * @author User
 *
 */
public class RequireIntegerAndNotify extends RequireIntegerBetween {

	/**
	 * The Subscription handle that will be notified each 
	 * time the input is successfully validated.
	 */
	private String notifyHandle;
	
	/**
	 * The Vendor (SceneControlPanel) that will be used to 
	 * register the Subscription handle.
	 */
	private Vendor vendor;
	
	/**
	 * Constructs a RequireIntegerAndNotify instance that 
	 * forces the input to be an integer greater than a 
	 * given minimum value. Each time the input changes 
	 * it is validated and, if valid, a given Vendor is 
	 * registered with a given handle.
	 * 
	 * @param min Minimum value accepted by the 
	 * RequirementFilter.
	 * @param notifyHandle Subscription handle that the 
	 * Vendor will be registered to and updated when 
	 * the input changes to a valid one.
	 * @param vendor Reference to the Vendor 
	 * (SceneControlsPane) that is to be registered each 
	 * time the input changes to a valid one.
	 */
	public RequireIntegerAndNotify(int min, String notifyHandle, Vendor vendor) {
		super(Require.MIN_ONLY, min);
		
		this.notifyHandle = notifyHandle;
		this.vendor = vendor;
	}
	
	
	@Override
	protected boolean validateInput() {
		boolean isValid = super.validateInput();
		
		if( isValid )
		Application.window.subscriptionService.register(this.notifyHandle, this.vendor);
		
		return isValid;
	}
}
