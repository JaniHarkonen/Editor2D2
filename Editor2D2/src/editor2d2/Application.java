package editor2d2;

import editor2d2.gui.Window;
import editor2d2.model.app.Controller;
import editor2d2.model.subservice.SubscriptionService;

public class Application {
	
		// Reference to the Subscription Service
	public static final SubscriptionService subscriptionService = SubscriptionService.instantiate();
	
		// Reference to the application Controller
	public static final Controller controller = Controller.instantiate();
	
		// Reference to the application master Window
	public static final Window window = Window.instantiate();
	
	
	public static void main(String[] args) { }
}
