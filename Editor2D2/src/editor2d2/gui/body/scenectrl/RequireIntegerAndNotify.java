package editor2d2.gui.body.scenectrl;

import editor2d2.Application;
import editor2d2.gui.components.requirements.Require;
import editor2d2.gui.components.requirements.RequireIntegerBetween;
import editor2d2.subservice.Vendor;

public class RequireIntegerAndNotify extends RequireIntegerBetween {

	private String notifyHandle;
	
	private Vendor vendor;
	
	
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
